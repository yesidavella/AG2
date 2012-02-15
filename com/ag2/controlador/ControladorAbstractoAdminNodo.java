package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.ModeloCrearNodo;
import com.ag2.modelo.SimulacionBase;
import com.ag2.presentacion.VistaNodosGraficos;
import com.ag2.presentacion.diseño.GraphNode;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class ControladorAbstractoAdminNodo implements Serializable {

    protected ArrayList<VistaNodosGraficos> listaVistaNodosGraficos = new ArrayList<VistaNodosGraficos>();
    protected ArrayList<ModeloCrearNodo> modelosRegistrados;
    protected Hashtable<GraphNode, Entity> parejasDeNodosExistentes;

    public ControladorAbstractoAdminNodo() {
        modelosRegistrados = new ArrayList<ModeloCrearNodo>();
        parejasDeNodosExistentes = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer();
        SimulacionBase.getInstance().setControladorAbstractoAdminNodo(this);
    }

    public void addVistaGraficaNodoses(VistaNodosGraficos vistaGrDeDiseño) {
        listaVistaNodosGraficos.add(vistaGrDeDiseño);
    }

    public void removeVistaGraficaNodoses(VistaNodosGraficos vistaGrDeDiseño) {
        listaVistaNodosGraficos.remove(vistaGrDeDiseño);
    }

    public boolean addModelo(ModeloCrearNodo modeloCrearNodo) {
        return modelosRegistrados.add(modeloCrearNodo)
                && modeloCrearNodo.addControladorCrearNodo(this);
    }

    public boolean removeModelo(ModeloCrearNodo modeloCrearNodo) {
        return modelosRegistrados.remove(modeloCrearNodo);
    }

    public void addNodoGraficoYNodoPhosphorous(GraphNode nodoGrafico, Entity nodoPhosphorous) {
        parejasDeNodosExistentes.put(nodoGrafico, nodoPhosphorous);
    }

    public void removeNodoGraficoYNodoPhosphorous(GraphNode nodoGrafico) {
        parejasDeNodosExistentes.remove(nodoGrafico);
    }

    public abstract Entity crearNodo(GraphNode nodoGrafico);

    public abstract void consultarPropiedades(GraphNode nodoGrafico);

    public abstract void updatePropiedad(boolean isSubProperty, boolean conusultar, String id, String valor);

    public abstract void removeNodo(GraphNode nodoGrafico);

    public abstract void reCreatePhosphorousNodos();

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
        //    listaVistaNodosGraficos = new ArrayList<VistaNodosGraficos>();
            for (int i = 0; i < listaVistaNodosGraficos.size(); i++) {
                listaVistaNodosGraficos.remove(0);                
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}