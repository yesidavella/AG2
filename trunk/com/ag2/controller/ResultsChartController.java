package com.ag2.controller;

import javafx.application.Platform;

public class ResultsChartController extends ResultsChartAbstractController {

    @Override
    public void createClientResult(
            final String clientName,
            final double requestCreated,
            final double requestSent,
            final double jobSent,
            final double resultReceive) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsClientChart.createClientResult(clientName, requestCreated, requestSent, jobSent, resultReceive);
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void createResourceResult(final String resourceName, final double jobReceive, final double resultSent) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsResourceChart.createResourceResult(resourceName, jobReceive, resultSent);
            }
        };
        Platform.runLater(runnable);

    }

    @Override
    public void createBrokerResults(final String brokerName, final double registrationReceived, final double reqRecieved, final double noFreeResouce, final double reqAckSent, final double sendingFailed, final double relativeAckSent) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsBrokerChart.createBrokerResults(brokerName,
                        registrationReceived,
                        reqRecieved,
                        noFreeResouce,
                        reqAckSent,
                        sendingFailed,
                        relativeAckSent);
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void createSwitchResults(
            final String switchName,
            final double jobsSwitched,
            final double jobsNoSwitched,
            final double resultsSwiched,
            final double resultsNoSwitched,
            final double requestsSwitched,
            final double requestsNoSwitched,
            final double relativeNojobsNoSwitched,
            final double relativeResultsNoSwitched,
            final double relativeRequestsNoSwitched,
            final double relativeAllNoSwitched) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsSwitchChart.createSwitchResults(
                        switchName,
                        jobsSwitched,
                        jobsNoSwitched,
                        resultsSwiched,
                        resultsNoSwitched,
                        requestsSwitched,
                        requestsNoSwitched,
                        relativeNojobsNoSwitched,
                        relativeResultsNoSwitched,
                        relativeRequestsNoSwitched,
                        relativeAllNoSwitched);
            }
        };
        Platform.runLater(runnable);
    }
}
