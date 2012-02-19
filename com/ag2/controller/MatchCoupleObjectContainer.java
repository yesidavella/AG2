package com.ag2.controller;

import Grid.Entity;
import com.ag2.model.PhosphorusLinkModel;
import com.ag2.presentation.design.GraphLink;
import com.ag2.presentation.design.GraphNode;
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
