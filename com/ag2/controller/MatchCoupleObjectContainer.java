package com.ag2.controller;

import Grid.Entity;
import com.ag2.model.PhosphorusLinkModel;
import com.ag2.presentation.design.GraphLink;
import com.ag2.presentation.design.GraphNode;
import java.util.HashMap;


public abstract class MatchCoupleObjectContainer {
    
    private static HashMap<GraphNode,Entity> nodeMatchCoupleObjectContainer;
    private static HashMap<GraphLink,PhosphorusLinkModel> linkMatchCoupleObjectContainer;
    
    public static HashMap<GraphNode,Entity> getInstanceNodeMatchCoupleObjectContainer(){
    
        if(nodeMatchCoupleObjectContainer == null){
            nodeMatchCoupleObjectContainer = new HashMap<GraphNode,Entity>();
        }
        return nodeMatchCoupleObjectContainer;
    }
    
    public static HashMap getInstanceLinkMatchCoupleObjectContainer() {

        if (linkMatchCoupleObjectContainer == null) {
            linkMatchCoupleObjectContainer = new HashMap<GraphLink,PhosphorusLinkModel>();
        }
        return linkMatchCoupleObjectContainer;
    }
}
