package com.ag2.presentacion;

import com.ag2.controlador.*;
import com.ag2.modelo.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{ 
  
    
    
    @Override
    public void start(Stage stgEscenario){

        stgEscenario.setTitle("Modelo AG2- Simulador Grafico");
        stgEscenario.setScene(IGU.getInstanciaIGUAg2());
        IGU.getInstanciaIGUAg2().setStage(stgEscenario);
        stgEscenario.show();       
      
        inicializarModelosYContrladoresDeCreacionDeNodos();
    }
    
    public static void main(String[] args){
        Application.launch(args);
    }

    private void inicializarModelosYContrladoresDeCreacionDeNodos() {
        
        //Controladores y Modelos
        ControladorAbstractoAdminNodo ctrlCreadorYAdministradorNodo = new ControladorAdminNodo();
        ExecuteAbstractController  executeAbstractController = new ExecuteController(); 
        
        IGU.getInstanciaIGUAg2().getGrGrupoDeDiseño().addControladorCrearNodo(ctrlCreadorYAdministradorNodo);
        ctrlCreadorYAdministradorNodo.addVistaGraficaNodoses(IGU.getInstanciaIGUAg2().getGrGrupoDeDiseño());
        ctrlCreadorYAdministradorNodo.addVistaGraficaNodoses(IGU.getInstanciaIGUAg2().getPropiedadesDispositivoTbl());
        IGU.getInstanciaIGUAg2().getPropiedadesDispositivoTbl().addControladorAbstractoAdminNodo(ctrlCreadorYAdministradorNodo);
        IGU.getInstanciaIGUAg2().getExecutePane().setExecuteAbstractController(executeAbstractController);
        
        
        ModeloCrearNodo modeloCrearNodo = new ModeloCrearCliente();
        ctrlCreadorYAdministradorNodo.addModelo(modeloCrearNodo);
        
        modeloCrearNodo = new ModeloCrearNodoDeServicio();
        ctrlCreadorYAdministradorNodo.addModelo(modeloCrearNodo);
        
        modeloCrearNodo = new ModeloCrearNodoDeRecurso();
        ctrlCreadorYAdministradorNodo.addModelo(modeloCrearNodo);
        
        modeloCrearNodo = new ModeloCrearEnrutadorRafaga();
        ctrlCreadorYAdministradorNodo.addModelo(modeloCrearNodo);
        
        modeloCrearNodo = new ModeloCrearEnrutadorOptico();
        ctrlCreadorYAdministradorNodo.addModelo(modeloCrearNodo);
        
        modeloCrearNodo = new ModeloCrearEnrutadorHibrido();
        ctrlCreadorYAdministradorNodo.addModelo(modeloCrearNodo);
        
        ControladorAbstractoAdminEnlace ctrlCrearYAdminEnlace = new ControladorAdminEnlace();
        ModeloAbstractoCrearEnlace modeloCrearEnlace = new ModeloCrearEnlace();
        ctrlCrearYAdminEnlace.addModelo(modeloCrearEnlace);
        
        ctrlCrearYAdminEnlace.addVistaEnlace(IGU.getInstanciaIGUAg2().getPropiedadesDispositivoTbl());
        IGU.getInstanciaIGUAg2().getPropiedadesDispositivoTbl().addControladorAdminEnlace(ctrlCrearYAdminEnlace);
        IGU.getInstanciaIGUAg2().getGrGrupoDeDiseño().addControladorCrearEnlace(ctrlCrearYAdminEnlace);
        
        ResultsController resultsController = new ResultsController();
        resultsController.setViewResultsPhosphorus(IGU.getInstanciaIGUAg2().getResustadosPhosphorus());
        SimulacionBase.getInstance().setResultsAbstractController(resultsController);
    }
}
