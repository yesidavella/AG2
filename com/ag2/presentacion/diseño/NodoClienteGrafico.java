package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminNodo;

public class NodoClienteGrafico extends NodoGrafico{

    private static int contadorNodo  =0; 
    public NodoClienteGrafico(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo)
    {
        super("Cliente_"+(++contadorNodo), "../../../../recursos/imagenes/cliente_mapa.png", controladorAbstractoAdminNodo);
    }
}