/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.config.serialization;

import com.ag2.presentacion.Main;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author Frank
 */
public class UtilSerializator {

    private Stage primaryStage;
    private FileChooser fileChooser = new FileChooser();
    private Main main;
    private static String NAME_BASE_FILE = "Base.ag2";

    public UtilSerializator(Main main, final Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
        fileChooser.getExtensionFilters().add(new ExtensionFilter("AG2 Document (*.ag2)", "*.ag2"));
    }

    public void OpenDialogToSave() {
        try {
            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(main);
                objectOutputStream.flush();
                objectOutputStream.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(UtilSerializator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Main OpenDialogToLoad() {
        File file = fileChooser.showOpenDialog(primaryStage);
        try {

            if (file != null) {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                Main mainToLoad = (Main) objectInputStream.readObject();
                objectInputStream.close();
                return mainToLoad;
            }
            return null;

        } catch (InvalidClassException classException) {
            JOptionPane.showMessageDialog(null, "La versión de archivo  no es compatible con el simulador ", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Main loadFileBaseSimulation() {
        try {

            FileInputStream fileInputStream = new FileInputStream(NAME_BASE_FILE);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Main mainToLoad = (Main) objectInputStream.readObject();

            objectInputStream.close();
            return mainToLoad;

        } catch (InvalidClassException classException) {
            JOptionPane.showMessageDialog(null, "La versión de archivo  no es compatible con el simulador ", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
