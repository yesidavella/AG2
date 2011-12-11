package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminNodo;

public class NodoDeServicioGrafico extends NodoGrafico{

    private   static short contadorNodo = 0;
    public NodoDeServicioGrafico(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo) {
        super( "Agentador_"+(++contadorNodo),"../../../../recursos/imagenes/nodo_servicio_mapa.png",controladorAbstractoAdminNodo);
    }
}
