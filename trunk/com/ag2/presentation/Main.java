package com.ag2.presentation;

import com.ag2.model.BrokerCreationModel;
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
import com.ag2.controller.NodeAdminController;
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

    private transient UtilSerializator utilSerializator;
    private NodeAdminController nodeAdminController;
    private ExecuteController executeController;
    private NodeCreationModel nodeCreationModel;
    private LinkAdminAbstractController linkAdminAbstractController;
    private GraphDesignGroup graphDesignGroup;
    private SimulationBase simulationBase =  SimulationBase.getInstance();
    private ResultsController resultsController;


    @Override
    public void start(final Stage stage) {

        stage.setTitle("Modelo AG2- Simulador Grafico");
        stage.setScene(GUI.getInstance());
        GUI.getInstance().setStage(stage);
        GUI.getInstance().setMain(this);
        stage.show();
        graphDesignGroup = GUI.getInstance().getGraphDesignGroup();

        initModelsAndControllers();
        GUI.getInstance().initStateIGU();
        utilSerializator = new UtilSerializator(this, stage);
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


    public SimulationBase getSimulationBase() {
        return simulationBase;
    }


    public static void main(String[] args) {
        Application.launch(args);
    }

    private void initModelsAndControllers() {

        //Controladores y Modelos
        nodeAdminController = new NodeAdminController();
        executeController = new ExecuteController();
        resultsController = new ResultsController();

        GUI.getInstance().getGraphDesignGroup().addNodeAdminAbstractControllers(nodeAdminController);

        nodeAdminController.addGraphNodesView(GUI.getInstance().getGraphDesignGroup());
        nodeAdminController.addGraphNodesView(GUI.getInstance().getEntityPropertyTb());
        GUI.getInstance().getEntityPropertyTb().setControladorAbstractoAdminNodo(nodeAdminController);
        GUI.getInstance().getExecutePane().setExecuteAbstractController(executeController);


        nodeCreationModel = new ModeloCrearCliente();
        nodeAdminController.addModel(nodeCreationModel);

        nodeCreationModel = new BrokerCreationModel();
        nodeAdminController.addModel(nodeCreationModel);

        nodeCreationModel = new ModeloCrearNodoDeRecurso();
        nodeAdminController.addModel(nodeCreationModel);

        nodeCreationModel = new ModeloCrearEnrutadorRafaga();
        nodeAdminController.addModel(nodeCreationModel);

        nodeCreationModel = new ModeloCrearEnrutadorOptico();
        nodeAdminController.addModel(nodeCreationModel);

        nodeCreationModel = new ModeloCrearEnrutadorHibrido();
        nodeAdminController.addModel(nodeCreationModel);

        linkAdminAbstractController = new LinkAdminController();
        LinkCreationAbstractModel modeloCrearEnlace = new ModeloCrearEnlace();
        linkAdminAbstractController.addModel(modeloCrearEnlace);

        linkAdminAbstractController.setLinkView(GUI.getInstance().getEntityPropertyTb());
        GUI.getInstance().getEntityPropertyTb().setLinkAdminAbstractController(linkAdminAbstractController);
        GUI.getInstance().getGraphDesignGroup().addLinkAdminAbstractControllers(linkAdminAbstractController);


        resultsController.setViewResultsPhosphorus(GUI.getInstance().getPhosphosrusResults());
        SimulationBase.getInstance().setResultsAbstractController(resultsController);

        GUI.getInstance().getTbwSimulationProperties().setItems(PropertyPhosphorusTypeEnum.getData(executeController));

    }

    public ResultsController getResultsController() {
        return resultsController;
    }

    public NodeAdminController getNodeAdminController() {
        return nodeAdminController;
    }

    public LinkAdminAbstractController getLinkAdminAbstractController() {
        return linkAdminAbstractController;
    }

    public ExecuteController getExecuteAbstractController() {
        return executeController;
    }

    public NodeCreationModel getNodeCreationModel() {
        return nodeCreationModel;
    }

    public UtilSerializator getUtilSerializator() {
        return utilSerializator;
    }

    public GraphDesignGroup getGraphDesignGroup() {
        return graphDesignGroup;
    }
    public void loadFileBaseSimulation()
    {
        Main main = utilSerializator.loadFileBaseSimulation();
        if (main != null)
        {
            loadControllers(main);
        }
    }

    private void loadControllers(Main main)
    {
        simulationBase =   main.getSimulationBase();  ///SimulacionBase.getInstance();
        SimulationBase.loadInstance(simulationBase);
        graphDesignGroup = main.getGraphDesignGroup();

        GUI.getInstance().loadGraphDesignGroup(graphDesignGroup);

        nodeAdminController = main.getNodeAdminController();
        linkAdminAbstractController = main.getLinkAdminAbstractController();
        executeController= main.getExecuteAbstractController();
        resultsController = main.getResultsController();

        nodeCreationModel = main.getNodeCreationModel();


        GUI.getInstance().getExecutePane().setExecuteAbstractController(executeController);
        resultsController.setViewResultsPhosphorus(GUI.getInstance().getPhosphosrusResults());

        linkAdminAbstractController.setLinkView(GUI.getInstance().getEntityPropertyTb());
        GUI.getInstance().getEntityPropertyTb().setLinkAdminAbstractController(linkAdminAbstractController);

        SimulationBase.getInstance().setLinkAdminAbstractController(linkAdminAbstractController);

        nodeAdminController.addGraphNodesView(GUI.getInstance().getGraphDesignGroup());
        nodeAdminController.addGraphNodesView(GUI.getInstance().getEntityPropertyTb());
        GUI.getInstance().getEntityPropertyTb().setControladorAbstractoAdminNodo(nodeAdminController);
      //  executeAbstractController.stop();
    }

    public void save(boolean  thenClose) {
        utilSerializator.OpenDialogToSave();
        if(thenClose)
        {
             System.exit(0);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException
    {

        simulationBase = SimulationBase.getInstance();
        simulationBase.getSimulador().getEntities();
        objectOutputStream.defaultWriteObject();

    }
    public void load() {
        Main main = utilSerializator.OpenDialogToLoad();
        if (main != null)
        {
            loadControllers(main);

        }
    }
}