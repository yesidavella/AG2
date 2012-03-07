package com.ag2.model;

import Grid.Entity;

public class HybridSwitchCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {
        return Grid.Utilities.Util.createHybridSwitch(id, simulationBase.getGridSimulatorModel());
    }
}