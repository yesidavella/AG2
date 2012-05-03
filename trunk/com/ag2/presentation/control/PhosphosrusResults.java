package com.ag2.presentation.control;

import Grid.Entity;
import Grid.Utilities.HtmlWriter;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.controller.MatchCoupleObjectContainer;
import com.ag2.presentation.design.GraphNode;
import com.ag2.util.Utils;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
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
//FIME: Traducir esta clase
public class PhosphosrusResults implements ViewResultsPhosphorus, Serializable {

    private transient Tab tab;
    private transient TableView tvClientResults = new TableView();
    private transient TableView tvResourceResults = new TableView();
    private transient TableView tvSwitchResults = new TableView();
    private transient TableView tvBrokerResults = new TableView();
    private transient Label lblClient = new Label("Resultados del cliente");
    private transient Label lbResource = new Label("Resultados del recurso");
    private transient Label lbSwitch = new Label("Resultados del Conmutador Optico");
    private transient Label lbBroker = new Label("Resultados del Agendador");
    private transient ObservableList<ConjuntoProiedadesPhosphorus> dataClient = FXCollections.observableArrayList();
    private transient ObservableList<ConjuntoProiedadesPhosphorus> dataResource = FXCollections.observableArrayList();
    private transient ObservableList<ConjuntoProiedadesPhosphorus> dataSwitch = FXCollections.observableArrayList();
    private transient ObservableList<ConjuntoProiedadesPhosphorus> dataBroker = FXCollections.observableArrayList();
    private transient ScrollPane scPnResults;
    private transient VBox vBoxMain = new VBox();
    private transient ProgressIndicator progressIndicator;
    private transient HBox hBoxProgress;
    private transient VBox vBoxDataProgress;
    private transient VBox vBoxImageProgress;
    private transient GridPane gpnDataProgress;
    private transient Label lblSimulationTimeValue = new Label("00.00");
    private transient Label lblSimulationTimePercentageValue = new Label("00%");
    private transient Label lblRealTimeValue = new Label("00.00");
    private transient Label lblPageHTMLCountValue = new Label("0");
    private transient Timeline time = new Timeline();
    private transient long tiempoInicial = System.currentTimeMillis();
    protected HashMap<GraphNode, Entity> nodeMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer();
    private int columnWidthSmall = 65;
    private int columnWidthMedium = 100;
    private int columnWidthBig = 160;
    private int tableViewHeight = 300;

    public PhosphosrusResults(Tab tab) {
        this.tab = tab;

        lblClient.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbResource.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbSwitch.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbBroker.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        scPnResults = new ScrollPane();

        hBoxProgress = new HBox();
        vBoxDataProgress = new VBox();
        vBoxImageProgress = new VBox();

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
        vBoxImageProgress.setEffect(dropShadow);

        hBoxProgress.setPadding(new Insets(20, 20, 20, 20));
        hBoxProgress.setSpacing(20);

        hBoxProgress.setAlignment(Pos.CENTER);

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


        vBoxDataProgress.getChildren().add(gpnDataProgress);

        progressIndicator = new ProgressIndicator(0);
        progressIndicator.setMinSize(200, 200);


        vBoxMain.setSpacing(7);
        vBoxMain.setPadding(new Insets(8, 8, 8, 8));
        vBoxMain.setPrefSize(1200, 900);
        vBoxMain.setAlignment(Pos.CENTER);

        createtvClientResults();
        createtvResultsResource();
        createtvBrokerResults();
        createtvResultsSwitch();

        vBoxImageProgress.getChildren().add(progressIndicator);

        hBoxProgress.getChildren().addAll(vBoxDataProgress, vBoxImageProgress);
        tvClientResults.setPrefHeight(tableViewHeight);
        tvResourceResults.setPrefHeight(tableViewHeight);
        tvSwitchResults.setPrefHeight(tableViewHeight);
        tvBrokerResults.setPrefHeight(tableViewHeight);
        tvClientResults.setMinHeight(tableViewHeight);
        tvResourceResults.setMinHeight(tableViewHeight);
        tvSwitchResults.setMinHeight(tableViewHeight);
        tvBrokerResults.setMinHeight(tableViewHeight);

        vBoxMain.getChildren().addAll(hBoxProgress, lblClient, tvClientResults, lbResource, tvResourceResults,
                lbSwitch, tvSwitchResults, lbBroker, tvBrokerResults);
        scPnResults.setContent(vBoxMain);
        scPnResults.setFitToWidth(true);
        tab.setContent(scPnResults);

        time.setCycleCount(Timeline.INDEFINITE);


        tiempoInicial = System.currentTimeMillis();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500), new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                Calendar tiempo = Calendar.getInstance();
                tiempo.setTimeInMillis(System.currentTimeMillis() - tiempoInicial);
                String hour = String.valueOf((tiempo.get(Calendar.HOUR) - 7));
                String minute = String.valueOf(tiempo.get(Calendar.MINUTE));
                String second = String.valueOf(tiempo.get(Calendar.SECOND));

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
        tiempoInicial = System.currentTimeMillis();
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

        TableColumn tcConmutador = new TableColumn("Conmutador 0ptico");
        tcConmutador.setMinWidth(130);
        tcConmutador.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property1"));

        TableColumn tcMensajesTrabajoConmutados = new TableColumn("Trabajos conmutados");
        tcMensajesTrabajoConmutados.setMinWidth(130);
        tcMensajesTrabajoConmutados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property2"));

        TableColumn tcMensajesTrabajoNoConmutados = new TableColumn("Trabajos no conmutados");
        tcMensajesTrabajoNoConmutados.setMinWidth(130);
        tcMensajesTrabajoNoConmutados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property3"));

        TableColumn tcMensajesResultadosConmutados = new TableColumn("Resultados conmutados");
        tcMensajesResultadosConmutados.setMinWidth(130);
        tcMensajesResultadosConmutados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property4"));


        TableColumn tcMensajesResultadosNoConmutados = new TableColumn("Resultados no conmutados");
        tcMensajesResultadosNoConmutados.setMinWidth(150);
        tcMensajesResultadosNoConmutados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property5"));

        TableColumn tcMensajesSolicitudConmutados = new TableColumn("Solicitudes conmutadas");
        tcMensajesSolicitudConmutados.setMinWidth(160);
        tcMensajesSolicitudConmutados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property6"));

        TableColumn tcMensajesSolicitudNoConmutados = new TableColumn("Solicitudes no conmutadas");
        tcMensajesSolicitudNoConmutados.setMinWidth(160);
        tcMensajesSolicitudNoConmutados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property7"));





        TableColumn relDropJob = new TableColumn("% Trabajos no conmutados");
        relDropJob.setMinWidth(150);
        relDropJob.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property8"));

        TableColumn relDropRes = new TableColumn("% Resultados no conmutados");
        relDropRes.setMinWidth(150);
        relDropRes.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property9"));

        TableColumn relDropReq = new TableColumn("% Solicitudes no conmutadas");
        relDropReq.setMinWidth(150);
        relDropReq.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property10"));


        TableColumn reltotDrop = new TableColumn("% Total no conmutados");
        reltotDrop.setMinWidth(150);
        reltotDrop.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property11"));

        tvSwitchResults.getColumns().addAll(tcConmutador, tcMensajesTrabajoConmutados,
                tcMensajesTrabajoNoConmutados, tcMensajesResultadosConmutados, tcMensajesResultadosNoConmutados,
                tcMensajesSolicitudConmutados, tcMensajesSolicitudNoConmutados,
                relDropJob, relDropRes, relDropReq, reltotDrop);
        tvSwitchResults.setItems(dataSwitch);
    }

    private void createtvResultsResource() {

        TableColumn tcRecurso = new TableColumn("Recurso");
        tcRecurso.setMinWidth(80);
        tcRecurso.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property1"));

        TableColumn tableColumn1 = new TableColumn("Trabajos recibidos");
        tableColumn1.setMinWidth(130);
        tableColumn1.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property2"));

        TableColumn tableColumn2 = new TableColumn("Respuestas enviadas");
        tableColumn2.setMinWidth(130);
        tableColumn2.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property3"));

        TableColumn tableColumn3 = new TableColumn("%Respuestas enviadas");
        tableColumn3.setMinWidth(135);
        tableColumn3.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property4"));

        TableColumn tableColumn4 = new TableColumn("Fallas enviadas");
        tableColumn4.setMinWidth(120);
        tableColumn4.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property5"));

        TableColumn tableColumn5 = new TableColumn("% Fallas enviadas");
        tableColumn5.setMinWidth(130);
        tableColumn5.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property6"));

        TableColumn tableColumn6 = new TableColumn("Tiempo ocupacion (ms) ");
        tableColumn6.setMinWidth(160);
        tableColumn6.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property7"));

        TableColumn tableColumn7 = new TableColumn("% Tiempo ocupacion");
        tableColumn7.setMinWidth(160);
        tableColumn7.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property8"));


        TableColumn tableColumn8 = new TableColumn("CPU/buffer no libre  ");
        tableColumn8.setMinWidth(160);
        tableColumn8.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property9"));


        TableColumn tableColumn9 = new TableColumn("% CPU/buffer no libre  ");
        tableColumn9.setMinWidth(160);
        tableColumn9.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property10"));

        tvResourceResults.getColumns().addAll(tcRecurso, tableColumn1, tableColumn2, tableColumn3,
                tableColumn4, tableColumn5, tableColumn6, tableColumn7, tableColumn8, tableColumn9);
        tvResourceResults.setItems(dataResource);
    }

    private void createtvClientResults() {


        TableColumn tcCliente = new TableColumn("Cliente");
        tcCliente.setMinWidth(80);
        tcCliente.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property1"));


        TableColumn tableColumn1 = new TableColumn("Solicitudes \n creadas");
        tableColumn1.setMinWidth(columnWidthSmall);
        tableColumn1.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property2"));

        TableColumn tableColumn2 = new TableColumn("Solicitudes \n enviadas");
        tableColumn2.setMinWidth(columnWidthSmall);
        tableColumn2.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property3"));

        TableColumn tableColumn3 = new TableColumn("Solicitudes \n no enviadas");
        tableColumn3.setMinWidth(columnWidthSmall);
        tableColumn3.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property4"));

        TableColumn tableColumn4 = new TableColumn("Trabajos \n enviados");
        tableColumn4.setMinWidth(columnWidthSmall);
        tableColumn4.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property5"));

        TableColumn tableColumn5 = new TableColumn("Trabajos \n no enviados");
        tableColumn5.setMinWidth(columnWidthSmall);
        tableColumn5.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property6"));

        TableColumn tableColumn6 = new TableColumn("Resultados \n recibidos");
        tableColumn6.setMinWidth(columnWidthSmall);
        tableColumn6.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property7"));

        TableColumn tableColumn7 = new TableColumn("% Solicitudes enviadas \n/Solicitudes creadas ");
        tableColumn7.setMinWidth(columnWidthBig);
        tableColumn7.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property8"));

        TableColumn tableColumn8 = new TableColumn("% Trabajos enviados\n/Solicitudes enviadas");
        tableColumn8.setMinWidth(columnWidthBig);
        tableColumn8.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property9"));

        TableColumn tableColumn9 = new TableColumn("% Resultados recibidos\n/Trabajos enviados");
        tableColumn9.setMinWidth(columnWidthBig);
        tableColumn9.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property10"));

        TableColumn tableColumn10 = new TableColumn("% Resultados recibidos\n/Solicitudes enviadas");
        tableColumn10.setMinWidth(columnWidthBig);
        tableColumn10.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property11"));

        TableColumn tableColumn11 = new TableColumn("% Resultados recibidos\n/Solicitudes creadas ");
        tableColumn11.setMinWidth(columnWidthBig);
        tableColumn11.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property12"));




        tvClientResults.getColumns().addAll(
                tcCliente,
                tableColumn1,
                tableColumn2,
                tableColumn3,
                tableColumn4,
                tableColumn5,
                tableColumn6,
                tableColumn7,
                tableColumn8,
                tableColumn9,
                tableColumn10,
                tableColumn11);
        tvClientResults.setItems(dataClient);
    }

    @Override
    public void addClientResult(
            String clientName,
            String requestCreated,
            String requestSent,
            String requestNoSent,
            String jobSent,
            String jobNoSent,
            String resultReceive,
            String relativeRequestSent,
            String relativeJobSent,
            String relativeReceiveResult_jobSent,
            String relativeReceiveResult_requsetSent,
            String relativeReceiveresult_requestCreated) {

        time.stop();

        ConjuntoProiedadesPhosphorus cpp = new ConjuntoProiedadesPhosphorus();
        cpp.setProperty1(Utils.findGraphicalName(clientName));
        cpp.setProperty2(requestCreated);
        cpp.setProperty3(requestSent);
        cpp.setProperty4(requestNoSent);
        cpp.setProperty5(jobSent);
        cpp.setProperty6(jobNoSent);
        cpp.setProperty7(resultReceive);
        cpp.setProperty8(relativeRequestSent);
        cpp.setProperty9(relativeJobSent);
        cpp.setProperty10(relativeReceiveResult_jobSent);
        cpp.setProperty11(relativeReceiveResult_requsetSent);
        cpp.setProperty12(relativeReceiveresult_requestCreated);

        dataClient.add(cpp);
    }

    @Override
    public void addResourceResult(
            String tcRecurso,
            String tcTrabajosRecibidos,
            String tcTrabajosEnviados,
            String relativeTrabajosEnviados,
            String tcFallasEnviadas,
            String relativeFallasEnviadas,
            String tcTimeNoCPUFree,
            String tcRelativeTimeNoCPUFree,
            String tcVecesFallasNoEspacio,
            String relativeVecesFallasNoEspacio) {

        ConjuntoProiedadesPhosphorus cpp = new ConjuntoProiedadesPhosphorus();
        cpp.setProperty1(Utils.findGraphicalName(tcRecurso));
        cpp.setProperty2(tcTrabajosRecibidos);
        cpp.setProperty3(tcTrabajosEnviados);
        cpp.setProperty4(relativeTrabajosEnviados);
        cpp.setProperty5(tcFallasEnviadas);
        cpp.setProperty6(relativeFallasEnviadas);
        cpp.setProperty7(tcTimeNoCPUFree);
        cpp.setProperty8(tcRelativeTimeNoCPUFree);
        cpp.setProperty9(tcVecesFallasNoEspacio);
        cpp.setProperty10(relativeVecesFallasNoEspacio);
        dataResource.add(cpp);

    }

    @Override
    public void addSwitchResult(
            String tcConmutador,
            String tcMensajesTrabajoConmutados,
            String tcMensajesTrabajoNoConmutados,
            String tcMensajesResultadosConmutados,
            String tcMensajesResultadosNoConmutados,
            String tcSwitchedJobRequest,
            String tcNonSwitchedJobRequest,
            String relDropJob,
            String relDropRes,
            String relDropReq,
            String reltotDrop) {

        ConjuntoProiedadesPhosphorus cpp = new ConjuntoProiedadesPhosphorus();
        cpp.setProperty1(Utils.findGraphicalName(tcConmutador));
        cpp.setProperty2(tcMensajesTrabajoConmutados);
        cpp.setProperty3(tcMensajesTrabajoNoConmutados);
        cpp.setProperty4(tcMensajesResultadosConmutados);
        cpp.setProperty5(tcMensajesResultadosNoConmutados);
        cpp.setProperty6(tcSwitchedJobRequest);
        cpp.setProperty7(tcNonSwitchedJobRequest);
        cpp.setProperty8(relDropJob);
        cpp.setProperty9(relDropRes);
        cpp.setProperty10(relDropReq);
        cpp.setProperty11(reltotDrop);

        dataSwitch.add(cpp);

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
    public void addBrokerResult(String brokerName,
            String noFreeResource,
            String registrationReceived,
            String reqAckSent,
            String reqRecieved,
            String sendingFailed) {

        ConjuntoProiedadesPhosphorus cpp = new ConjuntoProiedadesPhosphorus();
        cpp.setProperty1(Utils.findGraphicalName(brokerName));
        cpp.setProperty2(noFreeResource);
        cpp.setProperty3(registrationReceived);
        cpp.setProperty4(reqAckSent);
        cpp.setProperty5(reqRecieved);
        cpp.setProperty6(sendingFailed);

        dataBroker.add(cpp);
    }

    private void createtvBrokerResults() {

        TableColumn tcRecurso = new TableColumn("Agendador");
        tcRecurso.setMinWidth(80);
        tcRecurso.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property1"));

        TableColumn tableColumn1 = new TableColumn("Recurso no libre");
        tableColumn1.setMinWidth(130);
        tableColumn1.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property2"));

        TableColumn tableColumn2 = new TableColumn("Registro recibido");
        tableColumn2.setMinWidth(130);
        tableColumn2.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property3"));

        TableColumn tableColumn3 = new TableColumn("Solicitud de confirmacion enviada");
        tableColumn3.setMinWidth(135);
        tableColumn3.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property4"));

        TableColumn tableColumn4 = new TableColumn("Solicitud recibida");
        tableColumn4.setMinWidth(120);
        tableColumn4.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property5"));

        TableColumn tableColumn5 = new TableColumn("Fallo envio");
        tableColumn5.setMinWidth(120);
        tableColumn5.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property6"));

        tvBrokerResults.getColumns().addAll(tcRecurso, tableColumn1, tableColumn2, tableColumn3,
                tableColumn4, tableColumn5);
        tvBrokerResults.setItems(dataBroker);
    }

    public static class ConjuntoProiedadesPhosphorus {

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

        private ConjuntoProiedadesPhosphorus() {
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
