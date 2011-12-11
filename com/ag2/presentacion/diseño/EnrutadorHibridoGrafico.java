package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminNodo;

public class EnrutadorHibridoGrafico extends EnrutadorGrafico{

    private static short contadorNodo = 0; 
    public EnrutadorHibridoGrafico(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo) 
    {
        super("Enrutador_Hibrido_"+(++contadorNodo) ,"../../../../recursos/imagenes/enrutador_hibrido_mapa.png", controladorAbstractoAdminNodo );
    }
    
}
