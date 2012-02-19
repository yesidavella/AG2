package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;

public class ResourceGraphNode extends GraphNode{

    private   static short contadorNodo = 0;
    public ResourceGraphNode(GraphDesignGroup grupoDeDiseno,NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace) {
        super(grupoDeDiseno, "Cluster_"+(++contadorNodo),"../../../../resource/image/recurso_cursor_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        setAlto((short)67);
        setAncho((short)49);
                
        pasoDeSaltoLinea = 11;
    }

    @Override
    public boolean puedeGenerarEnlaceCon(GraphNode nodoComienzoEnlace) {
        return (nodoComienzoEnlace instanceof SwitchGraphNode) ;
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
