package com.ag2.modelo;

import Grid.GridSimulation;
import Grid.GridSimulator;
import Grid.Outputter;
import simbase.SimulationInstance;

public class SimulacionBase {

    private static SimulacionBase simulacionBase;
    private GridSimulator simulador;
    private SimulationInstance simulacion;
    private Outputter outputter;

    private SimulacionBase() 
    {
        simulacion = new GridSimulation("ConfigInit.cfg");
        simulador = new GridSimulator();
        simulacion.setSimulator(simulador);

    }

    public static SimulacionBase getInstance() {
        if (simulacionBase == null) {
            simulacionBase = new SimulacionBase();
        }
        return simulacionBase;
    }

    public void route() {
        simulador.route();
    }

    public void initEntities() {
        simulador.initEntities();
    }

    public void run() {
        simulacion.run();
        
    }

    public GridSimulator getSimulador() {
        return simulador;
    }
}
