package com.ag2.modelo;

import Grid.Entity;
import com.ag2.controlador.ControladorAbstractoAdminNodo;

public abstract class ModeloCrearNodo 
{
    
    private ControladorAbstractoAdminNodo ctrlCrearNodo;
    protected SimulacionBase simulacionBase; 
    
    public ModeloCrearNodo()
    {
       loadSimulacionBase();
    }
    public  void loadSimulacionBase(){
         simulacionBase=  SimulacionBase.getInstance();
    }
            
           
            
    
    public boolean addControladorCrearNodo(ControladorAbstractoAdminNodo ctrlCrearNodo){
        this.ctrlCrearNodo = ctrlCrearNodo;
        
        if(this.ctrlCrearNodo!=null){
            return true;
        }else{
            return false;
        }
    }
    
    public abstract Entity crearNodoPhophorous(String nombreNodoGrafico);

}
