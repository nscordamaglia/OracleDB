/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracledb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.*;

/**
 *
 * @author u189299
 */
public class ReadTXT extends FileReader{

    private String path;
    private String type;
    private ArrayList<String> rowToArray = new ArrayList<String>();
    private int rowposition;
    private int numCol;
    private File txt;
    private Scanner s;
    private String prop;

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public File getTxt() {
        return txt;
    }

    public void setTxt(File txt) {
        this.txt = txt;
    }

    
    public int getNumCol() {
        File file = new File(getPath());
        
        try {
            Scanner sc = new Scanner(file);
            if(sc.hasNextLine()){
                        String row = sc.nextLine().replace("\t", ";");
                        numCol = row.split(";").length;
                        sc.close();
                      }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
    
    public ReadTXT(String path,String prop) {
        super(path,prop);
        try {
            this.path = path;
            this.rowposition = 1;
            this.txt = new File(getPath());
            this.s = new Scanner(this.txt);
            this.prop = prop;
            System.out.println("inicio de reader");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        
                  
                  System.out.println(txt.getName());
                  if(s.hasNextLine()){
                    String row = s.nextLine().replace("\t", ";");
                    //System.out.println("registro: " + row);
                    for (int i = 0;i<row.split(";").length;i++){

                        rowToArray.add(i,row.split(";")[i]);

                    }
                  }
                 
            
    }

    @Override
    public boolean hasRow() {
        boolean hasrow = false;
       
            if(s.hasNextLine()){
            
                hasrow = true;
                System.out.println("hay registro");
            
            }else{
            
                hasrow = false;
                System.out.println("no hay registro");
                s.close();
            
            }
        
        return hasrow;
    }

    @Override
    public ArrayList<String> readHeader() {
        
        ArrayList<String> array = new ArrayList<String>();
        
        
                  //System.out.println(txt.getName());
                  String row = s.nextLine().replace("\t", ";");
                  //System.out.println("header: " + row);
                  for (int i = 0;i<row.split(";").length;i++){
                  
                      array.add(i,row.split(";")[i]);
                  
                  }
                 
           
        return array;
    }

    @Override
    void move() {
        
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH.mm");
        Date date = new Date();
        try {
            File afile =FileUtils.getFile(getPath());
            File bfile =FileUtils.getFile("y://Vtex/olds/" + dateFormat.format(date) + "_" +  afile.getName());
            FileUtils.moveFile(afile,bfile);
            
        } catch (IOException ex) {
            Logger.getLogger(ReadTXT.class.getName()).log(Level.SEVERE, null, ex);
        }

    	
    }

    @Override
    void delete() {
       
    }

    @Override
    void close() {
        try{
        System.out.println("cierra archivo " + getPath());
        s.close();
        }catch(NullPointerException e){}
    }
    
    
    
    
}
