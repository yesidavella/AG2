package com.ag2.model;

import Grid.Entity;

public class ModeloCrearCliente extends NodeCreationModel{

    @Override
    public Entity crearNodoPhophorous(String nombreNodoGrafico) {
        //System.out.println("Creo Cliente con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createHybridClient(nombreNodoGrafico,simulacionBase.getSimulador());
    }
    
}
