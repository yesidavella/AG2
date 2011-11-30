package com.ag2.presentacion.controles;


import com.ag2.presentacion.Main;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.diseño.*;
import java.io.Serializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

public class GrupoDeDiseno extends Group implements EventHandler<MouseEvent>, Serializable  {

    private transient ScrollPane spZonaDeDiseño;
    private double posicionActualRatonX = 0;
    private double posicionActualRatonY = 0;
    private transient ObservableList  listaClientes = FXCollections.observableArrayList() ;     
    private transient ObservableList  listaRecursos = FXCollections.observableArrayList(); 
    private transient ObservableList  listaSwitches = FXCollections.observableArrayList(); 
    private transient ObservableList  listaNodoServicio = FXCollections.observableArrayList(); 
    
    
    private NodoGrafico nodoGraficoSelecionado;

    public NodoGrafico getNodoGraficoSelecionado() {
        return nodoGraficoSelecionado;
    }

    public void setNodoGraficoSelecionado(NodoGrafico nodoGraficoSelecionado) {
        this.nodoGraficoSelecionado = nodoGraficoSelecionado;
    }
     
    public GrupoDeDiseno(ScrollPane spZonaDeDiseño) {
        this.spZonaDeDiseño =  spZonaDeDiseño;
        setOnMousePressed(this);
        setOnMouseDragged(this);
        setOnMouseReleased(this);
    }
    
    public void handle(MouseEvent mouEvent) {
        
        EventType tipoDeEvento = mouEvent.getEventType();
        TiposDeBoton botonSeleccionado = Main.getEstadoTipoBoton();
        
        if(tipoDeEvento == MouseEvent.MOUSE_PRESSED){
            
            if (botonSeleccionado==TiposDeBoton.MANO) {
                setCursor(TiposDeBoton.MANO.getImagenSobreObjetoCursor());
                posicionActualRatonX=mouEvent.getSceneX();
                posicionActualRatonY=mouEvent.getSceneY();
            }
            
        }else if(tipoDeEvento == MouseEvent.MOUSE_DRAGGED){

            if (botonSeleccionado == TiposDeBoton.MANO) {
                
                double factorX=Math.abs(posicionActualRatonX-mouEvent.getSceneX());
                
                if (mouEvent.getSceneX() < posicionActualRatonX) {
                    spZonaDeDiseño.setHvalue(spZonaDeDiseño.getHvalue() + factorX*0.0000413 * spZonaDeDiseño.getScaleX());
                } else if (mouEvent.getSceneX() > posicionActualRatonX) {
                    spZonaDeDiseño.setHvalue(spZonaDeDiseño.getHvalue() -factorX*0.0000413 * spZonaDeDiseño.getScaleX());
                }
                posicionActualRatonX = mouEvent.getSceneX();

                double factorY=Math.abs(posicionActualRatonY-mouEvent.getSceneY());

                if (mouEvent.getSceneY() < posicionActualRatonY) {
                    spZonaDeDiseño.setVvalue(spZonaDeDiseño.getVvalue() + factorY*0.0000544 * spZonaDeDiseño.getScaleY());
                } else if (mouEvent.getSceneY() > posicionActualRatonY) {
                    spZonaDeDiseño.setVvalue(spZonaDeDiseño.getVvalue() - factorY*0.0000544 * spZonaDeDiseño.getScaleY());
                }
                posicionActualRatonY = mouEvent.getSceneY();
            }

        }else if(tipoDeEvento == MouseEvent.MOUSE_RELEASED){
            
            NodoGrafico nuevoNodo = null;
            
            if (botonSeleccionado == TiposDeBoton.MANO) 
            {
                setCursor(TiposDeBoton.MANO.getImagenCursor());
                
            } else if (botonSeleccionado == TiposDeBoton.CLIENTE) 
            {  
                nuevoNodo = new NodoClienteGrafico();
                listaClientes.add(nuevoNodo);
                
            } else if (botonSeleccionado == TiposDeBoton.NODO_DE_SERVICIO)
            {
                nuevoNodo = new NodoDeServicioGrafico();
                listaNodoServicio.add(nuevoNodo);
                
            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_OPTICO)
            {                
                nuevoNodo = new EnrutadorOpticoGrafico();
                listaSwitches.add(nuevoNodo);
                
            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_RAFAGA) 
            {                
                nuevoNodo = new EnrutadorRafagaGrafico();
                 listaSwitches.add(nuevoNodo);
                
            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_HIBRIDO) 
            {
                nuevoNodo = new EnrutadorHibridoGrafico();
                 listaSwitches.add(nuevoNodo);
                 
            } else if (botonSeleccionado == TiposDeBoton.RECURSO) 
            {
                nuevoNodo = new NodoDeRecursoGrafico();
                listaRecursos.add(nuevoNodo); 
            }
            if(nuevoNodo!=null)
            {                
                if(nodoGraficoSelecionado!=null)
                {
                    nodoGraficoSelecionado.setSelecionado(false);
                }    
                nodoGraficoSelecionado = nuevoNodo;                 
            }     
           
           dibujarNuevoNodoEnElMapa(nuevoNodo,mouEvent);
        }

    }
  
    public void eliminarNodeListaNavegacion(NodoGrafico nodoGrafico)
    {
        if(nodoGrafico instanceof NodoClienteGrafico)
        {
            listaClientes.remove(nodoGrafico);
        }
        else if(nodoGrafico instanceof NodoDeRecursoGrafico)
        {
            listaRecursos.remove(nodoGrafico);
        }
        else if(nodoGrafico instanceof EnrutadorGrafico)
        {
            listaSwitches.remove(nodoGrafico);
        } 
        else if(nodoGrafico instanceof NodoDeServicioGrafico)
        {
            listaNodoServicio.remove(nodoGrafico);
        } 
        
    }         
            
    private void dibujarNuevoNodoEnElMapa(NodoGrafico nuevoNodo,MouseEvent me) {
        
        double posicionX=0;
        double posicionY=0;
        
        if(nuevoNodo != null)
        {            
            posicionX = me.getX()-(Main.getEstadoTipoBoton().getPosicionImagenDeCursorEnXyY().getX()+(nuevoNodo.getImagen().getWidth()/8));
            posicionY = me.getY()-(Main.getEstadoTipoBoton().getPosicionImagenDeCursorEnXyY().getY()+(nuevoNodo.getImagen().getHeight()/8));
                    
            nuevoNodo.setPosX(posicionX);
            nuevoNodo.setPosY(posicionY);
            getChildren().addAll(nuevoNodo);
        }
    }

    public ObservableList getListaClientes() {
        return listaClientes;
    }

    public ObservableList getListaNodoServicio() {
        return listaNodoServicio;
    }

    public ObservableList getListaRecursos() {
        return listaRecursos;
    }

    public ObservableList getListaSwitches() {
        return listaSwitches;
    }

}
