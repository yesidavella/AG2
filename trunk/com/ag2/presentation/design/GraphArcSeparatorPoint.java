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

public class GraphArcSeparatorPoint  implements ArcListener,Serializable {

    
    private transient Circle circle; 
    
    private GraphArc arcoGraficoA;
    private GraphArc arcoGraficoB;
    private boolean eliminado = false; 
    private final double diametro = 3;
    private boolean arrastro;
    private double centerX; 
    private double centerY; 
    private GraphDesignGroup grupoDeDiseno; 
    
    public GraphArcSeparatorPoint(GraphDesignGroup grupoDeDiseno,GraphArc arcoGraficoA, GraphArc arcoGraficoB, double posX, double posY) {
       
        this.grupoDeDiseno = grupoDeDiseno; 
        this.arcoGraficoA = arcoGraficoA;
        this.arcoGraficoB = arcoGraficoB;      
        
        initTransientObjects();      
        setCenterX(posX);
        setCenterY(posY); 
        
        this.arcoGraficoA.addNodoListener(this);
        this.arcoGraficoB.addNodoListener(this);
        
        
    }
    public void initTransientObjects() {
        
        circle = new Circle(); 
         circle.setRadius(diametro);
         circle.toFront();
         circle.setFill(Color.LIGHTGREEN);
        establecerEventoOnMouseEntered();
        establecerEventoOnMouseDragged();
        establecerEventoOnMouseClicked();
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

   

    private void establecerEventoOnMouseEntered() {
      circle.setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {

                if (IGU.getEstadoTipoBoton() == ActionTypeEmun.PUNTERO) {
                    circle.setCursor(Cursor.MOVE);
                }
            }
        });
    }

    private void establecerEventoOnMouseDragged() {
        circle.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                if (IGU.getEstadoTipoBoton() == ActionTypeEmun.PUNTERO) {
                    
                    GraphArcSeparatorPoint verticeEnlaceGrafico = GraphArcSeparatorPoint.this; 

                    double dragX = mouseEvent.getX();
                    double dragY = mouseEvent.getY();

                    verticeEnlaceGrafico.setCenterX(dragX);
                    verticeEnlaceGrafico.setCenterY(dragY);

                    verticeEnlaceGrafico.getArcoGraficoA().setEndX(dragX);
                    verticeEnlaceGrafico.getArcoGraficoA().setEndY(dragY);

                    verticeEnlaceGrafico.getArcoGraficoB().setStartX(dragX);
                    verticeEnlaceGrafico.getArcoGraficoB().setStartY(dragY);
                    
                    if(!verticeEnlaceGrafico.getArcoGraficoA().getEnlaceGrafico().getSeleccionado()){
                        verticeEnlaceGrafico.getArcoGraficoA().getEnlaceGrafico().select(true);
                    }
                    verticeEnlaceGrafico.setArrastro(true);
                }
            }
        });
    }
    
    private void establecerEventoOnMouseClicked() {
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent eventoDeRaton) {

                if (IGU.getEstadoTipoBoton() == ActionTypeEmun.PUNTERO) {
                    GraphArcSeparatorPoint verticeGrafico = GraphArcSeparatorPoint.this; 
                    
                    if (!verticeGrafico.isArrastro()) {

                        if (verticeGrafico.getArcoGraficoA().getEnlaceGrafico().getSeleccionado()) {
                            verticeGrafico.getArcoGraficoA().getEnlaceGrafico().select(false);
                        } else {
                            verticeGrafico.getArcoGraficoA().getEnlaceGrafico().select(true);
                        }
                    }
                    
                    verticeGrafico.setArrastro(false);
                }
            }
        });
    }

    public GraphArc getArcoGraficoA() {
        return arcoGraficoA;
    }

    public GraphArc getArcoGraficoB() {
        return arcoGraficoB;
    }
    
    public void setArcoGraficoA(GraphArc arcoGraficoA) {
        this.arcoGraficoA = arcoGraficoA;
    }

    public void setArcoGraficoB(GraphArc arcoGraficoB) {
        this.arcoGraficoB = arcoGraficoB;
    }
    
    public boolean isArrastro() {
        return arrastro;
    }

    public void setArrastro(boolean arrastro) {
        this.arrastro = arrastro;
    }

    public void updateArc() {
        if (  !eliminado && (arcoGraficoA.isEliminado() || arcoGraficoB.isEliminado() )) 
        {
            eliminado= true; 
            arcoGraficoA=null; 
            arcoGraficoB=null; 
            arcoGraficoA.revomeNodoListener(this); 
            arcoGraficoB.revomeNodoListener(this);             
            grupoDeDiseno.remove(this);
        }
    }
    
    private void readObject(ObjectInputStream inputStream) {
        try 
        {
           inputStream.defaultReadObject();
           

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
