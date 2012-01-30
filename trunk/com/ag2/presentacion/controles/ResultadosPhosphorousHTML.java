/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion.controles;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 *
 * @author Frank
 */
public class ResultadosPhosphorousHTML {

    private ImageView ivIzqFin = new ImageView(new Image(getClass().getResourceAsStream("../../../../recursos/imagenes/mini_izquierda_fin.png")));
    private ImageView ivIzq = new ImageView(new Image(getClass().getResourceAsStream("../../../../recursos/imagenes/mini_izquierda.png")));
    private ImageView ivDer = new ImageView(new Image(getClass().getResourceAsStream("../../../../recursos/imagenes/mini_derecha.png")));
    private ImageView ivDerFin = new ImageView(new Image(getClass().getResourceAsStream("../../../../recursos/imagenes/mini_derecha_fin.png")));
    private Button btnIzqFin = new Button();
    private Button btnIzq = new Button();
    private Button btnDer = new Button();
    private Button btnDerFin = new Button();   
    private TextField txtPagina = new TextField();
    private String CARPETA_RESULTADOS = "Results_HTML";
    private int paginasTotales = 0;
    private int paginaActual = 0;
    private String[] archivosHTML;
    WebView browser = new WebView();
    WebEngine webEngine = browser.getEngine();
    String directorioResultados;
    VBox vBox = new VBox();
    HBox hBox = new HBox();
    int countExecutions = 0; 

    public void lookToNextExecution()
    {
        countExecutions++; 
    }
    public ResultadosPhosphorousHTML(final Tab tab)
    {
        
        btnIzqFin.setGraphic(ivIzqFin);
        btnIzq.setGraphic(ivIzq);
        btnDer.setGraphic(ivDer);
        btnDerFin.setGraphic(ivDerFin);

        btnIzqFin.setMaxSize(20, 18);
        btnIzq.setMaxSize(20, 18);
        btnDer.setMaxSize(20, 18);
        btnDerFin.setMaxSize(20, 18);
        hBox.getChildren().addAll(btnIzqFin, btnIzq, txtPagina, btnDer, btnDerFin);

        hBox.setAlignment(Pos.CENTER);

        //loadFilesHTMLs(tab);

        tab.setOnSelectionChanged(new EventHandler<Event>() {

            public void handle(Event t) {
                if (tab.isSelected()) {
                    loadFilesHTMLs(tab);
                }
            }
        });

    }

   
    private void loadFilesHTMLs(Tab tab) {


        String directorioActual = new File("").getAbsolutePath();
        directorioResultados = directorioActual + File.separator + CARPETA_RESULTADOS+"_"+countExecutions;
        File file = new File(directorioResultados);

        if (file.exists()) {
            archivosHTML = file.list();
            paginasTotales = archivosHTML.length;
        }

        txtPagina.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                txtPagina.setText("");
            }
        });


        txtPagina.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                String valorPagina = txtPagina.getText();
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(valorPagina);

                if (m.matches()) {
                    paginaActual = Integer.parseInt(txtPagina.getText());

                    if (paginaActual > paginasTotales || paginaActual < 1) {
                        paginaActual = 1;
                        txtPagina.setText(paginaActual + "/" + paginasTotales);
                        webEngine.load("file:///" + directorioResultados + File.separator + archivosHTML[paginaActual - 1]);
                        deshabilitarBotonesNavegacion();
                    } else {
                        txtPagina.setText(paginaActual + "/" + paginasTotales);
                        webEngine.load("file:///" + directorioResultados + File.separator + archivosHTML[paginaActual - 1]);
                        deshabilitarBotonesNavegacion();
                    }
                }
            }
        });

        txtPagina.setMaxWidth(50);


        if (archivosHTML != null && archivosHTML.length > 1) {
            paginaActual = 1;
            txtPagina.setText(paginaActual + "/" + paginasTotales);
            webEngine.load("file:///" + directorioResultados + File.separator + archivosHTML[0]);
            if (!vBox.getChildren().contains(browser)) {
                vBox.getChildren().addAll(browser);
            }
            if (!vBox.getChildren().contains(hBox)) {
                vBox.getChildren().addAll(hBox);
            }
        } else {
            txtPagina.setText("--");
            if (!vBox.getChildren().contains(hBox)) {
                vBox.getChildren().addAll(hBox);
            }
            btnDer.setDisable(true);
            btnDerFin.setDisable(true);
        }

        tab.setContent(vBox);
        establecerEventoBtnIzqFin(btnIzqFin);
        establecerEventoBtnDerFin(btnDerFin);
        establecerEventoBtnIzq(btnIzq);
        establecerEventoBtnDer(btnDer);

        btnIzq.setDisable(true);
        btnIzqFin.setDisable(true);
    }

    private void deshabilitarBotonesNavegacion() {
        if (paginaActual == 1) {
            btnIzq.setDisable(true);
            btnIzqFin.setDisable(true);
        } else {
            btnIzq.setDisable(false);
            btnIzqFin.setDisable(false);
        }

        if (paginaActual == paginasTotales) {
            btnDer.setDisable(true);
            btnDerFin.setDisable(true);
        } else {
            btnDer.setDisable(false);
            btnDerFin.setDisable(false);
        }

    }

    private void establecerEventoBtnIzq(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {

                paginaActual -= 1;
                txtPagina.setText(paginaActual + "/" + paginasTotales);
                webEngine.load("file:///" + directorioResultados + File.separator + archivosHTML[paginaActual - 1]);
                deshabilitarBotonesNavegacion();
            }
        });

    }

    private void establecerEventoBtnDer(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {

                paginaActual += 1;
                txtPagina.setText(paginaActual + "/" + paginasTotales);
                webEngine.load("file:///" + directorioResultados + File.separator + archivosHTML[paginaActual - 1]);
                deshabilitarBotonesNavegacion();

            }
        });

    }

    private void establecerEventoBtnIzqFin(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                paginaActual = 1;
                txtPagina.setText(paginaActual + "/" + paginasTotales);
                webEngine.load("file:///" + directorioResultados + File.separator + archivosHTML[0]);
                deshabilitarBotonesNavegacion();
            }
        });

    }

    private void establecerEventoBtnDerFin(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                paginaActual = archivosHTML.length;
                txtPagina.setText(archivosHTML.length + "/" + paginasTotales);
                webEngine.load("file:///" + directorioResultados + File.separator + archivosHTML[archivosHTML.length - 1]);
                deshabilitarBotonesNavegacion();
            }
        });

    }
}
