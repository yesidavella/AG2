package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.paint.Color;

public class EnlaceGrafico implements NodoListener,Serializable,ObjetoSeleccionable  {

    private NodoGrafico nodoGraficoA;
    private NodoGrafico nodoGraficoB;
    private ArrayList<ArcoGrafico> arcos = new ArrayList<ArcoGrafico>();
    private ArcoGrafico arcInicial,arcFinal;
    private GrupoDeDiseno grGrDeDiseño;
    private ControladorAbstractoAdminEnlace controladorAdminEnlace;
    private boolean isSeleccionado;
    private HashMap<String,String> properties;

    public EnlaceGrafico(GrupoDeDiseno group, NodoGrafico nodoGraficoA, NodoGrafico nodoGraficoB,ControladorAbstractoAdminEnlace controladorAdminEnlace) {
        
        this.nodoGraficoA = nodoGraficoA;
        this.nodoGraficoB = nodoGraficoB;
        this.controladorAdminEnlace = controladorAdminEnlace;
        this.nodoGraficoA.addNodoListener(this);
        this.nodoGraficoB.addNodoListener(this);

        this.grGrDeDiseño = group;

        arcInicial = new ArcoGrafico(this, grGrDeDiseño);
        
        arcInicial.setStartX(nodoGraficoA.getLayoutX() + nodoGraficoA.getAnchoActual()/2);
        arcInicial.setStartY(nodoGraficoA.getLayoutY() + nodoGraficoA.getAltoActual()/2);
        arcInicial.setEndX(nodoGraficoB.getLayoutX() + nodoGraficoB.getAnchoActual()/2);
        arcInicial.setEndY(nodoGraficoB.getLayoutY() + nodoGraficoB.getAltoActual()/2);
        arcInicial.calcularCentroXY();
        arcos.add(arcInicial);
 
        properties = new HashMap<String, String>();
        determinarArcoInicialYFinal();
        controladorAdminEnlace.crearEnlace(this);
        seleccionar(true);
        
    }

    public void addArcosInicialAlGrupo() {
        grGrDeDiseño.add(arcInicial);
    }

    public ArrayList<ArcoGrafico> getArcos() {
        return arcos;
    }
    
    public NodoGrafico getNodoGraficoB() {
        return nodoGraficoB;
    }

    
    @Override
    public void update()
    {
        if (nodoGraficoA != null && nodoGraficoB != null) 
        {
            if (nodoGraficoA.isEliminado() || nodoGraficoB.isEliminado()) {

                for (ArcoGrafico arcoGrafico : arcos) 
                {
                    arcoGrafico.setEliminado(true);
                    arcoGrafico.updateArcoListeners();
                    grGrDeDiseño.remove(arcoGrafico);
                }
                nodoGraficoA = null;
                nodoGraficoB = null;

            }else{
                
                arcInicial.setStartX(nodoGraficoA.getLayoutX() + nodoGraficoA.getAnchoActual()/2);
                arcInicial.setStartY(nodoGraficoA.getLayoutY() + 0.75*nodoGraficoA.getAltoActual() - nodoGraficoA.getAltoInicial()/4);

                arcFinal.setEndX(nodoGraficoB.getLayoutX() + nodoGraficoB.getAnchoActual()/2);
                arcFinal.setEndY(nodoGraficoB.getLayoutY() + 0.75*nodoGraficoB.getAltoActual()- nodoGraficoB.getAltoInicial()/4);
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
            
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void seleccionar(boolean isSeleccionado) {
        
        ObjetoSeleccionable objSeleccionado = grGrDeDiseño.getObjetoGraficoSelecionado();
        
        if(objSeleccionado!=null && objSeleccionado!=this){
            objSeleccionado.seleccionar(false);
        }
        
        this.isSeleccionado = isSeleccionado;
        
        if(isSeleccionado){
            for(ArcoGrafico arcoGrafico:arcos){
                arcoGrafico.getQuadCurve().getStyleClass().remove("arcoNoSeleccionado");
                arcoGrafico.getQuadCurve().getStyleClass().add("arcoSeleccionado");
                
                VerticeEnlaceGrafico verticeGrafico = arcoGrafico.getVerticeGrafInicial();
                
                if(verticeGrafico!= null){
                    verticeGrafico.setFill(Color.web("#44FF00"));
                    verticeGrafico.toFront();
                }
            }
            
            grGrDeDiseño.setObjetoGraficoSelecionado(this);
            controladorAdminEnlace.consultarPropiedades(this);

        }else{
            for(ArcoGrafico arcoGrafico:arcos){
                arcoGrafico.getQuadCurve().getStyleClass().remove("arcoSeleccionado");
                arcoGrafico.getQuadCurve().getStyleClass().add("arcoNoSeleccionado");
                
                VerticeEnlaceGrafico verticeGrafico = arcoGrafico.getVerticeGrafInicial();
                
                if(verticeGrafico!= null){
                    verticeGrafico.setFill(Color.AQUAMARINE);
                    verticeGrafico.toFront();
                }
            }
            grGrDeDiseño.setObjetoGraficoSelecionado(null);
        }
        
    }
    
    public boolean getSeleccionado(){
        return isSeleccionado;
    }
    
    public void determinarArcoInicialYFinal(){

        for (ArcoGrafico arco : arcos) {
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
}
