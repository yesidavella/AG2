package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;

public class EnrutadorOpticoGrafico extends EnrutadorGrafico{

   private   static short contadorNodo = 0;
    public EnrutadorOpticoGrafico(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo,ControladorAbstractoAdminEnlace ctrlAbsAdminEnlace) {
        super("Enrutador_Optico_"+(++contadorNodo),"../../../../recursos/imagenes/enrutador_optico_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
    }
}
