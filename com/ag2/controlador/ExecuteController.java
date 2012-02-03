package com.ag2.controlador;

import Grid.GridSimulator;
import com.ag2.modelo.NetworkChecker;
import com.ag2.modelo.SimulacionBase;
import com.ag2.presentacion.ErrorsView;
import simbase.SimulationInstance;

public class ExecuteController extends ExecuteAbstractController {

    @Override
    public void initNetwork() {
        SimulacionBase.getInstance().initNetwork();
    }

    @Override
    public void run() {

        Thread thread = new Thread(SimulacionBase.getInstance());
        thread.start();
        System.out.println("###########----  RUN ----####################################################");

    }

    @Override
    public void stop() {
        SimulacionBase.getInstance().stop();
    }

    @Override
    public boolean isWellFormedNetwork() {
        
        SimulationInstance simulacion = SimulacionBase.getInstance().getSimulacion();
        GridSimulator simulador = SimulacionBase.getInstance().getSimulador();
        
        //Creo el modelo
        NetworkChecker networkChecker = new NetworkChecker(simulacion,simulador);
        
        networkChecker.check();
        
        if(!networkChecker.passCheck()){
            
            ErrorsView errorWindow = new ErrorsView();
            
            for(String descriptionOfError:networkChecker.getListOfErrors()){
                errorWindow.addErrorToShow(descriptionOfError);
            }
            
            errorWindow.showReport();
            return false;
        }
        
        return true;
    }
}
