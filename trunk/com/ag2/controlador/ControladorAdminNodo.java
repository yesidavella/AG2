package com.ag2.controlador;

import Distributions.*;
import Grid.Entity;
import Grid.Interfaces.CPU;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Interfaces.Switches.AbstractSwitch;
import Grid.Port.GridOutPort;
import com.ag2.modelo.*;
import com.ag2.presentacion.VistaNodosGraficos;
import com.ag2.presentacion.diseño.*;
import com.ag2.presentacion.diseño.propiedades.NodeRelationProperty;
import com.ag2.presentacion.diseño.propiedades.PropiedadNodoDistribuciones;
import com.ag2.presentacion.diseño.propiedades.PropiedadeNodo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import simbase.Port.SimBaseOutPort;
import simbase.SimBaseEntity;
import simbase.SimBaseSimulator;
import simbase.Time;

public class ControladorAdminNodo extends ControladorAbstractoAdminNodo implements Serializable{

    private NodoGrafico nodoGraficoSeleccionado;

    @Override
    public Entity crearNodo(NodoGrafico nodoGrafico) {

         Entity nuevoNodoPhophorous = null;
        for (ModeloCrearNodo modeloRegistrado : modelosRegistrados) {
        

            if (modeloRegistrado instanceof ModeloCrearCliente && nodoGrafico instanceof NodoClienteGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearCliente) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (modeloRegistrado instanceof ModeloCrearNodoDeServicio && nodoGrafico instanceof NodoDeServicioGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearNodoDeServicio) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (modeloRegistrado instanceof ModeloCrearNodoDeRecurso && nodoGrafico instanceof NodoDeRecursoGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearNodoDeRecurso) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (modeloRegistrado instanceof ModeloCrearEnrutadorRafaga && nodoGrafico instanceof EnrutadorRafagaGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorRafaga) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (modeloRegistrado instanceof ModeloCrearEnrutadorOptico && nodoGrafico instanceof EnrutadorOpticoGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorOptico) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            } else if (modeloRegistrado instanceof ModeloCrearEnrutadorHibrido && nodoGrafico instanceof EnrutadorHibridoGrafico) {
                nuevoNodoPhophorous = ((ModeloCrearEnrutadorHibrido) modeloRegistrado).crearNodoPhophorous(nodoGrafico.getNombre());
            }
            if (nuevoNodoPhophorous != null) {
                addNodoGraficoYNodoPhosphorous(nodoGrafico, nuevoNodoPhophorous);
            }
        }
        return  nuevoNodoPhophorous; 
    }

    public void consultarPropiedades(NodoGrafico nodoGrafico) {
        nodoGraficoSeleccionado = nodoGrafico;

            ArrayList<PropiedadeNodo> propiedadesDeNodo = new ArrayList<PropiedadeNodo>();

        //===========================================================================================================
        PropiedadeNodo propiedadNodoNombre = new PropiedadeNodo("nombre", "Nombre", PropiedadeNodo.TipoDePropiedadNodo.TEXTO, false);
        propiedadNodoNombre.setPrimerValor(nodoGraficoSeleccionado.getNombre());
        propiedadesDeNodo.add(propiedadNodoNombre);
        //===========================================================================================================

        if (nodoGrafico instanceof NodoClienteGrafico) {

            ClientNode clientNode = (ClientNode) parejasDeNodosExistentes.get(nodoGrafico);

            //===========================================================================================================
            NodeRelationProperty nodeRelationProperty = new NodeRelationProperty("ServiceNode", "Service node");

            for (NodoGrafico nodoGraficoService : parejasDeNodosExistentes.keySet()) {
                if (nodoGraficoService instanceof NodoDeServicioGrafico) {
                    nodeRelationProperty.getObservableListNodes().add(nodoGraficoService);
                }
            }
            if (clientNode.getServiceNode() != null) {
                NodoGrafico nodoServiceSelected = findKeyNodeByValueNode(clientNode.getServiceNode(), parejasDeNodosExistentes);
                if (nodoServiceSelected != null) {
                    nodeRelationProperty.setPrimerValor(nodoServiceSelected);
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


        } else if (nodoGrafico instanceof NodoDeRecursoGrafico) {
            ResourceNode resource = (ResourceNode) parejasDeNodosExistentes.get(nodoGrafico);


            PropiedadeNodo propiedadCpuCapacity = new PropiedadeNodo("CpuCapacity", "Cpu Capacity", PropiedadeNodo.TipoDePropiedadNodo.TEXTO, false);
            CPU cpu = (CPU) resource.getCpuSet().get(0);
            if (cpu != null) {

                propiedadCpuCapacity.setPrimerValor(String.valueOf(cpu.getCpuCapacity()));
            } else {
                propiedadCpuCapacity.setPrimerValor("0");
            }
            propiedadesDeNodo.add(propiedadCpuCapacity);

            //===========================================================================================================
            PropiedadeNodo propiedadQueueSize = new PropiedadeNodo("QueueSize", "Queue Size", PropiedadeNodo.TipoDePropiedadNodo.TEXTO,false);
            propiedadQueueSize.setPrimerValor(String.valueOf(resource.getMaxQueueSize()));
            propiedadesDeNodo.add(propiedadQueueSize);

            //===========================================================================================================
            PropiedadeNodo propiedadCpuCount = new PropiedadeNodo("CpuCount", "Cpu Count", PropiedadeNodo.TipoDePropiedadNodo.TEXTO, false);
            propiedadCpuCount.setPrimerValor(String.valueOf(resource.getCpuCount()));
            propiedadesDeNodo.add(propiedadCpuCount);
            //============================================================================================================

            for (NodoGrafico nodoGraficoService : parejasDeNodosExistentes.keySet()) {


                if (nodoGraficoService instanceof NodoDeServicioGrafico) {
                    PropiedadeNodo propiedadeNodo = new PropiedadeNodo("RelationshipResouceAndServiceNodo", nodoGraficoService.getNombre(), PropiedadeNodo.TipoDePropiedadNodo.BOLEANO, false);
                    propiedadesDeNodo.add(propiedadeNodo);
                    for (ServiceNode serviceNode : resource.getServiceNodes())
                    {
                        if (serviceNode.getID().equals(nodoGraficoService.getNombreOriginal()))
                        {
                            propiedadeNodo.setPrimerValor("true");
                            propiedadeNodo.setDisable(true);
                        }
                    }
                }
            }

            //============================================================================================================

        } else if (nodoGrafico instanceof EnrutadorGrafico) {
            AbstractSwitch abstractSwitch = (AbstractSwitch) parejasDeNodosExistentes.get(nodoGrafico);

            //===========================================================================================================
            PropiedadeNodo propiedadHandleDelay = new PropiedadeNodo("HandleDelay", "Handle Delay", PropiedadeNodo.TipoDePropiedadNodo.TEXTO, false);
            propiedadHandleDelay.setPrimerValor(String.valueOf(abstractSwitch.getHandleDelay().getTime()));
            propiedadesDeNodo.add(propiedadHandleDelay);
        }

        for (VistaNodosGraficos vistaNodosGraficos : listaVistaNodosGraficos) {
            vistaNodosGraficos.cargarPropiedades(propiedadesDeNodo);
        }

    }

    private void crearPropiedadDistriducion(DiscreteDistribution discreteDistribution, ArrayList<PropiedadeNodo> propiedadeNodos, PropiedadNodoDistribuciones propiedadNodoDistribuciones, String id) {

        propiedadeNodos.add(propiedadNodoDistribuciones);

        if (discreteDistribution instanceof DDErlang) {

            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.ER_LANG);

            DDErlang dDErlang = (DDErlang) discreteDistribution;

            PropiedadeNodo propiedaA = new PropiedadeNodo(id + "_DDErlang_Orden", "Orden", PropiedadeNodo.TipoDePropiedadNodo.NUMERO,true);
            propiedaA.setPrimerValor(String.valueOf(dDErlang.getN()));
            propiedadeNodos.add(propiedaA);

            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_DDErlang_Promedio", "Promedio", PropiedadeNodo.TipoDePropiedadNodo.NUMERO,true);
            propiedaB.setPrimerValor(String.valueOf(dDErlang.getAvg()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDHyperExp) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.HYPER_EXPONENTIAL);
            DDHyperExp dDHyperExp = (DDHyperExp) discreteDistribution;

            PropiedadeNodo propiedaA = new PropiedadeNodo(id + "_DDHyperExp_Lamdas", "Lamdas", PropiedadeNodo.TipoDePropiedadNodo.TEXTO,true);
            propiedaA.setPrimerValor(getStringArrayDoubles(dDHyperExp.getLambdas()));
            propiedadeNodos.add(propiedaA);

            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_DDHyperExp_Oportunidades", "Oportunidades", PropiedadeNodo.TipoDePropiedadNodo.TEXTO,true);
            propiedaB.setPrimerValor(getStringArrayDoubles(dDHyperExp.getChances()));
            propiedadeNodos.add(propiedaB);

        } else if (discreteDistribution instanceof DDNegExp) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.NEGATIVE_EXPONENTIAL);
            DDNegExp dDNegExp = (DDNegExp) discreteDistribution;
            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_DDNegExp_Promedio", "Promedio", PropiedadeNodo.TipoDePropiedadNodo.NUMERO,true);
            propiedaB.setPrimerValor(String.valueOf(dDNegExp.getAvg()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDNormal) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.NORMAL);
            DDNormal dDNormal = (DDNormal) discreteDistribution;

            PropiedadeNodo propiedaA = new PropiedadeNodo(id + "_DDNormal_DesviacionEstandar", "Desviación estandar", PropiedadeNodo.TipoDePropiedadNodo.NUMERO,true);
            propiedaA.setPrimerValor(String.valueOf(dDNormal.getDev()));
            propiedadeNodos.add(propiedaA);

            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_DDNormal_Promedio", "Promedio", PropiedadeNodo.TipoDePropiedadNodo.NUMERO,true);
            propiedaB.setPrimerValor(String.valueOf(dDNormal.getAvg()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDPoissonProcess) {

            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.POISSON_PROCESS);
            DDPoissonProcess dDPoissonProcess = (DDPoissonProcess) discreteDistribution;
            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_DDPoissonProcess_Promedio", "Promedio", PropiedadeNodo.TipoDePropiedadNodo.NUMERO,true);
            propiedaB.setPrimerValor(String.valueOf(dDPoissonProcess.getAverage()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof DDUniform) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.UNMIFORM);
            DDUniform dDUniform = (DDUniform) discreteDistribution;

            PropiedadeNodo propiedaA = new PropiedadeNodo(id + "_DDUniform_Minimo", "Minimo", PropiedadeNodo.TipoDePropiedadNodo.NUMERO,true);
            propiedaA.setPrimerValor(String.valueOf(dDUniform.getMin()));
            propiedadeNodos.add(propiedaA);

            PropiedadeNodo propiedaB = new PropiedadeNodo(id + "_DDUniform_Maximo", "Maximo", PropiedadeNodo.TipoDePropiedadNodo.NUMERO,true);
            propiedaB.setPrimerValor(String.valueOf(dDUniform.getMax()));
            propiedadeNodos.add(propiedaB);


        } else if (discreteDistribution instanceof ConstantDistribution) {
            propiedadNodoDistribuciones.setPrimerValor(PropiedadNodoDistribuciones.TipoDeDistribucion.CONSTANT);
            ConstantDistribution constantDistribution = (ConstantDistribution) discreteDistribution;

            PropiedadeNodo propiedaA = new PropiedadeNodo(id + "_ConstantDistribution_Constante", "Constante", PropiedadeNodo.TipoDePropiedadNodo.NUMERO,true);
            propiedaA.setPrimerValor(String.valueOf(constantDistribution.getConstant()));
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
    public void updatePropiedad(boolean isSubProperty,  boolean conusultar, String id, String valor) 
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
            nodoGraficoSeleccionado.setNombre(valor);
        }

        if (nodoGraficoSeleccionado instanceof NodoClienteGrafico) {
            ClientNode clientNode = (ClientNode) parejasDeNodosExistentes.get(nodoGraficoSeleccionado);

            if (id.equalsIgnoreCase("ServiceNode")) {
                NodoGrafico nodoGraficoServiceSelected = findNodoGraficoByName(valor, parejasDeNodosExistentes);
                if (nodoGraficoServiceSelected != null) {
                    clientNode.setServiceNode((ServiceNode) parejasDeNodosExistentes.get(nodoGraficoServiceSelected));
                }


            } else if (id.equalsIgnoreCase("generacionTrabajos")) {
                clientNode.getState().setJobInterArrival(getDistributionByText(valor));
                if(conusultar){consultarPropiedades(nodoGraficoSeleccionado);}
            } else if (id.contains("generacionTrabajos")) {
                setValuesDistribution(clientNode.getState().getJobInterArrival(), valor, id);
            } else if (id.equalsIgnoreCase("generacionFlops")) {
                clientNode.getState().setFlops(getDistributionByText(valor));
                if(conusultar){consultarPropiedades(nodoGraficoSeleccionado);}
            } else if (id.contains("generacionFlops")) {
                setValuesDistribution(clientNode.getState().getFlops(), valor, id);
            } else if (id.equalsIgnoreCase("generacionMaximoRetraso")) {
                clientNode.getState().setMaxDelayInterval(getDistributionByText(valor));
                if(conusultar){consultarPropiedades(nodoGraficoSeleccionado);}
            } else if (id.contains("generacionMaximoRetraso")) {
                setValuesDistribution(clientNode.getState().getMaxDelayInterval(), valor, id);

            } else if (id.equalsIgnoreCase("generacionTamañoTrabajo")) {
                clientNode.getState().setSizeDistribution(getDistributionByText(valor));
                if(conusultar){consultarPropiedades(nodoGraficoSeleccionado);}

            } else if (id.contains("generacionTamañoTrabajo")) {
                setValuesDistribution(clientNode.getState().getSizeDistribution(), valor, id);
            } else if (id.equalsIgnoreCase("generacionTamañoRespuesta")) {
                clientNode.getState().setAckSizeDistribution(getDistributionByText(valor));
                if(conusultar){consultarPropiedades(nodoGraficoSeleccionado);}

            } else if (id.contains("generacionTamañoRespuesta")) {
                setValuesDistribution(clientNode.getState().getAckSizeDistribution(), valor, id);
            }

        } else if (nodoGraficoSeleccionado instanceof NodoDeRecursoGrafico) {
            ResourceNode resource = (ResourceNode) parejasDeNodosExistentes.get(nodoGraficoSeleccionado);


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

                Enumeration<NodoGrafico> enumeration = parejasDeNodosExistentes.keys();
                NodoGrafico nodoGrafico;
                while ((nodoGrafico = enumeration.nextElement()) != null) {
                    if (nodoGrafico.getNombre().equals(serviceNodeName)) {
                        break;
                    }
                }
                Entity entity = parejasDeNodosExistentes.get(nodoGrafico);
                if (valor.contains("_ON")) {
                    if (entity != null && entity instanceof ServiceNode) 
                    {
                        ServiceNode serviceNode = (ServiceNode) entity;
                        resource.addServiceNode(serviceNode);                  
                    }

                } else if (valor.contains("_OFF")) {
                    if (entity != null && entity instanceof ServiceNode) {
                        ServiceNode serviceNode = (ServiceNode) entity;
                    }
                }

            }


        } else if (nodoGraficoSeleccionado instanceof EnrutadorGrafico) {
            AbstractSwitch abstractSwitch = (AbstractSwitch) parejasDeNodosExistentes.get(nodoGraficoSeleccionado);
            if (id.equalsIgnoreCase("HandleDelay")) {
                abstractSwitch.setHandleDelay(new Time(Double.parseDouble(valor)));
            }
        }
    }

    private void setValuesDistribution(DiscreteDistribution distribution, String value, String id) {
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

    private DiscreteDistribution getDistributionByText(String value) 
    {
        SimBaseSimulator baseSimulator = SimulacionBase.getInstance().getSimulador(); 
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

    public NodoGrafico findKeyNodeByValueNode(Entity entity, Hashtable<NodoGrafico, Entity> hashtable) {
        for (NodoGrafico nodoGrafico : hashtable.keySet()) {
            if (hashtable.get(nodoGrafico) == entity) {
                return nodoGrafico;
            }
        }
        return null;
    }

    public NodoGrafico findNodoGraficoByName(String name, Hashtable<NodoGrafico, Entity> hashtable) {
        for (NodoGrafico nodoGrafico : hashtable.keySet()) {
            if (nodoGrafico.getNombre().equals(name)) {
                return nodoGrafico;
            }
        }
        return null;
    }

    @Override
    public void removeNodo(NodoGrafico nodoGrafico) 
    {
        Entity deletedEntity = parejasDeNodosExistentes.get(nodoGrafico);
        
        for(SimBaseOutPort outPort:deletedEntity.getOutPorts()){
            
            Entity target = (Entity)outPort.getTarget().getOwner();
            
            while(target.getOutportTo(deletedEntity)!=null){
                GridOutPort outPortToDeleteInTarget = target.getOutportTo(deletedEntity);
                target.getOutPorts().remove(outPortToDeleteInTarget);
            }
        }
        
        SimulacionBase.getInstance().getSimulador().unRegister(deletedEntity);
        parejasDeNodosExistentes.remove(nodoGrafico);
        
    }

    @Override
    public void reCreatePhosphorousNodos() 
    {
        for (ModeloCrearNodo modeloRegistrado : modelosRegistrados)
        {
            modeloRegistrado.loadSimulacionBase();
        }        
        
        for(NodoGrafico nodoGrafico : parejasDeNodosExistentes.keySet())
        {
           crearNodo(nodoGrafico);
               
        }
        for(NodoGrafico nodoGrafico : parejasDeNodosExistentes.keySet())
        {
            for(String id: nodoGrafico.getNodeProperties().keySet())
           {
               nodoGraficoSeleccionado = nodoGrafico; 
               updatePropiedad(false,false, id, nodoGrafico.getNodeProperties().get(id));
           } 
           for(String id: nodoGrafico.getSubPropertiesNode().keySet())
           {
               nodoGraficoSeleccionado = nodoGrafico; 
               updatePropiedad(true,false, id, nodoGrafico.getSubPropertiesNode().get(id));
           }
            
            
        }
         
        
        for (VistaNodosGraficos vistaNodosGraficos : listaVistaNodosGraficos) {
            vistaNodosGraficos.enableDisign();
        }
     
          
            
    }
}
