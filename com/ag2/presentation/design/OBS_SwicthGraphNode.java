package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;

public class OBS_SwicthGraphNode extends SwitchGraphNode
{
    private   static short contadorNodo = 0;
    public OBS_SwicthGraphNode(GraphDesignGroup grupoDeDiseno,NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace) {
        super(grupoDeDiseno,"Enrutador_Rafaga_"+(++contadorNodo), "../../../../resource/image/enrutador_rafaga_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
    }
    private void readObject(ObjectInputStream inputStream)
    {
        try
        {
           inputStream.defaultReadObject();
           contadorNodo++; 
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

}
