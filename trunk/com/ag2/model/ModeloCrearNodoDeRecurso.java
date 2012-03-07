package com.ag2.model;

import Grid.Entity;

public class ModeloCrearNodoDeRecurso extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String nombreNodoGrafico) {
        return Grid.Utilities.Util.createHyridResourceNode(nombreNodoGrafico,simulationBase.getGridSimulatorModel());
    }
}