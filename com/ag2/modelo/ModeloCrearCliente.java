package com.ag2.modelo;

import Grid.Entity;
import com.ag2.presentacion.Main;

public class ModeloCrearCliente extends ModeloCrearNodo{

    @Override
    public Entity crearNodoPhophorous(String nombreNodoGrafico) {
        //System.out.println("Creo Cliente con monbre:"+nombreNodoGrafico);
        Grid.Utilities.Util.createBiDirectionalLink(null, null);
        return Grid.Utilities.Util.createHybridClient(nombreNodoGrafico,simulacionBase.getSimulador());
    }
    
}
