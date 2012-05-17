package com.ag2.presentation.control;

import Grid.Utilities.HtmlWriter;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.util.Utils;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    private Label lblClient = new Label("Resultados del Cliente");
    private Label lbResource = new Label("Resultados del Recurso");
    private Label lbSwitch = new Label("Resultados del Conmutador Optico");
    private Label lbBroker = new Label("Resultados del Agendador");
    private ObservableList<PhosphorusPropertySet> dataClient = FXCollections.observableArrayList();
    private ObservableList<PhosphorusPropertySet> dataResource = FXCollections.observableArrayList();
    private ObservableList<PhosphorusPropertySet> dataSwitch = FXCollections.observableArrayList();
    private ObservableList<PhosphorusPropertySet> dataBroker = FXCollections.observableArrayList();
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
    private int columnWidthSmall = 60;
    private int columnWidthMedium = 100;
    private int columnWidthBig = 120;
    private int tableViewHeight = 300;
    private int generalColumnWight = 130;
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
        tvClientResults.setPrefHeight(tableViewHeight);
        tvResourceResults.setPrefHeight(tableViewHeight);
        tvSwitchResults.setPrefHeight(tableViewHeight);
        tvBrokerResults.setPrefHeight(tableViewHeight);
        tvClientResults.setMinHeight(tableViewHeight);
        tvResourceResults.setMinHeight(tableViewHeight);
        tvSwitchResults.setMinHeight(tableViewHeight);
        tvBrokerResults.setMinHeight(tableViewHeight);

        vbxMain.getChildren().addAll(hbxProgress, lblClient, tvClientResults, lbResource, tvResourceResults,
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
        int sizeClients = dataClient.size();
        for (int i = 0; i < sizeClients; i++) {
            dataClient.remove(0);
        }
        int sizeSwitches = dataSwitch.size();
        for (int i = 0; i < sizeSwitches; i++) {
            dataSwitch.remove(0);
        }
        int sizeResources = dataResource.size();
        for (int i = 0; i < sizeResources; i++) {
            dataResource.remove(0);
        }
        int sizeBrokers = dataBroker.size();
        for (int i = 0; i < sizeBrokers; i++) {
            dataBroker.remove(0);
        }

        showProgressIndicator();
    }

    public void showProgressIndicator() {
        progressIndicator.setProgress(0);
    }

    private void createtvResultsSwitch() {

        TableColumn tclSwitchName = new TableColumn("Conmutador 0ptico");
        setMinPrefAndMaxWidthToColTable(tclSwitchName, generalColumnWight);
        tclSwitchName.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property1"));

        TableColumn tclSwitchedJobMsg = new TableColumn("Conmutados");
        setMinPrefAndMaxWidthToColTable(tclSwitchedJobMsg, generalColumnWight);
        tclSwitchedJobMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property2"));

        TableColumn tclNotSwitchedJobMsg = new TableColumn("No conmutados");
        setMinPrefAndMaxWidthToColTable(tclNotSwitchedJobMsg, generalColumnWight);
        tclNotSwitchedJobMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property3"));

        TableColumn tclSwitchedResultMsg = new TableColumn("Conmutados");
        setMinPrefAndMaxWidthToColTable(tclSwitchedResultMsg, generalColumnWight);
        tclSwitchedResultMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property4"));

        TableColumn tclNotSwitchedResultMsg = new TableColumn("No conmutados");
        setMinPrefAndMaxWidthToColTable(tclNotSwitchedResultMsg, generalColumnWight);
        tclNotSwitchedResultMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property5"));

        TableColumn tclSwitchedReqMsg = new TableColumn("Conmutadas");
        setMinPrefAndMaxWidthToColTable(tclSwitchedReqMsg, generalColumnWight);
        tclSwitchedReqMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property6"));

        TableColumn tclNotSwitchedReqMsg = new TableColumn("No conmutadas");
        setMinPrefAndMaxWidthToColTable(tclNotSwitchedReqMsg, generalColumnWight);
        tclNotSwitchedReqMsg.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property7"));

        TableColumn relDropJob = new TableColumn("% no conmutado");
        relDropJob.setMinWidth(150);
        relDropJob.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property8"));

        TableColumn relDropRes = new TableColumn("% no conmutados");
        relDropRes.setMinWidth(150);
        relDropRes.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property9"));

        TableColumn relDropReq = new TableColumn("% no conmutadas");
        relDropReq.setMinWidth(150);
        relDropReq.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property10"));

        TableColumn reltotDrop = new TableColumn("% Total no conmutado");
        reltotDrop.setMinWidth(150);
        reltotDrop.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property11"));

        TableColumn tclJobs = new TableColumn("Trabajos");
        tclJobs.getColumns().addAll(tclSwitchedJobMsg, tclNotSwitchedJobMsg, relDropJob);

        TableColumn tclJobResults = new TableColumn("Resultados de Trabajo");
        tclJobResults.getColumns().addAll(tclSwitchedResultMsg, tclNotSwitchedResultMsg, relDropRes);

        TableColumn tclScheduleReq = new TableColumn("Solicitudes de Agendamiento");
        tclScheduleReq.getColumns().addAll(tclSwitchedReqMsg, tclNotSwitchedReqMsg, relDropReq);

        tvSwitchResults.getColumns().addAll(tclSwitchName, tclJobs, tclJobResults, tclScheduleReq, reltotDrop);
        tvSwitchResults.setItems(dataSwitch);
    }

    private void createtvResultsResource() {

        TableColumn tcRecurso = new TableColumn("Recurso");
        tcRecurso.setMinWidth(80);
        tcRecurso.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property1"));

        TableColumn tableColumn1 = new TableColumn("Trabajos recibidos");
        tableColumn1.setMinWidth(130);
        tableColumn1.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property2"));

        TableColumn tableColumn2 = new TableColumn("Respuestas enviadas");
        tableColumn2.setMinWidth(130);
        tableColumn2.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property3"));

        TableColumn tableColumn3 = new TableColumn("%Respuestas enviadas");
        tableColumn3.setMinWidth(135);
        tableColumn3.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property4"));

        TableColumn tableColumn4 = new TableColumn("Fallas enviadas");
        tableColumn4.setMinWidth(120);
        tableColumn4.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property5"));

        TableColumn tableColumn5 = new TableColumn("% Fallas enviadas");
        tableColumn5.setMinWidth(130);
        tableColumn5.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property6"));

        TableColumn tableColumn6 = new TableColumn("Tiempo ocupacion (ms) ");
        tableColumn6.setMinWidth(160);
        tableColumn6.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property7"));

        TableColumn tableColumn7 = new TableColumn("% Tiempo ocupacion");
        tableColumn7.setMinWidth(160);
        tableColumn7.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property8"));


        TableColumn tableColumn8 = new TableColumn("CPU/buffer no libre  ");
        tableColumn8.setMinWidth(160);
        tableColumn8.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property9"));


        TableColumn tableColumn9 = new TableColumn("% CPU/buffer no libre  ");
        tableColumn9.setMinWidth(160);
        tableColumn9.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property10"));

        tvResourceResults.getColumns().addAll(tcRecurso, tableColumn1, tableColumn2, tableColumn3,
                tableColumn4, tableColumn5, tableColumn6, tableColumn7, tableColumn8, tableColumn9);

        tvResourceResults.setItems(dataResource);
    }

    private void createtvClientResults() {

        TableColumn tcClient = new TableColumn("Cliente");
        tcClient.setMinWidth(80);
        tcClient.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property1"));

        TableColumn tcReqsCreated = new TableColumn("Solicitudes \n creadas");
        tcReqsCreated.setMinWidth(columnWidthSmall);
        tcReqsCreated.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property2"));

        TableColumn tcReqsSent = new TableColumn("Solicitudes \n enviadas");
        tcReqsSent.setMinWidth(columnWidthSmall);
        tcReqsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property3"));

        TableColumn tcReqsNotSent = new TableColumn("Solicitudes \n no enviadas");
        tcReqsNotSent.setMinWidth(columnWidthSmall);
        tcReqsNotSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property4"));

        TableColumn tcJobsSent = new TableColumn("Trabajos \n enviados");
        tcJobsSent.setMinWidth(columnWidthSmall);
        tcJobsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property5"));

        TableColumn tcJobsNotSent = new TableColumn("Trabajos \n no enviados");
        tcJobsNotSent.setMinWidth(columnWidthSmall);
        tcJobsNotSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property6"));

        TableColumn tcReceivedResults = new TableColumn("Resultados \n recibidos");
        tcReceivedResults.setMinWidth(columnWidthSmall);
        tcReceivedResults.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property7"));

        TableColumn relReqsSent_reqsCreated = new TableColumn("% Solicitudes enviadas \n/Solicitudes creadas ");
        relReqsSent_reqsCreated.setMinWidth(columnWidthBig);
        relReqsSent_reqsCreated.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property8"));

        TableColumn relJobsSent_reqsSent = new TableColumn("% Trabajos enviados\n/Solicitudes enviadas");
        relJobsSent_reqsSent.setMinWidth(columnWidthBig);
        relJobsSent_reqsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property9"));

        TableColumn relResultsReceived_jobsSent = new TableColumn("% Resultados recibidos\n/Trabajos enviados");
        relResultsReceived_jobsSent.setMinWidth(columnWidthBig);
        relResultsReceived_jobsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property10"));

        TableColumn relResultsReceived_reqsSent = new TableColumn("% Resultados recibidos\n/Solicitudes enviadas");
        relResultsReceived_reqsSent.setMinWidth(columnWidthBig);
        relResultsReceived_reqsSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property11"));

        TableColumn relReceivedResults_createdReqs = new TableColumn("% Resultados recibidos\n/Solicitudes creadas ");
        relReceivedResults_createdReqs.setMinWidth(columnWidthBig);
        relReceivedResults_createdReqs.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property12"));

        tvClientResults.getColumns().addAll(
                tcClient,
                tcReqsCreated,
                tcReqsSent,
                tcReqsNotSent,
                tcJobsSent,
                tcJobsNotSent,
                tcReceivedResults,
                relReqsSent_reqsCreated,
                relJobsSent_reqsSent,
                relResultsReceived_jobsSent,
                relResultsReceived_reqsSent,
                relReceivedResults_createdReqs);
        tvClientResults.setItems(dataClient);
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

        PhosphorusPropertySet clientsPropSet = new PhosphorusPropertySet();
        clientsPropSet.setProperty1(Utils.findGraphicalName(clientName));
        clientsPropSet.setProperty2(integer.format(requestCreated));
        clientsPropSet.setProperty3(integer.format(requestSent));
        clientsPropSet.setProperty4(integer.format(requestNoSent));
        clientsPropSet.setProperty5(integer.format(jobSent));
        clientsPropSet.setProperty6(integer.format(jobNoSent));
        clientsPropSet.setProperty7(integer.format(resultReceive));
        clientsPropSet.setProperty8(percent.format(relativeRequestSent));
        clientsPropSet.setProperty9(percent.format(relativeJobSent));
        clientsPropSet.setProperty10(percent.format(relativeReceiveResult_jobSent));
        clientsPropSet.setProperty11(percent.format(relativeReceiveResult_requestSent));
        clientsPropSet.setProperty12(percent.format(relativeReceiveresult_requestCreated));

        dataClient.add(clientsPropSet);
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

        PhosphorusPropertySet resourcesPropSet = new PhosphorusPropertySet();
        resourcesPropSet.setProperty1(Utils.findGraphicalName(resourceName));
        resourcesPropSet.setProperty2(integer.format(jobsReceived));
        resourcesPropSet.setProperty3(integer.format(jobsSent));
        resourcesPropSet.setProperty4(percent.format(relJobsSent));
        resourcesPropSet.setProperty5(integer.format(faultsSent));
        resourcesPropSet.setProperty6(percent.format(relFaultsSent));
        resourcesPropSet.setProperty7(integer.format(timesNoCPUFree));
        resourcesPropSet.setProperty8(percent.format(relTimesNoCPUFree));
        resourcesPropSet.setProperty9(integer.format(timesNoSpaceFault));
        resourcesPropSet.setProperty10(percent.format(relTimesNoSpaceFault));

        dataResource.add(resourcesPropSet);
    }

    @Override
    public void addSwitchResult(
            String tcConmutador,
            double tcMensajesTrabajoConmutados,
            double tcMensajesTrabajoNoConmutados,
            double tcMensajesResultadosConmutados,
            double tcMensajesResultadosNoConmutados,
            double tcSwitchedJobRequest,
            double tcNonSwitchedJobRequest,
            double relDropJob,
            double relDropRes,
            double relDropReq,
            double reltotDrop) {

        PhosphorusPropertySet switchesPropSet = new PhosphorusPropertySet();
        switchesPropSet.setProperty1(Utils.findGraphicalName(tcConmutador));
        switchesPropSet.setProperty2(integer.format(tcMensajesTrabajoConmutados));
        switchesPropSet.setProperty3(integer.format(tcMensajesTrabajoNoConmutados));
        switchesPropSet.setProperty4(integer.format(tcMensajesResultadosConmutados));
        switchesPropSet.setProperty5(integer.format(tcMensajesResultadosNoConmutados));
        switchesPropSet.setProperty6(integer.format(tcSwitchedJobRequest));
        switchesPropSet.setProperty7(integer.format(tcNonSwitchedJobRequest));
        switchesPropSet.setProperty8(percent.format(relDropJob));
        switchesPropSet.setProperty9(percent.format(relDropRes));
        switchesPropSet.setProperty10(percent.format(relDropReq));
        switchesPropSet.setProperty11(percent.format(reltotDrop));

        dataSwitch.add(switchesPropSet);
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

    @Override
    public void addBrokerResult(
            String brokerName,
            double registrationReceived,
            double reqRecieved,
            double noFreeResource,
            double reqAckSent,
            double sendingFailed,
            double relativeAckSent) {

        PhosphorusPropertySet brokersPropSet = new PhosphorusPropertySet();
        brokersPropSet.setProperty1(Utils.findGraphicalName(brokerName));
        brokersPropSet.setProperty2(integer.format(registrationReceived));
        brokersPropSet.setProperty3(integer.format(reqRecieved));
        brokersPropSet.setProperty4(integer.format(noFreeResource));
        brokersPropSet.setProperty5(integer.format(reqAckSent));
        brokersPropSet.setProperty6(integer.format(sendingFailed));
        brokersPropSet.setProperty7(percent.format(relativeAckSent));

        dataBroker.add(brokersPropSet);
    }

    private void createtvBrokerResults() {

        TableColumn tclBrokerName = new TableColumn("Agendador");
        tclBrokerName.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property1"));
        setMinPrefAndMaxWidthToColTable(tclBrokerName, generalColumnWight);

        TableColumn tclRegistrationReceived = new TableColumn("Registros de Clúster");
        tclRegistrationReceived.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property2"));
        setMinPrefAndMaxWidthToColTable(tclRegistrationReceived, generalColumnWight);

        TableColumn tclReqRecieved = new TableColumn("Total Solicitudes");
        tclReqRecieved.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property3"));
        setMinPrefAndMaxWidthToColTable(tclReqRecieved, generalColumnWight);

        TableColumn tclNoFreeResource = new TableColumn("No asignadas");
        tclNoFreeResource.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property4"));
        setMinPrefAndMaxWidthToColTable(tclNoFreeResource, generalColumnWight);

        TableColumn tclReqAckSent = new TableColumn("Enviadas(con ack)");
        tclReqAckSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property5"));
        setMinPrefAndMaxWidthToColTable(tclReqAckSent, generalColumnWight);

        TableColumn tclSendingFailed = new TableColumn("No enviadas");
        tclSendingFailed.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property6"));
        setMinPrefAndMaxWidthToColTable(tclSendingFailed, generalColumnWight);

        TableColumn tclRelativeReqAckSent = new TableColumn("% de envió(con ack)");
        tclRelativeReqAckSent.setCellValueFactory(new PropertyValueFactory<PhosphorusPropertySet, String>("property7"));
        setMinPrefAndMaxWidthToColTable(tclRelativeReqAckSent, generalColumnWight);

        TableColumn tclAllReqRecieved = new TableColumn("SOLICITUDES RECIBIDAS DE AGENDAMIENTO");

        TableColumn reqRecievedAssigned = new TableColumn("Asignadas");

        reqRecievedAssigned.getColumns().addAll(tclReqAckSent, tclSendingFailed, tclRelativeReqAckSent);

        tclAllReqRecieved.getColumns().addAll(reqRecievedAssigned, tclNoFreeResource, tclReqRecieved);

        tvBrokerResults.getColumns().addAll(tclBrokerName, tclRegistrationReceived, tclAllReqRecieved);
        tvBrokerResults.setItems(dataBroker);
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

    /**
     * ***********************************************************************
     */
    /**
     * ******************Static class to show the results in*************************
     */
    /**
     * ********************the tablesview of results**************************
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