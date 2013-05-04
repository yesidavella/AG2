package edu.ag2.presentation;

import com.sun.java.accessibility.util.GUIInitializedListener;
import edu.ag2.controller.ExecuteAbstractController;
import edu.ag2.model.SimulationBase;
import edu.ag2.presentation.control.PhosphosrusHTMLResults;
import edu.ag2.presentation.control.PhosphosrusResults;
import edu.ag2.presentation.control.SimulationOptionSwitcher;
import edu.ag2.presentation.control.ToggleButtonAg2;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

public class ExecutePane extends TilePane implements ExecuteView, Runnable {

    private ToggleButtonAg2 btnRun;
    private ToggleButtonAg2 btnStop;
    private ToggleGroup tgRun = new ToggleGroup();
    private PhosphosrusHTMLResults phosphosrusHTMLResults;
    private PhosphosrusResults phosphosrusResults;
    private ExecuteAbstractController executeController;
    private SimulationOptionSwitcher simulationOptionSwitcher;

    public ExecutePane() {



        btnRun = new ToggleButtonAg2(ActionTypeEmun.RUN) {
            @Override
            public void setGraphDesignGroup(final Group group) {
                setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent mouEvent) {

                      
                        
                        

                       
//                          Platform.runLater(ExecutePane.this);
                          ExecutePane.this.playAll();



                    }
                });
            }
        };


        btnStop = new ToggleButtonAg2(ActionTypeEmun.STOP) {
            @Override
            public void setGraphDesignGroup(final Group grGrupoDeDiseño) {
                setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent mouEvent) {

                        //System.out.println("################    STOP  ################");
                        GUI.getInstance().stop();
                        enable();
                        executeController.stop();

                    }
                });
            }
        };
        btnStop.setDisable(true);

        getStyleClass().add("bg-general-container");
        setPadding(new Insets(10, 6, 10, 6));
        setHgap(4);
        setPrefColumns(2);
        setPrefRows(1);

        btnRun.setTooltip(new Tooltip("Ejecutar simulaci\u00d3n"));
        btnRun.setToggleGroup(tgRun);

        btnStop.setTooltip(new Tooltip("Parar simulaci\u00d3n"));
        btnStop.setToggleGroup(tgRun);
        btnStop.setSelected(true);
        getChildren().addAll(btnRun, btnStop);
    }

    public void playAll() {

        btnRun.setSelected(true);
        if (executeController != null) {

            //System.out.println("################    PLAY  ################");

            if (phosphosrusHTMLResults != null) {
                phosphosrusHTMLResults.lookToNextExecution();
            }
            if (phosphosrusResults != null) {
                phosphosrusResults.lookToNextExecution();
            }

            executeController.initNetwork();
            simulationOptionSwitcher.loadSimulationOptionBeforeRun();

            if (executeController.isWellFormedNetwork()) {


                if (GUI.getInstance().getGraphDesignGroup().getSelectable() != null) {
                    GUI.getInstance().getGraphDesignGroup().getSelectable().select(false);
                    GUI.getInstance().getGraphDesignGroup().setSelectable(null);
                }

                GUI.getInstance().getEntityPropertyTable().clearData();
                GUI.getInstance().play();

                btnRun.setDisable(true);
                btnStop.setDisable(false);
                executeController.run();
            } else {

                btnStop.setSelected(true);
            }
        }
    }

    public void setExecuteAbstractController(ExecuteAbstractController executeAbstractController) {
        this.executeController = executeAbstractController;
        executeAbstractController.setExecuteView(this);
    }

    public void setPhosphosrusHTMLResults(PhosphosrusHTMLResults phosphosrusHTMLResults) {
        this.phosphosrusHTMLResults = phosphosrusHTMLResults;
    }

    public void setPhosphosrusResults(PhosphosrusResults phosphosrusResults) {
        this.phosphosrusResults = phosphosrusResults;
    }

    public void enable() {
        btnStop.setSelected(true);
        btnRun.setDisable(false);
        btnStop.setDisable(true);

    }

    public void setGroup(Group group) {
        btnRun.setGraphDesignGroup(group);
        btnStop.setGraphDesignGroup(group);
    }

    public void setSimulationOptionSwitcher(SimulationOptionSwitcher simulationOptionSwitcher) {
        this.simulationOptionSwitcher = simulationOptionSwitcher;
    }

    @Override
    public void run() {

        
                
        try {
           
                
            for (int i = 0; i < 3; i++) {

                while (SimulationBase.running) 
                {
                    System.out.println("Esperando..");
                    Thread.sleep(10000);

                }

              

                        System.out.println("RUN playALL..");
                        Thread.sleep(4000);
                playAll();
                Thread.sleep(4000);


            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ExecutePane.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}