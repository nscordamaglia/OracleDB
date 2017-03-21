/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracledb;





/**
 *
 * @author u189299
 */
class MakeReg {
    
    private FileReader file;
    private DBproperties properties;

    MakeReg(FileReader file) {
        
        this.file = file;
        this.properties = new DBproperties(this.file); 
    }
    
     public boolean isRow(){
         
         return file.hasRow();
     
     }
     public String Format() {
        
       
            /*
             * Leo el tipo, busco en las propiedades que columnas necesito, armo el insert
             */
          
            
            file.run();
            String insert = properties.Check();
            //System.out.println("sql -->" + insert);
             
        return insert;
    }
     
     public String Update(String update_name){
         
        String update;
        
                    ConfigManager.setPath(ConfigManager.getAppSetting("path_update"));
        update = ConfigManager.getAppSetting(update_name);
        return update;
     
     
     }
}
