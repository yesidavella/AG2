package com.ag2.presentation.control;

public interface ViewResultsPhosphorus {

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
            double relativeReceiveResult_requestSent,
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
            final String switchName,
            final double jobSwitched,
            final double jobNoSwitched,
            final double resultSwiched,
            final double resultNoSwitched,
            final double requestSwitched,
            final double requestNoSwitched,
            final double relativeNojobNoSwitched,
            final double relativeResultNoSwitched,
            final double relativeRequestNoSwitched,
            final double relativeAllNoSwitched);

    public abstract void addBrokerResult(
            final String brokerName,
            final double registrationReceived,
            final double reqRecieved,
            final double noFreeResource,
            final double reqAckSent,
            final double sendingFailed,
            final double relativeAckSent);

    public void setExecutionPercentage(double Percentage, double simulationTime);
}
