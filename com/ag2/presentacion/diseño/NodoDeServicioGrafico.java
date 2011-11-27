package com.ag2.presentacion.diseño;

public class NodoDeServicioGrafico extends NodoGrafico{

    private   static short contadorNodo = 0;
    public NodoDeServicioGrafico() {
        super( "Agentador_"+(++contadorNodo),"../../../../recursos/imagenes/nodo_servicio_mapa.png");
    }
}
