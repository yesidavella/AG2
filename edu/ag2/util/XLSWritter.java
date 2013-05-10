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
public class XLSWritter {

    private static XLSWritter xLSWritter;
    FileWriter fileWriter = null;
    PrintWriter printWriter = null;

    private XLSWritter() {
        
        try {
            fileWriter = new FileWriter(getURLAndFileName());
            printWriter = new PrintWriter(fileWriter);
        } catch (IOException ex) {
            Logger.getLogger(XLSWritter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static XLSWritter getInstance() {

        if (xLSWritter == null) {
            return xLSWritter = new XLSWritter();
        } else {
            return xLSWritter;
        }
    }

    public String getURLAndFileName() {

        String separator = System.getProperty("file.separator");
        String language = System.getProperty("user.language");
        String desktop;

        if (language.equalsIgnoreCase("es")) {
            desktop = "Desktop";
        } else if (language.equalsIgnoreCase("en")) {
            desktop = "Desktop";
        } else {
            throw new IllegalStateException("El idioma es distinto de espa\u00F1ol o ingles");
        }

        Calendar currentDate = Calendar.getInstance();
        StringBuilder fileName = new StringBuilder();
        
        fileName.append(currentDate.get(Calendar.YEAR));
        fileName.append((currentDate.get(Calendar.MONTH)+1));
        fileName.append(currentDate.get(Calendar.DATE));
        fileName.append(currentDate.get(Calendar.HOUR));
        fileName.append(currentDate.get(Calendar.MINUTE));
        fileName.append(currentDate.get(Calendar.SECOND));
        fileName.append(".xls");
        
        System.out.println(System.getProperty("user.home")+separator+desktop+separator+fileName.toString());
        return System.getProperty("user.home")+separator+desktop+separator+fileName.toString();
    }
    
    public void writteInFile(){
        System.out.println("Escribiendo en archivo");
        printWriter.println("A,aa;aaaaaaaaaaa...");
        printWriter.flush();
    }
    
    public void closeFile(){
        try {
            fileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(XLSWritter.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
