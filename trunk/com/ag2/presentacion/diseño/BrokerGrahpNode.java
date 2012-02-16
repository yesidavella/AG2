package com.ag2.presentacion.diseño;

import com.ag2.controlador.LinkAdminAbstractController;
import com.ag2.controlador.NodeAdminAbstractController;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;

public class BrokerGrahpNode extends GraphNode{

    private   static short contadorNodo = 0;
    public BrokerGrahpNode(GrupoDeDiseno grupoDeDiseno, NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace) {
        super(grupoDeDiseno, "Agentador_"+(++contadorNodo),"../../../../recursos/imagenes/nodo_servicio_mapa.png",controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        setAlto((short)74);
        setAncho((short)61);
                
        pasoDeSaltoLinea = 12;
    }

    @Override
    public boolean puedeGenerarEnlaceCon(GraphNode nodoInicioDelEnlace) {
        return (nodoInicioDelEnlace instanceof SwitchGraphNode);
    }
     private void readObject(ObjectInputStream inputStream)
    {
        try
        {
           inputStream.defaultReadObject();
           contadorNodo++; 
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

}
