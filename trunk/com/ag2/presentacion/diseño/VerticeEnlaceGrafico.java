package com.ag2.presentacion.diseño;

import com.ag2.presentacion.Main;
import com.ag2.presentacion.TiposDeBoton;
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

    public VerticeEnlaceGrafico(ArcoGrafico arcoGraficoA, ArcoGrafico arcoGraficoB, double posX, double posY) {
        super(posX, posY, 3);
        setFill(Color.LIGHTBLUE);
        this.arcoGraficoA = arcoGraficoA;
        this.arcoGraficoB = arcoGraficoB;

        this.arcoGraficoA.addNodoListener(this);
        this.arcoGraficoB.addNodoListener(this);

        setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                if (Main.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
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

        setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {

                if (Main.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    setCursor(Cursor.MOVE);
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
}
