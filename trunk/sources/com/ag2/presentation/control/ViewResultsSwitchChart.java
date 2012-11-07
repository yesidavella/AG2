package com.ag2.presentation.control;

public interface ViewResultsSwitchChart {

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
            final double relativeAllNoSwitched);
}
