package com.ag2.controller;

import com.ag2.model.LinkCreationAbstractModel;
import com.ag2.model.PhosphorusLinkModel;
import com.ag2.presentation.design.GraphLink;
import com.ag2.presentation.design.property.EntityPropertyTableView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class LinkAdminAbstractController implements Serializable {

    protected ArrayList<LinkCreationAbstractModel> linkCreationAbstractModels;
    protected EntityPropertyTableView entityPropertyTable;
    protected Hashtable<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer;

    public LinkAdminAbstractController() {
        linkCreationAbstractModels = new ArrayList<LinkCreationAbstractModel>();
        linkMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer();
    }

    public boolean addModel(LinkCreationAbstractModel linkCreationAbstractModel) {
        return linkCreationAbstractModels.add(linkCreationAbstractModel) && linkCreationAbstractModel.addController(this);
    }

    public boolean removeModel(LinkAdminAbstractController linkAdminAbstractController) {
        return linkCreationAbstractModels.remove(linkAdminAbstractController) && linkAdminAbstractController.removeModel(this);
    }

    public void setLinkView(EntityPropertyTableView entityPropertyTable) {
        this.entityPropertyTable = entityPropertyTable;
    }

    public abstract void createLink(GraphLink graphLink);
    
    public abstract boolean removeLink(GraphLink graphLink);

    public abstract void queryProperty(GraphLink graphLink);

    public abstract void updatePropiedad(GraphLink graphLink ,String id, String value);

    public abstract void reCreatePhosphorousLinks();
}