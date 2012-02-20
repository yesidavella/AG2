/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.model;

import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.Switch;
import Grid.Outputter;
import com.ag2.controller.ResultsAbstractController;
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
    
    public OutputterModel(GridSimulator gridSimulator)
    {
        super(gridSimulator); 
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
    public void printResource(ResourceNode resourceNode) 
    {         
        resultsAbstractController.adicionarResultadoRecurso(
                " " + returnStringWithAsterix(resourceNode), 
                " " + sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_JOB_RECEIVED), 
                " " + sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_FAIL_NO_FREE_PLACE), 
                " " + sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_SENDING_FAILED));

    }
    @Override
     public void printSwitch(Switch switch1) 
     {
      
        double fail_resultMessage = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_DROPPED);
        double switchedResultMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_SWITCHED);
        double resultRelative = fail_resultMessage / (fail_resultMessage + switchedResultMessages);

        double switchedJobMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_SWITCHED);
        double droppedJobMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_DROPPED);
        double jobRelative = droppedJobMessages / (switchedJobMessages + droppedJobMessages);

        double messagesDropped = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_MESSAGE_DROPPED);
        double messagesSwitched = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_MESSAGE_SWITCHED);
        double relative = messagesDropped / (messagesDropped + messagesSwitched);
              
        
        resultsAbstractController.adicionarResultadoConmutador(
                returnStringWithAsterix(switch1), 
                "" + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_SWITCHED),
                "" + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_DROPPED), 
                "" + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_SWITCHED),
                "" + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_DROPPED),
                ""+jobRelative,
                ""+resultRelative, 
                "" + relative);
    }
    
    
    
}
