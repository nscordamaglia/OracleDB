/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracledb;

/**
 *
 * @author u189299
 */
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Calse que se encarga de inicializar la programacion de las acciones
 * @author Nicolas Scordamaglia
 */
class DBtask {
    
     Timer timer;
        int count = 0;
        boolean finTarea = false;
        //Save logStart = new Save();
    
        /**
         * Genera el programador de tareas
         */
        public DBtask(){
            timer = new Timer () ; //instancia inicial de una programacion de tareas
            System.out.println("RUN!");
            
            //logStart.file("Timer created!", "logs/logserver.log");
        }
        
        /**
         * Metodo que toma el server y el parametro ingresado por el usuario y lo utiliza para decidir la accion a realizar
         * @param server
         * @param aux
         * @param since 
         */
        public  void tasker (HTTPserver server, float aux, Date since){
        
           Date now = new Date();
           
           long delay = (long) (aux*3600000);
            System.out.println("task " + delay + "ms");
            //logStart.file("schedule " + aux + "hs", "logs/logserver.log");
            // timer toma el valor expresado en hora y lo pasa a milisegundos
    
            //se crea un planificador que planificara las tareas
            
           
            if (aux > 0){
            
            //la Tarea se ejecutara pasado 1 segundo y luego periódicamente segun el delay
            timer.cancel();//se mata cualquier objeto timer anterior

            timer= new Timer();// se crea uno nuevo para ser programado
            timer.schedule (new task(), since, delay);//se programa la tarea segun lo necesario
           
            
            System.out.println("TASKER INIT!");
            //logStart.file("TASKER INIT!", "logs/logserver.log");
            }else{
            timer.cancel();// se mata el objeto timer para que deje de repetirse
             System.out.println("TASKER CLOSE!");
             //logStart.file("TASKER CLOSE!", "logs/logserver.log");
            //System.exit(0);
            }
            
            if(aux==-1){
                
                //logStart.file("close_server", "logs/logserver.log");
                server.get_server().stop(1);// se mata al servidor de ser necesario
            }

            
}

        /**
         * Subclase que inicia la programacion de la tarea cada x tiempo segun lo ingresado por el usuario
         */
        class task extends TimerTask {
              
                public void run() {
                   
                        OracleDB db = new OracleDB();// ejecucion del proceso ppal 
                        db.StartDB();
                        System.out.println("Waiting...");
                        //logStart.file("Waiting...", "logs/logserver.log");

                        
                   
                }
        }
    
}