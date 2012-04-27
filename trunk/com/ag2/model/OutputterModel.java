package com.ag2.model;

import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.Switch;
import Grid.Outputter;
import com.ag2.controller.ResultsAbstractController;
import java.text.DecimalFormat;
import simbase.Stats.SimBaseStats;

public class OutputterModel extends Outputter {

    private ResultsAbstractController resultsAbstractController;
    DecimalFormat decimalFormat = new DecimalFormat("###############.###");

    public void setResultsAbstractController(ResultsAbstractController resultsAbstractController) {
        this.resultsAbstractController = resultsAbstractController;
    }

    public OutputterModel(GridSimulator gridSimulator) {
        super(gridSimulator);
        SimulationBase.getInstance().setOutputterModel(this);
    }

    @Override
    public void printClient(ClientNode client) {

        resultsAbstractController.addClientResult(
                client.toString(),
                "  " + sim.getStat(client, SimBaseStats.Stat.CLIENT_REQ_SENT),
                "  " + sim.getStat(client, SimBaseStats.Stat.CLIENT_JOB_SENT),
                "  " + sim.getStat(client, SimBaseStats.Stat.CLIENT_RESULTS_RECEIVED),
                "  " + sim.getStat(client, SimBaseStats.Stat.CLIENT_SENDING_FAILED),
                "  " +decimalFormat.format(sim.getStat(client, SimBaseStats.Stat.CLIENT_RESULTS_RECEIVED) / sim.getStat(client, SimBaseStats.Stat.CLIENT_JOB_SENT)*100) +"%" );
    }

    @Override
    public void printResource(ResourceNode resourceNode) 
    {
        
        double recive = sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_JOB_RECEIVED);
        double sent = sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_JOB_RECEIVED);
        double sentFall = sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_SENDING_FAILED);
        
        double relativeSent  =  sent/recive;
        double relativeSentFall  =  sentFall/recive;
        
        resultsAbstractController.adicionarResultadoRecurso(
                 resourceNode.toString(),
                " " + recive,                
                " " + decimalFormat.format(relativeSent*100)+"%",
                " " + sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_SENDING_FAILED),                 
                " " + sentFall,
                 " " + decimalFormat.format(relativeSentFall*100)+"%",
                " " + sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_BUSY_TIME),
                " " + sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_FAIL_NO_FREE_PLACE));
    }

    @Override
    public void printSwitch(Switch switch1) {

        double fail_resultMessage = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_DROPPED);
        double switchedResultMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_SWITCHED);
        double resultRelative = fail_resultMessage / (fail_resultMessage + switchedResultMessages);

        double switchedJobMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_SWITCHED);
        double droppedJobMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_DROPPED);
        double jobRelative = droppedJobMessages / (switchedJobMessages + droppedJobMessages);

        double messagesDropped = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_MESSAGE_DROPPED);
        double messagesSwitched = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_MESSAGE_SWITCHED);
        
        double messagesReqDropped = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_REQ_MESSAGE_DROPPED);
        double messagesReqSwitched = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_REQ_MESSAGE_SWITCHED);
        
         double relativeReq = messagesReqDropped / (messagesReqDropped + messagesReqSwitched);
        
        double relative = messagesDropped / (messagesDropped + messagesSwitched);

        resultsAbstractController.adicionarResultadoConmutador(
                switch1.toString(),
                " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_SWITCHED),
                " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_DROPPED),
                " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_SWITCHED),
                " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_DROPPED),
                 " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_REQ_MESSAGE_SWITCHED),
                 " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_REQ_MESSAGE_DROPPED),
                " " + decimalFormat.format(jobRelative*100)+"%",
                " " + decimalFormat.format(resultRelative*100)+"%",
                " " + decimalFormat.format(relativeReq*100)+"%",
                " " + decimalFormat.format( relative*100 )+"%");
    }
}