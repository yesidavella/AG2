package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.ModeloAbstractoCrearEnlace;
import com.ag2.modelo.ModeloCrearEnlace;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import com.ag2.presentacion.diseño.NodoGrafico;
import com.ag2.util.ContenedorParejasNodosExistentes;

public class ControladorAdminEnlace extends ControladorAbstractoAdminEnlace {

    @Override
    public void crearEnlace(EnlaceGrafico enlaceGrafico) {
                
        for(ModeloAbstractoCrearEnlace modelo:modelosRegistrados){

            if(modelo instanceof ModeloCrearEnlace){
                
                NodoGrafico nodoGraficoA = enlaceGrafico.getNodoGraficoA();
                NodoGrafico nodoGraficoB = enlaceGrafico.getNodoGraficoB();

                Entity nodoPhosphorousA = ContenedorParejasNodosExistentes.getInstanciaParejasDeNodosExistentes().get(nodoGraficoA);
                Entity nodoPhosphorousB = ContenedorParejasNodosExistentes.getInstanciaParejasDeNodosExistentes().get(nodoGraficoB);
                
                if( nodoPhosphorousA!=null && nodoPhosphorousB!=null){
                    modelo.crearEnlacePhosphorous(nodoPhosphorousA,nodoPhosphorousB);
                }else{
                    System.out.println("Algun nodo PHOSPHOROUS esta NULL, NO se creo el ENLACE en PHOPHOROUS.");
                }
            }
        }
    }
}
