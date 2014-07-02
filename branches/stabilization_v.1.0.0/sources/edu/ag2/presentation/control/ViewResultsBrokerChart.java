package edu.ag2.presentation.control;

public interface ViewResultsBrokerChart {
    
    public void createBrokerResults(
                String brokerName,
                double registrationReceived,
                double reqRecieved,
                double noFreeResouce,
                double reqAckSent,
                double sendingFailed,
                double relativeAckSent);
}