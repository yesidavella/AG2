/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.config.serializacion;

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

    public Serializador(final Stage primaryStage) {
        this.primaryStage = primaryStage;
         fileChooser.getExtensionFilters().add(new ExtensionFilter("AG2 Document (*.ag2)", "*.ag2"));
    }

    public void guardar(GrupoDeDiseno grupoDeDiseno) {
        try {

           

            FileOutputStream fileOutputStream = new FileOutputStream(fileChooser.showSaveDialog(primaryStage));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(grupoDeDiseno);
            objectOutputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(Serializador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public GrupoDeDiseno cargar() {
        try 
        {

            FileInputStream fileInputStream = new FileInputStream(fileChooser.showOpenDialog(primaryStage));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            GrupoDeDiseno grupoDeDiseno = (GrupoDeDiseno) objectInputStream.readObject();
            objectInputStream.close();
            return grupoDeDiseno;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
