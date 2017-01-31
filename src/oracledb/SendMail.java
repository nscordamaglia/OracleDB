/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracledb;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author u189299
 */
public class SendMail {
        
      private String to;
      private String from;
      private String host;
      private String msj;

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }
      
      
    public void Ready (){
    
        ConfigManager.setPath("config.properties");
    
        // Recipient's email ID needs to be mentioned.
       to = ConfigManager.getAppSetting("mailto");

      // Sender's email ID needs to be mentioned
       from = ConfigManager.getAppSetting("mailfrom");
              
      // Assuming you are sending email from localhost
       host = ConfigManager.getAppSetting("smtp");

     
            // Get system properties
      Properties properties = System.getProperties();

      // Setup mail server
      properties.setProperty("mail.smtp.host", host);

      // Get the default Session object.
      Session session = Session.getDefaultInstance(properties);

      try{
         // Create a default MimeMessage object.
         MimeMessage message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Set Subject: header field
         message.setSubject("Automatización de formularios");

         // Send the actual HTML message, as big as you like
         message.setContent(  msj, "text/plain" );

         // Send message
         Transport.send(message);
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
         
      }
    
    }

}
