package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.presentation.ActionTypeEmun;
import com.ag2.presentation.GraphNodesView;
import com.ag2.presentation.IGU;
import com.ag2.presentation.design.property.EntityProperty;
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
import javafx.scene.Node;
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

public class GraphDesignGroup implements EventHandler<MouseEvent>, Serializable, GraphNodesView {

    private transient ObservableList clientsObservableList;
    private transient ObservableList resourcesObservableList;
    private transient ObservableList switchesObservableList;
    private transient ObservableList brokersObservableList;
    private transient ScrollPane scPnWorld;
    private transient Scale scZoom;
    private transient Group group;
    private Selectable selectable;
    private ArrayList<Serializable> serializableObjects = new ArrayList<Serializable>();
    private final int MAP_SCALE = 17;
    private final double PERCENT_ZOOM = 1.2;
    private ArrayList<NodeAdminAbstractController> nodeAdminAbstractControllers;
    private ArrayList<LinkAdminAbstractController> linkAdminAbstractControllers;
    private double dragMouseX = 0;
    private double dragMouseY = 0;
    private boolean serializableComplete = false;

    public GraphDesignGroup() {
        initTransientObjects();
        nodeAdminAbstractControllers = new ArrayList<NodeAdminAbstractController>();
        linkAdminAbstractControllers = new ArrayList<LinkAdminAbstractController>();

    }

    public boolean isSerializableComplete() {
        return serializableComplete;
    }


    public void initTransientObjects() {
        group = new Group();
        group.setOnMousePressed(this);
        group.setOnMouseDragged(this);
        group.setOnMouseReleased(this);
        scZoom = new Scale(1.44, -1.44);
        group.getTransforms().add(scZoom);
        clientsObservableList = FXCollections.observableArrayList();

        resourcesObservableList = FXCollections.observableArrayList();
        switchesObservableList = FXCollections.observableArrayList();
        brokersObservableList = FXCollections.observableArrayList();
        loadGeoMap();

    }

    public Selectable getSelectable() {
        return selectable;
    }

    public void setSelectable(Selectable selectable) {
        this.selectable = selectable;

    }

    public Scale getScZoom() {
        return scZoom;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

        EventType eventType = mouseEvent.getEventType();
        ActionTypeEmun actionTypeEmun = IGU.getEstadoTipoBoton();

        if (eventType == MouseEvent.MOUSE_PRESSED) {

            if (actionTypeEmun == ActionTypeEmun.MANO) {
                group.setCursor(ActionTypeEmun.MANO.getImagenSobreObjetoCursor());
                dragMouseX = mouseEvent.getX();
                dragMouseY = mouseEvent.getY();

            } else if (actionTypeEmun == ActionTypeEmun.ZOOM_MINUS) {
                zoom(1 / PERCENT_ZOOM, mouseEvent.getX() * scZoom.getX(), mouseEvent.getY() * scZoom.getY());
            } else if (actionTypeEmun == ActionTypeEmun.ZOOM_PLUS) {
                zoom(PERCENT_ZOOM, mouseEvent.getX() * scZoom.getX(), mouseEvent.getY() * scZoom.getY());
            }

        } else if (eventType == MouseEvent.MOUSE_DRAGGED) {

            if (actionTypeEmun == ActionTypeEmun.MANO) {

                double currentWidth = group.getBoundsInParent().getWidth();
                double distanceMovedX = dragMouseX - mouseEvent.getX();
                double percentToMoveX = distanceMovedX / currentWidth;

                scPnWorld.setHvalue(scPnWorld.getHvalue() + percentToMoveX);

                double currentHeight = group.getBoundsInParent().getHeight();
                double distanceMovedY = dragMouseY - mouseEvent.getY();
                double percentToMoveY = distanceMovedY / currentHeight;

                scPnWorld.setVvalue(scPnWorld.getVvalue() - percentToMoveY);
            }

        } else if (eventType == MouseEvent.MOUSE_RELEASED) {

            GraphNode graphNode = null;
            NodeAdminAbstractController nodeAdminAbstractController = nodeAdminAbstractControllers.get(0);
            LinkAdminAbstractController linkAdminAbstractController = linkAdminAbstractControllers.get(0);

            if (actionTypeEmun == ActionTypeEmun.MANO) {
                group.setCursor(ActionTypeEmun.MANO.getImagenCursor());

            } else if (actionTypeEmun == ActionTypeEmun.CLIENTE) {

                graphNode = new ClientGraphNode(this, nodeAdminAbstractController, linkAdminAbstractController);
                clientsObservableList.add(graphNode);

            } else if (actionTypeEmun == ActionTypeEmun.NODO_DE_SERVICIO) {
                graphNode = new BrokerGrahpNode(this, nodeAdminAbstractController, linkAdminAbstractController);
                brokersObservableList.add(graphNode);

            } else if (actionTypeEmun == ActionTypeEmun.ENRUTADOR_OPTICO) {
                graphNode = new OCS_SwicthGraphNode(this, nodeAdminAbstractController, linkAdminAbstractController);
                switchesObservableList.add(graphNode);

            } else if (actionTypeEmun == ActionTypeEmun.ENRUTADOR_RAFAGA) {
                graphNode = new OBS_SwicthGraphNode(this, nodeAdminAbstractController, linkAdminAbstractController);
                switchesObservableList.add(graphNode);

            } else if (actionTypeEmun == ActionTypeEmun.ENRUTADOR_HIBRIDO) {
                graphNode = new HybridSwitchGraphNode(this, nodeAdminAbstractController, linkAdminAbstractController);
                switchesObservableList.add(graphNode);

            } else if (actionTypeEmun == ActionTypeEmun.RECURSO) {
                graphNode = new ResourceGraphNode(this, nodeAdminAbstractController, linkAdminAbstractController);
                resourcesObservableList.add(graphNode);
            }

            if (graphNode != null) {
                if (selectable != null) {
                    selectable.select(false);
                }

                addNodeOnMap(graphNode, mouseEvent);
                selectable = graphNode;

                for (NodeAdminAbstractController controladorRegistrado : nodeAdminAbstractControllers) {
                    controladorRegistrado.createNode(graphNode);
                }
                graphNode.select(true);
            }
        }
    }

    public void deleteNodeFromGoList(GraphNode graphNode) {
        if (graphNode instanceof ClientGraphNode) {
            clientsObservableList.remove(graphNode);
        } else if (graphNode instanceof ResourceGraphNode) {
            resourcesObservableList.remove(graphNode);
        } else if (graphNode instanceof SwitchGraphNode) {
            switchesObservableList.remove(graphNode);
        } else if (graphNode instanceof BrokerGrahpNode) {
            brokersObservableList.remove(graphNode);
        }
    }

    public void add(Node node) {
        group.getChildren().add(node);
        if (node instanceof Serializable) {
            serializableObjects.add((Serializable) node);
        }
    }

    public void add(GraphNode graphNode) {
        group.getChildren().add(graphNode.getGroup());
        if (graphNode instanceof Serializable) {
            serializableObjects.add((Serializable) graphNode);
        }
    }

    public void add(GraphArcSeparatorPoint graphArcSeparatorPoint) {
        group.getChildren().add(graphArcSeparatorPoint.getCircle());
        if (graphArcSeparatorPoint instanceof Serializable) {
            serializableObjects.add((Serializable) graphArcSeparatorPoint);
        }
    }

    public void add(GraphArc graphArc) {
        group.getChildren().add(graphArc.getQuadCurve());

        if (graphArc instanceof Serializable) {
            serializableObjects.add((Serializable) graphArc);
        }
    }

    public void remove(GraphArcSeparatorPoint graphArcSeparatorPoint) {
        group.getChildren().add(graphArcSeparatorPoint.getCircle());
        if (graphArcSeparatorPoint instanceof Serializable) {
            serializableObjects.add((Serializable) graphArcSeparatorPoint);
        }

    }

    public void remove(Node node) {
        group.getChildren().remove(node);
        if (node instanceof Serializable) {
            serializableObjects.remove((Serializable) node);
        }
    }

    public void remove(GraphNode graphNode) {

        group.getChildren().remove(graphNode.getGroup());
        if (graphNode instanceof Serializable) {
            serializableObjects.remove((Serializable) graphNode);
        }
    }

    public void remove(GraphArc graphArc) {
        group.getChildren().remove(graphArc.getQuadCurve());
        if (graphArc instanceof Serializable) {
            serializableObjects.remove((Serializable) graphArc);
        }
    }

    private void addNodeOnMap(GraphNode graphNode, MouseEvent mouseEvent) {

        double posicionX = 0;
        double posicionY = 0;

        if (graphNode != null) {

            posicionX = mouseEvent.getX() - graphNode.getWidth() / 2;
            posicionY = mouseEvent.getY() - graphNode.getHeight() / 2;

            graphNode.setLayoutX(posicionX);
            graphNode.setLayoutY(posicionY);
            add(graphNode);

        }
    }

    public Group getGroup() {
        return group;
    }

    public ObservableList getClientsObservableList() {
        return clientsObservableList;
    }

    public ObservableList getBrokersObservableList() {
        return brokersObservableList;
    }

    public ObservableList getResourcesObservableList() {
        return resourcesObservableList;
    }

    public ObservableList getSwitchesObservableList() {
        return switchesObservableList;
    }

    public void addNodeAdminAbstractControllers(NodeAdminAbstractController nodeAdminAbstractController) {
        nodeAdminAbstractControllers.add(nodeAdminAbstractController);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        try {
            serializableComplete = false;
            objectOutputStream.defaultWriteObject();

            System.out.println("Write:" + " Grupo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
            initTransientObjects();


            for (Serializable serializable : serializableObjects) {
                if (serializable instanceof GraphArc) {
                    GraphArc graphArc = (GraphArc) serializable;

                    graphArc.initTransientObjects();

                    graphArc.getQuadCurve().setStartX(graphArc.getStartX());
                    graphArc.getQuadCurve().setStartY(graphArc.getStartY());

                    graphArc.getQuadCurve().setEndX(graphArc.getEndX());
                    graphArc.getQuadCurve().setEndY(graphArc.getEndY());

                    graphArc.getQuadCurve().setControlX(graphArc.getControlX());
                    graphArc.getQuadCurve().setControlY(graphArc.getControlY());
                    group.getChildren().add(graphArc.getQuadCurve());
                }
            }
            for (Serializable serializable : serializableObjects) {
                if (serializable instanceof GraphArcSeparatorPoint) {
                    GraphArcSeparatorPoint graphArcSeparatorPoint = (GraphArcSeparatorPoint) serializable;

                    graphArcSeparatorPoint.initTransientObjects();
                    graphArcSeparatorPoint.getCircle().setCenterX(graphArcSeparatorPoint.getCenterX());
                    graphArcSeparatorPoint.getCircle().setCenterY(graphArcSeparatorPoint.getCenterY());

                    group.getChildren().add(graphArcSeparatorPoint.getCircle());
                }
            }

            for (Serializable serializable : serializableObjects) {
                if (serializable instanceof GraphNode) {
                    GraphNode graphNode = (GraphNode) serializable;

                    if (graphNode.getName() != null) {
                        graphNode.initTransientObjects();
                        graphNode.getGroup().setLayoutX(graphNode.getLayoutX());
                        graphNode.getGroup().setLayoutY(graphNode.getLayoutY());
                        graphNode.select(false);
                        group.getChildren().add(graphNode.getGroup());
                    }

                    if (graphNode instanceof ClientGraphNode) {
                        clientsObservableList.add(graphNode);
                    } else if (graphNode instanceof ResourceGraphNode) {
                        resourcesObservableList.add(graphNode);
                    } else if (graphNode instanceof SwitchGraphNode) {
                        switchesObservableList.add(graphNode);
                    } else if (graphNode instanceof BrokerGrahpNode) {
                        brokersObservableList.add(graphNode);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        serializableComplete = true;
    }

    public void loadProperties(ArrayList<EntityProperty> entityPropertys) {
    }

    public void addLinkAdminAbstractControllers(LinkAdminAbstractController linkAdminAbstractController) {
        linkAdminAbstractControllers.add(linkAdminAbstractController);
    }

    private void loadGeoMap() {

        Group texts = new Group();

        try {

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
                SimpleFeature country = featuresIterator.next();
                String name = (String) country.getAttribute("NAME");
                Object geometry = country.getDefaultGeometry();

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
                        path.setFill(Color.web("#A4A4A4"));//A0A5CE,B7B7B7

                        path.getElements().add(new MoveTo(coords[0].x * MAP_SCALE, coords[0].y * MAP_SCALE));

                        for (int i = 0; i < coords.length; i++) {
                            path.getElements().add(new LineTo(coords[i].x * MAP_SCALE, coords[i].y * MAP_SCALE));
                        }
                        //path.getElements().add(new LineTo(coords[0].x * MAP_SCALE, coords[0].y * MAP_SCALE));
                        add(path);
                        path.toBack();
                    }
                }
            }

            add(texts);

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

            add(backgroudRec);
            backgroudRec.toBack();



        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void zoom(final double percentZoom, double pivotPointX, double pivotPointY) {

        scZoom.setPivotX(pivotPointX);
        scZoom.setPivotY(pivotPointY);
        scZoom.setX(scZoom.getX() * percentZoom);
        scZoom.setY(scZoom.getY() * percentZoom);

        double currentWidth = group.getBoundsInParent().getWidth();
        double currentHeight = group.getBoundsInParent().getHeight();

        double corrimientoX = currentWidth / 2;
        double corrimientoY = currentHeight / 2;

        double coorAbsClickX = corrimientoX + pivotPointX * percentZoom;
        double coorAbsClickY = corrimientoY + pivotPointY * percentZoom;

        double percentageClickX = coorAbsClickX / currentWidth;
        double percentageClickY = coorAbsClickY / currentHeight;

        double desfaceMaxX = scPnWorld.getViewportBounds().getWidth() / 2;
        double functionDesfaceX = (-2 * desfaceMaxX * (percentageClickX)) + desfaceMaxX;

        double desfaceMaxY = scPnWorld.getViewportBounds().getHeight() / 2;
        double functionDesfaceY = (-2 * (desfaceMaxY) * (percentageClickY)) + desfaceMaxY;
        /*
         * Convercion de pixeles a porcentaje del resultado de la funcion de
         * desface.
         */
        double percentageCorrectionX = functionDesfaceX / currentWidth;
        double percentageCorrectionY = functionDesfaceY / currentHeight;

        scPnWorld.setVvalue(percentageClickY - percentageCorrectionY);
        scPnWorld.setHvalue(percentageClickX - percentageCorrectionX);

    }

    public void setScrollPane(ScrollPane scPnPanelWorld) {
        this.scPnWorld = scPnPanelWorld;
    }

    public void enableDisign() {
        IGU.getInstance().habilitar();
        IGU.getInstance().getExecutePane().habilitar();
    }

    public void updateProperty(boolean isSubProperty, String id, String valor) {
    }
}
