package com.ag2.controlador;

import com.ag2.modelo.PhosphorusLinkModel;
import com.ag2.modelo.LinkCreationAbstractModel;
import com.ag2.presentacion.diseño.GraphLink;
import com.ag2.presentacion.diseño.propiedades.TablaPropiedadesDispositivo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class LinkAdminAbstractController implements Serializable {

    protected ArrayList<LinkCreationAbstractModel> registeredModels;
    protected TablaPropiedadesDispositivo tblPropiedadesDispositivo;
    protected Hashtable<GraphLink, PhosphorusLinkModel> parejasDeEnlacesExistentes;

    public LinkAdminAbstractController() {
        registeredModels = new ArrayList<LinkCreationAbstractModel>();
        parejasDeEnlacesExistentes = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObject();
    }

    public boolean addModelo(LinkCreationAbstractModel modelo) {
        return registeredModels.add(modelo) && modelo.addControlador(this);
    }

    public boolean removeModelo(LinkAdminAbstractController controlador) {
        return registeredModels.remove(controlador) && controlador.removeModelo(this);
    }

    public void setVistaEnlace(TablaPropiedadesDispositivo propiedadesDispositivoTbl) {
        this.tblPropiedadesDispositivo = propiedadesDispositivoTbl;
    }

    public abstract void crearEnlace(GraphLink enlaceGrafico);
    
    public abstract boolean removeLink(GraphLink graphLink);

    public abstract void consultarPropiedades(GraphLink enlaceGrafico);

    public abstract void updatePropiedad(GraphLink enlaceGrafico, String id, String valor);

    public abstract void reCreatePhosphorousLinks();
}
