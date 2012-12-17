/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ag2.presentation.design;

import edu.ag2.presentation.Main;
import edu.ag2.presentation.control.PhosphorusPropertySet;
import edu.ag2.presentation.images.ImageHelper;
import edu.ag2.util.Utils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.animation.StrokeTransition;
import javafx.animation.StrokeTransitionBuilder;
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
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class GraphOCS implements NodeListener {

    private Line line;
    private GraphNode graphNodeSource;
    private GraphNode graphNodeDestination;
    private GraphDesignGroup graphDesignGroup;
    private Label lblCountOCS;
    private int countOCS;
    private int deteledCountOCS;
    private int countOCS_Inverted = 0;
    private int deteledCountOCS_Inverted = 0;
    private Group group;
    private double posStartX;
    private double posStartY;
    private double posEndX;
    private double posEndY;
    private final int desfaseX = 24;
    private final int desfaseY = 34;
    private Line lineMiddle = new Line();
    private TitledPane tpnDireccion1 = new TitledPane();
    private TitledPane tpnDireccion2 = new TitledPane();
    private Accordion accordion = new Accordion();
    private VBox vbxInfoOCS = new VBox();
    private ImageView ivwClose = new ImageView(new Image(ImageHelper.getResourceInputStream("close_blue.jpg")));
    private Button btnClose = new Button();
    private ObservableList<PhosphorusPropertySet> dataSummaryOCS;
    private ObservableList<PhosphorusPropertySet> dataSummaryOCS_Inverted;
    private TableView tvSummaryOCS;
    private TableView tvSummaryOCS_Inverted;
    private PhosphorusPropertySet tvSummaryOCS_Created;
    private PhosphorusPropertySet tvSummaryOCS_CreatedInverted;
    private PhosphorusPropertySet tvSummaryOCS_Deleted;
    private PhosphorusPropertySet tvSummaryOCS_DeletedInverted;
    private PhosphorusPropertySet tvSummaryOCS_Traffic;
    private PhosphorusPropertySet tvSummaryOCS_TrafficInverted;
    private PhosphorusPropertySet tvSummaryOCS_JobSent;
    private PhosphorusPropertySet tvSummaryOCS_JobSentInverted;
    private PhosphorusPropertySet tvSummaryOCS_JobTraffic;
    private PhosphorusPropertySet tvSummaryOCS_JobTrafficInverted;
    private PhosphorusPropertySet tvSummaryOCS_RequestSent;
    private PhosphorusPropertySet tvSummaryOCS_RequestSentInverted;
    private PhosphorusPropertySet tvSummaryOCS_RequestTraffic;
    private PhosphorusPropertySet tvSummaryOCS_RequestTrafficInverted;
    private PhosphorusPropertySet tvSummaryOCS_ackRequestSent;
    private PhosphorusPropertySet tvSummaryOCS_ackRequestSentInverted;
    private PhosphorusPropertySet tvSummaryOCS_ackRequestTraffic;
    private PhosphorusPropertySet tvSummaryOCS_ackRequestTrafficInverted;
    private PhosphorusPropertySet tvSummaryOCS_ResultSent;
    private PhosphorusPropertySet tvSummaryOCS_ResultSentInverted;
    private PhosphorusPropertySet tvSummaryOCS_ResultTraffic;
    private PhosphorusPropertySet tvSummaryOCS_ResultTrafficInverted;
    private StrokeTransition strokeTransition;
    private StrokeTransition strokeTransitionDeleted;
    private StrokeTransition strokeTransitionOut;
    private final AudioClip bar1Note = new AudioClip(Main.class.getResource("Note1.wav").toString());
    private final AudioClip bar2Note = new AudioClip(Main.class.getResource("Note8.wav").toString());
    private NumberFormat numberFormat = DecimalFormat.getInstance();
    private String TRAFFIC_UNIT = "(MB)";

    public GraphOCS(final GraphNode graphNodeSource, final GraphNode graphNodeDestination, final GraphDesignGroup graphDesignGroup, int countInstanceOCS) {



        numberFormat.setMaximumFractionDigits(3);

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
        
        posStartX = (int) graphNodeSource.getLayoutX() + 0.85*graphNodeSource.getWidth();
        posStartY = (int) graphNodeSource.getLayoutY() + desfaseY + (graphNodeSource.getHeight() / 2);

        posEndX =  graphNodeDestination.getLayoutX()  + 0.85*graphNodeSource.getWidth();
        posEndY = (int) graphNodeDestination.getLayoutY() + desfaseY + (graphNodeSource.getHeight() / 2);
//        posStartX = (int) graphNodeSource.getLayoutX() + 0 ;
//        posStartY = (int) graphNodeSource.getLayoutY() + (graphNodeSource.getHeight() );
//
//        posEndX = (int) graphNodeDestination.getLayoutX()  + graphNodeDestination.getWidth() ;
//        posEndY = (int) graphNodeDestination.getLayoutY()  + (graphNodeDestination.getHeight() );

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
        vbxInfoOCS.setAlignment(Pos.CENTER_RIGHT);

        btnClose.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
//                vbxInfoOCS.setVisible(false);

                graphDesignGroup.getGroup().getChildren().remove(vbxInfoOCS);

            }
        });
        vbxInfoOCS.setSpacing(1);
        vbxInfoOCS.getChildren().addAll(btnClose, accordion);
        graphDesignGroup.getGroup().getChildren().addAll(group);

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
//        vbxInfoOCS.setVisible(false);

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

        tvSummaryOCS_Deleted = new PhosphorusPropertySet();
        tvSummaryOCS_DeletedInverted = new PhosphorusPropertySet();


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

        tvSummaryOCS_Deleted.setProperty1("OCS Eliminados");
        tvSummaryOCS_DeletedInverted.setProperty1("OCS Eliminados");

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
                tvSummaryOCS_Deleted,
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
                tvSummaryOCS_DeletedInverted,
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
                tvSummaryOCS_Deleted,
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
                tvSummaryOCS_DeletedInverted,
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

        strokeTransition = StrokeTransitionBuilder.create()
                .duration(Duration.seconds(1))
                .shape(line)
                .fromValue(Color.web("#5B88E0"))
                .toValue(Color.web("#40FF00"))
                .cycleCount(2)
                .autoReverse(true)
                .build();

        strokeTransitionDeleted = StrokeTransitionBuilder.create()
                .duration(Duration.seconds(1))
                .shape(line)
                .fromValue(Color.web("#5B88E0"))
                .toValue(Color.web("#FF0000"))
                .cycleCount(2)
                .autoReverse(true)
                .build();

        strokeTransitionOut = StrokeTransitionBuilder.create()
                .duration(Duration.seconds(1))
                .shape(line)
                .fromValue(Color.web("#5B88E0"))
                .toValue(Color.web("#FFFFFF"))
                .cycleCount(1)
                .autoReverse(false)
                .build();


    }

    private void setInitValue(PhosphorusPropertySet... argPropertySet) {
        for (PhosphorusPropertySet phosphorusPropertySet : argPropertySet) {
            phosphorusPropertySet.setProperty2(" " + 0);
        }
    }

    public void showLabel(double x, double y) {

//        vbxInfoOCS.setVisible(true);

        if (!graphDesignGroup.getGroup().getChildren().contains(vbxInfoOCS)) {
            graphDesignGroup.getGroup().getChildren().add(vbxInfoOCS);

            vbxInfoOCS.setLayoutX(x);
            vbxInfoOCS.setLayoutY(y);
            vbxInfoOCS.setScaleX(0.7);
            vbxInfoOCS.setScaleY(-.7);
            vbxInfoOCS.toFront();
        }


    }

    public void remove() {

        graphDesignGroup.getGroup().getChildren().removeAll(group, lblCountOCS);
        graphDesignGroup.getLinesOCS().remove(group);


    }

    public Group getGroup() {
        return group;
    }

    public void addInstanceOCS() {
        countOCS++;
        tvSummaryOCS_Created.setProperty2(numberFormat.format(countOCS));
        strokeTransition.play();
        bar1Note.play();
        verificarColor();
    }

    public void addInstanceOCS_Inverted() {
        if (countOCS_Inverted == 0) {
            addDirection();
        }
        countOCS_Inverted++;
        tvSummaryOCS_CreatedInverted.setProperty2(numberFormat.format(countOCS_Inverted));
        strokeTransition.play();
        bar1Note.play();
        verificarColor();
    }

    public void addDeleteOCS() {
        deteledCountOCS++;
        tvSummaryOCS_Deleted.setProperty2(numberFormat.format(deteledCountOCS));

        bar2Note.play();
        if (!(countOCS - deteledCountOCS == 0 && countOCS_Inverted - deteledCountOCS_Inverted == 0)) {
            strokeTransitionDeleted.play();
        }
        verificarColor();

    }

    public void addDeleteOCS_Inverted() {
        deteledCountOCS_Inverted++;
        tvSummaryOCS_DeletedInverted.setProperty2(numberFormat.format(deteledCountOCS_Inverted));

        bar2Note.play();
        if (!(countOCS - deteledCountOCS == 0 && countOCS_Inverted - deteledCountOCS_Inverted == 0)) {
            strokeTransitionDeleted.play();
        }
        verificarColor();

    }

    public void setOCSJobSent(long jobSent) {
        tvSummaryOCS_JobSent.setProperty2(numberFormat.format(jobSent));
    }

    public void setOCSJobSentInverted(long jobSent) {
        tvSummaryOCS_JobSentInverted.setProperty2(numberFormat.format(jobSent));
    }

    public void setTraffic(double traffic) {
        tvSummaryOCS_Traffic.setProperty2(numberFormat.format(traffic) + TRAFFIC_UNIT);
        tvSummaryOCS.setVisible(false);
        tvSummaryOCS.setVisible(true);

    }

    public void setTrafficInverted(double traffic) {
        tvSummaryOCS_TrafficInverted.setProperty2(numberFormat.format(traffic) + TRAFFIC_UNIT);


    }

    public void setJobTraffic(double traffic) {
        tvSummaryOCS_JobTraffic.setProperty2(numberFormat.format(traffic) + TRAFFIC_UNIT);



    }

    public void setJobTrafficInverted(double traffic) {
        tvSummaryOCS_JobTrafficInverted.setProperty2(numberFormat.format(traffic) + TRAFFIC_UNIT);
    }

    public void setRequestJobSent(long jobSent) {
        tvSummaryOCS_RequestSent.setProperty2(numberFormat.format(jobSent));
    }

    public void setRequestJobSentInverted(long jobSent) {
        tvSummaryOCS_RequestSentInverted.setProperty2(numberFormat.format(jobSent));
    }

    public void setRequestJobTraffic(double traffic) {

        tvSummaryOCS_RequestTraffic.setProperty2(numberFormat.format(traffic) + TRAFFIC_UNIT);

    }

    public void setRequestJobTrafficInverted(double traffic) {
        tvSummaryOCS_RequestTrafficInverted.setProperty2(numberFormat.format(traffic) + TRAFFIC_UNIT);
    }

    public void set_ackRequestJobSent(long jobSent) {
        tvSummaryOCS_ackRequestSent.setProperty2(numberFormat.format(jobSent));
    }

    public void set_ackRequestJobSentInverted(long jobSent) {
        tvSummaryOCS_ackRequestSentInverted.setProperty2(numberFormat.format(jobSent));
    }

    public void set_ackRequestJobTraffic(double traffic) {
        tvSummaryOCS_ackRequestTraffic.setProperty2(numberFormat.format(traffic) + TRAFFIC_UNIT);
    }

    public void set_ackRequestJobTrafficInverted(double traffic) {
        tvSummaryOCS_ackRequestTrafficInverted.setProperty2(numberFormat.format(traffic) + TRAFFIC_UNIT);
    }

    public void setResultJobSent(long jobSent) {

        tvSummaryOCS_ResultSent.setProperty2(numberFormat.format(jobSent));

    }

    public void setResultJobSentInverted(long jobSent) {

        tvSummaryOCS_ResultSentInverted.setProperty2(numberFormat.format(jobSent));

    }

    public void setResultJobTraffic(double traffic) {

        tvSummaryOCS_ResultTraffic.setProperty2(numberFormat.format(traffic) + TRAFFIC_UNIT);

    }

    public void setResultJobTrafficInverted(double traffic) {
        tvSummaryOCS_ResultTrafficInverted.setProperty2(numberFormat.format(traffic) + TRAFFIC_UNIT);
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
        return vbxInfoOCS;
    }

    private void verificarColor() {
        if (countOCS - deteledCountOCS == 0 && countOCS_Inverted - deteledCountOCS_Inverted == 0) {
            strokeTransitionOut.play();

        }


    }
}
