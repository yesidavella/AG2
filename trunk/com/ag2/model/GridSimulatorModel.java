package com.ag2.model;

import Grid.Routing.RoutingViaJung;
import Grid.Routing.ShortesPathRouting;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.controller.ResultsAbstractController;
import java.io.ObjectInputStream;
import java.util.ArrayList;
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
    private long countEventToAdvise = 0;
    private ResultsAbstractController resultsAbstractController;
    private ArrayList<OCSRequest> OCSDesignedCircuits;

    public GridSimulatorModel() {
        initRoutingObjLoadingConfigFile();
        simulationTime = PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SIMULATION_TIME);
        OCSDesignedCircuits = new ArrayList<OCSRequest>();
    }

    private void initRoutingObjLoadingConfigFile() {
        if (PropertyPhosphorusTypeEnum.getBooleanProperty(PropertyPhosphorusTypeEnum.ROUTED_VIA_JUNG)) {
            super.setRouting(new RoutingViaJung(this));
        } else {
            super.setRouting(new ShortesPathRouting(this));
        }
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
            System.out.println("No hay ams eventos¡¡¡");
            throw new StopException("There are no events to execute anymore");
        }
        eventCount++;
        SimBaseEvent nextEvent = events.first();
        events.remove(nextEvent);
        masterClock = nextEvent.getTime();
        nextEvent.getTarget().getOwner().updateTime(masterClock);

        SimBaseMessage msg = nextEvent.getMessage();
        SimBaseInPort inPort = nextEvent.getTarget();
        SimBaseEntity targetEntity = inPort.getOwner();
        targetEntity.receive(inPort, msg);

        reloadTime();
        percentage = masterClock.getTime() * 100 / simulationTime;

        countEventToAdvise++;

        if (countEventToAdvise > 10) {
            countEventToAdvise = 0;
            resultsAbstractController.setExecutionPercentage(percentage, masterClock.getTime());
        }

        percentageShort = Math.round((float) percentage);
        if (percentageShort != percentageSwapShort) {
            percentageSwapShort = percentageShort;
            resultsAbstractController.setExecutionPercentage(percentage, masterClock.getTime());

        }
        return true;
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
            initRoutingObjLoadingConfigFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<OCSRequest> getDesignedOCSCircuits() {
        return OCSDesignedCircuits;
    }
}