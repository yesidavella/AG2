package com.ag2.modelo;

import Grid.Entity;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import java.util.ArrayList;
import simbase.SimBaseEntity;
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
        checkAmountOfNodesCreated();
        checkLinksBetweenNodes();
        checkCorrectNodesWithBroker();
    }

    public ArrayList<String> getListOfErrors() {
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
        if (simulador.getEntities().size() <= 0) {
            listOfErrors.add("*La simulación no contiene ningun nodo, asegurese de colocar almenos uno de cada uno de los siguientes:Enrutador,Cliente,Cluster y Servivio.");
        }
    }

    private void checkLinksBetweenNodes() {

        for (SimBaseEntity entity : simulador.getEntities()) {
            Entity node = (Entity) entity;

            if (node.getOutPorts().size() <= 0) {
                //FIXME:Estoy sacando el nombre del nodo phosphorous y no del node q esta en la GUI, pero el lio adicional es q aqui NO debo tener elementos graficos por q esto es un modelo. 
                listOfErrors.add("*El nodo con nombre \"" + node.getId() + "\" no tiene enlaces.");
            }
        }
    }

    private void checkCorrectNodesWithBroker() {
        
        ArrayList<ClientNode> clienteNodes = new ArrayList<ClientNode>();
        ArrayList<ResourceNode> ResourceNodes = new ArrayList<ResourceNode>();
        
        for (SimBaseEntity entity : simulador.getEntities()) {

            if (entity instanceof ClientNode) {
                clienteNodes.add((ClientNode)entity);
            }else if(entity instanceof ResourceNode){
                ResourceNodes.add((ResourceNode)entity);
            }
        }
        
        for(ClientNode clien:clienteNodes){
            if(clien.getServiceNode()==null){
                listOfErrors.add("*El Nodo Cliente con nombre \"" + clien.getId() + "\" no tiene Nodo de Servicio registrado."); 
            }
        }
        
        for(ResourceNode resource:ResourceNodes){
            if(resource.getServiceNodes().size()<=0){
                listOfErrors.add("*El Nodo de Recurso con nombre \"" + resource.getId() + "\" debe tener almenos un Nodo de Servicio registrado."); 
            }
        }
    }
}
