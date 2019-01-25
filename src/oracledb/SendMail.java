package oracledb;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Nicolas Scordamaglia
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

        // Destino
       to = ConfigManager.getAppSetting("mailto");

      // Origen
       from = ConfigManager.getAppSetting("mailfrom");

      // smtp
       host = ConfigManager.getAppSetting("smtp");


            // propiedades de sistema
      Properties properties = System.getProperties();

      // configurar servidor
      properties.setProperty("mail.smtp.host", host);

      // sesion por defecto
      Session session = Session.getDefaultInstance(properties);

      try{
         // Crear mensaje
         MimeMessage message = new MimeMessage(session);

         // Configurar origen
         message.setFrom(new InternetAddress(from));

         // Configurar destino
         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

         // Configurar asunto
         message.setSubject("Automatización de formularios");

         // Crear mensaje
         message.setContent(  msj, "text/plain" );

         // Enviar mensaje
         Transport.send(message);
         System.out.println("Sent message successfully....");
      }catch (MessagingException mex) {
         mex.printStackTrace();
         
      }

    }

}
