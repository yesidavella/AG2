/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.controller;

import com.ag2.presentation.control.ViewResultsClientChart;
import com.ag2.presentation.control.ViewResultsResourceChart;
import java.io.Serializable;

/**
 *
 * @author Frank
 */
public abstract class ResultsChartAbstractController implements Serializable {

    protected ViewResultsClientChart viewResultsClientChart;
    protected ViewResultsResourceChart viewResultsResourceChart;

    public void setViewResultsClientChart(ViewResultsClientChart viewResultsChart) {
        this.viewResultsClientChart = viewResultsChart;
    }

    public void setViewResultsResourceChart(ViewResultsResourceChart viewResultsResourceChart) {
        this.viewResultsResourceChart = viewResultsResourceChart;
    }

    public abstract void createClientResult(
            String clientName,
            double requestCreated,
            double requestSent,
            double jobSent,
            double resultReceive);

      public abstract void  createResourceResult( 
            final String resourceName,
            final double jobReceive, 
            final double resultSent);
}
