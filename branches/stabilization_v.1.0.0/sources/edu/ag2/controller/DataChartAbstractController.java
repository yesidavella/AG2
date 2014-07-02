/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ag2.controller;

import edu.ag2.presentation.design.GraphNode;

/**
 *
 * @author Frank
 */
public abstract class DataChartAbstractController 
{
    
    protected double time; 
    protected double value1; 
    protected double value2; 
    
    public abstract boolean loadDataChartResourceCPU(GraphNode graphNode);
    public abstract boolean loadDataChartResourceBuffer(GraphNode graphNode);

    public double getTime() {
        return time;
    }

    public double getValue1() {
        return value1;
    }

    public double getValue2() {
        return value2;
    }
    
      
    
    
}
