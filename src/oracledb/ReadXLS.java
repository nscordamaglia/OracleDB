package oracledb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


/**
 *
 * @author Nicolas Scordamaglia
 */
public class ReadXLS extends FileReader{

    private String path;
    private String type;
    private ArrayList<String> rowToArray = new ArrayList<String>();
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
        this.hasRow();
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


    public ReadXLS(String path,String prop) {
        super(path,prop);
        this.path = path;
        this.rowposition = 1;
        this.prop = prop;
        System.out.println("inicio de reader");
        //creo workbook - sheet - row - cell
    }

    @Override
    public boolean isValid() {

        return super.isValid();
    }


    @Override
    public ArrayList<String> readHeader() {

        ArrayList<String> array = new ArrayList<String>();

        FileInputStream fis = null;
        try {
            File excel =  new File (path);
            fis = new FileInputStream(excel);
            Workbook wb = WorkbookFactory.create(fis);
            Sheet ws = wb.getSheetAt(0);

            int colNum = ws.getRow(0).getLastCellNum();

           //recorro cabecera
                Row row = ws.getRow(0);
                    for (int j = 0; j < colNum; j++){
                        Cell cell = row.getCell(j);

                        try{
                            //guardo la cabecera detectada
                            String value = cell.toString();
                            array.add(j,value);

                        }catch(NullPointerException ex){
                            //ante la posibilidad de que venga sucio el archivo y el conteo de cabecera este equivocado
                            array.add(j,"vacio");
                        }
                    }



        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return array;
    }



    @Override
    public boolean hasRow() {

        boolean empty = false;
        try {
            FileInputStream fis = null;
            File excel =  new File (path);
            fis = new FileInputStream(excel);
            Workbook wb = WorkbookFactory.create(fis);
            Sheet ws = wb.getSheetAt(0);

                int Num = ws.getLastRowNum()+1;
                //System.out.println(Num + " - " + rowposition);
                if(Num-rowposition>0 && Num>1){

                     empty = true;
                     //System.out.println("hay " + (Num-rowposition) + " filas");
                     int colNum = ws.getRow(0).getLastCellNum();
                     setNumCol(colNum);

                }else{

                     empty = false;
                     System.out.println("no hay filas");

                }



        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return empty;
    }

    public void run() {
        FileInputStream fis = null;
        try {
            File excel =  new File (path);
            fis = new FileInputStream(excel);
            Workbook wb = WorkbookFactory.create(fis);
            Sheet ws = wb.getSheetAt(0);
            int rowNum = ws.getLastRowNum() + 1;
            int colNum = ws.getRow(0).getLastCellNum();
            setNumCol(colNum);
            String [][] data = new String [rowNum] [colNum];

                Row row = ws.getRow(rowposition);
                    for (int j = 0; j < colNum; j++){
                        Cell cell = row.getCell(j);
                        String value = null;
                        int cellType;
                        try{
                             cellType = cell.getCellType();
                        }catch(NullPointerException e){

                             cellType = Cell.CELL_TYPE_BLANK;
                        }
                        switch (cellType) {
                            case Cell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue();
                                break;

                            case Cell.CELL_TYPE_FORMULA:
                                value = cell.getCellFormula();
                                break;

                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    // instancio formato para fecha
                                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                                    // extraigo fecha de la celda
                                    Date date = cell.getDateCellValue();
                                    //aplico formato
                                    value = df.format(date);
                                } else {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                value = cell.getStringCellValue();
                                }
                                break;

                            case Cell.CELL_TYPE_BLANK:
                                value = "";
                                break;

                            case Cell.CELL_TYPE_ERROR:
                                value = "";
                                break;

                            case Cell.CELL_TYPE_BOOLEAN:
                                value = Boolean.toString(cell.getBooleanCellValue());
                                break;

                            }

                        rowToArray.add(j,value);
                        //System.out.println ("posicion: " + j + " - valor: " + rowToArray.get(j));
                    }

            rowposition  = rowposition+1;
            //System.out.println("posicion " + rowposition);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ReadXLS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    void move() {

        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH.mm");
        Date date = new Date();
        try{

    	   File afile =new File(getPath());

    	   if(afile.renameTo(new File("y:\\vtex\\olds\\" +  dateFormat.format(date) + "_" + afile.getName()))){
    		System.out.println("File is moved successful!");
    	   }else{
    		System.out.println("File is failed to move!");
    	   }

    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }

    @Override
    void delete() {

    }

    @Override
    void close() {

    }




}
