/*
I declare that this assignment is my own work and that all material previously 
written or published in any source by any other person has been duly acknowledged 
in the assignment. I have not submitted this work, or a significant part thereof, 
previously as part of any academic program. In submitting this assignment I give 
permission to copy it for assessment purposes only.

Author: Luke Zhang
Student Number: 3330978
Assignment 4, Question 3
May 27, 2018

DESIGN:
This looks up an email by taking in the arguments for server, email, password and iterates
through each message in the email's inbox after establishing a connection. If the user also
enters an argument for email index number, the program will find that specific email and print
the contents. 



TEST PLAN

1. Run the application: 
In Command Prompt, go to directory of this program. Make sure to seth path to
java files (i.e.: set path=%path%; "My path")
Tell system where to find JDK programs and be sure to include the .jar files javax.mail.jar
and activaton.jar in the classpath: javac -cp javax.mail.jar;activation.jar; GetMail.java

Run the program while also including the .jar files in the classpath as well as entering a server, email, password
i.e.: java -cp javax.mail.jar;activation.jar; GetMail pop.gmail.com 729HomerSimpson@gmail.com AthabascaCOMP348!

EXPECTED:
    Prints each email's subject and sender
ACTUAL:
    Works as expected

2. Run the program, but also with a fourth argumnet to lookup email. 
i.e.: java -cp javax.mail.jar;activation.jar; GetMail pop.gmail.com 729HomerSimpson@gmail.com AthabascaCOMP348! 5

EXPECTED:
   Prints out the index number, subject, sender, as well as the contents of the email
ACTUAL:
  As expected

3. Bad Run: Use an email index greater than the number of messages
i.e.: java -cp javax.mail.jar;activation.jar; GetMail pop.gmail.com 729HomerSimpson@gmail.com AthabascaCOMP348! 500

EXPECTED:
  Does not print anyting (as the loop never reaches the index of interest)
ACTUAL:
  As expected

4. Bad Run: Enter wrong password

EXPECTED:
  Username and password not accepted
ACTUAL:
  As expected
  


REFERENCES:
Much of the code was inspired off of Tutorials Point JavaMail tutorials: 
    https://www.tutorialspoint.com/javamail_api/javamail_api_checking_emails.htm

*/

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

//Gets the user's email, and finds the messages in their inbox. Can either print each message's
//subject and sender, or print the contents of a specific email. 
public class GetMail {

   //Finds the email's inbox, and iterates through each message. If the 'contextIndex' is
   //non-zero, that means the user would like to look up that email based on that index.
   //Otherwise, just prints out each email in the inbox
   public static void check(String host, String storeType, String user,
      String password, int contextIndex) 
   {
      //Setup connection with user's email, and then iterate through each message in
      //inbox to either print to subject and sender, or contents of specific email
      try {

      //create properties field
      Properties properties = new Properties();

      properties.put("mail.pop3.host", host);
      properties.put("mail.pop3.port", "995");
      properties.put("mail.pop3.starttls.enable", "true");
      Session emailSession = Session.getDefaultInstance(properties);
  
      //create the POP3 store object and connect with the pop server
      Store store = emailSession.getStore("pop3s");

      store.connect(host, user, password);

      //create the folder object and open it
      Folder emailFolder = store.getFolder("INBOX");
      emailFolder.open(Folder.READ_ONLY);

      // retrieve the messages from the folder in an array and print it
      Message[] messages = emailFolder.getMessages();

      //Iterate through each message in the inbox. Either print each message or find
      //the one of interest and print its contents
      for (int i = 0, n = messages.length; i < n; i++) {
         Message message = messages[i];
         
         //Current index of email
         int index = i+1;

         //If 'contextIndex' is zero, that means the user has not entered an argument 
         //to look up a specific email. Instead, just print each email's subject and sender
         if(contextIndex == 0){
            
            System.out.println("" + index + ". " + message.getSubject() + "    ("
            + message.getFrom()[0] + ") ");
         }
         //Otherwise, find the email of interest, and print the contents
         else if(contextIndex == index){
            System.out.println("" + index + ". " + message.getSubject() + "    ("
            + message.getFrom()[0] + ") ");

            System.out.println((String) message.getContent());
            break;
         } 

      }

      //close the store and folder objects
      emailFolder.close(false);
      store.close();

      } catch (NoSuchProviderException e) {
         e.printStackTrace();
      } catch (MessagingException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void main(String[] args) {

      String host = args[0];// The email server. For gmail use: "pop.gmail.com"
      String mailStoreType = "pop3";
      String username = args[1];// Email of interest
      String password = args[2];// Email's password

      int getIndex = 0; //Checks which index of email to retrieve (if user decides to)

      //If user has inputed a value to look at email, convert argument to integer
      if(args.length == 4){
         getIndex = Integer.parseInt(args[3]);
      }

      //Runs the method to look at the email's inbox
      check(host, mailStoreType, username, password, getIndex);

   }

}