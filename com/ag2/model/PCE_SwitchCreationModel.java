package com.ag2.model;

import Grid.Entity;

public class PCE_SwitchCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {      
        return UtilModel.createPCE(id, simulationBase.getGridSimulatorModel());
    }
}