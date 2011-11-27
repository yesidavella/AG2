package com.ag2.presentacion.diseño;

public class NodoDeRecursoGrafico extends NodoGrafico{

    private   static short contadorNodo = 0;
    public NodoDeRecursoGrafico() {
        super("Cluster_"+(++contadorNodo),"../../../../recursos/imagenes/recurso_cursor_mapa.png");
    }
}
