package com.ag2.controller;

import Grid.Entity;
import com.ag2.model.*;
import com.ag2.presentation.design.GraphLink;
import com.ag2.presentation.design.GraphNode;
import java.util.ArrayList;
import simbase.SimBaseEntity;

public class OCSAdminController extends LinkAdminAbstractController {

    @Override
    public boolean createLink(GraphNode sourceGraphNode, GraphNode destinationGraphNode) {

        GridSimulatorModel simulatorModel = (GridSimulatorModel) SimulationBase.getInstance().getGridSimulatorModel();

        Entity phosphorusNodeA = (Entity) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(sourceGraphNode);
        Entity phosphorusNodeB = (Entity) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(destinationGraphNode);

        simulatorModel.getDesignedOCSCircuits().add(new OCSRequest(phosphorusNodeA, phosphorusNodeB));

        System.out.println("Solicito OCS circuito entre:" + sourceGraphNode.getName() + " -y- " + destinationGraphNode.getName());

        return true;
    }

    @Override
    public boolean removeLink(GraphLink graphLink) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void queryProperty(GraphLink graphLink) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updatePropiedad(GraphLink graphLink, String id, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reCreatePhosphorousLinks() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canCreateLink(GraphNode wildcardNodeA, GraphNode graphNode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void createOCS() {

        for (LinkCreationAbstractModel linkCreationModel : linkCreationAbstractModels) {

            if (linkCreationModel instanceof OCSCreationModel) {

                GridSimulatorModel simulatorModel = (GridSimulatorModel) SimulationBase.getInstance().getGridSimulatorModel();
                ArrayList<OCSRequest> designedOCSCircuits = simulatorModel.getDesignedOCSCircuits();

                ArrayList<OCSRequest> validOCSs = checkOCSsNodesExist(designedOCSCircuits);

                for (OCSRequest ocsRequest : validOCSs) {

                    Entity sourceNode = ocsRequest.getSource();
                    Entity destinationNode = ocsRequest.getDestination();

                    for (int i = 0; i < 15; i++) {
                        linkCreationModel.createLink(sourceNode, destinationNode);//FIXME: NO VA EL FOR
                    }
                }
            }
        }
    }

    /**
     * Check if the nodes that are part of the OCS circuits actually exist in
     * the gridSimulator entities.Example: We can play re-run the simulation
     * after deleted a node, the request was made before and the deleted node
     * saved in the ArrayList<OCSRequest> designedOCSCircuits.
     *
     * @param designedOCSCircuits
     */
    private ArrayList<OCSRequest> checkOCSsNodesExist(ArrayList<OCSRequest> designedOCSCircuits) {

        ArrayList<OCSRequest> requetsOCSstoRemove = new ArrayList<OCSRequest>();

        for (OCSRequest ocsRequest : designedOCSCircuits) {

            Entity sourceNode = ocsRequest.getSource();
            Entity destinationNode = ocsRequest.getDestination();

            ArrayList<SimBaseEntity> currentEntities = SimulationBase.getInstance().getGridSimulatorModel().getEntities();
            //Determino q solicitudes de ocs entre nodos NO EXISTENTES deben eliminarse  
            if ((sourceNode == null || destinationNode == null) ||
                    (!currentEntities.contains(sourceNode) || !currentEntities.contains(destinationNode))) {
                requetsOCSstoRemove.add(ocsRequest);
            }
        }
        //Si existen solicitudes q deben eliminarse, las elimino de la lista
        //global de solicitudes 
        for (OCSRequest requestOCStoRemove : requetsOCSstoRemove) {
            designedOCSCircuits.remove(requestOCStoRemove);
        }

        return designedOCSCircuits;
    }
}
