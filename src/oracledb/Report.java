package oracledb;

/**
 *
 * @author Nicolas Scordamaglia
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
                form = "Proponi Product Play";
                break;

            case "ptpys":
                form = "Proponi provider Planes y Servicios";
                break;

            case "pab":
                form = "Proponi Product BAF";
                break;

            case "pte":
                form = "Proponi provider Equipos";
                break;

            case "vtex":
                form = "VTEX";
                break;

            case "th":
                form = "TFORM Product DIGITAL";
                break;

            case "ta6mg":
                form = "TFORM Product 6MB";
                break;

            case "ta20mg":
                form = "TFORM Product 20MB";
                break;

        }

        return form;
    }

    private static class ReportHolder {

        private static final Report INSTANCE = new Report();
    }
}
