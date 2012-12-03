package edu.ag2.presentation.design;

import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;
import java.util.List;

public class ClientGraphNode extends GraphNode {

    private static int nodeCounter = 0;

    public ClientGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController,
            List<LinkAdminAbstractController> linkAdminAbstractController) {

        super(graphDesignGroup, "Cliente_" + (++nodeCounter),
                "cliente_mapa.png",
                "cliente_mapa_node.png",
                nodeAdminAbstractController, linkAdminAbstractController,
                (short) 15);

        setWidth((short) 42);
        setHeight((short) 38);
    }

    @Override
    public boolean isEnableToCreateLink(GraphNode graphNode) {
        return graphNode instanceof SwitchGraphNode;
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
            nodeCounter++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}