package com.ag2.controller;

import com.ag2.presentation.design.ClientGraphNode;
import com.ag2.presentation.design.HybridSwitchGraphNode;
import com.ag2.presentation.design.SwitchGraphNode;
import com.ag2.presentation.design.OCS_SwicthGraphNode;
import com.ag2.presentation.design.ResourceGraphNode;
import com.ag2.presentation.design.BrokerGrahpNode;
import com.ag2.presentation.design.OBS_SwicthGraphNode;
import com.ag2.presentation.design.GraphNode;
import com.ag2.model.ModeloCrearNodoDeServicio;
import com.ag2.model.ModeloCrearEnrutadorHibrido;
import com.ag2.model.ModeloCrearCliente;
import com.ag2.model.ModeloCrearEnrutadorRafaga;
import com.ag2.model.ModeloCrearEnrutadorOptico;
import com.ag2.model.NodeCreationModel;
import com.ag2.model.ModeloCrearNodoDeRecurso;
import com.ag2.model.SimulationBase;
import Distributions.*;
import Grid.Entity;
import Grid.Interfaces.CPU;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Interfaces.Switches.AbstractSwitch;
import Grid.Port.GridOutPort;
import com.ag2.presentation.IGU;
import com.ag2.presentation.GraphNodesView;
import com.ag2.presentation.design.property.NodeRelationProperty;
import com.ag2.presentation.design.property.PropiedadNodoDistribuciones;
import com.ag2.presentation.design.property.EntityProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import simbase.Port.SimBaseOutPort;
import simbase.SimBaseEntity;
import simbase.SimBaseSimulator;
import simbase.Time;

public class ControladorAdminNodo extends NodeAdminAbstractController implements Serializable{

    private GraphNode nodoGraficoSeleccionado;




    @Override
    public Entity createNode(GraphNode nodoGrafico) {

         Entity nuevoNodoPhophorous = null;
        for (NodeCreationModel modeloRegistrado : nodeCreationModels) {


            if (modeloRegistrado instanceof ModeloCrearCliente && nodoGrafico instanceof ClientGraphNode) {
                nuevoNodoPhophorous = ((ModeloCrearCliente) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getName());
            } else if (modeloRegistrado instanceof ModeloCrearNodoDeServicio && nodoGrafico instanceof BrokerGrahpNode) {
                nuevoNodoPhophorous = ((ModeloCrearNodoDeServicio) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getName());
            } else if (modeloRegistrado instanceof ModeloCrearNodoDeRecurso && nodoGrafico instanceof ResourceGraphNode) {
                nuevoNodoPhophorous = ((ModeloCrearNodoDeRecurso) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getName());
            } else if (modeloRegistrado instanceof ModeloCrearEnrutadorRafaga && nodoGrafico instanceof OBS_SwicthGraphNode) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorRafaga) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getName());
            } else if (modeloRegistrado instanceof ModeloCrearEnrutadorOptico && nodoGrafico instanceof OCS_SwicthGraphNode) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorOptico) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getName());
            } else if (modeloRegistrado instanceof ModeloCrearEnrutadorHibrido && nodoGrafico instanceof HybridSwitchGraphNode) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorHibrido) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getName());
            }
            if (nuevoNodoPhophorous != null) {
                addNodeMatchCouple(nodoGrafico, nuevoNodoPhophorous);
            }
        }
        return  nuevoNodoPhophorous;
    }

    public void queryProperties(GraphNode nodoGrafico) {
        nodoGraficoSeleccionado = nodoGrafico;

            ArrayList<EntityProperty> propiedadesDeNodo = new ArrayList<EntityProperty>();

        //===========================================================================================================
        EntityProperty propiedadNodoNombre = new EntityProperty("nombre", "Nombre", EntityProperty.PropertyType.TEXT, false);
        propiedadNodoNombre.setFirstValue(nodoGraficoSeleccionado.getName());
        propiedadesDeNodo.add(propiedadNodoNombre);
        //===========================================================================================================

        if (nodoGrafico instanceof ClientGraphNode) {

            ClientNode clientNode = (ClientNode) nodeMatchCoupleObjectContainer.get(nodoGrafico);

            //===========================================================================================================
            NodeRelationProperty nodeRelationProperty = new NodeRelationProperty("ServiceNode", "Service node");

            for (GraphNode nodoGraficoService : nodeMatchCoupleObjectContainer.keySet()) {
                if (nodoGraficoService instanceof BrokerGrahpNode) {
                    nodeRelationProperty.getObservableListNodes().add(nodoGraficoService);
                }
            }
            if (clientNode.getServiceNode() != null) {
                GraphNode nodoServiceSelected = findKeyNodeByValueNode(clientNode.getServiceNode(), nodeMatchCoupleObjectContainer);
                if (nodoServiceSelected != null) {
                    nodeRelationProperty.setFirstValue(nodoServiceSelected);
                }
            }
            propiedadesDeNodo.add(nodeRelationProperty);

            //===========================================================================================================
            PropiedadNodoDistribuciones distribucionesTrabajos = new PropiedadNodoDistribuciones("generacionTrabajos", "Generación de trabajos");
            crearPropiedadDistriducion(clientNode.getState().getJobInterArrival(), propiedadesDeNodo, distribucionesTrabajos, "generacionTrabajos");
            //===========================================================================================================
            PropiedadNodoDistribuciones distribucionesFlops = new PropiedadNodoDistribuciones("generacionFlops", "Generación de flops por trabajo");
            crearPropiedadDistriducion(clientNode.getState().getFlops(), propiedadesDeNodo, distribucionesFlops, "generacionFlops");

            //===========================================================================================================
            PropiedadNodoDistribuciones distribucionesMaximoRetraso = new PropiedadNodoDistribuciones("generacionMaximoRetraso", "Generación de intervalo maximo de retraso");
            crearPropiedadDistriducion(clientNode.getState().getMaxDelayInterval(), propiedadesDeNodo, distribucionesMaximoRetraso, "generacionMaximoRetraso");

            //===========================================================================================================
            PropiedadNodoDistribuciones distribucionesTamanoTrabajo = new PropiedadNodoDistribuciones("generacionTamañoTrabajo", "Generación del tamaño del trabajo");
            crearPropiedadDistriducion(clientNode.getState().getSizeDistribution(), propiedadesDeNodo, distribucionesTamanoTrabajo, "generacionTamañoTrabajo");

            //===========================================================================================================
            PropiedadNodoDistribuciones distribucionesTamanoRespuesta = new PropiedadNodoDistribuciones("generacionTamañoRespuesta", "Generación del tamaño de la respuesta");
            crearPropiedadDistriducion(clientNode.getState().getAckSizeDistribution(), propiedadesDeNodo, distribucionesTamanoRespuesta, "generacionTamañoRespuesta");


        } else if (nodoGrafico instanceof ResourceGraphNode) {
            ResourceNode resource = (ResourceNode) nodeMatchCoupleObjectContainer.get(nodoGrafico);


            EntityProperty propiedadCpuCapacity = new EntityProperty("CpuCapacity", "Cpu Capacity", EntityProperty.PropertyType.TEXT, false);
            CPU cpu = (CPU) resource.getCpuSet().get(0);
            if (cpu != null) {

                propiedadCpuCapacity.setFirstValue(String.valueOf(cpu.getCpuCapacity()));
            } else {
                propiedadCpuCapacity.setFirstValue("0");
            }
            propiedadesDeNodo.add(propiedadCpuCapacity);

            //===========================================================================================================
            EntityProperty propiedadQueueSize = new EntityProperty("QueueSize", "Queue Size", EntityProperty.PropertyType.TEXT,false);
            propiedadQueueSize.setFirstValue(String.valueOf(resource.getMaxQueueSize()));
            propiedadesDeNodo.add(propiedadQueueSize);

            //===========================================================================================================
            EntityProperty propiedadCpuCount = new EntityProperty("CpuCount", "Cpu Count", EntityProperty.PropertyType.TEXT, false);
            propiedadCpuCount.setFirstValue(String.valueOf(resource.getCpuCount()));
            propiedadesDeNodo.add(propiedadCpuCount);
            //============================================================================================================

            for (GraphNode nodoGraficoService : nodeMatchCoupleObjectContainer.keySet()) {


                if (nodoGraficoService instanceof BrokerGrahpNode) {
                    EntityProperty propiedadeNodo = new EntityProperty("RelationshipResouceAndServiceNodo", nodoGraficoService.getName(), EntityProperty.PropertyType.BOOLEAN, false);
                    propiedadesDeNodo.add(propiedadeNodo);
                    for (ServiceNode serviceNode : resource.getServiceNodes())
                    {
                        if (serviceNode.getID().equals(nodoGraficoService.getOriginalName()))
                        {
                            propiedadeNodo.setFirstValue("true");
                           // propiedadeNodo.setDisable(true);
                        }
                    }
                }
            }

            //============================================================================================================

        } else if (nodoGrafico instanceof SwitchGraphNode) {
            AbstractSwitch abstractSwitch = (AbstractSwitch) nodeMatchCoupleObjectContainer.get(nodoGrafico);

            //===========================================================================================================
            EntityProperty propiedadHandleDelay = new EntityProperty("HandleDelay", "Handle Delay", EntityProperty.PropertyType.TEXT, false);
            propiedadHandleDelay.setFirstValue(String.valueOf(abstractSwitch.getHandleDelay().getTime()));
            propiedadesDeNodo.add(propiedadHandleDelay);
        }

        for (GraphNodesView vistaNodosGraficos : graphNodesViews) {
            vistaNodosGraficos.loadProperties(propiedadesDeNodo);
        }

    }

    private void crearPropiedadDistriducion(DiscreteDistribution discreteDistribution, ArrayList<EntityProperty> propiedadeNodos, PropiedadNodoDistribuciones propiedadNodoDistribuciones, String id) {

        propiedadeNodos.add(propiedadNodoDistribuciones);

        if (discreteDistribution instanceof DDErlang) {

            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.ER_LANG);

            DDErlang dDErlang = (DDErlang) discreteDistribution;

            EntityProperty propiedaA = new EntityProperty(id + "_DDErlang_Orden", "Orden", EntityProperty.PropertyType.NUMBER,true);
            propiedaA.setFirstValue(String.valueOf(dDErlang.getN()));
            propiedadeNodos.add(propiedaA);

            EntityProperty propiedaB = new EntityProperty(id + "_DDErlang_Promedio", "Promedio", EntityProperty.PropertyType.NUMBER,true);
            propiedaB.setFirstValue(String.valueOf(dDErlang.getAvg()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDHyperExp) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.HYPER_EXPONENTIAL);
            DDHyperExp dDHyperExp = (DDHyperExp) discreteDistribution;

            EntityProperty propiedaA = new EntityProperty(id + "_DDHyperExp_Lamdas", "Lamdas", EntityProperty.PropertyType.TEXT,true);
            propiedaA.setFirstValue(getStringArrayDoubles(dDHyperExp.getLambdas()));
            propiedadeNodos.add(propiedaA);

            EntityProperty propiedaB = new EntityProperty(id + "_DDHyperExp_Oportunidades", "Oportunidades", EntityProperty.PropertyType.TEXT,true);
            propiedaB.setFirstValue(getStringArrayDoubles(dDHyperExp.getChances()));
            propiedadeNodos.add(propiedaB);

        } else if (discreteDistribution instanceof DDNegExp) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.NEGATIVE_EXPONENTIAL);
            DDNegExp dDNegExp = (DDNegExp) discreteDistribution;
            EntityProperty propiedaB = new EntityProperty(id + "_DDNegExp_Promedio", "Promedio", EntityProperty.PropertyType.NUMBER,true);
            propiedaB.setFirstValue(String.valueOf(dDNegExp.getAvg()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDNormal) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.NORMAL);
            DDNormal dDNormal = (DDNormal) discreteDistribution;

            EntityProperty propiedaA = new EntityProperty(id + "_DDNormal_DesviacionEstandar", "Desviación estandar", EntityProperty.PropertyType.NUMBER,true);
            propiedaA.setFirstValue(String.valueOf(dDNormal.getDev()));
            propiedadeNodos.add(propiedaA);

            EntityProperty propiedaB = new EntityProperty(id + "_DDNormal_Promedio", "Promedio", EntityProperty.PropertyType.NUMBER,true);
            propiedaB.setFirstValue(String.valueOf(dDNormal.getAvg()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDPoissonProcess) {

            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.POISSON_PROCESS);
            DDPoissonProcess dDPoissonProcess = (DDPoissonProcess) discreteDistribution;
            EntityProperty propiedaB = new EntityProperty(id + "_DDPoissonProcess_Promedio", "Promedio", EntityProperty.PropertyType.NUMBER,true);
            propiedaB.setFirstValue(String.valueOf(dDPoissonProcess.getAverage()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDUniform) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.UNMIFORM);
            DDUniform dDUniform = (DDUniform) discreteDistribution;

            EntityProperty propiedaA = new EntityProperty(id + "_DDUniform_Minimo", "Minimo", EntityProperty.PropertyType.NUMBER,true);
            propiedaA.setFirstValue(String.valueOf(dDUniform.getMin()));
            propiedadeNodos.add(propiedaA);

            EntityProperty propiedaB = new EntityProperty(id + "_DDUniform_Maximo", "Maximo", EntityProperty.PropertyType.NUMBER,true);
            propiedaB.setFirstValue(String.valueOf(dDUniform.getMax()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof ConstantDistribution) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.CONSTANT);
            ConstantDistribution constantDistribution = (ConstantDistribution) discreteDistribution;

            EntityProperty propiedaA = new EntityProperty(id + "_ConstantDistribution_Constante", "Constante", EntityProperty.PropertyType.NUMBER,true);
            propiedaA.setFirstValue(String.valueOf(constantDistribution.getConstant()));
            propiedadeNodos.add(propiedaA);
        }
    }

    private String getStringArrayDoubles(double[] valores) {
        StringBuilder resultado = new StringBuilder();
        String separador = "";
        for (int i = 0; i < valores.length; i++) {
            resultado.append(separador + valores[i]);
            separador = "-";
        }
        return resultado.toString();
    }

    @Override
    public void updateProperty(boolean isSubProperty,  boolean conusultar, String id, String valor)
    {
        if(isSubProperty)
        {
            nodoGraficoSeleccionado.getSubPropertiesNode().put(id, valor);
        }
        else
        {
            nodoGraficoSeleccionado.getNodeProperties().put(id, valor);
        }

        if (id.equalsIgnoreCase("nombre")) {
            nodoGraficoSeleccionado.setName(valor);
            if(nodoGraficoSeleccionado instanceof ClientGraphNode)
            {
                IGU.getInstance().getGrGrupoDeDiseño().getClientsObservableList().remove(nodoGraficoSeleccionado);
                IGU.getInstance().getGrGrupoDeDiseño().getClientsObservableList().add(nodoGraficoSeleccionado);
            }else if(nodoGraficoSeleccionado instanceof ResourceGraphNode)
            {
                IGU.getInstance().getGrGrupoDeDiseño().getResourcesObservableList().remove(nodoGraficoSeleccionado);
                IGU.getInstance().getGrGrupoDeDiseño().getResourcesObservableList().add(nodoGraficoSeleccionado);
            }
            else if(nodoGraficoSeleccionado instanceof BrokerGrahpNode)
            {
                IGU.getInstance().getGrGrupoDeDiseño().getBrokersObservableList().remove(nodoGraficoSeleccionado);
                IGU.getInstance().getGrGrupoDeDiseño().getBrokersObservableList().add(nodoGraficoSeleccionado);
            }
            else if(nodoGraficoSeleccionado instanceof SwitchGraphNode)
            {
                IGU.getInstance().getGrGrupoDeDiseño().getSwitchesObservableList().remove(nodoGraficoSeleccionado);
                IGU.getInstance().getGrGrupoDeDiseño().getSwitchesObservableList().add(nodoGraficoSeleccionado);
            }

        }

        if (nodoGraficoSeleccionado instanceof ClientGraphNode) {
            ClientNode clientNode = (ClientNode) nodeMatchCoupleObjectContainer.get(nodoGraficoSeleccionado);

            if (id.equalsIgnoreCase("ServiceNode")) {
                GraphNode nodoGraficoServiceSelected = findNodoGraficoByName(valor, nodeMatchCoupleObjectContainer);
                if (nodoGraficoServiceSelected != null) {
                    clientNode.setServiceNode((ServiceNode) nodeMatchCoupleObjectContainer.get(nodoGraficoServiceSelected));
                }


            } else if (id.equalsIgnoreCase("generacionTrabajos")) {
                clientNode.getState().setJobInterArrival(getDistributionByText(valor));
                if(conusultar){queryProperties(nodoGraficoSeleccionado);}
            } else if (id.contains("generacionTrabajos")) {
                setValuesDistribution(clientNode.getState().getJobInterArrival(), valor, id);
            } else if (id.equalsIgnoreCase("generacionFlops")) {
                clientNode.getState().setFlops(getDistributionByText(valor));
                if(conusultar){queryProperties(nodoGraficoSeleccionado);}
            } else if (id.contains("generacionFlops")) {
                setValuesDistribution(clientNode.getState().getFlops(), valor, id);
            } else if (id.equalsIgnoreCase("generacionMaximoRetraso")) {
                clientNode.getState().setMaxDelayInterval(getDistributionByText(valor));
                if(conusultar){queryProperties(nodoGraficoSeleccionado);}
            } else if (id.contains("generacionMaximoRetraso")) {
                setValuesDistribution(clientNode.getState().getMaxDelayInterval(), valor, id);

            } else if (id.equalsIgnoreCase("generacionTamañoTrabajo")) {
                clientNode.getState().setSizeDistribution(getDistributionByText(valor));
                if(conusultar){queryProperties(nodoGraficoSeleccionado);}

            } else if (id.contains("generacionTamañoTrabajo")) {
                setValuesDistribution(clientNode.getState().getSizeDistribution(), valor, id);
            } else if (id.equalsIgnoreCase("generacionTamañoRespuesta")) {
                clientNode.getState().setAckSizeDistribution(getDistributionByText(valor));
                if(conusultar){queryProperties(nodoGraficoSeleccionado);}

            } else if (id.contains("generacionTamañoRespuesta")) {
                setValuesDistribution(clientNode.getState().getAckSizeDistribution(), valor, id);
            }

        } else if (nodoGraficoSeleccionado instanceof ResourceGraphNode) {
            ResourceNode resource = (ResourceNode) nodeMatchCoupleObjectContainer.get(nodoGraficoSeleccionado);


            if (id.equalsIgnoreCase("CpuCapacity")) {
                resource.setCpuCapacity(Double.parseDouble(valor));
            } else if (id.equalsIgnoreCase("QueueSize")) {
                resource.setQueueSize(Integer.parseInt(valor));
            } else if (id.equalsIgnoreCase("CpuCount")) {
                CPU cpu = (CPU) resource.getCpuSet().get(0);
                double capacity = 0;
                if (cpu != null) {
                    capacity = cpu.getCpuCapacity();
                }
                resource.setCpuCount(Integer.parseInt(valor), capacity);
            } else if (id.contains("RelationshipResouceAndServiceNodo")) {
                String serviceNodeName = valor.replace("_ON", "").replace("_OFF", "");

                Enumeration<GraphNode> enumeration = nodeMatchCoupleObjectContainer.keys();
                GraphNode nodoGrafico;
                while ((nodoGrafico = enumeration.nextElement()) != null) {
                    if (nodoGrafico.getName().equals(serviceNodeName)) {
                        break;
                    }
                }
                Entity entity = nodeMatchCoupleObjectContainer.get(nodoGrafico);
                if (valor.contains("_ON")) {
                    if (entity != null && entity instanceof ServiceNode)
                    {
                        ServiceNode serviceNode = (ServiceNode) entity;
                        resource.addServiceNode(serviceNode);
                    }

                } else if (valor.contains("_OFF")) {
                    if (entity != null && entity instanceof ServiceNode)
                    {
                        ServiceNode serviceNode = (ServiceNode) entity;
                        resource.removeServiceNode(serviceNode);

                    }
                }

            }


        } else if (nodoGraficoSeleccionado instanceof SwitchGraphNode) {
            AbstractSwitch abstractSwitch = (AbstractSwitch) nodeMatchCoupleObjectContainer.get(nodoGraficoSeleccionado);
            if (id.equalsIgnoreCase("HandleDelay")) {
                abstractSwitch.setHandleDelay(new Time(Double.parseDouble(valor)));
            }
        }
    }

    private void setValuesDistribution(DiscreteDistribution distribution, String value, String id) {
        try
        {

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

            if (id.contains("Lamdas")) {
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

            if (id.contains("Minimo")) {
                dDUniform.setMin(Double.parseDouble(value));
            } else if (id.contains("Maximo")) {
                dDUniform.setMax(Double.parseDouble(value));
            }

        } else if (id.contains("ConstantDistribution")) {
            ConstantDistribution constantDistribution = (ConstantDistribution) distribution;

            if (id.contains("Constante")) {
                constantDistribution.setConstant(Double.parseDouble(value));
            }
        }

        }
        catch(ClassCastException exception)
        {
            System.out.println( " Error class dd "+distribution+" value:  "+value+" id "+ id);
        }

    }

    private DiscreteDistribution getDistributionByText(String value)
    {
        SimBaseSimulator baseSimulator = SimulationBase.getInstance().getSimulador();
        if (value.equals("Uniforme")) {
            return new DDUniform(baseSimulator,10, 20);
        } else if (value.equals("Constante")) {
            return new ConstantDistribution(10);
        } else if (value.equals("Erlang")) {
            return new DDErlang(baseSimulator,1, 2);
        } else if (value.equals("Hiper-exponencial")) {
            return new DDHyperExp(baseSimulator,new double[]{1, 2, 3}, new double[]{1, 2, 3});
        } else if (value.equals("Exponencial-negativa")) {
            return new DDNegExp(baseSimulator,2);
        } else if (value.equals("Normal")) {
            return new DDNormal(baseSimulator,2, 3);
        } else if (value.equals("Possion")) {
            return new DDPoissonProcess(baseSimulator,2);
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
    public void removeNode(GraphNode nodoGrafico)
    {
        Entity phosNodeRemoved = nodeMatchCoupleObjectContainer.remove(nodoGrafico);
        SimulationBase.getInstance().getSimulador().unRegister(phosNodeRemoved);
    }

    @Override
    public void reCreatePhosphorousNodos()
    {
        for (NodeCreationModel modeloRegistrado : nodeCreationModels)
        {
            modeloRegistrado.loadSimulacionBase();
        }

        for(GraphNode nodoGrafico : nodeMatchCoupleObjectContainer.keySet())
        {
           createNode(nodoGrafico);

        }
        for(GraphNode nodoGrafico : nodeMatchCoupleObjectContainer.keySet())
        {
            for(String id: nodoGrafico.getNodeProperties().keySet())
           {
               nodoGraficoSeleccionado = nodoGrafico;
               updateProperty(false,false, id, nodoGrafico.getNodeProperties().get(id));
           }
           for(String id: nodoGrafico.getSubPropertiesNode().keySet())
           {
               nodoGraficoSeleccionado = nodoGrafico;
               updateProperty(true,false, id, nodoGrafico.getSubPropertiesNode().get(id));
           }


        }

        for (GraphNodesView vistaNodosGraficos : graphNodesViews) {
            vistaNodosGraficos.enableDisign();
        }
    }
}
