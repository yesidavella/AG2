package com.ag2.modelo;

import Grid.Entity;
import com.ag2.presentacion.IGU;

public class ModeloCrearCliente extends ModeloCrearNodo{

    @Override
    public Entity crearNodoPhophorous(String nombreNodoGrafico) {
        System.out.println("Creo Cliente con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createHybridClient(nombreNodoGrafico,IGU.simulador);
    }
    
}
