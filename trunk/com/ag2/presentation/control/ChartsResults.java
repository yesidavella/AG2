/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;

import Grid.Entity;
import com.ag2.controller.DataChartResourceController;
import com.ag2.controller.MatchCoupleObjectContainer;
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

        final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        final XYChart.Series<Number, Number> series2 = new XYChart.Series<Number, Number>();

        final XYChart.Series<Number, Number> series3 = new XYChart.Series<Number, Number>();
        final XYChart.Series<Number, Number> series4 = new XYChart.Series<Number, Number>();

        series.setName("C1");
        series2.setName("C2");

//        series.getData().add(new XYChart.Data<Number, Number>(20d, 50d));
//        series.getData().add(new XYChart.Data<Number, Number>(40d, 80d));
//        series.getData().add(new XYChart.Data<Number, Number>(50d, 90d));
//        series.getData().add(new XYChart.Data<Number, Number>(70d, 30d));
//        series.getData().add(new XYChart.Data<Number, Number>(90d, 100d));
//        
//        series2.getData().add(new XYChart.Data<Number, Number>(10d, 30d));
//        series2.getData().add(new XYChart.Data<Number, Number>(20d, 50d));
//        series2.getData().add(new XYChart.Data<Number, Number>(30d, 60d));
//        series2.getData().add(new XYChart.Data<Number, Number>(40d, 70d));
//        series2.getData().add(new XYChart.Data<Number, Number>(50d, 5d));


        KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if (loadResources) {
                    loadResources = false;
                    for (GraphNode graphNode : nodeMatchCoupleObjectContainer.keySet()) {
                        if (graphNode instanceof ResourceGraphNode) {
                            XYChart.Series<Number, Number> serie = new XYChart.Series<Number, Number>();
                            lineChart.getData().addAll(serie);
                            relationResourceSerie.put((ResourceGraphNode) graphNode, serie);
                        }
                    }
                }

                for (ResourceGraphNode resourceGraphNode : relationResourceSerie.keySet()) {

                    dataChartResourceController.loadDataChart(resourceGraphNode);
                    XYChart.Series<Number, Number> serie = relationResourceSerie.get(resourceGraphNode);
                    serie.getData().add(new XYChart.Data<Number, Number>(dataChartResourceController.getTime(), dataChartResourceController.getValue1()));
                    System.out.println(" value " + dataChartResourceController.getValue1());
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

    }

    public void stop() {
        time.stop();
       // lineChart.getData().clear();
    }
}
