package com.ag2.model;

import Grid.Entity;
import Grid.Port.GridOutPort;
import com.ag2.presentation.Main;
import com.ag2.presentation.design.GraphArc;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
     private void writeObject(ObjectOutputStream stream) {
        try {
            stream.defaultWriteObject();
            Main.countObject++;
            //System.out.println("Writing: " + Main.countObject + "  " + this.getClass().getCanonicalName());
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}