package com.ag2.presentation.design;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.presentation.ActionTypeEmun;
import com.ag2.presentation.GUI;
import com.ag2.presentation.Main;
import com.ag2.util.Utils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public abstract class GraphNode implements Selectable, Serializable {

    private static ImageView IMG_VW_DENY_LINK = new ImageView(new Image(Utils.ABS_PATH_IMGS + "prohibido_enlace.png"));
    public static boolean linkBegin = false;
    private static GraphNode wildcardNodeA = null;
    protected transient Image image = null;
    private transient Line wildcardLink;
    private transient ImageView imageView;
    protected transient Label lblName;
    private transient DropShadow dropShadow;
    protected transient VBox vBoxWrapper;
    private transient Group group;
    private String name = null;
    private ArrayList<NodeListener> nodeListeners = new ArrayList<NodeListener>();
    private boolean deleted = false;
    private boolean selected = false;
    private String imageURL;
    private boolean dragging = false;
    private NodeAdminAbstractController nodeAdminAbstractController;
    private LinkAdminAbstractController linkAdminAbstractController;
    private short height;
    private short width;
    private String originalName;
    protected short lineBreakStep;
    private short initialHeight = 0;
    private HashMap<String, String> nodeProperties;
    private HashMap<String, String> subNodeProperties;
    private GraphDesignGroup graphDesignGroup;
    private double layoutX;
    private double layoutY;

    public GraphNode(GraphDesignGroup graphDesignGroup, String name, String imageURL, NodeAdminAbstractController nodeAdminAbstractController, LinkAdminAbstractController linkAdminAbstractController) {

        this.graphDesignGroup = graphDesignGroup;
        this.nodeAdminAbstractController = nodeAdminAbstractController;
        this.linkAdminAbstractController = linkAdminAbstractController;
        this.imageURL = imageURL;
        this.name = name;
        this.originalName = name;
        nodeProperties = new HashMap<String, String>();
        subNodeProperties = new HashMap<String, String>();
        initTransientObjects();
    }

    public void initTransientObjects() {

        group = new Group();
        dropShadow = new DropShadow();
        group.setEffect(dropShadow);
        lblName = new Label(formatearNombre(name));
        lblName.setTextFill(Color.BLACK);
        lblName.setTextAlignment(TextAlignment.CENTER);
        lblName.setStyle("-fx-font: bold 12pt 'Arial'; -fx-background-color:#CCD4EC");

        wildcardLink = new Line();
        vBoxWrapper = new VBox();
        image = new Image(imageURL);
        imageView = new ImageView(image);
        vBoxWrapper.setAlignment(Pos.CENTER);
        vBoxWrapper.getChildren().addAll(imageView, lblName);

        group.getChildren().addAll(vBoxWrapper);
        group.setScaleX(0.5);
        group.setScaleY(-0.5);

        establishEventOnMouseClicked();
        establishEventOnMousePressed();
        establishEventOnMouseDragged();
        establishEventOnMouseReleased();
        establishEventOnMouseEntered();
        establishEventOnMouseExit();
    }

    public HashMap<String, String> getNodeProperties() {
        return nodeProperties;
    }

    public void setInitialHeight(short initialHeight) {
        this.initialHeight = initialHeight;
    }

    public short getInitialHeight() {
        return initialHeight;
    }

    public String getOriginalName() {
        return originalName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GraphNode) {
            GraphNode graphNode = (GraphNode) obj;
            return originalName.equals(graphNode.getOriginalName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.originalName != null ? this.originalName.hashCode() : 0);
        return hash;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public void select(boolean selected) {
        this.selected = selected;
        if (!selected) {
            dropShadow.setColor(Color.WHITESMOKE);
            dropShadow.setSpread(.2);
            dropShadow.setWidth(20);
            dropShadow.setHeight(20);

            vBoxWrapper.getStyleClass().remove("nodoSeleccionado");
            vBoxWrapper.getStyleClass().add("nodoNoSeleccionado");
        } else {
            nodeAdminAbstractController.queryProperties(this);
            vBoxWrapper.getStyleClass().remove("nodoNoSeleccionado");
            vBoxWrapper.getStyleClass().add("nodoSeleccionado");

            group.toFront();
            dropShadow.setColor(Color.web("#44FF00"));
            dropShadow.setSpread(.2);
            dropShadow.setWidth(25);
            dropShadow.setHeight(25);
        }
    }

    @Override
    public String toString() {
        return name;
    }

    private void establishEventOnMouseEntered() {
        group.setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                ActionTypeEmun actionTypeEmun = GUI.getActionTypeEmun();
                GraphNode graphNode = GraphNode.this;

                if (actionTypeEmun == ActionTypeEmun.LINK) {
                    group.setCursor(actionTypeEmun.getOverCursorImage());
                } else {
                    group.setCursor(actionTypeEmun.getCursorImage());
                }

                if (linkBegin == false) {
                    wildcardNodeA = null;
                }

                if (actionTypeEmun == ActionTypeEmun.LINK) {

                    if (wildcardNodeA != null && wildcardNodeA != graphNode && GraphNode.linkBegin) {

                        if (wildcardNodeA.isEnableToCreateLInk(graphNode) && linkAdminAbstractController.canCreateLink(wildcardNodeA,graphNode)) {
                            graphDesignGroup.remove(wildcardLink);

                            GraphLink graphLink = new GraphLink(graphDesignGroup, wildcardNodeA, graphNode, linkAdminAbstractController);
                            graphLink.addInitialGraphArc();

                            wildcardNodeA.getGroup().toFront();
                            graphNode.getGroup().toFront();
                        } else {
                            graphNode.playDenyLinkAnimation();
                        }
                    }

                } else if (actionTypeEmun == ActionTypeEmun.DELETED) {
                    group.setCursor(actionTypeEmun.getOverCursorImage());
                }
                wildcardNodeA = null;
            }
        });
    }

    private void establishEventOnMousePressed() {
        group.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                setWidth((short) vBoxWrapper.getWidth());
                setHeight((short) vBoxWrapper.getHeight());

                if (GUI.getActionTypeEmun() == ActionTypeEmun.LINK) {

                    GraphNode graphNode = GraphNode.this;
                    wildcardNodeA = graphNode;

                    double x = graphNode.getLayoutX();
                    double y = graphNode.getLayoutY();
                    wildcardLink.setStartX(x + width / 2);
                    wildcardLink.setStartY(y + height / 2);
                    wildcardLink.setEndX(x + width / 2);
                    wildcardLink.setEndY(y + height / 2);
                    graphDesignGroup.add(wildcardLink);
                    wildcardLink.toFront();
                    graphNode.group.toFront();

                    if (mouseEvent.isPrimaryButtonDown() && group.isHover()) {
                        GraphNode.linkBegin = true;
                    }
                }
                group.setScaleX(1);
                group.setScaleY(-1);
            }
        });
    }

    private void establishEventOnMouseDragged() {
        group.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {


                if (GUI.getActionTypeEmun() == ActionTypeEmun.POINTER) {
                    setLayoutX(getLayoutX() + mouseEvent.getX() - width / 2);
                    setLayoutY(getLayoutY() - (mouseEvent.getY() - height / 2));
                    updateNodeListener();
                } else if (GUI.getActionTypeEmun() == ActionTypeEmun.LINK) {
                    dragging = true;
                    wildcardLink.setEndX(getLayoutX() + (mouseEvent.getX()));
                    wildcardLink.setEndY(getLayoutY() + height - (mouseEvent.getY()));
                }
            }
        });
    }

    private void establishEventOnMouseReleased() {
        group.setOnMouseReleased(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                group.setScaleX(0.5);
                group.setScaleY(-0.5);
                if (GUI.getActionTypeEmun() == ActionTypeEmun.LINK) {
                    graphDesignGroup.remove(wildcardLink);
                }
            }
        });
    }

    private void establishEventOnMouseClicked() {
        group.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                GraphNode graphNode = GraphNode.this;

                if (GUI.getActionTypeEmun() == ActionTypeEmun.DELETED) {

                    graphNode.setDeleted(true);
                    graphDesignGroup.remove(graphNode);
                    graphDesignGroup.deleteNodeFromGoList(graphNode);
                    nodeAdminAbstractController.removeNode(graphNode);
                }

                if (GUI.getActionTypeEmun() == ActionTypeEmun.POINTER) {

                    Selectable objSeleccionado = graphDesignGroup.getSelectable();

                    if (!dragging) {
                        if (objSeleccionado == graphNode) {
                            objSeleccionado.select(false);
                            graphDesignGroup.setSelectable(null);
                        } else {
                            if (objSeleccionado == null) {
                                graphNode.select(true);
                                graphDesignGroup.setSelectable(graphNode);
                            } else {
                                objSeleccionado.select(false);
                                graphNode.select(true);
                                graphDesignGroup.setSelectable(graphNode);
                            }
                        }

                    } else {
                        if (graphNode != objSeleccionado) {
                            if (objSeleccionado == null) {
                                graphNode.select(true);
                                graphDesignGroup.setSelectable(graphNode);
                            } else {
                                objSeleccionado.select(false);
                                graphNode.select(true);
                                graphDesignGroup.setSelectable(graphNode);
                            }
                        }
                    }
                    dragging = false;
                }
                updateNodeListener();
            }
        });
    }

    private void establishEventOnMouseExit() {
        group.setOnMouseExited(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    GraphNode.linkBegin = false;
                    wildcardNodeA = null;
                }
            }
        });
    }

    public void addNodeListener(NodeListener nodeListener) {
        nodeListeners.add(nodeListener);
    }

    public void removeNodeListener(NodeListener nodeListener) {
        nodeListeners.remove(nodeListener);
    }

    public void updateNodeListener() {
        layoutX = this.getLayoutX();
        layoutY = this.getLayoutY();

        for (NodeListener nodeListener : nodeListeners) {
            nodeListener.update();
        }
    }

    public Image getImage() {
        return image;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        lblName.setText(formatNameWithBreakSpaces(name));
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
            System.out.println("read :" + name);
            if (graphDesignGroup.isSerializableComplete()) {
                initTransientObjects();
                getGroup().setLayoutX(getLayoutX());
                getGroup().setLayoutY(getLayoutY());
                select(false);
                graphDesignGroup.getGroup().getChildren().add(getGroup());
                System.out.println("read load post :" + name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {

        if (initialHeight == 0) {
            setInitialHeight(height);
        }
        this.height = height;
    }

    public short getWidth() {
        return width;
    }

    public void setWidth(short ancho) {
        this.width = ancho;
    }
//FIXME:Eliminar este y dejar el generico y mira q enrutador no descuadre

    private String formatearNombre(String nombre) {

        if (nombre.startsWith("Enrutador")) {
            return nombre.substring(0, "Enrutador".length()) + "\n" + nombre.substring("Enrutador".length() + 1, nombre.length());
        }
        return nombre;
    }

    public boolean isLinkBegin() {
        return linkBegin;
    }

    public void setLinkBegin(boolean linkBegin) {
        this.linkBegin = linkBegin;
    }

    public String formatNameWithBreakSpaces(String nameToFormat) {

        StringBuilder alterName = new StringBuilder();

        nameToFormat = nameToFormat.trim();
        int tamaño = nameToFormat.length();
        int i = 0;

        while (tamaño >= lineBreakStep) {

            alterName.append(nameToFormat.substring(i * lineBreakStep, (i * lineBreakStep) + lineBreakStep)).append("\n");

            tamaño = nameToFormat.substring(((i * lineBreakStep) + lineBreakStep)).length();

            if (tamaño > 0 && tamaño < lineBreakStep) {
                alterName.append(nameToFormat.substring(((i * lineBreakStep) + lineBreakStep)));
            }

            i++;
        }
        setHeight((short) vBoxWrapper.getHeight());
        setWidth((short) vBoxWrapper.getWidth());
        updateNodeListener();
        return (alterName.length() == 0) ? nameToFormat : alterName.toString();
    }

    public Line getWildcardLink() {
        return wildcardLink;
    }

    public HashMap<String, String> getSubPropertiesNode() {
        return subNodeProperties;
    }

    public Group getGroup() {
        return group;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
        group.setLayoutX(layoutX);
    }

    public double getLayoutY() {
        return layoutY;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
        group.setLayoutY(layoutY);
    }

    private void playDenyLinkAnimation() {

        IMG_VW_DENY_LINK.setLayoutX(getLayoutX() + getWidth() / 2 - IMG_VW_DENY_LINK.getBoundsInParent().getWidth() / 2);
        IMG_VW_DENY_LINK.setLayoutY(getLayoutY() + 0.75 * getHeight() - (getInitialHeight() / 4 + IMG_VW_DENY_LINK.getBoundsInParent().getHeight() / 2));

        graphDesignGroup.add(IMG_VW_DENY_LINK);

        FadeTransition fadeImgDenyLink = new FadeTransition(Duration.millis(800), IMG_VW_DENY_LINK);
        fadeImgDenyLink.setFromValue(1.0);
        fadeImgDenyLink.setToValue(0);
        fadeImgDenyLink.play();

        fadeImgDenyLink.setOnFinished(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent arg0) {
                graphDesignGroup.remove(IMG_VW_DENY_LINK);
            }
        });
    }

    public abstract boolean isEnableToCreateLInk(GraphNode graphNode);
    
    private void writeObject(ObjectOutputStream stream) {
        try {
            stream.defaultWriteObject();
            Main.countObject++;
            System.out.println("Writing: " + Main.countObject + "  " + this.getClass().getCanonicalName());
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
