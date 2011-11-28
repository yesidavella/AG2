package com.ag2.presentacion.diseño;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.shape.QuadCurve;

public class EnlaceGrafico implements NodoListener,  Serializable  {

    private NodoGrafico nodoGraficoA;
    private NodoGrafico nodoGraficoB;
    private ArrayList<ArcoGrafico> arcos = new ArrayList<ArcoGrafico>();
    private ArcoGrafico arcInicial;
    private Group group;

    public NodoGrafico getNodoGraficoB() {
        return nodoGraficoB;
    }

    public ArrayList<ArcoGrafico> getArcos() {
        return arcos;
    }

    public EnlaceGrafico(Group group, NodoGrafico nodoGraficoA, NodoGrafico nodoGraficoB) {
        this.nodoGraficoA = nodoGraficoA;
        this.nodoGraficoB = nodoGraficoB;

        this.nodoGraficoA.addNodoListener(this);
        this.nodoGraficoB.addNodoListener(this);

        this.group = group;

        arcInicial = new ArcoGrafico(this, this.group);
        
        arcInicial.setStartX(nodoGraficoA.getLayoutX() + nodoGraficoA.getImagen().getWidth()/2);
        arcInicial.setStartY(nodoGraficoA.getLayoutY() + nodoGraficoA.getImagen().getHeight()/2);
        arcInicial.setEndX(nodoGraficoB.getLayoutX() + nodoGraficoB.getImagen().getWidth()/2);
        arcInicial.setEndY(nodoGraficoB.getLayoutY() + nodoGraficoB.getImagen().getHeight()/2);
        arcInicial.calcularCentroXY();
        arcos.add(arcInicial);

    }

    public void addArcosInicialAlGrupo() {

        group.getChildren().add(arcInicial);

    }

    //nuevo 
    public void update() {
        if (nodoGraficoA != null && nodoGraficoB != null) 
        {
            if (nodoGraficoA.isEliminado() || nodoGraficoB.isEliminado()) {

               
                for (ArcoGrafico arcoGrafico : arcos) {
                    arcoGrafico.setEliminado(true);
                    arcoGrafico.updateArcoListeners();
                    group.getChildren().remove(arcoGrafico);
                }
                nodoGraficoA = null;
                nodoGraficoB = null;


            } else {
                arcInicial.setStartX(nodoGraficoA.getLayoutX() + nodoGraficoA.getImagen().getWidth()/2);
                arcInicial.setStartY(nodoGraficoA.getLayoutY() + nodoGraficoA.getImagen().getHeight()/2);

                QuadCurve arcoFinal = arcos.get(arcos.size() - 1);

                arcoFinal.setEndX(nodoGraficoB.getLayoutX() + nodoGraficoB.getImagen().getWidth()/2);
                arcoFinal.setEndY(nodoGraficoB.getLayoutY() + nodoGraficoB.getImagen().getHeight()/2);

            }
        }
    }
    //

    public NodoGrafico getNodoGraficoA() {
        return nodoGraficoA;
    }
}
