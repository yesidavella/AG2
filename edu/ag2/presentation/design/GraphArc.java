package edu.ag2.presentation.design;

import edu.ag2.presentation.ActionTypeEmun;
import edu.ag2.presentation.GUI;
import edu.ag2.presentation.Main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.StrokeType;
import javax.swing.JOptionPane;

public class GraphArc implements Serializable {

    private transient QuadCurve quadCurve;
    private GraphDesignGroup graphDesignGroup;
    private GraphNode graphNodeB;
    private ArrayList<ArcListener> arcListeners = new ArrayList<ArcListener>();
    private boolean deleted = false;
    private GraphLink graphLink;
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double controlX;
    private double controlY;
    private GraphArcSeparatorPoint initialGraphArcSeparatorPoint, finalGraphArcSeparatorPoint;

    public GraphArc(GraphLink graphLink, GraphDesignGroup graphDesignGroup) {
        this.graphDesignGroup = graphDesignGroup;
        this.graphLink = graphLink;
        this.graphNodeB = this.graphLink.getGraphNodeB();
        initTransientObjects();
    }

    public void setVisible(boolean visible) {
        quadCurve.setVisible(visible);
        
        if (initialGraphArcSeparatorPoint != null) {
            initialGraphArcSeparatorPoint.getCircle().setVisible(visible);
        }
        
        if (finalGraphArcSeparatorPoint != null) {
            finalGraphArcSeparatorPoint.getCircle().setVisible(visible);
        }
    }

    public GraphArcSeparatorPoint getInitialGraphArcSeparatorPoint() {
        return initialGraphArcSeparatorPoint;
    }

    public void setInitialGraphArcSeparatorPoint(GraphArcSeparatorPoint initialGraphArcSeparatorPoint) {
        this.initialGraphArcSeparatorPoint = initialGraphArcSeparatorPoint;
    }

    public GraphArcSeparatorPoint getFinalGraphArcSeparatorPoint() {
        return finalGraphArcSeparatorPoint;
    }

    public void setFinalGraphArcSeparatorPoint(GraphArcSeparatorPoint finalGraphArcSeparatorPoint) {
        this.finalGraphArcSeparatorPoint = finalGraphArcSeparatorPoint;
    }

    public void initTransientObjects() {
        quadCurve = new QuadCurve();
        quadCurve.setFill(null);
        quadCurve.setStrokeWidth(2);
        quadCurve.setStrokeType(StrokeType.CENTERED);
        quadCurve.setStroke(Color.LIGHTGREEN);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.WHITESMOKE);
        dropShadow.setSpread(0.5);
        dropShadow.setWidth(30);
        dropShadow.setHeight(30);
        quadCurve.setEffect(dropShadow);

        establishEventMouseDragged();
        establecerEnventoClicked();
        establishEventOnMouseEntered();
    }

    public void calculateCenter() {
        double x1 = getStartX();
        double y1 = getStartY();

        double x2 = getEndX();
        double y2 = getEndY();

        controlX = (x1 + x2) / 2;
        controlY = (y1 + y2) / 2;

        setControlX(controlX);
        setControlY(controlY);
    }

    private void establishEventOnMouseEntered() {

        quadCurve.setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                GraphArc quadCurveFuente = GraphArc.this;

                ActionTypeEmun actionTypeEmun = GUI.getActionTypeEmun();

                if (actionTypeEmun == ActionTypeEmun.ADD_LINK_SEPARATOR || actionTypeEmun == ActionTypeEmun.DELETED || 
                        actionTypeEmun == ActionTypeEmun.POINTER) {
                    quadCurveFuente.getQuadCurve().setCursor(actionTypeEmun.getOverCursorImage());
                }else{
                    quadCurveFuente.getQuadCurve().setCursor(actionTypeEmun.getCursorImage());
                }
            }
        });
    }

    private void establishEventMouseDragged() {
        quadCurve.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (GUI.getActionTypeEmun() == ActionTypeEmun.POINTER) {
                    double dragX = me.getX();
                    double dragY = me.getY();
                    GraphArc graphArc = GraphArc.this;
                    graphArc.setControlX(dragX);
                    graphArc.setControlY(dragY);

                    if (!graphLink.isSelected()) {
                        graphLink.select(true);
                    }
                }
            }
        });
    }

    private void establecerEnventoClicked() {
        
        quadCurve.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                double clickX = mouseEvent.getX();
                double clickY = mouseEvent.getY();

                if (GUI.getActionTypeEmun() == ActionTypeEmun.POINTER) {
                    if (!graphLink.isSelected()) {
                        graphLink.select(true);
                    } else {
                        graphLink.select(false);
                    }
                }

                if (GUI.getActionTypeEmun() == ActionTypeEmun.ADD_LINK_SEPARATOR) {

                    GraphArc graphArc = GraphArc.this;
                    GraphArc newGraphArc = new GraphArc(graphArc.getGraphLink(), graphArc.getGroup());

                    newGraphArc.setStartX(clickX);
                    newGraphArc.setStartY(clickY);
                    newGraphArc.setEndX(graphArc.getEndX());
                    newGraphArc.setEndY(graphArc.getEndY());

                    graphArc.setEndX(clickX);
                    graphArc.setEndY(clickY);

                    graphArc.calculateCenter();
                    newGraphArc.calculateCenter();

                    GraphArcSeparatorPoint graphArcSeparatorPointNew = new GraphArcSeparatorPoint(graphDesignGroup, graphArc, newGraphArc, clickX, clickY);

                    if (graphArc.getFinalGraphArcSeparatorPoint() != null) {
                        graphArc.getFinalGraphArcSeparatorPoint().setGraphArcA(newGraphArc);
                        newGraphArc.setFinalGraphArcSeparatorPoint(graphArc.getFinalGraphArcSeparatorPoint());
                    }

                    newGraphArc.setInitialGraphArcSeparatorPoint(graphArcSeparatorPointNew);
                    graphArc.setFinalGraphArcSeparatorPoint(graphArcSeparatorPointNew);

                    graphDesignGroup.add(newGraphArc);
                    graphDesignGroup.add(graphArcSeparatorPointNew);
                    graphNodeB.getGroup().toFront();

                    graphLink.getGraphArcs().add(newGraphArc);
                    graphLink.findOutInitialAndFinalArc();

                    graphLink.select(true);

                } else if (GUI.getActionTypeEmun() == ActionTypeEmun.DELETED) {

                    GraphNode nodeA = graphLink.getGraphNodeA();
                    GraphNode nodeB = graphLink.getGraphNodeB();

                    nodeA.removeNodeListener(graphLink);
                    nodeB.removeNodeListener(graphLink);

                    for (GraphArc graphArc : graphLink.getGraphArcs()) {
                        graphArc.setDeleted(true);
                        graphArc.updateArcListeners();
                        graphDesignGroup.remove(graphArc);
                    }

                    if (!graphLink.removeGraphLink()) {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar los puertos de los Nodos Phosphorous satisfactoriamente.");
                    }

                }
            }
        });
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public GraphLink getGraphLink() {
        return graphLink;
    }

    public void addNodeListener(ArcListener arcListener) {
        arcListeners.add(arcListener);
    }

    public void revomeNodeListener(ArcListener arcListener) {
        arcListeners.remove(arcListener);
    }

    public void updateArcListeners() {
        
        for (ArcListener arcoListener : arcListeners) {
            arcoListener.updateArc();
        }
    }

    public GraphDesignGroup getGroup() {
        return graphDesignGroup;
    }

    public GraphNode getGraphNodeB() {
        return graphNodeB;
    }

    public double getControlX() {
        return controlX;
    }

    public void setControlX(double controlX) {
        this.controlX = controlX;
        quadCurve.setControlX(controlX);
    }

    public double getControlY() {
        return controlY;
    }

    public void setControlY(double controlY) {
        this.controlY = controlY;
        quadCurve.setControlY(controlY);
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
        quadCurve.setEndX(endX);
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
        quadCurve.setEndY(endY);
    }

    public QuadCurve getQuadCurve() {
        return quadCurve;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
        quadCurve.setStartX(startX);
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
        quadCurve.setStartY(startY);
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeObject(ObjectOutputStream stream) {
        try {
            stream.defaultWriteObject();
            Main.countObject++;
            //System.out.println("Writing: " + Main.countObject + "  " + this.getClass().getCanonicalName());
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}