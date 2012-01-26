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

        arcInicial = new ArcoGrafico(this, this.grGrDeDiseño);
        
        arcInicial.setPosIniX(nodoGraficoA.getLayoutX() + nodoGraficoA.getAnchoActual()/2);
        arcInicial.setPosIniY(nodoGraficoA.getLayoutY() + nodoGraficoA.getAltoActual()/2);
        arcInicial.setPosFinX(nodoGraficoB.getLayoutX() + nodoGraficoB.getAnchoActual()/2);
        arcInicial.setPosFinY(nodoGraficoB.getLayoutY() + nodoGraficoB.getAltoActual()/2);
        arcInicial.calcularCentroXY();
        arcos.add(arcInicial);
 
        properties = new HashMap<String, String>();
        determinarArcoInicialYFinal();
        controladorAdminEnlace.crearEnlace(this);
        seleccionar(true);
        
    }

    public void addArcosInicialAlGrupo() {
        grGrDeDiseño.getChildren().add(arcInicial);
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
                    grGrDeDiseño.getChildren().remove(arcoGrafico);
                }
                nodoGraficoA = null;
                nodoGraficoB = null;

            }else{
                
                arcInicial.setPosIniX(nodoGraficoA.getLayoutX() + nodoGraficoA.getAnchoActual()/2);
                arcInicial.setPosIniY(nodoGraficoA.getLayoutY() + 
                        ((nodoGraficoA.getAltoActual()>nodoGraficoA.getAltoInicial())?(nodoGraficoA.getAltoInicial()/4)+(nodoGraficoA.getAltoActual()/4):nodoGraficoA.getAltoInicial()/2));

                arcFinal.setPosFinX(nodoGraficoB.getLayoutX() + nodoGraficoB.getAnchoActual()/2);
                arcFinal.setPosFinY(nodoGraficoB.getLayoutY() + 
                        ((nodoGraficoB.getAltoActual()>nodoGraficoB.getAltoInicial())?(nodoGraficoB.getAltoInicial()/4)+(nodoGraficoB.getAltoActual()/4):+nodoGraficoB.getAltoInicial()/2));
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
                grGrDeDiseño.getChildren().add(arcoGrafico);
            }    
            
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
                arcoGrafico.getStyleClass().remove("arcoNoSeleccionado");
                arcoGrafico.getStyleClass().add("arcoSeleccionado");
                
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
                arcoGrafico.getStyleClass().remove("arcoSeleccionado");
                arcoGrafico.getStyleClass().add("arcoNoSeleccionado");
                
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
