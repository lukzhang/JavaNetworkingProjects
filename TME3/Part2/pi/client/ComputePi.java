/*
I declare that this assignment is my own work and that all material previously 
written or published in any source by any other person has been duly acknowledged 
in the assignment. I have not submitted this work, or a significant part thereof, 
previously as part of any academic program. In submitting this assignment I give 
permission to copy it for assessment purposes only.

Author: Luke Zhang
Student Number: 3330978
Assignment 3, Question 2
May 22, 2018

DESIGN:
This takes the original Pi RMI example and modifies the Pi and ComputePi classes.
The Pi is altered so that it takes two int arguments in the constructor, which
can be used in the 'computePi' method which now computes the largest integer
within the range. The ComputePi class is altered in the Main where the user 
requiers to input 2 arguments. The corresponding batch file 'runclient.bat' is
edited so that the user must input 2 arguments (rather than 1 as it was previously).
The Main also checks if the returned value is less or equal to 1, in which case
that means no prime number was found within the range.



TEST PLAN

1. Run the application: 
In Command Prompt, go to directory of this program. Make sure to seth path to
java files (i.e.: set path=%path%; "My path")
Build and run the server program: runserver.bat
Build and run the client program with two numbers: runclient.bat 5 20

EXPECTED:
    Prints the largest prime number in range, 19
ACTUAL:
    Works as expected 

2. Input the range as two negative number (i.e. runclient.bat -10, -4)

EXPECTED:
  Prints that no prime number was found
ACTUAL:
  As expected

3. Reverse the number range where the second number is less than first 
(i.e.runclient.bat 20 5)

EXPECTED:
  Gives result identical to Run 1
ACTUAL:
  As expected

4. Bad Run. Enter only 1 argument (i.e. runclient.bat 3)

EXPECTED:
  ArrayIndexOutOfBounds Exception
ACTUAL:
  As expected


5. Bad Run. Enter argument other than int (i.e. runclient.bat three twenty)

EXPECTED:
  NumberFormatException
ACTUAL:
  As expected
  


REFERENCES:
Base code was taken from The Landing for COMP348, Athabasca University
https://landing.athabascau.ca/file/view/747375/rmi-example

*/


package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.math.BigDecimal;
import compute.Compute;

public class ComputePi {
    public static void main(String args[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "Compute";
            Registry registry = LocateRegistry.getRegistry(args[0]);
            Compute comp = (Compute) registry.lookup(name);

            //The original 'Pi' class takes two arguments to use as a range to 
            //find the largest prime number within
            Pi task = new Pi(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            BigDecimal pi = comp.executeTask(task);

            
            //If there was no prime number found, the prime number returned is 
            //the default 0 (which is not a prime number) and should tell the user
            //that there was no prime number in range. Same goes if there is a
            //negative value as the user entered a range starting with a negative
            //number. 
            //Essentially, if the "prime number" is less or equal to 1,
            //no prime number was found within the range
            if(pi.intValueExact() <= 1){
                System.out.println("There was no prime number in range");
            }
            else{
                System.out.println("The largest prime number is: " + pi);
            }
            

        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
    }    
}
