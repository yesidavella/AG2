package com.ag2.config;

import com.ag2.util.ResourcesPath;
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
            FileInputStream fileInputStream = new FileInputStream(ResourcesPath.ABS_PATH_CONFIG_AG2 + FILE_NAME);
            properties.load(fileInputStream);
            fileInputStream.close();

        } catch (Exception ex) {
            System.out.println("Failed to read from " + ResourcesPath.ABS_PATH_CONFIG_AG2 + FILE_NAME + " file. Due to :" + ex.toString());
        }

    }

    public void writeFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(ResourcesPath.ABS_PATH_CONFIG_AG2 + FILE_NAME);
            properties.store(fileOutputStream, null);
            fileOutputStream.flush();
            fileOutputStream.close();


        } catch (Exception ex) {
            //System.out.println("Failed to write from " + nameArchivo + " file. Due to :" + ex.toString());
        }

    }

    public String getPropertyValue(PropertyPhosphorusTypeEnum propertyPhosphorusTypeEnum) {
        return properties.getProperty(propertyPhosphorusTypeEnum.getPhosphorusPropertyName());
    }

    public void setPropertyValue(PropertyPhosphorusTypeEnum propiedadePhosphorus, String valor) {
        properties.setProperty(propiedadePhosphorus.getPhosphorusPropertyName(), valor);
        writeFile();
    }

    public Properties getProperties() {
        return properties;
    }
}