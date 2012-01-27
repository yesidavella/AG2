/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion.controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author Frank
 */
public class ResultadosPhosphorus implements ViewResultsPhosphorus {
    
    private Tab tab;
    private TableView tvResultadosClientePhosphorus = new TableView();
    private TableView tvResultadosRecursoPhosphorus = new TableView();
    private TableView tvResultadosConmutadorPhosphorus = new TableView();
    Label lbCliente = new Label("Resultados del cliente");
    Label lbRecurso = new Label("Resultados del recurso");
    Label lbConmutador = new Label("Resultados del Conmutador Optico");
    private ObservableList<ConjuntoProiedadesPhosphorus> dataCliente = FXCollections.observableArrayList();
    private ObservableList<ConjuntoProiedadesPhosphorus> dataRecurso = FXCollections.observableArrayList();
    private ObservableList<ConjuntoProiedadesPhosphorus> dataConmutador = FXCollections.observableArrayList();
    private VBox vBox = new VBox();
    private final ProgressIndicator progressIndicator = new ProgressIndicator(0);
    
    public ResultadosPhosphorus(Tab tab) {
        this.tab = tab;
        progressIndicator.setMinSize(1, 1);    
        progressIndicator.setVisible(false);
       
        vBox.setSpacing(7);
        vBox.setPadding(new Insets(8, 8, 8, 8));
        vBox.setAlignment(Pos.CENTER);
        
        creartvResultadosCliente();
        creartvResultadosRecurso();
        creartvResultadosConmutadores();
        vBox.getChildren().addAll(progressIndicator, lbCliente, tvResultadosClientePhosphorus, lbRecurso, tvResultadosRecursoPhosphorus, lbConmutador, tvResultadosConmutadorPhosphorus);
        tab.setContent(vBox);
        
        
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
    public void showProgressIndicator()
    {
        progressIndicator.setMinSize(250, 250);
        progressIndicator.setVisible(true);
        lbCliente.setVisible(false);
        tvResultadosClientePhosphorus.setVisible(false);
        lbRecurso.setVisible(false);
        tvResultadosRecursoPhosphorus.setVisible(false);
        lbConmutador.setVisible(false);
        tvResultadosConmutadorPhosphorus.setVisible(false);
        
    }
    public void hideProgressIndicator()
    {
        if (progressIndicator.isVisible()) 
        {
            progressIndicator.setMaxSize(1, 1);
            progressIndicator.setMinSize(1, 1);
            progressIndicator.setVisible(false);

            lbCliente.setVisible(true);
            tvResultadosClientePhosphorus.setVisible(true);
            lbRecurso.setVisible(true);
            tvResultadosRecursoPhosphorus.setVisible(true);
            lbConmutador.setVisible(true);
            tvResultadosConmutadorPhosphorus.setVisible(true);
        }
    }
    
    private void creartvResultadosConmutadores() {
        lbConmutador.setFont(new Font("Arial", 14));
        lbConmutador.setLayoutY(10);
        
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
        lbRecurso.setFont(new Font("Arial", 14));
        lbRecurso.setLayoutY(10);
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
        
        
        lbCliente.setFont(new Font("Arial", 14));
        lbCliente.setLayoutY(10);
        
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
        
        
       hideProgressIndicator();
        
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
    
    public void setExecutionPercentage(double Percentage) {
        progressIndicator.setProgress(Percentage / 100);
      
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
