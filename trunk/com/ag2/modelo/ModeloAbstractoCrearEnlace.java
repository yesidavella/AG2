package com.ag2.modelo;

import Grid.Entity;
import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class ModeloAbstractoCrearEnlace implements Serializable{
    
    private ArrayList<ControladorAbstractoAdminEnlace> controladoresRegistrados;
    
    public ModeloAbstractoCrearEnlace(){
        controladoresRegistrados = new ArrayList<ControladorAbstractoAdminEnlace>();
    }
    
    public boolean addControlador(ControladorAbstractoAdminEnlace controlador){
        return controladoresRegistrados.add(controlador);
    }
    
    public boolean removeControlador(ControladorAbstractoAdminEnlace controlador){
        return controladoresRegistrados.remove(controlador);
    }
    
    public abstract EnlacePhosphorous crearEnlacePhosphorous(Entity nodoPhosA,Entity nodoPhosB);
}