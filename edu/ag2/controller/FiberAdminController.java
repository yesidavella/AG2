package edu.ag2.controller;

import Grid.Entity;
import Grid.GridSimulator;
import Grid.Port.GridOutPort;
import edu.ag2.model.FiberCreationModel;
import edu.ag2.model.LinkCreationAbstractModel;
import edu.ag2.model.PhosphorusLinkModel;
import edu.ag2.model.SimulationBase;
import edu.ag2.presentation.GUI;
import edu.ag2.presentation.design.ClientGraphNode;
import edu.ag2.presentation.design.GraphLink;
import edu.ag2.presentation.design.GraphNode;
import edu.ag2.presentation.design.SwitchGraphNode;
import edu.ag2.presentation.design.property.EntityProperty;
import java.util.ArrayList;
import java.util.HashMap;
import simbase.Port.SimBaseInPort;
import simbase.SimBaseEntity;

public class FiberAdminController extends LinkAdminAbstractController {

    private LinkAdminAbstractController ocsAdminCtl;

    @Override
    public boolean createLink(GraphNode sourceGraphNode, GraphNode destinationGraphNode) {

        Entity phosphorusNodeA = (Entity) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(sourceGraphNode);
        Entity phosphorusNodeB = (Entity) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(destinationGraphNode);

        for (LinkCreationAbstractModel linkCreationModel : linkCreationAbstractModels) {

            if (linkCreationModel instanceof FiberCreationModel) {

                GraphLink graphLink = new GraphLink(GUI.getInstance().getGraphDesignGroup(), sourceGraphNode, destinationGraphNode, this);
                PhosphorusLinkModel phosphorusLinkModel = (PhosphorusLinkModel) linkCreationModel.createLink(phosphorusNodeA, phosphorusNodeB);

                MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer().put(graphLink, phosphorusLinkModel);

                graphLink.addInitialGraphArc();
                graphLink.select(true);

                //Creo los ocs por default en ambas direcciones entre enrutadores
                if (sourceGraphNode instanceof SwitchGraphNode && destinationGraphNode instanceof SwitchGraphNode) {

                    ocsAdminCtl.createLink(sourceGraphNode, destinationGraphNode);
                    ocsAdminCtl.createLink(destinationGraphNode, sourceGraphNode);
                }

                return (graphLink != null && phosphorusLinkModel != null) ? true : false;
            }
        }

        return false;
    }

    @Override
    public void queryProperty(GraphLink graphLink) {

        ArrayList<EntityProperty> entityPropertys = new ArrayList<EntityProperty>();
        PhosphorusLinkModel phosphorusSelectedLinkModel = (PhosphorusLinkModel) MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer().get(graphLink);
        GridOutPort gridOutPortA = phosphorusSelectedLinkModel.getGridOutPortA();
        GridOutPort gridOutPortB = phosphorusSelectedLinkModel.getGridOutPortB();

        /*
         **Propiedades comunes en ambas direcciones del canal.
         */
        EntityProperty wavelengthsProperty = new EntityProperty("defaultWavelengths", "Número de lambdas:", EntityProperty.PropertyType.NUMBER, false);
        wavelengthsProperty.setFirstValue((gridOutPortA.getMaxNumberOfWavelengths() == gridOutPortB.getMaxNumberOfWavelengths()) ? Integer.toString(gridOutPortB.getMaxNumberOfWavelengths()) : "Problema leyendo el numero de λ.");
        entityPropertys.add(wavelengthsProperty);
        
        EntityProperty switchingSpeedProperty = new EntityProperty("switchingSpeed", "Velocidad de conmutación:(Mbps)", EntityProperty.PropertyType.NUMBER, false);
        switchingSpeedProperty.setFirstValue((gridOutPortA.getSwitchingSpeed() == gridOutPortB.getSwitchingSpeed()) ? Double.toString(gridOutPortB.getSwitchingSpeed()) : "Problema leyendo Vel. de conmutación.");
        entityPropertys.add(switchingSpeedProperty);

        /*
         **Enlace de nodo Phosphorous de A hacia B (A->B)
         */
        //===========================================================================================================
        EntityProperty linkAB_NameProperty = new EntityProperty("channelDirectionAB", "Dirección del Canal:", EntityProperty.PropertyType.LABEL, false);
        linkAB_NameProperty.setFirstValue(graphLink.getGraphNodeA().getName() + "-->" + graphLink.getGraphNodeB().getName());
        entityPropertys.add(linkAB_NameProperty);

        EntityProperty linkABSpeedProperty = new EntityProperty("linkSpeedAB", "Velocidad del enlace:(Mbps)", EntityProperty.PropertyType.NUMBER, false);
        linkABSpeedProperty.setFirstValue(Double.toString(gridOutPortA.getLinkSpeed()));
        entityPropertys.add(linkABSpeedProperty);
        //===========================================================================================================

        /*
         **Enlace de nodo Phosphorous de B hacia A (B->A)
         */
        //===========================================================================================================
        EntityProperty linkBA_NameProperty = new EntityProperty("channelDirectionBA", "Dirección del Canal:", EntityProperty.PropertyType.LABEL, false);
        linkBA_NameProperty.setFirstValue(graphLink.getGraphNodeB().getName() + "-->" + graphLink.getGraphNodeA().getName());
        entityPropertys.add(linkBA_NameProperty);

        EntityProperty linkBASpeedProperty = new EntityProperty("linkSpeedBA", "Velocidad del enlace:(Mbps)", EntityProperty.PropertyType.NUMBER, false);
        linkBASpeedProperty.setFirstValue(Double.toString(gridOutPortB.getLinkSpeed()));
        entityPropertys.add(linkBASpeedProperty);
        //===========================================================================================================

        entityPropertyTable.loadProperties(entityPropertys);
    }

    @Override
    public void updatePropiedad(GraphLink graphLink, String id, String value) {

        graphLink.getProperties().put(id, value);

        PhosphorusLinkModel phosphorusLinkModelSelected = (PhosphorusLinkModel) MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer().get(graphLink);

        if (id.equalsIgnoreCase("linkSpeedAB")) {
            phosphorusLinkModelSelected.getGridOutPortA().setLinkSpeed(Double.valueOf(value));
        } else if (id.equalsIgnoreCase("linkSpeedBA")) {
            phosphorusLinkModelSelected.getGridOutPortB().setLinkSpeed(Double.valueOf(value));
        } else if (id.equalsIgnoreCase("switchingSpeed")) {
            phosphorusLinkModelSelected.getGridOutPortA().setSwitchingSpeed(Double.valueOf(value));
            phosphorusLinkModelSelected.getGridOutPortB().setSwitchingSpeed(Double.valueOf(value));
        } else if (id.equalsIgnoreCase("defaultWavelengths")) {
            phosphorusLinkModelSelected.getGridOutPortA().setMaxNumberOfWavelengths(Integer.parseInt(value));
            phosphorusLinkModelSelected.getGridOutPortB().setMaxNumberOfWavelengths(Integer.parseInt(value));
        }
    }

    @Override
    public void reCreatePhosphorousLinks() {

        HashMap<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer();

        for (GraphLink graphLink : linkMatchCoupleObjectContainer.keySet()) {
//            createLink(graphLink.getGraphNodeA(), graphLink.getGraphNodeB());

            GraphNode sourceGraphNode = graphLink.getGraphNodeA();
            GraphNode destinationGraphNode = graphLink.getGraphNodeB();

            Entity phosphorusNodeA = (Entity) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(sourceGraphNode);
            Entity phosphorusNodeB = (Entity) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(destinationGraphNode);

            PhosphorusLinkModel phosphorusLinkModel = null;

            for (LinkCreationAbstractModel linkCreationModel : linkCreationAbstractModels) {

                if (linkCreationModel instanceof FiberCreationModel) {
                    phosphorusLinkModel = (PhosphorusLinkModel) linkCreationModel.createLink(phosphorusNodeA, phosphorusNodeB);
                }
            }

            MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer().put(graphLink, phosphorusLinkModel);

            //Creo los ocs por default en ambas direcciones entre enrutadores
            if (sourceGraphNode instanceof SwitchGraphNode
                    && destinationGraphNode instanceof SwitchGraphNode) {

                ocsAdminCtl.createLink(sourceGraphNode, destinationGraphNode);
                ocsAdminCtl.createLink(destinationGraphNode, sourceGraphNode);
            }

            for (String id : graphLink.getProperties().keySet()) {
                updatePropiedad(graphLink, id, graphLink.getProperties().get(id));
            }
        }

    }

    @Override
    public boolean removeLink(GraphLink graphLink) {
//        //System.out.print("Elimino enlace");
        HashMap<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer();
        PhosphorusLinkModel phosLink = linkMatchCoupleObjectContainer.remove(graphLink);

        boolean canRemovePortsInNodeA = removeInAndOutPort(phosLink.getGridOutPortA());
        boolean canRemovePortsInNodeB = removeInAndOutPort(phosLink.getGridOutPortB());

        //Elimino los circuitos OCSs
        ocsAdminCtl.removeLink(graphLink);

        return (canRemovePortsInNodeA && canRemovePortsInNodeB);
    }

    private boolean removeInAndOutPort(GridOutPort outPort) {
        //Remuevo el puerto de salida y de entrada
        SimBaseInPort inPort = outPort.getTarget();

        GridSimulator gridSimulatorModel = SimulationBase.getInstance().getGridSimulatorModel();

        SimBaseEntity source = gridSimulatorModel.getEntityWithId(outPort.getOwner().getId());
        SimBaseEntity target = gridSimulatorModel.getEntityWithId(inPort.getOwner().getId());

        boolean canRemoveOutPort = false;
        boolean canRemoveInPort = false;

        if (source != null) {
            canRemoveOutPort = source.getOutPorts().remove(outPort);
        }

        if (target != null) {
            canRemoveInPort = target.getInPorts().remove(inPort);
        }

        return canRemoveInPort && canRemoveOutPort;
    }

    @Override
    public boolean canCreateLink(GraphNode sourceGraphNode, GraphNode targetGraphNode) {

        HashMap<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer();

        for (GraphLink graphLink : linkMatchCoupleObjectContainer.keySet()) {

            GraphNode nodeInLinkA = graphLink.getGraphNodeA();
            GraphNode nodeInLinkB = graphLink.getGraphNodeB();

            //Verifico q no haya un enlace entre este par de nodos
            if ((sourceGraphNode.equals(nodeInLinkA) || sourceGraphNode.equals(nodeInLinkB))
                    && (targetGraphNode.equals(nodeInLinkA) || targetGraphNode.equals(nodeInLinkB))) {
                return false;
            }

            //Verifico q los nodos clientes no puedan tener mas de un enlace
            boolean isInPreviousLinkSourceNode = clientNodeExistInPreviousLink(sourceGraphNode, nodeInLinkA, nodeInLinkB);
            boolean isInPreviousLinkTargetNode = clientNodeExistInPreviousLink(targetGraphNode, nodeInLinkA, nodeInLinkB);

            if (isInPreviousLinkSourceNode || isInPreviousLinkTargetNode) {
                return false;
            }
        }

        return true;
    }

    private boolean clientNodeExistInPreviousLink(GraphNode graphNodeToCheck, GraphNode nodeInLinkA, GraphNode nodeInLinkB) {

        if (graphNodeToCheck instanceof ClientGraphNode) {

            if (graphNodeToCheck.equals(nodeInLinkA) || graphNodeToCheck.equals(nodeInLinkB)) {
                return true;
            }
        }

        return false;
    }

    public void addOCSAdminController(OCSAdminController ocsAdminCtl) {
        this.ocsAdminCtl = ocsAdminCtl;
    }
}
