package com.ag2.controlador;

import Grid.Entity;
import com.ag2.modelo.PhosphorusLinkModel;
import com.ag2.presentacion.diseño.GraphLink;
import com.ag2.presentacion.diseño.GraphNode;
import java.io.Serializable;
import java.util.Hashtable;

public abstract class MatchCoupleObjectContainer implements Serializable{
    
    private static Hashtable<GraphNode,Entity> nodeMatchCoupleObjectContainer;
    private static Hashtable<GraphLink,PhosphorusLinkModel> linkMatchCoupleObjectContainer;
    
    public static Hashtable<GraphNode,Entity> getInstanceNodeMatchCoupleObjectContainer(){
    
        if(nodeMatchCoupleObjectContainer == null){
            nodeMatchCoupleObjectContainer = new Hashtable<GraphNode,Entity>();
        }
        return nodeMatchCoupleObjectContainer;
    }
    
    public static Hashtable getInstanceLinkMatchCoupleObjectContainer() {

        if (linkMatchCoupleObjectContainer == null) {
            linkMatchCoupleObjectContainer = new Hashtable<GraphLink,PhosphorusLinkModel>();
        }
        return linkMatchCoupleObjectContainer;
    }
}
