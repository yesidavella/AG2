package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminNodo;

public class EnrutadorOpticoGrafico extends EnrutadorGrafico{

   private   static short contadorNodo = 0;
    public EnrutadorOpticoGrafico(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo) {
        super("Enrutador_Optico_"+(++contadorNodo),"../../../../recursos/imagenes/enrutador_optico_mapa.png", controladorAbstractoAdminNodo);
    }
}
