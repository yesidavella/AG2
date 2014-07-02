package edu.ag2.model;

import Grid.Entity;

public class HybridSwitchCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {
        return UtilModel.createHybridSwitch(id, simulationBase.getGridSimulatorModel());
    }
}