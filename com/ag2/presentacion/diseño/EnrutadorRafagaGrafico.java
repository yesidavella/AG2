package com.ag2.presentacion.diseño;

public class EnrutadorRafagaGrafico extends EnrutadorGrafico
{
    private   static short contadorNodo = 0;
    public EnrutadorRafagaGrafico() {
        super("Enrutador_Rafaga_"+(++contadorNodo), "../../../../recursos/imagenes/enrutador_rafaga_mapa.png");
    }

}
