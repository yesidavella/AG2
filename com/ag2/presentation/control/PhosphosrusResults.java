package com.ag2.presentation.control;

import Grid.Utilities.HtmlWriter;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.util.Utils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class PhosphosrusResults implements ViewResultsPhosphorus {

    private Tab tab;
    private TableView tvClientResults = new TableView();
    private TableView tvResourceResults = new TableView();
    private TableView tvSwitchResults = new TableView();
    private TableView tvBrokerResults = new TableView();
    private Label lbTitlePage = LabelBuilder.create().text("RESULTADOS DE SIMULACIÓN")
            .font(Font.font("Arial", FontWeight.BOLD, 20)).minWidth(20).alignment(Pos.CENTER).build();
    private Label lblClient = new Label("Clientes");
    private Label lbResource = new Label("Recursos");
    private Label lbSwitch = new Label("Conmutadores Opticos");
    private Label lbBroker = new Label("Agendadores");
    private ObservableList<PhosphorusPropertySet> dataClients = FXCollections.observableArrayList();
    private ObservableList<PhosphorusPropertySet> dataResources = FXCollections.observableArrayList();
    private ObservableList<PhosphorusPropertySet> dataSwitches = FXCollections.observableArrayList();
    private ObservableList<PhosphorusPropertySet> dataBrokers = FXCollections.observableArrayList();
    private ScrollPane scpResults;
    private VBox vbxMain = new VBox();
    private ProgressIndicator progressIndicator;
    private HBox hbxProgress;
    private VBox vbxDataProgress;
    private VBox vbxImageProgress;
    private GridPane gpnDataProgress;
    private Label lblSimulationTimeValue = new Label("00.00");
    private Label lblSimulationTimePercentageValue = new Label("00%");
    private Label lblRealTimeValue = new Label("00.00");
    private Label lblPageHTMLCountValue = new Label("0");
    private Timeline time = new Timeline();
    private long initialTime = System.currentTimeMillis();
//    private int columnWidthSmall = 60;
//    private int columnWidthMedium = 100;
//    private int columnWidthBig = 120;
//    private int generalColumnWight = 100;
    private NumberFormat percent = DecimalFormat.getPercentInstance();
    private NumberFormat decimal = DecimalFormat.getInstance();
    private NumberFormat integer = DecimalFormat.getIntegerInstance();

    public PhosphosrusResults(Tab tab) {
        this.tab = tab;
        percent.setMaximumFractionDigits(3);

        lblClient.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbResource.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbSwitch.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbBroker.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        scpResults = new ScrollPane();

        hbxProgress = new HBox();
        vbxDataProgress = new VBox();
        vbxImageProgress = new VBox();

        gpnDataProgress = new GridPane();
        gpnDataProgress.setPadding(new Insets(10, 10, 10, 10));

        gpnDataProgress.setVgap(5);
        gpnDataProgress.setHgap(11);

//        scPnResults.getStyleClass().addAll("boxLogosVerticalGradient");

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.web("#FFD779"));

        gpnDataProgress.getStyleClass().add("data-progress-box");
//        vBoxImageProgress.getStyleClass().add("progress-indicator-box");
        gpnDataProgress.setEffect(dropShadow);
        vbxImageProgress.setEffect(dropShadow);

        hbxProgress.setPadding(new Insets(20, 20, 20, 20));
        hbxProgress.setSpacing(20);

        hbxProgress.setAlignment(Pos.CENTER);

        Label lbTitle = new Label(" Progreso ");
        lbTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblSimulationTimeValue.setMinWidth(250);
        Label lblSimulationTime = new Label("Tiempo de la simulación");
        setFont(lblSimulationTime);
        Label lblSimulationTimePercentage = new Label("% Tiempo de la simulación");
        setFont(lblSimulationTimePercentage);
        Label lblRealTime = new Label("Tiempo transcurrido");
        setFont(lblRealTime);
        Label lblPageHTMLCount = new Label("Numero de paginas HTML");
        setFont(lblPageHTMLCount);

        setFont(lblSimulationTimeValue);
        setFont(lblSimulationTimePercentageValue);
        setFont(lblRealTimeValue);
        setFont(lblPageHTMLCountValue);

        GridPane.setConstraints(lbTitle, 0, 0);
        GridPane.setColumnSpan(lbTitle, 3);
        GridPane.setHalignment(lbTitle, HPos.CENTER);
        gpnDataProgress.getChildren().add(lbTitle);

        GridPane.setConstraints(lblSimulationTime, 0, 1);
        gpnDataProgress.getChildren().add(lblSimulationTime);
        GridPane.setConstraints(lblSimulationTimeValue, 1, 1);
        gpnDataProgress.getChildren().add(lblSimulationTimeValue);

        GridPane.setConstraints(lblSimulationTimePercentage, 0, 2);
        gpnDataProgress.getChildren().add(lblSimulationTimePercentage);
        GridPane.setConstraints(lblSimulationTimePercentageValue, 1, 2);
        gpnDataProgress.getChildren().add(lblSimulationTimePercentageValue);

        GridPane.setConstraints(lblRealTime, 0, 3);
        gpnDataProgress.getChildren().add(lblRealTime);
        GridPane.setConstraints(lblRealTimeValue, 1, 3);
        gpnDataProgress.getChildren().add(lblRealTimeValue);

        GridPane.setConstraints(lblPageHTMLCount, 0, 4);
        gpnDataProgress.getChildren().add(lblPageHTMLCount);
        GridPane.setConstraints(lblPageHTMLCountValue, 1, 4);
        gpnDataProgress.getChildren().add(lblPageHTMLCountValue);

        vbxDataProgress.getChildren().add(gpnDataProgress);

        progressIndicator = new ProgressIndicator(0);
        progressIndicator.setMinSize(200, 200);

        vbxMain.setSpacing(7);
        vbxMain.setPadding(new Insets(8, 8, 8, 8));
        vbxMain.setPrefSize(1200, 900);
        vbxMain.setAlignment(Pos.CENTER);

        createtvClientResults();
        createtvResultsResource();
        createtvBrokerResults();
        createtvResultsSwitch();

        vbxImageProgress.getChildren().add(progressIndicator);
        hbxProgress.getChildren().addAll(vbxDataProgress, vbxImageProgress);
        vbxMain.getChildren().addAll(lbTitlePage, hbxProgress, lblClient, tvClientResults, lbResource, tvResourceResults,
                lbSwitch, tvSwitchResults, lbBroker, tvBrokerResults);
        scpResults.setContent(vbxMain);
        scpResults.setFitToWidth(true);
        tab.setContent(scpResults);

        time.setCycleCount(Timeline.INDEFINITE);

        initialTime = System.currentTimeMillis();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis() - initialTime);
                String hour = String.valueOf((calendar.get(Calendar.HOUR) - 7));
                String minute = String.valueOf(calendar.get(Calendar.MINUTE));
                String second = String.valueOf(calendar.get(Calendar.SECOND));

                if (hour.length() == 1) {
                    hour = "0" + hour;
                }
                if (minute.length() == 1) {
                    minute = "0" + minute;
                }
                if (second.length() == 1) {
                    second = "0" + second;
                }
                lblRealTimeValue.setText(hour + ":" + minute + ":" + second);
            }
        });
        time.getKeyFrames().add(keyFrame);
    }

    private void setFont(Label label) {
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
    }

    public void lookToNextExecution() {
        initialTime = System.currentTimeMillis();
        time.play();
        int sizeClients = dataClients.size();
        for (int i = 0; i < sizeClients; i++) {
            dataClients.remove(0);
        }
        int sizeSwitches = dataSwitches.size();
        for (int i = 0; i < sizeSwitches; i++) {
            dataSwitches.remove(0);
        }
        int sizeResources = dataResources.size();
        for (int i = 0; i < sizeResources; i++) {
            dataResources.remove(0);
        }
        int sizeBrokers = dataBrokers.size();
        for (int i = 0; i < sizeBrokers; i++) {
            dataBrokers.remove(0);
        }

        showProgressIndicator();
    }

    public void showProgressIndicator() {
        progressIndicator.setProgress(0);
    }

    private void createtvResultsSwitch() {

        TableColumn tclSwitchName = new TableColumn("Conmutador 0ptico");
        TableColumn tclSwitchedJobMsg = new TableColumn("Conmutados");
        TableColumn tclNotSwitchedJobMsg = new TableColumn("No conmutados");
        TableColumn tclSwitchedResultMsg = new TableColumn("Conmutados");
        TableColumn tclNotSwitchedResultMsg = new TableColumn("No conmutados");
        TableColumn tclSwitchedReqMsg = new TableColumn("Conmutadas");
        TableColumn tclNotSwitchedReqMsg = new TableColumn("No conmutadas");
        TableColumn relDropJob = new TableColumn("% no conmutado");
        TableColumn relDropRes = new TableColumn("% no conmutados");
        TableColumn relDropReq = new TableColumn("% no conmutadas");
        TableColumn reltotDrop = new TableColumn("% Total no conmutado");

        tclSwitchName.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property1"));
        tclSwitchedJobMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property2"));
        tclNotSwitchedJobMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property3"));
        tclSwitchedResultMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property4"));
        tclNotSwitchedResultMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property5"));
        tclSwitchedReqMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property6"));
        tclNotSwitchedReqMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property7"));
        relDropJob.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property8"));
        relDropRes.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property9"));
        relDropReq.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property10"));
        reltotDrop.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property11"));

        TableColumn tclJobs = new TableColumn("Trabajos");
        TableColumn tclJobResults = new TableColumn("Trabajo Atendidos");
        TableColumn tclScheduleReq = new TableColumn("Solicitudes de Agendamiento");

        tclJobs.getColumns().addAll(tclSwitchedJobMsg, tclNotSwitchedJobMsg, relDropJob);
        tclJobResults.getColumns().addAll(tclSwitchedResultMsg, tclNotSwitchedResultMsg, relDropRes);
        tclScheduleReq.getColumns().addAll(tclSwitchedReqMsg, tclNotSwitchedReqMsg, relDropReq);

        tvSwitchResults.getColumns().addAll(tclSwitchName, tclJobs, tclJobResults, tclScheduleReq, reltotDrop);
        tvSwitchResults.setItems(dataSwitches);
    }

    private void createtvResultsResource() {

        TableColumn resourceName = new TableColumn("Recurso");
        TableColumn tclJobsReceived = new TableColumn("Recibidos");
        TableColumn tclSentServedJobs = new TableColumn("Enviados a origen");
        TableColumn tclRelSentServedJobs = new TableColumn("% enviados a origen");
        TableColumn tclFaultsSent = new TableColumn("Enviados a origen");
        TableColumn tclRelFaultsSent = new TableColumn("% enviados a origen");
        TableColumn tclOccupancyTime = new TableColumn("Tiempo de ocupacion (ms)");
        TableColumn tclRelOccupancyTime = new TableColumn("% Tiempo de ocupacion");
        TableColumn tclCPU_buffNoFree = new TableColumn("CPU/buffer no libre");
        TableColumn tclRelCPU_buffNoFree = new TableColumn("% CPU/buffer no libre");

        resourceName.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property1"));
        tclJobsReceived.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property2"));
        tclSentServedJobs.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property3"));
        tclRelSentServedJobs.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property4"));
        tclFaultsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property5"));
        tclRelFaultsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property6"));
        tclOccupancyTime.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property7"));
        tclRelOccupancyTime.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property8"));
        tclCPU_buffNoFree.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property9"));
        tclRelCPU_buffNoFree.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property10"));

        TableColumn tclAllJobs = new TableColumn("Trabajos");
        TableColumn tclServedJobs = new TableColumn("Atendidos");
        TableColumn tclFaults = new TableColumn("No Atendidos");
        TableColumn tclTime = new TableColumn("Tiempo");
        TableColumn tclCPU_buff = new TableColumn("CPU");

        tclAllJobs.getColumns().addAll(tclJobsReceived, tclServedJobs, tclFaults);
        tclServedJobs.getColumns().addAll(tclSentServedJobs, tclRelSentServedJobs);
        tclFaults.getColumns().addAll(tclFaultsSent, tclRelFaultsSent);
        tclTime.getColumns().addAll(tclOccupancyTime, tclRelOccupancyTime);
        tclCPU_buff.getColumns().addAll(tclCPU_buffNoFree, tclRelCPU_buffNoFree);

        tvResourceResults.getColumns().addAll(resourceName, tclAllJobs, tclTime, tclCPU_buff);

        tvResourceResults.setItems(dataResources);
    }

    private void createtvClientResults() {

        TableColumn tcClient = new TableColumn("Cliente");
        TableColumn tcReqsCreated = new TableColumn("Creadas");
        TableColumn tcReqsSent = new TableColumn("Enviadas");
        TableColumn tcReqsNotSent = new TableColumn("No enviadas");
        TableColumn relReqsSent_reqsCreated = new TableColumn("% enviadas/creadas ");
        TableColumn tclScheduledReqs = new TableColumn("Agendadas");

        TableColumn tcJobsSent = new TableColumn("Enviados");
        TableColumn tcJobsNotSent = new TableColumn("No enviados");
        TableColumn tcReceivedResults = new TableColumn("Recibidos");
        TableColumn relResultsReceived_jobsSent = new TableColumn("% recibidos/enviados");

        TableColumn relJobsSent_reqsSent = new TableColumn("% Trabajos enviados\n/Solicitudes enviadas");
        TableColumn relResultsReceived_reqsSent = new TableColumn("% Resultados recibidos\n/Solicitudes enviadas");
        TableColumn relReceivedResults_createdReqs = new TableColumn("% Resultados recibidos\n/Solicitudes creadas");

        tcClient.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property1"));
        tcReqsCreated.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property2"));
        tcReqsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property3"));
        tcReqsNotSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property4"));
        tcJobsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property5"));
        tcJobsNotSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property6"));
        tcReceivedResults.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property7"));
        relReqsSent_reqsCreated.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property8"));
        relJobsSent_reqsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property9"));
        relResultsReceived_jobsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property10"));
        relResultsReceived_reqsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property11"));
        relReceivedResults_createdReqs.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property12"));

        TableColumn tclAllReqs = new TableColumn("Solicitudes de Agendamiento");
        TableColumn tclAllJobs = new TableColumn("Trabajos");

        tclAllReqs.getColumns().addAll(tcReqsCreated, tcReqsSent, tcReqsNotSent, relReqsSent_reqsCreated, tclScheduledReqs);
        tclAllJobs.getColumns().addAll(tcJobsSent, tcJobsNotSent, tcReceivedResults, relResultsReceived_jobsSent);

        tvClientResults.getColumns().addAll(
                tcClient,
                tclAllReqs,
                tclAllJobs,
                relJobsSent_reqsSent,
                relReceivedResults_createdReqs,
                relResultsReceived_reqsSent);
        tvClientResults.setItems(dataClients);
    }

    private void createtvBrokerResults() {

        TableColumn tclBrokerName = new TableColumn("Agendador");
        TableColumn tclRegistrationReceived = new TableColumn("Registros de Clúster");
        TableColumn tclReqRecieved = new TableColumn("Total Solicitudes");
        TableColumn tclNoFreeResource = new TableColumn("No asignadas");
        TableColumn tclReqAckSent = new TableColumn("Enviadas(con ack)");
        TableColumn tclSendingFailed = new TableColumn("No enviadas");
        TableColumn tclRelativeReqAckSent = new TableColumn("% de envió(con ack)");

        tclBrokerName.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property1"));
        tclRegistrationReceived.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property2"));
        tclReqRecieved.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property3"));
        tclNoFreeResource.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property4"));
        tclReqAckSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property5"));
        tclSendingFailed.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property6"));
        tclRelativeReqAckSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property7"));

        //setMinPrefAndMaxWidthToColTable(tclBrokerName, generalColumnWight);
        //setMinPrefAndMaxWidthToColTable(tclRegistrationReceived, generalColumnWight);
        //setMinPrefAndMaxWidthToColTable(tclReqRecieved, generalColumnWight);
        //setMinPrefAndMaxWidthToColTable(tclNoFreeResource, generalColumnWight);
        //setMinPrefAndMaxWidthToColTable(tclReqAckSent, generalColumnWight);
        //setMinPrefAndMaxWidthToColTable(tclSendingFailed, generalColumnWight);
        //setMinPrefAndMaxWidthToColTable(tclRelativeReqAckSent, generalColumnWight);

        TableColumn tclAllReqRecieved = new TableColumn("Solicitudes Recibidas de Agendamiento");
        TableColumn reqRecievedAssigned = new TableColumn("Asignadas");

        reqRecievedAssigned.getColumns().addAll(tclReqAckSent, tclSendingFailed, tclRelativeReqAckSent);
        tclAllReqRecieved.getColumns().addAll(reqRecievedAssigned, tclNoFreeResource, tclReqRecieved);

        tvBrokerResults.getColumns().addAll(tclBrokerName, tclRegistrationReceived, tclAllReqRecieved);
        tvBrokerResults.setItems(dataBrokers);
    }

    @Override
    public void addClientResult(
            String clientName,
            double requestCreated,
            double requestSent,
            double requestNoSent,
            double jobSent,
            double jobNoSent,
            double resultReceive,
            double relativeRequestSent,
            double relativeJobSent,
            double relativeReceiveResult_jobSent,
            double relativeReceiveResult_requestSent,
            double relativeReceiveresult_requestCreated) {

        time.stop();

        PhosphorusPropertySet clientPropSet = new PhosphorusPropertySet();
        clientPropSet.setProperty1(Utils.findGraphicalName(clientName));
        clientPropSet.setProperty2(integer.format(requestCreated));
        clientPropSet.setProperty3(integer.format(requestSent));
        clientPropSet.setProperty4(integer.format(requestNoSent));
        clientPropSet.setProperty5(integer.format(jobSent));
        clientPropSet.setProperty6(integer.format(jobNoSent));
        clientPropSet.setProperty7(integer.format(resultReceive));
        clientPropSet.setProperty8(percent.format(relativeRequestSent));
        clientPropSet.setProperty9(percent.format(relativeJobSent));
        clientPropSet.setProperty10(percent.format(relativeReceiveResult_jobSent));
        clientPropSet.setProperty11(percent.format(relativeReceiveResult_requestSent));
        clientPropSet.setProperty12(percent.format(relativeReceiveresult_requestCreated));

        dataClients.add(clientPropSet);
    }

    @Override
    public void addResourceResult(
            String resourceName,
            double jobsReceived,
            double jobsSent,
            double relJobsSent,
            double faultsSent,
            double relFaultsSent,
            double timesNoCPUFree,
            double relTimesNoCPUFree,
            double timesNoSpaceFault,
            double relTimesNoSpaceFault) {

        PhosphorusPropertySet resourcePropSet = new PhosphorusPropertySet();
        resourcePropSet.setProperty1(Utils.findGraphicalName(resourceName));
        resourcePropSet.setProperty2(integer.format(jobsReceived));
        resourcePropSet.setProperty3(integer.format(jobsSent));
        resourcePropSet.setProperty4(percent.format(relJobsSent));
        resourcePropSet.setProperty5(integer.format(faultsSent));
        resourcePropSet.setProperty6(percent.format(relFaultsSent));
        resourcePropSet.setProperty7(integer.format(timesNoCPUFree));
        resourcePropSet.setProperty8(percent.format(relTimesNoCPUFree));
        resourcePropSet.setProperty9(integer.format(timesNoSpaceFault));
        resourcePropSet.setProperty10(percent.format(relTimesNoSpaceFault));

        dataResources.add(resourcePropSet);
    }

    @Override
    public void addSwitchResult(
            String switchName,
            double jobsSwitched,
            double jobsNoSwitched,
            double resultsSwiched,
            double resultsNoSwitched,
            double requestsSwitched,
            double requestsNoSwitched,
            double relativeNojobsNoSwitched,
            double relativeResultsNoSwitched,
            double relativeRequestsNoSwitched,
            double relativeAllNoSwitched) {

        PhosphorusPropertySet switchPropSet = new PhosphorusPropertySet();
        switchPropSet.setProperty1(Utils.findGraphicalName(switchName));
        switchPropSet.setProperty2(integer.format(jobsSwitched));
        switchPropSet.setProperty3(integer.format(jobsNoSwitched));
        switchPropSet.setProperty4(integer.format(resultsSwiched));
        switchPropSet.setProperty5(integer.format(resultsNoSwitched));
        switchPropSet.setProperty6(integer.format(requestsSwitched));
        switchPropSet.setProperty7(integer.format(requestsNoSwitched));
        switchPropSet.setProperty8(percent.format(relativeNojobsNoSwitched));
        switchPropSet.setProperty9(percent.format(relativeResultsNoSwitched));
        switchPropSet.setProperty10(percent.format(relativeRequestsNoSwitched));
        switchPropSet.setProperty11(percent.format(relativeAllNoSwitched));

        dataSwitches.add(switchPropSet);
    }

    @Override
    public void addBrokerResult(
            String brokerName,
            double registrationReceived,
            double reqRecieved,
            double noFreeResource,
            double reqAckSent,
            double sendingFailed,
            double relativeAckSent) {

        PhosphorusPropertySet brokerPropSet = new PhosphorusPropertySet();
        brokerPropSet.setProperty1(Utils.findGraphicalName(brokerName));
        brokerPropSet.setProperty2(integer.format(registrationReceived));
        brokerPropSet.setProperty3(integer.format(reqRecieved));
        brokerPropSet.setProperty4(integer.format(noFreeResource));
        brokerPropSet.setProperty5(integer.format(reqAckSent));
        brokerPropSet.setProperty6(integer.format(sendingFailed));
        brokerPropSet.setProperty7(percent.format(relativeAckSent));

        dataBrokers.add(brokerPropSet);
    }

    @Override
    public void setExecutionPercentage(final double Percentage, double simulationTime) {
        progressIndicator.setProgress(Percentage / 100);
        String valuePercentage = String.valueOf(Math.round(Percentage));
        lblSimulationTimePercentageValue.setText(valuePercentage + " %");
        lblPageHTMLCountValue.setText(String.valueOf(HtmlWriter.getInstance().getPagina()));
        lblSimulationTimeValue.setText(String.valueOf(simulationTime) + " ms");
        if (Math.round(Percentage) == 100) {
            progressIndicator.setProgress(1);
            lblSimulationTimePercentageValue.setText("100%");
            lblSimulationTimeValue.setText(PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SIMULATION_TIME) + " ms (Finalizado) ");
        }
    }

    /**
     * Le asigna a una columna de TableView el ancho minimo,preferido y maximo a
     * el mismo valor del parametro q le envio.
     *
     * @param tColumn, la columna a dar el ancho.
     * @param sameWidht, el valor q va a tener en el ancho esta columna.
     */
    private void setMinPrefAndMaxWidthToColTable(TableColumn tColumn, double sameWidht) {
        tColumn.setMinWidth(sameWidht);
        tColumn.setPrefWidth(sameWidht);
        tColumn.setMaxWidth(sameWidht);
    }

    /**
     * Le asigna a una columna de TableView el ancho minimo,preferido y maximo.
     *
     * @param tColumn, la columna a dar el ancho.
     * @param minWidht, el ancho minimo de la columna.
     * @param prefWidht, el ancho preferido de la columna.
     * @param maxWidht, el ancho maximo de la columna.
     */
    private void setMinPrefAndMaxWidthToColTable(TableColumn tColumn, double minWidht, double prefWidht, double maxWidht) {
        tColumn.setMinWidth(minWidht);
        tColumn.setPrefWidth(prefWidht);
        tColumn.setMaxWidth(maxWidht);
    }

    /*
     * ***********************************************************************
     * ******************Static class to show the results in******************
     * ****** **************the tablesview of results**************************
     */
    /**
     * This class shows the results in the tablesview of results.
     */
    public static class PhosphorusPropertySet {

        private String property1;
        private String property2;
        private String property3;
        private String property4;
        private String property5;
        private String property6;
        private String property7;
        private String property8;
        private String property9;
        private String property10;
        private String property11;
        private String property12;
        private String property13;

        private PhosphorusPropertySet() {
        }

        public String getProperty10() {
            return property10;
        }

        public void setProperty10(String property10) {
            this.property10 = property10;
        }

        public String getProperty11() {
            return property11;
        }

        public void setProperty11(String property11) {
            this.property11 = property11;
        }

        public String getProperty9() {
            return property9;
        }

        public void setProperty9(String property9) {
            this.property9 = property9;
        }

        public String getProperty1() {
            return property1;
        }

        public void setProperty1(String property1) {
            this.property1 = property1;
        }

        public String getProperty2() {
            return property2;
        }

        public void setProperty2(String property2) {
            this.property2 = property2;
        }

        public String getProperty3() {
            return property3;
        }

        public void setProperty3(String property3) {
            this.property3 = property3;
        }

        public String getProperty4() {
            return property4;
        }

        public void setProperty4(String property4) {
            this.property4 = property4;
        }

        public String getProperty5() {
            return property5;
        }

        public void setProperty5(String property5) {
            this.property5 = property5;
        }

        public String getProperty6() {
            return property6;
        }

        public void setProperty6(String property6) {
            this.property6 = property6;
        }

        public String getProperty7() {
            return property7;
        }

        public void setProperty7(String property7) {
            this.property7 = property7;
        }

        public String getProperty8() {
            return property8;
        }

        public void setProperty8(String property8) {
            this.property8 = property8;
        }

        public String getProperty12() {
            return property12;
        }

        public void setProperty12(String property12) {
            this.property12 = property12;
        }

        public String getProperty13() {
            return property13;
        }

        public void setProperty13(String property13) {
            this.property13 = property13;
        }
    }
}