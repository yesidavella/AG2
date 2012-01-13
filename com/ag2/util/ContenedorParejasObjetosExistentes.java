package com.ag2.util;

import com.ag2.modelo.EnlacePhosphorous;
import Grid.Entity;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import com.ag2.presentacion.diseño.NodoGrafico;
import java.util.Hashtable;

public class ContenedorParejasObjetosExistentes{
    
    private static ContenedorParejasDeNodosExistentes contenedorParejasNodosExistentes;
    private static ContenedorParejasDeEnlacesExistentes contenedorParejasEnlacesExistentes;
    
    public static ContenedorParejasDeNodosExistentes getInstanciaParejasDeNodosExistentes(){
    
        if(contenedorParejasNodosExistentes == null){
            contenedorParejasNodosExistentes = new ContenedorParejasDeNodosExistentes();
        }
        return contenedorParejasNodosExistentes;
    }
    
    public static ContenedorParejasDeEnlacesExistentes getInstanciaParejasDeEnlacesExistentes() {

        if (contenedorParejasEnlacesExistentes == null) {
            contenedorParejasEnlacesExistentes = new ContenedorParejasDeEnlacesExistentes();
        }
        return contenedorParejasEnlacesExistentes;
    }
    
    //Clases internas para los contenedores//
    public static class ContenedorParejasDeNodosExistentes extends Hashtable<NodoGrafico,Entity> {
        
        public ContenedorParejasDeNodosExistentes(){
            super();
        }
    }
    
    public static class ContenedorParejasDeEnlacesExistentes extends Hashtable<EnlaceGrafico,EnlacePhosphorous> {

        public ContenedorParejasDeEnlacesExistentes(){
            super();
        }
    }
    
}
