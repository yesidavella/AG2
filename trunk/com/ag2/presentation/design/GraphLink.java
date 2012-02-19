package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.MatchCoupleObjectContainer;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.paint.Color;

public class GraphLink implements NodeListener,Serializable,Selectable  {

    private GraphNode nodoGraficoA;
    private GraphNode nodoGraficoB;
    private ArrayList<GraphArc> arcos = new ArrayList<GraphArc>();
    private GraphArc arcInicial,arcFinal;
    private GraphDesignGroup grGrDeDiseño;
    private LinkAdminAbstractController adminLinkController;
    private boolean isSeleccionado;
    private HashMap<String,String> properties;

    public GraphLink(GraphDesignGroup group, GraphNode nodoGraficoA, GraphNode nodoGraficoB,LinkAdminAbstractController controladorAdminEnlace) {
        
        this.nodoGraficoA = nodoGraficoA;
        this.nodoGraficoB = nodoGraficoB;
        this.adminLinkController = controladorAdminEnlace;
        this.nodoGraficoA.addNodoListener(this);
        this.nodoGraficoB.addNodoListener(this);

        this.grGrDeDiseño = group;

        arcInicial = new GraphArc(this, grGrDeDiseño);
        
        arcInicial.setStartX(nodoGraficoA.getLayoutX() + nodoGraficoA.getAnchoActual()/2);
        arcInicial.setStartY(nodoGraficoA.getLayoutY() + nodoGraficoA.getAltoActual()/2);
        arcInicial.setEndX(nodoGraficoB.getLayoutX() + nodoGraficoB.getAnchoActual()/2);
        arcInicial.setEndY(nodoGraficoB.getLayoutY() + nodoGraficoB.getAltoActual()/2);
        arcInicial.calcularCentroXY();
        arcos.add(arcInicial);
 
        properties = new HashMap<String, String>();
        determinarArcoInicialYFinal();
        controladorAdminEnlace.createLink(this);
        seleccionar(true);
        
    }

    public void addArcosInicialAlGrupo() {
        grGrDeDiseño.add(arcInicial);
    }

    public ArrayList<GraphArc> getArcos() {
        return arcos;
    }
    
    public GraphNode getNodoGraficoB() {
        return nodoGraficoB;
    }

    
    @Override
    public void update()
    {
        if (nodoGraficoA != null && nodoGraficoB != null) 
        {
            if (nodoGraficoA.isEliminado() || nodoGraficoB.isEliminado()) {

                for (GraphArc arcoGrafico : arcos) 
                {
                    arcoGrafico.setEliminado(true);
                    arcoGrafico.updateArcoListeners();
                    grGrDeDiseño.remove(arcoGrafico);
                }
                nodoGraficoA = null;
                nodoGraficoB = null;

                removeGraphLink();

            }else{
                
                arcInicial.setStartX(nodoGraficoA.getLayoutX() + nodoGraficoA.getAnchoActual()/2);
                arcInicial.setStartY(nodoGraficoA.getLayoutY() + 0.75*nodoGraficoA.getAltoActual() - nodoGraficoA.getAltoInicial()/4);

                arcFinal.setEndX(nodoGraficoB.getLayoutX() + nodoGraficoB.getAnchoActual()/2);
                arcFinal.setEndY(nodoGraficoB.getLayoutY() + 0.75*nodoGraficoB.getAltoActual()- nodoGraficoB.getAltoInicial()/4);
            }
        }
    }

    public GraphNode getNodoGraficoA() {
        return nodoGraficoA;
    }
    
    
    private void readObject(ObjectInputStream inputStream) {
        try 
        {
            inputStream.defaultReadObject(); 
        
            
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void seleccionar(boolean isSeleccionado) {
        
        Selectable objSeleccionado = grGrDeDiseño.getObjetoGraficoSelecionado();
        
        if(objSeleccionado!=null && objSeleccionado!=this){
            objSeleccionado.seleccionar(false);
        }
        
        this.isSeleccionado = isSeleccionado;
        
        if(isSeleccionado){
            for(GraphArc arcoGrafico:arcos){
                arcoGrafico.getQuadCurve().getStyleClass().remove("arcoNoSeleccionado");
                arcoGrafico.getQuadCurve().getStyleClass().add("arcoSeleccionado");
                
                GraphArcSeparatorPoint verticeGrafico = arcoGrafico.getVerticeGrafInicial();
                
                if(verticeGrafico!= null){
                    verticeGrafico.getCircle().setFill(Color.web("#44FF00"));
                    verticeGrafico.getCircle().toFront();
                }
            }
            
            grGrDeDiseño.setObjetoGraficoSelecionado(this);
            adminLinkController.queryProperty(this);

        }else{
            for(GraphArc arcoGrafico:arcos){
                arcoGrafico.getQuadCurve().getStyleClass().remove("arcoSeleccionado");
                arcoGrafico.getQuadCurve().getStyleClass().add("arcoNoSeleccionado");
                
                GraphArcSeparatorPoint verticeGrafico = arcoGrafico.getVerticeGrafInicial();
                
                if(verticeGrafico!= null){
                    verticeGrafico.getCircle().setFill(Color.LIGHTGREEN);
                    verticeGrafico.getCircle().toFront();
                }
            }
            grGrDeDiseño.setObjetoGraficoSelecionado(null);
        }
        
    }
    
    public boolean getSeleccionado(){
        return isSeleccionado;
    }
    
    public void determinarArcoInicialYFinal(){

        for (GraphArc arco : arcos) {
            /*Para saber por q esto se puede asegurar, vease ArcoGrafico metodo setOnMouseClicked
             * cuando el tipo de boton seleccionado es TiposDeBoton.ADICIONAR_VERTICE
            */
            if (arco.getVerticeGrafInicial() == null) {
                arcInicial = arco;
            }
            
            if (arco.getVerticeGrafFinal() == null) {
                arcFinal = arco;
            }
        }
    }
    
    public HashMap<String, String> getProperties() {
        return properties;
    }
    
    public boolean removeGraphLink(){
        return adminLinkController.removeLink(this);
    }
}
