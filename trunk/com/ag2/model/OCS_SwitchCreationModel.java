package com.ag2.model;

import Grid.Entity;

public class OCS_SwitchCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {      
        return Grid.Utilities.Util.createOCSSwitch(id, simulationBase.getGridSimulatorModel(), 10);
    }
}