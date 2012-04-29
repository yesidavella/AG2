/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.controller;

import javafx.application.Platform;

/**
 *
 * @author Frank
 */
public class ResultsChartController extends ResultsChartAbstractController {

    @Override
    public void createClientResult(final String clientName, final double relativeResultReceive, final double relativeJobsNoSent) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsChart.createClientResult(clientName, relativeResultReceive, relativeJobsNoSent);
            }
        };
        Platform.runLater(runnable);
    }
}
