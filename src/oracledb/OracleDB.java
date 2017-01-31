/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracledb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author u189299
 */
public class OracleDB {

    private SendMail mail = new SendMail();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        

                //inicializacion de servidor http
		try {
                        HTTPserver server = new HTTPserver();
                        server.start();

                    } catch (Exception ex) {
                        Logger.getLogger(OracleDB.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
	}
    
      public void StartDB(){
          
          DBconnect db = null;
          boolean update = false;
          
          //Report.getInstance().setDeleted(0);//inicio el contador de registros eliminados por linea mal
        try {
            /*
             * Busco en el archivo lista_path.txt los archivos que tengo que verificar para extraer los formularios
             * por cada archivo encontrado tengo que recorrerlo e ir insertando los registros en la base auxiliar de zoom
             */
                      //Borro y creo nuevamente el archivo campana_mails.txt
                      SaveFile save = new SaveFile();
                      save.Exist("y://Vtex/config/campana_mail.txt");  
                      
                      String path =   "y://Vtex/lista_path.txt";
                      File file = new File(path);
                      Scanner s = new Scanner(file);
                      //System.out.println(file.getName());
                      boolean line = s.hasNextLine();
                      db = new DBconnect();
                      db.cleanDB();
                      while (line == true){
                      
                            String row = s.nextLine();
                            String form = row.split(";")[0];
                            String type = row.split(";")[1];
                            String prop = row.split(";")[2];
                            db.setPath(form);
                            db.setType(type);
                            db.setProp(prop);
                            
                            //System.out.println(form);
                            File f = new File(form);
                            if(f.exists()){
                                
                                    update = true;
                                    db.Insert();
                                    //Report.getInstance().setDeleted(0);//reinicio el contador de registros eliminados por linea mal
                                    db.getFile().close();
                                    db.getFile().move();
                            }else{
                            
                                System.out.println("no se encontró el formulario " + form);
                            }
                            line = s.hasNextLine();
                      
                      
                      }
                      if(update == true){
                          //necesito que exista un form como minimo para actualizar la db, caso contrario estaria vacia
                            db.Update();
                            System.out.println("fin update");
                            db.report(mail);
                          //cargo campana_mails.txt en tabla de mismo nombre
                            db.campanaMailing();
                            db.transmision();
                      }else{
                      
                            System.out.println("Sin formularios para actualizar la DB");
                            mail.setMsj("Sin formularios para actualizar la DB");
                            mail.Ready();
                      }
                      
                      s.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(OracleDB.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(OracleDB.class.getName()).log(Level.SEVERE, null, ex);
        }
            //Al finalzar la lista de formularios insertados en Tmp_Tzoom_1 se deben hacer los update/innerjoin para dejar la tabla en tzoom
            
        
      }    
      
      public void SearchFolder(){
      
          
       /* String path =   "s://Vtex/";
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        System.out.println("Existen " + listOfFiles.length + " files");
        for (File file : listOfFiles) {
        if (file.isFile()&&(file.getName().substring(file.getName().lastIndexOf('.')+1).equals("xlsx"))) {
            try {
                // Scanner 
                  Scanner s = new Scanner(file);
                  System.out.println(file.getName());
                  ReadXLS read = new ReadXLS(path+file.getName());
                  read.run();
                  System.out.println(s.nextLine().replace("\t", ";"));
                  s.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(OracleDB.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (NoSuchElementException ex){
            
                System.out.println("archivo vacio");
            } catch (IOException ex) {
                Logger.getLogger(OracleDB.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
        }*/
      }

}