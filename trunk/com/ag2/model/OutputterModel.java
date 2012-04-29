package com.ag2.model;

import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.Switch;
import Grid.Outputter;
import com.ag2.config.PropertyPhosphorusTypeEnum;
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
                "  " + decimalFormat.format(sim.getStat(client, SimBaseStats.Stat.CLIENT_RESULTS_RECEIVED) / sim.getStat(client, SimBaseStats.Stat.CLIENT_JOB_SENT) * 100) + "%");
    }

    @Override
    public void printResource(ResourceNode resourceNode) {

        double recive = sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_JOB_RECEIVED);

        double sent = 0;
        double sentFall = 0;

        double relativeSent = 0;
        double relativeSentFall = 0;

        double timeNoCPUFree = 0;
        double timeNoCPUFreePerCPU = 0;

        double relativeTimeNoCPUFree = 0;
        double resourceFailNoFreespace = 0;
        double relativeResourceFailNoFreespace = 0;

        if (recive > 0) {

            sent = sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_RESULTS_SENT);
            sentFall = sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_SENDING_FAILED);

            relativeSent = sent / recive;
            relativeSentFall = sentFall / recive;

            timeNoCPUFree = sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_BUSY_TIME);
            timeNoCPUFreePerCPU = timeNoCPUFree / resourceNode.getCpuSet().size();

            relativeTimeNoCPUFree = timeNoCPUFreePerCPU / PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SIMULATION_TIME);
            resourceFailNoFreespace = sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_FAIL_NO_FREE_PLACE);
            relativeResourceFailNoFreespace = sim.getStat(resourceNode, SimBaseStats.Stat.RESOURCE_FAIL_NO_FREE_PLACE) / recive;

        }

        resultsAbstractController.addResourceResult(
                resourceNode.toString(),
                " " + recive,
                " " + sent,
                " " + decimalFormat.format(relativeSent * 100) + "%",
                " " + sentFall,
                " " + decimalFormat.format(relativeSentFall * 100) + "%",
                " " + decimalFormat.format(timeNoCPUFreePerCPU),
                " " + decimalFormat.format(relativeTimeNoCPUFree * 100) + "%",
                " " + resourceFailNoFreespace,
                " " + decimalFormat.format(relativeResourceFailNoFreespace * 100) + "%");
    }

    @Override
    public void printSwitch(Switch switch1) {

        double fail_resultMessage = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_DROPPED);

        double switchedResultMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_SWITCHED);
        double totalResultMessage = fail_resultMessage + switchedResultMessages;
        double resultRelative = 0;
        if (totalResultMessage > 0) {
            resultRelative = fail_resultMessage / (totalResultMessage);
        }

        double switchedJobMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_SWITCHED);
        double droppedJobMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_DROPPED);

        double jobRelative = 0;
        double totalJobMessage = switchedJobMessages + droppedJobMessages;

        if (totalJobMessage > 0) {
            jobRelative = droppedJobMessages / totalJobMessage;
        }

        double messagesDropped = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_MESSAGE_DROPPED);
        double messagesSwitched = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_MESSAGE_SWITCHED);

        double messagesReqDropped = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_REQ_MESSAGE_DROPPED);
        double messagesReqSwitched = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_REQ_MESSAGE_SWITCHED);

        double relativeReq = 0;
        double totalReqMessage = messagesReqDropped + messagesReqSwitched;

        if (totalReqMessage > 0) {
            relativeReq = messagesReqDropped / (totalReqMessage);
        }

        double relativeMessage = 0;
        double totalMessage = messagesDropped + messagesSwitched;

        if (totalMessage > 0) 
        {
            relativeMessage = messagesDropped / (totalMessage);
        }

        resultsAbstractController.addSwitchResult(
                switch1.toString(),
                " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_SWITCHED),
                " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_DROPPED),
                " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_SWITCHED),
                " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_DROPPED),
                " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_REQ_MESSAGE_SWITCHED),
                " " + sim.getStat(switch1, SimBaseStats.Stat.SWITCH_REQ_MESSAGE_DROPPED),
                " " + decimalFormat.format(jobRelative * 100) + "%",
                " " + decimalFormat.format(resultRelative * 100) + "%",
                " " + decimalFormat.format(relativeReq * 100) + "%",
                " " + decimalFormat.format(relativeMessage * 100) + "%");
    }
}