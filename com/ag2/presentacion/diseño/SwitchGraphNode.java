package com.ag2.presentacion.diseño;

import com.ag2.controlador.LinkAdminAbstractController;
import com.ag2.controlador.NodeAdminAbstractController;

public  abstract class SwitchGraphNode extends GraphNode{      
    
    public SwitchGraphNode(GraphDesignGroup grupoDeDiseno,  String nombre, String urlDeImagen, NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace)
    {        
        super(grupoDeDiseno,nombre, urlDeImagen, controladorAbstractoAdminNodo,ctrlAbsAdminEnlace); 
        setAlto((short)62);
        setAncho((short)42);
        
        pasoDeSaltoLinea = 10;
    }

    @Override
    public boolean puedeGenerarEnlaceCon(GraphNode nodoComienzoEnlace) {
        
        if(nodoComienzoEnlace instanceof ClientGraphNode){
            return (nodoComienzoEnlace.getCantidadDeEnlaces()>=1)?false:true;
        }
        
        return true;
    }
    
}
