package com.ag2.model;

import Grid.Entity;

public class ResourceCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String nombreNodoGrafico) {
        return UtilModel.createHyridResourceNode(nombreNodoGrafico,simulationBase.getGridSimulatorModel());
    }
}