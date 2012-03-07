package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.util.ResourcesPath;
import java.io.ObjectInputStream;

public class OBS_SwicthGraphNode extends SwitchGraphNode
{
    private   static short nodeCounter = 0;
    
    public OBS_SwicthGraphNode(GraphDesignGroup graphDesignGroup,NodeAdminAbstractController nodeAdminAbstractController,
            LinkAdminAbstractController linkAdminAbstractController) {
        
        super(graphDesignGroup,"Enrutador_Rafaga_"+(++nodeCounter), ResourcesPath.ABS_PATH_IMGS+"enrutador_rafaga_mapa.png",
                nodeAdminAbstractController,linkAdminAbstractController);
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