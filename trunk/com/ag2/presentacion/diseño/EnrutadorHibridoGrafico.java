package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;

public class EnrutadorHibridoGrafico extends EnrutadorGrafico{

    private static short contadorNodo = 0;
    
    public EnrutadorHibridoGrafico(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo,ControladorAbstractoAdminEnlace ctrlAbsAdminEnlace) 
    {
        super("Enrutador_Hibrido_"+(++contadorNodo) ,"../../../../recursos/imagenes/enrutador_hibrido_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        
        if(contadorNodo>9){
            setAncho((short)46);
        }
    }
    
}
