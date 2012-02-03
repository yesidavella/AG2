package com.ag2.controlador;

import Grid.Entity;
import Grid.GridSimulator;
import com.ag2.modelo.NetworkChecker;
import com.ag2.modelo.SimulacionBase;
import com.ag2.presentacion.ErrorsView;
import com.ag2.presentacion.diseño.NodoGrafico;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
            
            //Esta es la vista
            ErrorsView errorWindow = new ErrorsView();
            
            Iterator itListOfErrors = networkChecker.getListOfErrors().entrySet().iterator();
            
            int errorCounter=1;
            while(itListOfErrors.hasNext()){
                Map.Entry<Entity,String> errorInfo = (Map.Entry<Entity,String>)itListOfErrors.next();
                NodoGrafico graphNode = findGraphNode(errorInfo.getKey());
                errorWindow.addErrorToShow(errorCounter+((graphNode==null)?"":". El nodo con nombre: "+graphNode.getNombre())+errorInfo.getValue());
                errorCounter++;
            }

            errorWindow.showReport();
            return false;
        }
        
        return true;
    }


    private NodoGrafico findGraphNode(Entity phosNode) {

        for(NodoGrafico grafNode:ContenedorParejasObjetosExistentes.getInstanciaParejasDeNodosExistentes().keySet()){
            
            if(ContenedorParejasObjetosExistentes.getInstanciaParejasDeNodosExistentes().get(grafNode).equals(phosNode)){
                return grafNode;
            }
        }

        return null;
    }
}
