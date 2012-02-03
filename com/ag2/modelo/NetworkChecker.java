package com.ag2.modelo;

import Grid.Entity;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.Switch;
import java.util.ArrayList;
import java.util.HashMap;
import simbase.SimBaseEntity;
import simbase.SimulationInstance;

public class NetworkChecker {

    private SimulationInstance simulation;
    private GridSimulator simulator;
    private HashMap<Entity,String> listOfErrors;
    private boolean statusOfCheck = false;

    public NetworkChecker(SimulationInstance simulation, GridSimulator simulator) {
        this.simulation = simulation;
        this.simulator = simulator;
        listOfErrors = new HashMap<Entity,String>();
    }

    public void check() {
        checkAmountOfNodesCreated();
        checkLinksBetweenNodes();
        checkCorrectNodesWithBroker();
        checkSwitchesWithOneLink();
        checkIsolatedNetworks();
    }

    public HashMap<Entity,String> getListOfErrors() {
        return listOfErrors;
    }

    public boolean passCheck() {

        if (listOfErrors.size() == 0) {
            statusOfCheck = true;
        } else {
            statusOfCheck = false;
        }
        return statusOfCheck;
    }

    private void checkAmountOfNodesCreated() {
        if (simulator.getEntities().size() <= 0) {
            addError(null,". La simulación no contiene ningun nodo, asegurese de colocar almenos uno de cada uno de los siguientes:Enrutador,Cliente,Cluster y Servivio.");
        }
    }

    private void checkLinksBetweenNodes() {

        for (SimBaseEntity simBaseEntity : simulator.getEntities()) {
            Entity node = (Entity)simBaseEntity;

            if (node.getOutPorts().size() <= 0) {
                addError(node,". No tiene enlaces");
            }
        }
    }

    private void checkCorrectNodesWithBroker() {
        
        for(SimBaseEntity clienNode:simulator.getEntitiesOfType(ClientNode.class)){
            if(((ClientNode)clienNode).getServiceNode()==null){
                addError((ClientNode)clienNode,". Al ser un Cliente debe tiener un Nodo de Servicio registrado"); 
            }
        }
        
        for(SimBaseEntity resourceNode:simulator.getEntitiesOfType(ResourceNode.class)){
            if(((ResourceNode)resourceNode).getServiceNodes().size()<=0){
                addError((ResourceNode)resourceNode,". Al ser un Recurso debe tener almenos un Nodo de Servicio registrado"); 
            }
        }
    }
    
    private void addError(Entity node,String description){
        StringBuffer previousDescription=new StringBuffer();
        
        if(listOfErrors.containsKey(node)){
            previousDescription.append(listOfErrors.get(node));
        }
        listOfErrors.put(node, previousDescription.append(description).append(".").toString());
    }

    private void checkSwitchesWithOneLink() {
        
        for(SimBaseEntity oneSwitch:simulator.getEntitiesOfType(Switch.class)){
            if(oneSwitch.getOutPorts().size()==1){
                addError((Switch)oneSwitch,". Solo tiene un enlace, debe tener almeno otro");
            }
        }
    }

    private void checkIsolatedNetworks() {
        System.out.print("Figurin candonga.... nada q hago lo de las redes aisladas :D");
    }
}
