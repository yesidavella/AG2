package com.ag2.presentacion.diseño;

import com.ag2.presentacion.IGU;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.IOException;
import java.io.ObjectInputStream;
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

public class ArcoGrafico extends QuadCurve implements Serializable {

    private GrupoDeDiseno grGrDeDiseño;
    private NodoGrafico nodoGraficoB;
    private ArrayList<ArcoListener> arcoListeners = new ArrayList<ArcoListener>();
    private boolean eliminado = false;
    private EnlaceGrafico enlaceGrafico;
    private double posIniX;
    private double posIniY;
    private double centroX;
    private double centroY;
    private double posFinX;
    private double posFinY;
    private VerticeEnlaceGrafico verticeGrafInicial,verticeGrafFinal;

    public ArcoGrafico(EnlaceGrafico enlaceGrafico, GrupoDeDiseno grGrDeDiseño) {
        this.grGrDeDiseño = grGrDeDiseño;
        this.enlaceGrafico = enlaceGrafico;

        this.nodoGraficoB = this.enlaceGrafico.getNodoGraficoB();
        establecerConfigInicial();
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
    
    public double getCentroX() {
        return centroX;
    }

    public void setCentroX(double centroX) {
        setControlX(centroX);
        this.centroX = centroX;
    }

    public double getCentroY() {
        return centroY;
    }

    public void setCentroY(double centroY) {
        setControlY(centroY);
        this.centroY = centroY;
    }

    public double getPosFinX() {

        return posFinX;
    }

    public void setPosFinX(double posFinX) {
        setEndX(posFinX);
        this.posFinX = posFinX;
    }

    public double getPosFinY() {
        return posFinY;
    }

    public void setPosFinY(double posFinY) {
        setEndY(posFinY);
        this.posFinY = posFinY;
    }

    public double getPosIniX() {
        return posIniX;
    }

    public void setPosIniX(double posIniX) {
        setStartX(posIniX);
        this.posIniX = posIniX;
    }

    public double getPosIniY() {
        return posIniY;
    }

    public void setPosIniY(double posIniY) {
        setStartY(posIniY);
        this.posIniY = posIniY;
    }

    private void establecerConfigInicial() {
        setFill(null);
        setStrokeWidth(2);
        setStrokeType(StrokeType.CENTERED);

        DropShadow drpShdResplandorArco = new DropShadow();
        drpShdResplandorArco.setColor(Color.LIGHTGREY);
        drpShdResplandorArco.setSpread(0.5);
        drpShdResplandorArco.setWidth(30);
        drpShdResplandorArco.setHeight(30);
        setEffect(drpShdResplandorArco);

        establecerEventoMouseDragged(this);
        establecerEnventoClicked(this);
        establecerEventoOnMouseEntered(this);
    }

    public void calcularCentroXY() {
        double x1 = getStartX();
        double y1 = getStartY();

        double x2 = getEndX();
        double y2 = getEndY();

        centroX = (x1 + x2) / 2;
        centroY = (y1 + y2) / 2;

        setControlX(centroX);
        setControlY(centroY);

    }

    private void establecerEventoOnMouseEntered(ArcoGrafico arco) {

        arco.setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                ArcoGrafico quadCurveFuente = (ArcoGrafico) mouseEvent.getSource();

                TiposDeBoton tipoDeBotonSeleccionado = IGU.getEstadoTipoBoton();

                if (tipoDeBotonSeleccionado == TiposDeBoton.ADICIONAR_VERTICE || tipoDeBotonSeleccionado == TiposDeBoton.ELIMINAR) {
                    quadCurveFuente.setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                } else if (tipoDeBotonSeleccionado == TiposDeBoton.PUNTERO) {
                    quadCurveFuente.setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                }
            }
        });
    }

    private void establecerEventoMouseDragged(QuadCurve arco) {
        arco.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (IGU.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    double dragX = me.getX();
                    double dragY = me.getY();
                    ArcoGrafico arcoGrafico = (ArcoGrafico) me.getSource();
                    arcoGrafico.setCentroX(dragX);
                    arcoGrafico.setCentroY(dragY);
                    
                    if(!enlaceGrafico.getSeleccionado()){
                        enlaceGrafico.seleccionar(true);
                    }
                    
                }
            }
        });
    }

    private void establecerEnventoClicked(QuadCurve arco) {
        arco.setOnMouseClicked(new EventHandler<MouseEvent>() {

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
                    
                    ArcoGrafico arcGrafFuente = (ArcoGrafico)mouseEvent.getSource();
                    ArcoGrafico arcGrafNuevo = new ArcoGrafico(arcGrafFuente.getEnlaceGrafico(), arcGrafFuente.getGroup());

                    arcGrafNuevo.setPosIniX(clickX);
                    arcGrafNuevo.setPosIniY(clickY);
                    arcGrafNuevo.setPosFinX(arcGrafFuente.getPosFinX());
                    arcGrafNuevo.setPosFinY(arcGrafFuente.getPosFinY());

                    arcGrafFuente.setPosFinX(clickX);
                    arcGrafFuente.setPosFinY(clickY);

                    arcGrafFuente.calcularCentroXY();
                    arcGrafNuevo.calcularCentroXY();

                    VerticeEnlaceGrafico verticeNuevo = new VerticeEnlaceGrafico(arcGrafFuente, arcGrafNuevo, clickX, clickY);
                    
                    if( arcGrafFuente.getVerticeGrafFinal()!=null){
                        arcGrafFuente.getVerticeGrafFinal().setArcoGraficoA(arcGrafNuevo);
                        arcGrafNuevo.setVerticeGrafFinal(arcGrafFuente.getVerticeGrafFinal());
                    }
                    
                    arcGrafNuevo.setVerticeGrafInicial(verticeNuevo);
                    arcGrafFuente.setVerticeGrafFinal(verticeNuevo);
                    
                    grGrDeDiseño.getChildren().addAll(arcGrafNuevo,verticeNuevo);
                    nodoGraficoB.toFront();
                    
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

                        grGrDeDiseño.getChildren().remove(arcoGrafico);
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
      private void writeObject(java.io.ObjectOutputStream outputStream)
    {
        try {
            posIniX = getStartX(); 
            posIniY = getStartY(); 
            posFinX = getEndX(); 
            posFinY = getEndY(); 
            centroX = getControlX();        
            centroY = getCentroY(); 
                    
            outputStream.defaultWriteObject();
          
        } catch (IOException ex) {
            Logger.getLogger(NodoGrafico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
            setStartX(posIniX);
            setStartY(posIniY);
            setEndX(posFinX);
            setEndY(posFinY);
            setControlX(centroX);
            setControlY(centroY);
            establecerConfigInicial();
            toFront();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
