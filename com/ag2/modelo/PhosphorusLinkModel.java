package com.ag2.modelo;

import Grid.Entity;
import Grid.Port.GridOutPort;
import java.io.Serializable;

public class PhosphorusLinkModel implements Serializable {

    private Entity phosphorusNodeA ;
    private GridOutPort gridOutPortA;
    private Entity phosphorusNodeB;
    private GridOutPort gridOutPortB;

    public PhosphorusLinkModel(Entity phosphorusNodeA, GridOutPort gridOutPortA,
            Entity phosphorusNodeB, GridOutPort gridOutPortB) {

        this.phosphorusNodeA  = phosphorusNodeA;
        this.gridOutPortA = gridOutPortA;
        this.phosphorusNodeB = phosphorusNodeB;
        this.gridOutPortB = gridOutPortB;
    }

    public GridOutPort getGridOutPortA() {
        return gridOutPortA;
    }

    public GridOutPort getGridOutPortB() {
        return gridOutPortB;
    }

    public Entity getPhosphorusNodeA() {
        return phosphorusNodeA ;
    }

    public void setPhosphorusNodeA(Entity phosphorusNodeA) {
        this.phosphorusNodeA  = phosphorusNodeA;
    }

    public Entity getPhosphorusNodeB() {
        return phosphorusNodeB;
    }

    public void setPhosphorusNodeB(Entity phosphorusNodeB) {
        this.phosphorusNodeB = phosphorusNodeB;
    }

    public void setGridOutPortA(GridOutPort gridOutPortA) {
        this.gridOutPortA = gridOutPortA;
    }

    public void setGridOutPortB(GridOutPort gridOutPortB) {
        this.gridOutPortB = gridOutPortB;
    }
}
