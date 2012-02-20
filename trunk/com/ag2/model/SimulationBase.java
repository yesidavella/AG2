package com.ag2.model;

import Grid.GridSimulation;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.Switch;
import Grid.Routing.ShortesPathRouting;
import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.controller.ResultsAbstractController;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import simbase.SimBaseEntity;
import simbase.SimulationInstance;

public class SimulationBase implements Runnable, Serializable {

    private static SimulationBase simulacionBase;
    private GridSimulatorModel gridSimulatorModel;
    private SimulationInstance simulationInstance;
    private OutputterModel outputterModel;
    private NodeAdminAbstractController nodeAdminAbstractController;
    private LinkAdminAbstractController linkAdminAbstractController;
    private ResultsAbstractController resultsAbstractController;
    private String id;

    private SimulationBase() {

        simulationInstance = new GridSimulation("ConfigInit.cfg");
        gridSimulatorModel = new GridSimulatorModel();
        simulationInstance.setSimulator(gridSimulatorModel);
        id = new Date().toString();
    }

    public static SimulationBase getInstance() {
        if (simulacionBase == null) {
            simulacionBase = new SimulationBase();
        }
        return simulacionBase;
    }

    public String getId() {
        return id;
    }

    public static void loadInstance(SimulationBase simulacionBase) {
        SimulationBase.simulacionBase = simulacionBase;
    }

    public ResultsAbstractController getResultsAbstractController() {
        return resultsAbstractController;
    }

    public void setNodeAdminAbstractController(NodeAdminAbstractController nodeAdminAbstractController) {
        this.nodeAdminAbstractController = nodeAdminAbstractController;
    }

    public void setOutputterModel(OutputterModel outputterModel) {
        this.outputterModel = outputterModel;

    }

    public void setResultsAbstractController(ResultsAbstractController resultsAbstractController) {
        this.resultsAbstractController = resultsAbstractController;
        outputterModel.setResultsAbstractController(resultsAbstractController);
        gridSimulatorModel.setViewResultsPhosphorus(resultsAbstractController);
    }

    private void route() {
        gridSimulatorModel.route();
    }

    private void initEntities() {
        gridSimulatorModel.initEntities();
    }

    public void stop() {

        simulationInstance.stopEvent = true;
        simulacionBase = new SimulationBase();
        OutputterModel outputterModelNew = new OutputterModel(simulacionBase.getGridSimulatorModel());
        simulacionBase.setResultsAbstractController(resultsAbstractController);
        simulacionBase.setOutputterModel(outputterModelNew);
        simulacionBase.setNodeAdminAbstractController(nodeAdminAbstractController);
        simulacionBase.setLinkAdminAbstractController(linkAdminAbstractController);
        nodeAdminAbstractController.reCreatePhosphorousNodos();
        linkAdminAbstractController.reCreatePhosphorousLinks();

    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();

    }

    public void initNetwork() {
        simulationInstance.stopEvent = false;
        gridSimulatorModel.setRouting(new ShortesPathRouting(gridSimulatorModel));
        route();
    }

    @Override
    public void run() {

        initEntities();
        simulationInstance.run();


        for (SimBaseEntity entity : gridSimulatorModel.getEntities()) {
            if (entity instanceof ClientNode) {
                outputterModel.printClient((ClientNode) entity);
            } else if (entity instanceof Switch) {
                outputterModel.printSwitch((Switch) entity);
            } else if (entity instanceof ResourceNode) {
                outputterModel.printResource((ResourceNode) entity);
            }
        }
        stop();

    }

    public SimulationInstance getSimulationInstance() {
        return simulationInstance;
    }

    public GridSimulator getGridSimulatorModel() {
        return gridSimulatorModel;
    }

    public void setLinkAdminAbstractController(LinkAdminAbstractController controladorAdminEnlace) {
        this.linkAdminAbstractController = controladorAdminEnlace;
    }
}
