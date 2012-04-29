/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.controller;

import com.ag2.presentation.control.ViewResultsChart;

/**
 *
 * @author Frank
 */
public abstract class ResultsChartAbstractController {

    public abstract void createClientResult(String clientName, double relativeResultReceive, double relativeJobsNoSent);
    protected ViewResultsChart viewResultsChart;

    public void setViewResultsChart(ViewResultsChart viewResultsChart) {
        this.viewResultsChart = viewResultsChart;
    }
}
