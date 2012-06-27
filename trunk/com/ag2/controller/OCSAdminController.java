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

//        System.out.println("Solicito OCS circuito entre:" + sourceGraphNode.getName() + " -y- " + destinationGraphNode.getName());

        return simulatorModel.getDesignedOCSCircuits().add(new OCSRequest(phosphorusNodeA, phosphorusNodeB));
    }

    @Override
    public boolean removeLink(GraphLink graphLink) {

        GraphNode sourceGraphNode = graphLink.getGraphNodeA();
        GraphNode destinationGraphNode = graphLink.getGraphNodeB();

        Entity phosphorusNodeA = (Entity) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(sourceGraphNode);
        Entity phosphorusNodeB = (Entity) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(destinationGraphNode);

        GridSimulatorModel simulatorModel = (GridSimulatorModel) SimulationBase.getInstance().getGridSimulatorModel();
        ArrayList<OCSRequest> designedOCSCircuits = simulatorModel.getDesignedOCSCircuits();

        ArrayList<SimBaseEntity> currentEntities = SimulationBase.getInstance().getGridSimulatorModel().getEntities();
        ArrayList<OCSRequest> requetsOCSstoRemove = new ArrayList<OCSRequest>();

        for (OCSRequest ocsRequest : designedOCSCircuits) {

            Entity ocsSource = ocsRequest.getSource();
            Entity ocsDestination = ocsRequest.getDestination();

            if ((phosphorusNodeA == ocsSource && phosphorusNodeB == ocsDestination) ||
                    (phosphorusNodeB == ocsSource && phosphorusNodeA == ocsDestination) ||
                    (!currentEntities.contains(ocsSource)) || (!currentEntities.contains(ocsDestination))) {
                requetsOCSstoRemove.add(ocsRequest);
            }
        }

        //Si existen solicitudes q deben eliminarse, las elimino de la lista
        //global de solicitudes 
        for (OCSRequest requestOCStoRemove : requetsOCSstoRemove) {
            designedOCSCircuits.remove(requestOCStoRemove);
        }

        return true;
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

                for (OCSRequest ocsRequest : designedOCSCircuits) {

                    Entity sourceNode = ocsRequest.getSource();
                    Entity destinationNode = ocsRequest.getDestination();

//                    for (int i = 0; i < 15; i++) {
                    linkCreationModel.createLink(sourceNode, destinationNode);//FIXME: NO VA EL FOR
//                    }
                }
            }
        }
    }
}
