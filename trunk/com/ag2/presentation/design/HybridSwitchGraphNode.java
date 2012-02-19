package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;

public class HybridSwitchGraphNode extends SwitchGraphNode{

    private static short nodeCounter = 0;
    
    public HybridSwitchGraphNode(GraphDesignGroup grupoDeDiseno, NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace) 
    {
        super(grupoDeDiseno,"Enrutador_Hibrido_"+(++nodeCounter) ,"../../../../resource/image/enrutador_hibrido_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        
        if(nodeCounter>9){
            setWidth((short)46);
        }
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
