package com.ag2.modelo;

import Grid.GridSimulator;
import java.util.ArrayList;
import simbase.SimulationInstance;

public class NetworkChecker {

    private SimulationInstance simulacion;
    private GridSimulator simulador;
    private ArrayList<String> listOfErrors;
    private boolean statusOfCheck = false;

    public NetworkChecker(SimulationInstance simulacion, GridSimulator simulador) {
        this.simulacion = simulacion;
        this.simulador = simulador;
        listOfErrors = new ArrayList<String>();
    }

    public void check() {
        listOfErrors.add("Error 1.");
        listOfErrors.add("Error 2.");
        listOfErrors.add("Error 3.");
        listOfErrors.add("Error 4.");
        listOfErrors.add("Error 5.");
    }
    
    public ArrayList<String> getListOfErrors(){
        return listOfErrors;
    }

    public boolean passCheck() {
        return statusOfCheck;
    }   
}
