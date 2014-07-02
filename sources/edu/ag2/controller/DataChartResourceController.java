/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ag2.controller;

import Grid.Entity;
import Grid.Interfaces.CPU;
import Grid.Interfaces.ResourceNode;
import Grid.Nodes.AbstractResourceNode;
import edu.ag2.config.PropertyPhosphorusTypeEnum;
import edu.ag2.presentation.design.GraphNode;
import edu.ag2.presentation.design.ResourceGraphNode;
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
public class DataChartResourceController extends DataChartAbstractController {

    private DecimalFormat decimalFormat = new DecimalFormat("#.######");
    private int countCPU = 0;
    private double capacityCPU = 0;
    private int maxQueueSize;
    private HashMap<GraphNode, Double> nodeTimeCPU = new HashMap<GraphNode, Double>();
    private HashMap<GraphNode, Double> nodeTimeBuffer = new HashMap<GraphNode, Double>();

    @Override
    public boolean loadDataChartResourceCPU(GraphNode graphNode) {
        AbstractResourceNode resourceNode = (AbstractResourceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(graphNode);


        try {
            time = decimalFormat.parse(String.valueOf(100 * resourceNode.getCurrentTime().getTime() / PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SIMULATION_TIME))).doubleValue();
            if (nodeTimeCPU.containsKey(graphNode)) {
                if (nodeTimeCPU.get(graphNode) != time) {
                    nodeTimeCPU.put(graphNode, time);
                } else {
                    return false;
                }
            } else {
                nodeTimeCPU.put(graphNode, time);
            }
        } catch (ParseException ex) {
            Logger.getLogger(DataChartResourceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        value1 = resourceNode.getAverageCPU() * 100;

        return true;
    }

    @Override
    public boolean loadDataChartResourceBuffer(GraphNode graphNode) {
        AbstractResourceNode abstractResourceNode = (AbstractResourceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(graphNode);
        ResourceNode resourceNode = (ResourceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(graphNode);

        
         try {
            time = decimalFormat.parse(String.valueOf(100 * resourceNode.getCurrentTime().getTime() / PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SIMULATION_TIME))).doubleValue();
            if (nodeTimeBuffer.containsKey(graphNode)) {
                if (nodeTimeBuffer.get(graphNode) != time) {
                    nodeTimeBuffer.put(graphNode, time);
                } else {
                    return false;
                }
            } else {
                nodeTimeBuffer.put(graphNode, time);
            }
        } catch (ParseException ex) {
            Logger.getLogger(DataChartResourceController.class.getName()).log(Level.SEVERE, null, ex);
        }               
        
        value1 = abstractResourceNode.getAverageBuffer();
        
        return true;
        
    }

    public void loadInfoCPUResouce(ResourceGraphNode graphNode) {
        ResourceNode resourceNode = (ResourceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(graphNode);

        CPU cpu = (CPU) resourceNode.getCpuSet().get(0);

        if (cpu != null) {
            capacityCPU = cpu.getCpuCapacity();
        } else {
            capacityCPU = 0;
        }
        countCPU = (int) resourceNode.getCpuCount();
    }

    public void loadInfoBufferResouce(ResourceGraphNode graphNode) {
        ResourceNode resourceNode = (ResourceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(graphNode);

        maxQueueSize = resourceNode.getMaxQueueSize();

    }

    public double getCapacityCPU() {
        return capacityCPU;
    }

    public int getCountCPU() {
        return countCPU;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }
}
