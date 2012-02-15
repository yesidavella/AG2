package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.EnlacePhosphorous;
import com.ag2.presentacion.diseño.GraphLink;
import com.ag2.presentacion.diseño.GraphNode;
import java.io.Serializable;
import java.util.Hashtable;

public abstract class ContenedorParejasObjetosExistentes implements Serializable{
    
    private static Hashtable<GraphNode,Entity> contenedorParejasNodosExistentes;
    private static Hashtable<GraphLink,EnlacePhosphorous> contenedorParejasEnlacesExistentes;
    
    public static Hashtable<GraphNode,Entity> getInstanciaParejasDeNodosExistentes(){
    
        if(contenedorParejasNodosExistentes == null){
            contenedorParejasNodosExistentes = new Hashtable<GraphNode,Entity>();
        }
        return contenedorParejasNodosExistentes;
    }
    
    public static Hashtable getInstanciaParejasDeEnlacesExistentes() {

        if (contenedorParejasEnlacesExistentes == null) {
            contenedorParejasEnlacesExistentes = new Hashtable<GraphLink,EnlacePhosphorous>();
        }
        return contenedorParejasEnlacesExistentes;
    }
}
