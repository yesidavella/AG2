package com.ag2.presentacion.controles;

import com.ag2.controlador.ControladorCreacionYAdminDeNodo;
import com.ag2.presentacion.InterfaceGraficaNodos;
import com.ag2.presentacion.IGU;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.diseño.*;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;

public class GrupoDeDiseno extends Group implements EventHandler<MouseEvent>, Serializable,InterfaceGraficaNodos {

    private transient ScrollPane spZonaDeDiseño;
    private ArrayList<ControladorCreacionYAdminDeNodo> controladoresRegistrados;

    public ScrollPane getSpZonaDeDiseño() {
        return spZonaDeDiseño;
    }

    public void setSpZonaDeDiseño(ScrollPane spZonaDeDiseño) {
        this.spZonaDeDiseño = spZonaDeDiseño;
    }
    private double posicionActualRatonX = 0;
    private double posicionActualRatonY = 0;
    private transient ObservableList listaClientes = FXCollections.observableArrayList();
    private transient ObservableList listaRecursos = FXCollections.observableArrayList();
    private transient ObservableList listaSwitches = FXCollections.observableArrayList();
    private transient ObservableList listaNodoServicio = FXCollections.observableArrayList();
    private NodoGrafico nodoGraficoSelecionado;
    private ArrayList<Serializable> objectosSerializables = new ArrayList<Serializable>();

    public NodoGrafico getNodoGraficoSelecionado() {
        return nodoGraficoSelecionado;
    }

    public void setNodoGraficoSelecionado(NodoGrafico nodoGraficoSelecionado) {
        this.nodoGraficoSelecionado = nodoGraficoSelecionado;
    }

    public GrupoDeDiseno(ScrollPane spZonaDeDiseño) {
        this.spZonaDeDiseño = spZonaDeDiseño;
        setOnMousePressed(this);
        setOnMouseDragged(this);
        setOnMouseReleased(this);
        controladoresRegistrados = new ArrayList<ControladorCreacionYAdminDeNodo>();
    }

    public void handle(MouseEvent mouEvent) {

        EventType tipoDeEvento = mouEvent.getEventType();
        TiposDeBoton botonSeleccionado = IGU.getEstadoTipoBoton();

        if (tipoDeEvento == MouseEvent.MOUSE_PRESSED) {

            if (botonSeleccionado == TiposDeBoton.MANO) {
                setCursor(TiposDeBoton.MANO.getImagenSobreObjetoCursor());
                posicionActualRatonX = mouEvent.getSceneX();
                posicionActualRatonY = mouEvent.getSceneY();
            }

        } else if (tipoDeEvento == MouseEvent.MOUSE_DRAGGED) {

            if (botonSeleccionado == TiposDeBoton.MANO) {

                double factorX = Math.abs(posicionActualRatonX - mouEvent.getSceneX());

                if (mouEvent.getSceneX() < posicionActualRatonX) {
                    spZonaDeDiseño.setHvalue(spZonaDeDiseño.getHvalue() + factorX * 0.0000413 * spZonaDeDiseño.getScaleX());
                } else if (mouEvent.getSceneX() > posicionActualRatonX) {
                    spZonaDeDiseño.setHvalue(spZonaDeDiseño.getHvalue() - factorX * 0.0000413 * spZonaDeDiseño.getScaleX());
                }
                posicionActualRatonX = mouEvent.getSceneX();

                double factorY = Math.abs(posicionActualRatonY - mouEvent.getSceneY());

                if (mouEvent.getSceneY() < posicionActualRatonY) {
                    spZonaDeDiseño.setVvalue(spZonaDeDiseño.getVvalue() + factorY * 0.0000544 * spZonaDeDiseño.getScaleY());
                } else if (mouEvent.getSceneY() > posicionActualRatonY) {
                    spZonaDeDiseño.setVvalue(spZonaDeDiseño.getVvalue() - factorY * 0.0000544 * spZonaDeDiseño.getScaleY());
                }
                posicionActualRatonY = mouEvent.getSceneY();
            }

        } else if (tipoDeEvento == MouseEvent.MOUSE_RELEASED) {

            NodoGrafico nuevoNodo = null;

            if (botonSeleccionado == TiposDeBoton.MANO) {
                setCursor(TiposDeBoton.MANO.getImagenCursor());

            } else if (botonSeleccionado == TiposDeBoton.CLIENTE) {
                nuevoNodo = new NodoClienteGrafico();
                listaClientes.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.NODO_DE_SERVICIO) {
                nuevoNodo = new NodoDeServicioGrafico();
                listaNodoServicio.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_OPTICO) {
                nuevoNodo = new EnrutadorOpticoGrafico();
                listaSwitches.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_RAFAGA) {
                nuevoNodo = new EnrutadorRafagaGrafico();
                listaSwitches.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_HIBRIDO) {
                nuevoNodo = new EnrutadorHibridoGrafico();
                listaSwitches.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.RECURSO) {
                nuevoNodo = new NodoDeRecursoGrafico();
                listaRecursos.add(nuevoNodo);
            }
            if (nuevoNodo != null) {
                if (nodoGraficoSelecionado != null) {
                    nodoGraficoSelecionado.setSelecionado(false);
                }
                nodoGraficoSelecionado = nuevoNodo;
            }

            dibujarNuevoNodoEnElMapa(nuevoNodo, mouEvent);
            
            //Le aviso a todos los controladores de la generacion del nuevo NodoGrafico
            for(ControladorCreacionYAdminDeNodo controladorRegistrado:controladoresRegistrados){
                controladorRegistrado.crearNodo(nuevoNodo);
            }
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
//            posicionX = me.getX() - (Main.getEstadoTipoBoton().getPosicionImagenDeCursorEnXyY().getX() + (nuevoNodo.getImagen().getWidth() / 8));
//            posicionY = me.getY() - (Main.getEstadoTipoBoton().getPosicionImagenDeCursorEnXyY().getY() + (nuevoNodo.getImagen().getHeight() / 8));

            posicionX = me.getX() - (IGU.getEstadoTipoBoton().getPosicionImagenDeCursorEnXyY().getX());
            posicionY = me.getY() - (IGU.getEstadoTipoBoton().getPosicionImagenDeCursorEnXyY().getY());
            
            System.out.println("posicionX:"+posicionX);
            System.out.println("posicionY:"+posicionY);
            
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
    
    public void addControladorCrearNodo(ControladorCreacionYAdminDeNodo ctrlCrearNodo){
        controladoresRegistrados.add(ctrlCrearNodo);
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
}