package com.ag2.presentation.design;

import com.ag2.presentation.IGU;
import com.ag2.presentation.ActionTypeEmun;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GraphArcSeparatorPoint implements ArcListener, Serializable {

    private transient Circle circle;
    private GraphArc graphArcA;
    private GraphArc graphArcB;
    private boolean deleted = false;
    private final double diameter = 3;
    private boolean dragged;
    private double centerX;
    private double centerY;
    private GraphDesignGroup designGroup;

    public GraphArcSeparatorPoint(GraphDesignGroup graphDesignGroup, GraphArc graphArcA, GraphArc graphArcB, double posX, double posY) {

        this.designGroup = graphDesignGroup;
        this.graphArcA = graphArcA;
        this.graphArcB = graphArcB;

        initTransientObjects();
        setCenterX(posX);
        setCenterY(posY);

        this.graphArcA.addNodeListener(this);
        this.graphArcB.addNodeListener(this);


    }

    public void initTransientObjects() {

        circle = new Circle();
        circle.setRadius(diameter);
        circle.toFront();
        circle.setFill(Color.LIGHTGREEN);
        establishEventOnMouseEntered();
        establishEventOnMouseDragged();
        establishEventOnMouseClicked();
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
        circle.setCenterX(centerX);
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
        circle.setCenterY(centerY);
    }

    public Circle getCircle() {
        return circle;
    }

    private void establishEventOnMouseEntered() {
        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {

                if (IGU.getEstadoTipoBoton() == ActionTypeEmun.PUNTERO) {
                    circle.setCursor(Cursor.MOVE);
                }
            }
        });
    }

    private void establishEventOnMouseDragged() {
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                if (IGU.getEstadoTipoBoton() == ActionTypeEmun.PUNTERO) {

                    GraphArcSeparatorPoint verticeEnlaceGrafico = GraphArcSeparatorPoint.this;

                    double dragX = mouseEvent.getX();
                    double dragY = mouseEvent.getY();

                    verticeEnlaceGrafico.setCenterX(dragX);
                    verticeEnlaceGrafico.setCenterY(dragY);

                    verticeEnlaceGrafico.getGraphArcA().setEndX(dragX);
                    verticeEnlaceGrafico.getGraphArcA().setEndY(dragY);

                    verticeEnlaceGrafico.getGraphArcB().setStartX(dragX);
                    verticeEnlaceGrafico.getGraphArcB().setStartY(dragY);

                    if (!verticeEnlaceGrafico.getGraphArcA().getGraphLink().getSeleccionado()) {
                        verticeEnlaceGrafico.getGraphArcA().getGraphLink().select(true);
                    }
                    verticeEnlaceGrafico.setDragged(true);
                }
            }
        });
    }

    private void establishEventOnMouseClicked() {
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent eventoDeRaton) {

                if (IGU.getEstadoTipoBoton() == ActionTypeEmun.PUNTERO) {
                    GraphArcSeparatorPoint graphArcSeparatorPoint = GraphArcSeparatorPoint.this;

                    if (!graphArcSeparatorPoint.isDragged()) {

                        if (graphArcSeparatorPoint.getGraphArcA().getGraphLink().getSeleccionado()) {
                            graphArcSeparatorPoint.getGraphArcA().getGraphLink().select(false);
                        } else {
                            graphArcSeparatorPoint.getGraphArcA().getGraphLink().select(true);
                        }
                    }

                    graphArcSeparatorPoint.setDragged(false);
                }
            }
        });
    }

    public GraphArc getGraphArcA() {
        return graphArcA;
    }

    public GraphArc getGraphArcB() {
        return graphArcB;
    }

    public void setGraphArcA(GraphArc graphArcA) {
        this.graphArcA = graphArcA;
    }

    public void setGraphArcB(GraphArc graphArcB) {
        this.graphArcB = graphArcB;
    }

    public boolean isDragged() {
        return dragged;
    }

    public void setDragged(boolean dragged) {
        this.dragged = dragged;
    }

    public void updateArc() {
        if (!deleted && (graphArcA.isDeleted() || graphArcB.isDeleted())) {
            deleted = true;
            graphArcA = null;
            graphArcB = null;
            graphArcA.revomeNodeListener(this);
            graphArcB.revomeNodeListener(this);
            designGroup.remove(this);
        }
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
