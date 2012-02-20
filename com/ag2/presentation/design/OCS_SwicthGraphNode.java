package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;

public class OCS_SwicthGraphNode extends SwitchGraphNode{

   private   static short nodeCounter = 0;
    public OCS_SwicthGraphNode(GraphDesignGroup graphDesignGroup,NodeAdminAbstractController nodeAdminAbstractController,LinkAdminAbstractController linkAdminAbstractController) {
        super(graphDesignGroup,"Enrutador_Optico_"+(++nodeCounter),"../../../../resource/image/enrutador_optico_mapa.png", nodeAdminAbstractController,linkAdminAbstractController);
    }
     private void readObject(ObjectInputStream inputStream)
    {
        try
        {
           inputStream.defaultReadObject();
           nodeCounter++;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
