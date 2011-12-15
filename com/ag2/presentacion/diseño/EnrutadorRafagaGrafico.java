package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminNodo;

public class EnrutadorRafagaGrafico extends EnrutadorGrafico
{
    private   static short contadorNodo = 0;
    public EnrutadorRafagaGrafico(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo) {
        super("Enrutador\nRafaga_"+(++contadorNodo), "../../../../recursos/imagenes/enrutador_rafaga_mapa.png", controladorAbstractoAdminNodo);
    }

}
