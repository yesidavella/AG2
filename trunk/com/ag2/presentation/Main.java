package com.ag2.presentation;

import com.ag2.model.ModeloCrearNodoDeServicio;
import com.ag2.model.ModeloCrearEnrutadorHibrido;
import com.ag2.model.ModeloCrearCliente;
import com.ag2.model.LinkCreationAbstractModel;
import com.ag2.model.ModeloCrearEnlace;
import com.ag2.model.ModeloCrearEnrutadorRafaga;
import com.ag2.model.ModeloCrearEnrutadorOptico;
import com.ag2.model.NodeCreationModel;
import com.ag2.model.SimulationBase;
import com.ag2.model.ModeloCrearNodoDeRecurso;
import com.ag2.controller.LinkAdminController;
import com.ag2.controller.ExecuteController;
import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.ControladorAdminNodo;
import com.ag2.controller.ResultsController;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.config.serialization.UtilSerializator;
import com.ag2.presentation.design.GraphDesignGroup;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;

public class Main extends Application implements Serializable {

    private transient UtilSerializator serializador;
    private ControladorAdminNodo ctrlCreadorYAdministradorNodo;
    private ExecuteController executeAbstractController;
    private NodeCreationModel modeloCrearNodo;
    private LinkAdminAbstractController ctrlCrearYAdminEnlace;
    private GraphDesignGroup grupoDeDiseno;
    private SimulationBase simulacionBase =  SimulationBase.getInstance(); 
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
        serializador = new UtilSerializator(this, stage);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) 
            {
                int result = JOptionPane.showConfirmDialog(
                null, "¿Desea guardar los cambios efectuados en la simulación?", "Simulador AG2", JOptionPane.YES_NO_CANCEL_OPTION);

                if (result == JOptionPane.NO_OPTION) {
                    System.exit(0);
                } else if (result == JOptionPane.YES_OPTION) {
                    save(true);
                }event.consume();


            }
        });
        

    }
    

    public SimulationBase getSimulacionBase() {
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
        
        ctrlCreadorYAdministradorNodo.addGraphNodesView(IGU.getInstance().getGrGrupoDeDiseño());
        ctrlCreadorYAdministradorNodo.addGraphNodesView(IGU.getInstance().getPropiedadesDispositivoTbl());
        IGU.getInstance().getPropiedadesDispositivoTbl().setControladorAbstractoAdminNodo(ctrlCreadorYAdministradorNodo);
        IGU.getInstance().getExecutePane().setExecuteAbstractController(executeAbstractController);


        modeloCrearNodo = new ModeloCrearCliente();
        ctrlCreadorYAdministradorNodo.addModel(modeloCrearNodo);

        modeloCrearNodo = new ModeloCrearNodoDeServicio();
        ctrlCreadorYAdministradorNodo.addModel(modeloCrearNodo);

        modeloCrearNodo = new ModeloCrearNodoDeRecurso();
        ctrlCreadorYAdministradorNodo.addModel(modeloCrearNodo);

        modeloCrearNodo = new ModeloCrearEnrutadorRafaga();
        ctrlCreadorYAdministradorNodo.addModel(modeloCrearNodo);

        modeloCrearNodo = new ModeloCrearEnrutadorOptico();
        ctrlCreadorYAdministradorNodo.addModel(modeloCrearNodo);

        modeloCrearNodo = new ModeloCrearEnrutadorHibrido();
        ctrlCreadorYAdministradorNodo.addModel(modeloCrearNodo);

        ctrlCrearYAdminEnlace = new LinkAdminController();
        LinkCreationAbstractModel modeloCrearEnlace = new ModeloCrearEnlace();
        ctrlCrearYAdminEnlace.addModel(modeloCrearEnlace);

        ctrlCrearYAdminEnlace.setLinkView(IGU.getInstance().getPropiedadesDispositivoTbl());
        IGU.getInstance().getPropiedadesDispositivoTbl().setControladorAdminEnlace(ctrlCrearYAdminEnlace);
        IGU.getInstance().getGrGrupoDeDiseño().addControladorCrearEnlace(ctrlCrearYAdminEnlace);

       
        resultsController.setViewResultsPhosphorus(IGU.getInstance().getResustadosPhosphorus());
        SimulationBase.getInstance().setResultsAbstractController(resultsController);
        
        IGU.getInstance().getTbvSimulationProperties().setItems(PropertyPhosphorusTypeEnum.getData(executeAbstractController));
       
    }

    public ResultsController getResultsController() {
        return resultsController;
    }

    public ControladorAdminNodo getCtrlCreadorYAdministradorNodo() {
        return ctrlCreadorYAdministradorNodo;
    }

    public LinkAdminAbstractController getCtrlCrearYAdminEnlace() {
        return ctrlCrearYAdminEnlace;
    }

    public ExecuteController getExecuteAbstractController() {
        return executeAbstractController;
    }

    public NodeCreationModel getModeloCrearNodo() {
        return modeloCrearNodo;
    }

    public UtilSerializator getSerializador() {
        return serializador;
    }

    public GraphDesignGroup getGrupoDeDiseno() {
        return grupoDeDiseno;
    }
    public void loadFileBaseSimulation()
    {
        Main main = serializador.loadFileBaseSimulation();
        if (main != null) 
        {
            loadControllers(main);
        }
    }

    private void loadControllers(Main main) 
    {
        simulacionBase =   main.getSimulacionBase();  ///SimulacionBase.getInstance(); 
        SimulationBase.loadInstance(simulacionBase); 
        grupoDeDiseno = main.getGrupoDeDiseno();
        
        IGU.getInstance().loadGrupoDeDiseno(grupoDeDiseno);
   
        ctrlCreadorYAdministradorNodo = main.getCtrlCreadorYAdministradorNodo();
        ctrlCrearYAdminEnlace = main.getCtrlCrearYAdminEnlace(); 
        executeAbstractController= main.getExecuteAbstractController();
        resultsController = main.getResultsController(); 
       
        modeloCrearNodo = main.getModeloCrearNodo(); 
        
        
        IGU.getInstance().getExecutePane().setExecuteAbstractController(executeAbstractController);
        resultsController.setViewResultsPhosphorus(IGU.getInstance().getResustadosPhosphorus());
        
        ctrlCrearYAdminEnlace.setLinkView(IGU.getInstance().getPropiedadesDispositivoTbl());
        IGU.getInstance().getPropiedadesDispositivoTbl().setControladorAdminEnlace(ctrlCrearYAdminEnlace);
        
        SimulationBase.getInstance().setLinkAdminAbstractController(ctrlCrearYAdminEnlace);
        
        ctrlCreadorYAdministradorNodo.addGraphNodesView(IGU.getInstance().getGrGrupoDeDiseño());
        ctrlCreadorYAdministradorNodo.addGraphNodesView(IGU.getInstance().getPropiedadesDispositivoTbl());
        IGU.getInstance().getPropiedadesDispositivoTbl().setControladorAbstractoAdminNodo(ctrlCreadorYAdministradorNodo);
      //  executeAbstractController.stop();
    }

    public void save(boolean  thenClose) {
        serializador.OpenDialogToSave();
        if(thenClose)
        {
             System.exit(0);
        }
    }   

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException 
    {
        
        simulacionBase = SimulationBase.getInstance(); 
        simulacionBase.getSimulador().getEntities(); 
        objectOutputStream.defaultWriteObject();

    }
    public void load() {
        Main main = serializador.OpenDialogToLoad();
        if (main != null) 
        {
            loadControllers(main);

        }
    }
}
