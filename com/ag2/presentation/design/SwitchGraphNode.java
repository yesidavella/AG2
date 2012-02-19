package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;

public  abstract class SwitchGraphNode extends GraphNode{      
    
    public SwitchGraphNode(GraphDesignGroup grupoDeDiseno,  String nombre, String urlDeImagen, NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace)
    {        
        super(grupoDeDiseno,nombre, urlDeImagen, controladorAbstractoAdminNodo,ctrlAbsAdminEnlace); 
        setHeight((short)62);
        setWidth((short)42);
        
        lineBreakStep = 10;
    }

    @Override
    public boolean isEnableToCreateLInk(GraphNode nodoComienzoEnlace) {
        
        if(nodoComienzoEnlace instanceof ClientGraphNode){
            return (nodoComienzoEnlace.getCantidadDeEnlaces()>=1)?false:true;
        }
        
        return true;
    }
    
}
