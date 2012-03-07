package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;

public  abstract class SwitchGraphNode extends GraphNode{

    public SwitchGraphNode(GraphDesignGroup graphDesignGroup,String name,String imageURL,NodeAdminAbstractController nodeAdminAbstractController,LinkAdminAbstractController linkAdminAbstractController)
    {
        super(graphDesignGroup,name, imageURL, nodeAdminAbstractController,linkAdminAbstractController);
        setHeight((short)62);
        setWidth((short)42);

        lineBreakStep = 10;
    }

    @Override
    public boolean isEnableToCreateLInk(GraphNode graphNode) {

        if(graphNode instanceof ClientGraphNode){
            return (graphNode.getLinkCounter()>=1)?false:true;
        }

        return true;
    }

}