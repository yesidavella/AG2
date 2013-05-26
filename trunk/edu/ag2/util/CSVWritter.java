package edu.ag2.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author AG2 team
 */
public class CSVWritter {

    FileWriter fileWriter = null;
    PrintWriter printWriter = null;

    public CSVWritter() {
        
        try {
            fileWriter = new FileWriter(getURLAndFileName());
            printWriter = new PrintWriter(fileWriter);
            printHeaderFile();
        } catch (IOException ex) {
            Logger.getLogger(CSVWritter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getURLAndFileName() {
        
        String osName = System.getProperty("os.name").toLowerCase();
        String separator = System.getProperty("file.separator");
        String desktop;

        if(osName.indexOf("win") >= 0){
            desktop = "Desktop";
        }else if( osName.indexOf("nix") >= 0 || osName.indexOf("nux") >= 0 || osName.indexOf("aix") > 0 ){
            desktop = getLanguage();
        }else if( osName.indexOf("mac") >= 0 ){
            desktop = getLanguage();
        }else{
            throw new IllegalStateException("El SO no es Win, Mac o Linux, no se puede generar el archivo csv.");
        }

        Calendar currentDate = Calendar.getInstance();
        StringBuilder fileName = new StringBuilder();
        
        fileName.append(currentDate.get(Calendar.YEAR));
        fileName.append(getTwoDigits(currentDate.get(Calendar.MONTH)+1) );
        fileName.append(getTwoDigits(currentDate.get(Calendar.DATE)));
        fileName.append("-");
        fileName.append(getTwoDigits( currentDate.get(Calendar.HOUR)));
        fileName.append(getTwoDigits(currentDate.get(Calendar.MINUTE)));
        fileName.append(getTwoDigits(currentDate.get(Calendar.SECOND)));
        fileName.append(".csv");
        
//        System.out.println(System.getProperty("user.home")+separator+desktop+separator+fileName.toString());
        return System.getProperty("user.home")+separator+desktop+separator+fileName.toString();
    }
    
    public void writteInFile(Object... valores)
    {
        String lineToPrint = ""; 
        String separador = ""; 
        for(Object valor: valores)
        {
            lineToPrint+=separador+String.valueOf( valor); 
             separador = ";"; 
        }
        
//        System.out.println("Escribiendo en archivo");
        printWriter.println(lineToPrint);
        printWriter.flush();
    }
    
    private String getTwoDigits(int value){
        String stringValue = String.valueOf(value);
        return (stringValue.length()<2)?"0"+stringValue:stringValue;
    }
    
    public void closeFile(){
        try {
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(CSVWritter.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

    private String getLanguage() throws IllegalStateException {
        
        String language = System.getProperty("user.language");
        
        if (language.equalsIgnoreCase("es")) {
            return "Escritorio";
        } else if (language.equalsIgnoreCase("en")) {
            return "Desktop";
        } else {
            throw new IllegalStateException("El idioma del SO es distinto de espa\u00F1ol o ingles");
        }
    }

    private void printHeaderFile() {
        StringBuffer headerFile = new StringBuffer();
        headerFile.append("# Simulacion;Cx (Indep.Topo);Cy(Cfind);%Grilla;");
        headerFile.append("Cliente1;Cliente2;Cliente3;Cliente4;Cliente5;");
        headerFile.append("Cliente6;Cliente7;Cliente8;Cliente9;Cliente10;");
        headerFile.append("#OCS final simulacion;");
        writteInFile(headerFile);
    }
}