/*
I declare that this assignment is my own work and that all material previously 
written or published in any source by any other person has been duly acknowledged 
in the assignment. I have not submitted this work, or a significant part thereof, 
previously as part of any academic program. In submitting this assignment I give 
permission to copy it for assessment purposes only.

Author: Luke Zhang
Student Number: 3330978
Assignment 1, Question 1
May 9, 2018

DESIGN:
This reads a web logfile and records the stats of each remote host and their
respective accesses, bytes recieved, as well as total bytes transferred. As each
line is read in the logfile, the address in the first part of the string is kept
in an arraylist. Its corresponding number of requests and bytes received are kept
in 2 other arraylists with the same index. The user can select one of 3 options to
print the stats. Option 1 prints each remote hosts' number of accesses, Option 2
gets the total bytes transmitted, Option 3 gets total bytes for each remote host.
For Options 1 and 3, each arraylist is iterated and printed for display. Option 2
simply has the incremnted number of bytes printed.
Note, that this program makes use of Harold's LookupTask class




TEST PLAN

To setup:
In Command Prompt, go to directory of this program. Make sure to seth path to
java files (i.e.: set path=%path%; "My path")
Tell system where to find JDK programs: javac PooledWeblog.java
Run program with 2 args; first is the path to the logfile, second is the 
option (1, 2, or 3).


1. Run the application with option 1: 
EXPECTED:
    Prints each line in the logfile. At the end shows each remote host and their
number of accesses
ACTUAL:
    As expected

2. Run the application with option 2: 
EXPECTED:
    Prints each line in the logfile. At the end shows the total bytes transmitted
ACTUAL:
    As expected

3. Run the application with option 3: 
EXPECTED:
    Prints each line in the logfile. At the end shows the bytes received from each
remote host
ACTUAL:
    As expected

4. Run the application with an int other than 1,2,or3
EXPECTED:
    Only prints the logfile's lines, and nothing after.
ACTUAL:
    As expected. After it is done reading the logfile, the stats are printed using 
an if statement.

5. Bad run: uses a file to read other than a proper web logfile
EXPECTED:
    String index out of range: -1
ACTUAL:
    As expected


  


REFERENCES:
Base code was taken from "Java Network Programming" by Elliotte Rusty Harold
from Chapter 4. 

*/

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

// Requires Java 7 for try-with-resources and multi-catch
public class PooledWeblog {

  private final static int NUM_THREADS = 4;
  
  public static void main(String[] args) throws IOException {

    ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
    Queue<LogEntry> results = new LinkedList<LogEntry>();

    //Testing for the 2nd argument, that lets the user choose which
    //option to use for stat
    int someAnswer = Integer.parseInt(args[1]);
    
    //Total number of bytes transmitted
    int totalBytesTransmitted = 0;

    //Each remote host that has visited
     ArrayList<String> remoteHosts=new ArrayList<String>(); 
     //Number of accesses by each remote host
     ArrayList<String> accessesByEachHost = new ArrayList<String>();
     //Number of bytes by each remote host
     ArrayList<String> bytesByEachHost = new ArrayList<String>();



    try (BufferedReader in = new BufferedReader(      
      new InputStreamReader(new FileInputStream(args[0]), "UTF-8"));) {
      for (String entry = in.readLine(); entry != null; entry = in.readLine()) {
        LookupTask task = new LookupTask(entry);
        Future<String> future = executor.submit(task);
        LogEntry result = new LogEntry(entry, future);
        results.add(result);

        //*************STATS******************
          //Find the beginning of the string to obtain the address
          int index = entry.indexOf(' ');
          String address = entry.substring(0, index);

          //Find the end of the string to obtain the bytes transmitted
          String theBytes = entry.substring(entry.lastIndexOf(' ') + 1);
          int myBytes;

          //If last substring is a number, convert it to an int. Otherwise,
          //set it to 0
          try{
              myBytes = Integer.parseInt(theBytes);
            }
            catch(NumberFormatException nfe){
              myBytes=0;
            }

          //Tally total bytes transmitted
          totalBytesTransmitted += myBytes;

          //If host has already visited, then increment number of visits and add
          //bytes to the corresponding host
          if(remoteHosts.contains(address)){
                //Find current host
                int currIndex = remoteHosts.indexOf(address);
                //Increment number of visits
                String theNumber = accessesByEachHost.get(currIndex);
                int myNumber = Integer.parseInt(theNumber);
                myNumber++;
                theNumber = Integer.toString(myNumber);
                accessesByEachHost.set(currIndex, theNumber);
                //Tally the number of bytes
                String hostBytes = bytesByEachHost.get(currIndex);
                int convertBytes = Integer.parseInt(hostBytes);
                convertBytes += myBytes;
                hostBytes = Integer.toString(convertBytes);
                bytesByEachHost.set(currIndex, hostBytes);
          }
          //If host is new, add it to the arraylist along with number of visits and
          //total bytes
          else{
            remoteHosts.add(address);
              accessesByEachHost.add("1");
              bytesByEachHost.add("" + myBytes);
          }


      }
    }


    
    // Start printing the results. This blocks each time a result isn't ready.
    for (LogEntry result : results) {
      try {
        System.out.println(result.future.get());
      } catch (InterruptedException | ExecutionException ex) {
        System.out.println(result.original);
      }
    }

    executor.shutdown();

    //Options for stats to be printed
    //1. Get number of accesses by each remote host
    //2. Get total bytes transmitted
    //3. Get total bytes by each remote host
    if(someAnswer==1){
    System.out.println("***Number of accesses by each remote host***");
      for(int i=0; i<remoteHosts.size(); i++){
         System.out.println(remoteHosts.get(i) + ": " 
          + accessesByEachHost.get(i) + " accesses");
      }
    }
    else if(someAnswer==2){
      System.out.println("Total Bytes Transmitted: " + totalBytesTransmitted);
    }
    else if(someAnswer==3){
      System.out.println("***Total bytes by each remote host***");
      for(int i=0; i<remoteHosts.size(); i++){
         System.out.println(remoteHosts.get(i) + ": " 
          + accessesByEachHost.get(i) + " : " + bytesByEachHost.get(i) + 
          " bytes");
      }
    }
  }
  
  
  private static class LogEntry {
    String original;
    Future<String> future;
    
    LogEntry(String original, Future<String> future) {
     this.original = original;
     this.future = future;
    }
  }


}