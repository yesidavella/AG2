package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.ModeloCrearCliente;
import com.ag2.modelo.ModeloCrearNodo;
import com.ag2.modelo.ModeloCrearNodoDeServicio;
import com.ag2.presentacion.diseño.NodoClienteGrafico;
import com.ag2.presentacion.diseño.NodoDeServicioGrafico;
import com.ag2.presentacion.diseño.NodoGrafico;

public class ControladorCreacionNodo extends ControladorCreacionYAdminDeNodo {

    @Override
    public void crearNodo(NodoGrafico nodoGrafico) {

        for (ModeloCrearNodo modeloRegistrado : modelosRegistrados) {

            Entity nuevoNodoPhophorous = null;
            
            if (nodoGrafico instanceof NodoClienteGrafico && modeloRegistrado instanceof ModeloCrearCliente) {
                nuevoNodoPhophorous = ((ModeloCrearCliente)modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (nodoGrafico instanceof NodoDeServicioGrafico && modeloRegistrado instanceof ModeloCrearNodoDeServicio) {
                nuevoNodoPhophorous = ((ModeloCrearNodoDeServicio)modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            }
            
            if(nuevoNodoPhophorous != null){
                addNodoGraficoYNodoPhosphorous(nodoGrafico, nuevoNodoPhophorous);
            }
        }
    }

}
