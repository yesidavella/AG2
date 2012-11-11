package com.ag2.model;

import Grid.Entity;
import Grid.Interfaces.CPU;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.Messages.JobAckMessage;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ResourceSelector;
import Grid.Nodes.AbstractResourceNode;
import Grid.Nodes.PCE;
import com.ag2.presentation.Main;
import com.ag2.presentation.design.GraphArc;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AG2_ResourceSelectorModel implements ResourceSelector, Serializable {

//    private static final double COEFFICIENT_PROPORTIONALITY_GRID = 9670.77;//Coef. de proporcionalidad entre la red y la grilla.
    boolean swithOrder = true;

    public AG2_ResourceSelectorModel(List<ResourceNode> resources) {
    }

    @Override
    public ResourceNode findBestResource(Entity sourceEntity, List<ResourceNode> resources, double jobFlops, PCE pce, JobAckMessage ackMsg) {

        double maxCPUCount = 0;
        double maxCPUCountSwap = 0;
        double maxBuffer = 0;
        double maxBufferSwap = 0;
        ResourceNode resourceNodeMaxCPU = null;

        Map<ResourceNode, Double> mapResourceNetworkCost = pce.getMarkovCostList((ClientNode) sourceEntity, resources, ackMsg);

        for (ResourceNode resourceNode : resources) {
            maxCPUCountSwap = resourceNode.getCpuCount();
            if (maxCPUCountSwap > maxCPUCount) {
                maxCPUCount = maxCPUCountSwap;
                resourceNodeMaxCPU = resourceNode;
            }
            maxBufferSwap = resourceNode.getMaxQueueSize();
            if (maxBufferSwap > maxBuffer) {
                maxBuffer = maxBufferSwap;
            }
        }


        boolean allFullBusy = true; 
        /**
         * Variables de menor y mayor costo de red y grilla
         */
        double minGridCost = Double.MAX_VALUE;
//        double minNetworkCost = Double.MAX_VALUE;
        double maxGridCost = 0;
        double maxNetworkCost = 0;

        HashMap<ResourceNode, Double> mapResourceGridCost = new HashMap();

        for (ResourceNode resourceNode : resources) 
        {
            double gridCost = getCostProcByResource(resourceNode, jobFlops, maxCPUCount, maxBuffer);
//             System.out.println("Recurtso:"+resourceNode +" costo grid" +gridCost);
            mapResourceGridCost.put(resourceNode, gridCost);
            Double networkCost = mapResourceNetworkCost.get(resourceNode);

            if (gridCost < minGridCost) {
                minGridCost = gridCost;
            }

            if (gridCost > maxGridCost) {
                maxGridCost = gridCost;
            }

            if (networkCost > maxNetworkCost) {
                maxNetworkCost = networkCost;
            }

            if (Double.MAX_VALUE != gridCost) 
            {
                allFullBusy = false;
            }
        
        }

        double degreeGrid = maxGridCost - minGridCost;

        double percentGrid = 0.5;
        double percentNetwork = 0.5;
        double minTotalGrade = Double.MAX_VALUE;
        ResourceNode resourceSelectedByGrade = null;
        double totalGrade = 0;

        for (ResourceNode resourceNode : resources) {
            double relativeGridCost = mapResourceGridCost.get(resourceNode) - minGridCost;
            double relativeNetworkCost = mapResourceNetworkCost.get(resourceNode);
           

            double gradeGrid = 0;
            if (degreeGrid > 0) {
                gradeGrid = (relativeGridCost * 100) / degreeGrid;
            }
            double gradeNetwork = 0;

            if (maxNetworkCost > 0) {
                gradeNetwork = (relativeNetworkCost * 100) / maxNetworkCost;
            }

            if( mapResourceGridCost.get(resourceNode) == Double.MAX_VALUE)
            {
                totalGrade = 100+(gradeNetwork * percentNetwork) ;
            }   
            else
            {
                
                 totalGrade = (gradeNetwork * percentNetwork) + (gradeGrid * percentGrid);
            }
           

            
            if (totalGrade < minTotalGrade)
            {
                minTotalGrade = totalGrade;
                resourceSelectedByGrade = resourceNode;

            }

        //   System.out.println("Recurso:" + resourceNode.getID() + " NOta grilla:" + gradeGrid + ". Nota Red:" + gradeNetwork + ". TotalGrade:" + totalGrade);
        }

//
//        System.out.println("Red max:" + maxNetworkCost + "-- Grilla min:" + minGridCost + " max:" + maxGridCost);
//        System.out.println("seleccionado:" + resourceSelectedByGrade + " con GradeTotal:" + minTotalGrade);

        ackMsg.setEstimatedNetworkCost( mapResourceGridCost.get(resourceSelectedByGrade));
        ackMsg.setEstimatedGridCost(mapResourceNetworkCost.get(resourceSelectedByGrade));
        ackMsg.setDomainPCE(pce);

        if (allFullBusy) {
            return resourceNodeMaxCPU;
        }

        return resourceSelectedByGrade;
    }

    public double getCostProcByResource(ResourceNode resourceNode, double jobFlops, double maxCPUCount, double maxBuffer) {
        double Tproc;
        double Cproc;
        double Acpu;
        int CPU_libre = 0;
        double CPU_totales;
        double d;
        double s;
        double bufferBusy;
        double bufferFree;
        double z = 1;//Coeficiente de ajuste entre el costo estimado de red+grilla y el costo real obtendio.
        /////////////
        double capacityCPU;

        CPU cpu1 = (CPU) resourceNode.getCpuSet().get(0);
        if (cpu1 != null) {
            capacityCPU = cpu1.getCpuCapacity();
        } else {
            capacityCPU = 0;
        }
        List<CPU> cpuSet = resourceNode.getCpuSet();
        for (CPU cpu : cpuSet) {
            if (!cpu.isBusy()) {
                CPU_libre++;
            }
        }
        if (CPU_libre == 0) {
            return Double.MAX_VALUE;
        }

        bufferBusy = ((AbstractResourceNode) resourceNode).getQueue().size();
        bufferFree = resourceNode.getMaxQueueSize() - bufferBusy;
//        Acpu = CPU_libre * capacityCPU;
        CPU_totales = maxCPUCount;

        Cproc = (jobFlops / (capacityCPU)  ) /CPU_libre;
        
        d = (40 / 100) * (1 - (CPU_libre / CPU_totales));
        s = (20 / 100) * (1 - (bufferFree / maxBuffer));

        Tproc = Cproc + (d * Cproc) + (s * Cproc);

        return z * Tproc;
    }

    @Override
    public ResourceNode findBestresource(double jobFlops) {
        return null; // FIXME: por ahora no se debe  usar // findBestResource(resources, jobFlops);
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
