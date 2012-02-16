package com.ag2.modelo;

import Grid.Entity;
import com.ag2.presentacion.Main;

public class ModeloCrearEnrutadorRafaga extends NodeCreationModel{

    @Override
    public Entity crearNodoPhophorous(String nombreNodoGrafico) {
        //System.out.println("Creo Enrutador Rafaga con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createOBSSwitch(nombreNodoGrafico,simulacionBase.getSimulador(), true);
    }
}
