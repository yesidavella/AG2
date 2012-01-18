package com.ag2.presentacion.controles;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.IGU;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.VistaNodosGraficos;
import com.ag2.presentacion.diseño.*;
import com.ag2.presentacion.diseño.propiedades.PropiedadeNodo;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

public class GrupoDeDiseno extends Group implements EventHandler<MouseEvent>, Serializable, VistaNodosGraficos {

    private ArrayList<ControladorAbstractoAdminNodo> ctrladoresRegistradosAdminNodo;
    private ArrayList<ControladorAbstractoAdminEnlace> ctrladoresRegistradosAdminEnlace;
    private transient ObservableList listaClientes = FXCollections.observableArrayList();
    private transient ObservableList listaRecursos = FXCollections.observableArrayList();
    private transient ObservableList listaSwitches = FXCollections.observableArrayList();
    private transient ObservableList listaNodoServicio = FXCollections.observableArrayList();
    private ObjetoSeleccionable objetoGraficoSelecionado;
    private ArrayList<Serializable> objectosSerializables = new ArrayList<Serializable>();
    private Scale sclEscalaDeZoom;
    private Rectangle backgroudRec;
    
    private double dragBaseX, dragBaseY;
    private double dragBase2X, dragBase2Y;

    public GrupoDeDiseno() {
        backgroudRec = new Rectangle(360, 175, Color.BLUE);
        backgroudRec.setTranslateX(-backgroudRec.getWidth()/2);
        backgroudRec.setTranslateY(-backgroudRec.getHeight()/2);
        backgroudRec.setScaleX(17);
        backgroudRec.setScaleY(17);
        
        getChildren().add(backgroudRec);
        setOnMousePressed(this);
        setOnMouseDragged(this);
        setOnMouseReleased(this);
        ctrladoresRegistradosAdminNodo = new ArrayList<ControladorAbstractoAdminNodo>();
        ctrladoresRegistradosAdminEnlace = new ArrayList<ControladorAbstractoAdminEnlace>();
        sclEscalaDeZoom = new Scale();
        getTransforms().add(sclEscalaDeZoom);
    }

    public ObjetoSeleccionable getObjetoGraficoSelecionado() {
        return objetoGraficoSelecionado;
    }

    public void setObjetoGraficoSelecionado(ObjetoSeleccionable objetoGraficoSelecionado) {
        this.objetoGraficoSelecionado = objetoGraficoSelecionado;
    }

    public void handle(MouseEvent mouEvent) {

        EventType tipoDeEvento = mouEvent.getEventType();
        TiposDeBoton botonSeleccionado = IGU.getEstadoTipoBoton();

        if (tipoDeEvento == MouseEvent.MOUSE_PRESSED) {

            if (botonSeleccionado == TiposDeBoton.MANO) {
                setCursor(TiposDeBoton.MANO.getImagenSobreObjetoCursor());
                dragBaseX = translateXProperty().get();
                dragBaseY = translateYProperty().get();
                dragBase2X = mouEvent.getSceneX();
                dragBase2Y = mouEvent.getSceneY();
            }

        } else if (tipoDeEvento == MouseEvent.MOUSE_DRAGGED) {
            
            if(botonSeleccionado==TiposDeBoton.MANO){
                setTranslateX(dragBaseX + (mouEvent.getSceneX()-dragBase2X));
                setTranslateY(dragBaseY + (mouEvent.getSceneY()-dragBase2Y)); 
            }
            
        } else if (tipoDeEvento == MouseEvent.MOUSE_RELEASED) {

            NodoGrafico nuevoNodo = null;
            ControladorAbstractoAdminNodo controladorAdminNodo = ctrladoresRegistradosAdminNodo.get(0);
            ControladorAbstractoAdminEnlace controladorAdminEnlace = ctrladoresRegistradosAdminEnlace.get(0);
            double posClcikX = mouEvent.getX();
            double posClcikY = mouEvent.getY();

            if (botonSeleccionado == TiposDeBoton.MANO) {
                setCursor(TiposDeBoton.MANO.getImagenCursor());

            } else if (botonSeleccionado == TiposDeBoton.ZOOM_PLUS) {
                setZoom(1.2, posClcikX, posClcikY);
                
            } else if (botonSeleccionado == TiposDeBoton.ZOOM_MINUS) {
                
                if(sclEscalaDeZoom.getX()>0.2){
                    setZoom(0.8, posClcikX, posClcikY);
                }
                
                
            } else if (botonSeleccionado == TiposDeBoton.CLIENTE) {
              
                nuevoNodo = new NodoClienteGrafico(controladorAdminNodo, controladorAdminEnlace);
                listaClientes.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.NODO_DE_SERVICIO) {
                nuevoNodo = new NodoDeServicioGrafico(controladorAdminNodo, controladorAdminEnlace);
                listaNodoServicio.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_OPTICO) {
                nuevoNodo = new EnrutadorOpticoGrafico(controladorAdminNodo, controladorAdminEnlace);
                listaSwitches.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_RAFAGA) {
                nuevoNodo = new EnrutadorRafagaGrafico(controladorAdminNodo, controladorAdminEnlace);
                listaSwitches.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_HIBRIDO) {
                nuevoNodo = new EnrutadorHibridoGrafico(controladorAdminNodo, controladorAdminEnlace);
                listaSwitches.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.RECURSO) {
                nuevoNodo = new NodoDeRecursoGrafico(controladorAdminNodo, controladorAdminEnlace);
                listaRecursos.add(nuevoNodo);
            }

            if (nuevoNodo != null) {
                if (objetoGraficoSelecionado != null) {
                    objetoGraficoSelecionado.seleccionar(false);
                }

                dibujarNuevoNodoEnElMapa(nuevoNodo, mouEvent);
                objetoGraficoSelecionado = nuevoNodo;

                for (ControladorAbstractoAdminNodo controladorRegistrado : ctrladoresRegistradosAdminNodo) {
                    controladorRegistrado.crearNodo(nuevoNodo);
                }
                nuevoNodo.seleccionar(true);
            }




        } else if (tipoDeEvento == MouseEvent.MOUSE_CLICKED) {
        }
    }

    public void eliminarNodeListaNavegacion(NodoGrafico nodoGrafico) {
        if (nodoGrafico instanceof NodoClienteGrafico) {
            listaClientes.remove(nodoGrafico);
        } else if (nodoGrafico instanceof NodoDeRecursoGrafico) {
            listaRecursos.remove(nodoGrafico);
        } else if (nodoGrafico instanceof EnrutadorGrafico) {
            listaSwitches.remove(nodoGrafico);
        } else if (nodoGrafico instanceof NodoDeServicioGrafico) {
            listaNodoServicio.remove(nodoGrafico);
        }

    }

    private void dibujarNuevoNodoEnElMapa(NodoGrafico nuevoNodo, MouseEvent me) {

        double posicionX = 0;
        double posicionY = 0;

        if (nuevoNodo != null) {

            posicionX = me.getX() - nuevoNodo.getAnchoActual() / 2;
            posicionY = me.getY() - nuevoNodo.getAltoActual() / 2;

            nuevoNodo.setPosX(posicionX);
            nuevoNodo.setPosY(posicionY);
            getChildren().addAll(nuevoNodo);
            addcionarObjectoSerializable(nuevoNodo);
        }
    }

    public void addcionarObjectoSerializable(Serializable serializable) {
        objectosSerializables.add(serializable);
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

    public void addControladorCrearNodo(ControladorAbstractoAdminNodo ctrlCrearNodo) {
        ctrladoresRegistradosAdminNodo.add(ctrlCrearNodo);
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
            setOnMousePressed(this);
            setOnMouseDragged(this);
            setOnMouseReleased(this);

            for (Serializable serializable : objectosSerializables) {
                if (serializable instanceof NodoGrafico) {
                    NodoGrafico nodoGrafico = (NodoGrafico) serializable;
                    getChildren().add(nodoGrafico);
                }
            }

            listaClientes = FXCollections.observableArrayList();
            listaRecursos = FXCollections.observableArrayList();
            listaSwitches = FXCollections.observableArrayList();
            listaNodoServicio = FXCollections.observableArrayList();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void cargarPropiedades(ArrayList<PropiedadeNodo> propiedadeNodos) {
    }

    public void updatePropiedad(String id, String valor) {
    }

    private void setZoom(double escala, double posClickX, double posClickY) {

        sclEscalaDeZoom.setPivotX(posClickX);
        sclEscalaDeZoom.setPivotY(posClickY);
//        System.out.println("X:"+2*spZonaDeDiseño.getHvalue()*12500+" Y:"+2*spZonaDeDiseño.getVvalue()*9375);
        sclEscalaDeZoom.setX(sclEscalaDeZoom.getX() * escala);
        sclEscalaDeZoom.setY(sclEscalaDeZoom.getY() * escala);

    }

    public void addControladorCrearEnlace(ControladorAbstractoAdminEnlace ctrlCrearYAdminEnlace) {
        ctrladoresRegistradosAdminEnlace.add(ctrlCrearYAdminEnlace);
    }
}