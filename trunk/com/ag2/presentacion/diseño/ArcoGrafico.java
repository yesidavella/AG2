package com.ag2.presentacion.diseño;

import com.ag2.presentacion.IGU;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.StrokeType;

public class ArcoGrafico  implements Serializable {

    private transient QuadCurve quadCurve;
    
    private GrupoDeDiseno grGrDeDiseño;
    private NodoGrafico nodoGraficoB;
    private ArrayList<ArcoListener> arcoListeners = new ArrayList<ArcoListener>();
    private boolean eliminado = false;
    private EnlaceGrafico enlaceGrafico;   
    private double startX;    
    private double startY;
    private double endX;
    private double endY;
    private double controlX;
    private double controlY;
  
    
    private VerticeEnlaceGrafico verticeGrafInicial,verticeGrafFinal;

    
    public ArcoGrafico(EnlaceGrafico enlaceGrafico, GrupoDeDiseno grGrDeDiseño)
    {
        this.grGrDeDiseño = grGrDeDiseño;
        this.enlaceGrafico = enlaceGrafico;
        this.nodoGraficoB = this.enlaceGrafico.getNodoGraficoB();
       
        initTransientObjects();
//        controlX = quadCurve.getControlX();
//        controlY = quadCurve.getControlY();
    }
  
    public VerticeEnlaceGrafico getVerticeGrafInicial() {
        return verticeGrafInicial;
    }

    public void setVerticeGrafInicial(VerticeEnlaceGrafico verticeGrafInicial) {
        this.verticeGrafInicial = verticeGrafInicial;
    }

    public VerticeEnlaceGrafico getVerticeGrafFinal() {
        return verticeGrafFinal;
    }

    public void setVerticeGrafFinal(VerticeEnlaceGrafico verticeGrafFinal) {
        this.verticeGrafFinal = verticeGrafFinal;
    }
       

    private void initTransientObjects()
    {
        quadCurve = new QuadCurve(); 
        quadCurve.setFill(null);
        quadCurve.setStrokeWidth(2);
        quadCurve.setStrokeType(StrokeType.CENTERED);
        quadCurve.setStroke(Color.LIGHTGREEN);
        
        
        
        DropShadow drpShdResplandorArco = new DropShadow();
        drpShdResplandorArco.setColor(Color.LIGHTGREY);
        drpShdResplandorArco.setSpread(0.5);
        drpShdResplandorArco.setWidth(30);
        drpShdResplandorArco.setHeight(30);
        quadCurve.setEffect(drpShdResplandorArco);

        establecerEventoMouseDragged();
        establecerEnventoClicked();
        establecerEventoOnMouseEntered();
        
       
    }

    public void calcularCentroXY() {
        double x1 = getStartX();
        double y1 = getStartY();

        double x2 = getEndX();
        double y2 = getEndY();

        controlX = (x1 + x2) / 2;
        controlY = (y1 + y2) / 2;

        setControlX(controlX);
        setControlY(controlY);

    }

    private void establecerEventoOnMouseEntered() {

        quadCurve.setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                ArcoGrafico quadCurveFuente = ArcoGrafico.this;
                

                TiposDeBoton tipoDeBotonSeleccionado = IGU.getEstadoTipoBoton();

                if (tipoDeBotonSeleccionado == TiposDeBoton.ADICIONAR_VERTICE || tipoDeBotonSeleccionado == TiposDeBoton.ELIMINAR) {
                    quadCurveFuente.getQuadCurve().setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                } else if (tipoDeBotonSeleccionado == TiposDeBoton.PUNTERO) {
                    quadCurveFuente.getQuadCurve().setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                }
            }
        });
    }

    private void establecerEventoMouseDragged() {
        quadCurve.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (IGU.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    double dragX = me.getX();
                    double dragY = me.getY();
                    ArcoGrafico arcoGrafico = ArcoGrafico.this ;
                    arcoGrafico.setControlX(dragX);
                    arcoGrafico.setControlY(dragY);
                    
                    if(!enlaceGrafico.getSeleccionado()){
                        enlaceGrafico.seleccionar(true);
                    }
                    
                }
            }
        });
    }

    private void establecerEnventoClicked() {
        quadCurve.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                double clickX = mouseEvent.getX();
                double clickY = mouseEvent.getY();

                if(IGU.getEstadoTipoBoton() == TiposDeBoton.PUNTERO){
                    if(!enlaceGrafico.getSeleccionado()){
                        enlaceGrafico.seleccionar(true);
                    }else{
                        enlaceGrafico.seleccionar(false);
                    }
                }
                
                if (IGU.getEstadoTipoBoton() == TiposDeBoton.ADICIONAR_VERTICE) {
                    
                    ArcoGrafico arcGrafFuente = ArcoGrafico.this;
                    ArcoGrafico arcGrafNuevo = new ArcoGrafico(arcGrafFuente.getEnlaceGrafico(), arcGrafFuente.getGroup());

                    arcGrafNuevo.setStartX(clickX);
                    arcGrafNuevo.setStartY(clickY);
                    arcGrafNuevo.setEndX(arcGrafFuente.getEndX());
                    arcGrafNuevo.setEndY(arcGrafFuente.getEndY());

                    arcGrafFuente.setEndX(clickX);
                    arcGrafFuente.setEndY(clickY);

                    arcGrafFuente.calcularCentroXY();
                    arcGrafNuevo.calcularCentroXY();

                    VerticeEnlaceGrafico verticeNuevo = new VerticeEnlaceGrafico(arcGrafFuente, arcGrafNuevo, clickX, clickY);
                    
                    if( arcGrafFuente.getVerticeGrafFinal()!=null){
                        arcGrafFuente.getVerticeGrafFinal().setArcoGraficoA(arcGrafNuevo);
                        arcGrafNuevo.setVerticeGrafFinal(arcGrafFuente.getVerticeGrafFinal());
                    }
                    
                    arcGrafNuevo.setVerticeGrafInicial(verticeNuevo);
                    arcGrafFuente.setVerticeGrafFinal(verticeNuevo);
                    
                    grGrDeDiseño.add(arcGrafNuevo);
                    grGrDeDiseño.add(verticeNuevo); 
                    nodoGraficoB.getGroup().toFront();
                    
                    enlaceGrafico.getArcos().add(arcGrafNuevo);
                    enlaceGrafico.determinarArcoInicialYFinal();
                    
                    enlaceGrafico.seleccionar(true);
                    
                } else if (IGU.getEstadoTipoBoton() == TiposDeBoton.ELIMINAR) {

                    NodoGrafico nodoA = enlaceGrafico.getNodoGraficoA();
                    NodoGrafico nodoB = enlaceGrafico.getNodoGraficoB();

                    nodoA.removeNodoListener(enlaceGrafico);
                    nodoB.removeNodoListener(enlaceGrafico);

                    nodoA.setCantidadDeEnlaces((short) (nodoA.getCantidadDeEnlaces() - 1));
                    nodoB.setCantidadDeEnlaces((short) (nodoB.getCantidadDeEnlaces() - 1));

                    for (ArcoGrafico arcoGrafico : enlaceGrafico.getArcos()) {
                        arcoGrafico.setEliminado(true);
                        arcoGrafico.updateArcoListeners();

                        grGrDeDiseño.remove(arcoGrafico);
                    }
                }
            }
        });
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public EnlaceGrafico getEnlaceGrafico() {
        return enlaceGrafico;
    }

    public void addNodoListener(ArcoListener arcoListener) {
        arcoListeners.add(arcoListener);
    }

    public void revomeNodoListener(ArcoListener arcoListener) {
        arcoListeners.remove(arcoListener);
    }

    public void updateArcoListeners() {
        for (ArcoListener arcoListener : arcoListeners) {
            arcoListener.updateArco();
        }
    }

    public GrupoDeDiseno getGroup() {
        return grGrDeDiseño;
    }

    public NodoGrafico getNodoGraficoB() {
        return nodoGraficoB;
    }
     

    public double getControlX() {
        return controlX;
    }

    public void setControlX(double controlX)
    {
        this.controlX = controlX;
        quadCurve.setControlX(controlX);
    }

    public double getControlY() {
        return controlY;
    }

    public void setControlY(double controlY) {
        this.controlY = controlY;
        quadCurve.setControlY(controlY);
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
        quadCurve.setEndX(endX);
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
        quadCurve.setEndY(endY); 
    }

    public QuadCurve getQuadCurve() {
        return quadCurve;
    }

   

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
        quadCurve.setStartX(startX); 
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
        quadCurve.setStartY(startY); 
    }
      

    private void readObject(ObjectInputStream inputStream) {
        try {
            
            inputStream.defaultReadObject();
            initTransientObjects();
            
            quadCurve.setStartX(startX);
            quadCurve.setStartY(startY); 
            
            quadCurve.setEndX(endX);
            quadCurve.setEndY(endY); 
            
            quadCurve.setControlX(controlX);
            quadCurve.setControlY(controlY);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void writeObject(ObjectOutputStream stream){
        try {
            stream.defaultWriteObject();
        } catch (IOException ex) {
            Logger.getLogger(ArcoGrafico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
