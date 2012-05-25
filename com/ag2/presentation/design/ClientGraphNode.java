package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.util.Utils;
import java.io.ObjectInputStream;
import java.util.List;

public class ClientGraphNode extends GraphNode{

    private static int nodeCounter = 0;
    
    public ClientGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController,
            List<LinkAdminAbstractController> linkAdminAbstractController) {
        
        super(graphDesignGroup,"Cliente_"+(++nodeCounter), Utils.ABS_PATH_IMGS+"cliente_mapa.png", 
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
    public boolean isEnableToCreateLink(GraphNode graphNode) {
        return graphNode instanceof SwitchGraphNode;
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