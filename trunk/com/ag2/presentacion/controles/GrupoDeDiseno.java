package com.ag2.presentacion.controles;

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
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Scale;

public class GrupoDeDiseno extends Group implements EventHandler<MouseEvent>, Serializable, VistaNodosGraficos {

    private transient ScrollPane spZonaDeDiseño;
    private ArrayList<ControladorAbstractoAdminNodo> controladoresRegistrados;
    private double posicionActualRatonX = 0;
    private double posicionActualRatonY = 0;
    private transient ObservableList listaClientes = FXCollections.observableArrayList();
    private transient ObservableList listaRecursos = FXCollections.observableArrayList();
    private transient ObservableList listaSwitches = FXCollections.observableArrayList();
    private transient ObservableList listaNodoServicio = FXCollections.observableArrayList();
    private ObjetoSeleccionable objetoGraficoSelecionado;
    private ArrayList<Serializable> objectosSerializables = new ArrayList<Serializable>();
    private Scale sclEscalaDeZoom;
    
    public GrupoDeDiseno(ScrollPane spZonaDeDiseño) {
        this.spZonaDeDiseño = spZonaDeDiseño;
        setOnMousePressed(this);
        setOnMouseDragged(this);
        setOnMouseReleased(this);
        controladoresRegistrados = new ArrayList<ControladorAbstractoAdminNodo>();
        sclEscalaDeZoom = new Scale();
        getTransforms().add(sclEscalaDeZoom);
    }

    public ScrollPane getSpZonaDeDiseño() {
        return spZonaDeDiseño;
    }

    public void setSpZonaDeDiseño(ScrollPane spZonaDeDiseño) {
        this.spZonaDeDiseño = spZonaDeDiseño;
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
            ControladorAbstractoAdminNodo controladorPrincipal = controladoresRegistrados.get(0);

            if (botonSeleccionado == TiposDeBoton.MANO) {
                setCursor(TiposDeBoton.MANO.getImagenCursor());

            } else if (botonSeleccionado == TiposDeBoton.CLIENTE) {
                nuevoNodo = new NodoClienteGrafico(controladorPrincipal);
                listaClientes.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.NODO_DE_SERVICIO) {
                nuevoNodo = new NodoDeServicioGrafico(controladorPrincipal);
                listaNodoServicio.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_OPTICO) {
                nuevoNodo = new EnrutadorOpticoGrafico(controladorPrincipal);
                listaSwitches.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_RAFAGA) {
                nuevoNodo = new EnrutadorRafagaGrafico(controladorPrincipal);
                listaSwitches.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.ENRUTADOR_HIBRIDO) {
                nuevoNodo = new EnrutadorHibridoGrafico(controladorPrincipal);
                listaSwitches.add(nuevoNodo);

            } else if (botonSeleccionado == TiposDeBoton.RECURSO) {
                nuevoNodo = new NodoDeRecursoGrafico(controladorPrincipal);
                listaRecursos.add(nuevoNodo);
            }
            
            if (nuevoNodo != null) {
                if (objetoGraficoSelecionado != null) {
                    objetoGraficoSelecionado.seleccionar(false);
                }
                
                dibujarNuevoNodoEnElMapa(nuevoNodo, mouEvent);
                objetoGraficoSelecionado = nuevoNodo;
                
                for (ControladorAbstractoAdminNodo controladorRegistrado : controladoresRegistrados) {
                    controladorRegistrado.crearNodo(nuevoNodo);
                }
                nuevoNodo.seleccionar(true);
            }
            System.out.println("PosX:"+mouEvent.getX()+" PosY:"+mouEvent.getY());
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

            posicionX = me.getX() - nuevoNodo.getAnchoActual()/2;
            posicionY = me.getY() - nuevoNodo.getAltoActual()/2;
            
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

    public void cargarPropiedades(ArrayList<PropiedadeNodo> propiedadeNodos) {
    }

    public void updatePropiedad( String id, String valor) {
        
    }

    public void generarZoom(double factorEscala) {
        
        sclEscalaDeZoom.setPivotX(2*spZonaDeDiseño.getHvalue()*12500);//10100
        sclEscalaDeZoom.setPivotY(2*spZonaDeDiseño.getVvalue()*9375);//9800
        
//        System.out.println("X:"+2*spZonaDeDiseño.getHvalue()*12500+" Y:"+2*spZonaDeDiseño.getVvalue()*9375);
        
        sclEscalaDeZoom.setX(factorEscala);
        sclEscalaDeZoom.setY(factorEscala);
        
    }
    
}