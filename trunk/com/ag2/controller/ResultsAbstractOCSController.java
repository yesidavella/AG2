/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.controller;

import com.ag2.presentation.design.GraphNode;
import java.util.ArrayList;

/**
 *
 * @author Frank
 */
public abstract class ResultsAbstractOCSController 
{
    protected  GraphNode gnSummarySource; 
    protected  GraphNode gnSummaryDestination;
    protected  double  requestedSummaryOCS; 
    protected  double  createdSummaryOCS; 
    protected  double  faultSummaryOCS;
    protected  double  timeDuracionSummaryOCS;
    
    protected ArrayList<GraphNode> pathInstaceOCS;
    protected  int     wavelengthID;
    protected  double  requestTimeInstanceOCS;
    protected  double  setupTimeInstanceOCS;
    protected  double  durationTimeInstanceOCS;
    protected  double  tearDownTimeInstanceOCS;
    protected  double  trafficInstanceOCS;
    protected  String  problemInstanceOCS;
    protected  GraphNode  nodeErrorInstanceOCS;
    
        
    public abstract int sizeSummaryOCS();
    public abstract void loadOCS_SummaryByIndex(int index);
    public abstract int sizeInstanceOCS(GraphNode graphNodeSource, GraphNode graphNodeDestination);
    public abstract void loadOCS_InstanceByIndex(GraphNode graphNodeSource, GraphNode graphNodeDestination, int index);

    
    public double getCreatedSummaryOCS() {
        return createdSummaryOCS;
    }


    public double getFaultSummaryOCS() {
        return faultSummaryOCS;
    }
 
    public GraphNode getGnSummaryDestination() {
        return gnSummaryDestination;
    }

  
    public GraphNode getGnSummarySource() {
        return gnSummarySource;
    }


    public double getRequestedSummaryOCS() {
        return requestedSummaryOCS;
    }

    public double getTimeDuracionSummaryOCS() {
        return timeDuracionSummaryOCS;
    }

    public double getDurationTimeInstanceOCS() {
        return durationTimeInstanceOCS;
    }

    public GraphNode getNodeErrorInstanceOCS() {
        return nodeErrorInstanceOCS;
    }

    public ArrayList<GraphNode> getPathInstaceOCS() {
        return pathInstaceOCS;
    }

    public String getProblemInstanceOCS() {
        return problemInstanceOCS;
    }

    public double getRequestTimeInstanceOCS() {
        return requestTimeInstanceOCS;
    }

    public double getSetupTimeInstanceOCS() {
        return setupTimeInstanceOCS;
    }

    public double getTearDownTimeInstanceOCS() {
        return tearDownTimeInstanceOCS;
    }

    public double getTrafficInstanceOCS() {
        return trafficInstanceOCS;
    }

    public int getWavelengthID() {
        return wavelengthID;
    }

        
        
}
