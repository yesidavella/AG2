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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AG2_ResourceSelectorModel implements ResourceSelector, Serializable {

    // private List<ResourceNode> resources;
    boolean swithOrder = true;

    public AG2_ResourceSelectorModel(List<ResourceNode> resources) {
    }

    @Override
    public ResourceNode findBestResource(Entity sourceEntity, List<ResourceNode> resources, double jobFlops, PCE pce,JobAckMessage job) {

        // this.resources = resources;

        double maxCPUCount = 0;
        double maxCPUCountSwap = 0;
        double maxBuffer = 0;
        double maxBufferSwap = 0;
        
        Map<ResourceNode ,Double> mapResourceNetworkCost = pce.getMarkovCostList((ClientNode)sourceEntity, resources, job);
        
        for (ResourceNode resourceNode : resources) {
            maxCPUCountSwap = resourceNode.getCpuCount();
            if (maxCPUCountSwap > maxCPUCount) {
                maxCPUCount = maxCPUCountSwap;
            }
            maxBufferSwap = resourceNode.getMaxQueueSize();
            if (maxBufferSwap > maxBuffer) {
                maxBuffer = maxBufferSwap;
            }
        }

        double cost = Double.MAX_VALUE;
        ResourceNode resourceNodeSelected = null;
        double totalCost= 0;
        for (ResourceNode resourceNode : resources) 
        {
            double gridCost = getCostProcByResource(resourceNode, jobFlops, maxCPUCount, maxBuffer);            
            Double networkCost = mapResourceNetworkCost.get(resourceNode); 
            
            
            if(networkCost!=null)
            {
                totalCost =  gridCost+ networkCost;
            }
            else
            {
                totalCost = gridCost+1000000000; //FIXME: Solo temp. Cuando  no encuentre al recurso
            }
            
            if (swithOrder) {
                if (totalCost < cost) {
                    cost = totalCost;
                    resourceNodeSelected = resourceNode;
                }
            } else {
                if (totalCost <= cost) {
                    cost = totalCost;
                    resourceNodeSelected = resourceNode;
                }
            }
        }

        swithOrder = !swithOrder;


        // System.out.println(" Recurso seleccionado "+resourceNodeSelected+" al costo "+cost);
        return resourceNodeSelected;
    }
//FIXME: verificar el synchronized
    public synchronized double getCostProcByResource(ResourceNode resourceNode, double jobFlops, double maxCPUCount, double maxBuffer) {
        double Tproc;
        double Cproc;
        double Acpu;
        double CPU_libre = 0;
        double CPU_totales;
        double d;
        double s;
        double bufferBusy;
        double bufferFree;
        double z = 1;
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
        Acpu = CPU_libre * capacityCPU;
        CPU_totales = maxCPUCount;

        Cproc = jobFlops / (Acpu / CPU_libre);
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
            System.out.println("Writing: " + Main.countObject + "  " + this.getClass().getCanonicalName());
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
