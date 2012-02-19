package com.ag2.model;

import Grid.Entity;

public class ModeloCrearEnlace extends LinkCreationAbstractModel {

    @Override
    public PhosphorusLinkModel createPhosphorusLink(Entity nodoPhosA, Entity nodoPhosB) {
        return ModeloUtil.crearEnlaceBiDireccional(nodoPhosA,nodoPhosB);
    }
}