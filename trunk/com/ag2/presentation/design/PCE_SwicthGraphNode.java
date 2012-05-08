package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.util.Utils;
import java.io.ObjectInputStream;

public class PCE_SwicthGraphNode extends SwitchGraphNode {

    private static short nodeCounter = 0;

    public PCE_SwicthGraphNode(GraphDesignGroup graphDesignGroup, NodeAdminAbstractController nodeAdminAbstractController,
            LinkAdminAbstractController linkAdminAbstractController) {

        super(graphDesignGroup, "PCE" + (++nodeCounter), Utils.ABS_PATH_IMGS + "PCE_mapa.png",
                nodeAdminAbstractController, linkAdminAbstractController);
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