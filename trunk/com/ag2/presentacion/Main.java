package com.ag2.presentacion;

import com.ag2.config.serializacion.Serializador;
import com.ag2.controlador.*;
import com.ag2.modelo.*;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.Serializable;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application implements Serializable {

    private transient Serializador serializador;
    private ControladorAbstractoAdminNodo ctrlCreadorYAdministradorNodo;
    private ExecuteAbstractController executeAbstractController;
    private ModeloCrearNodo modeloCrearNodo;
    private ControladorAbstractoAdminEnlace ctrlCrearYAdminEnlace;
    private GrupoDeDiseno grupoDeDiseno;
    private SimulacionBase simulacionBase =  SimulacionBase.getInstance(); 
    private ResultsController resultsController;

    @Override
    public void start(final Stage stage) {

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
    

    public SimulacionBase getSimulacionBase() {
        return simulacionBase;
    }
   

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void inicializarModelosYContrladoresDeCreacionDeNodos() {

        //Controladores y Modelos
        ctrlCreadorYAdministradorNodo = new ControladorAdminNodo();
        executeAbstractController = new ExecuteController();
        resultsController = new ResultsController();

        IGU.getInstance().getGrGrupoDeDiseño().addControladorCrearNodo(ctrlCreadorYAdministradorNodo);
        
        ctrlCreadorYAdministradorNodo.addVistaGraficaNodoses(IGU.getInstance().getGrGrupoDeDiseño());
        ctrlCreadorYAdministradorNodo.addVistaGraficaNodoses(IGU.getInstance().getPropiedadesDispositivoTbl());
        IGU.getInstance().getPropiedadesDispositivoTbl().setControladorAbstractoAdminNodo(ctrlCreadorYAdministradorNodo);
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

        ctrlCrearYAdminEnlace.setVistaEnlace(IGU.getInstance().getPropiedadesDispositivoTbl());
        IGU.getInstance().getPropiedadesDispositivoTbl().setControladorAdminEnlace(ctrlCrearYAdminEnlace);
        IGU.getInstance().getGrGrupoDeDiseño().addControladorCrearEnlace(ctrlCrearYAdminEnlace);

       
        resultsController.setViewResultsPhosphorus(IGU.getInstance().getResustadosPhosphorus());
        SimulacionBase.getInstance().setResultsAbstractController(resultsController);
    }

    public ResultsController getResultsController() {
        return resultsController;
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
    public void loadFileBaseSimulation()
    {
        Main main = serializador.loadFileBaseSimulation();
        if (main != null) 
        {
            SimulacionBase.loadInstance(main.getSimulacionBase()); 
            
            ctrlCreadorYAdministradorNodo = main.getCtrlCreadorYAdministradorNodo();
            ctrlCrearYAdminEnlace = main.getCtrlCrearYAdminEnlace(); 
            executeAbstractController= main.getExecuteAbstractController();
            resultsController = main.getResultsController(); 
            
            IGU.getInstance().loadGrupoDeDiseno(main.getGrupoDeDiseno());
            
            IGU.getInstance().getExecutePane().setExecuteAbstractController(executeAbstractController);
              resultsController.setViewResultsPhosphorus(IGU.getInstance().getResustadosPhosphorus());
            
             ctrlCrearYAdminEnlace.setVistaEnlace(IGU.getInstance().getPropiedadesDispositivoTbl());
            IGU.getInstance().getPropiedadesDispositivoTbl().setControladorAdminEnlace(ctrlCrearYAdminEnlace);
            
            ctrlCreadorYAdministradorNodo.addVistaGraficaNodoses(IGU.getInstance().getGrGrupoDeDiseño());
            ctrlCreadorYAdministradorNodo.addVistaGraficaNodoses(IGU.getInstance().getPropiedadesDispositivoTbl());
            IGU.getInstance().getPropiedadesDispositivoTbl().setControladorAbstractoAdminNodo(ctrlCreadorYAdministradorNodo);

        }
    }

    public void save(boolean  thenClose) {
        serializador.guardar();
        if(thenClose)
        {
             System.exit(0);
        }
    }   

    public void load() {
        Main main = serializador.cargar();
        if (main != null) 
        {
            SimulacionBase.loadInstance(main.getSimulacionBase()); 
            
            ctrlCreadorYAdministradorNodo = main.getCtrlCreadorYAdministradorNodo();
            ctrlCrearYAdminEnlace = main.getCtrlCrearYAdminEnlace(); 
            executeAbstractController= main.getExecuteAbstractController();
            resultsController = main.getResultsController(); 
            
            IGU.getInstance().loadGrupoDeDiseno(main.getGrupoDeDiseno());
            
            IGU.getInstance().getExecutePane().setExecuteAbstractController(executeAbstractController);
              resultsController.setViewResultsPhosphorus(IGU.getInstance().getResustadosPhosphorus());
            
             ctrlCrearYAdminEnlace.setVistaEnlace(IGU.getInstance().getPropiedadesDispositivoTbl());
            IGU.getInstance().getPropiedadesDispositivoTbl().setControladorAdminEnlace(ctrlCrearYAdminEnlace);
            
            ctrlCreadorYAdministradorNodo.addVistaGraficaNodoses(IGU.getInstance().getGrGrupoDeDiseño());
            ctrlCreadorYAdministradorNodo.addVistaGraficaNodoses(IGU.getInstance().getPropiedadesDispositivoTbl());
            IGU.getInstance().getPropiedadesDispositivoTbl().setControladorAbstractoAdminNodo(ctrlCreadorYAdministradorNodo);

        }
    }
}
