package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.ModeloCrearNodo;
import com.ag2.presentacion.InterfaceGraficaNodos;
import com.ag2.presentacion.diseño.NodoGrafico;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class ControladorAbstractoAdminNodo {
    
    private InterfaceGraficaNodos interfaceGraficaNodos;
    protected  ArrayList<ModeloCrearNodo> modelosRegistrados;
    protected Hashtable<NodoGrafico,Entity> parejasDeNodosExistentes;
    
    public ControladorAbstractoAdminNodo(){
        modelosRegistrados = new ArrayList<ModeloCrearNodo>();
        parejasDeNodosExistentes = new Hashtable<NodoGrafico, Entity>();
    }
    
    public void addVistaGrDeDiseño(InterfaceGraficaNodos vistaGrDeDiseño){
        
        this.interfaceGraficaNodos = vistaGrDeDiseño;
    }
    
    public boolean addModelo(ModeloCrearNodo modeloCrearNodo){
        return modelosRegistrados.add(modeloCrearNodo) && 
                modeloCrearNodo.addControladorCrearNodo(this);
    }
    
    public boolean removeModelo(ModeloCrearNodo modeloCrearNodo){
        return modelosRegistrados.remove(modeloCrearNodo);
    }
    
    protected void addNodoGraficoYNodoPhosphorous(NodoGrafico nodoGrafico,Entity nodoReal){
        parejasDeNodosExistentes.put(nodoGrafico, nodoReal);
    }
    
    protected void removeNodoGraficoYNodoPhosphorous(NodoGrafico nodoGrafico){
        parejasDeNodosExistentes.remove(nodoGrafico);
    }
    
    
    public abstract void crearNodo(NodoGrafico nodoGrafico);
    
}