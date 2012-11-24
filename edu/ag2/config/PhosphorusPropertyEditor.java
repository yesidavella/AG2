package edu.ag2.config;

import java.util.Properties;
import simbase.SimulationInstance;

public final class PhosphorusPropertyEditor {

 

    private static PhosphorusPropertyEditor phosphorusPropertyEditor;

    private PhosphorusPropertyEditor() 
    {
        
  
        
    }

    public static PhosphorusPropertyEditor getUniqueInstance() {
        if (phosphorusPropertyEditor == null) {
            phosphorusPropertyEditor = new PhosphorusPropertyEditor();
        }
        return phosphorusPropertyEditor;
    }



    public String getPropertyValue(PropertyPhosphorusTypeEnum propertyPhosphorusTypeEnum) 
    {         
        System.out.println("-"+propertyPhosphorusTypeEnum.getPhosphorusPropertyName()+"-");
        return SimulationInstance.getConfiguration().getProperty(propertyPhosphorusTypeEnum.getPhosphorusPropertyName());
    }

    public void setPropertyValue(PropertyPhosphorusTypeEnum propiedadePhosphorus, String value) {
        SimulationInstance.getConfiguration().setProperty(propiedadePhosphorus.getPhosphorusPropertyName(), value);
        SimulationInstance.getConfiguration().save();
//        writeFile();
    }

    public Properties getProperties() {
        return SimulationInstance.getConfiguration();
    }
}