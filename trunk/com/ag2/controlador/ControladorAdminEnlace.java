package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.ModeloAbstractoCrearEnlace;
import com.ag2.modelo.ModeloCrearEnlace;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import com.ag2.presentacion.diseño.NodoGrafico;

public class ControladorAdminEnlace extends ControladorAbstractoAdminEnlace {

    @Override
    public void crearEnlace(EnlaceGrafico enlaceGrafico) {
                
        for(ModeloAbstractoCrearEnlace modelo:modelosRegistrados){

            if(modelo instanceof ModeloCrearEnlace){
                
                NodoGrafico nodoGraficoA = enlaceGrafico.getNodoGraficoA();
                NodoGrafico nodoGraficoB = enlaceGrafico.getNodoGraficoB();

                Entity nodoPhophorousA = ControladorAbstractoAdminNodo.PAREJA_DE_NODOS_EXISTENTES.get(nodoGraficoA);
                Entity nodoPhophorousB = ControladorAbstractoAdminNodo.PAREJA_DE_NODOS_EXISTENTES.get(nodoGraficoB);
                
                if( nodoPhophorousA!=null && nodoPhophorousB!=null){
                    modelo.crearEnlacePhosphorous(nodoPhophorousA,nodoPhophorousB);
                }else{
                    System.out.println("Algun nodo PHOSPHOROUS esta NULL, NO se creo el ENLACE en PHOPHOROUS.");
                }
            }
        }
        
    }
}
