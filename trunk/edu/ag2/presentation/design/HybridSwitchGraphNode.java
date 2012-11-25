package edu.ag2.presentation.design;

import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import edu.ag2.presentation.images.ImageHelper;
import edu.ag2.util.Utils;
import java.io.ObjectInputStream;
import java.util.List;

public class HybridSwitchGraphNode extends SwitchGraphNode {

    private static short nodeCounter = 0;

    public HybridSwitchGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController,
            List<LinkAdminAbstractController> linkAdminAbstractController) {

        super(graphDesignGroup, "Enrutador_Hibrido_" + (++nodeCounter),
                ImageHelper.getResourceInputStream("enrutador_hibrido_mapa.png"),
                ImageHelper.getResourceInputStream("enrutador_hibrido_mapa_node.png"),
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