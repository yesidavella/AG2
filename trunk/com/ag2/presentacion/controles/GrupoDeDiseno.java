package com.ag2.presentacion.controles;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.IGU;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.VistaNodosGraficos;
import com.ag2.presentacion.diseño.*;
import com.ag2.presentacion.diseño.propiedades.PropiedadeNodo;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

public class GrupoDeDiseno extends Group implements EventHandler<MouseEvent>, Serializable, VistaNodosGraficos {

    private ArrayList<ControladorAbstractoAdminNodo> ctrladoresRegistradosAdminNodo;
    private ArrayList<ControladorAbstractoAdminEnlace> ctrladoresRegistradosAdminEnlace;
    private transient ObservableList listaClientes = FXCollections.observableArrayList();
    private transient ObservableList listaRecursos = FXCollections.observableArrayList();
    private transient ObservableList listaSwitches = FXCollections.observableArrayList();
    private transient ObservableList listaNodoServicio = FXCollections.observableArrayList();
    private ObjetoSeleccionable objetoGraficoSelecionado;
    private ArrayList<Serializable> objectosSerializables = new ArrayList<Serializable>();
    private transient Scale sclEscalaDeZoom;
    private final int MAP_SCALE = 17;
    private final double PERCENT_ZOOM = 1.2;
    private transient ScrollPane scPnPanelWorld;
    private double dragMouX = 0;
    private double dragMouY = 0;

    public GrupoDeDiseno() {
        setOnMousePressed(this);
        setOnMouseDragged(this);
        setOnMouseReleased(this);
        ctrladoresRegistradosAdminNodo = new ArrayList<ControladorAbstractoAdminNodo>();
        ctrladoresRegistradosAdminEnlace = new ArrayList<ControladorAbstractoAdminEnlace>();
        sclEscalaDeZoom = new Scale(1.44, -1.44);
        getTransforms().add(sclEscalaDeZoom);

        loadGeoMap();
    }

    public ObjetoSeleccionable getObjetoGraficoSelecionado() {
        return objetoGraficoSelecionado;
    }

    public void setObjetoGraficoSelecionado(ObjetoSeleccionable objetoGraficoSelecionado) {
        this.objetoGraficoSelecionado = objetoGraficoSelecionado;
    }

    public Scale getSclEscalaDeZoom() {
        return sclEscalaDeZoom;
    }

    public void handle(MouseEvent mouEvent) {

        EventType tipoDeEvento = mouEvent.getEventType();
        TiposDeBoton botonSeleccionado = IGU.getEstadoTipoBoton();

        if (tipoDeEvento == MouseEvent.MOUSE_PRESSED) {

            if (botonSeleccionado == TiposDeBoton.MANO) {
                setCursor(TiposDeBoton.MANO.getImagenSobreObjetoCursor());
                dragMouX = mouEvent.getX();
                dragMouY = mouEvent.getY();

            } else if (botonSeleccionado == TiposDeBoton.ZOOM_MINUS) {
                zoom(1 / PERCENT_ZOOM, mouEvent.getX() * sclEscalaDeZoom.getX(), mouEvent.getY() * sclEscalaDeZoom.getY());
            } else if (botonSeleccionado == TiposDeBoton.ZOOM_PLUS) {
                zoom(PERCENT_ZOOM, mouEvent.getX() * sclEscalaDeZoom.getX(), mouEvent.getY() * sclEscalaDeZoom.getY());
            }

        } else if (tipoDeEvento == MouseEvent.MOUSE_DRAGGED) {

            if (botonSeleccionado == TiposDeBoton.MANO) {

                double currentWidth = getBoundsInParent().getWidth();
                double distanceMovedX = dragMouX - mouEvent.getX();
                double percentToMoveX = distanceMovedX / currentWidth;

                scPnPanelWorld.setHvalue(scPnPanelWorld.getHvalue() + percentToMoveX);

                double currentHeight = getBoundsInParent().getHeight();
                double distanceMovedY = dragMouY - mouEvent.getY();
                double percentToMoveY = distanceMovedY / currentHeight;

                scPnPanelWorld.setVvalue(scPnPanelWorld.getVvalue() - percentToMoveY);
            }

        } else if (tipoDeEvento == MouseEvent.MOUSE_RELEASED) {

            NodoGrafico nuevoNodo = null;
            ControladorAbstractoAdminNodo controladorAdminNodo = ctrladoresRegistradosAdminNodo.get(0);
            ControladorAbstractoAdminEnlace controladorAdminEnlace = ctrladoresRegistradosAdminEnlace.get(0);
            double posClcikX = mouEvent.getX();
            double posClcikY = mouEvent.getY();

            if (botonSeleccionado == TiposDeBoton.MANO) {
                setCursor(TiposDeBoton.MANO.getImagenCursor());

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
            loadGeoMap();
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
            sclEscalaDeZoom = new Scale(1.44, -1.44);
            getTransforms().add(sclEscalaDeZoom);
            
          

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void cargarPropiedades(ArrayList<PropiedadeNodo> propiedadeNodos) {
    }

    public void addControladorCrearEnlace(ControladorAbstractoAdminEnlace ctrlCrearYAdminEnlace) {
        ctrladoresRegistradosAdminEnlace.add(ctrlCrearYAdminEnlace);
    }

    private void loadGeoMap() {

        Group texts = new Group();

        try {

            Color[] colors = new Color[]{Color.web("#A0A5CE")};//, Color.RED, Color.ORANGE, Color.VIOLET, Color.CHOCOLATE, Color.YELLOW, Color.AZURE };
            int currentColor = 0;

            File file = new File("src\\maps\\110m_admin_0_countries.shp");
            FileDataStore store = FileDataStoreFinder.getDataStore(file);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection c = featureSource.getFeatures();
            SimpleFeatureIterator featuresIterator = c.features();
            Coordinate[] coords;
            Geometry polygon;
            Point centroid;
            Bounds bounds;


            while (featuresIterator.hasNext()) {
                SimpleFeature o = featuresIterator.next();
                String name = (String) o.getAttribute("NAME");
                Object geometry = o.getDefaultGeometry();

                if (geometry instanceof MultiPolygon) {
                    MultiPolygon multiPolygon = (MultiPolygon) geometry;

                    centroid = multiPolygon.getCentroid();
                    final Text text = new Text(name);
                    bounds = text.getBoundsInLocal();
                    text.setFont(new Font(6));
                    text.getTransforms().add(new Translate(centroid.getX() * MAP_SCALE, centroid.getY() * MAP_SCALE));
                    text.getTransforms().add(new Scale(0.1 * MAP_SCALE, -0.1 * MAP_SCALE));
                    text.getTransforms().add(new Translate(-bounds.getWidth() / 2., bounds.getHeight() / 2.));
                    texts.getChildren().add(text);

                    for (int geometryI = 0; geometryI < multiPolygon.getNumGeometries(); geometryI++) {
                        polygon = multiPolygon.getGeometryN(geometryI);

                        coords = polygon.getCoordinates();
                        Path path = new Path();
                        
                        path.setStrokeWidth(0.5);
                        path.setFill(Color.BLACK);
                        currentColor = (currentColor + 1) % colors.length;
                        path.setFill(colors[currentColor]);
                        path.getElements().add(new MoveTo(coords[0].x * MAP_SCALE, coords[0].y * MAP_SCALE));

                        for (int i = 1; i < coords.length; i++) {
                            path.getElements().add(new LineTo(coords[i].x * MAP_SCALE, coords[i].y * MAP_SCALE));
                        }
                        path.getElements().add(new LineTo(coords[0].x * MAP_SCALE, coords[0].y * MAP_SCALE));
                        getChildren().add(path);
                        path.toBack();
                    }
                }
            }
            //setEffect(ds);
            getChildren().add(texts);

            Stop[] stops = {
                new Stop(0.1, Color.web("#9DCAFF")),
                new Stop(0.6, Color.web("#005AFF")),
                new Stop(0.8, Color.BLUE)
            };
            Rectangle backgroudRec = new Rectangle(360, 175,
                    new RadialGradient(-90, 0.7, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE, stops));

            backgroudRec.setTranslateX(-backgroudRec.getWidth() / 2);
            backgroudRec.setTranslateY(-backgroudRec.getHeight() / 2);
            backgroudRec.setScaleX(MAP_SCALE);
            backgroudRec.setScaleY(MAP_SCALE);
            getChildren().add(backgroudRec);
            backgroudRec.toBack();

            Rectangle r = new Rectangle(10, 10);
            r.setFill(Color.WHITE);
            r.setLayoutX(0);
            r.setLayoutY(0);
            getChildren().add(r);

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void zoom(final double percentZoom, double puntoPivoteX, double puntoPivoteY) {

        sclEscalaDeZoom.setPivotX(puntoPivoteX);
        sclEscalaDeZoom.setPivotY(puntoPivoteY);
        sclEscalaDeZoom.setX(sclEscalaDeZoom.getX() * percentZoom);
        sclEscalaDeZoom.setY(sclEscalaDeZoom.getY() * percentZoom);

        double anchoActual = getBoundsInParent().getWidth();
        double altoActual = getBoundsInParent().getHeight();

        double corrimientoX = anchoActual / 2;
        double corrimientoY = altoActual / 2;

        double coorAbsClickX = corrimientoX + puntoPivoteX * percentZoom;
        double coorAbsClickY = corrimientoY + puntoPivoteY * percentZoom;

        double porcentajeClickX = coorAbsClickX / anchoActual;
        double porcentajeClickY = coorAbsClickY / altoActual;

        double desfaceMaxEnX = scPnPanelWorld.getViewportBounds().getWidth() / 2;
        double funcDeDesfaceEnX = (-2 * desfaceMaxEnX * (porcentajeClickX)) + desfaceMaxEnX;

        double desfaceMaxEnY = scPnPanelWorld.getViewportBounds().getHeight() / 2;
        double funcDeDesfaceEnY = (-2 * (desfaceMaxEnY) * (porcentajeClickY)) + desfaceMaxEnY;
        /*
         * Convercion de pixeles a porcentaje del resultado de la funcion de
         * desface.
         */
        double porcCorreccionX = funcDeDesfaceEnX / anchoActual;
        double porcCorreccionY = funcDeDesfaceEnY / altoActual;

        scPnPanelWorld.setVvalue(porcentajeClickY - porcCorreccionY);
        scPnPanelWorld.setHvalue(porcentajeClickX - porcCorreccionX);

    }

    public void setScrollPane(ScrollPane scPnPanelWorld) {
        this.scPnPanelWorld = scPnPanelWorld;
    }

    public void enableDisign() {
        IGU.getInstance().habilitar();
        IGU.getInstance().getExecutePane().habilitar();
    }

    public void updatePropiedad(boolean isSubProperty, String id, String valor) {
    }
}
