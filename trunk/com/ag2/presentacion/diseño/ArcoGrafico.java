package com.ag2.presentacion.diseño;

import com.ag2.presentacion.Main;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.StrokeType;

public class ArcoGrafico extends QuadCurve implements Serializable {

    private GrupoDeDiseno group;
    private NodoGrafico nodoGraficoB;
    private ArrayList<ArcoListener> arcoListeners  = new ArrayList<ArcoListener>(); 
    private boolean eliminado= false;
    private EnlaceGrafico enlaceGrafico;
    private double posIniX; 
    private double posIniY;
    private double centroX;
    private double centroY;

    public double getCentroX() {
        return centroX;
    }

    public void setCentroX(double centroX) 
    {
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

    public double getPosFinX()
    {
        
        return posFinX;
    }

    public void setPosFinX(double posFinX)
    {
        setEndX(posFinX);
        this.posFinX = posFinX;       
    }

    public double getPosFinY() {
        return posFinY;
    }

    public void setPosFinY(double posFinY)
    {
        setEndY(posFinY);
        this.posFinY = posFinY;
    }

    public double getPosIniX() {
        return posIniX;
    }

    public void setPosIniX(double posIniX)
    {
        setStartX(posIniX);
        this.posIniX = posIniX;
    }

    public double getPosIniY() {
        return posIniY;
    }

    public void setPosIniY(double posIniY) 
    {
         setStartY(posIniY);
        this.posIniY = posIniY;
    }
    private double posFinX; 
    private double posFinY; 

    public ArcoGrafico(EnlaceGrafico enlaceGrafico, GrupoDeDiseno group)
    {
        this.group = group;
        this.enlaceGrafico = enlaceGrafico;
        this.nodoGraficoB = this.enlaceGrafico.getNodoGraficoB();       
        establecerConfigInicial();

    }

    private void establecerConfigInicial() {
        setFill(null);
        setStrokeWidth(2);
        setStroke(Color.AQUAMARINE);
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
        public void calcularCentroXY()
    {
        double x1 = getStartX();
        double y1 = getStartY();

        double x2 = getEndX();
        double y2 = getEndY();

        centroX = (x1 + x2) / 2;
        centroY = (y1 + y2) / 2;

        setControlX(centroX);
        setControlY(centroY);     
        
    }        

    private void establecerEventoMouseDragged(QuadCurve arco) {
        arco.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (Main.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    double dragX = me.getX();
                    double dragY = me.getY();
                    ArcoGrafico arcoGrafico = (ArcoGrafico)me.getSource();
                    arcoGrafico.setCentroX(dragX); 
                    arcoGrafico.setCentroY(dragY); 
                   
                }
            }
        });
    }

    private void establecerEnventoClicked(QuadCurve arco) {
        arco.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                double clickX = mouseEvent.getX();
                double clickY = mouseEvent.getY();
                if (Main.getEstadoTipoBoton() == TiposDeBoton.ADICIONAR_VERTICE) 
                {
                    ArcoGrafico quadCurveFuente = (ArcoGrafico) mouseEvent.getSource();           
                    ArcoGrafico quadCurveNueva = new ArcoGrafico(quadCurveFuente.getEnlaceGrafico(),quadCurveFuente.getGroup() );

                    quadCurveNueva.setPosIniX(clickX);
                    quadCurveNueva.setPosIniY(clickY);
                    quadCurveNueva.setPosFinX(quadCurveFuente.getPosFinX());
                    quadCurveNueva.setPosFinY(quadCurveFuente.getPosFinY());

                    quadCurveFuente.setPosFinX(clickX);
                    quadCurveFuente.setPosFinY(clickY);              
                   
                    quadCurveFuente.calcularCentroXY(); 
                    quadCurveNueva.calcularCentroXY();                 
                                       
                    VerticeEnlaceGrafico separadorGrafico = new VerticeEnlaceGrafico(quadCurveFuente,quadCurveNueva,clickX,clickY   );              

                    group.getChildren().addAll(quadCurveNueva, separadorGrafico);
                    nodoGraficoB.toFront();
                    enlaceGrafico.getArcos().add(quadCurveNueva);
                   
                }
                else if(Main.getEstadoTipoBoton() == TiposDeBoton.ELIMINAR)
                {
                  
                   enlaceGrafico.getNodoGraficoA().removeNodoListener(enlaceGrafico);
                   enlaceGrafico.getNodoGraficoB().removeNodoListener(enlaceGrafico);
                   
                   for(ArcoGrafico arcoGrafico: enlaceGrafico.getArcos() )
                   {
                       arcoGrafico.setEliminado(true); 
                       arcoGrafico.updateArcoListeners(); 
                   
                       group.getChildren().remove(arcoGrafico);
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
    
    public void addNodoListener(ArcoListener arcoListener )
    {
        arcoListeners.add(arcoListener);
    }  
    public void revomeNodoListener(ArcoListener arcoListener )
    {
        arcoListeners.remove(arcoListener);
    }    
    
    public void updateArcoListeners()
    {
        for( ArcoListener arcoListener : arcoListeners)
        {
            arcoListener.update();
        }     
        
    }        

    public GrupoDeDiseno getGroup() {
        return group;
    }
     public NodoGrafico getNodoGraficoB() {
        return nodoGraficoB;
    }

    private void establecerEventoOnMouseEntered(ArcoGrafico arco) {


        arco.setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                ArcoGrafico quadCurveFuente = (ArcoGrafico) mouseEvent.getSource();

                TiposDeBoton tipoDeBotonSeleccionado = Main.getEstadoTipoBoton();

                if (tipoDeBotonSeleccionado == TiposDeBoton.ADICIONAR_VERTICE || tipoDeBotonSeleccionado == TiposDeBoton.ELIMINAR) {
                    quadCurveFuente.setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                } else if (tipoDeBotonSeleccionado == TiposDeBoton.PUNTERO) {
                    quadCurveFuente.setCursor(Cursor.CROSSHAIR);
                } else {
                    quadCurveFuente.setCursor(tipoDeBotonSeleccionado.getImagenCursor());
                }
            }
        });
    }
    private void readObject(ObjectInputStream inputStream)
    {
        try 
        {
            inputStream.defaultReadObject();  
            setStartX(posIniX);
            setStartY(posIniY);
            setEndX(posFinX);
            setEndY(posFinY);           
            setControlX(centroX);
            setControlY(centroY);  
            establecerConfigInicial();
            
            
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
    }
}
