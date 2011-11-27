package com.ag2.presentacion.diseño;

public class EnrutadorHibridoGrafico extends EnrutadorGrafico{

    private   static short contadorNodo = 0; 
    public EnrutadorHibridoGrafico() 
    {
        super("Enrutador_Hibrido_"+(++contadorNodo) ,"../../../../recursos/imagenes/enrutador_hibrido_mapa.png");
    }
    
}
