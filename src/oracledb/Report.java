/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracledb;

/**
 *
 * @author u189299
 */
public class Report {
    String msj;
    int deleted;

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
    
    

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }
    
    
    private Report() {
        
        
    }
    
    public static Report getInstance() {
        return ReportHolder.INSTANCE;
    }

    String getForm(String prop) {
        String form = null;
        switch(prop){
        
            case "pap":
                form = "Proponi Arnet Play";
                break;
            
            case "ptpys":
                form = "Proponi Telecom Planes y Servicios";
                break;
            
            case "pab":
                form = "Proponi Arnet BAF";
                break;
                
            case "pte":
                form = "Proponi Telecom Equipos";
                break;    
             
            case "vtex":
                form = "VTEX";
                break;
                
            case "th":
                form = "TFORM ARGENTINA DIGITAL";
                break;
                
            case "ta6mg":
                form = "TFORM ARNET 6MB";
                break;
                
            case "ta20mg":
                form = "TFORM ARNET 20MB";
                break;    
        
        }
        
        return form;
    }
    
    private static class ReportHolder {

        private static final Report INSTANCE = new Report();
    }
}
