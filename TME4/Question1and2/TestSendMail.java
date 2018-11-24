/*
I declare that this assignment is my own work and that all material previously 
written or published in any source by any other person has been duly acknowledged 
in the assignment. I have not submitted this work, or a significant part thereof, 
previously as part of any academic program. In submitting this assignment I give 
permission to copy it for assessment purposes only.

Author: Luke Zhang
Student Number: 3330978
Assignment 4, Question 1 & 2
May 27, 2018

DESIGN:
This sends an email by passing information from a .txt file (such as sender, password,
recipients, main body) into an email to be sent. Additionally, if the user adds a path to
a file, this file can be appended to the email. Each part (body text and file) are added 
in bodyparts, after which the entire message can be sent.



TEST PLAN

1. Run the application: 
In Command Prompt, go to directory of this program. Make sure to seth path to
java files (i.e.: set path=%path%; "My path")
Tell system where to find JDK programs and be sure to include the .jar files javax.mail.jar
and activaton.jar in the classpath: javac -cp javax.mail.jar;activation.jar; TestSendMail.java

Run the program while also including the .jar files in the classpath as well as the .txt file 
to be read. (i.e.: java -cp javax.mail.jar;activation.jar; TestSendMail "D:\COMP348stuff\thisFile.txt")

EXPECTED:
    Sends the email to each recipient with appropriate subject, body of text, to the right recipients
(to, cc, bcc)
ACTUAL:
    Works as expected

2. Send the email, but with an attachment as well. (i.e.: java -cp javax.mail.jar;activation.jar; TestSendMail 
"D:\COMP348stuff\thisFile.txt" "D:\COMP348stuff\husky.jpg")

EXPECTED:
   Does the same as Run 1, but also adds a .jpg file (i.e.: java -cp javax.mail.jar;activation.jar; TestSendMail 
"D:\COMP348stuff\thisFile.txt" "D:\COMP348stuff\homer.gif")
ACTUAL:
  As expected

3. Bad Run: Send the email, but with an invalid path for the attachment

EXPECTED:
  System could not find file
ACTUAL:
  As expected

4. Bad Run: Send email, but with invalid path for .txt file

EXPECTED:
  File not found exception
ACTUAL:
  As expected
  


REFERENCES:
Much of the code was inspired off of Tutorials Point JavaMail tutorials: 
    https://www.tutorialspoint.com/javamail_api/javamail_api_sending_emails.htm

*/

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Scanner;
import java.io.*;
import javax.activation.*;

//Sends an email (through gmail) by reading a .txt file in main(), and passing in the params of
//sender, recipients, body, subject, server, and potential attachment into the sendFromGmail() method
public class TestSendMail {

    public static void main(String[] args) {

        String from = null;     //Sender of email
        String pass = null;     //Sender's password
        String[] to = null;     //list of main recipients
        String[] cc = null;     //list of CC recipients
        String[] bcc = null;    //list of BCC recipients
        String subject = null;  //Subject of email
        String body = null;     //Main text of email
        String server = null;   //Server of email

        //File where email contents are
        File emailFile = new File(args[0]);

        //Filepath of attachment (if any)
        String filePath = "";
        //If the path to attachment is included, define the path
        if(args.length == 2){
            filePath = args[1];
        }
        

        //************************************************
        //***Reads the .txt file for email information****
        //************************************************
        Scanner scanner;
        StringBuffer stringBuffer = new StringBuffer(); //Used to append lines for the main body of email

        try {
            scanner = new Scanner(emailFile);
            boolean readBody = false;

            //Checks the .txt for poems line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                //If we are at last description (Body: ), then append each line to
                //the stringbuffer as main text of email
                if(readBody){
                    stringBuffer.append(line);
                    stringBuffer.append("\n\r");
                }
                
                //If the line STARTS with description followed by ':', then define
                //Split the line between the colon and space. Use 2nd part of array as
                //the defined variable
                if(line.startsWith("Server:")) {
                    String[] tokens = line.split(": ");
                    server = tokens[1];
                }
                else if(line.startsWith("User:")){
                    String[] tokens = line.split(": ");
                    String user = tokens[1];
                    from = user;
                }
                else if(line.startsWith("Password:")){
                    String[] tokens = line.split(": ");
                    String password = tokens[1];
                    pass = password;
                }
                else if(line.startsWith("To:")){
                    String[] tokens = line.split(": ");
                    String recipients = tokens[1];
                    String[] eachRecipient = recipients.split(",");
                    to = new String[eachRecipient.length];
                    for(int i=0; i<eachRecipient.length; i++){
                        to[i] = eachRecipient[i];
                    }
                }
                else if(line.startsWith("CC:")){
                    String[] tokens = line.split(": ");
                    String recipients = tokens[1];
                    String[] eachRecipient = recipients.split(",");
                    cc = new String[eachRecipient.length];
                    for(int i=0; i<eachRecipient.length; i++){
                        cc[i] = eachRecipient[i];
                    }
                }
                else if(line.startsWith("BCC:")){
                    String[] tokens = line.split(": ");
                    String recipients = tokens[1];
                    String[] eachRecipient = recipients.split(",");
                    bcc = new String[eachRecipient.length];
                    for(int i=0; i<eachRecipient.length; i++){
                        bcc[i] = eachRecipient[i];
                    }
                }
                else if(line.startsWith("Subject:")){
                    String[] tokens = line.split(": ");
                    subject = tokens[1];
                }
                //Body is the last part of .txt file, so allow append of txt to string buffer until
                //end of file
                else if(line.startsWith("Body:")){
                    readBody = true;
                }  
        }

        //Set the body of email to that of the string buffer
        body = stringBuffer.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //Send the email by passing in the variables defined by the .txt file into the method
        sendFromGMail(from, pass, to, cc, bcc, subject, body, server, filePath); 

    }


    //Sends an email via GMail to a list of recipients, CCs, and BCCs. The subject, body, server, are all defined previously in the .txt file. A filename may be chosen
    //as a path to attachment if the user decides to use it
    private static void sendFromGMail(String from, String pass, String[] to, String[] cc, String[] bcc, String subject, String body, String server, String filename) {

        //Initialize the host and port. Connect with the sender's email and password
        Properties props = System.getProperties();
        String host = server;
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        //Create the session and message
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);


        //Finds each recipients of the email and then builds the email in parts; 1st with the main
        //body of the email, and 2nd with the attachment (provided the user has entered a path)
        try {
            //Sender
            message.setFrom(new InternetAddress(from));
            //Send recipients for each of to, cc, bcc
            InternetAddress[] toAddress = new InternetAddress[to.length + cc.length + bcc.length];

            // To get the array of addresses for main recipients
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }
            // Gets array of CC members
            for(int i=0; i<cc.length; i++){
                toAddress[i + to.length] = new InternetAddress(cc[i]);
            }
            // Gets array of BCC members
            for(int i=0; i<bcc.length; i++){
                toAddress[i + to.length+cc.length] = new InternetAddress(bcc[i]);
            }


            //Starting at beginning of InternetAddress array, iterate through all TO
            for(int i=0; i<to.length; i++){
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }
            //After, start next iteration at CC
            for(int i=0; i<cc.length; i++){
                message.addRecipient(Message.RecipientType.CC, toAddress[i + to.length]);
            }
            //After, start next iteration at BCC
            for(int i=0; i<bcc.length; i++){
                message.addRecipient(Message.RecipientType.BCC, toAddress[i + to.length + bcc.length]);
            }


            // Create a multi-part to combine the parts
            Multipart multipart = new MimeMultipart("alternative");

            // Add the potential attachment as well as the message
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();

            //The actual body of email
            messageBodyPart.setText(body);

            //If user has entered filename, get the file
            if(filename!=""){
                //The file
                DataSource source = new FileDataSource(filename);
                File theFile = new File(filename);

                //Try adding the attachment
                try{
                    attachmentBodyPart.attachFile(theFile);
                    multipart.addBodyPart(attachmentBodyPart);
                }
                catch(IOException e){
                    System.out.println("Could not attach file");
                }
            }

            //Add the main message
            multipart.addBodyPart(messageBodyPart);            

            // Associate multi-part with message
            message.setContent(multipart);

            message.setSubject(subject);
            //message.setText(body);
            
            //Send the message
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}