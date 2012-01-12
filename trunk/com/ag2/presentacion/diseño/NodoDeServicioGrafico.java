package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;

public class NodoDeServicioGrafico extends NodoGrafico{

    private   static short contadorNodo = 0;
    public NodoDeServicioGrafico(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo,ControladorAbstractoAdminEnlace ctrlAbsAdminEnlace) {
        super( "Agentador_"+(++contadorNodo),"../../../../recursos/imagenes/nodo_servicio_mapa.png",controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        setAlto((short)74);
        setAncho((short)61);
                
        pasoDeSaltoLinea = 12;
    }

    @Override
    public boolean puedeGenerarEnlaceCon(NodoGrafico nodoInicioDelEnlace) {
        return (nodoInicioDelEnlace instanceof EnrutadorGrafico);
    }

}
