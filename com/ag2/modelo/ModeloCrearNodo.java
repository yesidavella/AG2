package com.ag2.modelo;

import Grid.Entity;
import com.ag2.controlador.ControladorCreacionYAdminDeNodo;

public abstract class ModeloCrearNodo {
    
    private ControladorCreacionYAdminDeNodo ctrlCrearNodo;
    
    public boolean addControladorCrearNodo(ControladorCreacionYAdminDeNodo ctrlCrearNodo){
        this.ctrlCrearNodo = ctrlCrearNodo;
        
        if(this.ctrlCrearNodo!=null){
            return true;
        }else{
            return false;
        }
    }
    
    public abstract Entity crearNodoPhophorous(String nombreNodoGrafico);

}
