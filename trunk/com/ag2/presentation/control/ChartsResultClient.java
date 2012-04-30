/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;

import com.ag2.util.Utils;
import com.sun.javafx.scene.web.skin.ColorPicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Frank
 */
public class ChartsResultClient implements ViewResultsChart {

    private Tab tab;
    private ScrollPane scrollPane;
    private VBox vBoxPlay;
    private VBox vBoxMain;
    private HBox hBoxMainClient;
    private int countPlays = 1;

    public ChartsResultClient(Tab tab) {
        this.tab = tab;
        scrollPane = new ScrollPane();
        vBoxMain = new VBox();
        vBoxMain.setPadding(new Insets(10, 10, 10, 10));
        vBoxMain.setSpacing(10);
        scrollPane.setContent(vBoxMain);
        tab.setContent(scrollPane);
    }

    public void play() {
        vBoxPlay = new VBox();
        hBoxMainClient = new HBox();
        hBoxMainClient.setPadding(new Insets(10, 10, 10, 10));
        Label lblTitle = new Label("Resultados totales clientes - Ejecución N." + countPlays);
        lblTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 18));

        vBoxPlay.getChildren().addAll(lblTitle, hBoxMainClient);
        vBoxPlay.getStyleClass().add("boxChart");
        vBoxPlay.setAlignment(Pos.CENTER);
        vBoxMain.getChildren().add(0,vBoxPlay);
        countPlays++;
    }

    @Override
    public void createClientResult(String clientName, double relativeResultReceive, double relativeJobsNoSent) {
        System.out.println(" ######  " + clientName + " " + relativeResultReceive + "  " + relativeJobsNoSent);

        double relativeNoResultReceive = 100 - relativeResultReceive;
        double relativeJobsSent = 100 - relativeJobsNoSent;


        ObservableList<PieChart.Data> dataRelativeResultReceive = FXCollections.observableArrayList(
                new PieChart.Data("Resultado no recibidos", relativeNoResultReceive),
                new PieChart.Data("Resultado  recibidos", relativeResultReceive));
        PieChart chartRelativeResultReceive = new PieChart(dataRelativeResultReceive);
        chartRelativeResultReceive.setTitle("Resultados recibidos");
        chartRelativeResultReceive.setClockwise(false);


        ObservableList<PieChart.Data> dataRelativeJobsNoSent = FXCollections.observableArrayList(
                new PieChart.Data("Trabajos no enviados", relativeJobsNoSent),
                new PieChart.Data("Trabajos enviados", relativeJobsSent));
        PieChart chartRelativeJobsNoSent = new PieChart(dataRelativeJobsNoSent);
        chartRelativeJobsNoSent.setTitle("Trabajos enviados");
        chartRelativeJobsNoSent.setClockwise(false);

        
        Label lblTitleClient = new Label("Resultados cliente  "+Utils.findGraphicalName(clientName)) ;
        lblTitleClient.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        
        VBox vBoxClient = new VBox();
        HBox hBoxClient = new HBox();
        vBoxClient.setAlignment(Pos.CENTER);
        vBoxClient.getStyleClass().add("boxChart2");
        
        vBoxClient.setSpacing(10);
        vBoxClient.setPadding(new Insets(10, 10, 10, 10));
        hBoxClient.getChildren().addAll(chartRelativeResultReceive, chartRelativeJobsNoSent);
        vBoxClient.getChildren().addAll(lblTitleClient, hBoxClient);
        
        hBoxClient.setMaxHeight(270);
        hBoxMainClient.getChildren().addAll(vBoxClient);


    }
}
