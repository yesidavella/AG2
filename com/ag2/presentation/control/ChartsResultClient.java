package com.ag2.presentation.control;

import com.ag2.util.Utils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ChartsResultClient implements ViewResultsClientChart {

    private Tab tab;
    private ScrollPane scrollPane;
    private VBox vbxPlay;
    private VBox vbxAllPage;
    private HBox hBoxMainClient;
    private int countPlays = 1;
    private BarChart<String, Number> barChart;
    private XYChart.Series<String, Number> series1;
    private XYChart.Series<String, Number> series2;
    private XYChart.Series<String, Number> series3;
    private XYChart.Series<String, Number> series4;
    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis;

    public ChartsResultClient(Tab tab) {
        this.tab = tab;
        scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("bg-general-container");
        vbxAllPage = new VBox();
        vbxAllPage.setPadding(new Insets(10, 10, 10, 10));
        vbxAllPage.setSpacing(10);
        scrollPane.setContent(vbxAllPage);
        tab.setContent(scrollPane);
    }

    public void play() {
        vbxPlay = new VBox();
        hBoxMainClient = new HBox();
        hBoxMainClient.setPadding(new Insets(10, 10, 10, 10));
        Label lblTitle = new Label("Resultados totales clientes - Ejecución N." + countPlays);
        lblTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 18));

        vbxPlay.getChildren().addAll(lblTitle, hBoxMainClient);
        vbxPlay.getStyleClass().add("boxChart");
        vbxPlay.setAlignment(Pos.CENTER);
        vbxAllPage.getChildren().add(0, vbxPlay);

        countPlays++;

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();

        barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle("Grafico de clientes solicitudes/trabajos");
        barChart.setMinHeight(550);
        barChart.setMinWidth(1000);

        xAxis.setLabel("Clientes");
        yAxis.setLabel("Solicitudes/Trabajos");

        series1 = new XYChart.Series<String, Number>();
        series1.setName("Solicitudes creadas");
        series2 = new XYChart.Series<String, Number>();
        series2.setName("Solicitudes enviadas");
        series3 = new XYChart.Series<String, Number>();
        series3.setName("Trabajos enviados");
        series4 = new XYChart.Series<String, Number>();
        series4.setName("Resultados recibidos");
        barChart.getData().addAll(series1, series2, series3, series4);

        VBox vBoxClient = new VBox();
        HBox hBoxClient = new HBox();
        vBoxClient.setAlignment(Pos.CENTER);
        vBoxClient.getStyleClass().add("boxChart2");
        vBoxClient.getChildren().addAll(hBoxClient);
        vBoxClient.getChildren().add(barChart);
        hBoxMainClient.getChildren().addAll(vBoxClient);

    }

    @Override
    public void createClientResult(final String clientName,
            final double requestSent,
            final double jobNosent,
            final double jobSent,
            final double jobReceive) {

        String graficalname = Utils.findGraphicalName(clientName);
        series1.getData().add(new XYChart.Data<String, Number>(graficalname, requestSent));
        series2.getData().add(new XYChart.Data<String, Number>(graficalname, jobNosent));
        series3.getData().add(new XYChart.Data<String, Number>(graficalname, jobSent));
        series4.getData().add(new XYChart.Data<String, Number>(graficalname, jobReceive));

        barChart.setMinWidth(barChart.getMinWidth() + 240);

    }
}
