/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.config.serializacion;

import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.Stage;

/**
 *
 * @author Frank
 */
public class Serializador {

    Stage primaryStage;

    public Serializador(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void guardar(GrupoDeDiseno grupoDeDiseno) {
        try {

            FileOutputStream fs = new FileOutputStream("PruebaSerial.ag2");
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(grupoDeDiseno);
            os.close();

        } catch (IOException ex) {
            Logger.getLogger(Serializador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public GrupoDeDiseno cargar()
    {
        try 
        {
            FileInputStream fileInputStream = new FileInputStream("PruebaSerial.ag2");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            GrupoDeDiseno grupoDeDiseno = (GrupoDeDiseno) objectInputStream.readObject();   
            objectInputStream.close();
            return grupoDeDiseno; 
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null; 
    }

}
