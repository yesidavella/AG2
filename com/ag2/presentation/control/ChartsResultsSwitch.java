package com.ag2.presentation.control;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class ChartsResultsSwitch implements ViewResultsSwitchChart {

    private Tab tabChartsSwitchResults;
    private ScrollPane scrollPane;
    private VBox vbxMain;

    public ChartsResultsSwitch(Tab tabChartsSwitchResults) {
        this.tabChartsSwitchResults=tabChartsSwitchResults;
    }

    @Override
    public void createSwitchResults(String switchName, double jobsSwitched, double jobsNoSwitched, double resultsSwiched, double resultsNoSwitched, double requestsSwitched, double requestsNoSwitched, double relativeNojobsNoSwitched, double relativeResultsNoSwitched, double relativeRequestsNoSwitched, double relativeAllNoSwitched) {
        System.out.println("Esto es el switch");
    }
}
