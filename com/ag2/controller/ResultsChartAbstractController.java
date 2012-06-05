package com.ag2.controller;

import com.ag2.presentation.control.ChartsResultsBroker;
import com.ag2.presentation.control.ViewResultsBrokerChart;
import com.ag2.presentation.control.ViewResultsClientChart;
import com.ag2.presentation.control.ViewResultsResourceChart;
import java.io.Serializable;

public abstract class ResultsChartAbstractController implements Serializable {

    protected transient ViewResultsClientChart viewResultsClientChart;
    protected transient ViewResultsResourceChart viewResultsResourceChart;
    protected transient ViewResultsBrokerChart viewResultsBrokerChart;

    public void setViewResultsClientChart(ViewResultsClientChart viewResultsChart) {
        this.viewResultsClientChart = viewResultsChart;
    }

    public void setViewResultsResourceChart(ViewResultsResourceChart viewResultsResourceChart) {
        this.viewResultsResourceChart = viewResultsResourceChart;
    }

    public void setViewResultsBrokerChart(ChartsResultsBroker chartsResultsBroker) {
        this.viewResultsBrokerChart = chartsResultsBroker;
    }

    public abstract void createClientResult(
            String clientName,
            double requestCreated,
            double requestSent,
            double jobSent,
            double resultReceive);

    public abstract void createResourceResult(
            final String resourceName,
            final double jobReceive,
            final double resultSent);

    public abstract void createBrokerResults(
            String brokerName,
            double registrationReceived,
            double reqRecieved,
            double noFreeResouce,
            double reqAckSent,
            double sendingFailed,
            double relativeAckSent);
}
