package com.ag2.presentacion.diseño;

import com.ag2.presentacion.IGU;
import com.ag2.presentacion.TiposDeBoton;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class VerticeEnlaceGrafico extends Circle implements ArcoListener,Serializable {

    private ArcoGrafico arcoGraficoA;
    private ArcoGrafico arcoGraficoB;
    private boolean eliminado = false; 
    private double posX;  
    private double posY; 
    private double diametro = 3;
    private boolean arrastro;
    
    public VerticeEnlaceGrafico(ArcoGrafico arcoGraficoA, ArcoGrafico arcoGraficoB, double posX, double posY) {
        
        this.posX = posX; 
        this.posY = posY; 
        this.arcoGraficoA = arcoGraficoA;
        this.arcoGraficoB = arcoGraficoB;
        
        establecerConfigInicial();

        this.arcoGraficoA.addNodoListener(this);
        this.arcoGraficoB.addNodoListener(this);
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) 
    {
        setCenterX(posX);
        this.posX = posX;
    }

    public double getPosY() 
    {
        return posY;
    }

    public void setPosY(double posY) 
    {
        setCenterY(posY);
        this.posY = posY;
    }

    private void establecerConfigInicial() 
    {
        setCenterX(this.posX);
        setCenterY(this.posY);
        setRadius(diametro);
        
        establecerEventoOnMouseEntered();
        establecerEventoOnMouseDragged();
        establecerEventoOnMouseClicked();

    }

    private void establecerEventoOnMouseEntered() {
        setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {

                if (IGU.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    setCursor(Cursor.MOVE);
                }
            }
        });
    }

    private void establecerEventoOnMouseDragged() {
        setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                if (IGU.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    
                    VerticeEnlaceGrafico verticeEnlaceGrafico = (VerticeEnlaceGrafico)mouseEvent.getSource();

                    double dragX = mouseEvent.getX();
                    double dragY = mouseEvent.getY();

                    verticeEnlaceGrafico.setCenterX(dragX);
                    verticeEnlaceGrafico.setCenterY(dragY);

                    verticeEnlaceGrafico.getArcoGraficoA().setEndX(dragX);
                    verticeEnlaceGrafico.getArcoGraficoA().setEndY(dragY);

                    verticeEnlaceGrafico.getArcoGraficoB().setStartX(dragX);
                    verticeEnlaceGrafico.getArcoGraficoB().setStartY(dragY);
                    
                    if(!verticeEnlaceGrafico.getArcoGraficoA().getEnlaceGrafico().getSeleccionado()){
                        verticeEnlaceGrafico.getArcoGraficoA().getEnlaceGrafico().seleccionar(true);
                    }
                    verticeEnlaceGrafico.setArrastro(true);
                }
            }
        });
    }
    
    private void establecerEventoOnMouseClicked() {
        setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent eventoDeRaton) {

                if (IGU.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    VerticeEnlaceGrafico verticeGrafico = (VerticeEnlaceGrafico)eventoDeRaton.getSource();
                    
                    if (!verticeGrafico.isArrastro()) {

                        if (verticeGrafico.getArcoGraficoA().getEnlaceGrafico().getSeleccionado()) {
                            verticeGrafico.getArcoGraficoA().getEnlaceGrafico().seleccionar(false);
                        } else {
                            verticeGrafico.getArcoGraficoA().getEnlaceGrafico().seleccionar(true);
                        }
                    }
                    
                    verticeGrafico.setArrastro(false);
                }
            }
        });
    }

    public ArcoGrafico getArcoGraficoA() {
        return arcoGraficoA;
    }

    public ArcoGrafico getArcoGraficoB() {
        return arcoGraficoB;
    }
    
    public void setArcoGraficoA(ArcoGrafico arcoGraficoA) {
        this.arcoGraficoA = arcoGraficoA;
    }

    public void setArcoGraficoB(ArcoGrafico arcoGraficoB) {
        this.arcoGraficoB = arcoGraficoB;
    }
    
    public boolean isArrastro() {
        return arrastro;
    }

    public void setArrastro(boolean arrastro) {
        this.arrastro = arrastro;
    }

    public void updateArco() {
        if (  !eliminado && (arcoGraficoA.isEliminado() || arcoGraficoB.isEliminado() )) 
        {
            eliminado= true; 
            arcoGraficoA=null; 
            arcoGraficoB=null; 
//            arcoGraficoA.revomeNodoListener(this); 
//            arcoGraficoB.revomeNodoListener(this);             
            ((Group) getParent()).getChildren().remove(this);
        }
    }
    
    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
            establecerConfigInicial();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
