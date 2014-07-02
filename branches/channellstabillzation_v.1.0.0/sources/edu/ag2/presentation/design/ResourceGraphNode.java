package edu.ag2.presentation.design;

import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;
import java.util.List;

public class ResourceGraphNode extends GraphNode {

    private static short nodeCounter = 0;

    public ResourceGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController, List<LinkAdminAbstractController> linkAdminAbstractController) {
        super(graphDesignGroup, "Cluster_" + (++nodeCounter),
                "recurso_cursor_mapa.png",
                "recurso_cursor_mapa_node.png",
                nodeAdminAbstractController, linkAdminAbstractController,
                (short) 11);
        
        setWidth((short) 59);
        setHeight((short) 70);
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