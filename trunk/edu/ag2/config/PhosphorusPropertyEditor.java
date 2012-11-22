package edu.ag2.config;

import edu.ag2.util.Utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public final class PhosphorusPropertyEditor {

    private final String FILE_NAME = "ConfigInitAG2.cfg";
    private Properties properties = new Properties();
    private static PhosphorusPropertyEditor phosphorusPropertyEditor;

    private PhosphorusPropertyEditor() 
    {
        
        properties.setProperty("stopEventOffSetTime", "100");
        properties.setProperty("defaultWavelengths", "253");
        properties.setProperty("maxDelay", "10");
        properties.setProperty("clientTrafficPriority", "5");
        properties.setProperty("defaultFlopSize", "500");
        properties.setProperty("defaultResultSize", "200");
        properties.setProperty("defaultDataSize", "10");
        properties.setProperty("routedViaJUNG", "true");
        properties.setProperty("defaultCPUCount", "3");
        properties.setProperty("defaultJobIAT", "200");
        properties.setProperty("output", "true");
        properties.setProperty("allocateWavelenght", "0.01");
        properties.setProperty("outputFileName", "resultado.html");
        properties.setProperty("defaultQueueSize", "20");
        properties.setProperty("simulationTime", "1000000");
        properties.setProperty("switchingSpeed", "100000");
        properties.setProperty("findCommonWavelenght", "0.03");
        properties.setProperty("OCS_SwitchingDelay", "0.01"); 
        properties.setProperty("confirmOCSDelay", "0.0001");
        properties.setProperty("ACKsize", "0.1"); 
        properties.setProperty("OCSSetupHandleTime", "0.5");
        properties.setProperty("linkSpeed", "100");
        properties.setProperty("OBSHandleTime", "10");
        properties.setProperty("defaultCpuCapacity", "100");
        
    }

    public static PhosphorusPropertyEditor getUniqueInstance() {
        if (phosphorusPropertyEditor == null) {
            phosphorusPropertyEditor = new PhosphorusPropertyEditor();
        }
        return phosphorusPropertyEditor;
    }

//    public void readFile() {
//
//        try {
//         
//            FileInputStream fileInputStream = new FileInputStream(Utils.ABS_PATH_CONFIG_AG2 + FILE_NAME);
//            properties.load(fileInputStream);
//            fileInputStream.close();
//
//        } catch (Exception ex) {
//            //System.out.println("Failed to read from " + Utils.ABS_PATH_CONFIG_AG2 + FILE_NAME + " file. Due to :" + ex.toString());
//        }
//    }
//
//    public void writeFile() {
//        
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(Utils.ABS_PATH_CONFIG_AG2 + FILE_NAME);
//            properties.store(fileOutputStream, null);
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (Exception ex) {
//            //System.out.println("Failed to write from " + Utils.ABS_PATH_CONFIG_AG2 + FILE_NAME + " file. Due to :" + ex.toString());
//        }
//    }

    public String getPropertyValue(PropertyPhosphorusTypeEnum propertyPhosphorusTypeEnum) {
        return properties.getProperty(propertyPhosphorusTypeEnum.getPhosphorusPropertyName());
    }

    public void setPropertyValue(PropertyPhosphorusTypeEnum propiedadePhosphorus, String value) {
        properties.setProperty(propiedadePhosphorus.getPhosphorusPropertyName(), value);
//        writeFile();
    }

    public Properties getProperties() {
        return properties;
    }
}