package com.ag2.model;

import Grid.Entity;

public class FiberCreationModel extends LinkCreationAbstractModel {

    @Override
    public PhosphorusLinkModel createLink(Entity entityA, Entity entityB) {
        return UtilModel.createBiDireccionalFiberLink(entityA,entityB);
    }
}