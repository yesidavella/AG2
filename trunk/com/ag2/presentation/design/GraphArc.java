package com.ag2.presentation.design;

import com.ag2.presentation.IGU;
import com.ag2.presentation.ActionTypeEmun;
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

public class GraphArc  implements Serializable {

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

    private GraphArcSeparatorPoint initialGraphArcSeparatorPoint,finalGraphArcSeparatorPoint;

    public GraphArc(GraphLink graphLink, GraphDesignGroup graphDesignGroup)
    {
        this.graphDesignGroup = graphDesignGroup;
        this.graphLink = graphLink;
        this.graphNodeB = this.graphLink.GraphLink.super.getGraphNodeB();
        initTransientObjects();

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


    public void initTransientObjects()
    {
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

                ActionTypeEmun actionTypeEmun = IGU.getEstadoTipoBoton();

                if (actionTypeEmun == ActionTypeEmun.ADICIONAR_VERTICE || actionTypeEmun == ActionTypeEmun.ELIMINAR) {
                    quadCurveFuente.getQuadCurve().setCursor(actionTypeEmun.getImagenSobreObjetoCursor());
                } else if (actionTypeEmun == ActionTypeEmun.PUNTERO) {
                    quadCurveFuente.getQuadCurve().setCursor(actionTypeEmun.getImagenSobreObjetoCursor());
                }
            }
        });
    }

    private void establishEventMouseDragged() {
        quadCurve.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (IGU.getEstadoTipoBoton() == ActionTypeEmun.PUNTERO) {
                    double dragX = me.getX();
                    double dragY = me.getY();
                    GraphArc graphArc = GraphArc.this ;
                    graphArc.setControlX(dragX);
                    graphArc.setControlY(dragY);

                    if(!graphLink.isSelected()){
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

                if(IGU.getEstadoTipoBoton() == ActionTypeEmun.PUNTERO){
                    if(!graphLink.isSelected()){
                        graphLink.select(true);
                    }else{
                        graphLink.select(false);
                    }
                }

                if (IGU.getEstadoTipoBoton() == ActionTypeEmun.ADICIONAR_VERTICE) {

                    GraphArc graphArc = GraphArc.this;
                    GraphArc arcGrafNuevo = new GraphArc(graphArc.getGraphLink(), graphArc.getGroup());

                    arcGrafNuevo.setStartX(clickX);
                    arcGrafNuevo.setStartY(clickY);
                    arcGrafNuevo.setEndX(graphArc.getEndX());
                    arcGrafNuevo.setEndY(graphArc.getEndY());

                    graphArc.setEndX(clickX);
                    graphArc.setEndY(clickY);

                    graphArc.calculateCenter();
                    arcGrafNuevo.calculateCenter();

                    GraphArcSeparatorPoint graphArcSeparatorPointNew = new GraphArcSeparatorPoint(graphDesignGroup, graphArc, arcGrafNuevo, clickX, clickY);

                    if( graphArc.getFinalGraphArcSeparatorPoint()!=null){
                        graphArc.getFinalGraphArcSeparatorPoint().setGraphArcA(arcGrafNuevo);
                        arcGrafNuevo.setFinalGraphArcSeparatorPoint(graphArc.getFinalGraphArcSeparatorPoint());
                    }

                    arcGrafNuevo.setInitialGraphArcSeparatorPoint(graphArcSeparatorPointNew);
                    graphArc.setFinalGraphArcSeparatorPoint(graphArcSeparatorPointNew);

                    graphDesignGroup.add(arcGrafNuevo);
                    graphDesignGroup.add(graphArcSeparatorPointNew);
                    graphNodeB.getGroup().toFront();

                    graphLink.getGraphArcs().add(arcGrafNuevo);
                    graphLink.findOutInitialAndFinalArc();

                    graphLink.select(true);

                } else if (IGU.getEstadoTipoBoton() == ActionTypeEmun.ELIMINAR) {

                    GraphNode nodeA = graphLink.getGraphNodeA();
                    GraphNode nodeB = graphLink.getGraphNodeB();

                    nodeA.removeNodeListener(graphLink);
                    nodeB.removeNodeListener(graphLink);

                    nodeA.setLinkCounter((short) (nodeA.getLinkCounter() - 1));
                    nodeB.setLinkCounter((short) (nodeB.getLinkCounter() - 1));

                    for (GraphArc graphArc : graphLink.getGraphArcs()) {
                        graphArc.setDeleted(true);
                        graphArc.updateArcListeners();

                        graphDesignGroup.remove(graphArc);
                    }

                    if(!graphLink.removeGraphLink()){
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

    public void setControlX(double controlX)
    {
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

    private void writeObject(ObjectOutputStream stream){
        try {
            stream.defaultWriteObject();
            System.out.println("Write : Arco" );
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
