package com.ag2.model;

import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Interfaces.Switch;
import Grid.Outputter;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.controller.ResultsAbstractController;
import com.ag2.controller.ResultsChartAbstractController;
import simbase.Stats.SimBaseStats;

public class OutputterModel extends Outputter {

    private ResultsAbstractController resultsController;
    private ResultsChartAbstractController chartAbstractController;

    public OutputterModel(GridSimulator gridSimulator) {
        super(gridSimulator);
        SimulationBase.getInstance().setOutputterModel(this);
    }

    public void setResultsAbstractController(ResultsAbstractController resultsAbstractController) {
        this.resultsController = resultsAbstractController;
    }

    public void setChartAbstractController(ResultsChartAbstractController chartAbstractController) {
        this.chartAbstractController = chartAbstractController;
    }

    @Override
    public void printClient(ClientNode client) {

        double requestCreated = sim.getStat(client, SimBaseStats.Stat.CLIENT_CREATED_REQ);
        double requestSent = sim.getStat(client, SimBaseStats.Stat.CLIENT_REQ_SENT);
        double requestNoSent = sim.getStat(client, SimBaseStats.Stat.CLIENT_NO_REQ_SENT);
        double jobSent = sim.getStat(client, SimBaseStats.Stat.CLIENT_JOB_SENT);
        double jobNoSent = sim.getStat(client, SimBaseStats.Stat.CLIENT_SENDING_FAILED);
        double resultReceive = sim.getStat(client, SimBaseStats.Stat.CLIENT_RESULTS_RECEIVED);

        double relativeRequestSent = 0;
        double relativeJobSent = 0;
        double relativeReceiveResult_jobSent = 0;
        double relativeReceiveResult_requsetSent = 0;
        double relativeReceiveresult_requestCreated = 0;

        if (requestCreated > 0) {
            relativeRequestSent = requestSent / requestCreated;
            relativeReceiveresult_requestCreated = resultReceive / requestCreated;
        }
        if (requestSent > 0) {
            relativeJobSent = jobSent / requestSent;
            relativeReceiveResult_requsetSent = resultReceive / requestSent;
        }
        if (jobSent > 0) {
            relativeReceiveResult_jobSent = resultReceive / jobSent;
        }

        resultsController.addClientResult(
                client.toString(),
                requestCreated,
                requestSent,
                requestNoSent,
                jobSent,
                jobNoSent,
                resultReceive,
                relativeRequestSent,
                relativeJobSent,
                relativeReceiveResult_jobSent,
                relativeReceiveResult_requsetSent,
                relativeReceiveresult_requestCreated);

        chartAbstractController.createClientResult(client.toString(), requestCreated,  requestSent,   jobSent, resultReceive);
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

        chartAbstractController.createResourceResult(resourceNode.toString(), recive, sent);
        resultsController.addResourceResult(
                resourceNode.toString(),
                recive,
                sent,
                relativeSent,
                sentFall,
                relativeSentFall,
                timeNoCPUFreePerCPU,
                relativeTimeNoCPUFree,
                resourceFailNoFreespace,
                relativeResourceFailNoFreespace);
    }

    @Override
    public void printSwitch(Switch switch1) {

        double fail_resultMessage = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_DROPPED);

        double switchedResultMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_SWITCHED);
        double totalResultMessage = fail_resultMessage + switchedResultMessages;
        double relativeResult = 0;
        if (totalResultMessage > 0) {
            relativeResult = fail_resultMessage / (totalResultMessage);
        }

        double switchedJobMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_SWITCHED);
        double droppedJobMessages = sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_DROPPED);

        double relativeJob = 0;
        double totalJobMessage = switchedJobMessages + droppedJobMessages;

        if (totalJobMessage > 0) {
            relativeJob = droppedJobMessages / totalJobMessage;
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

        if (totalMessage > 0) {
            relativeMessage = messagesDropped / (totalMessage);
        }

        resultsController.addSwitchResult(
                switch1.toString(),
                sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_SWITCHED),
                sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBMESSAGE_DROPPED),
                sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_SWITCHED),
                sim.getStat(switch1, SimBaseStats.Stat.SWITCH_JOBRESULTMESSAGE_DROPPED),
                sim.getStat(switch1, SimBaseStats.Stat.SWITCH_REQ_MESSAGE_SWITCHED),
                sim.getStat(switch1, SimBaseStats.Stat.SWITCH_REQ_MESSAGE_DROPPED),
                relativeJob,
                relativeResult,
                relativeReq,
                relativeMessage);
    }

    public void printBroker(ServiceNode serviceNode) {

        double registrationReceived = sim.getStat(serviceNode, SimBaseStats.Stat.SERVICENODE_REGISTRATION_RECEIVED);
        double reqRecieved = sim.getStat(serviceNode, SimBaseStats.Stat.SERVICENODE_REQ_RECIEVED);
        double noFreeResouce = sim.getStat(serviceNode, SimBaseStats.Stat.SERVICENODE_NO_FREE_RESOURCE);
        double reqAckSent = sim.getStat(serviceNode, SimBaseStats.Stat.SERVICENODE_REQ_ACK_SENT);
        double sendingFailed = sim.getStat(serviceNode, SimBaseStats.Stat.SERVICENODE_SENDING_FAILED);
        double relativeAckSent = reqAckSent/reqRecieved; 

        resultsController.addBrokerResults(
                serviceNode.toString(),
                registrationReceived,
                reqRecieved,
                noFreeResouce,
                reqAckSent,
                sendingFailed,
                relativeAckSent);
        printCircuitsStats();
    }
}
