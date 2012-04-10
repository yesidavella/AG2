/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.controller;

import Grid.Entity;
import Grid.Interfaces.CPU;
import Grid.Interfaces.ResourceNode;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.presentation.design.GraphNode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frank
 */
public class DataChartResourceController extends DataChartAbstractController
{
    private HashMap<GraphNode, Entity> nodeMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer();

    @Override
    public void loadDataChart(GraphNode graphNode) 
    {
         ResourceNode resourceNode = (ResourceNode)  nodeMatchCoupleObjectContainer.get(graphNode);
         List<CPU> cpuSet = resourceNode.getCpuSet(); 
         
         double totalCPU = cpuSet.size();
         double countBusyCPU =0;
         
         for(CPU cpu : cpuSet )
         {
             if(cpu.isBusy())
             {
                 countBusyCPU++;
             }            
         }
         DecimalFormat decimalFormat = new DecimalFormat("###################.###");
        try {
            time = decimalFormat.parse(String.valueOf(100* resourceNode.getCurrentTime().getTime()/ PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SIMULATION_TIME))).doubleValue();
        } catch (ParseException ex) {
            Logger.getLogger(DataChartResourceController.class.getName()).log(Level.SEVERE, null, ex);
        }
         value1 =  Math.round(100*(countBusyCPU/totalCPU));       
         
        
       
    }
  
    
    
}
