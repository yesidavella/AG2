package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.ModeloCrearNodo;
import com.ag2.modelo.SimulacionBase;
import com.ag2.presentacion.VistaNodosGraficos;
import com.ag2.presentacion.diseño.NodoGrafico;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class ControladorAbstractoAdminNodo implements Serializable {

    protected ArrayList<VistaNodosGraficos> listaVistaNodosGraficos = new ArrayList<VistaNodosGraficos>();
    protected ArrayList<ModeloCrearNodo> modelosRegistrados;
    protected Hashtable<NodoGrafico,Entity> parejasDeNodosExistentes;

    public ControladorAbstractoAdminNodo() 
    {
        modelosRegistrados = new ArrayList<ModeloCrearNodo>();
        parejasDeNodosExistentes = ContenedorParejasObjetosExistentes.getInstanciaParejasDeNodosExistentes();
        SimulacionBase.getInstance().setControladorAbstractoAdminNodo(this);
    }

    public void addVistaGraficaNodoses(VistaNodosGraficos vistaGrDeDiseño) {
        listaVistaNodosGraficos.add(vistaGrDeDiseño);
    }

    public boolean addModelo(ModeloCrearNodo modeloCrearNodo) {
        return modelosRegistrados.add(modeloCrearNodo)
                && modeloCrearNodo.addControladorCrearNodo(this);
    }

    public boolean removeModelo(ModeloCrearNodo modeloCrearNodo) {
        return modelosRegistrados.remove(modeloCrearNodo);
    }

    public void addNodoGraficoYNodoPhosphorous(NodoGrafico nodoGrafico, Entity nodoPhosphorous) {
        parejasDeNodosExistentes.put(nodoGrafico, nodoPhosphorous);
    }

    public void removeNodoGraficoYNodoPhosphorous(NodoGrafico nodoGrafico) {
        parejasDeNodosExistentes.remove(nodoGrafico);
    }

    public abstract Entity crearNodo(NodoGrafico nodoGrafico);
    public abstract void consultarPropiedades(NodoGrafico nodoGrafico);
    public abstract void updatePropiedad(boolean isSubProperty,boolean conusultar, String id, String valor);
    public abstract void removeNodo(NodoGrafico nodoGrafico); 
     public abstract void reCreatePhosphorousNodos(); 
}