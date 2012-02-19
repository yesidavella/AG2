package com.ag2.presentation.design;

import com.ag2.presentation.IGU;
import com.ag2.presentation.ActionTypeEmun;
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
import javax.swing.JOptionPane;

public class GraphArc  implements Serializable {

    private transient QuadCurve quadCurve;
    
    private GraphDesignGroup grGrDeDiseño;
    private GraphNode nodoGraficoB;
    private ArrayList<ArcListener> arcoListeners = new ArrayList<ArcListener>();
    private boolean eliminado = false;
    private GraphLink enlaceGrafico;   
    private double startX;    
    private double startY;
    private double endX;
    private double endY;
    private double controlX;
    private double controlY;
  
    private GraphArcSeparatorPoint verticeGrafInicial,verticeGrafFinal;

    public GraphArc(GraphLink enlaceGrafico, GraphDesignGroup grGrDeDiseño)
    {
        this.grGrDeDiseño = grGrDeDiseño;
        this.enlaceGrafico = enlaceGrafico;
        this.nodoGraficoB = this.enlaceGrafico.getNodoGraficoB();
       
        initTransientObjects();
//        controlX = quadCurve.getControlX();
//        controlY = quadCurve.getControlY();
    }
  
    public GraphArcSeparatorPoint getVerticeGrafInicial() {
        return verticeGrafInicial;
    }

    public void setVerticeGrafInicial(GraphArcSeparatorPoint verticeGrafInicial) {
        this.verticeGrafInicial = verticeGrafInicial;
    }

    public GraphArcSeparatorPoint getVerticeGrafFinal() {
        return verticeGrafFinal;
    }

    public void setVerticeGrafFinal(GraphArcSeparatorPoint verticeGrafFinal) {
        this.verticeGrafFinal = verticeGrafFinal;
    }
       

    public void initTransientObjects()
    {
        quadCurve = new QuadCurve(); 
        quadCurve.setFill(null);
        quadCurve.setStrokeWidth(2);
        quadCurve.setStrokeType(StrokeType.CENTERED);
        quadCurve.setStroke(Color.LIGHTGREEN);
        
        DropShadow drpShdResplandorArco = new DropShadow();
        drpShdResplandorArco.setColor(Color.WHITESMOKE);
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
                GraphArc quadCurveFuente = GraphArc.this;
                

                ActionTypeEmun tipoDeBotonSeleccionado = IGU.getEstadoTipoBoton();

                if (tipoDeBotonSeleccionado == ActionTypeEmun.ADICIONAR_VERTICE || tipoDeBotonSeleccionado == ActionTypeEmun.ELIMINAR) {
                    quadCurveFuente.getQuadCurve().setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                } else if (tipoDeBotonSeleccionado == ActionTypeEmun.PUNTERO) {
                    quadCurveFuente.getQuadCurve().setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                }
            }
        });
    }

    private void establecerEventoMouseDragged() {
        quadCurve.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (IGU.getEstadoTipoBoton() == ActionTypeEmun.PUNTERO) {
                    double dragX = me.getX();
                    double dragY = me.getY();
                    GraphArc arcoGrafico = GraphArc.this ;
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

                if(IGU.getEstadoTipoBoton() == ActionTypeEmun.PUNTERO){
                    if(!enlaceGrafico.getSeleccionado()){
                        enlaceGrafico.seleccionar(true);
                    }else{
                        enlaceGrafico.seleccionar(false);
                    }
                }
                
                if (IGU.getEstadoTipoBoton() == ActionTypeEmun.ADICIONAR_VERTICE) {
                    
                    GraphArc arcGrafFuente = GraphArc.this;
                    GraphArc arcGrafNuevo = new GraphArc(arcGrafFuente.getEnlaceGrafico(), arcGrafFuente.getGroup());

                    arcGrafNuevo.setStartX(clickX);
                    arcGrafNuevo.setStartY(clickY);
                    arcGrafNuevo.setEndX(arcGrafFuente.getEndX());
                    arcGrafNuevo.setEndY(arcGrafFuente.getEndY());

                    arcGrafFuente.setEndX(clickX);
                    arcGrafFuente.setEndY(clickY);

                    arcGrafFuente.calcularCentroXY();
                    arcGrafNuevo.calcularCentroXY();

                    GraphArcSeparatorPoint verticeNuevo = new GraphArcSeparatorPoint(grGrDeDiseño, arcGrafFuente, arcGrafNuevo, clickX, clickY);
                    
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
                    
                } else if (IGU.getEstadoTipoBoton() == ActionTypeEmun.ELIMINAR) {

                    GraphNode nodoA = enlaceGrafico.getNodoGraficoA();
                    GraphNode nodoB = enlaceGrafico.getNodoGraficoB();

                    nodoA.removeNodoListener(enlaceGrafico);
                    nodoB.removeNodoListener(enlaceGrafico);

                    nodoA.setCantidadDeEnlaces((short) (nodoA.getCantidadDeEnlaces() - 1));
                    nodoB.setCantidadDeEnlaces((short) (nodoB.getCantidadDeEnlaces() - 1));

                    for (GraphArc arcoGrafico : enlaceGrafico.getArcos()) {
                        arcoGrafico.setEliminado(true);
                        arcoGrafico.updateArcoListeners();

                        grGrDeDiseño.remove(arcoGrafico);
                    }
                    
                    if(!enlaceGrafico.removeGraphLink()){
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar los puertos de los Nodos Phosphorous satisfactoriamente.");
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

    public GraphLink getEnlaceGrafico() {
        return enlaceGrafico;
    }

    public void addNodoListener(ArcListener arcoListener) {
        arcoListeners.add(arcoListener);
    }

    public void revomeNodoListener(ArcListener arcoListener) {
        arcoListeners.remove(arcoListener);
    }

    public void updateArcoListeners() {
        for (ArcListener arcoListener : arcoListeners) {
            arcoListener.updateArco();
        }
    }

    public GraphDesignGroup getGroup() {
        return grGrDeDiseño;
    }

    public GraphNode getNodoGraficoB() {
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
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void writeObject(ObjectOutputStream stream){
        try {
            
            stream.defaultWriteObject();
            System.out.println("Write : Arco" );
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
