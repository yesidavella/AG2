package com.ag2.presentacion.diseño;

import com.ag2.presentacion.Main;
import com.ag2.presentacion.TiposDeBoton;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.StrokeType;

public class ArcoGrafico extends QuadCurve {

    private Group group;
    private NodoGrafico nodoGraficoB;
    private ArrayList<ArcoListener> arcoListeners  = new ArrayList<ArcoListener>(); 
    private boolean eliminado= false;
    private EnlaceGrafico enlaceGrafico;

    public ArcoGrafico(EnlaceGrafico enlaceGrafico, Group group)
    {
        this.group = group;
        this.enlaceGrafico = enlaceGrafico;
        this.nodoGraficoB = this.enlaceGrafico.getNodoGraficoB();       
        
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

        double centroX = (x1 + x2) / 2;
        double centroY = (y1 + y2) / 2;

        setControlX(centroX);
        setControlY(centroY);     
        
    }        

    private void establecerEventoMouseDragged(QuadCurve arco) {
        arco.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent me) {
                if (Main.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    double dragX = me.getX();
                    double dragY = me.getY();
                    QuadCurve quadCurve = (QuadCurve)me.getSource();
                    quadCurve.setControlX(dragX);
                    quadCurve.setControlY(dragY);
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

                    quadCurveNueva.setStartX(clickX);
                    quadCurveNueva.setStartY(clickY);
                    quadCurveNueva.setEndX(quadCurveFuente.getEndX());
                    quadCurveNueva.setEndY(quadCurveFuente.getEndY());

                    quadCurveFuente.setEndX(clickX);
                    quadCurveFuente.setEndY(clickY);              
                   
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

    public Group getGroup() {
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
}
