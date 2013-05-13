package edu.ag2.model;

import Grid.GridSimulation;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Interfaces.Switch;
import Grid.Nodes.Coeficiente;
import Grid.Nodes.PCE;
import Grid.Routing.Routing;
import Grid.Utilities.HtmlWriter;
import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import edu.ag2.controller.OCSAdminController;
import edu.ag2.controller.ResultsAbstractController;
import edu.ag2.controller.ResultsChartAbstractController;
import edu.ag2.presentation.GUI;
import edu.ag2.presentation.Main;
import edu.ag2.presentation.design.GraphArc;
import edu.ag2.util.Utils;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import simbase.SimBaseEntity;
import simbase.SimulationInstance;

public class SimulationBase implements Runnable, Serializable {

    private static SimulationBase simulationBase;
    private GridSimulatorModel gridSimulatorModel;
    private SimulationInstance simulationInstance;
    private OutputterModel outputterModel;
    private NodeAdminAbstractController nodeAdminCtr;
    private LinkAdminAbstractController fiberLinkAdminCtr;
    private LinkAdminAbstractController OCSLinkAdminCtr;
    private ResultsAbstractController resultsAbstractController;
    private ResultsChartAbstractController resultsChartAbstractController;
    private String id;
    private double runTime;
    public static boolean running = false;

    private SimulationBase() {

        simulationInstance = new GridSimulation("ConfigInitAG2.cfg");
        gridSimulatorModel = new GridSimulatorModel();
        simulationInstance.setSimulator(gridSimulatorModel);
        id = new Date().toString();
    }

    public static SimulationBase getInstance() {
        if (simulationBase == null) {
            simulationBase = new SimulationBase();
        }
        return simulationBase;
    }

    public String getId() {
        return id;
    }

    public static void loadInstance(SimulationBase simulationBase) {
        SimulationBase.simulationBase = simulationBase;
    }

    public ResultsAbstractController getResultsAbstractController() {
        return resultsAbstractController;
    }

    public void setNodeAdminController(NodeAdminAbstractController nodeAdminAbstractController) {
        this.nodeAdminCtr = nodeAdminAbstractController;
    }

    public void setOutputterModel(OutputterModel outputterModel) {
        this.outputterModel = outputterModel;
    }

    public void setResultsAbstractController(ResultsAbstractController resultsAbstractController) {
        this.resultsAbstractController = resultsAbstractController;
        outputterModel.setResultsAbstractController(resultsAbstractController);
        gridSimulatorModel.setViewResultsPhosphorus(resultsAbstractController);
    }

    private void route() {
        gridSimulatorModel.route();
    }

    private void initEntities() {
        gridSimulatorModel.initEntities();
    }

    public void stop() {
        simulationInstance.stopEvent = true;
    }

    public void reload() {

        runTime = simulationBase.getSimulationInstance().getSimulator().getMasterClock().getTime();

        simulationBase = new SimulationBase();
        simulationBase.runTime = runTime;
        OutputterModel outputterModelNew = new OutputterModel(simulationBase.getGridSimulatorModel());

        simulationBase.setResultsAbstractController(resultsAbstractController);
        simulationBase.setOutputterModel(outputterModelNew);
        simulationBase.setNodeAdminController(nodeAdminCtr);
        simulationBase.setFiberLinkAdminController(fiberLinkAdminCtr);
        simulationBase.setOCSLinkAdminCtr(OCSLinkAdminCtr);
        simulationBase.setResultsChartAbstractController(resultsChartAbstractController);

        nodeAdminCtr.reCreatePhosphorousNodes();
        fiberLinkAdminCtr.reCreatePhosphorousLinks();
        HtmlWriter.getInstance().incrementFolderCount();

        running = false;
        System.out.println("RELOAD");
    }

    public void initNetwork() {
        simulationInstance.stopEvent = false;
        gridSimulatorModel.getRouting().clear();
        gridSimulatorModel.getPhysicTopology().clear();

        route();
    }

    @Override
    public void run() {

        running = true;
        System.out.println("SimulationBase-Init.Run");
        initEntities();
        ((OCSAdminController) OCSLinkAdminCtr).createOCS();

        if (GUI.reEjecutarAutonomamente) {
            setParametrosReEjecucionAutonoma();
        }

        //FIXME: Ojo solo para efectos de re_ejecucion sin ejecutar
        //simulationInstance.run();

        for (SimBaseEntity entity : gridSimulatorModel.getEntities()) {
            if (entity instanceof ClientNode) {
                outputterModel.printClient((ClientNode) entity);
            } else if (entity instanceof Switch) {
                outputterModel.printSwitch((Switch) entity);
            } else if (entity instanceof ResourceNode) {
                outputterModel.printResource((ResourceNode) entity);
            } else if (entity instanceof ServiceNode) {
                outputterModel.printBroker((ServiceNode) entity);
            }
        }
        reload();
    }

    public SimulationInstance getSimulationInstance() {
        return simulationInstance;
    }

    public GridSimulator getGridSimulatorModel() {
        return gridSimulatorModel;
    }

    public void setFiberLinkAdminController(LinkAdminAbstractController linkAdminController) {
        this.fiberLinkAdminCtr = linkAdminController;
    }

    public LinkAdminAbstractController getOCSLinkAdminCtr() {
        return OCSLinkAdminCtr;
    }

    public void setOCSLinkAdminCtr(LinkAdminAbstractController linkAdminOCSCtr) {
        this.OCSLinkAdminCtr = linkAdminOCSCtr;
    }

    public OutputterModel getOutputterModel() {
        return outputterModel;
    }

    public void setResultsChartAbstractController(ResultsChartAbstractController resultsChartAbstractController) {
        this.resultsChartAbstractController = resultsChartAbstractController;
        outputterModel.setChartAbstractController(resultsChartAbstractController);
    }

    private void writeObject(ObjectOutputStream stream) {
        try {
            stream.defaultWriteObject();
            Main.countObject++;
            //System.out.println("Writing: " + Main.countObject + "  " + this.getClass().getCanonicalName());
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public double getRunTime() {
        return runTime;
    }

    private void setParametrosReEjecucionAutonoma() {
        if (GUI.Cx == null) {
            GUI.Cx = new Coeficiente(0, 3, 1);
        }

        if (GUI.Cfindλ == null) {
            GUI.Cfindλ = new Coeficiente(0, 2, 1);
        }

        if (GUI.Callocate == null) {
            GUI.Callocate = new Coeficiente(0, 1, 1);
        }

        if (GUI.getInstance().Cx != null && GUI.getInstance().Cfindλ != null && GUI.getInstance().Callocate != null) {
            System.out.println("Cx NO es null... GUI.Cx:" + GUI.Cx.getValor());
            for (SimBaseEntity pceNode : SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(PCE.class)) {
                ((PCE) pceNode).getMultiCostMarkovAnalyzer().setCx(GUI.getInstance().Cx.getValor());
                ((PCE) pceNode).getMultiCostMarkovAnalyzer().setCfind(GUI.getInstance().Cfindλ.getValor());
                ((PCE) pceNode).getMultiCostMarkovAnalyzer().setCallocate(GUI.getInstance().Callocate.getValor());
            }
        }
    }
}