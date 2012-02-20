package com.ag2.model;

import Grid.Entity;
import com.ag2.presentation.Main;

public class ModeloCrearNodoDeRecurso extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String nombreNodoGrafico) {
        //System.out.println("Creo Nodo de Recurso con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createHyridResourceNode(nombreNodoGrafico,simulationBase.getSimulador());
    }
}