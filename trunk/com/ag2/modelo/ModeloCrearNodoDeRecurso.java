package com.ag2.modelo;

import Grid.Entity;
import com.ag2.presentacion.Main;

public class ModeloCrearNodoDeRecurso extends ModeloCrearNodo{

    @Override
    public Entity crearNodoPhophorous(String nombreNodoGrafico) {
        System.out.println("Creo Nodo de Recurso con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createHyridResourceNode(nombreNodoGrafico,Main.simulador);
    }   
}