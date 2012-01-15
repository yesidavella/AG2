package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.EnlacePhosphorous;
import com.ag2.modelo.ModeloAbstractoCrearEnlace;
import com.ag2.modelo.ModeloCrearEnlace;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import com.ag2.presentacion.diseño.NodoGrafico;
import com.ag2.presentacion.diseño.propiedades.PropiedadeNodo;
import com.ag2.util.ContenedorParejasObjetosExistentes;
import java.util.ArrayList;
import java.util.Hashtable;

public class ControladorAdminEnlace extends ControladorAbstractoAdminEnlace {

    @Override
    public void crearEnlace(EnlaceGrafico enlaceGrafico) {
                
        for(ModeloAbstractoCrearEnlace modelo:modelosRegistrados){

            if(modelo instanceof ModeloCrearEnlace){
                
                NodoGrafico nodoGraficoA = enlaceGrafico.getNodoGraficoA();
                NodoGrafico nodoGraficoB = enlaceGrafico.getNodoGraficoB();

                Entity nodoPhosphorousA = (Entity) ContenedorParejasObjetosExistentes.getInstanciaParejasDeNodosExistentes().get(nodoGraficoA);
                Entity nodoPhosphorousB = (Entity) ContenedorParejasObjetosExistentes.getInstanciaParejasDeNodosExistentes().get(nodoGraficoB);
                
                if( nodoPhosphorousA!=null && nodoPhosphorousB!=null){
                    EnlacePhosphorous nuevoEnlacePhosphorous = modelo.crearEnlacePhosphorous(nodoPhosphorousA,nodoPhosphorousB);
                    ContenedorParejasObjetosExistentes.getInstanciaParejasDeEnlacesExistentes().put(enlaceGrafico, nuevoEnlacePhosphorous);
                }else{
                    System.out.println("Algun nodo PHOSPHOROUS esta NULL, NO se creo el ENLACE en PHOPHOROUS.");
                }
            }
        }
    }

    @Override
    public void consultarPropiedades(EnlaceGrafico enlaceGrafico) {

        ArrayList<PropiedadeNodo> propiedadesDeNodo = new ArrayList<PropiedadeNodo>();

        //===========================================================================================================
        PropiedadeNodo propiedadNodoNombre = new PropiedadeNodo("nombre", "Nombre", PropiedadeNodo.TipoDePropiedadNodo.TEXTO);
        
        EnlacePhosphorous enlacePhosSeleccionado = (EnlacePhosphorous)ContenedorParejasObjetosExistentes.getInstanciaParejasDeEnlacesExistentes().get(enlaceGrafico);
        
        propiedadNodoNombre.setPrimerValor(enlaceGrafico.getNodoGraficoA().getNombre()+"<->"+enlaceGrafico.getNodoGraficoB().getNombre());
        propiedadesDeNodo.add(propiedadNodoNombre);
        //===========================================================================================================
        
        tblPropiedadesDispositivo.cargarPropiedades(propiedadesDeNodo);
    }
}
