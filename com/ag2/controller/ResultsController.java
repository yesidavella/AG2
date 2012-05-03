package com.ag2.controller;

import javafx.application.Platform;

public class ResultsController extends ResultsAbstractController {

    @Override
    public void addClientResult(
            final String clientName,
            final String requestCreated,
            final String requestSent,
            final String requestNoSent,
            final String jobSent,
            final String jobNoSent,
            final String resultReceive,
            final String relativeRequestSent,
            final String relativeJobSent,
            final String relativeReceiveResult_jobSent,
            final String relativeReceiveResult_requsetSent,
            final String relativeReceiveresult_requestCreated) {

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
            final String jobReceive,
            final String jobSent,
            final String relativeJobSent,
            final String failSent,
            final String relativeFailSent,
            final String busyTime,
            final String relativeBusyTime,
            final String noAvailable,
            final String relativeNoAvailable) {

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
            final String jobSwitched,
            final String jobNoSwitched,
            final String resultSwiched,
            final String resultNoSwitched,
            final String requestSwitched,
            final String requestNoSwitched,
            final String relativeNojobNoSwitched,
            final String relativeResultNoSwitched,
            final String relativeRequestNoSwitched,
            final String relativeAllNoSwitched) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsPhosphorus.addSwitchResult(
                        switchName,
                        jobSwitched,
                        jobNoSwitched,
                        resultSwiched,
                        resultNoSwitched,
                        requestSwitched,
                        requestNoSwitched,
                        relativeNojobNoSwitched,
                        relativeResultNoSwitched,
                        relativeRequestNoSwitched,
                        relativeAllNoSwitched);
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void addBrokerResults(
            final String brokerName,
            final String registrationReceived,
            final String noFreeResource,
            final String reqAckSent,
            final String reqRecieved,
            final String sendingFailed,
            final String relativeAckSent) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsPhosphorus.addBrokerResult(
                        brokerName,
                        registrationReceived,
                        noFreeResource,                        
                        reqAckSent,
                        reqRecieved,
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