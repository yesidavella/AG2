/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;

import com.ag2.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ChartsResultResource implements ViewResultsResourceChart {

    private transient Tab tab;
    private transient ScrollPane scrollPane;
    private transient VBox vBoxPlay;
    private transient VBox vBoxMain;
    private transient HBox hBoxMainResource;
    private transient int countPlays = 1;
    private transient BarChart<String, Number> barChart;
    private transient XYChart.Series<String, Number> series1;
    private transient XYChart.Series<String, Number> series2;
//    private transient XYChart.Series<String, Number> series3;
//    private transient XYChart.Series<String, Number> series4;
    private transient CategoryAxis xAxis = new CategoryAxis();
    private transient NumberAxis yAxis;
    private   PieChart pieChartRecieve;
    private   PieChart pieChartSent;
    private ObservableList<PieChart.Data> pieChartDataReceive;
    private ObservableList<PieChart.Data> pieChartDataSent;

    public ChartsResultResource(Tab tab) {
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
        hBoxMainResource = new HBox();
        hBoxMainResource.setPadding(new Insets(10, 10, 10, 10));
        Label lblTitle = new Label("Resultados totales recursos - Ejecución N." + countPlays);
        lblTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 18));

        vBoxPlay.getChildren().addAll(lblTitle, hBoxMainResource);
        vBoxPlay.getStyleClass().add("boxChart");
        vBoxPlay.setAlignment(Pos.CENTER);
        vBoxMain.getChildren().add(0, vBoxPlay);

        countPlays++;

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();

        barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle("Grafico de trabajos recibidos  y repustas enviadas");
        barChart.setMinHeight(550);
        barChart.setMinWidth(100);

        xAxis.setLabel("Recursos");
        yAxis.setLabel("Trabajos");

        series1 = new XYChart.Series<String, Number>();
        series1.setName("Trabajos recibidos");
        series2 = new XYChart.Series<String, Number>();
        series2.setName("Repuestas enviadas");
//        series3 = new XYChart.Series<String, Number>();
//        series3.setName("Trabajos enviados");
//        series4 = new XYChart.Series<String, Number>();
//        series4.setName("Resultados recibidos");
        barChart.getData().addAll(series1, series2);

        VBox vBoxBar = new VBox();
        HBox hBoxClient = new HBox();
        vBoxBar.setAlignment(Pos.CENTER);
        vBoxBar.getStyleClass().add("boxChart2");
        vBoxBar.getChildren().addAll(hBoxClient);
        vBoxBar.getChildren().add(barChart);
        
        pieChartDataReceive = FXCollections.observableArrayList();
        pieChartDataSent = FXCollections.observableArrayList();
        
        VBox vBoxPieReceive = new VBox();
        vBoxPieReceive.getStyleClass().add("boxChart2");
        pieChartRecieve = new PieChart(pieChartDataReceive);   
        pieChartRecieve.setTitle("Trabajos recibidos");
        vBoxPieReceive.getChildren().addAll(pieChartRecieve);
        
        VBox vBoxPieSent = new VBox();
        vBoxPieSent.getStyleClass().add("boxChart2");        
        pieChartSent = new PieChart(pieChartDataSent);        
        pieChartSent.setTitle("Resultados enviados");
        vBoxPieSent.getChildren().addAll(pieChartSent);
        
        hBoxMainResource.getChildren().addAll(vBoxPieReceive,vBoxPieSent, vBoxBar);

    }

    @Override
    public void createResourceResult(final String resourceName, final double jobReceive, final double resultSent) {

        String graficalname = Utils.findGraphicalName(resourceName);
        series1.getData().add(new XYChart.Data<String, Number>(graficalname, jobReceive));
        series2.getData().add(new XYChart.Data<String, Number>(graficalname, resultSent));
        barChart.setMinWidth(barChart.getMinWidth() + 120);
        
        pieChartDataReceive.add(    new PieChart.Data(graficalname, jobReceive));
        pieChartDataSent.add(    new PieChart.Data(graficalname, resultSent));

    }
}
