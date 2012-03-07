package com.ag2.model;

import Grid.Entity;

public class OBS_SwitchCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {
        return Grid.Utilities.Util.createOBSSwitch(id,simulationBase.getGridSimulatorModel(), true);
    }
}
