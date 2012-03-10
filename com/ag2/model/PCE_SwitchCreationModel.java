package com.ag2.model;

import Grid.Entity;

public class PCE_SwitchCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {      
        return Grid.Utilities.Util.createHybridSwitch(id, simulationBase.getGridSimulatorModel());
    }
}