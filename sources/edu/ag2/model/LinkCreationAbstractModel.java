package edu.ag2.model;

import Grid.Entity;
import edu.ag2.controller.LinkAdminAbstractController;
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
    
    public abstract Object createLink(Entity entityA,Entity entityB);
}