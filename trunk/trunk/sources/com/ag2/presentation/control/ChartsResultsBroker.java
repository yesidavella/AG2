package com.ag2.presentation.control;

import com.ag2.util.Utils;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChartsResultsBroker implements ViewResultsBrokerChart {

    private Tab tabChartsBrokerResults;
    private ScrollPane scpAllPage;
    private VBox vbxAllPage;
    private HBox hbxPerExecution;
    private BarChart barChart;
    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis;
    private XYChart.Series<String, Number> serieReqRecieved;
    private XYChart.Series<String, Number> serieNoFreeResouce;
    private XYChart.Series<String, Number> serieReqAckSent;
    private XYChart.Series<String, Number> serieSendingFailed;
    private int countPlays = 1;

    public ChartsResultsBroker(Tab tabChartsBrokerResults) {
        this.tabChartsBrokerResults = tabChartsBrokerResults;
        scpAllPage = new ScrollPane();
        scpAllPage.getStyleClass().add("bg-general-container");
        vbxAllPage = new VBox();
        vbxAllPage.setPadding(new Insets(10, 10, 10, 10));
        vbxAllPage.setSpacing(10);
        scpAllPage.setContent(vbxAllPage);
        tabChartsBrokerResults.setContent(scpAllPage);
    }

    public void play() {
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel("Nombre Agendadores");
        yAxis.setLabel("Solicitudes");
        barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setMinHeight(550);
        barChart.setMinWidth(1000);
        barChart.setTitle("Trafico de agendamiento. Ejecución numero:" + countPlays);
        countPlays++;

        hbxPerExecution = new HBox(10);
        hbxPerExecution.getChildren().addAll(barChart);
        hbxPerExecution.getStyleClass().add("boxChart");
        vbxAllPage.getChildren().add(0, hbxPerExecution);

        serieReqRecieved = new XYChart.Series<String, Number>();
        serieReqRecieved.setName("Recibidas");
        serieNoFreeResouce = new XYChart.Series<String, Number>();
        serieNoFreeResouce.setName("No asignadas");
        serieReqAckSent = new XYChart.Series<String, Number>();
        serieReqAckSent.setName("Asignadas Enviadas");
        serieSendingFailed = new XYChart.Series<String, Number>();
        serieSendingFailed.setName("Asignadas No enviadas");

        barChart.getData().addAll(serieReqRecieved, serieNoFreeResouce,
                serieReqAckSent, serieSendingFailed);
    }

    @Override
    public void createBrokerResults(String brokerName, double registrationReceived,
            double reqRecieved, double noFreeResouce, double reqAckSent,
            double sendingFailed, double relativeAckSent) {

        String graficalName = Utils.findGraphicalName(brokerName);
        serieReqRecieved.getData().add(new XYChart.Data<String, Number>(graficalName, reqRecieved));
        serieNoFreeResouce.getData().add(new XYChart.Data<String, Number>(graficalName, noFreeResouce));
        serieReqAckSent.getData().add(new XYChart.Data<String, Number>(graficalName, reqAckSent));
        serieSendingFailed.getData().add(new XYChart.Data<String, Number>(graficalName, sendingFailed));

    }
}
