package com.ag2.controlador;

import Distributions.*;
import Grid.Entity;
import Grid.Interfaces.ClientNode;
import com.ag2.modelo.*;
import com.ag2.presentacion.VistaNodosGraficos;
import com.ag2.presentacion.diseño.*;
import com.ag2.presentacion.diseño.propiedades.PropiedadNodoDistribuciones;
import com.ag2.presentacion.diseño.propiedades.PropiedadeNodo;
import com.ag2.presentacion.diseño.propiedades.TablaPropiedadesDispositivo;
import java.util.ArrayList;

public class ControladorAdminNodo extends ControladorAbstractoAdminNodo {

    private NodoGrafico nodoGraficoSeleccionado;

    @Override
    public void crearNodo(NodoGrafico nodoGrafico) {

        for (ModeloCrearNodo modeloRegistrado : modelosRegistrados) {

            Entity nuevoNodoPhophorous = null;

            if (modeloRegistrado instanceof ModeloCrearCliente && nodoGrafico instanceof NodoClienteGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearCliente) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (modeloRegistrado instanceof ModeloCrearNodoDeServicio && nodoGrafico instanceof NodoDeServicioGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearNodoDeServicio) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (modeloRegistrado instanceof ModeloCrearNodoDeRecurso && nodoGrafico instanceof NodoDeRecursoGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearNodoDeRecurso) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (modeloRegistrado instanceof ModeloCrearEnrutadorRafaga && nodoGrafico instanceof EnrutadorRafagaGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorRafaga) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (modeloRegistrado instanceof ModeloCrearEnrutadorOptico && nodoGrafico instanceof EnrutadorOpticoGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorOptico) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (modeloRegistrado instanceof ModeloCrearEnrutadorHibrido && nodoGrafico instanceof EnrutadorHibridoGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorHibrido) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            }
            if (nuevoNodoPhophorous != null) {
                addNodoGraficoYNodoPhosphorous(nodoGrafico, nuevoNodoPhophorous);
            }
        }
    }

    public void consultarPropiedades(NodoGrafico nodoGrafico) {
        nodoGraficoSeleccionado = nodoGrafico;

        ArrayList<PropiedadeNodo> propiedadeNodos = new ArrayList<PropiedadeNodo>();



        if (nodoGrafico instanceof NodoClienteGrafico) {

            ClientNode clientNode = (ClientNode) parejasDeNodosExistentes.get(nodoGrafico);

            //===========================================================================================================
            PropiedadeNodo propiedadNodoNombre = new PropiedadeNodo("nombre", "Nombre", PropiedadeNodo.TipoDePropiedadNodo.TEXTO);
            //propiedadNodoNombre.setPrimerValor(clientNode.getId());
            propiedadNodoNombre.setPrimerValor(nodoGraficoSeleccionado.getNombre());
            propiedadeNodos.add(propiedadNodoNombre);
            //===========================================================================================================

            PropiedadNodoDistribuciones distribucionesTrabajos = new PropiedadNodoDistribuciones("generacionTrabajos", "Generación de trabajos");
            crearPropiedadDistriducion(clientNode.getState().getJobInterArrival(), propiedadeNodos, distribucionesTrabajos, "generacionTrabajos");
            //===========================================================================================================
            PropiedadNodoDistribuciones distribucionesFlops = new PropiedadNodoDistribuciones("generacionFlops", "Generación de flops por trabajo");
            crearPropiedadDistriducion(clientNode.getState().getFlops(), propiedadeNodos, distribucionesFlops, "generacionFlops");

            //===========================================================================================================
            PropiedadNodoDistribuciones distribucionesMaximoRetraso = new PropiedadNodoDistribuciones("generacionMaximoRetraso", "Generación de intervalo maximo de retraso");
            crearPropiedadDistriducion(clientNode.getState().getMaxDelayInterval(), propiedadeNodos, distribucionesMaximoRetraso, "generacionMaximoRetraso");

            //===========================================================================================================
            PropiedadNodoDistribuciones distribucionesTamanoTrabajo = new PropiedadNodoDistribuciones("generacionTamañoTrabajo", "Generación del tamaño del trabajo");
            crearPropiedadDistriducion(clientNode.getState().getMaxDelayInterval(), propiedadeNodos, distribucionesTamanoTrabajo, "generacionTamañoTrabajo");

            //===========================================================================================================
            PropiedadNodoDistribuciones distribucionesTamanoRespuesta = new PropiedadNodoDistribuciones("generacionTamañoRespuesta", "Generación del tamaño de la respuesta");
            crearPropiedadDistriducion(clientNode.getState().getMaxDelayInterval(), propiedadeNodos, distribucionesTamanoRespuesta, "generacionTamañoRespuesta");


        }

        for (VistaNodosGraficos vistaNodosGraficos : listaVistaNodosGraficos) {
            vistaNodosGraficos.cargarPropiedades(propiedadeNodos);

        }

    }

    private void crearPropiedadDistriducion(DiscreteDistribution discreteDistribution, ArrayList<PropiedadeNodo> propiedadeNodos, PropiedadNodoDistribuciones propiedadNodoDistribuciones, String id) {

        propiedadeNodos.add(propiedadNodoDistribuciones);

        if (discreteDistribution instanceof DDErlang) {

            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.ER_LANG);

            DDErlang dDErlang = (DDErlang) discreteDistribution;

            PropiedadeNodo propiedaA = new PropiedadeNodo(id + "_Orden", "Orden", PropiedadeNodo.TipoDePropiedadNodo.NUMERO);
            propiedaA.setPrimerValor(String.valueOf(dDErlang.getN()));
            propiedadeNodos.add(propiedaA);

            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_Promedio", "Promedio", PropiedadeNodo.TipoDePropiedadNodo.NUMERO);
            propiedaB.setPrimerValor(String.valueOf(dDErlang.getAvg()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDHyperExp) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.HYPER_EXPONENTIAL);
            DDHyperExp dDHyperExp = (DDHyperExp) discreteDistribution;

            PropiedadeNodo propiedaA = new PropiedadeNodo(id + "_Lamdas", "Lamdas", PropiedadeNodo.TipoDePropiedadNodo.TEXTO);
            propiedaA.setPrimerValor(getStringArrayDoubles(dDHyperExp.getLambdas()));
            propiedadeNodos.add(propiedaA);

            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_Oportunidades", "Oportunidades", PropiedadeNodo.TipoDePropiedadNodo.TEXTO);
            propiedaB.setPrimerValor(getStringArrayDoubles(dDHyperExp.getChances()));
            propiedadeNodos.add(propiedaB);

        } else if (discreteDistribution instanceof DDNegExp) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.NEGATIVE_EXPONENTIAL);
            DDNegExp dDNegExp = (DDNegExp) discreteDistribution;
            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_Promedio", "Promedio", PropiedadeNodo.TipoDePropiedadNodo.NUMERO);
            propiedaB.setPrimerValor(String.valueOf(dDNegExp.getAvg()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDNormal) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.NORMAL);
            DDNormal dDNormal = (DDNormal) discreteDistribution;

            PropiedadeNodo propiedaA = new PropiedadeNodo(id + "_DesviacionEstandar", "Desviación estandar", PropiedadeNodo.TipoDePropiedadNodo.NUMERO);
            propiedaA.setPrimerValor(String.valueOf(dDNormal.getDev()));
            propiedadeNodos.add(propiedaA);

            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_Promedio", "Promedio", PropiedadeNodo.TipoDePropiedadNodo.NUMERO);
            propiedaB.setPrimerValor(String.valueOf(dDNormal.getAvg()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDPoissonProcess) {

            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.POISSON_PROCESS);
            DDPoissonProcess dDPoissonProcess = (DDPoissonProcess) discreteDistribution;
            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_Promedio", "Promedio", PropiedadeNodo.TipoDePropiedadNodo.NUMERO);
            propiedaB.setPrimerValor(String.valueOf(dDPoissonProcess.getAverage()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDUniform) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.UNMIFORM);
            DDUniform dDUniform = (DDUniform) discreteDistribution;

            PropiedadeNodo propiedaA = new PropiedadeNodo(id + "_Minimo", "Minimo", PropiedadeNodo.TipoDePropiedadNodo.NUMERO);
            propiedaA.setPrimerValor(String.valueOf(dDUniform.getMin()));
            propiedadeNodos.add(propiedaA);

            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_Maximo", "Maximo", PropiedadeNodo.TipoDePropiedadNodo.NUMERO);
            propiedaB.setPrimerValor(String.valueOf(dDUniform.getMax()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof ConstantDistribution) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.CONSTANT);
            ConstantDistribution constantDistribution = (ConstantDistribution) discreteDistribution;

            PropiedadeNodo propiedaA = new PropiedadeNodo(id + "_Constante", "Constante", PropiedadeNodo.TipoDePropiedadNodo.NUMERO);
            propiedaA.setPrimerValor(String.valueOf(constantDistribution.getConstant()));
            propiedadeNodos.add(propiedaA);
        }
    }

    private String getStringArrayDoubles(double[] valores) {
        StringBuilder resultado = new StringBuilder();
        String separador = "";
        for (int i = 0; i < valores.length; i++) {
            resultado.append(separador + valores[i]);
            separador = "-";
        }
        return resultado.toString();
    }

    @Override
    public void updatePropiedad(String id, String valor)
    {
        System.out.println("prop control " + id +" "+valor);

        if (id.equalsIgnoreCase("nombre")) 
        {
            nodoGraficoSeleccionado.setNombre(valor);
            //clientNode.setID(valor);
        }
        
        if (nodoGraficoSeleccionado instanceof NodoClienteGrafico)
        {
            ClientNode clientNode = (ClientNode) parejasDeNodosExistentes.get(nodoGraficoSeleccionado);
            if(id.equalsIgnoreCase("generacionTrabajos"))
            {
                clientNode.getState().setJobInterArrival(getDistributionByText(valor));                
            }            
        }  
    }
    
    private DiscreteDistribution getDistributionByText(String value)
    {
        if(value.equals("Uniforme"))
        {
            return new DDUniform(10, 20);
        }  
        else if(value.equals("Constante"))
        {
            return new ConstantDistribution(10);
        }
        else if(value.equals("Erlang"))
        {
            return new DDErlang(1, 2);
        }
        else if(value.equals("Hiper-exponencial"))
        {
            return new DDHyperExp(new double[]{1,2,3}, new double[]{1,2,3});
        }
        else if(value.equals("Exponencial-negativa"))
        {
            return new DDNegExp(2);
        }
        else if(value.equals("Normal"))
        {
            return new DDNormal(2,3);            
        }
        else if(value.equals("Possion"))
        {
            return new DDPoissonProcess(2);
        }
        
        
        
        
        return null;
        
    }
}