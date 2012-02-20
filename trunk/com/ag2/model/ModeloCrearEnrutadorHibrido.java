package com.ag2.model;

import Grid.Entity;
import com.ag2.presentation.Main;

public class ModeloCrearEnrutadorHibrido extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String nombreNodoGrafico) {
        //System.out.println("Creo Enrutador Hibrido con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createHybridSwitch(nombreNodoGrafico, simulationBase.getSimulador());
    }
}