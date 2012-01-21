package com.ag2.modelo;

import Grid.GridSimulation;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.Switch;
import com.ag2.controlador.ResultsAbstractController;
import java.util.ArrayList;
import simbase.SimBaseEntity;
import simbase.SimulationInstance;

public class SimulacionBase implements Runnable {
    
    private static SimulacionBase simulacionBase;
    private GridSimulator simulador;
    private SimulationInstance simulacion;
    private OutputterModel outputterModel;
    private static ArrayList<SimBaseEntity> simBaseEntitys;
    
    private SimulacionBase() {
        simulacion = new GridSimulation("ConfigInit.cfg");
        simulador = new GridSimulator();
        simulacion.setSimulator(simulador);
        
    }
    
    public void setOutputterModel(OutputterModel outputterModel) {
        this.outputterModel = outputterModel;
    }
    
    public void setResultsAbstractController(ResultsAbstractController resultsAbstractController) {
        outputterModel.setResultsAbstractController(resultsAbstractController);
    }
    
    public static SimulacionBase getInstance() {
        if (simulacionBase == null) {
            simulacionBase = new SimulacionBase();
        }
        return simulacionBase;
    }
    
    private void route() {
        simulador.route();
    }
    
    private void initEntities() {
        simulador.initEntities();
    }
    
    public void stop() {
        simBaseEntitys = simulador.getEntities();
        simulacionBase = new SimulacionBase();
        simulacionBase.getSimulador().setEntities(simBaseEntitys);        
        
        for (SimBaseEntity entity : simulacionBase.getSimulador().getEntities()) {
            entity.setSimulator(simulacionBase.getSimulador());
        }
    }
    
    @Override
    public void run() {
        
        
        
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
        
    }
    
    public SimulationInstance getSimulacion() {
        return simulacion;
    }
    
    public GridSimulator getSimulador() {
        return simulador;
    }
}
