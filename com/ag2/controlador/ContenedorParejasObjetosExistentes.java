package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.EnlacePhosphorous;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import com.ag2.presentacion.diseño.NodoGrafico;
import java.io.Serializable;
import java.util.Hashtable;

public abstract class ContenedorParejasObjetosExistentes implements Serializable{
    
    private static Hashtable<NodoGrafico,Entity> contenedorParejasNodosExistentes;
    private static Hashtable<EnlaceGrafico,EnlacePhosphorous> contenedorParejasEnlacesExistentes;
    
    public static Hashtable<NodoGrafico,Entity> getInstanciaParejasDeNodosExistentes(){
    
        if(contenedorParejasNodosExistentes == null){
            contenedorParejasNodosExistentes = new Hashtable<NodoGrafico,Entity>();
        }
        return contenedorParejasNodosExistentes;
    }
    
    public static Hashtable getInstanciaParejasDeEnlacesExistentes() {

        if (contenedorParejasEnlacesExistentes == null) {
            contenedorParejasEnlacesExistentes = new Hashtable<EnlaceGrafico,EnlacePhosphorous>();
        }
        return contenedorParejasEnlacesExistentes;
    }
}
