package com.ag2.util;

import Grid.Entity;
import com.ag2.presentacion.diseño.NodoGrafico;
import java.util.Hashtable;

public class ContenedorParejasNodosExistentes extends Hashtable<NodoGrafico, Entity> {
    
    private static ContenedorParejasNodosExistentes contenedorParejasNodosExistentes;
    
    public static ContenedorParejasNodosExistentes getInstanciaParejasDeNodosExistentes(){
    
        if(contenedorParejasNodosExistentes == null){
            contenedorParejasNodosExistentes = new ContenedorParejasNodosExistentes();
        }
        return contenedorParejasNodosExistentes;
    }
    
    private ContenedorParejasNodosExistentes(){
        super();
    }
}
