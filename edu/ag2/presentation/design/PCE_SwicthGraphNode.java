package edu.ag2.presentation.design;

import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;
import java.util.List;

public class PCE_SwicthGraphNode extends SwitchGraphNode {

    private static short nodeCounter = 0;

    public PCE_SwicthGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController,
            List<LinkAdminAbstractController> linkAdminAbstractController) {

        super(graphDesignGroup, "PCE" + (++nodeCounter),
                "PCE_mapa.png",
                "PCE_mapa_node.png",
                nodeAdminAbstractController, linkAdminAbstractController);
        
        setWidth((short)42);
        setHeight((short)60);
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