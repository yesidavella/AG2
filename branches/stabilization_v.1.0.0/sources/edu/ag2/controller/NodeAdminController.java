package edu.ag2.controller;

import Distributions.*;
import Grid.Entity;
import Grid.Interfaces.CPU;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Nodes.AbstractClient;
import Grid.Nodes.AbstractResourceNode;
import Grid.Nodes.AbstractServiceNode;
import Grid.Nodes.PCE;
import edu.ag2.model.BrokerCreationModel;
import edu.ag2.model.ClientCreationModel;
import edu.ag2.model.HybridSwitchCreationModel;
import edu.ag2.model.NodeCreationModel;
import edu.ag2.model.PCE_SwitchCreationModel;
import edu.ag2.model.ResourceCreationModel;
import edu.ag2.model.SimulationBase;
import edu.ag2.presentation.GUI;
import edu.ag2.presentation.GraphNodesView;
import edu.ag2.presentation.Main;
import edu.ag2.presentation.design.BrokerGrahpNode;
import edu.ag2.presentation.design.ClientGraphNode;
import edu.ag2.presentation.design.GraphArc;
import edu.ag2.presentation.design.GraphNode;
import edu.ag2.presentation.design.HybridSwitchGraphNode;
import edu.ag2.presentation.design.PCE_SwicthGraphNode;
import edu.ag2.presentation.design.ResourceGraphNode;
import edu.ag2.presentation.design.SwitchGraphNode;
import edu.ag2.presentation.design.property.EntityProperty;
import edu.ag2.presentation.design.property.NodeDistributionProperty;
import edu.ag2.presentation.design.property.NodeRelationProperty;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import simbase.SimBaseEntity;
import simbase.SimBaseSimulator;

public class NodeAdminController extends NodeAdminAbstractController implements Serializable {

    private GraphNode selectedGraphNode;

    @Override
    public Entity createNode(GraphNode graphNode) {

        Entity newPhosphorusNode = null;

        for (NodeCreationModel nodeCreationModel : nodeCreationModels) {

            if (nodeCreationModel instanceof ClientCreationModel && graphNode instanceof ClientGraphNode) {
                newPhosphorusNode = ((ClientCreationModel) nodeCreationModel).createPhosphorusNode(graphNode.getOriginalName());

            } else if (nodeCreationModel instanceof BrokerCreationModel && graphNode instanceof BrokerGrahpNode) {
                newPhosphorusNode = ((BrokerCreationModel) nodeCreationModel).createPhosphorusNode(graphNode.getOriginalName());

            } else if (nodeCreationModel instanceof ResourceCreationModel && graphNode instanceof ResourceGraphNode) {
                newPhosphorusNode = ((ResourceCreationModel) nodeCreationModel).createPhosphorusNode(graphNode.getOriginalName());

            } else if (nodeCreationModel instanceof PCE_SwitchCreationModel && graphNode instanceof PCE_SwicthGraphNode) {
                newPhosphorusNode = ((PCE_SwitchCreationModel) nodeCreationModel).createPhosphorusNode(graphNode.getOriginalName());

            } else if (nodeCreationModel instanceof HybridSwitchCreationModel && graphNode instanceof HybridSwitchGraphNode) {
                newPhosphorusNode = ((HybridSwitchCreationModel) nodeCreationModel).createPhosphorusNode(graphNode.getOriginalName());
            }

            if (newPhosphorusNode != null) {
                addNodeMatchCouple(graphNode, newPhosphorusNode);
                return newPhosphorusNode;
            }
        }
        return null;
    }

    @Override
    public void queryProperties(GraphNode graphNode) {

        selectedGraphNode = graphNode;
        ArrayList<EntityProperty> nodeProperties = new ArrayList<EntityProperty>();

        //===========================================================================================================
        EntityProperty nodeNamePropperty = new EntityProperty("name", "Nombre:", EntityProperty.PropertyType.TEXT, false);
        nodeNamePropperty.setFirstValue(selectedGraphNode.getName());
        nodeProperties.add(nodeNamePropperty);

        if (graphNode instanceof ClientGraphNode) {

            ClientNode clientNode = (ClientNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(graphNode);
            //===========================================================================================================
            NodeRelationProperty nodeRelationProperty = new NodeRelationProperty("broker", "Agendador:");

            for (GraphNode brokerGrahpNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {
                if (brokerGrahpNode instanceof BrokerGrahpNode) {
                    nodeRelationProperty.getObservableListNodes().add(brokerGrahpNode);
                }
            }

            if (clientNode.getServiceNode() != null) {
                GraphNode brokerSelected = findKeyNodeByValueNode(clientNode.getServiceNode(), MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer());
                if (brokerSelected != null) {
                    nodeRelationProperty.setFirstValue(brokerSelected);
                }
            }
            nodeProperties.add(nodeRelationProperty);
            //===========================================================================================================
            EntityProperty nodeTrafficPriorityProp = new EntityProperty("trafficPriority", "Prioridad Tráfico(1,2,...10)", EntityProperty.PropertyType.NUMBER, false);
            nodeTrafficPriorityProp.setFirstValue(String.valueOf(clientNode.getState().getTrafficPriority()));
            nodeProperties.add(nodeTrafficPriorityProp);
            //===========================================================================================================
            NodeDistributionProperty jobsDistribution = new NodeDistributionProperty("jobsDistribution", "Intervalo de generación de trabajo:(s)");
            createDistributionProperty(clientNode.getState().getJobInterArrival(), nodeProperties, jobsDistribution, "jobsDistribution");
            //===========================================================================================================
            NodeDistributionProperty answerSizeDistribution = new NodeDistributionProperty("answerSizeDistribution", "Tamaño de msg confirmación:(Mb)");
            createDistributionProperty(clientNode.getState().getAckSizeDistribution(), nodeProperties, answerSizeDistribution, "answerSizeDistribution");
            //===========================================================================================================
            NodeDistributionProperty jobSizeDistribution = new NodeDistributionProperty("jobSizeDistribution", "Tamaño de Trabajo:(Mb)");
            createDistributionProperty(clientNode.getState().getSizeDistribution(), nodeProperties, jobSizeDistribution, "jobSizeDistribution");
            //===========================================================================================================
            NodeDistributionProperty flopsDistribution = new NodeDistributionProperty("flopsDistribution", "FLOPS de Trabajo(Mflops):");
            createDistributionProperty(clientNode.getState().getFlops(), nodeProperties, flopsDistribution, "flopsDistribution");
            //===========================================================================================================
//            NodeDistributionProperty maxDelayDistribution = new NodeDistributionProperty("maxDelayDistribution", "Gen. de intervalo máximo de retraso:");
//            createDistributionProperty(clientNode.getState().getMaxDelayInterval(), nodeProperties, maxDelayDistribution, "maxDelayDistribution");

            NodeDistributionProperty resultSizeDistribution = new NodeDistributionProperty("resultSizeDistribution", "Tamaño de Respuesta:(Mb)");
            createDistributionProperty(clientNode.getState().getResultSizeDistribution(), nodeProperties, resultSizeDistribution, "resultSizeDistribution");



        } else if (graphNode instanceof ResourceGraphNode) {

            ResourceNode resource = (ResourceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(graphNode);

            EntityProperty cpuCapacityProperty = new EntityProperty("CpuCapacity", "Capacidad de CPU:(MHz)", EntityProperty.PropertyType.TEXT, false);
            CPU cpu = (CPU) resource.getCpuSet().get(0);

            if (cpu != null) {
                cpuCapacityProperty.setFirstValue(String.valueOf(cpu.getCpuCapacity()));
            } else {
                cpuCapacityProperty.setFirstValue("0");
            }
            nodeProperties.add(cpuCapacityProperty);

            //===========================================================================================================
            EntityProperty cpuCountProperty = new EntityProperty("CpuCount", "Número de CPUs:", EntityProperty.PropertyType.TEXT, false);
            cpuCountProperty.setFirstValue(String.valueOf(resource.getCpuCount()));
            nodeProperties.add(cpuCountProperty);
            //===========================================================================================================
            EntityProperty queueSizeProperty = new EntityProperty("QueueSize", "Tamaño Buffer de Trabajos:", EntityProperty.PropertyType.TEXT, false);
            queueSizeProperty.setFirstValue(String.valueOf(resource.getMaxQueueSize()));
            nodeProperties.add(queueSizeProperty);

            for (GraphNode brokerGrahpNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {

                if (brokerGrahpNode instanceof BrokerGrahpNode) {

                    EntityProperty entityProperty = new EntityProperty("RelationshipResourceAndServiceNodo", brokerGrahpNode.getName(), EntityProperty.PropertyType.BOOLEAN, false);
                    nodeProperties.add(entityProperty);

                    for (ServiceNode serviceNode : resource.getServiceNodes()) {

                        if (serviceNode.getID().equals(brokerGrahpNode.getOriginalName())) {
                            entityProperty.setFirstValue("true");
                            // propiedadeNodo.setDisable(true);
                        }
                    }
                }
            }





            //============================================================================================================

        } else if (graphNode instanceof BrokerGrahpNode) {

            ServiceNode broker = (ServiceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(graphNode);

            NodeRelationProperty nodeRelationProperty = new NodeRelationProperty("relationshipBrokerAndPCE", "PCE:");

            for (GraphNode pce : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {

                if (pce instanceof PCE_SwicthGraphNode) {
                    nodeRelationProperty.getObservableListNodes().add(pce);
                }
            }

            if (broker.getPce() != null) {
                GraphNode pceSelected = findKeyNodeByValueNode(broker.getPce(), MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer());
                if (pceSelected != null) {
                    nodeRelationProperty.setFirstValue(pceSelected);
                }
            }

            nodeProperties.add(nodeRelationProperty);

        }

        for (GraphNodesView graphNodesView : graphNodesViews) {
            graphNodesView.loadProperties(nodeProperties);
        }
    }

    @Override
    public void updateProperty(boolean isSubProperty, boolean query, String id, String value) {

        if (isSubProperty) {
            selectedGraphNode.getSubPropertiesNode().put(id, value);
        } else {
            selectedGraphNode.getNodeProperties().put(id, value);
        }

        if (id.equalsIgnoreCase("name")) {
            selectedGraphNode.setName(value);
            if (selectedGraphNode instanceof ClientGraphNode) {
                GUI.getInstance().getGraphDesignGroup().getClientsObservableList().remove(selectedGraphNode);
                GUI.getInstance().getGraphDesignGroup().getClientsObservableList().add(selectedGraphNode);
            } else if (selectedGraphNode instanceof ResourceGraphNode) {
                GUI.getInstance().getGraphDesignGroup().getResourcesObservableList().remove(selectedGraphNode);
                GUI.getInstance().getGraphDesignGroup().getResourcesObservableList().add(selectedGraphNode);
            } else if (selectedGraphNode instanceof BrokerGrahpNode) {
                GUI.getInstance().getGraphDesignGroup().getBrokersObservableList().remove(selectedGraphNode);
                GUI.getInstance().getGraphDesignGroup().getBrokersObservableList().add(selectedGraphNode);
            } else if (selectedGraphNode instanceof SwitchGraphNode) {
                GUI.getInstance().getGraphDesignGroup().getSwitchesObservableList().remove(selectedGraphNode);
                GUI.getInstance().getGraphDesignGroup().getSwitchesObservableList().add(selectedGraphNode);
            }
        }

        if (selectedGraphNode instanceof ClientGraphNode) {
            ClientNode clientNode = (ClientNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(selectedGraphNode);

            if (id.equalsIgnoreCase("broker")) {
                GraphNode brokerGraphNodeSelected = findNodoGraficoByName(value, MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer());
                if (brokerGraphNodeSelected != null) {
                    clientNode.setServiceNode((ServiceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(brokerGraphNodeSelected));
                }
            } else if (id.equalsIgnoreCase("trafficPriority")) {
                clientNode.getState().setTrafficPriority(Integer.parseInt(value));
            } else if (id.equalsIgnoreCase("jobsDistribution")) {
                clientNode.getState().setJobInterArrival(getDistributionByText(value));
                if (query) {
                    queryProperties(selectedGraphNode);
                }
            } else if (id.contains("jobsDistribution")) {
                setValuesDistribution(clientNode.getState().getJobInterArrival(), value, id);
            } else if (id.equalsIgnoreCase("flopsDistribution")) {
                clientNode.getState().setFlops(getDistributionByText(value));
                if (query) {
                    queryProperties(selectedGraphNode);
                }
            } else if (id.contains("flopsDistribution")) {
                setValuesDistribution(clientNode.getState().getFlops(), value, id);
            } else if (id.equalsIgnoreCase("maxDelayDistribution")) {
                clientNode.getState().setMaxDelayInterval(getDistributionByText(value));
                if (query) {
                    queryProperties(selectedGraphNode);
                }
            } else if (id.contains("maxDelayDistribution")) {
                setValuesDistribution(clientNode.getState().getMaxDelayInterval(), value, id);

            } else if (id.equalsIgnoreCase("jobSizeDistribution")) {
                clientNode.getState().setSizeDistribution(getDistributionByText(value));
                if (query) {
                    queryProperties(selectedGraphNode);
                }

            } else if (id.contains("jobSizeDistribution")) {
                setValuesDistribution(clientNode.getState().getSizeDistribution(), value, id);
            } else if (id.equalsIgnoreCase("answerSizeDistribution")) {
                clientNode.getState().setAckSizeDistribution(getDistributionByText(value));
                if (query) {
                    queryProperties(selectedGraphNode);
                }

            } else if (id.contains("answerSizeDistribution")) {
                setValuesDistribution(clientNode.getState().getAckSizeDistribution(), value, id);
            } else if (id.equalsIgnoreCase("resultSizeDistribution")) {
                clientNode.getState().setResultSizeDistribution(getDistributionByText(value));
                if (query) {
                    queryProperties(selectedGraphNode);
                }
            } else if (id.contains("resultSizeDistribution")) {
                setValuesDistribution(clientNode.getState().getResultSizeDistribution(), value, id);
            }

        } else if (selectedGraphNode instanceof ResourceGraphNode) {
            ResourceNode resource = (ResourceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(selectedGraphNode);

            if (id.equalsIgnoreCase("CpuCapacity")) {
                resource.setCpuCapacity(Double.parseDouble(value));
            } else if (id.equalsIgnoreCase("QueueSize")) {
                resource.setQueueSize(Integer.parseInt(value));
            } else if (id.equalsIgnoreCase("CpuCount")) {
                CPU cpu = (CPU) resource.getCpuSet().get(0);
                double capacity = 0;
                if (cpu != null) {
                    capacity = cpu.getCpuCapacity();
                }
                resource.setCpuCount(Integer.parseInt(value), capacity);
            } else if (id.contains("RelationshipResourceAndServiceNodo")) {
                String serviceNodeName = value.replace("_ON", "").replace("_OFF", "");

                Set<GraphNode> graphNodes = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet();
                GraphNode nodoGrafico = null;
                for (GraphNode nodoGraficoAux : graphNodes) {
                    if (nodoGraficoAux.getName().equals(serviceNodeName)) {
                        nodoGrafico = nodoGraficoAux;
                        break;
                    }
                }
                Entity entity = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(nodoGrafico);
                if (value.contains("_ON")) {
                    if (entity != null && entity instanceof ServiceNode) {
                        ServiceNode serviceNode = (ServiceNode) entity;
                        resource.addServiceNode(serviceNode);
                    }

                } else if (value.contains("_OFF")) {
                    if (entity != null && entity instanceof ServiceNode) {
                        ServiceNode serviceNode = (ServiceNode) entity;
                        resource.removeServiceNode(serviceNode);
                    }
                }
            }




        } else if (selectedGraphNode instanceof BrokerGrahpNode) {

            if (id.equalsIgnoreCase("relationshipBrokerAndPCE")) {

                ServiceNode broker = (ServiceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(selectedGraphNode);

                GraphNode pceGraphNodeSelected = findNodoGraficoByName(value, MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer());
                if (pceGraphNodeSelected != null) {
                    broker.setPce((PCE) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(pceGraphNodeSelected));
                }
            }
        }
//        else if (selectedGraphNode instanceof SwitchGraphNode) {
//            AbstractSwitch abstractSwitch = (AbstractSwitch) nodeMatchCoupleObjectContainer.get(selectedGraphNode);
//            if (id.equalsIgnoreCase("HandleDelay")) {
//                abstractSwitch.setHandleDelay(new Time(Double.parseDouble(value)));
//            }
//        }
    }

    private void createDistributionProperty(DiscreteDistribution discreteDistribution, ArrayList<EntityProperty> nodeProperties, NodeDistributionProperty nodeDistributionProperty, String id) {

        nodeProperties.add(nodeDistributionProperty);

        if (discreteDistribution instanceof DDErlang) {

            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.ER_LANG);
            DDErlang dDErlang = (DDErlang) discreteDistribution;

            EntityProperty propertyA = new EntityProperty(id + "_DDErlang_Orden", "orden", EntityProperty.PropertyType.NUMBER, true);
            propertyA.setFirstValue(String.valueOf(dDErlang.getN()));
            nodeProperties.add(propertyA);

            EntityProperty propertyB = new EntityProperty(id + "_DDErlang_Promedio", "promedio", EntityProperty.PropertyType.NUMBER, true);
            propertyB.setFirstValue(String.valueOf(dDErlang.getAvg()));
            nodeProperties.add(propertyB);

        } else if (discreteDistribution instanceof DDHyperExp) {
            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.HYPER_EXPONENTIAL);
            DDHyperExp dDHyperExp = (DDHyperExp) discreteDistribution;

            EntityProperty propertyA = new EntityProperty(id + "_DDHyperExp_Lambdas", "lambdas", EntityProperty.PropertyType.TEXT, true);
            propertyA.setFirstValue(getStringArrayDoubles(dDHyperExp.getLambdas()));
            nodeProperties.add(propertyA);

            EntityProperty propertyB = new EntityProperty(id + "_DDHyperExp_Oportunidades", "oportunidades", EntityProperty.PropertyType.TEXT, true);
            propertyB.setFirstValue(getStringArrayDoubles(dDHyperExp.getChances()));
            nodeProperties.add(propertyB);

        } else if (discreteDistribution instanceof DDNegExp) {
            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.NEGATIVE_EXPONENTIAL);
            DDNegExp dDNegExp = (DDNegExp) discreteDistribution;
            EntityProperty propertyA = new EntityProperty(id + "_DDNegExp_Promedio", "promedio", EntityProperty.PropertyType.NUMBER, true);
            propertyA.setFirstValue(String.valueOf(dDNegExp.getAvg()));
            nodeProperties.add(propertyA);

        } else if (discreteDistribution instanceof DDNormal) {
            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.NORMAL);
            DDNormal dDNormal = (DDNormal) discreteDistribution;

            EntityProperty propertyA = new EntityProperty(id + "_DDNormal_DesviacionEstandar", "desviación estándar", EntityProperty.PropertyType.NUMBER, true);
            propertyA.setFirstValue(String.valueOf(dDNormal.getDev()));
            nodeProperties.add(propertyA);

            EntityProperty propertyB = new EntityProperty(id + "_DDNormal_Promedio", "promedio", EntityProperty.PropertyType.NUMBER, true);
            propertyB.setFirstValue(String.valueOf(dDNormal.getAvg()));
            nodeProperties.add(propertyB);

        } else if (discreteDistribution instanceof DDPoissonProcess) {

            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.POISSON_PROCESS);
            DDPoissonProcess dDPoissonProcess = (DDPoissonProcess) discreteDistribution;
            EntityProperty propertyA = new EntityProperty(id + "_DDPoissonProcess_Promedio", "promedio", EntityProperty.PropertyType.NUMBER, true);
            propertyA.setFirstValue(String.valueOf(dDPoissonProcess.getAverage()));
            nodeProperties.add(propertyA);

        } else if (discreteDistribution instanceof DDUniform) {
            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.UNMIFORM);
            DDUniform dDUniform = (DDUniform) discreteDistribution;

            EntityProperty propertyA = new EntityProperty(id + "_DDUniform_Minimo", "mínimo", EntityProperty.PropertyType.NUMBER, true);
            propertyA.setFirstValue(String.valueOf(dDUniform.getMin()));
            nodeProperties.add(propertyA);

            EntityProperty propertyB = new EntityProperty(id + "_DDUniform_Maximo", "máximo", EntityProperty.PropertyType.NUMBER, true);
            propertyB.setFirstValue(String.valueOf(dDUniform.getMax()));
            nodeProperties.add(propertyB);

        } else if (discreteDistribution instanceof ConstantDistribution) {
            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.CONSTANT);
            ConstantDistribution constantDistribution = (ConstantDistribution) discreteDistribution;

            EntityProperty propertyA = new EntityProperty(id + "_ConstantDistribution_Constante", "constante", EntityProperty.PropertyType.NUMBER, true);
            propertyA.setFirstValue(String.valueOf(constantDistribution.getConstant()));
            nodeProperties.add(propertyA);
        }
    }

    private String getStringArrayDoubles(double[] values) {
        StringBuilder result = new StringBuilder();
        String separator = "";
        for (int i = 0; i < values.length; i++) {
            result.append(separator + values[i]);
            separator = "-";
        }
        return result.toString();
    }

    private void setValuesDistribution(DiscreteDistribution distribution, String value, String id) {
        try {

            if (id.contains("DDErlang")) {

                DDErlang dDErlang = (DDErlang) distribution;

                if (id.contains("Orden")) {
                    dDErlang.setN(Integer.parseInt(value));
                } else if (id.contains("Promedio")) {
                    dDErlang.setAvg(Double.parseDouble(value));
                }
            } else if (id.contains("DDHyperExp")) {
                DDHyperExp dDHyperExp = (DDHyperExp) distribution;

                String[] values = value.split("-");
                double valuesDoubles[] = new double[values.length];
                for (int i = 0; i < values.length; i++) {
                    valuesDoubles[i] = Double.parseDouble(values[i]);
                }

                if (id.contains("Lambdas")) {
                    dDHyperExp.setLambdas(valuesDoubles);

                } else if (id.contains("Oportunidades")) {
                    dDHyperExp.setChances(valuesDoubles);
                }

            } else if (id.contains("DDNegExp")) {
                DDNegExp dDNegExp = (DDNegExp) distribution;

                if (id.contains("Promedio")) {
                    dDNegExp.setAvg(Double.parseDouble(value));
                }

            } else if (id.contains("DDNormal")) {
                DDNormal dDNormal = (DDNormal) distribution;

                if (id.contains("Promedio")) {
                    dDNormal.setAvg(Double.parseDouble(value));
                } else if (id.contains("DesviacionEstandar")) {
                    dDNormal.setDev(Double.parseDouble(value));
                }

            } else if (id.contains("DDPoissonProcess")) {
                DDPoissonProcess dDPoissonProcess = (DDPoissonProcess) distribution;
                if (id.contains("Promedio")) {
                    dDPoissonProcess.setAverage(Double.parseDouble(value));
                }
            } else if (id.contains("DDUniform")) {
                DDUniform dDUniform = (DDUniform) distribution;

                if (id.contains("Mínimo")) {
                    dDUniform.setMin(Double.parseDouble(value));
                } else if (id.contains("Máximo")) {
                    dDUniform.setMax(Double.parseDouble(value));
                }

            } else if (id.contains("ConstantDistribution")) {
                ConstantDistribution constantDistribution = (ConstantDistribution) distribution;

                if (id.contains("Constante")) {
                    constantDistribution.setConstant(Double.parseDouble(value));
                }
            }
            return ;

        } catch (ClassCastException exception) {
            return ;
        } catch (NumberFormatException formatException) {
        }

    }

    private DiscreteDistribution getDistributionByText(String value) {
        SimBaseSimulator simBaseSimulator = SimulationBase.getInstance().getGridSimulatorModel();
        if (value.equals("Uniforme")) {
            return new DDUniform(simBaseSimulator, 10, 20);
        } else if (value.equals("Constante")) {
            return new ConstantDistribution(10);
        } else if (value.equals("Erlang")) {
            return new DDErlang(simBaseSimulator, 1, 2);
        } else if (value.equals("Hiper-exponencial")) {
            return new DDHyperExp(simBaseSimulator, new double[]{1, 2, 3}, new double[]{1, 2, 3});
        } else if (value.equals("Exponencial-negativa")) {
            return new DDNegExp(simBaseSimulator, 2);
        } else if (value.equals("Normal")) {
            return new DDNormal(simBaseSimulator, 5, 2);
        } else if (value.equals("Possion")) {
            return new DDPoissonProcess(simBaseSimulator, 2);
        }
        return null;

    }

    public GraphNode findKeyNodeByValueNode(Entity entity, HashMap<GraphNode, Entity> hashMap) {
        for (GraphNode nodoGrafico : hashMap.keySet()) {
            if (hashMap.get(nodoGrafico) == entity) {
                return nodoGrafico;
            }
        }
        return null;
    }

    public GraphNode findNodoGraficoByName(String name, HashMap<GraphNode, Entity> hashMap) {
        for (GraphNode nodoGrafico : hashMap.keySet()) {
            if (nodoGrafico.getName().equals(name)) {
                return nodoGrafico;
            }
        }
        return null;
    }

    @Override
    public void removeNode(GraphNode graphNode) {

        Entity phosNodeRemoved = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().remove(graphNode);
        SimulationBase.getInstance().getGridSimulatorModel().unRegister(phosNodeRemoved);

        if (graphNode instanceof ResourceGraphNode) {

            List<ServiceNode> serviceNodes = ((ResourceNode) phosNodeRemoved).getServiceNodes();

            for (ServiceNode serviceNode : serviceNodes) {
                ((AbstractServiceNode) serviceNode).getResources().remove((ResourceNode) phosNodeRemoved);
            }
        } else if (graphNode instanceof BrokerGrahpNode) {

            //#######Elimino las referencias a los brokers q puedan tener los clusters############
            ArrayList<ResourceNode> resourcesWithBrokersRemoved = new ArrayList<ResourceNode>();
            List<SimBaseEntity> resources = SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(ResourceNode.class);

            for (SimBaseEntity resourceNode : resources) {

                List<ServiceNode> serviceNodes = ((ResourceNode) resourceNode).getServiceNodes();

                for (ServiceNode serviceNode : serviceNodes) {
                    if (phosNodeRemoved.getId().equalsIgnoreCase(serviceNode.getID())) {
                        resourcesWithBrokersRemoved.add((ResourceNode) resourceNode);
                    }
                }
            }

            for (ResourceNode resourcesWithBrokerRemoved : resourcesWithBrokersRemoved) {
                resourcesWithBrokerRemoved.getServiceNodes().remove((ServiceNode) phosNodeRemoved);
            }

            //#######Elimino las referencias a los brokers q puedan tener los clientes#######
            for (SimBaseEntity clientNode : SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(ClientNode.class)) {
                ServiceNode serviceNode = ((AbstractClient) clientNode).getServiceNode();

                if (serviceNode != null && phosNodeRemoved.getId().equalsIgnoreCase(serviceNode.getID())) {
                    ((AbstractClient) clientNode).setServiceNode(null);
                }
            }
        } else if (graphNode instanceof PCE_SwicthGraphNode) {

            for (SimBaseEntity brokerNode : SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(ServiceNode.class)) {

                PCE pceRegisteredOnBroker = ((ServiceNode) brokerNode).getPce();

                if (pceRegisteredOnBroker != null && phosNodeRemoved.getId().equalsIgnoreCase(pceRegisteredOnBroker.getId())) {
                    ((ServiceNode) brokerNode).setPce(null);
                }
            }
        }

        GUI.getInstance().getEntityPropertyTable().clearData();
        if (GUI.getInstance().getGraphDesignGroup().getSelectable() != null) {
            GUI.getInstance().getGraphDesignGroup().getSelectable().select(false);
        }
    }

    @Override
    public void reCreatePhosphorousNodes() {

        for (NodeCreationModel nodeCreationModel : nodeCreationModels) {
            nodeCreationModel.loadSimulacionBase();
        }

        for (GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {
            createNode(graphNode);
        }

        for (final GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (String id : graphNode.getNodeProperties().keySet()) {
                        selectedGraphNode = graphNode;
                        updateProperty(false, false, id, graphNode.getNodeProperties().get(id));
                    }
                    for (String id : graphNode.getSubPropertiesNode().keySet()) {
                        selectedGraphNode = graphNode;
                        updateProperty(true, false, id, graphNode.getSubPropertiesNode().get(id));
                    }
                }
            };
            Platform.runLater(runnable);
        }

        for (final GraphNodesView graphNodesView : graphNodesViews) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    graphNodesView.enableDisign();
                }
            };
            Platform.runLater(runnable);
        }
    }

    private void writeObject(ObjectOutputStream stream) {
        try {
            stream.defaultWriteObject();
            Main.countObject++;
            //System.out.println("Writing: " + Main.countObject + "  " + this.getClass().getCanonicalName());
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}