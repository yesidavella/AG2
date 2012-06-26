package com.ag2.presentation.control;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ChartsResultsBroker implements ViewResultsBrokerChart {

    private Tab tabChartsBrokerResults;
    private ScrollPane scpAllPage;
    private VBox vbxAllPage;

    public ChartsResultsBroker(Tab tabChartsBrokerResults) {
        this.tabChartsBrokerResults = tabChartsBrokerResults;
    }

    @Override
    public void createBrokerResults(String brokerName, double registrationReceived,
            double reqRecieved, double noFreeResouce, double reqAckSent,
            double sendingFailed, double relativeAckSent) {

        scpAllPage = new ScrollPane();
        vbxAllPage = new VBox();
        vbxAllPage.setPadding(new Insets(10, 10, 10, 10));
        vbxAllPage.setSpacing(10);
        scpAllPage.setContent(vbxAllPage);
        tabChartsBrokerResults.setContent(scpAllPage);

        vbxAllPage.getChildren().add(new Rectangle(300, 100, Color.RED));
    }
}
