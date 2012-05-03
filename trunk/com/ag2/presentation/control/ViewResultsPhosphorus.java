package com.ag2.presentation.control;

public interface ViewResultsPhosphorus {

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
            String relativeReceiveresult_requestCreated    );

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

    public abstract void addBrokerResult(
            final String brokerName,
            final String registrationReceived,
            final String noFreeResource,
            final String reqAckSent,
            final String reqRecieved,
            final String sendingFailed,
            final String relativeAckSent);

    public void setExecutionPercentage(double Percentage, double simulationTime);
}
