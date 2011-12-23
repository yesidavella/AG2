package com.ag2.presentacion.diseño;

import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class EnlaceGrafico implements NodoListener,  Serializable  {

    private NodoGrafico nodoGraficoA;
    private NodoGrafico nodoGraficoB;
    private ArrayList<ArcoGrafico> arcos = new ArrayList<ArcoGrafico>();
    private ArcoGrafico arcInicial;
    private GrupoDeDiseno group;

    public EnlaceGrafico(GrupoDeDiseno group, NodoGrafico nodoGraficoA, NodoGrafico nodoGraficoB) {
        this.nodoGraficoA = nodoGraficoA;
        this.nodoGraficoB = nodoGraficoB;

        this.nodoGraficoA.addNodoListener(this);
        this.nodoGraficoB.addNodoListener(this);

        this.group = group;

        arcInicial = new ArcoGrafico(this, this.group);
        
        arcInicial.setPosIniX(nodoGraficoA.getLayoutX() + nodoGraficoA.getAncho()/2);
        arcInicial.setPosIniY(nodoGraficoA.getLayoutY() + nodoGraficoA.getAlto()/2);
        arcInicial.setPosFinX(nodoGraficoB.getLayoutX() + nodoGraficoB.getAncho()/2);
        arcInicial.setPosFinY(nodoGraficoB.getLayoutY() + nodoGraficoB.getAlto()/2);
        arcInicial.calcularCentroXY();
        arcos.add(arcInicial);
        
    }

    public void addArcosInicialAlGrupo() {
        group.getChildren().add(arcInicial);
    }

    public ArrayList<ArcoGrafico> getArcos() {
        return arcos;
    }
    
    public NodoGrafico getNodoGraficoB() {
        return nodoGraficoB;
    }

    //nuevo 
    public void update() {
        if (nodoGraficoA != null && nodoGraficoB != null) 
        {
            if (nodoGraficoA.isEliminado() || nodoGraficoB.isEliminado()) {

                for (ArcoGrafico arcoGrafico : arcos) 
                {
                    arcoGrafico.setEliminado(true);
                    arcoGrafico.updateArcoListeners();
                    group.getChildren().remove(arcoGrafico);
                }
                nodoGraficoA = null;
                nodoGraficoB = null;

            } 
            else{
                arcInicial.setPosIniX(nodoGraficoA.getLayoutX() + nodoGraficoA.getAncho()/2);
                arcInicial.setPosIniY(nodoGraficoA.getLayoutY() + nodoGraficoA.getAlto()/2);

                ArcoGrafico arcoFinal = arcos.get(arcos.size() - 1);

                arcoFinal.setPosFinX(nodoGraficoB.getLayoutX() + nodoGraficoB.getAncho()/2);
                arcoFinal.setPosFinY(nodoGraficoB.getLayoutY() + nodoGraficoB.getAlto()/2);
            }
        }
    }

    public NodoGrafico getNodoGraficoA() {
        return nodoGraficoA;
    }
    
    private void readObject(ObjectInputStream inputStream) {
        try 
        {
            inputStream.defaultReadObject();
            for(ArcoGrafico arcoGrafico: arcos)
            {
                group.getChildren().add(arcoGrafico);
            }    
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
