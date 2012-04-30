package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.util.Utils;
import java.io.ObjectInputStream;

public class HybridSwitchGraphNode extends SwitchGraphNode {

    private static short nodeCounter = 0;

    public HybridSwitchGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController,
            LinkAdminAbstractController linkAdminAbstractController) {

        super(graphDesignGroup, "Enrutador_Hibrido_" + (++nodeCounter), Utils.ABS_PATH_IMGS + "enrutador_hibrido_mapa.png",
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