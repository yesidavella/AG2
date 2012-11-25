package edu.ag2.presentation.design;

import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import edu.ag2.presentation.images.ImageHelper;
import edu.ag2.util.Utils;
import java.io.ObjectInputStream;
import java.util.List;

public class BrokerGrahpNode extends GraphNode {

    private static short nodeCounter = 0;

    public BrokerGrahpNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController,
            List<LinkAdminAbstractController> linkAdminAbstractController) {

        super(graphDesignGroup, "Agentador_" + (++nodeCounter),
                "nodo_servicio_mapa.png",
                 "nodo_servicio_mapa_node.png",
                nodeAdminAbstractController, linkAdminAbstractController);
        setHeight((short) 74);
        setWidth((short) 61);
        lineBreakStep = 12;
    }

    @Override
    public boolean isEnableToCreateLink(GraphNode graphNode) {
        return (graphNode instanceof SwitchGraphNode);
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