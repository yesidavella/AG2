package com.ag2.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public final class EditorPropiedades {

    private String nameArchivo = "ConfigInit.cfg";
    Properties properties = new Properties();
    private static EditorPropiedades editorPropiedades;  
    private EditorPropiedades()
    {
        leerArchivo();
    }   
    
    public static EditorPropiedades getUniqueInstance()
    {
        if(editorPropiedades == null)
        {
            editorPropiedades= new EditorPropiedades(); 
        }            
        return editorPropiedades;       
        
    }        
    
    public  void leerArchivo() {
       
        try 
        {
            FileInputStream fileInputStream = new  FileInputStream(nameArchivo);           
            properties.load(fileInputStream);          
            fileInputStream.close();

        }
        catch (Exception ex)
        {
            //System.out.println("Failed to read from " + nameArchivo + " file. Due to :" + ex.toString());
            
        }

    }
    public void escribir()
    {
        try 
        {
          FileOutputStream fileOutputStream = new  FileOutputStream(nameArchivo);          
          properties.store(fileOutputStream,null);
          fileOutputStream.flush();
          fileOutputStream.close();
            
        } catch (Exception ex) {
            
            //System.out.println("Failed to write from " + nameArchivo + " file. Due to :" + ex.toString());
        }
        
    }        
   
    public String getValorPropiedad(TipoDePropiedadesPhosphorus propiedadePhosphorus)
    {
        return  properties.getProperty(propiedadePhosphorus.getNombrePropiedadPhosphorus()); 
    } 
    public void setValorPropiedad(TipoDePropiedadesPhosphorus propiedadePhosphorus,String valor)
    {
        properties.setProperty(propiedadePhosphorus.getNombrePropiedadPhosphorus(),valor); 
        escribir();
    }
    
   
}
