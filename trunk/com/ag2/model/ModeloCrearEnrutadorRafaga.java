package com.ag2.model;

import Grid.Entity;
import com.ag2.presentation.Main;

public class ModeloCrearEnrutadorRafaga extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String nombreNodoGrafico) {
        //System.out.println("Creo Enrutador Rafaga con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createOBSSwitch(nombreNodoGrafico,simulationBase.getSimulador(), true);
    }
}