package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;

public class OCS_SwicthGraphNode extends SwitchGraphNode{

   private   static short contadorNodo = 0;
    public OCS_SwicthGraphNode(GraphDesignGroup grupoDeDiseno,NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace) {
        super(grupoDeDiseno,"Enrutador_Optico_"+(++contadorNodo),"../../../../resource/image/enrutador_optico_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
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
