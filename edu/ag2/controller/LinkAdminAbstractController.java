package edu.ag2.controller;

import edu.ag2.model.LinkCreationAbstractModel;
import edu.ag2.presentation.design.GraphLink;
import edu.ag2.presentation.design.GraphNode;
import edu.ag2.presentation.design.property.EntityPropertyTableView;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class LinkAdminAbstractController implements Serializable {

    protected ArrayList<LinkCreationAbstractModel> linkCreationAbstractModels;
    protected transient EntityPropertyTableView entityPropertyTable;   

    public LinkAdminAbstractController() {
        linkCreationAbstractModels = new ArrayList<LinkCreationAbstractModel>();
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

    public abstract boolean createLink(GraphNode sourceNode, GraphNode destinationNode);
    
    public abstract boolean removeLink(GraphLink graphLink);

    public abstract void queryProperty(GraphLink graphLink);

    public abstract void updatePropiedad(GraphLink graphLink ,String id, String value);

    public abstract void reCreatePhosphorousLinks();

    public abstract boolean canCreateLink(GraphNode wildcardNodeA, GraphNode graphNode);
}