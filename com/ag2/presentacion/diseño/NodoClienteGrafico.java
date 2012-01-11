package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminNodo;

public class NodoClienteGrafico extends NodoGrafico{

    private static int contadorNodo = 0;
    
    public NodoClienteGrafico(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo)
    {
        
        super("Cliente_"+(++contadorNodo), "../../../../recursos/imagenes/cliente_mapa.png", controladorAbstractoAdminNodo);
        
        setAlto((short)50);

        if(contadorNodo<10){
            setAncho((short)40);
        }else{
            setAncho((short)44);
        }
        
        pasoDeSaltoLinea = 9;
    }

    @Override
    public boolean puedeGenerarEnlaceCon(NodoGrafico nodoInicioDelEnlace) {
        return (nodoInicioDelEnlace instanceof EnrutadorGrafico) && getCantidadDeEnlaces()<1;
    }
    
}