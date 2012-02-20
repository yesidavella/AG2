package com.ag2.model;

import Grid.Entity;
import com.ag2.presentation.Main;

public class BrokerCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String nombreNodoGrafico) {
        //System.out.println("Creo Nodo se Servicio con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createHybridServiceNode(nombreNodoGrafico,simulationBase.getSimulador());
    }
}
