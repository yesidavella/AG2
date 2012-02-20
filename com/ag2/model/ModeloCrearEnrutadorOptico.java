package com.ag2.model;

import Grid.Entity;
import com.ag2.presentation.Main;

public class ModeloCrearEnrutadorOptico extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String nombreNodoGrafico) {
        //System.out.println("Creo Enrutador Optico con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createOCSSwitch(nombreNodoGrafico, simulationBase.getSimulador(), 10);
    }
}