package com.ag2.controller;

import javafx.application.Platform;

public class ResultsController extends ResultsAbstractController {

    @Override
    public void addClientResult(
            final String clientName,
            final double requestCreated,
            final double requestSent,
            final double requestNoSent,
            final double jobSent,
            final double jobNoSent,
            final double resultReceive,
            final double relativeRequestSent,
            final double relativeJobSent,
            final double relativeReceiveResult_jobSent,
            final double relativeReceiveResult_requsetSent,
            final double relativeReceiveresult_requestCreated) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsPhosphorus.addClientResult(
                        clientName,
                        requestCreated,
                        requestSent,
                        requestNoSent,
                        jobSent,
                        jobNoSent,
                        resultReceive,
                        relativeRequestSent,
                        relativeJobSent,
                        relativeReceiveResult_jobSent,
                        relativeReceiveResult_requsetSent,
                        relativeReceiveresult_requestCreated);
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void addResourceResult(
            final String resourceName,
            final double jobReceive,
            final double jobSent,
            final double relativeJobSent,
            final double failSent,
            final double relativeFailSent,
            final double busyTime,
            final double relativeBusyTime,
            final double noAvailable,
            final double relativeNoAvailable) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsPhosphorus.addResourceResult(
                        resourceName,
                        jobReceive,
                        jobSent,
                        relativeJobSent,
                        failSent,
                        relativeFailSent,
                        busyTime,
                        relativeBusyTime,
                        noAvailable,
                        relativeNoAvailable);
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void addSwitchResult(
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
                viewResultsPhosphorus.addSwitchResult(
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

    @Override
    public void addBrokerResults(
            final String brokerName,
            final double registrationReceived,
            final double reqRecieved,
            final double noFreeResource,
            final double reqAckSent,
            final double sendingFailed,
            final double relativeAckSent) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsPhosphorus.addBrokerResult(
                        brokerName,
                        registrationReceived,
                        reqRecieved,
                        noFreeResource,
                        reqAckSent,
                        sendingFailed,
                        relativeAckSent);
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void setExecutionPercentage(final double Percentage, final double simulationTime) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsPhosphorus.setExecutionPercentage(Percentage, simulationTime);
            }
        };

        Platform.runLater(runnable);
    }
}