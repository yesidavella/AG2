package com.ag2.presentacion.diseño;

public class NodoClienteGrafico extends NodoGrafico{

    private static int contadorNodo  =0; 
    public NodoClienteGrafico()
    {
        super("Cliente_"+(++contadorNodo), "../../../../recursos/imagenes/cliente_mapa.png");
    }
}