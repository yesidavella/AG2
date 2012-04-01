package com.ag2.presentation.control;

import com.ag2.model.SimulationBase;
import com.sun.javafx.runnable.Runnable0;
import java.io.Serializable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
//FIME: Traducir esta clase
public class PhosphosrusResults implements ViewResultsPhosphorus, Serializable {

    private transient Tab tab;
    private transient TableView tvResultadosClientePhosphorus = new TableView();
    private transient TableView tvResultadosRecursoPhosphorus = new TableView();
    private transient TableView tvResultadosConmutadorPhosphorus = new TableView();
    private transient Label lblCliente = new Label("Resultados del cliente");
    private transient Label lbRecurso = new Label("Resultados del recurso");
    private transient Label lbConmutador = new Label("Resultados del Conmutador Optico");
    private transient ObservableList<ConjuntoProiedadesPhosphorus> dataCliente = FXCollections.observableArrayList();
    private transient ObservableList<ConjuntoProiedadesPhosphorus> dataRecurso = FXCollections.observableArrayList();
    private transient ObservableList<ConjuntoProiedadesPhosphorus> dataConmutador = FXCollections.observableArrayList();
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

    public PhosphosrusResults(Tab tab) {
        this.tab = tab;

        lblCliente.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbRecurso.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbConmutador.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        scPnResults = new ScrollPane();

        hBoxProgress = new HBox();
        vBoxDataProgress = new VBox();
        vBoxImageProgress = new VBox();

        gpnDataProgress = new GridPane();
        gpnDataProgress.setPadding(new Insets(10, 10, 10, 10));

        gpnDataProgress.setVgap(5);
        gpnDataProgress.setHgap(11);

        scPnResults.getStyleClass().addAll("boxLogosVerticalGradient");
        gpnDataProgress.getStyleClass().addAll("boxLogosVerticalGradient");
        vBoxImageProgress.getStyleClass().addAll("boxLogosVerticalGradient");


        hBoxProgress.setPadding(new Insets(20, 20, 20, 20));
        hBoxProgress.setSpacing(20);

        hBoxProgress.setAlignment(Pos.CENTER);

        Label lbTitle = new Label(" Progreso ");
        lbTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));

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

        creartvResultadosCliente();
        creartvResultadosRecurso();
        creartvResultadosConmutadores();
        vBoxImageProgress.getChildren().add(progressIndicator);

        hBoxProgress.getChildren().addAll(vBoxDataProgress, vBoxImageProgress);
        tvResultadosClientePhosphorus.setPrefHeight(175);
        tvResultadosRecursoPhosphorus.setPrefHeight(175);
        tvResultadosConmutadorPhosphorus.setPrefHeight(175);

        vBoxMain.getChildren().addAll(hBoxProgress, lblCliente, tvResultadosClientePhosphorus, lbRecurso, tvResultadosRecursoPhosphorus, lbConmutador, tvResultadosConmutadorPhosphorus);
        scPnResults.setContent(vBoxMain);
        scPnResults.setFitToWidth(true);
        tab.setContent(scPnResults);

    }

    private void setFont(Label label) {
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
    }

    public void looktToNextExecution() {
        int sizeClients = dataCliente.size();
        for (int i = 0; i < sizeClients; i++) {
            dataCliente.remove(0);
        }
        int sizeSwitches = dataConmutador.size();
        for (int i = 0; i < sizeSwitches; i++) {
            dataConmutador.remove(0);
        }
        int sizeResources = dataRecurso.size();
        for (int i = 0; i < sizeResources; i++) {
            dataRecurso.remove(0);
        }

        showProgressIndicator();
    }

    public void showProgressIndicator() {
        progressIndicator.setProgress(0);
    }

    private void creartvResultadosConmutadores() {

        TableColumn tcConmutador = new TableColumn("Conmutador Optico");
        tcConmutador.setMinWidth(130);
        tcConmutador.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property1"));

        TableColumn tcMensajesTrabajoConmutados = new TableColumn("Mensajes De Trabajo Conmutados");
        tcMensajesTrabajoConmutados.setMinWidth(130);
        tcMensajesTrabajoConmutados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property2"));

        TableColumn tcMensajesTrabajoNoConmutados = new TableColumn("Mensajes De Trabajo No Conmutados");
        tcMensajesTrabajoNoConmutados.setMinWidth(130);
        tcMensajesTrabajoNoConmutados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property3"));

        TableColumn tcMensajesResultadosConmutados = new TableColumn("Mensajes De Resultados  Conmutados");
        tcMensajesResultadosConmutados.setMinWidth(130);
        tcMensajesResultadosConmutados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property4"));


        TableColumn tcMensajesResultadosNoConmutados = new TableColumn("Mensajes De Resultados No  Conmutados");
        tcMensajesResultadosNoConmutados.setMinWidth(130);
        tcMensajesResultadosNoConmutados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property5"));

        TableColumn relDropJob = new TableColumn("Rel Drop Job"); //FIME: Cambiar el nombre
        relDropJob.setMinWidth(50);
        relDropJob.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property6"));

        TableColumn relDropRes = new TableColumn("Rel Drop Res"); //FIME: Cambiar el nombre
        relDropRes.setMinWidth(50);
        relDropRes.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property7"));

        TableColumn reltotDrop = new TableColumn("Rel tot Drop"); //FIME: Cambiar el nombre
        reltotDrop.setMinWidth(50);
        reltotDrop.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property8"));

        tvResultadosConmutadorPhosphorus.getColumns().addAll(tcConmutador, tcMensajesTrabajoConmutados,
                tcMensajesTrabajoNoConmutados, tcMensajesResultadosConmutados, tcMensajesResultadosNoConmutados, relDropJob, relDropRes, reltotDrop);
        tvResultadosConmutadorPhosphorus.setItems(dataConmutador);
    }

    private void creartvResultadosRecurso() {

        TableColumn tcRecurso = new TableColumn("Recurso");
        tcRecurso.setMinWidth(80);
        tcRecurso.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property1"));

        TableColumn tcTrabajosRecibidos = new TableColumn("Trabajos recibidos");
        tcTrabajosRecibidos.setMinWidth(130);
        tcTrabajosRecibidos.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property2"));

        TableColumn tcFallasNoEspacio = new TableColumn("Fallas/No Espacio");
        tcFallasNoEspacio.setMinWidth(130);
        tcFallasNoEspacio.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property3"));

        TableColumn tcFallasEnviadas = new TableColumn("Fallas Enviadas");
        tcFallasEnviadas.setMinWidth(150);
        tcFallasEnviadas.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property4"));

        tvResultadosRecursoPhosphorus.getColumns().addAll(tcRecurso, tcTrabajosRecibidos, tcFallasNoEspacio, tcFallasEnviadas);
        tvResultadosRecursoPhosphorus.setItems(dataRecurso);
    }

    private void creartvResultadosCliente() {


        TableColumn tcCliente = new TableColumn("Cliente");
        tcCliente.setMinWidth(80);
        tcCliente.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property1"));

        TableColumn tcPeticionesEnviadas = new TableColumn("Peticiones Enviadas");
        tcPeticionesEnviadas.setMinWidth(130);
        tcPeticionesEnviadas.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property2"));

        TableColumn tcTrabajosEnviados = new TableColumn("Trabajos Enviados");
        tcTrabajosEnviados.setMinWidth(130);
        tcTrabajosEnviados.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property3"));

        TableColumn tcResultadosRecibidos = new TableColumn("Resultados Recibidos");
        tcResultadosRecibidos.setMinWidth(150);
        tcResultadosRecibidos.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property4"));

        TableColumn tcPeticionesFallidas = new TableColumn("Peticiones Fallidas");
        tcPeticionesFallidas.setMinWidth(130);
        tcPeticionesFallidas.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property5"));

        TableColumn tcPorcentajeResultadosRecibidos = new TableColumn(" %Resultados Recibidos");
        tcPorcentajeResultadosRecibidos.setMinWidth(160);
        tcPorcentajeResultadosRecibidos.setCellValueFactory(new PropertyValueFactory<ConjuntoProiedadesPhosphorus, String>("property6"));

        tvResultadosClientePhosphorus.getColumns().addAll(tcCliente, tcPeticionesEnviadas, tcTrabajosEnviados, tcResultadosRecibidos,
                tcPeticionesFallidas, tcPorcentajeResultadosRecibidos);
        tvResultadosClientePhosphorus.setItems(dataCliente);
    }

    public void adicionarResultadoCliente(String tcCliente, String tcPeticionesEnviadas,
            String tcTrabajosEnviados, String tcResultadosRecibidos, String tcPeticionesFallidas, String tcPorcentajeResultadosRecibidos) {


        progressIndicator.setProgress(1);
        lblSimulationTimePercentageValue.setText("100%");
        //  hideProgressIndicator();

        ConjuntoProiedadesPhosphorus cpp = new ConjuntoProiedadesPhosphorus();
        cpp.setProperty1(tcCliente);
        cpp.setProperty2(tcPeticionesEnviadas);
        cpp.setProperty3(tcTrabajosEnviados);
        cpp.setProperty4(tcResultadosRecibidos);
        cpp.setProperty5(tcPeticionesFallidas);
        cpp.setProperty6(tcPorcentajeResultadosRecibidos);
        dataCliente.add(cpp);

    }

    public void adicionarResultadoRecurso(String tcRecurso, String tcTrabajosRecibidos, String tcFallasNoEspacio, String tcFallasEnviadas) {

        ConjuntoProiedadesPhosphorus cpp = new ConjuntoProiedadesPhosphorus();
        cpp.setProperty1(tcRecurso);
        cpp.setProperty2(tcTrabajosRecibidos);
        cpp.setProperty3(tcFallasNoEspacio);
        cpp.setProperty4(tcFallasEnviadas);
        dataRecurso.add(cpp);

    }

    public void adicionarResultadoConmutador(
            String tcConmutador,
            String tcMensajesTrabajoConmutados,
            String tcMensajesTrabajoNoConmutados,
            String tcMensajesResultadosConmutados,
            String tcMensajesResultadosNoConmutados,
            String relDropJob,
            String relDropRes,
            String reltotDrop) {

        ConjuntoProiedadesPhosphorus cpp = new ConjuntoProiedadesPhosphorus();
        cpp.setProperty1(tcConmutador);
        cpp.setProperty2(tcMensajesTrabajoConmutados);
        cpp.setProperty3(tcMensajesTrabajoNoConmutados);
        cpp.setProperty4(tcMensajesResultadosConmutados);
        cpp.setProperty5(tcMensajesResultadosNoConmutados);
        cpp.setProperty6(relDropJob);
        cpp.setProperty7(relDropRes);
        cpp.setProperty8(reltotDrop);
        dataConmutador.add(cpp);

    }

    @Override
    public void setExecutionPercentage(final double Percentage) {
        //XXX:Esto se revienta aveces.

        progressIndicator.setProgress(Percentage / 100);
        String value = String.valueOf(Percentage).substring(0, 5);
        lblSimulationTimePercentageValue.setText(value + " %");


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

        private ConjuntoProiedadesPhosphorus() {
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
    }
}
