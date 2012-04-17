
package com.ag2.model;

import Grid.Interfaces.CPU;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ResourceSelector;
import java.io.Serializable;
import java.util.List;


public class AG2_ResourceSelectorModel implements ResourceSelector, Serializable {

    // private List<ResourceNode> resources;

     public AG2_ResourceSelectorModel(List<ResourceNode> resources)
     {
    }

    @Override
    public ResourceNode findBestResource(List<ResourceNode> resources, double jobFlops)
    {

       // this.resources = resources;

         double maxCPUCount=0;;

        double maxCPUCountSwap = 0;
        for(ResourceNode resourceNode :resources )
        {
            maxCPUCountSwap = resourceNode.getCpuCount();
            if(maxCPUCountSwap>maxCPUCount)
            {
                maxCPUCount =maxCPUCountSwap;
            }
        }
         System.out.println(" Recurso max cpu "+maxCPUCount);



        double cost = Double.MAX_VALUE;
        ResourceNode resourceNodeSelected =null;

        for(ResourceNode resourceNode :resources )
        {
            double costCurrent = getCostProcByResource(resourceNode, jobFlops,maxCPUCount);
         //     System.out.println(" recuroso opcion "+resourceNode+" al costo "+costCurrent);
            if(costCurrent<cost )
            {
                cost = costCurrent;
                resourceNodeSelected = resourceNode;
            }
        }
       // System.out.println(" Recurso seleccionado "+resourceNodeSelected+" al costo "+cost);
        return resourceNodeSelected;
    }
    public synchronized double getCostProcByResource( ResourceNode resourceNode, double jobFlops, double maxCPUCount)
    {
        double Tproc;
        double Cproc ;
        double Acpu ;
        double CPU_libre=0;
        double CPU_totales;
        double d;
        double z=1;
        /////////////
        double capacityCPU;

        CPU cpu1 = (CPU) resourceNode.getCpuSet().get(0);
        if (cpu1 != null)
        {
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
        if(CPU_libre ==0)
        {
            return  Double.MAX_VALUE;
        }

        Acpu= CPU_libre* capacityCPU ;
        CPU_totales = maxCPUCount;

        Cproc = jobFlops /   (Acpu/CPU_libre);
        d= (1/3)*(1 - (CPU_libre/CPU_totales ));

        Tproc = Cproc + (d*Cproc);

        return z*Tproc;


    }

    @Override
    public ResourceNode findBestresource(double jobFlops) {
      return  null; // FIXME: por ahora no se debe  usar // findBestResource(resources, jobFlops);
    }

}