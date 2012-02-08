package com.ag2.modelo;

import Grid.Entity;
import Grid.Port.GridOutPort;
import java.io.Serializable;

public class EnlacePhosphorous implements Serializable {

    private Entity nodoPhosphorousA;
    private GridOutPort puertoSalidaDeNodoPhosA;
    private Entity nodoPhosphorousB;
    private GridOutPort puertoSalidaDeNoodoPhosB;

    public EnlacePhosphorous(Entity nodoPhosphorousA, GridOutPort puertoSalidaNodoPhosA,
            Entity nodoPhosphorousB, GridOutPort puertoSalidaNodoPhosB) {

        this.nodoPhosphorousA = nodoPhosphorousA;
        this.puertoSalidaDeNodoPhosA = puertoSalidaNodoPhosA;
        this.nodoPhosphorousB = nodoPhosphorousB;
        this.puertoSalidaDeNoodoPhosB = puertoSalidaNodoPhosB;
    }

    public GridOutPort getPuertoSalidaNodoPhosA() {
        return puertoSalidaDeNodoPhosA;
    }

    public GridOutPort getPuertoSalidaNodoPhosB() {
        return puertoSalidaDeNoodoPhosB;
    }

    public Entity getNodoPhosphorousA() {
        return nodoPhosphorousA;
    }

    public void setNodoPhosphorousA(Entity nodoPhosphorousA) {
        this.nodoPhosphorousA = nodoPhosphorousA;
    }

    public Entity getNodoPhosphorousB() {
        return nodoPhosphorousB;
    }

    public void setNodoPhosphorousB(Entity nodoPhosphorousB) {
        this.nodoPhosphorousB = nodoPhosphorousB;
    }

    public void setPuertoSalidaDeNodoPhosA(GridOutPort puertoSalidaDeNodoPhosA) {
        this.puertoSalidaDeNodoPhosA = puertoSalidaDeNodoPhosA;
    }

    public void setPuertoSalidaDeNoodoPhosB(GridOutPort puertoSalidaDeNoodoPhosB) {
        this.puertoSalidaDeNoodoPhosB = puertoSalidaDeNoodoPhosB;
    }
}
