package com.ag2.model;

import Grid.Entity;

public class HybridSwitchCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {
        //System.out.println("Creo Enrutador Hibrido con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createHybridSwitch(id, simulationBase.getGridSimulatorModel());
    }
}