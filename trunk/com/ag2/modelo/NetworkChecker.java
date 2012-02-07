package com.ag2.modelo;

import Grid.Entity;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.Switch;
import Grid.Routing.GridVertex;
import Grid.Routing.Routing;
import Grid.Routing.RoutingViaJung;
import Grid.Routing.ShortesPathRouting;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JOptionPane;
import simbase.SimBaseEntity;
import simbase.SimulationInstance;

public class NetworkChecker {

    private SimulationInstance simulation;
    private GridSimulator simulator;
    private HashMap<Object, String> listOfErrors;
    private boolean statusOfCheck = false;

    public NetworkChecker(SimulationInstance simulation, GridSimulator simulator) {
        this.simulation = simulation;
        this.simulator = simulator;
        listOfErrors = new HashMap<Object, String>();
    }

    public void check() {
        checkIsolatedNetworks();
        checkAmountOfNodesCreated();
        checkLinksBetweenNodes();
        checkCorrectNodesWithBroker();
        checkSwitchesWithOneLink();
    }

    public HashMap<Object, String> getListOfErrors() {
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
            addError(simulator, " La simulación no contiene ningun nodo, asegurese de colocar almenos uno de cada uno de los siguientes: Enrutador,Cliente,Cluster y Servivio.");
        }
    }

    private void checkLinksBetweenNodes() {

        for (SimBaseEntity simBaseEntity : simulator.getEntities()) {
            Entity node = (Entity) simBaseEntity;

            if (node.getOutPorts().size() <= 0) {
                addError(node, " \n►No tiene enlaces.");
            }
        }
    }

    private void checkCorrectNodesWithBroker() {

        for (SimBaseEntity clienNode : simulator.getEntitiesOfType(ClientNode.class)) {
            if (((ClientNode) clienNode).getServiceNode() == null) {
                addError((ClientNode) clienNode, " \n►Al ser un Cliente debe tener un Nodo de Servicio registrado.");
            }
        }

        for (SimBaseEntity resourceNode : simulator.getEntitiesOfType(ResourceNode.class)) {
            if (((ResourceNode) resourceNode).getServiceNodes().size() <= 0) {
                addError((ResourceNode) resourceNode, " \n►Al ser un Recurso debe tener almenos un Nodo de Servicio registrado.");
            }
        }
    }

    private void addError(Object node, String description) {
        StringBuffer previousDescription = new StringBuffer();

        if (listOfErrors.containsKey(node)) {
            previousDescription.append(listOfErrors.get(node));
        }
        listOfErrors.put(node, previousDescription.append(description).toString());
    }

    private void checkSwitchesWithOneLink() {

        for (SimBaseEntity oneSwitch : simulator.getEntitiesOfType(Switch.class)) {
            if (oneSwitch.getOutPorts().size() == 1) {
                addError((Switch) oneSwitch, " \n►Solo tiene un enlace, debe tener almenos otro.");
            }
        }
    }

    /**
     * Verifica que no hayan partes de red aisladas. Todos los nodos deben estar
     * conectados a una misma red.
     */
    private void checkIsolatedNetworks() {

        Routing routing = simulator.getRouting();

        if (routing instanceof RoutingViaJung) {

            Boolean foundIsolatedNetworks = false;
            Graph networkRoutingGraph = ((RoutingViaJung) (simulator.getRouting())).getHybridNetwork();
            GridVertex pivotVertex = null;
            
            for (Iterator itVertexes = networkRoutingGraph.getVertices().iterator(); itVertexes.hasNext() && !foundIsolatedNetworks;) {
                GridVertex vertex = (GridVertex) itVertexes.next();
                
                if(pivotVertex==null){
                    pivotVertex = vertex;
                }
                
                if(pivotVertex!=vertex && pivotVertex.getTheEntity().getHopCount(vertex.getTheEntity())==-1){
                    addError(simulator,"La simulacion contiene redes disconexas.");
                    foundIsolatedNetworks=true;
                }                   
            }
        } else if (routing instanceof ShortesPathRouting) {
            JOptionPane.showMessageDialog(null, "El enrutamiento se esta haciendo de una forma no esperada. El programa se cerrara.");
            System.exit(1);
        }
    }
}
