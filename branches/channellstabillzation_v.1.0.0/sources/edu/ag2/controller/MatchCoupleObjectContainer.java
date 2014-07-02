package edu.ag2.controller;

import Grid.Entity;
import edu.ag2.model.PhosphorusLinkModel;
import edu.ag2.presentation.design.GraphLink;
import edu.ag2.presentation.design.GraphNode;
import edu.ag2.presentation.design.GraphOCS;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class MatchCoupleObjectContainer {

    private static HashMap<GraphNode, Entity> nodeMatchCoupleObjectContainer;
    private static HashMap<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer;
    private static HashMap<GraphSourceDestination, GraphOCS> OCSMatchCoupleObjectContainer;

    public static HashMap<GraphSourceDestination, GraphOCS> getOCSMatchCoupleObjectContainer() {

        if (OCSMatchCoupleObjectContainer == null) {
            OCSMatchCoupleObjectContainer = new HashMap<GraphSourceDestination, GraphOCS>();
        }
        return OCSMatchCoupleObjectContainer;
    }

    public static boolean containsIntanceOCS(GraphNode graphNodeSource, GraphNode graphNodeDestination) 
    {
        GraphSourceDestination sourceDestination = new GraphSourceDestination(graphNodeSource, graphNodeDestination);

        if (OCSMatchCoupleObjectContainer.containsKey(sourceDestination)) {
            return true;
        }
        return false;
    }

    public static GraphOCS getGraphOCS(GraphNode graphNodeSource, GraphNode graphNodeDestination) {
        GraphSourceDestination sourceDestination = new GraphSourceDestination(graphNodeSource, graphNodeDestination);

        return OCSMatchCoupleObjectContainer.get(sourceDestination);


    }

    public static void putInstanceOCS(GraphNode graphNodeSource, GraphNode graphNodeDestination, GraphOCS graphOCS) {

        GraphSourceDestination sourceDestination = new GraphSourceDestination(graphNodeSource, graphNodeDestination);

        if (!OCSMatchCoupleObjectContainer.containsKey(sourceDestination)) {
            getOCSMatchCoupleObjectContainer().put(sourceDestination, graphOCS);
        }

    }

    public static void cleanOCSMatchCoupleObjectContainer() {
        OCSMatchCoupleObjectContainer = new HashMap<GraphSourceDestination, GraphOCS>();

    }

    public static HashMap<GraphNode, Entity> getInstanceNodeMatchCoupleObjectContainer() {

        if (nodeMatchCoupleObjectContainer == null) {
            nodeMatchCoupleObjectContainer = new HashMap<GraphNode, Entity>();
        }
        return nodeMatchCoupleObjectContainer;
    }

    public static HashMap getInstanceLinkMatchCoupleObjectContainer() {

        if (linkMatchCoupleObjectContainer == null) {
            linkMatchCoupleObjectContainer = new HashMap<GraphLink, PhosphorusLinkModel>();
        }
        return linkMatchCoupleObjectContainer;
    }

    public static void setLinkMatchCoupleObjectContainer(HashMap<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer) {
        MatchCoupleObjectContainer.linkMatchCoupleObjectContainer = linkMatchCoupleObjectContainer;
    }

    public static void setNodeMatchCoupleObjectContainer(HashMap<GraphNode, Entity> nodeMatchCoupleObjectContainer) {
        MatchCoupleObjectContainer.nodeMatchCoupleObjectContainer = nodeMatchCoupleObjectContainer;
    }

    public static class GraphSourceDestination {

        GraphNode graphNodeSource;
        GraphNode graphNodeDestination;

        public GraphSourceDestination(GraphNode graphNodeSource, GraphNode graphNodeDestination) {

            if (graphNodeSource == null || graphNodeDestination == null) {
                throw new IllegalArgumentException("No debe ir Null "); 
                        
            }
            this.graphNodeSource = graphNodeSource;
            this.graphNodeDestination = graphNodeDestination;
        }

        @Override
        public int hashCode() {
            int hash = 5;
                hash = 83 * hash + (this.graphNodeSource.getOriginalName() != null ? this.graphNodeSource.getOriginalName().hashCode() : 0);
            hash = 83 * hash + (this.graphNodeDestination.getOriginalName() != null ? this.graphNodeDestination.getOriginalName().hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object object) {

            if (object instanceof GraphSourceDestination) {
                GraphSourceDestination sourceDestination = (GraphSourceDestination) object;

                if (sourceDestination.graphNodeSource.getOriginalName().equals(graphNodeSource.getOriginalName())
                        && sourceDestination.graphNodeDestination.getOriginalName().equals(graphNodeDestination.getOriginalName())) {
                    return true;
                }
            }
            return false;
        }

        public GraphNode getGraphNodeDestination() {
            return graphNodeDestination;
        }

        public void setGraphNodeDestination(GraphNode graphNodeDestination) {
            this.graphNodeDestination = graphNodeDestination;
        }

        public GraphNode getGraphNodeSource() {
            return graphNodeSource;
        }

        public void setGraphNodeSource(GraphNode graphNodeSource) {
            this.graphNodeSource = graphNodeSource;
        }
    }
}
