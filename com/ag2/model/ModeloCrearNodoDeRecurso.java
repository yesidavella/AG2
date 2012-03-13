package com.ag2.model;

import Grid.Entity;

public class ModeloCrearNodoDeRecurso extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String nombreNodoGrafico) {
        return UtilModel.createHyridResourceNode(nombreNodoGrafico,simulationBase.getGridSimulatorModel());
    }
}