package com.ag2.modelo;

import Grid.Entity;
import com.ag2.presentacion.Main;

public class ModeloCrearNodoDeServicio extends ModeloCrearNodo{

    @Override
    public Entity crearNodoPhophorous(String nombreNodoGrafico) {
        System.out.println("Creo Nodo se Servicio con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createHybridServiceNode(nombreNodoGrafico,Main.simulador);
    }
    
}
