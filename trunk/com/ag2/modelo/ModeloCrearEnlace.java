package com.ag2.modelo;

import Grid.Entity;

public class ModeloCrearEnlace extends ModeloAbstractoCrearEnlace {

    @Override
    public EnlacePhosphorous crearEnlacePhosphorous(Entity nodoPhosA, Entity nodoPhosB) {
        return ModeloUtil.crearEnlaceBiDireccional(nodoPhosA,nodoPhosB);
    }
}