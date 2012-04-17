
package com.ag2.model;

import Grid.Interfaces.CPU;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ResourceSelector;
import java.io.Serializable;
import java.util.List;


public class AG2_ResourceSelectorModel implements ResourceSelector, Serializable {

     private List<ResourceNode> resources;

     public AG2_ResourceSelectorModel(List<ResourceNode> resources) {
        this.resources = resources;
    }

    @Override
    public ResourceNode findBestResource(List<ResourceNode> resources, double jobFlops)
    {

        double cost = Double.MAX_VALUE;
        ResourceNode resourceNodeSelected =null;
        for(ResourceNode resourceNode :resources )
        {
            double costCurrent = getCostProcByResource(resourceNode, jobFlops);
            if(costCurrent<cost )
            {
                cost = costCurrent;
                resourceNodeSelected = resourceNode;
            }
        }

        return resourceNodeSelected;
    }
    public synchronized double getCostProcByResource( ResourceNode resourceNode, double jobFlops)
    {
        double Tproc;
        double Cproc ;
        double Acpu ;
        double CPU_libre;
        double CPU_totales;
        double d;
        double z=1;
        /////////////
        double capacityCPU;

         CPU cpu = (CPU) resourceNode.getCpuSet().get(0);
        if (cpu != null) {
            capacityCPU = cpu.getCpuCapacity();
        } else {
            capacityCPU = 0;
        }

        Acpu= resourceNode.getCpuFreeCount()* capacityCPU ;
        CPU_libre  = resourceNode.getCpuFreeCount();
        CPU_totales = resourceNode.getCpuCount();

        Cproc = jobFlops /   (Acpu/CPU_libre);
        d= (1/3)*(1 - (CPU_libre/CPU_totales ));

        Tproc = Cproc + (d*Cproc);
        return z*Tproc;


    }

    @Override
    public ResourceNode findBestresource(double jobFlops) {
      return  findBestResource(resources, jobFlops);
    }

}
