package edu.ag2.presentation.design;

import edu.ag2.controller.FiberAdminController;
import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import edu.ag2.controller.OCSAdminController;
import edu.ag2.presentation.ActionTypeEmun;
import edu.ag2.presentation.GUI;
import edu.ag2.presentation.Main;
import edu.ag2.presentation.images.ImageHelper;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private static ImageView IMV_DENY_LINK = new ImageView(new Image(ImageHelper.getResourceInputStream("prohibido_enlace.png")));
    public static boolean linkBegin = false;
    private static GraphNode wildcardNodeA = null;
    protected transient Image image = null;
    protected transient Image imageNode = null;
    private transient Line wildcardLink;
    private transient Line wildcardOCS;
    private transient ImageView imageView;
    private transient ImageView imageViewNode;
    protected transient Label lblName;
    private transient DropShadow dropShadow;
    protected transient VBox vbxWrapper;
    private transient Group group;
    private String imageURL;
    private String imageURLNode;
    private String name = null;
    private ArrayList<NodeListener> nodeListeners = new ArrayList<NodeListener>();
    private boolean deleted = false;
    private boolean selected = false;
    private boolean dragging = false;
    private NodeAdminAbstractController nodeAdminController;
    private List<LinkAdminAbstractController> linkAdminControllers;
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
    private boolean showSimpleNode = false;

    public GraphNode(GraphDesignGroup graphDesignGroup,
            String name,
            String imageURL,
            String imageURL_View,
            NodeAdminAbstractController nodeAdminAbstractController,
            List<LinkAdminAbstractController> linkAdminControllers,
            short lineBreakStep) {

        this.graphDesignGroup = graphDesignGroup;
        this.nodeAdminController = nodeAdminAbstractController;
        this.linkAdminControllers = linkAdminControllers;
        this.imageURL = imageURL;
        this.imageURLNode = imageURL_View;
        this.name = name;
        this.originalName = name;
        nodeProperties = new HashMap<String, String>();
        subNodeProperties = new HashMap<String, String>();
        this.lineBreakStep = lineBreakStep;
        initTransientObjects();

    }

    public void showSimpleNode() {
        if (!showSimpleNode) {

            vbxWrapper.getChildren().remove(imageView);
            if (!vbxWrapper.getChildren().contains(imageViewNode)) {
                vbxWrapper.getChildren().add(0, imageViewNode);
            }

            lblName.setStyle("-fx-font: bold 12pt 'Arial'; -fx-background-color:white");
            group.setScaleX(0.5);
            group.setScaleY(-0.5);
        }

        showSimpleNode = true;
    }

    public void hideSimpleNode() {
        if (showSimpleNode) {

            if (!vbxWrapper.getChildren().contains(imageView)) {
                vbxWrapper.getChildren().add(0, imageView);
            }

            vbxWrapper.getChildren().remove(imageViewNode);

            lblName.setStyle("-fx-font: bold 12pt 'Arial'; -fx-background-color:#CCD4EC");
            group.setScaleX(0.5);
            group.setScaleY(-0.5);
        }
        showSimpleNode = false;

    }

    public void initTransientObjects() {

        showSimpleNode = false;
        group = new Group();
        dropShadow = new DropShadow();
        group.setEffect(dropShadow);
        lblName = new Label(formatNameWithBreakSpaces(name));
        lblName.setTextFill(Color.BLACK);
        lblName.setTextAlignment(TextAlignment.CENTER);
        lblName.setStyle("-fx-font: bold 12pt 'Arial'; -fx-background-color:#CCD4EC");

        wildcardLink = new Line();
        wildcardOCS = new Line();
        vbxWrapper = new VBox();
        image = new Image(ImageHelper.getResourceInputStream(imageURL));
        imageNode = new Image(ImageHelper.getResourceInputStream(imageURLNode));

        imageView = new ImageView(image);
        imageViewNode = new ImageView(imageNode);

        vbxWrapper.setAlignment(Pos.CENTER);
        vbxWrapper.getChildren().addAll(imageView, lblName);
        
        group.getChildren().addAll(vbxWrapper);
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

            vbxWrapper.getStyleClass().remove("nodoSeleccionado");
            vbxWrapper.getStyleClass().add("nodoNoSeleccionado");
        } else {
            nodeAdminController.queryProperties(this);
            vbxWrapper.getStyleClass().remove("nodoNoSeleccionado");
            vbxWrapper.getStyleClass().add("nodoSeleccionado");

            group.toFront();
            dropShadow.setColor(Color.web("#44FF00"));
            dropShadow.setSpread(.2);
            dropShadow.setWidth(25);
            dropShadow.setHeight(25);
        }
        
        if(vbxWrapper.getWidth()!=0){
            setWidth((short) vbxWrapper.getWidth());
            setHeight((short) vbxWrapper.getHeight());
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

                if (actionTypeEmun == ActionTypeEmun.LINK || actionTypeEmun == ActionTypeEmun.OCS_CIRCUIT) {
                    group.setCursor(actionTypeEmun.getOverCursorImage());
                } else {
                    group.setCursor(actionTypeEmun.getCursorImage());
                }

                if (linkBegin == false) {
                    wildcardNodeA = null;
                }

                if (actionTypeEmun == ActionTypeEmun.LINK) {

                    if (wildcardNodeA != null && wildcardNodeA != graphNode && GraphNode.linkBegin) {

                        for (LinkAdminAbstractController linkAdminCtr : linkAdminControllers) {

                            if (linkAdminCtr instanceof FiberAdminController) {

                                if (wildcardNodeA.isEnableToCreateLink(graphNode) && linkAdminCtr.canCreateLink(wildcardNodeA, graphNode)) {

                                    graphDesignGroup.remove(wildcardLink);

                                    linkAdminCtr.createLink(wildcardNodeA, graphNode);

                                    wildcardNodeA.getGroup().toFront();
                                    graphNode.getGroup().toFront();

                                } else {
                                    graphNode.playDenyLinkAnimation();
                                }
                            }
                        }
                    }

                } else if (actionTypeEmun == actionTypeEmun.OCS_CIRCUIT) {

                    if (wildcardNodeA != null && wildcardNodeA != graphNode && GraphNode.linkBegin) {

                        graphDesignGroup.remove(wildcardOCS);

                        for (LinkAdminAbstractController linkAdminCtr : linkAdminControllers) {

                            if (linkAdminCtr instanceof OCSAdminController) {
                                linkAdminCtr.createLink(wildcardNodeA, graphNode);
                            }
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

                if (GraphNode.this.showSimpleNode) {
                    return;
                }

                setWidth((short) vbxWrapper.getWidth());
                setHeight((short) vbxWrapper.getHeight());

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
                } else if (GUI.getActionTypeEmun() == ActionTypeEmun.OCS_CIRCUIT) {

                    GraphNode graphNode = GraphNode.this;
                    wildcardNodeA = graphNode;
                    GraphNode.linkBegin = true;

                    double x = graphNode.getLayoutX();
                    double y = graphNode.getLayoutY();
                    wildcardOCS.setStartX(x + width / 2);
                    wildcardOCS.setStartY(y + height / 2);
                    wildcardOCS.setEndX(x + width / 2);
                    wildcardOCS.setEndY(y + height / 2);
                    graphDesignGroup.add(wildcardOCS);
                    wildcardOCS.toFront();
                    graphNode.group.toFront();

                }
                group.setScaleX(1);
                group.setScaleY(-1);
            }
        });
    }

    private void establishEventOnMouseDragged() {
        group.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                if (GraphNode.this.showSimpleNode) {
                    return;
                }

                if (GUI.getActionTypeEmun() == ActionTypeEmun.POINTER) {
                    setLayoutX(getLayoutX() + mouseEvent.getX() - width / 2);
                    setLayoutY(getLayoutY() - (mouseEvent.getY() - height / 2));
                    updateNodeListener();
                } else if (GUI.getActionTypeEmun() == ActionTypeEmun.LINK) {
                    dragging = true;
                    wildcardLink.setEndX(getLayoutX() + (mouseEvent.getX()));
                    wildcardLink.setEndY(getLayoutY() + height - (mouseEvent.getY()));

                } else if (GUI.getActionTypeEmun() == ActionTypeEmun.OCS_CIRCUIT) {
                    wildcardOCS.setEndX(getLayoutX() + (mouseEvent.getX()));
                    wildcardOCS.setEndY(getLayoutY() + height - (mouseEvent.getY()));
                }
            }
        });
    }

    private void establishEventOnMouseReleased() {
        group.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {

                if (GraphNode.this.showSimpleNode) {
                    return;
                }
                group.setScaleX(0.5);
                group.setScaleY(-0.5);
                if (GUI.getActionTypeEmun() == ActionTypeEmun.LINK) {
                    graphDesignGroup.remove(wildcardLink);
                } else if (GUI.getActionTypeEmun() == ActionTypeEmun.OCS_CIRCUIT) {
                    graphDesignGroup.remove(wildcardOCS);
                }
            }
        });
    }

    private void establishEventOnMouseClicked() {
        group.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {

                if (GraphNode.this.showSimpleNode) {
                    return;
                }

                GraphNode graphNode = GraphNode.this;

                if (GUI.getActionTypeEmun() == ActionTypeEmun.DELETED) {

                    graphNode.setDeleted(true);
                    graphDesignGroup.remove(graphNode);
                    graphDesignGroup.deleteNodeFromGoList(graphNode);
                    nodeAdminController.removeNode(graphNode);
                }

                if (GUI.getActionTypeEmun() == ActionTypeEmun.POINTER) {

                    Selectable selectedObj = graphDesignGroup.getSelectable();

                    if (!dragging) {
                        if (selectedObj == graphNode) {
                            selectedObj.select(false);
                            graphDesignGroup.setSelectable(null);
                        } else {
                            if (selectedObj == null) {
                                graphNode.select(true);
                                graphDesignGroup.setSelectable(graphNode);
                            } else {
                                selectedObj.select(false);
                                graphNode.select(true);
                                graphDesignGroup.setSelectable(graphNode);
                            }
                        }

                    } else {
                        if (graphNode != selectedObj) {
                            if (selectedObj == null) {
                                graphNode.select(true);
                                graphDesignGroup.setSelectable(graphNode);
                            } else {
                                selectedObj.select(false);
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
                if (GraphNode.this.showSimpleNode) {
                    return;
                }
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
            if (graphDesignGroup.isSerializableComplete()) {
                initTransientObjects();
                getGroup().setLayoutX(getLayoutX());
                getGroup().setLayoutY(getLayoutY());
                select(false);
                graphDesignGroup.getGroup().getChildren().add(getGroup());
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

    public void setWidth(short width) {
        this.width = width;
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
        int lengthName = nameToFormat.length();
        int i = 0;

        while (lengthName >= lineBreakStep) {

            alterName.append(nameToFormat.substring(i * lineBreakStep, (i * lineBreakStep) + lineBreakStep)).append("\n");

            lengthName = nameToFormat.substring(((i * lineBreakStep) + lineBreakStep)).length();

            if (lengthName > 0 && lengthName < lineBreakStep) {
                alterName.append(nameToFormat.substring(((i * lineBreakStep) + lineBreakStep)));
            }

            i++;
        }

        if (vbxWrapper != null) {
            setHeight((short) vbxWrapper.getHeight());
            setWidth((short) vbxWrapper.getWidth());
            updateNodeListener();
        }

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

        IMV_DENY_LINK.setLayoutX(getLayoutX() + getWidth() / 2 - IMV_DENY_LINK.getBoundsInParent().getWidth() / 2);
        IMV_DENY_LINK.setLayoutY(getLayoutY() + 0.75 * getHeight() - (getInitialHeight() / 4 + IMV_DENY_LINK.getBoundsInParent().getHeight() / 2));

        graphDesignGroup.add(IMV_DENY_LINK);

        FadeTransition fadeImgDenyLink = new FadeTransition(Duration.millis(800), IMV_DENY_LINK);
        fadeImgDenyLink.setFromValue(1.0);
        fadeImgDenyLink.setToValue(0);
        fadeImgDenyLink.play();

        fadeImgDenyLink.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent arg0) {
                graphDesignGroup.remove(IMV_DENY_LINK);
            }
        });
    }

    public abstract boolean isEnableToCreateLink(GraphNode graphNode);

    private void writeObject(ObjectOutputStream stream) {
        try {

            ArrayList<NodeListener> nodeListenersToDelete = new ArrayList<NodeListener>();
            for (NodeListener nodeListener : nodeListeners) {
                if (nodeListener instanceof GraphOCS) {
                    nodeListenersToDelete.add(nodeListener);
                }
            }
            nodeListeners.removeAll(nodeListenersToDelete);
            stream.defaultWriteObject();
            Main.countObject++;
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
