package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminNodo;

public class NodoDeRecursoGrafico extends NodoGrafico{

    private   static short contadorNodo = 0;
    public NodoDeRecursoGrafico(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo) {
        super("Cluster_"+(++contadorNodo),"../../../../recursos/imagenes/recurso_cursor_mapa.png", controladorAbstractoAdminNodo);
        setAlto((short)67);
        setAncho((short)49);
    }
}
