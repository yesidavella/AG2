package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.presentation.GUI;
import com.ag2.presentation.Main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.paint.Color;

public class GraphLink implements NodeListener, Serializable, Selectable {

    private GraphNode graphNodeA;
    private GraphNode graphNodeB;
    private ArrayList<GraphArc> graphArcs = new ArrayList<GraphArc>();
    private GraphArc initialGraphArc, finalGraphArc;
    private GraphDesignGroup graphDesignGroup;
    private LinkAdminAbstractController linkAdminCtr;
    private boolean isSelected;
    private HashMap<String, String> properties;

    public GraphLink(GraphDesignGroup graphDesignGroup, GraphNode graphNodeA, GraphNode graphNodeB, LinkAdminAbstractController linkAdminAbstractController) {

        this.graphNodeA = graphNodeA;
        this.graphNodeB = graphNodeB;
        this.linkAdminCtr = linkAdminAbstractController;
        this.graphNodeA.addNodeListener(this);
        this.graphNodeB.addNodeListener(this);

        this.graphDesignGroup = graphDesignGroup;

        initialGraphArc = new GraphArc(this, graphDesignGroup);

        initialGraphArc.setStartX(graphNodeA.getLayoutX() + graphNodeA.getWidth() / 2);
        initialGraphArc.setStartY(graphNodeA.getLayoutY() + graphNodeA.getHeight() / 2);
        initialGraphArc.setEndX(graphNodeB.getLayoutX() + graphNodeB.getWidth() / 2);
        initialGraphArc.setEndY(graphNodeB.getLayoutY() + graphNodeB.getHeight() / 2);
        initialGraphArc.calculateCenter();
        graphArcs.add(initialGraphArc);

        properties = new HashMap<String, String>();
        findOutInitialAndFinalArc();
//        linkAdminAbstractController.createLink(graphNodeA,graphNodeB);
//        select(true);

    }

    public void setVisible(boolean visible) {
        for (GraphArc graphArc : graphArcs) {
            graphArc.setVisible(visible);
        }
    }

    public void addInitialGraphArc() {
        graphDesignGroup.add(initialGraphArc);
    }

    public ArrayList<GraphArc> getGraphArcs() {
        return graphArcs;
    }

    public GraphNode getGraphNodeB() {
        return graphNodeB;
    }

    @Override
    public void update() {
        if (graphNodeA != null && graphNodeB != null) {
            if (graphNodeA.isDeleted() || graphNodeB.isDeleted()) {

                for (GraphArc graphArc : graphArcs) {
                    graphArc.setDeleted(true);
                    graphArc.updateArcListeners();
                    graphDesignGroup.remove(graphArc);
                }
                graphNodeA = null;
                graphNodeB = null;

                removeGraphLink();

            } else {

                initialGraphArc.setStartX(graphNodeA.getLayoutX() + graphNodeA.getWidth() / 2);
                initialGraphArc.setStartY(graphNodeA.getLayoutY() + 0.75 * graphNodeA.getHeight() - graphNodeA.getInitialHeight() / 4);

                finalGraphArc.setEndX(graphNodeB.getLayoutX() + graphNodeB.getWidth() / 2);
                finalGraphArc.setEndY(graphNodeB.getLayoutY() + 0.75 * graphNodeB.getHeight() - graphNodeB.getInitialHeight() / 4);
            }
        }
    }

    public GraphNode getGraphNodeA() {
        return graphNodeA;
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void select(boolean isSelected) {

        Selectable selectable = graphDesignGroup.getSelectable();

        if (selectable != null && selectable != this) {
            selectable.select(false);
        }

        this.isSelected = isSelected;

        if (isSelected) {
            for (GraphArc graphArc : graphArcs) {
                graphArc.getQuadCurve().getStyleClass().remove("arcoNoSeleccionado");
                graphArc.getQuadCurve().getStyleClass().add("arcoSeleccionado");

                GraphArcSeparatorPoint verticeGrafico = graphArc.getInitialGraphArcSeparatorPoint();

                if (verticeGrafico != null) {
                    verticeGrafico.getCircle().setFill(Color.web("#44FF00"));
                    verticeGrafico.getCircle().toFront();
                }
            }
            graphDesignGroup.setSelectable(this);
            linkAdminCtr.queryProperty(this);

        } else {
            for (GraphArc graphArc : graphArcs) {
                graphArc.getQuadCurve().getStyleClass().remove("arcoSeleccionado");
                graphArc.getQuadCurve().getStyleClass().add("arcoNoSeleccionado");

                GraphArcSeparatorPoint graphArcSeparatorPoint = graphArc.getInitialGraphArcSeparatorPoint();

                if (graphArcSeparatorPoint != null) {
                    graphArcSeparatorPoint.getCircle().setFill(Color.LIGHTGREEN);
                    graphArcSeparatorPoint.getCircle().toFront();
                }
            }
            graphDesignGroup.setSelectable(null);
        }

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void findOutInitialAndFinalArc() {

        for (GraphArc graphArc : graphArcs) {
            /*
             * Para saber por q esto se puede asegurar, vease ArcoGrafico metodo
             * setOnMouseClicked cuando el tipo de boton seleccionado es
             * TiposDeBoton.ADICIONAR_VERTICE
             */
            if (graphArc.getInitialGraphArcSeparatorPoint() == null) {
                initialGraphArc = graphArc;
            }

            if (graphArc.getFinalGraphArcSeparatorPoint() == null) {
                finalGraphArc = graphArc;
            }
        }
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public boolean removeGraphLink() {
        return linkAdminCtr.removeLink(this);
    }

    private void writeObject(ObjectOutputStream stream) {
        try {
            stream.defaultWriteObject();
            Main.countObject++;
            System.out.println("Writing: " + Main.countObject + "  " + this.getClass().getCanonicalName());
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}