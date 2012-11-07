package com.ag2.controller;

import com.ag2.model.OutputterModel;
import com.ag2.model.SimulationBase;
import com.ag2.presentation.control.ViewResultsPhosphorus;
import java.io.Serializable;

public abstract class ResultsAbstractController implements Serializable {

    protected transient ViewResultsPhosphorus viewResultsPhosphorus;
    protected OutputterModel outputterModel = new OutputterModel(SimulationBase.getInstance().getGridSimulatorModel());

    public void setViewResultsPhosphorus(ViewResultsPhosphorus viewResultsPhosphorus) {
        this.viewResultsPhosphorus = viewResultsPhosphorus;
    }

    public abstract void addClientResult(
            String clientName,
            double requestCreated,
            double requestSent,
            double requestNoSent,
            double jobSent,
            double jobNoSent,
            double resultReceive,
            double relativeRequestSent,
            double relativeJobSent,
            double relativeReceiveResult_jobSent,
            double relativeReceiveResult_requsetSent,
            double relativeReceiveresult_requestCreated);

    public abstract void addResourceResult(
            String resourceName,
            double jobReceive,
            double jobSent,
            double relativeJobSent,
            double failSent,
            double relativeFailSent,
            double busyTime,
            double relativeBusyTime,
            double noAvailable,
            double relativeNoAvailable);

    public abstract void addSwitchResult(
            String switchName,
            double jobSwitched,
            double jobNoSwitched,
            double resultSwiched,
            double resultNoSwitched,
            double requestSwitched,
            double requestNoSwitched,
            double relativeNojobNoSwitched,
            double relativeResultNoSwitched,
            double relativeRequestNoSwitched,
            double relativeAllNoSwitched);

    public abstract void setExecutionPercentage(double percentage, double simulationTime);

    public abstract void addBrokerResults(
            String brokerName,
            double registrationReceived,
            double reqRecieved,
            double noFreeResource,
            double reqAckSent,
            double sendingFailed,
            double relativeAckSent);
}
