package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.util.Utils;
import java.io.ObjectInputStream;
import java.util.List;

public class ResourceGraphNode extends GraphNode {

    private static short nodeCounter = 0;

    public ResourceGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController, List<LinkAdminAbstractController> linkAdminAbstractController) {
        super(graphDesignGroup, "Cluster_" + (++nodeCounter), Utils.ABS_PATH_IMGS + "recurso_cursor_mapa.png", nodeAdminAbstractController, linkAdminAbstractController);
        setHeight((short) 67);
        setWidth((short) 49);

        lineBreakStep = 11;
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
            nodeCounter++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isEnableToCreateLInk(GraphNode graphNode) {
        return (graphNode instanceof SwitchGraphNode);
    }
}