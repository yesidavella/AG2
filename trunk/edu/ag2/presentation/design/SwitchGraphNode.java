package edu.ag2.presentation.design;

import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import java.io.InputStream;
import java.util.List;

public abstract class SwitchGraphNode extends GraphNode {

    public SwitchGraphNode(GraphDesignGroup graphDesignGroup, String name,   String   imageURL,String   imageURL_View,  NodeAdminAbstractController nodeAdminAbstractController, List<LinkAdminAbstractController> linkAdminAbstractController) {
        super(graphDesignGroup, name, imageURL,imageURL_View ,nodeAdminAbstractController, linkAdminAbstractController);
        setHeight((short) 62);
        setWidth((short) 42);

        lineBreakStep = 10;
    }

    @Override
    public boolean isEnableToCreateLink(GraphNode graphNode) {
        return true;
    }
}