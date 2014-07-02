package edu.ag2.model;

import Grid.Entity;

public class ClientCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {
        
        return UtilModel.createHybridClient(id,simulationBase.getGridSimulatorModel());
    }
}