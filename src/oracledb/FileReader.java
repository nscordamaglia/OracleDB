package oracledb;


import java.util.ArrayList;

/**
 *
 * @author Nicolas Scordamaglia
 */
abstract public class FileReader {

    private String path;
    private String type;
    private ArrayList rowToArray;
    private int rowposition;
    private int numCol;
    private String prop;

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }



    public int getNumCol() {
        return numCol;
    }

    public void setNumCol(int numCol) {
        this.numCol = numCol;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getRowToArray() {
        return rowToArray;
    }

    public void setRowToArray(ArrayList<String> rowToArray) {
        this.rowToArray = rowToArray;
    }

    public int getRowposition() {
        return rowposition;
    }

    public void setRowposition(int rowposition) {
        this.rowposition = rowposition;
    }




    FileReader(String path,String prop) {
        this.path = path;
        this.rowposition = 0;
        this.prop = prop;
    }

    //simple way to check for both types of excel files
    public boolean isValid(){
        return false;

    }

    public void run(){

        /* metodo que lee registros */
    }

    public boolean hasRow(){

        /* metodo que devuelve si existe un registro para leer*/
        return false;
    }

    public ArrayList<String> readHeader(){

        /* metodo que lee la cabecera*/
        return null;

    }

    void move() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
