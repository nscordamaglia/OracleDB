package oracledb;

/**
 *
 * @author Nicolas Scordamaglia
 */
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Clase que se encarga de iniciar el server local para poder recibir informacion via http
 * @author Nicolas Scordamaglia
 */
class HTTPserver {

    private DBtask timer=new DBtask();//instacia inicial del objeto programador de tareas
    private HttpServer server;
    private  String response;

    /**
     * Al inicio del metodo principal, el servidor es iniciado para poder estar en modo escucha y recibir las
     * acciones via http
     * @throws Exception
     */
    public void start() throws Exception {
        ConfigManager.setPath("config.properties");
        server = HttpServer.create(new InetSocketAddress(Integer.parseInt(ConfigManager.getAppSetting("port"))), 0);
        server.createContext(ConfigManager.getAppSetting("server"), new MyHandler(this));

        server.setExecutor(null); // creates a default executor
        server.start();
        //Save logStart = new Save();
        //logStart.file("init_server", "logs/logserver.log");
        //logStart.Exist("logs/master.log");
        //logStart.Exist("logs/ReporteIM.csv");
        //logStart.Exist("logs/reporting.csv");
        /*try
        {
            Desktop.getDesktop().open(new File("test.html"));
            //Desktop.getDesktop().browse(new URL(ConfigManager.getAppSetting("urlADM")).toURI());
        }
        catch (Exception e) {}*/
    }

    /**
     * Metodo para pasar el server como referencia
     * @return
     */
    public HttpServer get_server(){return server;}

    /**
     * Metodo para capturar los datos por http y generar el primer scheduling al inicio
     */
    private class MyHandler implements HttpHandler {

        private HTTPserver server;

        /**
         * Metodo para generar el primer scheduling
         * @param s
         */
        public MyHandler(HTTPserver s){

           try {
                server =s;
                String time = ConfigManager.getAppSetting("Time");
                String dateInString =  ConfigManager.getAppSetting("Date");
                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
                System.out.println(dateInString);
                Date date = formatter.parse(dateInString);
                //inicio la programacion de la tarea
                timer.tasker(this.server,Float.valueOf(time),date);
            } catch (ParseException ex) {
                Logger.getLogger(HTTPserver.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        /**
         * Metodo para interpretar la recepcion del parametro que viene via http
         * @param t
         * @throws IOException
         */
        public void handle(HttpExchange t) throws IOException {


                // espero la variable por post
                InputStreamReader isr =  new InputStreamReader(t.getRequestBody(),"utf-8");
                BufferedReader br = new BufferedReader(isr);

                // From now on, the right way of moving from bytes to utf-8 characters:

                int b;
                StringBuilder buf = new StringBuilder(512);
                while ((b = br.read()) != -1) {
                    buf.append((char) b);
                }

                br.close();
                isr.close();

                String value = buf.toString();
                System.out.println(value);
                String data1 = value.split("=")[0];
                String data2 = value.split("=")[1];



                switch (data1){


                    case "time":
                        //inicio la programacion de la tarea
                        timer.tasker(this.server,Float.parseFloat(data2),new Date());
                        response = "Server Iniciado";
                        break;

                    case "action":
                        System.out.println("action-->" + data2);
                        OracleDB db = new OracleDB();// ejecucion del proceso ppal
                        db.StartDB();
                        response = "Accion ejecutada";
                        break;

                }


                t.sendResponseHeaders(200, response.length());
                OutputStream os = t.getResponseBody();
                String encoding = "UTF-8";
                t.getResponseHeaders().set("Content-Type", "application/x-www-form-urlencoded; charset=" + encoding);
                os.write(response.getBytes());
                os.close();

        }
    }


}
