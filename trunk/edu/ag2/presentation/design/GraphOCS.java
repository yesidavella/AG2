/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ag2.presentation.design;

import edu.ag2.presentation.GUI;
import edu.ag2.presentation.control.PhosphorusPropertySet;
import edu.ag2.presentation.control.ResultsOCS;
import edu.ag2.util.Utils;
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
    private PhosphorusPropertySet tvSummaryOCS_CreatedInverted;
    PhosphorusPropertySet tvSummaryOCS_Traffic;
    PhosphorusPropertySet tvSummaryOCS_TrafficInverted;
    PhosphorusPropertySet tvSummaryOCS_JobSent;
    PhosphorusPropertySet tvSummaryOCS_JobSentInverted;
    PhosphorusPropertySet tvSummaryOCS_JobTraffic;
    PhosphorusPropertySet tvSummaryOCS_JobTrafficInverted;
    PhosphorusPropertySet tvSummaryOCS_RequestSent;
    PhosphorusPropertySet tvSummaryOCS_RequestSentInverted;
    PhosphorusPropertySet tvSummaryOCS_RequestTraffic;
    PhosphorusPropertySet tvSummaryOCS_RequestTrafficInverted;
    PhosphorusPropertySet tvSummaryOCS_ackRequestSent;
    PhosphorusPropertySet tvSummaryOCS_ackRequestSentInverted;
    PhosphorusPropertySet tvSummaryOCS_ackRequestTraffic;
    PhosphorusPropertySet tvSummaryOCS_ackRequestTrafficInverted;
    PhosphorusPropertySet tvSummaryOCS_ResultSent;
    PhosphorusPropertySet tvSummaryOCS_ResultSentInverted;
    PhosphorusPropertySet tvSummaryOCS_ResultTraffic;
    PhosphorusPropertySet tvSummaryOCS_ResultTrafficInverted;

    public GraphOCS(final GraphNode graphNodeSource, final GraphNode graphNodeDestination, GraphDesignGroup graphDesignGroup, int countInstanceOCS) {


        graphNodeSource.addNodeListener(this);
        graphNodeDestination.addNodeListener(this);
        group = new Group();
        line = new Line();
        countOCS = 0;
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
        tcValue.setMinWidth(80);
        tvSummaryOCS = new TableView();
        tvSummaryOCS.setMaxHeight(125);
        tvSummaryOCS.getColumns().addAll(tcProperty, tcValue);

        TableColumn tcProperty_Inverted = new TableColumn("Resultado");
        TableColumn tcValue_Inverted = new TableColumn("Valor");
        tcValue_Inverted.setMinWidth(80);
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
        tvSummaryOCS_CreatedInverted = new PhosphorusPropertySet();
        tvSummaryOCS_Traffic = new PhosphorusPropertySet();
        tvSummaryOCS_TrafficInverted = new PhosphorusPropertySet();

        tvSummaryOCS_JobSent = new PhosphorusPropertySet();
        tvSummaryOCS_JobSentInverted = new PhosphorusPropertySet();
        tvSummaryOCS_JobTraffic = new PhosphorusPropertySet();
        tvSummaryOCS_JobTrafficInverted = new PhosphorusPropertySet();

        tvSummaryOCS_RequestSent = new PhosphorusPropertySet();
        tvSummaryOCS_RequestSentInverted = new PhosphorusPropertySet();
        tvSummaryOCS_RequestTraffic = new PhosphorusPropertySet();
        tvSummaryOCS_RequestTrafficInverted = new PhosphorusPropertySet();

        tvSummaryOCS_ackRequestSent = new PhosphorusPropertySet();
        tvSummaryOCS_ackRequestSentInverted = new PhosphorusPropertySet();
        tvSummaryOCS_ackRequestTraffic = new PhosphorusPropertySet();
        tvSummaryOCS_ackRequestTrafficInverted = new PhosphorusPropertySet();

        tvSummaryOCS_ResultSent = new PhosphorusPropertySet();
        tvSummaryOCS_ResultSentInverted = new PhosphorusPropertySet();
        tvSummaryOCS_ResultTraffic = new PhosphorusPropertySet();
        tvSummaryOCS_ResultTrafficInverted = new PhosphorusPropertySet();





        tvSummaryOCS_Created.setProperty1("OCS Creados");
        tvSummaryOCS_CreatedInverted.setProperty1("OCS Creados");
        tvSummaryOCS_Traffic.setProperty1("Trafico total");
        tvSummaryOCS_TrafficInverted.setProperty1("Trafico total");

        tvSummaryOCS_JobSent.setProperty1("Trabajos enviados");
        tvSummaryOCS_JobSentInverted.setProperty1("Trabajos enviados");
        tvSummaryOCS_JobTraffic.setProperty1("Trafico trabajos");
        tvSummaryOCS_JobTrafficInverted.setProperty1("Trafico trabajos");



        tvSummaryOCS_RequestSent.setProperty1("Solicitudes enviadas");
        tvSummaryOCS_RequestSentInverted.setProperty1("Solicitudes enviadas");
        tvSummaryOCS_RequestTraffic.setProperty1("Trafico solicitudes");
        tvSummaryOCS_RequestTrafficInverted.setProperty1("Trafico solicitudes");

        tvSummaryOCS_ackRequestSent.setProperty1("Solicitudes(ack) enviadas");
        tvSummaryOCS_ackRequestSentInverted.setProperty1("Solicitudes(ack)  enviadas");
        tvSummaryOCS_ackRequestTraffic.setProperty1("Trafico solicitudes(ack)");
        tvSummaryOCS_ackRequestTrafficInverted.setProperty1("Trafico solicitudes(ack)");

        tvSummaryOCS_ResultSent.setProperty1("Respuestas enviadas");
        tvSummaryOCS_ResultSentInverted.setProperty1("Respuestas enviadas");
        tvSummaryOCS_ResultTraffic.setProperty1("Trafico respuestas");
        tvSummaryOCS_ResultTrafficInverted.setProperty1("Trafico respuestas");


        tpnDireccion1.setText(graphNodeSource.getName() + "->" + graphNodeDestination.getName());
        tpnDireccion2.setText(graphNodeDestination.getName() + "->" + graphNodeSource.getName());


        dataSummaryOCS.addAll(
                tvSummaryOCS_Created,
                tvSummaryOCS_Traffic,
                tvSummaryOCS_JobSent,
                tvSummaryOCS_JobTraffic,
                tvSummaryOCS_RequestSent,
                tvSummaryOCS_RequestTraffic,
                tvSummaryOCS_ackRequestSent,
                tvSummaryOCS_ackRequestTraffic,
                tvSummaryOCS_ResultSent,
                tvSummaryOCS_ResultTraffic);


        dataSummaryOCS_Inverted.addAll(
                tvSummaryOCS_CreatedInverted,
                tvSummaryOCS_TrafficInverted,
                tvSummaryOCS_JobSentInverted,
                tvSummaryOCS_JobTrafficInverted,
                tvSummaryOCS_RequestSentInverted,
                tvSummaryOCS_RequestTrafficInverted,
                tvSummaryOCS_ackRequestSentInverted,
                tvSummaryOCS_ackRequestTrafficInverted,
                tvSummaryOCS_ResultSentInverted,
                tvSummaryOCS_ResultTrafficInverted);

        setInitValue(tvSummaryOCS_Created,
                tvSummaryOCS_Traffic,
                tvSummaryOCS_JobSent,
                tvSummaryOCS_JobTraffic,
                tvSummaryOCS_RequestSent,
                tvSummaryOCS_RequestTraffic,
                tvSummaryOCS_ackRequestSent,
                tvSummaryOCS_ackRequestTraffic,
                tvSummaryOCS_ResultSent,
                tvSummaryOCS_ResultTraffic,
                tvSummaryOCS_CreatedInverted,
                tvSummaryOCS_TrafficInverted,
                tvSummaryOCS_JobSentInverted,
                tvSummaryOCS_JobTrafficInverted,
                tvSummaryOCS_RequestSentInverted,
                tvSummaryOCS_RequestTrafficInverted,
                tvSummaryOCS_ackRequestSentInverted,
                tvSummaryOCS_ackRequestTrafficInverted,
                tvSummaryOCS_ResultSentInverted,
                tvSummaryOCS_ResultTrafficInverted);

        tvSummaryOCS.setItems(dataSummaryOCS);
        tvSummaryOCS_Inverted.setItems(dataSummaryOCS_Inverted);
        tpnDireccion1.setContent(tvSummaryOCS);
        tpnDireccion2.setContent(tvSummaryOCS_Inverted);


    }

    private void setInitValue(PhosphorusPropertySet... argPropertySet) {
        for (PhosphorusPropertySet phosphorusPropertySet : argPropertySet) {
            phosphorusPropertySet.setProperty2(" " + 0);
        }
    }

    public void showLabel(double x, double y) {

        vBoxInfoOCS.setVisible(true);
        vBoxInfoOCS.setLayoutX(x );
        vBoxInfoOCS.setLayoutY(y );
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

    public void setOCSJobSent(long jobSent) {

        tvSummaryOCS_JobSent.setProperty2(" " + jobSent);

    }

    public void setOCSJobSentInverted(long jobSent) {

        tvSummaryOCS_JobSentInverted.setProperty2(" " + jobSent);

    }

    public void setTraffic(double traffic) {

        tvSummaryOCS_Traffic.setProperty2(" " + traffic);

    }

    public void setTrafficInverted(double traffic) {
        tvSummaryOCS_TrafficInverted.setProperty2(" " + traffic);

    }

    public void addInstanceOCS_Inverted() {
        if (countOCS_Inverted == 0) {
            addDirection();
        }
        countOCS_Inverted++;
        tvSummaryOCS_CreatedInverted.setProperty2(" " + countOCS_Inverted);
    }

    public void setJobTraffic(double traffic) {
        tvSummaryOCS_JobTraffic.setProperty2(" " + traffic);
    }

    public void setJobTrafficInverted(double traffic) {
        tvSummaryOCS_JobTrafficInverted.setProperty2(" " + traffic);
    }

    public void setRequestJobSent(long jobSent) {
        tvSummaryOCS_RequestSent.setProperty2(" " + jobSent);
    }

    public void setRequestJobSentInverted(long jobSent) {
        tvSummaryOCS_RequestSentInverted.setProperty2(" " + jobSent);
    }

    public void setRequestJobTraffic(double traffic) {

        tvSummaryOCS_RequestTraffic.setProperty2(" " + traffic);

    }

    public void setRequestJobTrafficInverted(double traffic) {
        tvSummaryOCS_RequestTrafficInverted.setProperty2(" " + traffic);
    }

    public void set_ackRequestJobSent(long jobSent) {
        tvSummaryOCS_ackRequestSent.setProperty2(" " + jobSent);
    }

    public void set_ackRequestJobSentInverted(long jobSent) {
        tvSummaryOCS_ackRequestSentInverted.setProperty2(" " + jobSent);
    }

    public void set_ackRequestJobTraffic(double traffic) {
        tvSummaryOCS_ackRequestTraffic.setProperty2(" " + traffic);
    }

    public void set_ackRequestJobTrafficInverted(double traffic) {
        tvSummaryOCS_ackRequestTrafficInverted.setProperty2(" " + traffic);
    }

    public void setResultJobSent(long jobSent) {

        tvSummaryOCS_ResultSent.setProperty2(" " + jobSent);

    }

    public void setResultJobSentInverted(long jobSent) {

        tvSummaryOCS_ResultSentInverted.setProperty2(" " + jobSent);

    }

    public void setResultJobTraffic(double traffic) {

        tvSummaryOCS_ResultTraffic.setProperty2(" " + traffic);

    }

    public void setResultJobTrafficInverted(double traffic) {
        tvSummaryOCS_ResultTrafficInverted.setProperty2(" " + traffic);
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

    public VBox getvBoxInfoOCS() {
        return vBoxInfoOCS;
    }
    
    
}
