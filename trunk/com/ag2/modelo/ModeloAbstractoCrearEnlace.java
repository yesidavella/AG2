package com.ag2.modelo;

import Grid.Entity;
import com.ag2.controlador.AbsControllerAdminLink;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class ModeloAbstractoCrearEnlace implements Serializable{
    
    private ArrayList<AbsControllerAdminLink> controladoresRegistrados;
    
    public ModeloAbstractoCrearEnlace(){
        controladoresRegistrados = new ArrayList<AbsControllerAdminLink>();
    }
    
    public boolean addControlador(AbsControllerAdminLink controlador){
        return controladoresRegistrados.add(controlador);
    }
    
    public boolean removeControlador(AbsControllerAdminLink controlador){
        return controladoresRegistrados.remove(controlador);
    }
    
    public abstract EnlacePhosphorous crearEnlacePhosphorous(Entity nodoPhosA,Entity nodoPhosB);
}