package com.ag2.modelo;

import Grid.Entity;
import com.ag2.controlador.NodeAdminAbstractController;
import java.io.Serializable;

public abstract class NodeCreationModel implements Serializable 
{
    
    private NodeAdminAbstractController nodeAdminAbstractController;
    protected SimulationBase simulacionBase; 
    
    public NodeCreationModel()
    {
       loadSimulacionBase();
    }
    public  void loadSimulacionBase(){
         simulacionBase=  SimulationBase.getInstance();
    }
            
    
    public boolean addControladorCrearNodo(NodeAdminAbstractController ctrlCrearNodo){
        this.nodeAdminAbstractController = ctrlCrearNodo;
        
        if(this.nodeAdminAbstractController!=null){
            return true;
        }else{
            return false;
        }
    }
    
    public abstract Entity crearNodoPhophorous(String nombreNodoGrafico);

}
