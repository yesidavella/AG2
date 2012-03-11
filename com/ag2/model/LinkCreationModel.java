package com.ag2.model;

import Grid.Entity;

public class LinkCreationModel extends LinkCreationAbstractModel {

    @Override
    public PhosphorusLinkModel createPhosphorusLink(Entity entityA, Entity entityB) {
        return UtilModel.crearEnlaceBiDireccional(entityA,entityB);
    }
}