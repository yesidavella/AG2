package com.ag2.controller;

import javafx.application.Platform;

public class ResultsController extends ResultsAbstractController {

    @Override
    public void addClientResult(
            final String clientName,
            final String requestSent,
            final String jobSent,
            final String resultReceive,
            final String requestNoSent,
            final String relativeResultReceive) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsPhosphorus.addClientResult(
                        clientName,
                        requestSent,
                        jobSent,
                        resultReceive,
                        requestNoSent,
                        relativeResultReceive);
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