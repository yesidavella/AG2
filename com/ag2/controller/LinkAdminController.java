package com.ag2.controller;

import Grid.Entity;
import Grid.Port.GridOutPort;
import com.ag2.model.LinkCreationAbstractModel;
import com.ag2.model.LinkCreationModel;
import com.ag2.model.PhosphorusLinkModel;
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

            if (model instanceof LinkCreationModel) {

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
        EntityProperty propNameDirectionChannelAB = new EntityProperty("channelDirectionAB", "Dirección del Canal:", EntityProperty.PropertyType.LABEL, false);
        propNameDirectionChannelAB.setFirstValue(graphLink.getGraphNodeA().getName() + "-->" + graphLink.getGraphNodeB().getName());
        entityPropertys.add(propNameDirectionChannelAB);

        EntityProperty propLinkSpeedAB = new EntityProperty("linkSpeedAB", "Vel. del Enlace:", EntityProperty.PropertyType.NUMBER, false);
        propLinkSpeedAB.setFirstValue(Double.toString(gridOutPortA.getLinkSpeed()));
        entityPropertys.add(propLinkSpeedAB);
        //===========================================================================================================

        /*
         **Enlace de nodo Phosphorous de B hacia A (B->A)
         */
        //===========================================================================================================
        EntityProperty propNameDirectionChannelBA = new EntityProperty("channelDirectionBA", "Dirección del Canal:", EntityProperty.PropertyType.LABEL, false);
        propNameDirectionChannelBA.setFirstValue(graphLink.getGraphNodeB().getName() + "-->" + graphLink.getGraphNodeA().getName());
        entityPropertys.add(propNameDirectionChannelBA);

        EntityProperty propLinkSpeedBA = new EntityProperty("linkSpeedBA", "Vel. del Enlace:", EntityProperty.PropertyType.NUMBER, false);
        propLinkSpeedBA.setFirstValue(Double.toString(gridOutPortB.getLinkSpeed()));
        entityPropertys.add(propLinkSpeedBA);
        //===========================================================================================================

        /*
         **Propiedades comunes en ambas direcciones del canal.
         */
        EntityProperty propSwitchingSpeed = new EntityProperty("switchingSpeed", "Vel. de Conmutación:", EntityProperty.PropertyType.NUMBER, false);
        propSwitchingSpeed.setFirstValue((gridOutPortA.getSwitchingSpeed() == gridOutPortB.getSwitchingSpeed()) ? Double.toString(gridOutPortB.getSwitchingSpeed()) : "Problema leyendo Vel. de conmutación.");
        entityPropertys.add(propSwitchingSpeed);

        EntityProperty propWavelengths = new EntityProperty("defaultWavelengths", "Cantidad de λs:", EntityProperty.PropertyType.NUMBER, false);
        propWavelengths.setFirstValue((gridOutPortA.getMaxNumberOfWavelengths() == gridOutPortB.getMaxNumberOfWavelengths()) ? Integer.toString(gridOutPortB.getMaxNumberOfWavelengths()) : "Problema leyendo el numero de λ.");
        entityPropertys.add(propWavelengths);

        entityPropertyTable.loadProperties(entityPropertys);
    }

    @Override
    public void updatePropiedad(GraphLink graphLink, String id, String valor) {

        graphLink.getProperties().put(id, valor);

        PhosphorusLinkModel selectedPhosphorusLink = (PhosphorusLinkModel) MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer().get(graphLink);

        if (id.equalsIgnoreCase("linkSpeedAB")) {
            selectedPhosphorusLink.getGridOutPortA().setLinkSpeed(Double.valueOf(valor));
        } else if (id.equalsIgnoreCase("linkSpeedBA")) {
            selectedPhosphorusLink.getGridOutPortB().setLinkSpeed(Double.valueOf(valor));
        } else if (id.equalsIgnoreCase("switchingSpeed")) {
            selectedPhosphorusLink.getGridOutPortA().setSwitchingSpeed(Double.valueOf(valor));
            selectedPhosphorusLink.getGridOutPortB().setSwitchingSpeed(Double.valueOf(valor));
        } else if (id.equalsIgnoreCase("defaultWavelengths")) {
            selectedPhosphorusLink.getGridOutPortA().setMaxNumberOfWavelengths(Integer.parseInt(valor));
            selectedPhosphorusLink.getGridOutPortB().setMaxNumberOfWavelengths(Integer.parseInt(valor));
        }
    }

    @Override
    public void reCreatePhosphorousLinks() {

        for (GraphLink graphLink : linkMatchCoupleObjectContainer.keySet()) {
            createLink(graphLink);
        }

        for (GraphLink graphLink : linkMatchCoupleObjectContainer.keySet()) {

            for (String id : graphLink.getProperties().keySet()) {
                updatePropiedad(graphLink, id, graphLink.getProperties().get(id));
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