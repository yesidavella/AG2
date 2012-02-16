package com.ag2.modelo;

import Grid.Entity;
import com.ag2.presentacion.Main;

public class ModeloCrearEnrutadorHibrido extends NodeCreationModel{

    @Override
    public Entity crearNodoPhophorous(String nombreNodoGrafico) {
        //System.out.println("Creo Enrutador Hibrido con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createHybridSwitch(nombreNodoGrafico, simulacionBase.getSimulador());
    }
}