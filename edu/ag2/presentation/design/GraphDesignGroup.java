package edu.ag2.presentation.design;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import edu.ag2.presentation.ActionTypeEmun;
import edu.ag2.presentation.GUI;
import edu.ag2.presentation.GraphNodesView;
import edu.ag2.presentation.Main;
import edu.ag2.presentation.design.property.EntityProperty;
import edu.ag2.presentation.maps.MapHelper;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
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
    private transient ScrollPane scpWorld;
    private transient Scale scZoom;
    private transient Group group;
    private Selectable selectable;
    private ArrayList<Serializable> serializableObjects = new ArrayList<Serializable>();
    private final int MAP_SCALE = 17;
    private final double PERCENT_ZOOM = 1.2;
    private ArrayList<NodeAdminAbstractController> nodeAdminAbstractControllers;
    private ArrayList<LinkAdminAbstractController> linkAdminCtrs;
    private double dragMouseX = 0;
    private double dragMouseY = 0;
    private boolean serializableComplete = false;
    private transient Rectangle backgroundRec;
    private transient ArrayList<Group> linesOCS;
    private transient ArrayList<VBox> resultOCS;
    private transient ArrayList<Shape> listMapWorld;
    private File folderMap = new File("maps");

    public GraphDesignGroup() {
        initTransientObjects();
        nodeAdminAbstractControllers = new ArrayList<NodeAdminAbstractController>();
        linkAdminCtrs = new ArrayList<LinkAdminAbstractController>();
    }

    public GraphOCS addOCSLine(GraphNode graphNodeSource, GraphNode graphNodeDestination, int countInnstanceOCS) {
        GraphOCS graphOCS = new GraphOCS(graphNodeSource, graphNodeDestination, this, countInnstanceOCS);
        linesOCS.add(graphOCS.getGroup());
        graphOCS.getGroup().setVisible(GUI.getInstance().isOCSView());
        resultOCS.add(graphOCS.getvBoxInfoOCS());
        return graphOCS;
    }

    public void showLineOCS() {
        for (Group line : linesOCS) {
            line.setVisible(true);
        }
    }

    public void hideLineOCS() {
        for (Group line : linesOCS) {
            line.setVisible(false);
        }
        for (VBox vBox : resultOCS) {
            vBox.setVisible(false);
        }
    }

    public ArrayList<Group> getLinesOCS() {
        return linesOCS;
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

        linesOCS = new ArrayList<Group>();
        resultOCS = new ArrayList<VBox>();
        listMapWorld = new ArrayList<Shape>();
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
        ActionTypeEmun actionTypeEmun = GUI.getActionTypeEmun();

        if (eventType == MouseEvent.MOUSE_PRESSED) {

            if (actionTypeEmun == ActionTypeEmun.HAND) {
                group.setCursor(ActionTypeEmun.HAND.getOverCursorImage());
                dragMouseX = mouseEvent.getX();
                dragMouseY = mouseEvent.getY();

            } else if (actionTypeEmun == ActionTypeEmun.ZOOM_MINUS) {
                zoom(1 / PERCENT_ZOOM, mouseEvent.getX() * scZoom.getX(), mouseEvent.getY() * scZoom.getY());
            } else if (actionTypeEmun == ActionTypeEmun.ZOOM_PLUS) {
                zoom(PERCENT_ZOOM, mouseEvent.getX() * scZoom.getX(), mouseEvent.getY() * scZoom.getY());
            }

        } else if (eventType == MouseEvent.MOUSE_DRAGGED) {

            if (actionTypeEmun == ActionTypeEmun.HAND) {

                double currentWidth = group.getBoundsInParent().getWidth();
                double distanceMovedX = dragMouseX - mouseEvent.getX();
                double percentToMoveX = distanceMovedX / currentWidth;

                scpWorld.setHvalue(scpWorld.getHvalue() + percentToMoveX);

                double currentHeight = group.getBoundsInParent().getHeight();
                double distanceMovedY = dragMouseY - mouseEvent.getY();
                double percentToMoveY = distanceMovedY / currentHeight;

                scpWorld.setVvalue(scpWorld.getVvalue() - percentToMoveY);
            }

        } else if (eventType == MouseEvent.MOUSE_RELEASED) {

            GraphNode graphNode = null;
            NodeAdminAbstractController nodeAdminAbstractController = nodeAdminAbstractControllers.get(0);

            if (actionTypeEmun == ActionTypeEmun.HAND) {
                group.setCursor(ActionTypeEmun.HAND.getCursorImage());

            } else if (actionTypeEmun == ActionTypeEmun.CLIENT) {

                graphNode = new ClientGraphNode(this, nodeAdminAbstractController, linkAdminCtrs);
                clientsObservableList.add(graphNode);

            } else if (actionTypeEmun == ActionTypeEmun.BROKER) {
                graphNode = new BrokerGrahpNode(this, nodeAdminAbstractController, linkAdminCtrs);
                brokersObservableList.add(graphNode);

            } else if (actionTypeEmun == ActionTypeEmun.PCE_SWITCH) {
                graphNode = new PCE_SwicthGraphNode(this, nodeAdminAbstractController, linkAdminCtrs);
                switchesObservableList.add(graphNode);

            } else if (actionTypeEmun == ActionTypeEmun.HRYDRID_SWITCH) {
                graphNode = new HybridSwitchGraphNode(this, nodeAdminAbstractController, linkAdminCtrs);
                switchesObservableList.add(graphNode);

            } else if (actionTypeEmun == ActionTypeEmun.RESOURCE) {
                graphNode = new ResourceGraphNode(this, nodeAdminAbstractController, linkAdminCtrs);
                resourcesObservableList.add(graphNode);
            }

            if (graphNode != null) {
                if (selectable != null) {
                    selectable.select(false);
                }

                addNodeOnMap(graphNode, mouseEvent);
                selectable = graphNode;

                for (NodeAdminAbstractController nodeAdminController : nodeAdminAbstractControllers) {
                    nodeAdminController.createNode(graphNode);
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
        group.getChildren().remove(graphArcSeparatorPoint.getCircle());
        if (graphArcSeparatorPoint instanceof Serializable) {
            serializableObjects.remove((Serializable) graphArcSeparatorPoint);
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

        double posXNewNode = 0;
        double posYNewNode = 0;

        if (graphNode != null) {

            posXNewNode = mouseEvent.getX()- (graphNode.getWidth()/2);
            
            posYNewNode = mouseEvent.getY()- (GUI.getActionTypeEmun().getPoint2D().getY()+ graphNode.getHeight()/4 );

            graphNode.setLayoutX(posXNewNode);
            graphNode.setLayoutY(posYNewNode);
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

    private void writeObject(ObjectOutputStream stream) {
        try {
            serializableComplete = false;
            stream.defaultWriteObject();
            Main.countObject++;
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
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
        linkAdminCtrs.add(linkAdminAbstractController);
    }

    public void hideMap() {
        for (Shape shape : listMapWorld) {
            shape.setVisible(false);
            backgroundRec.setId("non-world-ocean");
        }
    }

    public void showMap() {
        for (Shape shape : listMapWorld) {
            shape.setVisible(true);
            backgroundRec.setId("world-ocean");
        }
    }

    private void loadGeoMap() {

        Group texts = new Group();

        try {
            copyFile("110m_admin_0_countries.dbf");
            copyFile("110m_admin_0_countries.prj");
            copyFile("110m_admin_0_countries.qix");
            copyFile("110m_admin_0_countries.shp");
            copyFile("110m_admin_0_countries.shx");

            System.gc();
            File file = new File(folderMap, "110m_admin_0_countries.shp");

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
                    text.setFont(new Font(4));
                    text.getTransforms().add(new Translate(centroid.getX() * MAP_SCALE, centroid.getY() * MAP_SCALE));
                    text.getTransforms().add(new Scale(0.1 * MAP_SCALE, -0.1 * MAP_SCALE));
                    text.getTransforms().add(new Translate(-bounds.getWidth() / 2., bounds.getHeight() / 2.));
                    listMapWorld.add(text);
                    texts.getChildren().add(text);

                    for (int geometryI = 0; geometryI < multiPolygon.getNumGeometries(); geometryI++) {
                        polygon = multiPolygon.getGeometryN(geometryI);

                        coords = polygon.getCoordinates();

                        Path path = new Path();
                        path.setStrokeWidth(0.2);

                        path.setFill(Color.web("#AD9784"));//A0A5CE,B7B7B7,A4A4A4,C1BBB1,AD9784 
                        //Si se cambia el color, dejar el registro de cual era el q estaba¡¡¡
                        path.getElements().add(new MoveTo(coords[0].x * MAP_SCALE, coords[0].y * MAP_SCALE));

                        for (int i = 0; i < coords.length; i++) {
                            path.getElements().add(new LineTo(coords[i].x * MAP_SCALE, coords[i].y * MAP_SCALE));
                        }
                        //path.getElements().add(new LineTo(coords[0].x * MAP_SCALE, coords[0].y * MAP_SCALE));
                        add(path);
                        listMapWorld.add(path);
                        path.toBack();
                    }
                }
            }

            add(texts);

//            Stop[] stops = {
//                new Stop(0.1, Color.web("#9DCAFF")),
//                new Stop(0.6, Color.web("#005AFF")),
//                new Stop(0.8, Color.BLUE)
//            };
//            Rectangle backgroundRec = new Rectangle(360, 175,
//                    new RadialGradient(-90, 0.7, 0.5, 0.5, 0.8, true, CycleMethod.NO_CYCLE, stops));
            backgroundRec = new Rectangle(360, 175);
            backgroundRec.setId("world-ocean");

            backgroundRec.setTranslateX(-backgroundRec.getWidth() / 2);
            backgroundRec.setTranslateY(-backgroundRec.getHeight() / 2);
            backgroundRec.setScaleX(MAP_SCALE);
            backgroundRec.setScaleY(MAP_SCALE);

            add(backgroundRec);
            backgroundRec.toBack();
            System.gc();

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void copyFile(String name) {
        InputStream inputStream = MapHelper.getResourceInputStream(name);
        OutputStream out = null;
        try {

            folderMap.mkdir();

            File file = new File(folderMap, name);
            out = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            inputStream.close();

        } catch (IOException ex) {
            try {

                out.close();
            } catch (IOException ex1) {
                Logger.getLogger(GraphDesignGroup.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(GraphDesignGroup.class.getName()).log(Level.SEVERE, null, ex);
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

        double desfaceMaxX = scpWorld.getViewportBounds().getWidth() / 2;
        double functionDesfaceX = (-2 * desfaceMaxX * (percentageClickX)) + desfaceMaxX;

        double desfaceMaxY = scpWorld.getViewportBounds().getHeight() / 2;
        double functionDesfaceY = (-2 * (desfaceMaxY) * (percentageClickY)) + desfaceMaxY;
        /*
         * Convercion de pixeles a porcentaje del resultado de la funcion de
         * desface.
         */
        double percentageCorrectionX = functionDesfaceX / currentWidth;
        double percentageCorrectionY = functionDesfaceY / currentHeight;

        scpWorld.setVvalue(percentageClickY - percentageCorrectionY);
        scpWorld.setHvalue(percentageClickX - percentageCorrectionX);

    }

    public void setScrollPane(ScrollPane scPnPanelWorld) {
        this.scpWorld = scPnPanelWorld;
    }

    public void enableDisign() {
        GUI.getInstance().stop();
        GUI.getInstance().getExecutePane().enable();
    }

    public void updateProperty(boolean isSubProperty, String id, String valor) {
    }
}
