
package oracledb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Clase para poder grabar en archivo
 * @author Nicolas Scordamaglia
 */
class SaveFile {
    
     /**
             * Metodo que recibe el texto y la ruta para poder guardar en archivo segun se requiera
             * @param data
             * @param path 
             */
    
            public void file(String data, String path){
                
                //creo el archivo para guardar la hash para otro momento
                            File file = null;
                            FileWriter filew = null;
                            PrintWriter pw = null;
                            try
                            {
                                
                                file = new File(path);/*  destino de fichero */
                                
                                filew = new FileWriter(path,true);
                                pw = new PrintWriter(filew);                             
                                
                                    
                                pw.println(data);
                                
                                

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                               try {
                               // Nuevamente aprovechamos el finally para 
                               // asegurarnos que se cierra el fichero.
                               if (null != filew)
                                  filew.close();
                               } catch (Exception e2) {
                                  e2.printStackTrace();
                               }
                            }
                
            
            }
            
            /**
             * Metodo que verifica la existencia del archivo para evaluar su creacion de ser necesario
             * @param path
             * @throws IOException 
             */
            public void Exist(String path) throws IOException {
                
                 
                 File file = null;
                 File oldFile = null;
                 file = new File(path);/*  destino de fichero */
                 
                
               
                                
                                        //si existe lo borro, ya que  debe ser unico por cada tarea realizada
                                        if(file.exists()){
                                            
                                           
                                            file.delete();
                                             
                                        }
                              
            
        
            
            }
}

