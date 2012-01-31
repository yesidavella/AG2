package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.controles.GrupoDeDiseno;

public class EnrutadorOpticoGrafico extends EnrutadorGrafico{

   private   static short contadorNodo = 0;
    public EnrutadorOpticoGrafico(GrupoDeDiseno grupoDeDiseno,ControladorAbstractoAdminNodo controladorAbstractoAdminNodo,ControladorAbstractoAdminEnlace ctrlAbsAdminEnlace) {
        super(grupoDeDiseno,"Enrutador_Optico_"+(++contadorNodo),"../../../../recursos/imagenes/enrutador_optico_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
    }
}
