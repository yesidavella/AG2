package com.ag2.controller;

import Grid.Entity;
import Grid.Port.GridOutPort;
import com.ag2.model.PhosphorusLinkModel;
import com.ag2.model.LinkCreationAbstractModel;
import com.ag2.model.ModeloCrearEnlace;
import com.ag2.model.SimulationBase;
import com.ag2.presentation.design.GraphLink;
import com.ag2.presentation.design.GraphNode;
import com.ag2.presentation.design.property.EntityProperty;
import java.util.ArrayList;
import java.util.Hashtable;
import simbase.Port.SimBaseInPort;
import simbase.SimBaseEntity;

public class LinkAdminController extends LinkAdminAbstractController {

  //  private Hashtable<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer();
    private Hashtable<GraphNode, Entity> nodeMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer();

    @Override
    public void createLink(GraphLink graphLink) {

        Entity phosphorusNodeA;
        Entity phosphorusNodeB;

        for (LinkCreationAbstractModel model : linkCreationAbstractModels) {

            if (model instanceof ModeloCrearEnlace) {

                GraphNode graphNodeA = graphLink.getGraphNodeA();
                GraphNode graphNodeB = graphLink.getGraphNodeB();

                phosphorusNodeA = (Entity) nodeMatchCoupleObjectContainer.get(graphNodeA);
                phosphorusNodeB = (Entity) nodeMatchCoupleObjectContainer.get(graphNodeB);

                if (phosphorusNodeA != null && phosphorusNodeB != null) {
                    PhosphorusLinkModel phosphorusLinkModel = model.createPhosphorusLink(phosphorusNodeA, phosphorusNodeB);
                    linkMatchCoupleObjectContainer.put(graphLink, phosphorusLinkModel);
                } else {
                    System.out.println("Algun nodo PHOSPHOROUS esta NULL, NO se creo el ENLACE en PHOPHOROUS.");
                }
            }
        }

        SimulationBase.getInstance().setLinkAdminAbstractController(this);
    }

    @Override
    public void queryProperty(GraphLink graphLink) {

        ArrayList<EntityProperty> entityPropertys = new ArrayList<EntityProperty>();
        PhosphorusLinkModel phosphorusSelectedLinkModel = (PhosphorusLinkModel) linkMatchCoupleObjectContainer.get(graphLink);
        GridOutPort gridOutPortA = phosphorusSelectedLinkModel.getGridOutPortA();
        GridOutPort gridOutPortB = phosphorusSelectedLinkModel.getGridOutPortB();
        /*
         **Enlace de nodo Phosphorous de A hacia B (A->B)
         */
        //===========================================================================================================
        EntityProperty propNombreDireccionCanalAB = new EntityProperty("direcciónCanalAB", "Dirección del Canal:", EntityProperty.TipoDePropiedadNodo.ETIQUETA, false);
        propNombreDireccionCanalAB.setPrimerValor(graphLink.getGraphNodeA().getName() + "-->" + graphLink.getGraphNodeB().getName());
        entityPropertys.add(propNombreDireccionCanalAB);

        EntityProperty propVelEnlaceAB = new EntityProperty("linkSpeedAB", "Vel. del Enlace:", EntityProperty.TipoDePropiedadNodo.NUMERO, false);
        propVelEnlaceAB.setPrimerValor(Double.toString(gridOutPortA.getLinkSpeed()));
        entityPropertys.add(propVelEnlaceAB);
        //===========================================================================================================

        /*
         **Enlace de nodo Phosphorous de B hacia A (B->A)
         */
        //===========================================================================================================
        EntityProperty propNombreDireccionCanalBA = new EntityProperty("direcciónCanalBA", "Dirección del Canal:", EntityProperty.TipoDePropiedadNodo.ETIQUETA, false);
        propNombreDireccionCanalBA.setPrimerValor(graphLink.getGraphNodeB().getName() + "-->" + graphLink.getGraphNodeA().getName());
        entityPropertys.add(propNombreDireccionCanalBA);

        EntityProperty propVelEnlaceBA = new EntityProperty("linkSpeedBA", "Vel. del Enlace:", EntityProperty.TipoDePropiedadNodo.NUMERO, false);
        propVelEnlaceBA.setPrimerValor(Double.toString(gridOutPortB.getLinkSpeed()));
        entityPropertys.add(propVelEnlaceBA);
        //===========================================================================================================

        /*
         **Propiedades comunes en ambas direcciones del canal.
         */
        EntityProperty propVelConmutacion = new EntityProperty("switchingSpeed", "Vel. de Conmutación:", EntityProperty.TipoDePropiedadNodo.NUMERO, false);
        propVelConmutacion.setPrimerValor((gridOutPortA.getSwitchingSpeed() == gridOutPortB.getSwitchingSpeed()) ? Double.toString(gridOutPortB.getSwitchingSpeed()) : "Problema leyendo Vel. de conmutación.");
        entityPropertys.add(propVelConmutacion);

        EntityProperty propWavelengths = new EntityProperty("defaultWavelengths", "Cantidad de λs:", EntityProperty.TipoDePropiedadNodo.NUMERO, false);
        propWavelengths.setPrimerValor((gridOutPortA.getMaxNumberOfWavelengths() == gridOutPortB.getMaxNumberOfWavelengths()) ? Integer.toString(gridOutPortB.getMaxNumberOfWavelengths()) : "Problema leyendo el numero de λ.");
        entityPropertys.add(propWavelengths);

        entityPropertyTable.loadProperties(entityPropertys);
    }

    @Override
    public void updatePropiedad(GraphLink enlaceGrafico, String id, String valor) {

        enlaceGrafico.getProperties().put(id, valor);

        PhosphorusLinkModel enlacePhosSeleccionado = (PhosphorusLinkModel) MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer().get(enlaceGrafico);

        if (id.equalsIgnoreCase("linkSpeedAB")) {
            enlacePhosSeleccionado.getGridOutPortA().setLinkSpeed(Double.valueOf(valor));
        } else if (id.equalsIgnoreCase("linkSpeedBA")) {
            enlacePhosSeleccionado.getGridOutPortB().setLinkSpeed(Double.valueOf(valor));
        } else if (id.equalsIgnoreCase("switchingSpeed")) {
            enlacePhosSeleccionado.getGridOutPortA().setSwitchingSpeed(Double.valueOf(valor));
            enlacePhosSeleccionado.getGridOutPortB().setSwitchingSpeed(Double.valueOf(valor));
        } else if (id.equalsIgnoreCase("defaultWavelengths")) {
            enlacePhosSeleccionado.getGridOutPortA().setMaxNumberOfWavelengths(Integer.parseInt(valor));
            enlacePhosSeleccionado.getGridOutPortB().setMaxNumberOfWavelengths(Integer.parseInt(valor));
        }
    }

    @Override
    public void reCreatePhosphorousLinks() {

        for (GraphLink enlaceGrafico : linkMatchCoupleObjectContainer.keySet()) {
            createLink(enlaceGrafico);
        }

        for (GraphLink enlaceGrafico : linkMatchCoupleObjectContainer.keySet()) {

            for (String id : enlaceGrafico.getProperties().keySet()) {
                updatePropiedad(enlaceGrafico, id, enlaceGrafico.getProperties().get(id));
            }
        }
    }

    @Override
    public boolean removeLink(GraphLink graphLink) {

        PhosphorusLinkModel phosLink = linkMatchCoupleObjectContainer.get(graphLink);

        boolean canRemovePortsInPhosNodeA = removeInAndOutPort(phosLink.getGridOutPortA());
        boolean canRemovePortsInPhosNodeB = removeInAndOutPort(phosLink.getGridOutPortB());

        linkMatchCoupleObjectContainer.remove(graphLink);

        return (canRemovePortsInPhosNodeA && canRemovePortsInPhosNodeB);
    }

    private boolean removeInAndOutPort(GridOutPort outPort) {
        //Remuevo el puerto de salida y de entrada
        SimBaseInPort inPort = outPort.getTarget();

        SimBaseEntity source = outPort.getOwner();
        SimBaseEntity target = inPort.getOwner();

        boolean canRemoveInPort = target.getInPorts().remove(inPort);
        boolean canRemoveOutPort = source.getOutPorts().remove(outPort);

        return canRemoveInPort && canRemoveOutPort;
    }
}
