package com.ag2.controller;

import com.ag2.model.OutputterModel;
import com.ag2.model.SimulationBase;
import com.ag2.presentation.control.ViewResultsPhosphorus;
import java.io.Serializable;

public abstract class ResultsAbstractController implements Serializable {

    protected ViewResultsPhosphorus viewResultsPhosphorus;
    protected OutputterModel outputterModel = new OutputterModel(SimulationBase.getInstance().getGridSimulatorModel());

    public void setViewResultsPhosphorus(ViewResultsPhosphorus viewResultsPhosphorus) {
        this.viewResultsPhosphorus = viewResultsPhosphorus;
    }

    public abstract void addClientResult(
            String clientName,
            String requestSent,
            String jobSent,
            String resultReceive ,
            String requestNoSent,
            String relativeResultReceive, 
            String relativeRequestNoSent);

    public abstract void addResourceResult(
            String resourceName,
            String jobReceive,
            String jobSent,
            String relativeJobSent,
            String failSent,
            String relativeFailSent,
            String busyTime,
            String relativeBusyTime,
            String noAvailable,
            String relativeNoAvailable);

    public abstract void addSwitchResult(
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
            final String relativeAllNoSwitched);

    public abstract void setExecutionPercentage(double percentage, double simulationTime);
}
