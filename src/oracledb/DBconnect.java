package oracledb;



import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;




/**
 *
 * @author Nicolas Scordamaglia
 */
public class DBconnect {

    private Connection connection = null;
    private String msj = null;
    private String path;
    private String input;
    private FileReader file;
    private MakeReg reg;
    private String type;
    private String prop;

    public FileReader getFile() {
        return file;
    }

    public void setFile(FileReader file) {
        this.file = file;
    }



    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }





    /*Currently it fetches this setting from the VM variable user.language (which is set automatically
     *by setting the current locale, or on startup from the system environment).
     *This is a problem when the users switch the application locale to a one that is unsupported
     *by the Oracle JDBC driver (e.g. mk_MK). In this case, the next time I fetch a connection
     *I get the following exception:
     *ORA-00604: error occurred at recursive SQL level 1
     *ORA-12705: Cannot access NLS data files or invalid environment specified
     */
    public DBconnect() {

                ConfigManager.setPath("config.properties");
                Locale en = new Locale("en", "US");
                System.out.println(en.getDisplayName(en));
                Locale.setDefault(new Locale ("en", "US"));

                System.out.println("-------- DB JDBC Connection Testing ------");

		try {

			    Class.forName(ConfigManager.getAppSetting("db_class"));


		} catch (ClassNotFoundException e) {

			System.out.println("Where is your DB JDBC Driver?");
			e.printStackTrace();
			return;

		}

		System.out.println("DB JDBC Driver Registered!");
    }


    private void Connect(){



                try {

                        //Busca en la configuracion la lista de archivos
                        //itera n veces ejecutando las acciones por cada archivo
                        //detecta si es txt,xls o csv para ejecutar una accion diferente para leerlo
                        //Una vez leido, valida segun el archivo de configuracion que columnas debe leer e ir armando el insert
                        //Con el registro creado se inserta en la tabla de oracle
                        //Al finalizar todo el formulario en la tabla auxiliar se corre la actualizacion de la tabla para ser insertada en tzoom

                        /*select * from Tmp_Tzoom_1 --> base auxiliar para guardar la info extraida del archivo*/
                                  ConfigManager.setPath("config.properties");
                                  connection = DriverManager.getConnection(
                                                  ConfigManager.getAppSetting("db_ref"),ConfigManager.getAppSetting("user"),ConfigManager.getAppSetting("pass"));
                                  connection.setAutoCommit(false);
                                  Statement stmt = connection.createStatement();
                                  String sql = getInput();
                                  System.out.println(sql);
                                  stmt.executeUpdate(sql);
                                  connection.commit();
                                  stmt.close();
                                  connection.close();

                                  /*
                                   * ejecucion de sentencia de transmision de brief a AC
                                   *
                                   * -->DBM.TRANSMICION_ZOOM_CCT.COPIA_REMOTA_XBRIEF(803048,'S',201611)
                                    CallableStatement procin = connection.prepareCall ("begin DBM.TRANSMICION_ZOOM_CCT.COPIA_REMOTA_XBRIEF (?,?,?); end;");
                                    procin.setInt(1, 803048);
                                    procin.setString (2, "'s'");//varchar2
                                    procin.setInt (3, 201611);
                                    procin.execute ();
                                    procin.close();
                                  /*
                                   * ejecucion de sentencia para obtener resultado
                                   *
                                   * ResultSet rs = stmt.executeQuery(sql);
                                        while(rs.next()){

                                      msj = msj + "Campana " + rs.getString("campana")+"Subcam " + rs.getString("subcam")+"Count " + rs.getString("count(*)")+"\n";
                                      System.out.println(msj);
                                      System.out.println("Campana: " + rs.getString("campana"));
                                      System.out.println("Subcam: " + rs.getString("subcam"));
                                      System.out.println("Count: " + rs.getString("count(*)"));


                                  }

                                  */

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();


		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}



    }



    public void Insert() {

        int read_reg = 0;


            System.out.println("voy a evaluar el formulario");
            //segun sea el tipo de archivo voy a instanciar la clare reader correspondiente
            switch (getType()){

                case "xls":
                    file = new ReadXLS(getPath(),getProp());
                    file.setType(getType());
                    break;

                case "txt":
                    file = new ReadTXT(getPath(),getProp());
                    file.setType(getType());
                    break;

            }

            reg = new MakeReg(file);
            //escribo en msj --> Formulario XXXX registros eliminados
            Report.getInstance().setMsj(Report.getInstance().getMsj() + "\n Formulario " + Report.getInstance().getForm(file.getProp())  );
            while(reg.isRow()){

                //por cada registro leido del archivo, debe haber un registro insertado si viene nulo no inserto nada
                String input = reg.Format();
                if (!input.equals("NULL")){

                    setInput(input);
                    Connect();
                    read_reg++;
                }
                //acumulo registros leidos


            }
            Report.getInstance().setMsj(Report.getInstance().getMsj()  + " --> " + read_reg + " registros leidos.");
            //acumulo la cantidad de registros eliminados y escribo en msj --> X (será cero en caso de no eliminar ninguno)
            //escribo en msj --> de X registros leidos \n




    }

    void Update() {

        String fmonth = null;
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        //System.out.println("estamos en el mes: " + month);
        switch (month){

            case 0:
                case 1:
                    case 2:
                        case 3:
                            case 4:
                                case 5:
                                    case 6:
                                        case 7:
                                            case 8:
                                              fmonth = String.valueOf(month+1);
                                                break;

                                            case 9:
                                                fmonth = "O";
                                                break;

                                            case 10:
                                                fmonth = "N";
                                                break;

                                            case 11:
                                                fmonth = "D";
                                                break;


        }

                                  ConfigManager.setPath(ConfigManager.getAppSetting("path_update"));
                      String update_array = ConfigManager.getAppSetting("update_array");
                      SaveBrief();
                      for (int i = 0;i<update_array.split(";").length;i++){

                          if(update_array.split(";")[i].equalsIgnoreCase("update_campana")){

                            System.out.println("UPDATE Tmp_Tzoom_1");
                            String aux = reg.Update(update_array.split(";")[i]).replace("%", fmonth);
                            setInput(aux);
                            setInput(getInput().replace(":", "="));

                          }else if(update_array.split(";")[i].equalsIgnoreCase("update_brief")){

                              updateBrief();

                          }else{

                            System.out.println("UPDATE Tmp_Tzoom_1");
                            setInput(reg.Update(update_array.split(";")[i]));
                            setInput(getInput().replace(":", "="));
                          }
                          Connect();

                      }


          }

    void report(SendMail mail) {
        try {
            connection = DriverManager.getConnection(
                                                  ConfigManager.getAppSetting("db_ref"),ConfigManager.getAppSetting("user"),ConfigManager.getAppSetting("pass"));
            connection.setAutoCommit(false);
                        ConfigManager.setPath(ConfigManager.getAppSetting("path_update"));
            Statement stmt = connection.createStatement();
            String sql = ConfigManager.getAppSetting("info_tzoom");
            ResultSet rs = stmt.executeQuery(sql);
            ConfigManager.setPath("config.properties");
            msj = ConfigManager.getAppSetting("mail_importar_ins").replace(";", "\t");
            while(rs.next()){

            msj = msj + "\n" + rs.getString("nro_brief")+ "\t" + rs.getString("campana")+"\t" + rs.getString("subcam")+"\t\t" + rs.getString("count(*)");




              }
                        ConfigManager.setPath(ConfigManager.getAppSetting("path_update"));
            Statement stmt1 = connection.createStatement();
            String sql1 = ConfigManager.getAppSetting("info_tzoom_del");
            ResultSet rs1 = stmt1.executeQuery(sql1);
            msj = msj + "\n\n" + "Eliminados por duplicidad o mal tlinea \n\n" + ConfigManager.getAppSetting("mail_importar_del").replace(";", "\t\t");
            while(rs1.next()){

            msj = msj + "\n" + rs1.getString("nro_brief")+ "\t\t" + rs1.getString("campana")+"\t\t" + rs1.getString("lote")+"\t\t" + rs1.getString("count(*)");




              }
            System.out.println(msj);
            mail.setMsj(Report.getInstance().getMsj().substring(4) + "\n\nBuenas, por favor importar. Gracias!\n\n" + msj);
            mail.Ready();
            Report.getInstance().setMsj(null);
            stmt.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBconnect.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    void cleanDB() {

        String log,msj;
        try {
            ConfigManager.setPath("config.properties");
            connection = DriverManager.getConnection(
                                                  ConfigManager.getAppSetting("db_ref"),ConfigManager.getAppSetting("user"),ConfigManager.getAppSetting("pass"));
            connection.setAutoCommit(false);
            ConfigManager.setPath(ConfigManager.getAppSetting("path_update"));
            Statement stmt = connection.createStatement();
            String array = ConfigManager.getAppSetting("clean_tables");
            int lenght_array = array.split(";").length;
            for(int i=0;i<lenght_array;i++){
                                  String sql = array.split(";")[i];
                                  System.out.println(sql);
                                  try{
                                      stmt.executeUpdate(sql);
                                  }catch(SQLException ex){

                                      Logger.getLogger(DBconnect.class.getName()).log(Level.SEVERE, null, ex);
                                  }
            }
                                  connection.commit();
                                  stmt.close();
                                  connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBconnect.class.getName()).log(Level.SEVERE, null, ex);
            msj = ex.getMessage().substring(0,9);
            /*
                ORA-28001: the password has expired
                ORA-01017: invalid username/password; logon denied
                ORA-28000: the account is locked
             */
            if (msj.equalsIgnoreCase("ORA-28001")){

                //corta el proceso y avisa por mail informando el problema con usuario
                log = "the password has expired";
                SendMail mail = new SendMail();
                mail.setMsj(log);
                mail.Ready();
                System.exit(0);

            }else if(msj.equalsIgnoreCase("ORA-01017")){

                //corta el proceso y avisa por mail informando el problema con usuario
                log = "invalid username/password; logon denied";
                SendMail mail = new SendMail();
                mail.setMsj(log);
                mail.Ready();
                System.exit(0);

            }else if(msj.equalsIgnoreCase("ORA-28000")){

                //corta el proceso y avisa por mail informando el problema con usuario
                log =  "the account is locked";
                SendMail mail = new SendMail();
                mail.setMsj(log);
                mail.Ready();
                System.exit(0);

            }


        }
    }

    void campanaMailing() {
        String mail,tlinea;
        Scanner scanner = null;
        try {
            //Leo linea por linea e inserto en tzoom_campana_mails
            ConfigManager.setPath("config.properties");
            File campana = new File(ConfigManager.getAppSetting("tabla_mail"));
            scanner = new Scanner(campana);
            while (scanner.hasNextLine()){

                String line = scanner.nextLine();
                mail = line.split(";")[0];
                tlinea = line.split(";")[1];
                if (tlinea.length()> 10){

                    tlinea = tlinea.substring(0, 10);
                }
                setInput("INSERT INTO tzoom_campana_mails (mail,tlinea) VALUES ('" + mail + "','" + tlinea + "')");
                Connect();


            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBconnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        scanner.close();
                    ConfigManager.setPath(ConfigManager.getAppSetting("path_update"));
        setInput(ConfigManager.getAppSetting("grant_table"));
        Connect();
    }

    void transmision() {

        int brief = 0, aniomes = 0;
        try {
            /*
               * ejecucion de sentencia de transmision de brief a AC
               *
               * -->DBM.TRANSMICION_ZOOM_CCT.COPIA_REMOTA_XBRIEF(803048,'S',201611)*/
                ConfigManager.setPath("config.properties");
                connection = DriverManager.getConnection(
                              ConfigManager.getAppSetting("db_ref"),ConfigManager.getAppSetting("user"),ConfigManager.getAppSetting("pass"));
                connection.setAutoCommit(false);
                            ConfigManager.setPath(ConfigManager.getAppSetting("path_update"));
                /* EXTRAIGO BRIEF*/
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(ConfigManager.getAppSetting("brief_transmit"));
                                        while(rs.next()){

                                            brief = Integer.parseInt(rs.getString("nro_brief"));
                                        }

                DateFormat dateFormat = new SimpleDateFormat("YYYYMM");
                Date date = new Date();
                aniomes = Integer.parseInt(dateFormat.format(date));
                System.out.println("Se transmitira el brief " + brief + " en " + aniomes);
                /*CallableStatement procin = connection.prepareCall ("begin DBM.TRANSMICION_ZOOM_CCT.COPIA_REMOTA_XBRIEF (?,?,?); end;");
                procin.setInt(1, brief);
                procin.setString (2, "'s'");//varchar2
                procin.setInt (3, aniomes);
                procin.execute ();
                procin.close();*/
                connection.commit();
                stmt.close();
                connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBconnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void SaveBrief() {
                   ConfigManager.setPath(ConfigManager.getAppSetting("path_update"));

    }

    private void updateBrief() {
        String brief = null, campana, sql_brief, sql_campana, sql_max_brief;
        ArrayList<String> arrayCampana = new ArrayList<>();
        try {
            ConfigManager.setPath("config.properties");
            connection = DriverManager.getConnection(
                          ConfigManager.getAppSetting("db_ref"),ConfigManager.getAppSetting("user"),ConfigManager.getAppSetting("pass"));
            connection.setAutoCommit(false);
                        ConfigManager.setPath(ConfigManager.getAppSetting("path_update"));
            sql_max_brief = ConfigManager.getAppSetting("select_max_brief");
            sql_campana = ConfigManager.getAppSetting("distinct_campana");
            /* busco campañas */
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql_campana);
                                    while(rs.next()){


                                        arrayCampana.add(rs.getString("campana"));
                                        System.out.println(rs.getString("campana"));

                                    }

            connection.commit();
            stmt.close();
            /* busco max brief  */
            Statement stmt1 = connection.createStatement();
            ResultSet rs1 = stmt1.executeQuery(sql_max_brief);
                                    while(rs1.next()){


                                        brief = rs1.getString("nro_brief");
                                        System.out.println(rs1.getString("nro_brief"));

                                    }
            connection.commit();
            stmt1.close();
            connection.close();
            int size = arrayCampana.size();
            for (int i=0;i<size;i++){

                int nro_brief = Integer.parseInt(brief) + i;
                sql_brief = ConfigManager.getAppSetting("update_brief").replace("%", "(" + nro_brief) + ") where campana='" + arrayCampana.get(i) + "'";
                System.out.println(sql_brief);
                setInput(sql_brief);
                Connect();

            }


        } catch (SQLException ex) {
            Logger.getLogger(DBconnect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



}
