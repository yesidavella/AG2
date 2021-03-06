package edu.ag2.controller;

import Grid.Entity;
import edu.ag2.model.NodeCreationModel;
import edu.ag2.model.SimulationBase;
import edu.ag2.presentation.GraphNodesView;
import edu.ag2.presentation.design.GraphNode;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class NodeAdminAbstractController implements Serializable {

    protected transient ArrayList<GraphNodesView>  graphNodesViews = new ArrayList<GraphNodesView>();
    protected ArrayList<NodeCreationModel>   nodeCreationModels;
   

    public NodeAdminAbstractController() {
        nodeCreationModels = new ArrayList<NodeCreationModel>();
        
        SimulationBase.getInstance().setNodeAdminController(this);
    }

    public void addGraphNodesView(GraphNodesView graphNodesView) {
        graphNodesViews.add(graphNodesView);
    }

    public void removeGraphNodesView(GraphNodesView graphNodesView) {
        graphNodesViews.remove(graphNodesView);
    }

    public boolean addModel(NodeCreationModel nodeCreationModel) {
        return nodeCreationModels.add(nodeCreationModel)
                && nodeCreationModel.addNodeAdminController(this);
    }

    public boolean removeModel(NodeCreationModel nodeCreationModel) {
        return nodeCreationModels.remove(nodeCreationModel);
    }

    public void addNodeMatchCouple(GraphNode graphNode, Entity phosphorusNode) {
        MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().put(graphNode, phosphorusNode);
    }

    public void removeNodeMatchCouple(GraphNode graphNode) {
        MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().remove(graphNode);
    }

    public abstract Entity createNode(GraphNode graphNode);

    public abstract void queryProperties(GraphNode graphNode);

    public abstract void updateProperty(boolean isSubProperty, boolean doQuery, String id, String value);

    public abstract void removeNode(GraphNode graphNode);

    public abstract void reCreatePhosphorousNodes();

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
            graphNodesViews = new ArrayList<GraphNodesView>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}