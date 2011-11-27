package com.ag2.presentacion.diseño;

public class EnrutadorOpticoGrafico extends EnrutadorGrafico{

   private   static short contadorNodo = 0;
    public EnrutadorOpticoGrafico() {
        super("Enrutador_Optico_"+(++contadorNodo),"../../../../recursos/imagenes/enrutador_optico_mapa.png");
    }
}
