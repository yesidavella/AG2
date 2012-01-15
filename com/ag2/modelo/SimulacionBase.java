package com.ag2.modelo;

import Grid.Entity;
import Grid.GridSimulation;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Interfaces.Switch;
import Grid.Outputter;
import simbase.SimBaseEntity;
import simbase.SimulationInstance;

public class SimulacionBase {

    private static SimulacionBase simulacionBase;
    private GridSimulator simulador;
    private SimulationInstance simulacion;
    private OutputterModel outputterModel;

    
    private SimulacionBase() 
    {
        simulacion = new GridSimulation("ConfigInit.cfg");
        simulador = new GridSimulator();
        simulacion.setSimulator(simulador);
        
    }

    public void setOutputterModel(OutputterModel outputterModel) 
    {
        this.outputterModel = outputterModel;
    }
    
    

    public static SimulacionBase getInstance() {
        if (simulacionBase == null) {
            simulacionBase = new SimulacionBase();
        }
        return simulacionBase;
    }

    public void route() 
    {
        simulador.route();
        
        for(SimBaseEntity entity: simulador.getEntities())
        {
            if(entity instanceof ClientNode)
            {
                outputterModel.printClient((ClientNode)entity);
            } 
            else if(entity instanceof  Switch)
            {
               outputterModel.printSwitch((Switch)entity); 
            }
            else if(entity instanceof ResourceNode )
            {
               outputterModel.printResource((ResourceNode)entity);
            }            
        }
            
    }

    public void initEntities() {
        simulador.initEntities();
    }

    public void run() {
        simulacion.run();
        
    }

    public SimulationInstance getSimulacion() {
        return simulacion;
    }

    public GridSimulator getSimulador() {
        return simulador;
    }
}
