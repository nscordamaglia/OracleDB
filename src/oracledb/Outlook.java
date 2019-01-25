package oracledb;

import java.awt.Desktop;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nicolas Scordamaglia
 */
public class Outlook {

     public void sendMail(String msj)  {

         msj = urlEncode(msj);
         try {
             try {
                 Desktop.getDesktop().mail(new URI("mailto:abc@def.com?subject=someSubject&cc=aa@bb.cc,dd@dd.ds&bcc=x@y.zz&body=" + msj));
             } catch (IOException ex) {
                 Logger.getLogger(Outlook.class.getName()).log(Level.SEVERE, null, ex);
             }
         } catch (URISyntaxException ex) {
             Logger.getLogger(Outlook.class.getName()).log(Level.SEVERE, null, ex);
         }


        }


                private static final String urlEncode(String str) {
               try {
                   return URLEncoder.encode(str, "UTF-8").replace("+", "%20");
               } catch (UnsupportedEncodingException e) {
                   throw new RuntimeException(e);
               }
           }
}
