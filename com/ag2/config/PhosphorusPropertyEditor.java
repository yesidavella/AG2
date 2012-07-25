package com.ag2.config;

import com.ag2.util.Utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public final class PhosphorusPropertyEditor {

    private final String FILE_NAME = "ConfigInitAG2.cfg";
    private Properties properties = new Properties();
    private static PhosphorusPropertyEditor phosphorusPropertyEditor;

    private PhosphorusPropertyEditor() {
        readFile();
    }

    public static PhosphorusPropertyEditor getUniqueInstance() {
        if (phosphorusPropertyEditor == null) {
            phosphorusPropertyEditor = new PhosphorusPropertyEditor();
        }
        return phosphorusPropertyEditor;
    }

    public void readFile() {

        try {
            FileInputStream fileInputStream = new FileInputStream(Utils.ABS_PATH_CONFIG_AG2 + FILE_NAME);
            properties.load(fileInputStream);
            fileInputStream.close();

        } catch (Exception ex) {
            System.out.println("Failed to read from " + Utils.ABS_PATH_CONFIG_AG2 + FILE_NAME + " file. Due to :" + ex.toString());
        }
    }

    public void writeFile() {
        
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Utils.ABS_PATH_CONFIG_AG2 + FILE_NAME);
            properties.store(fileOutputStream, null);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception ex) {
            System.out.println("Failed to write from " + Utils.ABS_PATH_CONFIG_AG2 + FILE_NAME + " file. Due to :" + ex.toString());
        }
    }

    public String getPropertyValue(PropertyPhosphorusTypeEnum propertyPhosphorusTypeEnum) {
        return properties.getProperty(propertyPhosphorusTypeEnum.getPhosphorusPropertyName());
    }

    public void setPropertyValue(PropertyPhosphorusTypeEnum propiedadePhosphorus, String value) {
        properties.setProperty(propiedadePhosphorus.getPhosphorusPropertyName(), value);
        writeFile();
    }

    public Properties getProperties() {
        return properties;
    }
}