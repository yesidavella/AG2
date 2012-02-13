/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.modelo;

import Grid.Utilities.Config;
import com.ag2.controlador.ResultsAbstractController;
import com.ag2.presentacion.controles.ViewResultsPhosphorus;
import simbase.*;
import simbase.Exceptions.StopException;
import simbase.Port.SimBaseInPort;

/**
 *
 * @author Frank
 */
public class GridSimulatorModel extends Grid.GridSimulator {

    private double simulationTime = 0;
    private double percentage = 0;
    private int percentageShort = 0;
    private int percentageSwapShort = 0;
    private ResultsAbstractController resultsAbstractController; 
    
    public GridSimulatorModel() {
        simulationTime = SimulationInstance.configuration.getDoubleProperty(Config.ConfigEnum.simulationTime);

    }
    public void reloadTime()
    {
           simulationTime = SimulationInstance.configuration.getDoubleProperty(Config.ConfigEnum.simulationTime);
    }

    public void setViewResultsPhosphorus(ResultsAbstractController resultsAbstractController) {
        this.resultsAbstractController = resultsAbstractController;
    }

    @Override
    public boolean runNextEvent() throws StopException 
    {
        if (events.isEmpty()) 
        {
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
        percentage = masterClock.getTime()*100 / simulationTime;
        percentageShort = Math.round((float) percentage);
        if(percentageShort!=percentageSwapShort)
        {
            percentageSwapShort=percentageShort; 
            resultsAbstractController.setExecutionPercentage(percentage);
           
        }     
        return true;

    }
}
