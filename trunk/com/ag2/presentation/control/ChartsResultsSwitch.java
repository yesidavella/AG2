package com.ag2.presentation.control;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ChartsResultsSwitch implements ViewResultsSwitchChart {

    private Tab tabChartsSwitchResults;
    private ScrollPane scrollPane;
    private VBox vbxMain;

    public ChartsResultsSwitch(Tab tabChartsSwitchResults) {
        this.tabChartsSwitchResults = tabChartsSwitchResults;
    }

    @Override
    public void createSwitchResults(String switchName, double jobsSwitched, double jobsNoSwitched, double resultsSwiched, double resultsNoSwitched, double requestsSwitched, double requestsNoSwitched, double relativeNojobsNoSwitched, double relativeResultsNoSwitched, double relativeRequestsNoSwitched, double relativeAllNoSwitched) {
        scrollPane = new ScrollPane();
        vbxMain = new VBox();
        vbxMain.setPadding(new Insets(10, 10, 10, 10));
        vbxMain.setSpacing(10);
        scrollPane.setContent(vbxMain);
        tabChartsSwitchResults.setContent(scrollPane);

        vbxMain.getChildren().add(new Rectangle(300, 100, Color.BLACK));
    }
}
