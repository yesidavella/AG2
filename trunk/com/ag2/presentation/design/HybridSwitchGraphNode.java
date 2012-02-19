package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import java.io.ObjectInputStream;

public class HybridSwitchGraphNode extends SwitchGraphNode{

    private static short contadorNodo = 0;
    
    public HybridSwitchGraphNode(GraphDesignGroup grupoDeDiseno, NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace) 
    {
        super(grupoDeDiseno,"Enrutador_Hibrido_"+(++contadorNodo) ,"../../../../resource/image/enrutador_hibrido_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        
        if(contadorNodo>9){
            setAncho((short)46);
        }
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
