package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import java.util.List;

public abstract class SwitchGraphNode extends GraphNode {

    public SwitchGraphNode(GraphDesignGroup graphDesignGroup, String name, String imageURL, NodeAdminAbstractController nodeAdminAbstractController, List<LinkAdminAbstractController> linkAdminAbstractController) {
        super(graphDesignGroup, name, imageURL, nodeAdminAbstractController, linkAdminAbstractController);
        setHeight((short) 62);
        setWidth((short) 42);

        lineBreakStep = 10;
    }

    @Override
    public boolean isEnableToCreateLInk(GraphNode graphNode) {
        return true;
    }
}