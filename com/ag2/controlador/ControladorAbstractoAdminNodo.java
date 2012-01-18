package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.ModeloCrearNodo;
import com.ag2.presentacion.VistaNodosGraficos;
import com.ag2.presentacion.diseño.NodoGrafico;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class ControladorAbstractoAdminNodo {

    protected ArrayList<VistaNodosGraficos> listaVistaNodosGraficos = new ArrayList<VistaNodosGraficos>();
    protected ArrayList<ModeloCrearNodo> modelosRegistrados;
    protected Hashtable<NodoGrafico,Entity> parejasDeNodosExistentes;

    public ControladorAbstractoAdminNodo() {
        modelosRegistrados = new ArrayList<ModeloCrearNodo>();
        parejasDeNodosExistentes = ContenedorParejasObjetosExistentes.getInstanciaParejasDeNodosExistentes();
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

    public abstract void crearNodo(NodoGrafico nodoGrafico);
    public abstract void consultarPropiedades(NodoGrafico nodoGrafico);
    public abstract void updatePropiedad(String id, String valor);
    public abstract void removeNodo(NodoGrafico nodoGrafico); 
}