/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.config;

import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;

/**
 *
 * @author Frank
 */
public class Serializador 
{
    Stage primaryStage; 
    public Serializador(final Stage primaryStage)
    {
        this.primaryStage= primaryStage;         
    }
    public void guardar(GrupoDeDiseno  grupoDeDiseno) 
    {
        try 
        {
          
            FileOutputStream fs = new FileOutputStream("PruebaSerial.ag2");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject( grupoDeDiseno); 
            os.close();
            
        } catch (IOException ex) 
        {
            Logger.getLogger(Serializador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
            
}