package com.ag2.model;

import Grid.Entity;
import Grid.GridSimulation;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.Switch;
import Grid.Routing.RoutingViaJung;
import Grid.Routing.ShortesPathRouting;
import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.controller.ResultsAbstractController;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import simbase.SimBaseEntity;
import simbase.SimulationInstance;

public class SimulationBase implements Runnable, Serializable {

    private static SimulationBase simulacionBase;
    private GridSimulatorModel simulador;
    private SimulationInstance simulacion;
    private OutputterModel outputterModel;
    private NodeAdminAbstractController controladorAbstractoAdminNodo;
    private LinkAdminAbstractController controladorAdminEnlace;
    private ResultsAbstractController resultsAbstractController;
    private String id;

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

    private SimulationBase() {

        simulacion = new GridSimulation("ConfigInit.cfg");
        simulador = new GridSimulatorModel();
        simulacion.setSimulator(simulador);
        id = new Date().toString(); 


    }

    public ResultsAbstractController getResultsAbstractController() {
        return resultsAbstractController;
    }

    public void setNodeAdminAbstractController(NodeAdminAbstractController nodeAdminAbstractController) {
        this.controladorAbstractoAdminNodo = nodeAdminAbstractController;
    }

    public void setOutputterModel(OutputterModel outputterModel) {
        this.outputterModel = outputterModel;
        
        
    }

    public void setResultsAbstractController(ResultsAbstractController resultsAbstractController) {
        this.resultsAbstractController = resultsAbstractController;
        outputterModel.setResultsAbstractController(resultsAbstractController);
        simulador.setViewResultsPhosphorus(resultsAbstractController);

    }

    private void route() {
        simulador.route();
    }

    private void initEntities() {
        simulador.initEntities();
    }

    public void stop() {

        simulacion.stopEvent = true;
        simulacionBase = new SimulationBase();


        OutputterModel outputterModelNew = new OutputterModel(simulacionBase.getSimulador());
        simulacionBase.setResultsAbstractController(resultsAbstractController);
        simulacionBase.setOutputterModel(outputterModelNew);
        //outputterModelNew.setResultsAbstractController(resultsAbstractController);


        simulacionBase.setNodeAdminAbstractController(controladorAbstractoAdminNodo);
        simulacionBase.setLinkAdminAbstractController(controladorAdminEnlace);
        controladorAbstractoAdminNodo.reCreatePhosphorousNodos();
        controladorAdminEnlace.reCreatePhosphorousLinks();

    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();

    }

    public void initNetwork() {
        simulacion.stopEvent = false;

        simulador.setRouting(new ShortesPathRouting(simulador));
        route();
    }

    @Override
    public void run() {

     
        initEntities();
        simulacion.run();

        
        for (SimBaseEntity entity : simulador.getEntities()) {
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

    public SimulationInstance getSimulacion() {
        return simulacion;
    }

    public GridSimulator getSimulador() {
        return simulador;
    }

    public void setLinkAdminAbstractController(LinkAdminAbstractController controladorAdminEnlace) {
        this.controladorAdminEnlace = controladorAdminEnlace;
    }
}
