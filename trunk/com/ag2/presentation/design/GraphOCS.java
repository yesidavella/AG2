/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.design;

import com.ag2.presentation.control.PhosphorusPropertySet;
import com.ag2.presentation.control.ResultsOCS;
import com.ag2.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import org.apache.bcel.generic.LLOAD;

/**
 *
 * @author Frank
 */
public class GraphOCS implements NodeListener {

    private Line line;
    private GraphNode graphNodeSource;
    private GraphNode graphNodeDestination;
    private GraphDesignGroup graphDesignGroup;
    private Label lblCountOCS;
    private int countOCS;
    private int countOCS_Inverted = 0;
    private Group group;
    private int posStartX;
    private int posStartY;
    private int posEndX;
    private int posEndY;
    private int desfaseX = 24;
    private int desfaseY = 34;
    private Line lineMiddle = new Line();
    private TitledPane tpnDireccion1 = new TitledPane();
    private TitledPane tpnDireccion2 = new TitledPane();
    private Accordion accordion = new Accordion();
    private VBox vBoxInfoOCS = new VBox();
    private ImageView ivwClose = new ImageView(new Image(Utils.ABS_PATH_IMGS + "close_blue.jpg"));
    private Button btnClose = new Button();
    private ObservableList<PhosphorusPropertySet> dataSummaryOCS;
    private ObservableList<PhosphorusPropertySet> dataSummaryOCS_Inverted;
    private TableView tvSummaryOCS;
    private TableView tvSummaryOCS_Inverted;
    private PhosphorusPropertySet tvSummaryOCS_Created;
    PhosphorusPropertySet tvSummaryOCS_Created_Inverted;
    PhosphorusPropertySet tvSummaryOCS_Traffic;
    PhosphorusPropertySet tvSummaryOCS_Traffic_Inverted;

    public GraphOCS(final GraphNode graphNodeSource, final GraphNode graphNodeDestination, GraphDesignGroup graphDesignGroup, int countInstanceOCS) {


        graphNodeSource.addNodeListener(this);
        graphNodeDestination.addNodeListener(this);
        group = new Group();
        line = new Line();
        countOCS = 1;
        lblCountOCS = new Label();
        this.graphNodeSource = graphNodeSource;
        this.graphNodeDestination = graphNodeDestination;
        this.graphDesignGroup = graphDesignGroup;


        DropShadow dropShadow = new DropShadow();
        dropShadow.setSpread(0.5);
        dropShadow.setWidth(10);
        dropShadow.setHeight(10);

        line.setEffect(dropShadow);


        line.setStroke(Color.web("#5B88E0"));
        dropShadow.setColor(Color.BLACK);



        posStartX = (int) graphNodeSource.getLayoutX() + desfaseX + (graphNodeSource.getWidth() / 2);
        posStartY = (int) graphNodeSource.getLayoutY() + desfaseY + (graphNodeSource.getHeight() / 2);

        posEndX = (int) graphNodeDestination.getLayoutX() + desfaseX + graphNodeSource.getWidth() / 2;
        posEndY = (int) graphNodeDestination.getLayoutY() + desfaseY + (graphNodeSource.getHeight() / 2);

        lblCountOCS.setScaleX(0.9);
        lblCountOCS.setScaleY(-.9);
        lblCountOCS.setStyle(" -fx-text-fill: #585858;-fx-font: bold 6pt 'Arial'; -fx-background-color:#FFFFFF;"
                + "-fx-border-style: solid; -fx-border-color:#2E64FE");
        lblCountOCS.setVisible(false);
        DropShadow dropShadow1 = new DropShadow();
        lblCountOCS.setEffect(dropShadow1);

        accordion.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                lblCountOCS.setScaleX(1.7);
                lblCountOCS.setScaleY(-1.7);
            }
        });


        line.setStartX(posStartX);
        line.setStartY(posStartY);
        line.setEndX(posEndX);
        line.setEndY(posEndY);

        line.setStrokeWidth(3);
        group.getChildren().addAll(line);

        btnClose.setGraphic(ivwClose);
        btnClose.setMaxSize(10, 10);
        btnClose.setMinSize(10, 10);
        btnClose.setPrefSize(10, 10);
        btnClose.setScaleX(0.7);
        btnClose.setScaleY(0.7);
        vBoxInfoOCS.setAlignment(Pos.CENTER_RIGHT);

        btnClose.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                vBoxInfoOCS.setVisible(false);
            }
        });
        vBoxInfoOCS.setSpacing(1);
        vBoxInfoOCS.getChildren().addAll(btnClose, accordion);
        graphDesignGroup.getGroup().getChildren().addAll(group, vBoxInfoOCS);

        group.toFront();
        graphNodeDestination.getGroup().toFront();
        graphNodeSource.getGroup().toFront();

        line.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                GraphOCS.this.showLabel(arg0.getX(), arg0.getY());
            }
        });

        accordion.getPanes().add(tpnDireccion1);
        accordion.getPanes().add(tpnDireccion2);
        vBoxInfoOCS.setVisible(false);

        TableColumn tcProperty = new TableColumn("Resultado");
        TableColumn tcValue = new TableColumn("Valor");
        tcValue.setMinWidth(100);
        tvSummaryOCS = new TableView();
        tvSummaryOCS.setMaxHeight(125);
        tvSummaryOCS.getColumns().addAll(tcProperty, tcValue);

        TableColumn tcProperty_Inverted = new TableColumn("Resultado");
        TableColumn tcValue_Inverted = new TableColumn("Valor");
        tcValue_Inverted.setMinWidth(100);
        tvSummaryOCS_Inverted = new TableView();
        tvSummaryOCS_Inverted.setMaxHeight(125);
        tvSummaryOCS_Inverted.getColumns().addAll(tcProperty_Inverted, tcValue_Inverted);



        dataSummaryOCS = FXCollections.observableArrayList();
        dataSummaryOCS_Inverted = FXCollections.observableArrayList();

        tcProperty.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property1"));
        tcValue.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property2"));

        tcProperty_Inverted.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property1"));
        tcValue_Inverted.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property2"));


        tvSummaryOCS_Created = new PhosphorusPropertySet();
        tvSummaryOCS_Created_Inverted = new PhosphorusPropertySet();
        tvSummaryOCS_Traffic = new PhosphorusPropertySet();
        tvSummaryOCS_Traffic_Inverted = new PhosphorusPropertySet();

        tvSummaryOCS_Created.setProperty1("OCS Creados");
        tvSummaryOCS_Created_Inverted.setProperty1("OCS Creados");
        tvSummaryOCS_Traffic.setProperty1("Trafico total");
        tvSummaryOCS_Traffic_Inverted.setProperty1("Trafico total");

        tpnDireccion1.setText(graphNodeSource.getName() + "->" + graphNodeDestination.getName());
        tpnDireccion2.setText(graphNodeDestination.getName() + "->" + graphNodeSource.getName());


        dataSummaryOCS_Inverted.addAll(tvSummaryOCS_Created_Inverted, tvSummaryOCS_Traffic_Inverted);
        tvSummaryOCS.setItems(dataSummaryOCS);
        tvSummaryOCS_Inverted.setItems(dataSummaryOCS_Inverted);
        tpnDireccion1.setContent(tvSummaryOCS);
        tpnDireccion2.setContent(tvSummaryOCS_Inverted);
        dataSummaryOCS.addAll(tvSummaryOCS_Created, tvSummaryOCS_Traffic);
        
        tvSummaryOCS_Created.setProperty2(" "+0);
        tvSummaryOCS_Created_Inverted.setProperty2(" "+0);
        tvSummaryOCS_Traffic.setProperty2(" "+0);
        tvSummaryOCS_Traffic_Inverted.setProperty2(" "+0);
        
        

    }

    public void showLabel(double x, double y) {

        vBoxInfoOCS.setVisible(true);
        vBoxInfoOCS.setLayoutX(x - 10);
        vBoxInfoOCS.setLayoutY(y - 10);
        vBoxInfoOCS.setScaleX(0.7);
        vBoxInfoOCS.setScaleY(-.7);
        vBoxInfoOCS.toFront();


    }

    public void remove() {
//        //System.out.println("Elminando linea  "+graphNodeSource.getName()+" -  "+graphNodeDestination.getName());
        graphDesignGroup.getGroup().getChildren().removeAll(group, lblCountOCS);
        graphDesignGroup.getLinesOCS().remove(group);


    }

    public Group getGroup() {
        return group;
    }

    public void addInstanceOCS() {
        countOCS++;
        tvSummaryOCS_Created.setProperty2(" " + countOCS);
        

    }

    public void setTraffic(double traffic) {
        
        tvSummaryOCS_Traffic.setProperty2(" " + traffic);

    }

    public void setTrafficInverted(double traffic) {
    
        tvSummaryOCS_Traffic_Inverted.setProperty2(" " + traffic);

    }

    public void addInstanceOCS_Inverted() {
        if (countOCS_Inverted == 0) {
            addDirection();
        }
        countOCS_Inverted++;
            tvSummaryOCS_Created_Inverted.setProperty2(" " + countOCS_Inverted);
    }

    private void addDirection() {

        lineMiddle.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                GraphOCS.this.showLabel(arg0.getX(), arg0.getY());
            }
        });
        lineMiddle.setStroke(Color.web("#000000"));
        lineMiddle.setStartX(posStartX);
        lineMiddle.setStartY(posStartY);
        lineMiddle.setEndX(posEndX);
        lineMiddle.setEndY(posEndY);
        line.setStrokeWidth(7);
        group.getChildren().addAll(lineMiddle);
    }

    public Label getLblCountOCS() {
        return lblCountOCS;
    }

    @Override
    public void update() {

        posStartX = (int) graphNodeSource.getLayoutX() + desfaseX + (graphNodeSource.getWidth() / 2);
        posStartY = (int) graphNodeSource.getLayoutY() + desfaseY + (graphNodeSource.getHeight() / 2);

        posEndX = (int) graphNodeDestination.getLayoutX() + desfaseX + graphNodeSource.getWidth() / 2;
        posEndY = (int) graphNodeDestination.getLayoutY() + desfaseY + (graphNodeSource.getHeight() / 2);

        line.setStartX(posStartX);
        line.setStartY(posStartY);
        line.setEndX(posEndX);
        line.setEndY(posEndY);

        lineMiddle.setStartX(posStartX);
        lineMiddle.setStartY(posStartY);
        lineMiddle.setEndX(posEndX);
        lineMiddle.setEndY(posEndY);


    }
}
