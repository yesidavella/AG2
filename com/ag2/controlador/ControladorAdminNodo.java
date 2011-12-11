package com.ag2.controlador;

import Distributions.DDNegExp;
import Grid.Entity;
import Grid.Interfaces.ClientNode;
import com.ag2.modelo.*;
import com.ag2.presentacion.VistaNodosGraficos;
import com.ag2.presentacion.diseño.*;
import java.util.ArrayList;

public class ControladorAdminNodo extends ControladorAbstractoAdminNodo {

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


    public void consultarPropiedades(NodoGrafico nodoGrafico)
    {
         
        ArrayList<PropiedadeNodo> propiedadeNodos = new ArrayList<PropiedadeNodo>(); 
        

        if(nodoGrafico instanceof NodoClienteGrafico)   
        {
            ClientNode clientNode =   (ClientNode) parejasDeNodosExistentes.get(nodoGrafico); 
            DDNegExp dDNegExp = (DDNegExp)clientNode.getState().getJobInterArrival(); 
      
            PropiedadeNodo propiedadedPromedioTrabajos = new PropiedadeNodo("Promedio Salida de trabajos", PropiedadeNodo.TipoDePropiedadNodo.NUMERO);             
            propiedadedPromedioTrabajos.setPrimerValor(String.valueOf(dDNegExp.getAvg()));
            propiedadeNodos.add(propiedadedPromedioTrabajos);
        }
       
        for(VistaNodosGraficos  vistaNodosGraficos: listaVistaNodosGraficos)
        {
            vistaNodosGraficos.cargarPropiedades(propiedadeNodos);
            
        }

    }

}