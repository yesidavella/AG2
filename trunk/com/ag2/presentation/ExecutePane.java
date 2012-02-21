package com.ag2.presentation;

import com.ag2.controller.ExecuteAbstractController;
import com.ag2.presentation.control.ToggleButtonAg2;
import com.ag2.presentation.control.PhosphosrusHTMLResults;
import com.ag2.presentation.control.PhosphosrusResults;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

public class ExecutePane extends TilePane implements ExecuteView {

    private ToggleButtonAg2 btnRun;
    private ToggleButtonAg2 btnStop;
    private ToggleGroup tgRun = new ToggleGroup();
    private PhosphosrusHTMLResults phosphosrusHTMLResults;
    private PhosphosrusResults phosphosrusResults;
    private ExecuteAbstractController executeController;

    public ExecutePane() {

        btnRun = new ToggleButtonAg2(ActionTypeEmun.RUN) {

            @Override
            public void setGraphDesignGroup(final Group group) {
                setOnMouseClicked(new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent mouEvent) {

                        ToggleButtonAg2 toggleButtonAg2 = (ToggleButtonAg2) mouEvent.getSource();

                        if (executeController != null) {
                            if (phosphosrusHTMLResults != null) {
                                phosphosrusHTMLResults.lookToNextExecution();
                            }
                            if (phosphosrusResults != null) {
                                phosphosrusResults.looktToNextExecution();
                            }

                            executeController.initNetwork();

                            if (executeController.isWellFormedNetwork()) {
                                if( GUI.getInstance().getGraphDesignGroup().getSelectable()!=null)
                                {    
                                    GUI.getInstance().getGraphDesignGroup().getSelectable().select(false);
                                    GUI.getInstance().getGraphDesignGroup().setSelectable(null);
                                }
                                
                                GUI.getInstance().getEntityPropertyTable().clearData();
                                GUI.getInstance().disable();
                                toggleButtonAg2.setSelected(true);
                                executeController.run();
                            } else {
                                btnStop.setSelected(true);
                            }
                        }
                    }
                });
            }
        };

        btnStop = new ToggleButtonAg2(ActionTypeEmun.STOP) {

            @Override
            public void setGraphDesignGroup(final Group grGrupoDeDiseño) {
                setOnMouseClicked(new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent mouEvent) {

                        ToggleButtonAg2 toggleButtonAg2 = (ToggleButtonAg2) mouEvent.getSource();
                        GUI.getInstance().enable();
                        enable();
                        executeController.stop();
                    }
                });
            }
        };

        getStyleClass().add("barraDeHerramientas");
        setPadding(new Insets(10, 10, 10, 10));
        setHgap(4);
        setPrefColumns(2);

        Tooltip tTipBtnEjecutar = new Tooltip("Ejecutar simulación");
        btnRun.setTooltip(tTipBtnEjecutar);
        btnRun.setToggleGroup(tgRun);

        btnRun.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
            }
        });

        Tooltip tTipBtnParar = new Tooltip("Parar simulación");

        btnStop.setTooltip(tTipBtnParar);
        btnStop.setToggleGroup(tgRun);
        btnStop.setSelected(true);
        getChildren().addAll(btnRun, btnStop);
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

    }

    public void setGroup(Group group) {
        btnRun.setGraphDesignGroup(group);
        btnStop.setGraphDesignGroup(group);
    }
}
