/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.controller;

import Grid.Entity;
import Grid.Interfaces.CPU;
import Grid.Interfaces.ResourceNode;
import Grid.Nodes.AbstractResourceNode;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.presentation.design.GraphNode;
import com.ag2.presentation.design.ResourceGraphNode;
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

    private HashMap<GraphNode, Entity> nodeMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer();
    private DecimalFormat decimalFormat = new DecimalFormat("###################.###");
    private int countCPU = 0;
    private double capacityCPU = 0;
    private int maxQueueSize;

    @Override
    public void loadDataChartResourceCPU(GraphNode graphNode) {
        ResourceNode resourceNode = (ResourceNode) nodeMatchCoupleObjectContainer.get(graphNode);
        List<CPU> cpuSet = resourceNode.getCpuSet();


        double totalCPU = cpuSet.size();
        double countBusyCPU = 0;

        for (CPU cpu : cpuSet) {
            if (cpu.isBusy()) {
                countBusyCPU++;
            }
        }

        try {
            time = decimalFormat.parse(String.valueOf(100 * resourceNode.getCurrentTime().getTime() / PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SIMULATION_TIME))).doubleValue();
        } catch (ParseException ex) {
            Logger.getLogger(DataChartResourceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        value1 = Math.round(100 * (countBusyCPU / totalCPU));
    }

    @Override
    public void loadDataChartResourceBuffer(GraphNode graphNode) {
        AbstractResourceNode abstractResourceNode = (AbstractResourceNode) nodeMatchCoupleObjectContainer.get(graphNode);
        ResourceNode resourceNode = (ResourceNode) nodeMatchCoupleObjectContainer.get(graphNode);

        value1 = 0.0;//abstractResourceNode.getQueue().size();
//        value2 = resourceNode.getMaxQueueSize();
        try {
            time = decimalFormat.parse(String.valueOf(100 * resourceNode.getCurrentTime().getTime() / PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SIMULATION_TIME))).doubleValue();
        } catch (ParseException ex) {
            Logger.getLogger(DataChartResourceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadInfoCPUResouce(ResourceGraphNode graphNode) {
        ResourceNode resourceNode = (ResourceNode) nodeMatchCoupleObjectContainer.get(graphNode);

        CPU cpu = (CPU) resourceNode.getCpuSet().get(0);

        if (cpu != null) {
            capacityCPU = cpu.getCpuCapacity();
        } else {
            capacityCPU = 0;
        }
        countCPU = (int) resourceNode.getCpuCount();
    }

    public void loadInfoBufferResouce(ResourceGraphNode graphNode) {
        ResourceNode resourceNode = (ResourceNode) nodeMatchCoupleObjectContainer.get(graphNode);

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
