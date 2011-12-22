package com.ag2.presentacion.diseño;

import com.ag2.presentacion.IGU;
import com.ag2.presentacion.TiposDeBoton;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VerticeEnlaceGrafico extends Circle implements ArcoListener, Serializable 
{

    private ArcoGrafico arcoGraficoA;
    private ArcoGrafico arcoGraficoB;
    private boolean eliminado = false; 
    private double posX;  
    private double posY; 
    private double diametro = 3;  
    
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
        setFill(Color.LIGHTBLUE);             
        establecerEventoOnMouseDragged();
        establecerEventoOnMouseEntered();
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
                    VerticeEnlaceGrafico verticeEnlaceGrafico = (VerticeEnlaceGrafico) mouseEvent.getSource();

                    double dragX = mouseEvent.getX();
                    double dragY = mouseEvent.getY();

                    verticeEnlaceGrafico.setCenterX(dragX);
                    verticeEnlaceGrafico.setCenterY(dragY);

                    verticeEnlaceGrafico.getArcoGraficoA().setPosFinX(dragX);
                    verticeEnlaceGrafico.getArcoGraficoA().setPosFinY(dragY);

                    verticeEnlaceGrafico.getArcoGraficoB().setPosIniX(dragX);
                    verticeEnlaceGrafico.getArcoGraficoB().setPosIniY(dragY);

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

    public void update() {
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
    
    private void readObject(ObjectInputStream inputStream)
    {
        try 
        {
inputStream.defaultReadObject(); 
            establecerConfigInicial();
            
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
    }
}
