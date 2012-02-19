package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;

public class BrokerGrahpNode extends GraphNode{

    private   static short contadorNodo = 0;
    public BrokerGrahpNode(GraphDesignGroup grupoDeDiseno, NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace) {
        super(grupoDeDiseno, "Agentador_"+(++contadorNodo),"../../../../resource/image/nodo_servicio_mapa.png",controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
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
