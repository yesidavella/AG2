package com.ag2.controller;

import Distributions.*;
import Grid.Entity;
import Grid.Interfaces.CPU;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Interfaces.Switches.AbstractSwitch;
import com.ag2.model.*;
import com.ag2.presentation.GUI;
import com.ag2.presentation.GraphNodesView;
import com.ag2.presentation.design.*;
import com.ag2.presentation.design.property.EntityProperty;
import com.ag2.presentation.design.property.NodeDistributionProperty;
import com.ag2.presentation.design.property.NodeRelationProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import simbase.SimBaseSimulator;
import simbase.Time;

public class NodeAdminController extends NodeAdminAbstractController implements Serializable {

    private GraphNode selectedGraphNode;

    @Override
    public Entity createNode(GraphNode graphNode) {

        Entity newPhosphorusNode = null;
        for (NodeCreationModel modeloRegistrado : nodeCreationModels) {

            if (modeloRegistrado instanceof ClientCreationModel && graphNode instanceof ClientGraphNode) {
                newPhosphorusNode = ((ClientCreationModel) modeloRegistrado).createPhosphorusNode(graphNode.getName());
            } else if (modeloRegistrado instanceof BrokerCreationModel && graphNode instanceof BrokerGrahpNode) {
                newPhosphorusNode = ((BrokerCreationModel) modeloRegistrado).createPhosphorusNode(graphNode.getName());
            } else if (modeloRegistrado instanceof ModeloCrearNodoDeRecurso && graphNode instanceof ResourceGraphNode) {
                newPhosphorusNode = ((ModeloCrearNodoDeRecurso) modeloRegistrado).createPhosphorusNode(graphNode.getName());
            } else if (modeloRegistrado instanceof PCE_SwitchCreationModel && graphNode instanceof PCE_SwicthGraphNode) {
                newPhosphorusNode = ((PCE_SwitchCreationModel) modeloRegistrado).createPhosphorusNode(graphNode.getName());
            } else if (modeloRegistrado instanceof HybridSwitchCreationModel && graphNode instanceof HybridSwitchGraphNode) {
                newPhosphorusNode = ((HybridSwitchCreationModel) modeloRegistrado).createPhosphorusNode(graphNode.getName());
            }
            
            if (newPhosphorusNode != null) {
                
                addNodeMatchCouple(graphNode, newPhosphorusNode);
                
            }
        }
        return newPhosphorusNode;
    }

    public void queryProperties(GraphNode graphNode) {

        selectedGraphNode = graphNode;
        ArrayList<EntityProperty> nodeProperties = new ArrayList<EntityProperty>();

        //===========================================================================================================
        EntityProperty propNodeName = new EntityProperty("name", "Nombre:", EntityProperty.PropertyType.TEXT, false);
        propNodeName.setFirstValue(selectedGraphNode.getName());
        nodeProperties.add(propNodeName);
        //===========================================================================================================

        if (graphNode instanceof ClientGraphNode) {

            ClientNode clientNode = (ClientNode) nodeMatchCoupleObjectContainer.get(graphNode);

            //===========================================================================================================
            NodeRelationProperty nodeRelationProperty = new NodeRelationProperty("broker", "Agendador:");

            for (GraphNode brokerGrahpNode : nodeMatchCoupleObjectContainer.keySet()) {
                if (brokerGrahpNode instanceof BrokerGrahpNode) {
                    nodeRelationProperty.getObservableListNodes().add(brokerGrahpNode);
                }
            }
            if (clientNode.getServiceNode() != null) {
                GraphNode nodoServiceSelected = findKeyNodeByValueNode(clientNode.getServiceNode(), nodeMatchCoupleObjectContainer);
                if (nodoServiceSelected != null) {
                    nodeRelationProperty.setFirstValue(nodoServiceSelected);
                }
            }
            nodeProperties.add(nodeRelationProperty);

            //===========================================================================================================
            NodeDistributionProperty jobsDistribution = new NodeDistributionProperty("jobsDistribution", "Generación de trabajos:");
            createDistributionProperty(clientNode.getState().getJobInterArrival(), nodeProperties, jobsDistribution, "jobsDistribution");
            //===========================================================================================================
            NodeDistributionProperty flopsDistribution = new NodeDistributionProperty("flopsDistribution", "Generación de flops por trabajo:");
            createDistributionProperty(clientNode.getState().getFlops(), nodeProperties, flopsDistribution, "flopsDistribution");

            //===========================================================================================================
            NodeDistributionProperty maxDelayDistribution = new NodeDistributionProperty("maxDelayDistribution", "Generación de intervalo máximo de retraso:");
            createDistributionProperty(clientNode.getState().getMaxDelayInterval(), nodeProperties, maxDelayDistribution, "maxDelayDistribution");

            //===========================================================================================================
            NodeDistributionProperty jobSizeDistribution = new NodeDistributionProperty("jobSizeDistribution", "Generación del tamaño del trabajo:");
            createDistributionProperty(clientNode.getState().getSizeDistribution(), nodeProperties, jobSizeDistribution, "jobSizeDistribution");

            //===========================================================================================================
            NodeDistributionProperty answerSizeDistribution = new NodeDistributionProperty("answerSizeDistribution", "Generación del tamaño de la respuesta:");
            createDistributionProperty(clientNode.getState().getAckSizeDistribution(), nodeProperties, answerSizeDistribution, "answerSizeDistribution");


        } else if (graphNode instanceof ResourceGraphNode) {
            ResourceNode resource = (ResourceNode) nodeMatchCoupleObjectContainer.get(graphNode);


            EntityProperty propCpuCapacity = new EntityProperty("CpuCapacity", "Capacidad de cada CPU:", EntityProperty.PropertyType.TEXT, false);
            CPU cpu = (CPU) resource.getCpuSet().get(0);

            if (cpu != null) {

                propCpuCapacity.setFirstValue(String.valueOf(cpu.getCpuCapacity()));
            } else {
                propCpuCapacity.setFirstValue("0");
            }
            nodeProperties.add(propCpuCapacity);

            //===========================================================================================================
            EntityProperty propertyCpuCount = new EntityProperty("CpuCount", "Cantidad de CPUs:", EntityProperty.PropertyType.TEXT, false);
            propertyCpuCount.setFirstValue(String.valueOf(resource.getCpuCount()));
            nodeProperties.add(propertyCpuCount);
            //============================================================================================================

            //===========================================================================================================
            EntityProperty propertyQueueSize = new EntityProperty("QueueSize", "Tamaño de Buffer:", EntityProperty.PropertyType.TEXT, false);
            propertyQueueSize.setFirstValue(String.valueOf(resource.getMaxQueueSize()));
            nodeProperties.add(propertyQueueSize);

            for (GraphNode brokerGrahpNode : nodeMatchCoupleObjectContainer.keySet()) {

                if (brokerGrahpNode instanceof BrokerGrahpNode) {
                    EntityProperty propiedadeNodo = new EntityProperty("RelationshipResouceAndServiceNodo", brokerGrahpNode.getName(), EntityProperty.PropertyType.BOOLEAN, false);
                    nodeProperties.add(propiedadeNodo);
                    for (ServiceNode serviceNode : resource.getServiceNodes()) {
                        if (serviceNode.getID().equals(brokerGrahpNode.getOriginalName())) {
                            propiedadeNodo.setFirstValue("true");
                            // propiedadeNodo.setDisable(true);
                        }
                    }
                }
            }

            //============================================================================================================

        } 
//        else if (graphNode instanceof SwitchGraphNode) {
//            AbstractSwitch abstractSwitch = (AbstractSwitch) nodeMatchCoupleObjectContainer.get(graphNode);
//
//            //===========================================================================================================
//            EntityProperty propertyHandleDelay = new EntityProperty("HandleDelay", "Retraso admitido:", EntityProperty.PropertyType.TEXT, false);
//            propertyHandleDelay.setFirstValue(String.valueOf(abstractSwitch.getHandleDelay().getTime()));
//            nodeProperties.add(propertyHandleDelay);
//        }

        for (GraphNodesView vistaNodosGraficos : graphNodesViews) {
            vistaNodosGraficos.loadProperties(nodeProperties);
        }
    }

    private void createDistributionProperty(DiscreteDistribution discreteDistribution, ArrayList<EntityProperty> nodeProperties, NodeDistributionProperty nodeDistributionProperty, String id) {

        nodeProperties.add(nodeDistributionProperty);

        if (discreteDistribution instanceof DDErlang) {

            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.ER_LANG);
            DDErlang dDErlang = (DDErlang) discreteDistribution;

            EntityProperty propertyA = new EntityProperty(id + "_DDErlang_Orden", "Orden", EntityProperty.PropertyType.NUMBER, true);
            propertyA.setFirstValue(String.valueOf(dDErlang.getN()));
            nodeProperties.add(propertyA);

            EntityProperty propertyB = new EntityProperty(id + "_DDErlang_Promedio", "Promedio", EntityProperty.PropertyType.NUMBER, true);
            propertyB.setFirstValue(String.valueOf(dDErlang.getAvg()));
            nodeProperties.add(propertyB);

        } else if (discreteDistribution instanceof DDHyperExp) {
            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.HYPER_EXPONENTIAL);
            DDHyperExp dDHyperExp = (DDHyperExp) discreteDistribution;

            EntityProperty propiedaA = new EntityProperty(id + "_DDHyperExp_Lambdas", "Lambdas", EntityProperty.PropertyType.TEXT, true);
            propiedaA.setFirstValue(getStringArrayDoubles(dDHyperExp.getLambdas()));
            nodeProperties.add(propiedaA);

            EntityProperty propiedaB = new EntityProperty(id + "_DDHyperExp_Oportunidades", "Oportunidades", EntityProperty.PropertyType.TEXT, true);
            propiedaB.setFirstValue(getStringArrayDoubles(dDHyperExp.getChances()));
            nodeProperties.add(propiedaB);

        } else if (discreteDistribution instanceof DDNegExp) {
            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.NEGATIVE_EXPONENTIAL);
            DDNegExp dDNegExp = (DDNegExp) discreteDistribution;
            EntityProperty propiedaB = new EntityProperty(id + "_DDNegExp_Promedio", "Promedio", EntityProperty.PropertyType.NUMBER, true);
            propiedaB.setFirstValue(String.valueOf(dDNegExp.getAvg()));
            nodeProperties.add(propiedaB);

        } else if (discreteDistribution instanceof DDNormal) {
            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.NORMAL);
            DDNormal dDNormal = (DDNormal) discreteDistribution;

            EntityProperty propiedaA = new EntityProperty(id + "_DDNormal_DesviacionEstandar", "Desviación estándar", EntityProperty.PropertyType.NUMBER, true);
            propiedaA.setFirstValue(String.valueOf(dDNormal.getDev()));
            nodeProperties.add(propiedaA);

            EntityProperty propiedaB = new EntityProperty(id + "_DDNormal_Promedio", "Promedio", EntityProperty.PropertyType.NUMBER, true);
            propiedaB.setFirstValue(String.valueOf(dDNormal.getAvg()));
            nodeProperties.add(propiedaB);

        } else if (discreteDistribution instanceof DDPoissonProcess) {

            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.POISSON_PROCESS);
            DDPoissonProcess dDPoissonProcess = (DDPoissonProcess) discreteDistribution;
            EntityProperty propiedaB = new EntityProperty(id + "_DDPoissonProcess_Promedio", "Promedio", EntityProperty.PropertyType.NUMBER, true);
            propiedaB.setFirstValue(String.valueOf(dDPoissonProcess.getAverage()));
            nodeProperties.add(propiedaB);

        } else if (discreteDistribution instanceof DDUniform) {
            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.UNMIFORM);
            DDUniform dDUniform = (DDUniform) discreteDistribution;

            EntityProperty propiedaA = new EntityProperty(id + "_DDUniform_Minimo", "Mínimo", EntityProperty.PropertyType.NUMBER, true);
            propiedaA.setFirstValue(String.valueOf(dDUniform.getMin()));
            nodeProperties.add(propiedaA);

            EntityProperty propiedaB = new EntityProperty(id + "_DDUniform_Maximo", "Máximo", EntityProperty.PropertyType.NUMBER, true);
            propiedaB.setFirstValue(String.valueOf(dDUniform.getMax()));
            nodeProperties.add(propiedaB);

        } else if (discreteDistribution instanceof ConstantDistribution) {
            nodeDistributionProperty.setFirstValue(NodeDistributionProperty.DistributionType.CONSTANT);
            ConstantDistribution constantDistribution = (ConstantDistribution) discreteDistribution;

            EntityProperty propiedaA = new EntityProperty(id + "_ConstantDistribution_Constante", "Constante", EntityProperty.PropertyType.NUMBER, true);
            propiedaA.setFirstValue(String.valueOf(constantDistribution.getConstant()));
            nodeProperties.add(propiedaA);
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
            ClientNode clientNode = (ClientNode) nodeMatchCoupleObjectContainer.get(selectedGraphNode);

            if (id.equalsIgnoreCase("broker")) {
                GraphNode nodoGraficoServiceSelected = findNodoGraficoByName(value, nodeMatchCoupleObjectContainer);
                if (nodoGraficoServiceSelected != null) {
                    clientNode.setServiceNode((ServiceNode) nodeMatchCoupleObjectContainer.get(nodoGraficoServiceSelected));
                }


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
            }

        } else if (selectedGraphNode instanceof ResourceGraphNode) {
            ResourceNode resource = (ResourceNode) nodeMatchCoupleObjectContainer.get(selectedGraphNode);


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
            } else if (id.contains("RelationshipResouceAndServiceNodo")) {
                String serviceNodeName = value.replace("_ON", "").replace("_OFF", "");

                Enumeration<GraphNode> enumeration = nodeMatchCoupleObjectContainer.keys();
                GraphNode nodoGrafico;
                while ((nodoGrafico = enumeration.nextElement()) != null) {
                    if (nodoGrafico.getName().equals(serviceNodeName)) {
                        break;
                    }
                }
                Entity entity = nodeMatchCoupleObjectContainer.get(nodoGrafico);
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

        } 
//        else if (selectedGraphNode instanceof SwitchGraphNode) {
//            AbstractSwitch abstractSwitch = (AbstractSwitch) nodeMatchCoupleObjectContainer.get(selectedGraphNode);
//            if (id.equalsIgnoreCase("HandleDelay")) {
//                abstractSwitch.setHandleDelay(new Time(Double.parseDouble(value)));
//            }
//        }
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

        } catch (ClassCastException exception) {
            System.out.println(" Error class dd " + distribution + " value:  " + value + " id " + id);
        }

    }

    private DiscreteDistribution getDistributionByText(String value) {
        SimBaseSimulator baseSimulator = SimulationBase.getInstance().getGridSimulatorModel();
        if (value.equals("Uniforme")) {
            return new DDUniform(baseSimulator, 10, 20);
        } else if (value.equals("Constante")) {
            return new ConstantDistribution(10);
        } else if (value.equals("Erlang")) {
            return new DDErlang(baseSimulator, 1, 2);
        } else if (value.equals("Hiper-exponencial")) {
            return new DDHyperExp(baseSimulator, new double[]{1, 2, 3}, new double[]{1, 2, 3});
        } else if (value.equals("Exponencial-negativa")) {
            return new DDNegExp(baseSimulator, 2);
        } else if (value.equals("Normal")) {
            return new DDNormal(baseSimulator, 2, 3);
        } else if (value.equals("Possion")) {
            return new DDPoissonProcess(baseSimulator, 2);
        }
        return null;

    }

    public GraphNode findKeyNodeByValueNode(Entity entity, Hashtable<GraphNode, Entity> hashtable) {
        for (GraphNode nodoGrafico : hashtable.keySet()) {
            if (hashtable.get(nodoGrafico) == entity) {
                return nodoGrafico;
            }
        }
        return null;
    }

    public GraphNode findNodoGraficoByName(String name, Hashtable<GraphNode, Entity> hashtable) {
        for (GraphNode nodoGrafico : hashtable.keySet()) {
            if (nodoGrafico.getName().equals(name)) {
                return nodoGrafico;
            }
        }
        return null;
    }

    @Override
    public void removeNode(GraphNode nodoGrafico) {
        Entity phosNodeRemoved = nodeMatchCoupleObjectContainer.remove(nodoGrafico);
        SimulationBase.getInstance().getGridSimulatorModel().unRegister(phosNodeRemoved);
    }

    @Override
    public void reCreatePhosphorousNodes() {
        for (NodeCreationModel modeloRegistrado : nodeCreationModels) {
            modeloRegistrado.loadSimulacionBase();
        }

        for (GraphNode nodoGrafico : nodeMatchCoupleObjectContainer.keySet()) {
            createNode(nodoGrafico);

        }
        for (GraphNode nodoGrafico : nodeMatchCoupleObjectContainer.keySet()) {
            for (String id : nodoGrafico.getNodeProperties().keySet()) {
                selectedGraphNode = nodoGrafico;
                updateProperty(false, false, id, nodoGrafico.getNodeProperties().get(id));
            }
            for (String id : nodoGrafico.getSubPropertiesNode().keySet()) {
                selectedGraphNode = nodoGrafico;
                updateProperty(true, false, id, nodoGrafico.getSubPropertiesNode().get(id));
            }
        }

        for (GraphNodesView vistaNodosGraficos : graphNodesViews) {
            vistaNodosGraficos.enableDisign();
        }
    }
}
