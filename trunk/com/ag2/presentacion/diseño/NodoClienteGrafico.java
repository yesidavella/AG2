package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.controles.GrupoDeDiseno;

public class NodoClienteGrafico extends NodoGrafico{

    private static int contadorNodo = 0;
    
    public NodoClienteGrafico(GrupoDeDiseno grupoDeDiseno, ControladorAbstractoAdminNodo controladorAbstractoAdminNodo,ControladorAbstractoAdminEnlace ctrlAbsAdminEnlace)
    {
        
        super(grupoDeDiseno,"Cliente_"+(++contadorNodo), "../../../../recursos/imagenes/cliente_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        
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