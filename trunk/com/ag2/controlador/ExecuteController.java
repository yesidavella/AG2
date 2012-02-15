package com.ag2.controlador;

import Grid.Entity;
import Grid.GridSimulator;
import com.ag2.modelo.NetworkChecker;
import com.ag2.modelo.SimulacionBase;
import com.ag2.presentacion.ErrorsView;
import com.ag2.presentacion.diseño.GraphNode;
import java.util.*;
import java.util.Map.Entry;
import javafx.scene.text.Font;
import simbase.SimulationInstance;

public class ExecuteController extends ExecuteAbstractController {
    private Hashtable<GraphNode,Entity> contenedorParejasNodosExistentes = ContenedorParejasObjetosExistentes.getInstanciaParejasDeNodosExistentes();

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
                    errorWindow.addErrorToShow(errorCounter+". El nodo con nombre \""+graphNode.getNombre().toUpperCase()+"\": "+errorInfo.getValue());
                }
                
            }else{
                errorWindow.addErrorToShow(errorCounter+". LA SIMULACIÓN:"+errorInfo.getValue());
            }
            
            errorCounter++;
        }
    }

    private GraphNode findGraphNode(Object phosNode) {

        for(GraphNode grafNode:contenedorParejasNodosExistentes.keySet()){
            
            if(contenedorParejasNodosExistentes.get(grafNode).equals(phosNode)){
                return grafNode;
            }
        }

        return null;
    }

    @Override
    public void reLoadConfigFile() {
       SimulationInstance.configuration.loadProperties();
      
    }
}