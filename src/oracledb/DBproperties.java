/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracledb;


import java.util.ArrayList;
import java.util.regex.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author u189299
 */
class DBproperties {

    private String input;
    private int tlinea,apellido,nombre,dni,mail,refcode,lineateco,fecha,contacto,designname,domicilio,producto;
    private String observaciones;
    private ArrayList<String> row = new ArrayList<String>();
    private ArrayList<Integer> headerIndex = new ArrayList<Integer>();
    private String[] headerName = {"tlinea","apellido","nombre","dni","mail","refcode","designname","lineateco","contacto","fecha","domicilio","producto"};
    private FileReader file;
    private ArrayList<String> rowHeader = new ArrayList<String>();
    private String lineOftxt;

    DBproperties(FileReader file) {
        this.file = file;
        this.rowHeader.addAll(this.file.readHeader());
        // Recibe el type y devuelvo el input armado segun la conf
       
        ConfigManager.setPath("y://Vtex/config/"+this.file.getProp()+".properties");
        cargo_config(this.file.getNumCol());
        
                
    }
    
    String Check() {
         
       row.clear();
       row.addAll(file.getRowToArray());
       
       observaciones = null;
              
       //Datos extraidos del formulario
       tlinea = headerIndex.get(0);
       apellido = headerIndex.get(1);
       nombre = headerIndex.get(2);
       dni = headerIndex.get(3);
       mail = headerIndex.get(4);
       refcode = headerIndex.get(5);
       designname = headerIndex.get(6);
       lineateco = headerIndex.get(7);
       contacto = headerIndex.get(8);
       fecha = headerIndex.get(9);
       domicilio = headerIndex.get(10);
       producto = headerIndex.get(11);
       
       Integer[] arrayObsIndex =        {fecha,                   mail,       dni,       refcode,           producto,       contacto,          domicilio};
       String[]  arrayObsText  =        {"Fecha Formulario : ","; Mail : ","; DNI : ","; Cod/ID Prod : ","; Producto : ","; Tel Contacto: ","; Domicilio: "};
       String[]  arrayObsValidate =     {"fecha",                 "mail",     "dni",     "refcode",         "producto",     "contacto",        "domicilio"};
       
     
       for (int i = 0; i<arrayObsIndex.length; i++){
       //defino el string observaciones
           if (arrayObsIndex[i] != 99){
           
               if(i==1){
               
                   //guardo mail para grabar en archivo
                   lineOftxt = validate(row.get(arrayObsIndex[i]),arrayObsValidate[i]);
               }
               observaciones = observaciones + arrayObsText[i] + validate(row.get(arrayObsIndex[i]),arrayObsValidate[i]);
           
           }
       
       }
       
       Integer[] arrayInputIndex = {tlinea,apellido,nombre,designname};
       String[]  arrayInputText  = new String[4];
       String[]  arrayInputValidate = {"tlinea","apellido","nombre","designname"};
       
       for (int j = 0; j<4; j++){
       
           if(arrayInputIndex[j] != 99){
           
               String field = validate(row.get(arrayInputIndex[j]),arrayInputValidate[j]);
               
                if (j==0 && field.equals("")){
                   //si el telefono en vtex esta vacio usa el contacto y si esta vacio borrara el registro
                    try{
                        arrayInputText[j] = validate(row.get(arrayObsIndex[5]),arrayObsValidate[5]);
                    }catch(IndexOutOfBoundsException e){ 
                        //en los casos que el telefono no exista en tform, no existe contacto para insertar por lo tanto queda vacio y
                        //se eliminara el registro
                        arrayInputText[j] ="0";
                        }
                           
               }else if(j==2 && field.equals("")){
               
                   arrayInputText[j] = "NULL";
               
               }else{
                        int lenght = field.length();
                        if (lenght>60){
                            arrayInputText[j] = field.substring(0, 60);
                        }else{
                            arrayInputText[j] = field;
                        }
                    }      
           }else{
               
               if (j==3){
                   
                   arrayInputText[j] = JsonParser(ConfigManager.getAppSetting("designname"),"prod");
                           
               }else{
               
                   arrayInputText[j] = "NULL";

               }
           }
       
       }
       //Fin de extraccion
      
       if (!arrayInputText[0].equals("") && !observaciones.equals("")){
       
           input = "INSERT INTO Tmp_Tzoom_1 (tlinea,apellido,nombre,observaciones,designname) VALUES ('" + arrayInputText[0] + "','"+ arrayInputText[1] +"','"+ arrayInputText[2] +"','"+ observaciones.replace("null", "") +"','"+ arrayInputText[3] +"')";
           
            //guardo tlinea y mail en campana_mails.txt si es vtex o th
            if(file.getProp().equals("vtex")||file.getProp().equals("th") && !lineOftxt.equals("")){

                lineOftxt = lineOftxt + ";" + arrayInputText[0];
                SaveFile save = new SaveFile();
                ConfigManager.setPath("config.properties");
                save.file(lineOftxt.trim(), ConfigManager.getAppSetting("tabla_mail"));
            }
       
       }else{
       
           System.out.println("Entra por insert null");
           input = "NULL";
       }
      
      
       return input;
    }
    
   
     private void cargo_config(int colNum){
         
         System.out.println("cargo conofig");
         int headerColumn,columns;
         /*
          * armo el array de indices
          */
        headerColumn = colNum;
        columns = headerName.length;
        for (int i=0;i<columns;i++){
        
            //System.out.println("busco los indices en las " + headerColumn + " columnas");
        //el resultado es poder ubicar la posicion en el formulario y guardarlo en el array
            
            headerIndex.add(i,searchHeader(JsonParser(ConfigManager.getAppSetting(headerName[i]),"destino"),headerColumn));
            
        
        }
        
    
   }

    private Integer searchHeader(String name, int colNum) {
        
        int index = 0;
        int col = colNum;
        
         
        for (int i=0; i<col; i++){
        
        //recorro la fila ubicando la posicion de cada campo cabecera
            if(rowHeader.get(i).equalsIgnoreCase(name)){
            
                //System.out.println(rowHeader.get(i) + " - " + name);
                index = i;
                break;
            
            }else{
            
                //System.out.println(rowHeader.get(i) + " - " + name);
                index = 99;
            }
            
        
        }
        
        
        return index;
    }

    private String JsonParser(String appSetting, String clave) {
        /*
         * 
         * extrae el json de cada campo y extre el valor de la clave buscada
         * ejemplo {
                    "destino":"nombre_campo",
                    "other":"nombre_campo",
                    "other_array":["msg 1","msg 2","msg 3"]
            }
         
         */
        
        JSONParser parser = new JSONParser();
        
        String destino = null;
	try {

		Object obj = parser.parse(appSetting);

		JSONObject jsonObject = (JSONObject) obj;

		destino = (String) jsonObject.get(clave);
		//System.out.println(destino);

		/* loop array
		JSONArray msg = (JSONArray) jsonObject.get("messages");
		Iterator<String> iterator = msg.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}*/

	}catch (ParseException e) {
            //falla de parseo
		e.printStackTrace();
                
	}catch(NullPointerException e){
                //si no encentra el tag
                e.printStackTrace();
        }
        return destino;
    }

    private String validate(String f, String t) {
        // String to be scanned to find the pattern.
      String field = f;
      String type = t;
      String pattern = regex(type);
      String found = null;
      boolean match = false;
      

      // Create a Pattern object
      Pattern r = Pattern.compile(pattern);

      // Now create matcher object.
      Matcher m = r.matcher(field);
      while (m.find( )) {
          
          match = true;
          //evaluo campo con la expresion regular correspondiente al tipo de campo
         System.out.println("Found value: " + m.group() + " in " + type);
         found = found + m.group() + " ";
         
         
      }
          if (match == true){
          
              field = found.replace("null", "").trim();
              /*if(type.equalsIgnoreCase("tlinea")){
              
                  boolean cellphone = verify_cell(field);
                  if (cellphone == true){
                  //si tiene 15 esta mal el numero y debe descartarse la linea
                      field = field.substring(1,field.length());
                      int del = Report.getInstance().getDeleted();
                      del = del+1;
                      Report.getInstance().setDeleted(del);
                      System.out.println("deleted "  + Report.getInstance().getDeleted());
                  
                  }
                  
              
              }*/
              
          }else{
          
              /*if(type.equalsIgnoreCase("tlinea")){
              
                      int del = Report.getInstance().getDeleted();
                      del = del+1;
                      Report.getInstance().setDeleted(del);
                      System.out.println("deleted "  + Report.getInstance().getDeleted());
              
              }*/
          
          }
         //en caso de cimplir los requicitos--> 
         System.out.println("MATCH: " + field);
      
      
      return field;
    }

    private String regex(String t) {
        String regex = null;
        
        switch(t){
            
            case "dni":
                regex = "(.*)(\\d+)(.*)";
                break;
                
            case "mail":
                regex = "\\S*@[a-z]*.[a-z]{2,3}";
                break;
                
            case "fecha":
                regex = "\\d{2}\\D\\d{2}\\D\\d{4}|\\d{4}\\D\\d{2}\\D\\d{2}";
                break;
            
            case "nombre":
            case "apellido":
                regex = "[a-zA-ZñÑáéíóúÁÉÍÓÚ]{0,60}";
                break;
                
            case "contacto":    
            case "tlinea":
                regex = "[^0]{1}[0-9]{1,22}";
                break;
                
                            
            default:
                regex = ".*";
                break;
        
        }
        
        return regex;
    }

    private boolean verify_cell(String field) {
        
        System.out.println(" carteristicas : " + field.substring(3, 5) + " - " + field.substring(4,6) + " - " + field.substring(1,3));
        if (field.substring(3, 5).equalsIgnoreCase("115")||field.substring(4,6).equalsIgnoreCase("115")||field.substring(1,3).equalsIgnoreCase("115")){
        
            return true;
        
        }else{
        
            return false;
            
        }
    }
    
}
