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

import java.util.Calendar;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.geotools.util.CheckedHashMap;

/**
 *
 * @author Frank
 */
public class ChartsResults {

    private Tab tab;
    private GridPane gpMain;
    private transient Timeline time = new Timeline();
    private ScrollPane scrollPane;
    private LineChart<Number, Number> lineChart;
    private HashMap<GraphNode, Entity> nodeMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer();
    private HashMap<ResourceGraphNode, XYChart.Series<Number, Number>> relationResourceSerie = new HashMap<ResourceGraphNode, XYChart.Series<Number, Number>>();
    private DataChartResourceController dataChartResourceController;
    private boolean loadResources = true;   
    private int countClients = 0;
    private HashMap<ResourceGraphNode, UtilSerieAverage> relationResourceDataAverage = new HashMap<ResourceGraphNode, UtilSerieAverage>();

    public ChartsResults(Tab tab) {
        this.tab = tab;
        scrollPane = new ScrollPane();
        dataChartResourceController = new DataChartResourceController();
        gpMain = new GridPane();
        gpMain.setAlignment(Pos.CENTER);
        gpMain.setPadding(new Insets(10));

        createChart();

        scrollPane.setContent(gpMain);
        tab.setContent(scrollPane);
    }

    protected void createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setMinWidth(1100);

        lineChart.setTitle("Ocupación de los recursos (CPU)");
        xAxis.setLabel("Tiempo de simulacion");
        yAxis.setLabel("Porcentaje de ocupación ");




        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if (loadResources) {
                    loadResources = false;
                    for (GraphNode graphNode : nodeMatchCoupleObjectContainer.keySet()) {
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
                }

                for (ResourceGraphNode resourceGraphNode : relationResourceSerie.keySet()) {

                    dataChartResourceController.loadDataChart(resourceGraphNode);
                    UtilSerieAverage utilSerieAverage = relationResourceDataAverage.get(resourceGraphNode);

                    if (dataChartResourceController.getTime() != 0) {
                        utilSerieAverage.countAverage++;
                        utilSerieAverage.timeAverage += dataChartResourceController.getTime();
                        utilSerieAverage.valueAverage += dataChartResourceController.getValue1();


                        if (utilSerieAverage.countAverage >= 4 * countClients) {
                            
                            XYChart.Series<Number, Number> serie = relationResourceSerie.get(resourceGraphNode);
                            serie.getData().add(new XYChart.Data<Number, Number>(utilSerieAverage.timeAverage / utilSerieAverage.countAverage, utilSerieAverage.valueAverage / utilSerieAverage.countAverage));

                            utilSerieAverage.timeAverage = 0;
                            utilSerieAverage.valueAverage = 0;
                            utilSerieAverage.countAverage = 0;
                        }

                    }

                }
            }
        });
        time.getKeyFrames().add(keyFrame);
        time.setCycleCount(Timeline.INDEFINITE);

        GridPane.setConstraints(lineChart, 0, 0);
        gpMain.getChildren().add(lineChart);
    }

    public void play() {
        loadResources = true;
        time.play();        
      

        for (ResourceGraphNode resourceGraphNode : relationResourceSerie.keySet()) {

            UtilSerieAverage utilSerieAverage = relationResourceDataAverage.get(resourceGraphNode);
            utilSerieAverage.timeAverage = 0;
            utilSerieAverage.valueAverage = 0;
            utilSerieAverage.countAverage = 0;
        }
        lineChart.getData().clear();

    }

    public void stop() {
        time.stop();

        for (ResourceGraphNode resourceGraphNode : relationResourceSerie.keySet()) 
        {
             UtilSerieAverage utilSerieAverage = relationResourceDataAverage.get(resourceGraphNode);
             XYChart.Series<Number, Number> serie = relationResourceSerie.get(resourceGraphNode);
             serie.getData().add(new XYChart.Data<Number, Number>(100, utilSerieAverage.valueAverage / utilSerieAverage.countAverage));

        }
    }

    public static class UtilSerieAverage {

        int countAverage = 0;
        double timeAverage = 0;
        double valueAverage = 0;

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
