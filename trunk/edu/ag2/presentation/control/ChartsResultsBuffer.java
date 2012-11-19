/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ag2.presentation.control;

import Grid.Entity;
import edu.ag2.controller.DataChartResourceController;
import edu.ag2.controller.MatchCoupleObjectContainer;
import edu.ag2.presentation.design.ClientGraphNode;
import edu.ag2.presentation.design.GraphNode;
import edu.ag2.presentation.design.ResourceGraphNode;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
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
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 *
 * @author Frank
 */
public class ChartsResultsBuffer {

    private Tab tab;
    private VBox vbxAllPage;
    private transient Timeline time = new Timeline();
    private ScrollPane scrollPane;
    private AreaChart<Number, Number> areaChart;
    private HashMap<ResourceGraphNode, XYChart.Series<Number, Number>> relationResourceSerie;
    private DataChartResourceController dataChartResourceController;
//    private boolean loadResources = true;
    private int countClients = 0;
    private int countPlays = 1;

    public ChartsResultsBuffer(Tab tab) {
        this.tab = tab;
        scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("bg-general-container");
        dataChartResourceController = new DataChartResourceController();
        vbxAllPage = new VBox();
        vbxAllPage.setAlignment(Pos.CENTER);
        vbxAllPage.setPadding(new Insets(10, 10, 10, 10));
        vbxAllPage.setSpacing(5);
        vbxAllPage.setFillWidth(true);
        scrollPane.setContent(vbxAllPage);
        tab.setContent(scrollPane);
    }

    protected void createChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        areaChart = new AreaChart<Number, Number>(xAxis, yAxis);
        areaChart.setMinWidth(1100);

        areaChart.setTitle("Uso del buffer del Cluster  - Ejecución N." + countPlays);
        xAxis.setLabel("Tiempo de simulacion");
        yAxis.setLabel("Trabajos en el buffer ");
        countPlays++;
        relationResourceSerie = new HashMap<ResourceGraphNode, XYChart.Series<Number, Number>>();


        for (GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {
            if (graphNode instanceof ResourceGraphNode) {
                XYChart.Series<Number, Number> serie = new XYChart.Series<Number, Number>();
                serie.setName(graphNode.toString());
                serie.getData().add(new XYChart.Data<Number, Number>(0, 0));
                areaChart.getData().addAll(serie);

                relationResourceSerie.put((ResourceGraphNode) graphNode, serie);

            }
            if (graphNode instanceof ClientGraphNode) {
                countClients++;
            }
        }


        KeyFrame keyFrame = new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                for (ResourceGraphNode resourceGraphNode : relationResourceSerie.keySet()) {

                    if (dataChartResourceController.loadDataChartResourceBuffer(resourceGraphNode)) {
                        XYChart.Series<Number, Number> serie = relationResourceSerie.get(resourceGraphNode);
                        if (dataChartResourceController.getTime() > 0) {
                            serie.getData().add(new XYChart.Data<Number, Number>(dataChartResourceController.getTime(), dataChartResourceController.getValue1()));
                        }
                    }
                }
            }
        });
        time.getKeyFrames().add(keyFrame);
        time.setCycleCount(Timeline.INDEFINITE);

        GridPane gridPane = loadInfoBufferResource();

        HBox hBox = new HBox();
        hBox.getStyleClass().add("boxChart");
        hBox.setFillHeight(true);
        areaChart.setEffect(null);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(5, 5, 5, 5));
        HBox.setHgrow(gridPane, Priority.ALWAYS);
        hBox.getChildren().addAll(gridPane, areaChart);
        hBox.setMinWidth(1200);
        vbxAllPage.getChildren().add(0, hBox);
    }

    private GridPane loadInfoBufferResource() {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(2);
        gridPane.setHgap(4);

        gridPane.setAlignment(Pos.CENTER);


        Label lblNameTitle = new Label("Cluster");
        Label lblBufferTitle = new Label("Max Buffer");


        lblNameTitle.setTextAlignment(TextAlignment.CENTER);
        lblBufferTitle.setTextAlignment(TextAlignment.CENTER);

        lblNameTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        lblBufferTitle.setFont(Font.font("Arial", FontWeight.BOLD, 10));

        GridPane.setConstraints(lblNameTitle, 0, 0);
        GridPane.setHalignment(lblNameTitle, HPos.LEFT);

        GridPane.setConstraints(lblBufferTitle, 1, 0);
        GridPane.setHalignment(lblBufferTitle, HPos.CENTER);

        gridPane.getChildren().addAll(lblNameTitle, lblBufferTitle);
        int row = 1;


        for (ResourceGraphNode resourceGraphNode : relationResourceSerie.keySet()) {
            dataChartResourceController.loadInfoBufferResouce(resourceGraphNode);
            String name = resourceGraphNode.getName();
            String maxBuffer = String.valueOf(dataChartResourceController.getMaxQueueSize());

            Label lblName = new Label(name);
            Label lblcapacityCPU = new Label(maxBuffer);

            lblName.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
            lblcapacityCPU.setFont(Font.font("Arial", FontWeight.NORMAL, 10));

            lblName.setTextAlignment(TextAlignment.CENTER);
            lblcapacityCPU.setTextAlignment(TextAlignment.CENTER);

            GridPane.setHalignment(lblName, HPos.CENTER);
            GridPane.setHalignment(lblcapacityCPU, HPos.CENTER);
            GridPane.setConstraints(lblName, 0, row);
            GridPane.setConstraints(lblcapacityCPU, 1, row);
            gridPane.getChildren().addAll(lblName, lblcapacityCPU);
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


    }
}
