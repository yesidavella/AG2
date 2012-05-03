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
            String requestCreated,
            String requestSent,
            String requestNoSent,
            String jobSent,
            String jobNoSent,                  
            String resultReceive,            
            String relativeRequestSent,
            String relativeJobSent,
            String relativeReceiveResult_jobSent,
            String relativeReceiveResult_requsetSent,
            String relativeReceiveresult_requestCreated       
            
            );

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
             String switchName,
             String jobSwitched,
             String jobNoSwitched,
             String resultSwiched,
             String resultNoSwitched,
             String requestSwitched,
             String requestNoSwitched,
             String relativeNojobNoSwitched,
             String relativeResultNoSwitched,
             String relativeRequestNoSwitched,
             String relativeAllNoSwitched);

    public abstract void setExecutionPercentage(double percentage, double simulationTime);

    public abstract void addBrokerResults(String brokerName,
            String registrationReceived,
            String noFreeResource,
            String reqAckSent,
            String reqRecieved,
            String sendingFailed,
            String relativeAckSent);

  
}
