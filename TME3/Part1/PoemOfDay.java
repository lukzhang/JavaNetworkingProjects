/*
I declare that this assignment is my own work and that all material previously 
written or published in any source by any other person has been duly acknowledged 
in the assignment. I have not submitted this work, or a significant part thereof, 
previously as part of any academic program. In submitting this assignment I give 
permission to copy it for assessment purposes only.

Author: Luke Zhang
Student Number: 3330978
Assignment 3, Question 1 
May 22, 2018

DESIGN:
This creates a server that interacts with the user by sending messages and recieving 
input. The server looks at a .txt file to find a list of poems. The user can select
which poem is to be read, before the connection exits. If an invalid input is used, 
the client is told of the error and the connection exits anyway. 



TEST PLAN

1. Run the application: 
In Command Prompt, go to directory of this program. Make sure to seth path to
java files (i.e.: set path=%path%; "My path")
Tell system where to find JDK programs: javac PoemOfDay.java
Run the program to begin server with argument being the path to the .txt file for poems.
Once the server is running, open another Command prompt and connect using port 8080 via
'telnet localhost 8080'

Enter an index value for poem. (i.e. 2)

EXPECTED:
    Prints the second poem (and nothing more) and exits connection
ACTUAL:
    Works as expected

2. Ener the last value for poem (i.e. 6)

EXPECTED:
  Prints the 6th poem and exits connection
ACTUAL:
  As expected

3. Enter a value other than 1-6 (i.e. 'seven')

EXPECTED:
  Prints "Invalid Poem Number. Good-bye!" then exits
ACTUAL:
  As expected

4. Bad Run: Start server with incorrect path to file

EXPECTED:
  Prints each line in the file and shows the number of accesses by each host (myself)
ACTUAL:
  As expected

4. Run the logefile "theLogFile.txt" using option 2 with PooledWeblog
eg. java PooledWeblog "C:\Users\Luke\Desktop\COMP348\SampleHTML\theLogFile.txt" 2

EXPECTED:
  After connecting on client side via telnet and inputing a value, cannot find file
ACTUAL:
  As expected


  


REFERENCES:
Poems taken from the website: http://www.writersdigest.com/whats-new/poems-for-kids

*/

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

public class PoemOfDay{

	File poem;

	PoemOfDay(File poem){
		this.poem = poem;
	}

    //Server socket that runs on port 8080
	private ServerSocket middleman;
    private int port = 8080;
    //Client socket
    private Socket client;

    //This creates a server socket and prints messages to the client. The server waits for the client's input
    //after which a .txt file is read and the corresponding poem is sent to the client. If no valid input
    //is entered, the client is told that. The connection exits after.
	protected void createSocketServer()
    {
        try
        {
            while (true){
                middleman = new ServerSocket(port);
                client = middleman.accept();
                middleman.close();
                //PrintWriter out = new PrintWriter(client.getOutputStream(),true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(),  "UTF-8"));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in,  "UTF-8"));
                String line;

                //Client out stream for outgoing messages
                DataOutputStream dOut = new DataOutputStream(client.getOutputStream());

                //Greets the user and shows a list of poems
                String message = "Good day to you user. Here is a selection of poems" + "\n\r" + "\n\r";
				byte[] bytes = message.getBytes("UTF-8");
				client.getOutputStream().write(bytes);

                //There are 6 poems, each which are displayed to the user
				String[] poems = {"cupcake", "waiter", "growing", "rabbit", "sharks", "pond"};
					for(int i=0; i< poems.length; i++){
						int index = i+1;
						String curr = "" + index +". " + poems[i] + "\n\r";
						bytes = curr.getBytes("UTF-8");
						client.getOutputStream().write(bytes);
					}

                //Tells the user to press an index number to select poem
				message = "\n\r" + "Press index number to select" + "\n\r" + "\n\r";
				bytes = message.getBytes("UTF-8");
				client.getOutputStream().write(bytes);

                //Reads the .txt file to get the corresponding poem
                while((line = in.readLine()) != null)
                {                    

                    //Responses
                    //
                    //1.Cupcake
                    //2.Waiter
                    //3.Growing Up
                    //4.Rabbit
                    //5.Sharks
                    //6.Pond

                    if(line.equals("1")){

                    	String thePoem = getPoem(1).toString();
						bytes = thePoem.getBytes("UTF-8");
						client.getOutputStream().write(bytes);
						client.close();
                    }
                    else if(line.equals("2")){
                    	
                    	String thePoem = getPoem(2).toString();
						bytes = thePoem.getBytes("UTF-8");
						client.getOutputStream().write(bytes);
						client.close();
                    }
                    else if(line.equals("3")){
                    	
                    	String thePoem = getPoem(3).toString();
						bytes = thePoem.getBytes("UTF-8");
						client.getOutputStream().write(bytes);
						client.close();
                    }
                    else if(line.equals("4")){
                    	
                    	String thePoem = getPoem(4).toString();
						bytes = thePoem.getBytes("UTF-8");
						client.getOutputStream().write(bytes);
						client.close();
                    }
                    else if(line.equals("5")){
                    	
                    	String thePoem = getPoem(5).toString();
						bytes = thePoem.getBytes("UTF-8");
						client.getOutputStream().write(bytes);
						client.close();
                    }
                    else if(line.equals("6")){
                    	
                    	String thePoem = getPoem(6).toString();
						bytes = thePoem.getBytes("UTF-8");
						client.getOutputStream().write(bytes);
						client.close();
                    }
                    //Anything else means an invalid poem number
                    else{                    	
                    	message = "\n\r" + "Invalid Poem Number. Good-bye!" + "\n\r";
						bytes = message.getBytes("UTF-8");
						client.getOutputStream().write(bytes);
						client.close();
                    }
                }
            }
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }
    

    //Method that returns the poem text in String form. Primarily used so that the 
    //server can send the data to the client
    public String getPoem(int index){

        //Initializes the scanner to read .txt
    	Scanner scanner;
        //Check if the line in .txt should be appended to the String
    	boolean printPoem = false;
        //Index after index of interest, so that it will stop reading at the next poem
        int nextIndex = index +1;
        //Poem text to be returned
        String thePoem;
        StringBuffer stringBuffer = new StringBuffer();

        try {
            scanner = new Scanner(poem);

            //Checks the .txt for poems line by line
            while (scanner.hasNextLine()) {
        		String poemLine = scanner.nextLine();
       			
                //If the line contains the index followed by '.', then begin reading
        		if(poemLine.contains("" + index + ".")) {
            		printPoem = true;
            		stringBuffer.append("\n\r");
       			}
                //When line contains the next index (the next poem) followed by '.', stop reading
       			else if(poemLine.contains("" + nextIndex + ".")){
       				printPoem = false;
       			}

            //Append the line to the StringBuffer if appropriate
       		if(printPoem){
       			stringBuffer.append(poemLine);
       			stringBuffer.append("\n\r");

       		}
    	}
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Convert StringBufer to String for export
    	thePoem = stringBuffer.toString();
    	return thePoem;

    }



	public static void main(String[] args){
		
        //.txt file is found via Path to file as argument
		File poemFile = new File(args[0]);

        //Instance of class, and invoke the createSocketServer() method
		PoemOfDay test = new PoemOfDay(poemFile);
		test.createSocketServer();

	}

}