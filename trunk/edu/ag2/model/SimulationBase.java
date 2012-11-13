package edu.ag2.model;

import Grid.GridSimulation;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Interfaces.Switch;
import Grid.Utilities.HtmlWriter;
import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import edu.ag2.controller.OCSAdminController;
import edu.ag2.controller.ResultsAbstractController;
import edu.ag2.controller.ResultsChartAbstractController;
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

    private SimulationBase() {

        simulationInstance = new GridSimulation(Utils.ABS_PATH_CONFIG_AG2 + "ConfigInitAG2.cfg");
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
        //System.out.println("------------------------Stop en base");
//        HybridSwitchSender.ocsTearDownSend=false;
    }

    public void initNetwork() {
        simulationInstance.stopEvent = false;
        gridSimulatorModel.getRouting().clear();
        gridSimulatorModel.getPhysicTopology().clear();
//        gridSimulatorModel.getEstablishedCircuits().clear();
        route();

        ((OCSAdminController) OCSLinkAdminCtr).createOCS();
    }

    @Override
    public void run() {

        initEntities();
        simulationInstance.run();

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

 
}