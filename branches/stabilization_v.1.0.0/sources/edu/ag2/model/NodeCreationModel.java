package edu.ag2.model;

import Grid.Entity;
import edu.ag2.controller.NodeAdminAbstractController;
import java.io.Serializable;

public abstract class NodeCreationModel implements Serializable {
    
    private NodeAdminAbstractController nodeAdminAbstractController;
    protected SimulationBase simulationBase;

    public NodeCreationModel() {
       loadSimulacionBase();
    }
    
    public  void loadSimulacionBase(){
         simulationBase =  simulationBase.getInstance();
    }

    public boolean addNodeAdminController(NodeAdminAbstractController nodeAdminAbstractController){
        this.nodeAdminAbstractController = nodeAdminAbstractController;

        if(this.nodeAdminAbstractController!=null){
            return true;
        }else{
            return false;
        }
    }

    public abstract Entity createPhosphorusNode(String id);
    
    
}
