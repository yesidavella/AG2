package com.ag2.model;

import Grid.Entity;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Interfaces.Switch;
import Grid.Routing.*;
import edu.uci.ics.jung.graph.Graph;
import java.util.HashMap;
import java.util.Iterator;
import simbase.Port.SimBaseOutPort;
import simbase.SimBaseEntity;
import trs.core.Connection;
import trs.core.NetworkRouting;

public class NetworkChecker {

    private HashMap<Object, String> listOfErrors;
    private boolean statusOfCheck = false;

    public NetworkChecker() {       
        listOfErrors = new HashMap<Object, String>();
    }

    public void check() {
        checkIsolatedNetworks();
        checkAmountOfNodesCreated();
        checkNecessaryNodes();
        checkLinksBetweenNodes();
        checkCorrectNodesWithBroker();
        checkSwitchesWellLinked();
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

    private void checkAmountOfNodesCreated()
    {
        
        if (SimulationBase.getInstance().getGridSimulatorModel().getEntities().size() <= 0) {
            addError(SimulationBase.getInstance().getGridSimulatorModel(), " \n►No contiene ningun nodo.");
        }
    }

    private void checkLinksBetweenNodes() {

        for (SimBaseEntity simBaseEntity : SimulationBase.getInstance().getGridSimulatorModel().getEntities()) {
            Entity node = (Entity) simBaseEntity;

            if (node.getOutPorts().size() <= 0) {
                addError(node, " \n►No tiene enlaces.");
            }
        }
    }

    private void checkCorrectNodesWithBroker() {
        
        for (SimBaseEntity clienNode : SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(ClientNode.class)) {
            if (((ClientNode) clienNode).getServiceNode() == null) {
                addError((ClientNode) clienNode, " \n►Al ser un Cliente debe tener un Nodo de Servicio registrado.");
            }
        }

        for (SimBaseEntity resourceNode : SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(ResourceNode.class)) {
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

    private void checkSwitchesWellLinked() {

        for (SimBaseEntity oneSwitch : SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(Switch.class)) {

            if (oneSwitch.getOutPorts().size() == 1) {
                addError((Switch) oneSwitch, " \n►Solo tiene un enlace, debe tener almenos otro.");
            } else if (oneSwitch.getOutPorts().size() > 1) {

                SimBaseEntity targetNode = null;
                boolean foundDiffTargets = false;

                for (SimBaseOutPort oneOutPort : oneSwitch.getOutPorts()) {

                    if (targetNode == null) {
                        targetNode = oneOutPort.getTarget().getOwner();
                    } else {
                        if (!targetNode.equals(oneOutPort.getTarget().getOwner())) {
                            foundDiffTargets = true;
                        }
                    }
                }

                if (!foundDiffTargets) {
                    addError((Switch) oneSwitch, " \n►Tiene varios enlaces pero todos van dirigidos al mismo nodo. Genere un enlace con otro nodo.");
                }
            }
        }
    }

    /**
     * Verifica que no hayan partes de red aisladas. Todos los nodos deben estar
     * conectados a una misma red.
     */
    private void checkIsolatedNetworks() {

        Routing routing = SimulationBase.getInstance().getGridSimulatorModel().getRouting();
        Boolean foundIsolatedNetworks = false;

        if (routing instanceof RoutingViaJung) {
            System.out.println("Entro a:RoutingViaJung");
            Graph networkRoutingGraph = ((RoutingViaJung) routing).getHybridNetwork();
            GridVertex pivotVertex = null;

            Iterator<GridVertex> itVertexes = networkRoutingGraph.getVertices().iterator();
            while ( itVertexes.hasNext() && !foundIsolatedNetworks) {
                GridVertex vertex = itVertexes.next();

                if (pivotVertex == null) {
                    pivotVertex = vertex;
                    continue;
                }

                if (pivotVertex.getTheEntity().getHopCount(vertex.getTheEntity()) == -1) {
                    addError(SimulationBase.getInstance().getGridSimulatorModel(), " \n►Contiene redes disconexas.");
                    foundIsolatedNetworks = true;
                }
            }
        } else if (routing instanceof ShortesPathRouting) {
            System.out.println("Entro a:ShortesPathRouting");
            NetworkProxy networkProxy = new NetworkProxy();
            networkProxy.setHyrbidNetwork(((ShortesPathRouting) routing).getHyrbidNetwork());
            NetworkRouting networkRouting = ((ShortesPathRouting) routing).getHybridNetworkRouting();

            String pivotVertexID = null;

            Iterator<String> itVertexesID = networkProxy.getNodeIDs().iterator();
            while (itVertexesID.hasNext() && !foundIsolatedNetworks) {
                
                String vertexID = itVertexesID.next();

                if (pivotVertexID == null) {
                    pivotVertexID = vertexID;
                    continue;
                }

                Iterator<Connection> itConns = networkRouting.findConnections(pivotVertexID, vertexID).iterator();

                while (itConns.hasNext()) {
                    Connection conn = itConns.next();

                    if (conn.getRoute() == null) {
                        addError(SimulationBase.getInstance().getGridSimulatorModel(), " \n►Contiene redes disconexas.");
                        foundIsolatedNetworks = true;
                    }
                }
            }
        }
    }

    private void checkNecessaryNodes() {

        if (SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(ClientNode.class).size() == 0) {
            addError(SimulationBase.getInstance().getGridSimulatorModel(), " \n►Debe haber por lo menos un \"Nodo Cliente\".");
        }

        if (SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(ResourceNode.class).size() == 0) {
            addError(SimulationBase.getInstance().getGridSimulatorModel(), " \n►Debe haber por lo menos un \"Nodo de Recurso\".");
        }

        if (SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(ServiceNode.class).size() == 0) {
            addError(SimulationBase.getInstance().getGridSimulatorModel(), " \n►Debe haber por lo menos un \"Nodo de Servicio\".");
        }
        if (SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(Switch.class).size() == 0) {
            addError(SimulationBase.getInstance().getGridSimulatorModel(), " \n►Debe haber por lo menos un \"Nodo de Conmutación\".");
        }
    }
}