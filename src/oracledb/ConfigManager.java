/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracledb;

/**
 *
 * @author u189299
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase que se encarga de extraer los datos estaticos del archivo de configuracion
 * @author Nicolas Scordamaglia
 */
class ConfigManager {
    
    private static Properties p = null;
    private static String path;    

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        ConfigManager.path = path;
    }
    
    
	
	private static Properties getInstance() {
		if (p==null) {
			p = new Properties();
			
		}
		return p;
	}
	
        /**
         * Metodo que toma la variable como clave de configuracion y devuelve el valor del mismo configurado
         * de forma estatica en un archivo externo. Dicha configuracion esta normalizada como clave=valor
         * @param appSetting
         * @return valor de la clave solicitada
         */
	public static String getAppSetting(String appSetting) {
		Properties prop = getInstance();
                try {
				prop.load(new FileInputStream(path));
		 	 } catch (IOException ex) {}
		return prop.getProperty(appSetting);
	}
}

