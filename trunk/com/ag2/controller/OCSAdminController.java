package com.ag2.controller;

import Grid.Entity;
import com.ag2.model.*;
import com.ag2.presentation.design.GraphLink;
import com.ag2.presentation.design.GraphNode;
import java.util.HashMap;

public class OCSAdminController extends LinkAdminAbstractController {

    @Override
    public boolean createLink(GraphNode sourceGraphNode, GraphNode destinationGraphNode) {

        GridSimulatorModel simulatorModel = (GridSimulatorModel) SimulationBase.getInstance().getGridSimulatorModel();

        Entity phosphorusNodeA = (Entity) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(sourceGraphNode);
        Entity phosphorusNodeB = (Entity) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(destinationGraphNode);

        simulatorModel.getDesignedOCSCircuits().put(phosphorusNodeA, phosphorusNodeB);

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
                
                System.out.println("Creando OCSs");
                GridSimulatorModel simulatorModel = (GridSimulatorModel) SimulationBase.getInstance().getGridSimulatorModel();
                HashMap<Entity, Entity> designedOCSCircuits = simulatorModel.getDesignedOCSCircuits();

                for (Entity sourceNode : designedOCSCircuits.keySet()) {

                    Entity destinationNode = designedOCSCircuits.get(sourceNode);
                    linkCreationModel.createLink(sourceNode, destinationNode);
                }
            }
        }
    }
}
