package com.ag2.presentation.control;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class ChartsResultsSwitch implements ViewResultsSwitchChart {

    private Tab tabChartsSwitchResults;
    private ScrollPane scpAllPage;
    private VBox vbxAllPage;

    public ChartsResultsSwitch(Tab tabChartsSwitchResults) {
        this.tabChartsSwitchResults = tabChartsSwitchResults;
    }

    @Override
    public void createSwitchResults(String switchName, double jobsSwitched, double jobsNoSwitched, double resultsSwiched, double resultsNoSwitched, double requestsSwitched, double requestsNoSwitched, double relativeNojobsNoSwitched, double relativeResultsNoSwitched, double relativeRequestsNoSwitched, double relativeAllNoSwitched) {
        scpAllPage = new ScrollPane();
        vbxAllPage = new VBox();
        vbxAllPage.setPadding(new Insets(10, 10, 10, 10));
        vbxAllPage.setSpacing(10);
        scpAllPage.setContent(vbxAllPage);
        tabChartsSwitchResults.setContent(scpAllPage);

        System.out.print("Creo ");
        
    }
}
