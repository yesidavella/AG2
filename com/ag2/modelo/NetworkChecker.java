package com.ag2.modelo;

import Grid.Entity;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Interfaces.Switch;
import Grid.Port.GridOutPort;
import Grid.Routing.GridVertex;
import Grid.Routing.Routing;
import Grid.Routing.RoutingViaJung;
import Grid.Routing.ShortesPathRouting;
import edu.uci.ics.jung.graph.Graph;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JOptionPane;
import simbase.Port.SimBaseOutPort;
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
        checkNecessaryNodes();
        checkLinksBetweenNodes();
        checkCorrectNodesWithBroker();
        checkSwitchesWithWellLinked();
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
            addError(simulator, " \n►No contiene ningun nodo.");
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

    private void checkSwitchesWithWellLinked() {

        for (SimBaseEntity oneSwitch : simulator.getEntitiesOfType(Switch.class)) {
            if (oneSwitch.getOutPorts().size() == 1) {
                addError((Switch)oneSwitch, " \n►Solo tiene un enlace, debe tener almenos otro.");
            }else if (oneSwitch.getOutPorts().size() > 1){
                SimBaseEntity targetNode = null;
                boolean foundDiffTargets = false;
                for(SimBaseOutPort oneOutPort:oneSwitch.getOutPorts()){
                    
                    if(targetNode == null){
                        targetNode = oneOutPort.getTarget().getOwner();
                    }else{
                        if(!targetNode.equals(oneOutPort.getTarget().getOwner())){
                            foundDiffTargets=true;
                        }
                    }
                }
                
                if(!foundDiffTargets){
                    addError((Switch)oneSwitch," \n►Tiene varios enlaces pero todos van dirigidos al mismo nodo. Genere un enlace con otro nodo.");
                }
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

                if (pivotVertex == null) {
                    pivotVertex = vertex;
                }

                if (pivotVertex != vertex && pivotVertex.getTheEntity().getHopCount(vertex.getTheEntity()) == -1) {
                    addError(simulator, " \n►Contiene redes disconexas.");
                    foundIsolatedNetworks = true;
                }
            }
        } else if (routing instanceof ShortesPathRouting) {
            JOptionPane.showMessageDialog(null, "El enrutamiento se esta haciendo de una forma no esperada. El programa se cerrara.");
            System.exit(1);
        }
    }

    private void checkNecessaryNodes() {

        if (simulator.getEntitiesOfType(ClientNode.class).size() == 0) {
            addError(simulator, " \n►Debe haber por lo menos un \"Nodo Cliente\".");
        }

        if (simulator.getEntitiesOfType(ResourceNode.class).size() == 0) {
            addError(simulator, " \n►Debe haber por lo menos un \"Nodo de Recurso\".");
        }
        
        if (simulator.getEntitiesOfType(ServiceNode.class).size() == 0) {
            addError(simulator, " \n►Debe haber por lo menos un \"Nodo de Servicio\".");
        }
        if (simulator.getEntitiesOfType(Switch.class).size() == 0) {
            addError(simulator, " \n►Debe haber por lo menos un \"Nodo de Conmutación\".");
        }
    }
}
