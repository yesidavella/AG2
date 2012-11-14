package com.ag2.presentation;

import Grid.Entity;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.config.serialization.UtilSerializator;
import com.ag2.controller.*;
import com.ag2.model.*;
import com.ag2.presentation.control.ResultsOCSController;
import com.ag2.presentation.design.GraphArc;
import com.ag2.presentation.design.GraphDesignGroup;
import com.ag2.presentation.design.GraphLink;
import com.ag2.presentation.design.GraphNode;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;
import netscape.javascript.JSObject;

public class Main extends Application implements Serializable {

    public static int countObject = 0;
    public static boolean IS_APPLET = false;
    private transient UtilSerializator utilSerializator;
    private transient LinkAdminAbstractController OCSlinkAdminCtrl;
    private transient GUI guiAG2;
    private transient ResultsOCSController resultsOCSController = new ResultsOCSController();
    private NodeAdminController nodeAdminController;
    private ExecuteController executeController;
    private NodeCreationModel nodeCreationModel;
    private LinkAdminAbstractController fiberLinkAdminACtrl;
    private GraphDesignGroup graphDesignGroup;
    private SimulationBase simulationBase = SimulationBase.getInstance();
    private ResultsController resultsController;
    private ResultsChartController resultsChartCtr;
    private JSObject webBrowser;
    private NotifyControllerOCS notifyControllerOCS;
    private HashMap<GraphNode, Entity> nodeMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer();
    private HashMap<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer();

    @Override
    public void start(final Stage stage) {

        stage.setTitle("Simulador de infraestructura de grillas opticas AG2");
        //FIXME: Pendiente multi-plataforma
        try {
            webBrowser = getHostServices().getWebContext();
            IS_APPLET = webBrowser != null;
        } catch (Exception e) {
            IS_APPLET = false;
        }

        GUI.setStage(stage);

        guiAG2 = GUI.getInstance();
        stage.setScene(guiAG2);
        guiAG2.setStage(stage);
        guiAG2.setMain(this);
        stage.show();
        graphDesignGroup = guiAG2.getGraphDesignGroup();

        initModelsAndControllers();
        guiAG2.initStateGUI();

        utilSerializator = new UtilSerializator(this, stage);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                int result = JOptionPane.showConfirmDialog(
                        null, "¿Desea guardar los cambios efectuados en la simulación?", "Simulador AG2", JOptionPane.YES_NO_CANCEL_OPTION);

                if (result == JOptionPane.NO_OPTION) {
                    System.exit(0);
                } else if (result == JOptionPane.YES_OPTION) {
                    save(true);
                }
                event.consume();
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
        resultsChartCtr = new ResultsChartController();

        guiAG2.getGraphDesignGroup().addNodeAdminAbstractControllers(nodeAdminController);

        nodeAdminController.addGraphNodesView(guiAG2.getGraphDesignGroup());
        nodeAdminController.addGraphNodesView(guiAG2.getEntityPropertyTb());
        guiAG2.getEntityPropertyTb().setControladorAbstractoAdminNodo(nodeAdminController);
        guiAG2.getExecutePane().setExecuteAbstractController(executeController);

        nodeCreationModel = new ClientCreationModel();
        nodeAdminController.addModel(nodeCreationModel);

        nodeCreationModel = new BrokerCreationModel();
        nodeAdminController.addModel(nodeCreationModel);

        nodeCreationModel = new ResourceCreationModel();
        nodeAdminController.addModel(nodeCreationModel);

        nodeCreationModel = new PCE_SwitchCreationModel();
        nodeAdminController.addModel(nodeCreationModel);

        nodeCreationModel = new HybridSwitchCreationModel();
        nodeAdminController.addModel(nodeCreationModel);

        fiberLinkAdminACtrl = new FiberAdminController();
        LinkCreationAbstractModel fiberCreationModel = new FiberCreationModel();
        fiberLinkAdminACtrl.addModel(fiberCreationModel);
        SimulationBase.getInstance().setFiberLinkAdminController(fiberLinkAdminACtrl);

        fiberLinkAdminACtrl.setLinkView(guiAG2.getEntityPropertyTb());
        guiAG2.getEntityPropertyTb().setLinkAdminAbstractController(fiberLinkAdminACtrl);
        guiAG2.getGraphDesignGroup().addLinkAdminAbstractControllers(fiberLinkAdminACtrl);

        OCSlinkAdminCtrl = new OCSAdminController();
        LinkCreationAbstractModel OCSCreationAbstractModel = new OCSCreationModel();
        OCSlinkAdminCtrl.addModel(OCSCreationAbstractModel);
        guiAG2.getGraphDesignGroup().addLinkAdminAbstractControllers(OCSlinkAdminCtrl);
        SimulationBase.getInstance().setOCSLinkAdminCtr(OCSlinkAdminCtrl);
        //Registro el controlador de creacion de ocs en el de creacion de fibra
        //para q cuando cree un enlace de fibra entre switches creo un ocs tambien
        ((FiberAdminController) fiberLinkAdminACtrl).addOCSAdminController((OCSAdminController) OCSlinkAdminCtrl);

        resultsController.setViewResultsPhosphorus(guiAG2.getPhosphosrusResults());
        resultsChartCtr.setViewResultsClientChart(guiAG2.getChartsResultClient());
        resultsChartCtr.setViewResultsResourceChart(guiAG2.getChartsResultResource());
        resultsChartCtr.setViewResultsBrokerChart(guiAG2.getChartsResultsBroker());
        resultsChartCtr.setViewResultsSwitchChart(guiAG2.getChartsResultsSwitch());

        SimulationBase.getInstance().setResultsAbstractController(resultsController);
        guiAG2.getTbwSimulationProperties().setItems(PropertyPhosphorusTypeEnum.getData(executeController));
        simulationBase.setResultsChartAbstractController(resultsChartCtr);

        guiAG2.getResultsOCS().setResultsOCSController(resultsOCSController);

        notifyControllerOCS = new NotifyControllerOCS();
        notifyControllerOCS.setGraphDesignGroup(guiAG2.getGraphDesignGroup());

    }

    public ResultsController getResultsController() {
        return resultsController;
    }

    public NodeAdminController getNodeAdminController() {
        return nodeAdminController;
    }

    public LinkAdminAbstractController getLinkAdminAbstractController() {
        return fiberLinkAdminACtrl;
    }

    public LinkAdminAbstractController getLinkAdminOCSCtl() {
        return OCSlinkAdminCtrl;
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

    public void loadFileBaseSimulation() {
        Main main = utilSerializator.loadFileBaseSimulation();
        if (main != null) {
            loadControllers(main);
        }
    }

    private void loadControllers(Main main) {

        resultsOCSController = new ResultsOCSController();
        guiAG2.getResultsOCS().setResultsOCSController(resultsOCSController);

        simulationBase = main.getSimulationBase();  ///SimulacionBase.getInstance();
        SimulationBase.loadInstance(simulationBase);
        graphDesignGroup = main.getGraphDesignGroup();

        guiAG2.loadGraphDesignGroup(graphDesignGroup);

        nodeAdminController = main.getNodeAdminController();
        fiberLinkAdminACtrl = main.getLinkAdminAbstractController();
//        linkAdminOCSCtl = main.getLinkAdminOCSCtl();

        executeController = main.getExecuteAbstractController();
        resultsController = main.getResultsController();
        nodeCreationModel = main.getNodeCreationModel();
        resultsChartCtr = main.getResultsChartController();
        nodeMatchCoupleObjectContainer = main.getNodeMatchCoupleObjectContainer();
        linkMatchCoupleObjectContainer = main.getLinkMatchCoupleObjectContainer();

        MatchCoupleObjectContainer.setNodeMatchCoupleObjectContainer(nodeMatchCoupleObjectContainer);
        MatchCoupleObjectContainer.setLinkMatchCoupleObjectContainer(linkMatchCoupleObjectContainer);

        guiAG2.loadGraphDesignGroup(graphDesignGroup);
        notifyControllerOCS.setGraphDesignGroup(graphDesignGroup);

        guiAG2.getExecutePane().setExecuteAbstractController(executeController);
        resultsController.setViewResultsPhosphorus(guiAG2.getPhosphosrusResults());

        fiberLinkAdminACtrl.setLinkView(guiAG2.getEntityPropertyTb());
        guiAG2.getEntityPropertyTb().setLinkAdminAbstractController(fiberLinkAdminACtrl);

        SimulationBase.getInstance().setFiberLinkAdminController(fiberLinkAdminACtrl);

        nodeAdminController.addGraphNodesView(guiAG2.getGraphDesignGroup());
        nodeAdminController.addGraphNodesView(guiAG2.getEntityPropertyTb());
        guiAG2.getEntityPropertyTb().setControladorAbstractoAdminNodo(nodeAdminController);

        resultsChartCtr.setViewResultsClientChart(guiAG2.getChartsResultClient());
        resultsChartCtr.setViewResultsResourceChart(guiAG2.getChartsResultResource());
        resultsChartCtr.setViewResultsBrokerChart(guiAG2.getChartsResultsBroker());
        resultsChartCtr.setViewResultsSwitchChart(guiAG2.getChartsResultsSwitch());
        //  executeAbstractController.stop();
    }

    public void save(boolean thenClose) {
        utilSerializator.OpenDialogToSave();
        if (thenClose) {
            System.exit(0);
        }
    }

    public void load() {
        Main main = utilSerializator.OpenDialogToLoad();
        if (main != null) {
            loadControllers(main);
        }
    }

    private void writeObject(ObjectOutputStream stream) {
        try {
            simulationBase = SimulationBase.getInstance();
            simulationBase.getGridSimulatorModel().getEntities();
            stream.defaultWriteObject();
            Main.countObject++;
            //System.out.println("Writing: " + Main.countObject + "  " + this.getClass().getCanonicalName());
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultsChartController getResultsChartController() {
        return resultsChartCtr;
    }

    public HashMap<GraphLink, PhosphorusLinkModel> getLinkMatchCoupleObjectContainer() {
        return linkMatchCoupleObjectContainer;
    }

    public HashMap<GraphNode, Entity> getNodeMatchCoupleObjectContainer() {
        return nodeMatchCoupleObjectContainer;
    }
}