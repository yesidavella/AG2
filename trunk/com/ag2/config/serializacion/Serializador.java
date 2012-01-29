/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.config.serializacion;

import com.ag2.presentacion.Main;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author Frank
 */
public class Serializador {

    Stage primaryStage;
    FileChooser fileChooser = new FileChooser();
    Main main;   
    public Serializador(Main main, final Stage primaryStage) 
    {
        this.main = main ; 
        this.primaryStage = primaryStage;
         fileChooser.getExtensionFilters().add(new ExtensionFilter("AG2 Document (*.ag2)", "*.ag2"));
    }

    public void guardar() {
        try 
        {           

            FileOutputStream fileOutputStream = new FileOutputStream(fileChooser.showSaveDialog(primaryStage));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(main);
            objectOutputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(Serializador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Main cargar() {
        try 
        {

            FileInputStream fileInputStream = new FileInputStream(fileChooser.showOpenDialog(primaryStage));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Main main = (Main) objectInputStream.readObject();
            objectInputStream.close();
            return main;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
