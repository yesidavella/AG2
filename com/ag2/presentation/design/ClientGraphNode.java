package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.util.ResourcesPath;
import java.io.ObjectInputStream;

public class ClientGraphNode extends GraphNode{

    private static int nodeCounter = 0;
    
    public ClientGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController,
            LinkAdminAbstractController linkAdminAbstractController) {
        
        super(graphDesignGroup,"Cliente_"+(++nodeCounter), ResourcesPath.ABS_PATH_IMGS+"cliente_mapa.png", 
                nodeAdminAbstractController,linkAdminAbstractController);        
        setHeight((short)50);

        if(nodeCounter<10){
            setWidth((short)40);
        }else{
            setWidth((short)44);
        }
        
        lineBreakStep = 9;
    }

    @Override
    public boolean isEnableToCreateLInk(GraphNode graphNode) {
        return (graphNode instanceof SwitchGraphNode) && getLinkCounter()<1;
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