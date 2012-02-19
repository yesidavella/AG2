package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;

public class ClientGraphNode extends GraphNode{

    private static int nodeCounter = 0;
  
    
    public ClientGraphNode(GraphDesignGroup grupoDeDiseno, NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace)
    {
        
        super(grupoDeDiseno,"Cliente_"+(++nodeCounter), "../../../../resource/image/cliente_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        
        setHeight((short)50);

        if(nodeCounter<10){
            setWidth((short)40);
        }else{
            setWidth((short)44);
        }
        
        lineBreakStep = 9;
    }

    @Override
    public boolean isEnableToCreateLInk(GraphNode nodoInicioDelEnlace) {
        return (nodoInicioDelEnlace instanceof SwitchGraphNode) && getCantidadDeEnlaces()<1;
    }
 
    private void readObject(ObjectInputStream inputStream)
    {
        try
        {
           inputStream.defaultReadObject();
           nodeCounter++; 
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
}