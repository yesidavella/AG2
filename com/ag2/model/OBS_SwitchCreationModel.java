package com.ag2.model;

import Grid.Entity;

public class OBS_SwitchCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {
        //System.out.println("Creo Enrutador Rafaga con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createOBSSwitch(id,simulationBase.getGridSimulatorModel(), true);
    }
}
