package com.ag2.model;

import Grid.GridSimulation;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.controller.ResultsAbstractController;
import simbase.Exceptions.StopException;
import simbase.Port.SimBaseInPort;
import simbase.SimBaseEntity;
import simbase.SimBaseEvent;
import simbase.SimBaseMessage;

public class GridSimulatorModel extends Grid.GridSimulator {

    private double simulationTime = 0;
    private double percentage = 0;
    private int percentageShort = 0;
    private int percentageSwapShort = 0;
    private ResultsAbstractController resultsAbstractController;

    public GridSimulatorModel() {
        simulationTime = PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SIMULATION_TIME);
    }

    public void reloadTime() {
        simulationTime = PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SIMULATION_TIME);
    }

    public void setViewResultsPhosphorus(ResultsAbstractController resultsAbstractController) {
        this.resultsAbstractController = resultsAbstractController;
    }

    @Override
    public boolean runNextEvent() throws StopException {
        if (events.isEmpty()) {
            throw new StopException("There are no events to execute anymore");
        }
        eventCount++;
        SimBaseEvent nextEvent = events.first();
        events.remove(nextEvent);
        masterClock = nextEvent.getTime();
        nextEvent.getTarget().getOwner().updateTime(masterClock);
        SimBaseInPort port = nextEvent.getTarget();
        SimBaseMessage msg = nextEvent.getMessage();
        SimBaseEntity entity = nextEvent.getTarget().getOwner();
        entity.receive(port, msg);

        reloadTime();
        percentage = masterClock.getTime() * 100 / simulationTime;
        percentageShort = Math.round((float) percentage);
        if (percentageShort != percentageSwapShort) {
            percentageSwapShort = percentageShort;
            resultsAbstractController.setExecutionPercentage(percentage);

        }
        return true;
    }
}