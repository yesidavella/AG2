package com.ag2.model;

import Grid.Entity;
import com.ag2.controller.LinkAdminAbstractController;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class LinkCreationAbstractModel implements Serializable{
    
    private ArrayList<LinkAdminAbstractController> linkAdminAbstractControllers;
    
    public LinkCreationAbstractModel(){
        linkAdminAbstractControllers = new ArrayList<LinkAdminAbstractController>();
    }
    
    public boolean addController(LinkAdminAbstractController linkAdminAbstractController){
        return linkAdminAbstractControllers.add(linkAdminAbstractController);
    }
    
    public boolean removeController(LinkAdminAbstractController linkAdminAbstractController){
        return linkAdminAbstractControllers.remove(linkAdminAbstractController);
    }
    
    public abstract PhosphorusLinkModel createPhosphorusLink(Entity entityA,Entity entityB);
}