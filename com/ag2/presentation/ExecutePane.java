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

    private ToggleButtonAg2 btnEjecutar;
    private ToggleButtonAg2 btnParar;
    private ToggleGroup tgEjecucion = new ToggleGroup();
    private PhosphosrusHTMLResults resultadosPhosphorousHTML;
    PhosphosrusResults resultadosPhosphorus;
    ExecuteAbstractController executeController;

    public ExecutePane() {

        btnEjecutar = new ToggleButtonAg2(ActionTypeEmun.RUN) {

            @Override
            public void setGraphDesignGroup(final Group grGrupoDeDiseño) {
                setOnMouseClicked(new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent mouEvent) {System.out.println ("Hola");

                        ToggleButtonAg2 btnEjecutar = (ToggleButtonAg2) mouEvent.getSource();

                        if (executeController != null) {
                            if (resultadosPhosphorousHTML != null) {
                                resultadosPhosphorousHTML.lookToNextExecution();
                            }
                            if (resultadosPhosphorus != null) {
                                resultadosPhosphorus.looktToNextExecution();
                            }

                            executeController.initNetwork();

                            if (executeController.isWellFormedNetwork()) {
                                IGU.getInstance().getGrGrupoDeDiseño().getSelectable().select(false);
                                IGU.getInstance().getGrGrupoDeDiseño().setSelectable(null);
                                IGU.getInstance().getTbDeviceProperties().clearData();
                                IGU.getInstance().deshabilitar();
                                btnEjecutar.setSelected(true);
                                executeController.run();
                            } else {
                                btnParar.setSelected(true);
                            }
                        }
                    }
                });
            }
        };

        btnParar = new ToggleButtonAg2(ActionTypeEmun.STOP) {

            @Override
            public void setGraphDesignGroup(final Group grGrupoDeDiseño) {
                setOnMouseClicked(new EventHandler<MouseEvent>() {

                    public void handle(MouseEvent mouEvent) {

                        ToggleButtonAg2 btnParar = (ToggleButtonAg2) mouEvent.getSource();
                        IGU.getInstance().habilitar();
                        habilitar();
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
        btnEjecutar.setTooltip(tTipBtnEjecutar);
        btnEjecutar.setToggleGroup(tgEjecucion);

        btnEjecutar.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
            }
        });

        Tooltip tTipBtnParar = new Tooltip("Parar simulación");

        btnParar.setTooltip(tTipBtnParar);
        btnParar.setToggleGroup(tgEjecucion);
        btnParar.setSelected(true);
        getChildren().addAll(btnEjecutar, btnParar);
    }

    public void setExecuteAbstractController(ExecuteAbstractController executeAbstractController) {
        this.executeController = executeAbstractController;
        executeAbstractController.setExecuteView(this);
    }

    public void setResultadosPhosphorousHTML(PhosphosrusHTMLResults resultadosPhosphorousHTML) {
        this.resultadosPhosphorousHTML = resultadosPhosphorousHTML;
    }

    public void setResultadosPhosphorus(PhosphosrusResults resultadosPhosphorus) {
        this.resultadosPhosphorus = resultadosPhosphorus;
    }

    public void habilitar() {
        btnParar.setSelected(true);

    }

    public void setGroup(Group group) {
        btnEjecutar.setGraphDesignGroup(group);
        btnParar.setGraphDesignGroup(group);






    }
}
