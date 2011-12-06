package com.ag2.presentacion;

import Grid.GridSimulation;
import Grid.GridSimulator;
import com.ag2.controlador.ControladorCreacionNodo;
import com.ag2.controlador.ControladorCreacionYAdminDeNodo;
import com.ag2.modelo.*;
import javafx.application.Application;
import javafx.stage.Stage;
import simbase.SimulationInstance;

public class Main extends Application{
    
    private static Stage stgEscenario;
    public static GridSimulator simulador;
    public SimulationInstance simulacion;
    
    @Override
    public void start(Stage stgEscenario){

        stgEscenario.setTitle("Modelo AG2- Simulador Grafico");
        this.stgEscenario=stgEscenario;//FIXME: Esto es una mierdaaaaaa
        stgEscenario.setScene(IGU.getInstanciaIGUAg2());
        stgEscenario.show();
        
        inicializarSimulacion();
        inicializarModelosYContrladoresDeCreacionDeNodos();
    }
    
    public static void main(String[] args){
        Application.launch(args);
    }
    
    public static Stage getStgEscenario() {
        return stgEscenario;
    }
    
    private void inicializarSimulacion() {
        simulacion = new GridSimulation("ConfigInit.cfg");
        simulador = new GridSimulator();
        simulacion.setSimulator(simulador);
    }

    private void inicializarModelosYContrladoresDeCreacionDeNodos() {
        
        //Controladores y Modelos
        ControladorCreacionYAdminDeNodo ctrlCreadorYAdministradorNodo = new ControladorCreacionNodo();
        IGU.getInstanciaIGUAg2().getGrGrupoDeDiseño().addControladorCrearNodo(ctrlCreadorYAdministradorNodo);
        ctrlCreadorYAdministradorNodo.addVistaGrDeDiseño(IGU.getInstanciaIGUAg2().getGrGrupoDeDiseño());
        
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
    }
}
