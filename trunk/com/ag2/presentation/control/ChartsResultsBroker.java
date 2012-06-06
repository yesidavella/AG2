package com.ag2.presentation.control;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ChartsResultsBroker implements ViewResultsBrokerChart {

    private Tab tabChartsBrokerResults;
    private ScrollPane scrollPane;
    private VBox vbxMain;

    public ChartsResultsBroker(Tab tabChartsBrokerResults) {
        this.tabChartsBrokerResults = tabChartsBrokerResults;
    }

    @Override
    public void createBrokerResults(String brokerName, double registrationReceived, double reqRecieved, double noFreeResouce, double reqAckSent, double sendingFailed, double relativeAckSent) {
        scrollPane = new ScrollPane();
        vbxMain = new VBox();
        vbxMain.setPadding(new Insets(10, 10, 10, 10));
        vbxMain.setSpacing(10);
        scrollPane.setContent(vbxMain);
        tabChartsBrokerResults.setContent(scrollPane);
        
        vbxMain.getChildren().add(new Rectangle(300, 100, Color.RED));
    }
}
