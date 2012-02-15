package com.ag2.controlador;

import com.ag2.modelo.PhosphorusLinkModel;
import com.ag2.modelo.LinkCreationAbstractModel;
import com.ag2.presentacion.diseño.GraphLink;
import com.ag2.presentacion.diseño.propiedades.EntityPropertyTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class LinkAdminAbstractController implements Serializable {

    protected ArrayList<LinkCreationAbstractModel> linkCreationAbstractModels;
    protected EntityPropertyTable entityPropertyTable;
    protected Hashtable<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer;

    public LinkAdminAbstractController() {
        linkCreationAbstractModels = new ArrayList<LinkCreationAbstractModel>();
        linkMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer();
    }

    public boolean addModel(LinkCreationAbstractModel linkCreationAbstractModel) {
        return linkCreationAbstractModels.add(linkCreationAbstractModel) && linkCreationAbstractModel.addControlador(this);
    }

    public boolean removeModel(LinkAdminAbstractController linkAdminAbstractController) {
        return linkCreationAbstractModels.remove(linkAdminAbstractController) && linkAdminAbstractController.removeModel(this);
    }

    public void setLinkView(EntityPropertyTable entityPropertyTable) {
        this.entityPropertyTable = entityPropertyTable;
    }

    public abstract void createLink(GraphLink graphLink);
    
    public abstract boolean removeLink(GraphLink graphLink);

    public abstract void queryProperty(GraphLink graphLink);

    public abstract void updatePropiedad(GraphLink graphLink ,String id, String value);

    public abstract void reCreatePhosphorousLinks();
}
