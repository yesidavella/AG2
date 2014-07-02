package edu.ag2.model;

import Grid.Entity;

public class BrokerCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {       
        return UtilModel.createHybridServiceNode(id,simulationBase.getGridSimulatorModel());
    }
}
