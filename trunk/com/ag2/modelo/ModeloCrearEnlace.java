package com.ag2.modelo;

import Grid.Entity;

public class ModeloCrearEnlace extends ModeloAbstractoCrearEnlace {

    @Override
    public void crearEnlacePhosphorous(Entity nodoPhosA, Entity nodoPhosB) {
        ModeloUtil.createBiDirectionalLink(nodoPhosA,nodoPhosB);
    }
}