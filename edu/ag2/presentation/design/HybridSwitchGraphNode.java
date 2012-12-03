package edu.ag2.presentation.design;

import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;
import java.util.List;

public class HybridSwitchGraphNode extends SwitchGraphNode {

    private static short nodeCounter = 0;

    public HybridSwitchGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController,
            List<LinkAdminAbstractController> linkAdminAbstractController) {

        super(graphDesignGroup, "Enrutador_" + (++nodeCounter),
                "enrutador_mapa.png",
               "enrutador_mapa_node.png",
                nodeAdminAbstractController, linkAdminAbstractController);

        if (nodeCounter > 9) {
            setWidth((short) 46);
        }
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