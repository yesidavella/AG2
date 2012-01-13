package com.ag2.modelo;

import Grid.Entity;
import Grid.Port.GridOutPort;

public class EnlacePhosphorous {
    
    private Entity nodoPhosphorousA;
    private GridOutPort puertoSalidaDeNodoPhosA;
    private Entity nodoPhosphorousB;
    private GridOutPort puertoSalidaDeNoodoPhosB;
    
    public EnlacePhosphorous (Entity nodoPhosphorousA, GridOutPort puertoSalidaNodoPhosA,
            Entity nodoPhosphorousB, GridOutPort puertoSalidaNodoPhosB){
        
        this.nodoPhosphorousA = nodoPhosphorousA;
        this.puertoSalidaDeNodoPhosA = puertoSalidaNodoPhosA;
        this.nodoPhosphorousB = nodoPhosphorousB;
        this.puertoSalidaDeNoodoPhosB = puertoSalidaNodoPhosB;
    }
    
    public GridOutPort getPuertoSalidaNodoPhosA(){
        return puertoSalidaDeNodoPhosA;
    }
    
    public GridOutPort getPuertoSalidaNodoPhosB(){
        return puertoSalidaDeNoodoPhosB;
    }
}
