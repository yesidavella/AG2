package edu.ag2.controller;

import edu.ag2.presentation.control.ChartsResultsBroker;
import edu.ag2.presentation.control.ViewResultsBrokerChart;
import edu.ag2.presentation.control.ViewResultsClientChart;
import edu.ag2.presentation.control.ViewResultsResourceChart;
import edu.ag2.presentation.control.ViewResultsSwitchChart;
import java.io.Serializable;

public abstract class ResultsChartAbstractController implements Serializable {

    protected transient ViewResultsClientChart viewResultsClientChart;
    protected transient ViewResultsResourceChart viewResultsResourceChart;
    protected transient ViewResultsBrokerChart viewResultsBrokerChart;
    protected transient ViewResultsSwitchChart viewResultsSwitchChart;

    public void setViewResultsClientChart(ViewResultsClientChart viewResultsChart) {
        this.viewResultsClientChart = viewResultsChart;
    }

    public void setViewResultsResourceChart(ViewResultsResourceChart viewResultsResourceChart) {
        this.viewResultsResourceChart = viewResultsResourceChart;
    }

    public void setViewResultsBrokerChart(ChartsResultsBroker chartsResultsBroker) {
        this.viewResultsBrokerChart = chartsResultsBroker;
    }

    public void setViewResultsSwitchChart(ViewResultsSwitchChart viewResultsSwitchChart) {
        this.viewResultsSwitchChart = viewResultsSwitchChart;
    }

    public abstract void createClientResult(
            String clientName,
            double requestCreated,
            double requestSent,
            double jobSent,
            double resultReceive);

    public abstract void createResourceResult(
            String resourceName,
            double jobReceive,
            double resultSent);

    public abstract void createBrokerResults(
            String brokerName,
            double registrationReceived,
            double reqRecieved,
            double noFreeResouce,
            double reqAckSent,
            double sendingFailed,
            double relativeAckSent);

    public abstract void createSwitchResults(
            String switchName,
            double jobsSwitched,
            double jobsNoSwitched,
            double resultsSwiched,
            double resultsNoSwitched,
            double requestsSwitched,
            double requestsNoSwitched,
            double relativeNojobsNoSwitched,
            double relativeResultsNoSwitched,
            double relativeRequestsNoSwitched,
            double relativeAllNoSwitched);
}
