package com.ag2.modelo;

import Grid.Entity;
import Grid.GridSimulation;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.Switch;
import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.controlador.ResultsAbstractController;
import java.io.Serializable;
import java.util.Iterator;
import simbase.SimBaseEntity;
import simbase.SimulationInstance;

public class SimulacionBase implements Runnable, Serializable {

    private static SimulacionBase simulacionBase;
    private  GridSimulatorModel simulador;
    private  SimulationInstance simulacion;
    private OutputterModel outputterModel;
    private ControladorAbstractoAdminNodo controladorAbstractoAdminNodo;
    private ControladorAbstractoAdminEnlace controladorAdminEnlace;
    private ResultsAbstractController resultsAbstractController;

    public static SimulacionBase getInstance() {
        if (simulacionBase == null) {
            simulacionBase = new SimulacionBase();
        }
        return simulacionBase;
    }
    public static void loadInstance(SimulacionBase simulacionBase) {
       
        SimulacionBase.simulacionBase = simulacionBase; 
        
    }

    private SimulacionBase() {

        simulacion = new GridSimulation("ConfigInit.cfg");
        simulador = new GridSimulatorModel();
        simulacion.setSimulator(simulador);

    }

    public ResultsAbstractController getResultsAbstractController() {
        return resultsAbstractController;
    }

    public void setControladorAbstractoAdminNodo(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo) {
        this.controladorAbstractoAdminNodo = controladorAbstractoAdminNodo;
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
        simulacionBase = new SimulacionBase();


        OutputterModel outputterModelNew = new OutputterModel(simulacionBase.getSimulador());
        simulacionBase.setResultsAbstractController(resultsAbstractController);
        simulacionBase.setOutputterModel(outputterModelNew);
        //outputterModelNew.setResultsAbstractController(resultsAbstractController);


        simulacionBase.setControladorAbstractoAdminNodo(controladorAbstractoAdminNodo);
        simulacionBase.setControladorAdminEnlace(controladorAdminEnlace);
        controladorAbstractoAdminNodo.reCreatePhosphorousNodos();
        controladorAdminEnlace.reCreatePhosphorousLinks();

    }

    @Override
    public void run() {

        simulacion.stopEvent = false;
        route();
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

    public void setControladorAdminEnlace(ControladorAbstractoAdminEnlace controladorAdminEnlace) {
        this.controladorAdminEnlace = controladorAdminEnlace;
    }
}
