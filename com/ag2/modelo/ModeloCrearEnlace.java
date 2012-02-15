package com.ag2.modelo;

import Grid.Entity;

public class ModeloCrearEnlace extends LinkCreationAbstractModel {

    @Override
    public PhosphorusLinkModel createPhosphorusLink(Entity nodoPhosA, Entity nodoPhosB) {
        return ModeloUtil.crearEnlaceBiDireccional(nodoPhosA,nodoPhosB);
    }
}