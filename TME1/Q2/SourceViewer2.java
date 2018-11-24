/*
I declare that this assignment is my own work and that all material previously 
written or published in any source by any other person has been duly acknowledged 
in the assignment. I have not submitted this work, or a significant part thereof, 
previously as part of any academic program. In submitting this assignment I give 
permission to copy it for assessment purposes only.

Author: Luke Zhang
Student Number: 3330978
Assignment 1, Question 2
May 9, 2018

DESIGN:
This creates a connection with a URL and reads the input stream. The program
takes 2 arguments. The first is the desired URL. The second is the string to
be searched. The stream appends each char into a string until a line break 
occurs. If the line contains the searched substring, then it prints. Otherwise,
it moves onto the next line.





TEST PLAN

1. Run the application: 
In Command Prompt, go to directory of this program. Make sure to seth path to
java files (i.e.: set path=%path%; "My path")
Tell system where to find JDK programs: javac SourceViewer2.java
Run program with 2 args; first is URL, second is substring to be searched
eg: java SourceViewer2 https://www.amazon.ca/ "deals"

EXPECTED:
    Prints lines that contains "deals" as input in the 2nd args
ACTUAL:
    Prints lines as expected

2. Do not input a second args to be searched for
eg: java SourceViewer2 https://www.amazon.ca/

EXPECTED:
  ArrayIndexOutOfBoundsException: 1
ACTUAL:
  As expected

3. Use an invalid URL
eg. java SourceViewer2 amazon.com "deals"

EXPECTED:
  amazon.com is not a parseable URL
ACTUAL:
  As expected


  


REFERENCES:
Base code was taken from "Java Network Programming" by Elliotte Rusty Harold
from Chapter 7. 

*/


import java.io.*;
import java.net.*;

public class SourceViewer2 {

  public static void main (String[] args) {
    if  (args.length > 0) {
      try {
        // Open the URLConnection for reading
        URL u = new URL(args[0]);
        URLConnection uc = u.openConnection();
        try (InputStream raw = uc.getInputStream()) { // autoclose
          InputStream buffer = new BufferedInputStream(raw);       
          // chain the InputStream to a Reader          
          Reader reader = new InputStreamReader(buffer);
          String findSubString = args[1];
          int c;

          //Each char gets placed into the stringbuilder so that 
          //each line may be built and checked if it contains a
          //substring
          StringBuilder sb = new StringBuilder();          

          while ((c = reader.read()) != -1) {
            if( c != '\r' && c != '\n'){
              sb.append((char) c);
            }
            else{
              String line = sb.toString();

              //Checks if each line contains the substring ignoring case
              if(line.toLowerCase().contains(findSubString.toLowerCase())){
                System.out.println(line);
              }

              sb.setLength(0);
            }
          }
          
          
        }
      } catch (MalformedURLException ex) {
        System.err.println(args[0] + " is not a parseable URL");
      } catch (IOException ex) {
        System.err.println(ex);
      }
    }
  }
}