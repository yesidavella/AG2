package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.*;
import com.ag2.presentacion.diseño.*;

public class ControladorCreacionNodo extends ControladorCreacionYAdminDeNodo {

    @Override
    public void crearNodo(NodoGrafico nodoGrafico) {

        for (ModeloCrearNodo modeloRegistrado : modelosRegistrados) {

            Entity nuevoNodoPhophorous = null;
            
            if(modeloRegistrado instanceof ModeloCrearCliente && nodoGrafico instanceof NodoClienteGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearCliente)modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            }else if(modeloRegistrado instanceof ModeloCrearNodoDeServicio && nodoGrafico instanceof NodoDeServicioGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearNodoDeServicio)modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            }else if(modeloRegistrado instanceof ModeloCrearNodoDeRecurso && nodoGrafico instanceof NodoDeRecursoGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearNodoDeRecurso)modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            }else if(modeloRegistrado instanceof ModeloCrearEnrutadorRafaga && nodoGrafico instanceof EnrutadorRafagaGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorRafaga)modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            }else if(modeloRegistrado instanceof ModeloCrearEnrutadorOptico && nodoGrafico instanceof EnrutadorOpticoGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorOptico)modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            }else if(modeloRegistrado instanceof ModeloCrearEnrutadorHibrido && nodoGrafico instanceof EnrutadorHibridoGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorHibrido)modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            }
            
            if(nuevoNodoPhophorous != null){
                addNodoGraficoYNodoPhosphorous(nodoGrafico, nuevoNodoPhophorous);
            }
        }
    }

}