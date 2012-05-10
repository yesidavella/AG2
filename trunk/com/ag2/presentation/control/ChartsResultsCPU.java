/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;

import Grid.Entity;
import com.ag2.controller.DataChartResourceController;
import com.ag2.controller.MatchCoupleObjectContainer;
import com.ag2.presentation.design.ClientGraphNode;
import com.ag2.presentation.design.GraphNode;
import com.ag2.presentation.design.ResourceGraphNode;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 *
 * @author Frank
 */
public class ChartsResultsCPU {

    private Tab tab;
    private VBox vBox;
    private transient Timeline time = new Timeline();
    private ScrollPane scrollPane;
    private LineChart<Number, Number> lineChart;    
    private HashMap<ResourceGraphNode, XYChart.Series<Number, Number>> relationResourceSerie = new HashMap<ResourceGraphNode, XYChart.Series<Number, Number>>();
    private DataChartResourceController dataChartResourceController;
//    private boolean loadResources = true;
    private int countClients = 0;
    private HashMap<ResourceGraphNode, UtilSerieAverage> relationResourceDataAverage;
    private int countPlays = 1;

    public ChartsResultsCPU(Tab tab) {
        this.tab = tab;
        scrollPane = new ScrollPane();
        dataChartResourceController = new DataChartResourceController();
        vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(5);
        vBox.setFillWidth(true);
        scrollPane.setContent(vBox);
        tab.setContent(scrollPane);
    }

    protected void createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setMinWidth(1100);

        lineChart.setTitle("Ocupación de CPU del cluster - Ejecución N." + countPlays);
        xAxis.setLabel("Tiempo de simulacion");
        yAxis.setLabel("Porcentaje de ocupación ");
        countPlays++;

        relationResourceSerie = new  HashMap<ResourceGraphNode, XYChart.Series<Number, Number>>();
        relationResourceDataAverage = new HashMap<ResourceGraphNode, UtilSerieAverage>();
        
        for (GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {
            if (graphNode instanceof ResourceGraphNode) {
                XYChart.Series<Number, Number> serie = new XYChart.Series<Number, Number>();
                serie.setName(graphNode.toString());
                serie.getData().add(new XYChart.Data<Number, Number>(0, 0));
                lineChart.getData().addAll(serie);

                relationResourceSerie.put((ResourceGraphNode) graphNode, serie);
                relationResourceDataAverage.put((ResourceGraphNode) graphNode, new UtilSerieAverage());
            }
            if (graphNode instanceof ClientGraphNode) {
                countClients++;
            }
        }


        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {


                for (ResourceGraphNode resourceGraphNode : relationResourceSerie.keySet()) {

                    dataChartResourceController.loadDataChartResourceCPU(resourceGraphNode);
                    UtilSerieAverage utilSerieAverage = relationResourceDataAverage.get(resourceGraphNode);

                    if (dataChartResourceController.getTime() != 0) {
                        utilSerieAverage.countAverage++;
                        utilSerieAverage.timeAverage += dataChartResourceController.getTime();
                        utilSerieAverage.valueAverage += dataChartResourceController.getValue1();
                        if (utilSerieAverage.countAverage >= 5 * countClients) {

                            XYChart.Series<Number, Number> serie = relationResourceSerie.get(resourceGraphNode);
                            serie.getData().add(new XYChart.Data<Number, Number>(utilSerieAverage.timeAverage / utilSerieAverage.countAverage, utilSerieAverage.valueAverage / utilSerieAverage.countAverage));
                            utilSerieAverage.timeAverage = 0;
                            utilSerieAverage.valueAverage = 0;
                            utilSerieAverage.countAverage = 0;
                            utilSerieAverage.lastData = true;
                        } else {
                            utilSerieAverage.lastData = false;
                        }

                    }

                }
            }
        });
        time.getKeyFrames().add(keyFrame);
        time.setCycleCount(Timeline.INDEFINITE);

        GridPane gridPane = loadInfoCPUResource();

        HBox hBox = new HBox();
        hBox.getStyleClass().add("boxChart");
        hBox.setFillHeight(true);
        lineChart.setEffect(null);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(5, 5, 5, 5));
        HBox.setHgrow(gridPane, Priority.ALWAYS);
        hBox.getChildren().addAll(gridPane, lineChart);
        hBox.setMinWidth(1200);
        vBox.getChildren().add(0, hBox);
    }

    private GridPane loadInfoCPUResource() {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(2);
        gridPane.setHgap(4);

        gridPane.setAlignment(Pos.CENTER);


        Label lblNameTitle = new Label("Cluster");
        Label lblcapacityCPUTitle = new Label("Flops C/U");
        Label lblcountCPUTitle = new Label("N.CPU");

        lblNameTitle.setTextAlignment(TextAlignment.CENTER);
        lblcapacityCPUTitle.setTextAlignment(TextAlignment.CENTER);
        lblcountCPUTitle.setTextAlignment(TextAlignment.CENTER);

        lblNameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        lblcapacityCPUTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        lblcountCPUTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));



        GridPane.setConstraints(lblNameTitle, 0, 0);
        GridPane.setHalignment(lblNameTitle, HPos.LEFT);

        GridPane.setConstraints(lblcountCPUTitle, 1, 0);
        GridPane.setHalignment(lblcountCPUTitle, HPos.CENTER);

        GridPane.setConstraints(lblcapacityCPUTitle, 2, 0);
        GridPane.setHalignment(lblcapacityCPUTitle, HPos.CENTER);

        gridPane.getChildren().addAll(lblNameTitle, lblcountCPUTitle, lblcapacityCPUTitle);
        int row = 1;

        for (ResourceGraphNode resourceGraphNode : relationResourceSerie.keySet()) {
            dataChartResourceController.loadInfoCPUResouce(resourceGraphNode);
            String name = resourceGraphNode.getName();
            String capacityCPU = String.valueOf(dataChartResourceController.getCapacityCPU());
            String countCPU = String.valueOf(dataChartResourceController.getCountCPU());

            Label lblName = new Label(name);
            Label lblcapacityCPU = new Label(capacityCPU);
            Label lblcountCPU = new Label(countCPU);

            lblName.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
            lblcapacityCPU.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
            lblcountCPU.setFont(Font.font("Arial", FontWeight.NORMAL, 10));

            lblName.setTextAlignment(TextAlignment.CENTER);
            lblcapacityCPU.setTextAlignment(TextAlignment.CENTER);
            lblcountCPU.setTextAlignment(TextAlignment.CENTER);

            GridPane.setHalignment(lblName, HPos.CENTER);
            GridPane.setHalignment(lblcapacityCPU, HPos.CENTER);
            GridPane.setHalignment(lblcountCPU, HPos.CENTER);


            GridPane.setConstraints(lblName, 0, row);
            GridPane.setConstraints(lblcountCPU, 1, row);
            GridPane.setConstraints(lblcapacityCPU, 2, row);

            gridPane.getChildren().addAll(lblName, lblcountCPU, lblcapacityCPU);

            row++;
        }
        int rowHeight = (row * 14) + 14;
        gridPane.setMinHeight(rowHeight);
        gridPane.setMaxHeight(rowHeight);
        gridPane.setPrefHeight(rowHeight);
        return gridPane;
    }

    public void play() {
        createChart();

        time.play();
    }

    public void stop() {
        time.stop();

        for (ResourceGraphNode resourceGraphNode : relationResourceSerie.keySet()) {

            UtilSerieAverage utilSerieAverage = relationResourceDataAverage.get(resourceGraphNode);
            XYChart.Series<Number, Number> serie = relationResourceSerie.get(resourceGraphNode);
            if (!utilSerieAverage.lastData) {
                serie.getData().add(new XYChart.Data<Number, Number>(100, utilSerieAverage.valueAverage / utilSerieAverage.countAverage));
            } else {
                double repeatValue = serie.getData().get(serie.getData().size() - 1).getYValue().doubleValue();
                serie.getData().add(new XYChart.Data<Number, Number>(100, repeatValue));
            }

        }
    }

    public static class UtilSerieAverage {

        private int countAverage = 0;
        private double timeAverage = 0;
        private double valueAverage = 0;
        private boolean lastData = true;

        public int getCountAverage() {
            return countAverage;
        }

        public void setCountAverage(int countAverage) {
            this.countAverage = countAverage;
        }

        public double getTimeAverage() {
            return timeAverage;
        }

        public void setTimeAverage(double timeAverage) {
            this.timeAverage = timeAverage;
        }

        public double getValueAverage() {
            return valueAverage;
        }

        public void setValueAverage(double valueAverage) {
            this.valueAverage = valueAverage;
        }
    }
    
    
}
