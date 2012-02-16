/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.modelo;

import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.Switch;
import Grid.Outputter;
import com.ag2.controlador.ResultsAbstractController;
import java.io.PrintStream;
import simbase.Stats.SimBaseStats;

/**
 *
 * @author Frank
 */
public class OutputterModel extends Outputter
{
    private ResultsAbstractController resultsAbstractController;

    
    public void setResultsAbstractController(ResultsAbstractController resultsAbstractController) {
        this.resultsAbstractController = resultsAbstractController;
    }   
    
    public OutputterModel(GridSimulator sim)
    {
        super(sim); 
       SimulationBase.getInstance().setOutputterModel(this);
    }
    @Override
     public void printClient(ClientNode client) 
     {
                       
         resultsAbstractController.addClientResult(
                 returnStringWithAsterix(client), 
                 " "+sim.getStat(client, SimBaseStats.Stat.CLIENT_REQ_SENT), 
                 " "+ sim.getStat(client, SimBaseStats.Stat.CLIENT_JOB_SENT),
                 " "+ sim.getStat(client, SimBaseStats.Stat.CLIENT_RESULTS_RECEIVED),
                 " "+ sim.getStat(client, SimBaseStats.Stat.CLIENT_SENDING_FAILED), 
                 " "+sim.getStat(client, SimBaseStats.Stat.CLIENT_RESULTS_RECEIVED) / sim.getStat(client, SimBaseStats.Stat.CLIENT_JOB_SENT));
          
    }
    @Override
    public void printResource(ResourceNode res) 
    {         
        resultsAbstractController.adicionarResultadoRecurso(
                " " + returnStringWithAsterix(res), 
                " " + sim.getStat(res, SimBaseStats.Stat.RESOURCE_JOB_RECEIVED), 
                " " + sim.getStat(res, SimBaseStats.Stat.RESOURCE_FAIL_NO_FREE_PLACE), 
                " " + sim.getStat(res, SimBaseStats.Stat.RESOURCE_SENDING_FAILED));

    }
    @Override
     public void printSwitch(Switch sw) 
     {
      
        double fail_resultMessage = sim.getStat(sw, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_DROPPED);
        double switchedResultMessages = sim.getStat(sw, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_SWITCHED);
        double resultRelative = fail_resultMessage / (fail_resultMessage + switchedResultMessages);

        double switchedJobMessages = sim.getStat(sw, SimBaseStats.Stat.SWITCH_JOBMESSAGE_SWITCHED);
        double droppedJobMessages = sim.getStat(sw, SimBaseStats.Stat.SWITCH_JOBMESSAGE_DROPPED);
        double jobRelative = droppedJobMessages / (switchedJobMessages + droppedJobMessages);

        double messagesDropped = sim.getStat(sw, SimBaseStats.Stat.SWITCH_MESSAGE_DROPPED);
        double messagesSwitched = sim.getStat(sw, SimBaseStats.Stat.SWITCH_MESSAGE_SWITCHED);
        double relative = messagesDropped / (messagesDropped + messagesSwitched);
              
        
        resultsAbstractController.adicionarResultadoConmutador(
                returnStringWithAsterix(sw), 
                "" + sim.getStat(sw, SimBaseStats.Stat.SWITCH_JOBMESSAGE_SWITCHED),
                "" + sim.getStat(sw, SimBaseStats.Stat.SWITCH_JOBMESSAGE_DROPPED), 
                "" + sim.getStat(sw, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_SWITCHED),
                "" + sim.getStat(sw, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_DROPPED),
                ""+jobRelative,
                ""+resultRelative, 
                "" + relative);
    }
    
    
    
}
