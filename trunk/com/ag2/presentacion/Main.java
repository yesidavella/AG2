package com.ag2.presentacion;

import com.ag2.config.serializacion.Serializador;
import com.ag2.controlador.*;
import com.ag2.modelo.*;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.Serializable;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application implements Serializable
{
    private transient Serializador serializador; 
    private ControladorAbstractoAdminNodo ctrlCreadorYAdministradorNodo;
    private ExecuteAbstractController  executeAbstractController;      
    private ModeloCrearNodo modeloCrearNodo;         
    private ControladorAbstractoAdminEnlace ctrlCrearYAdminEnlace ; 
    private GrupoDeDiseno grupoDeDiseno; 
     
    @Override
    public void start(Stage stage)
    {
        
        stage.setTitle("Modelo AG2- Simulador Grafico");
        stage.setScene(IGU.getInstance());        
        IGU.getInstance().setStage(stage);      
        IGU.getInstance().setMain(this);
        stage.show();
        grupoDeDiseno = IGU.getInstance().getGrGrupoDeDiseño(); 

        inicializarModelosYContrladoresDeCreacionDeNodos();
        IGU.getInstance().inicializarEstadoDeIGU();
        serializador = new Serializador(this, stage);

    }
    
    public static void main(String[] args){
        Application.launch(args);
    }

    private void inicializarModelosYContrladoresDeCreacionDeNodos() {
        
        //Controladores y Modelos
        ctrlCreadorYAdministradorNodo = new ControladorAdminNodo();
        executeAbstractController = new ExecuteController(); 
        
        IGU.getInstance().getGrGrupoDeDiseño().addControladorCrearNodo(ctrlCreadorYAdministradorNodo);
        ctrlCreadorYAdministradorNodo.addVistaGraficaNodoses(IGU.getInstance().getGrGrupoDeDiseño());
        ctrlCreadorYAdministradorNodo.addVistaGraficaNodoses(IGU.getInstance().getPropiedadesDispositivoTbl());
        IGU.getInstance().getPropiedadesDispositivoTbl().addControladorAbstractoAdminNodo(ctrlCreadorYAdministradorNodo);
        IGU.getInstance().getExecutePane().setExecuteAbstractController(executeAbstractController);
        
        
        modeloCrearNodo = new ModeloCrearCliente();
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
        
        ctrlCrearYAdminEnlace = new ControladorAdminEnlace();
        ModeloAbstractoCrearEnlace modeloCrearEnlace = new ModeloCrearEnlace();
        ctrlCrearYAdminEnlace.addModelo(modeloCrearEnlace);
        
        ctrlCrearYAdminEnlace.addVistaEnlace(IGU.getInstance().getPropiedadesDispositivoTbl());
        IGU.getInstance().getPropiedadesDispositivoTbl().addControladorAdminEnlace(ctrlCrearYAdminEnlace);
        IGU.getInstance().getGrGrupoDeDiseño().addControladorCrearEnlace(ctrlCrearYAdminEnlace);
        
        ResultsController resultsController = new ResultsController();
        resultsController.setViewResultsPhosphorus(IGU.getInstance().getResustadosPhosphorus());
        SimulacionBase.getInstance().setResultsAbstractController(resultsController);
    }

    public ControladorAbstractoAdminNodo getCtrlCreadorYAdministradorNodo() {
        return ctrlCreadorYAdministradorNodo;
    }

    public ControladorAbstractoAdminEnlace getCtrlCrearYAdminEnlace() {
        return ctrlCrearYAdminEnlace;
    }

    public ExecuteAbstractController getExecuteAbstractController() {
        return executeAbstractController;
    }

    public ModeloCrearNodo getModeloCrearNodo() {
        return modeloCrearNodo;
    }

    public Serializador getSerializador() {
        return serializador;
    }

    public GrupoDeDiseno getGrupoDeDiseno() {
        return grupoDeDiseno;
    }
    
    
    public void save()
    {
        serializador.guardar();
    }
    public void load()
    {
        Main main =  serializador.cargar(); 
        if(main!=null)
        {
            IGU.getInstance().loadGrupoDeDiseno(main.getGrupoDeDiseno());
        }
        
    }
     
}

