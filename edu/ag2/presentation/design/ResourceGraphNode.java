package edu.ag2.presentation.design;

import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import edu.ag2.presentation.images.ImageHelper;
import edu.ag2.util.Utils;
import java.io.ObjectInputStream;
import java.util.List;

public class ResourceGraphNode extends GraphNode {

    private static short nodeCounter = 0;

    public ResourceGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController, List<LinkAdminAbstractController> linkAdminAbstractController) {
        super(graphDesignGroup, "Cluster_" + (++nodeCounter), 
                ImageHelper.getResourceInputStream( "recurso_cursor_mapa.png"),
                ImageHelper.getResourceInputStream( "recurso_cursor_mapa_node.png"),
                nodeAdminAbstractController, linkAdminAbstractController);
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
    public boolean isEnableToCreateLink(GraphNode graphNode) {
        return (graphNode instanceof SwitchGraphNode);
    }
}