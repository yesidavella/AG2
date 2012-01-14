package com.ag2.util;

import Grid.Entity;
import com.ag2.modelo.EnlacePhosphorous;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import com.ag2.presentacion.diseño.NodoGrafico;
import java.util.Hashtable;

public class ContenedorParejasObjetosExistentes{
    
    private static Hashtable<NodoGrafico,Entity> contenedorParejasNodosExistentes;
    private static Hashtable<EnlaceGrafico,EnlacePhosphorous> contenedorParejasEnlacesExistentes;
    
    public static Hashtable getInstanciaParejasDeNodosExistentes(){
    
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
