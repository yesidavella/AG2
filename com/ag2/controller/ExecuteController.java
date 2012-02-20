package com.ag2.controller;

import Grid.Entity;
import Grid.GridSimulator;
import com.ag2.model.NetworkChecker;
import com.ag2.model.SimulationBase;
import com.ag2.presentation.ErrorsView;
import com.ag2.presentation.design.GraphNode;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import simbase.SimulationInstance;

public class ExecuteController extends ExecuteAbstractController {
    private Hashtable<GraphNode,Entity> nodeMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer();

    @Override
    public void initNetwork() {
        SimulationBase.getInstance().initNetwork();
    }

    @Override
    public void run() {

        Thread thread = new Thread(SimulationBase.getInstance());
        thread.start();
        System.out.println("###########----  RUN ----####################################################");

    }

    @Override
    public void stop() {
        SimulationBase.getInstance().stop();
    }

    @Override
    public boolean isWellFormedNetwork() {
        
        SimulationInstance simulacion = SimulationBase.getInstance().getSimulationInstance();
        GridSimulator simulador = SimulationBase.getInstance().getGridSimulatorModel();
        
        //Hace las veces de modelo
        NetworkChecker networkChecker = new NetworkChecker(simulacion,simulador);
        
        networkChecker.check();
        
        if(!networkChecker.passCheck()){
            
            ErrorsView errorWindow = new ErrorsView();
            chargeErrorsListToShow(networkChecker, errorWindow);
            errorWindow.showReport();
            
            return false;
        }
        
        return true;
    }

    private void chargeErrorsListToShow(NetworkChecker networkChecker, ErrorsView errorWindow) {
        
        Iterator itListOfErrors = networkChecker.getListOfErrors().entrySet().iterator();
        
        int errorCounter=1;
        while(itListOfErrors.hasNext()){
            
            Map.Entry<Object,String> errorInfo = (Map.Entry<Object,String>)itListOfErrors.next();
            
            if(errorInfo.getKey() instanceof Entity){
                GraphNode graphNode = findGraphNode(errorInfo.getKey());
                
                if(graphNode!= null){
                    errorWindow.addErrorToShow(errorCounter+". El nodo con nombre \""+graphNode.getName().toUpperCase()+"\": "+errorInfo.getValue());
                }
                
            }else{
                errorWindow.addErrorToShow(errorCounter+". LA SIMULACIÓN:"+errorInfo.getValue());
            }
            
            errorCounter++;
        }
    }

    private GraphNode findGraphNode(Object object) {

        for(GraphNode graphNode:nodeMatchCoupleObjectContainer.keySet()){
            
            if(nodeMatchCoupleObjectContainer.get(graphNode).equals(object)){
                return graphNode;
            }
        }

        return null;
    }

    @Override
    public void reLoadConfigFile() {
       SimulationInstance.configuration.loadProperties();
      
    }
}