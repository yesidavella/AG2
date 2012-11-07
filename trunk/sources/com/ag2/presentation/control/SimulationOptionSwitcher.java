package com.ag2.presentation.control;

import com.ag2.controller.SimulationOptionSwictherController;
import com.ag2.controller.SimulationOptionSwictherController.OptionSimulation;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SimulationOptionSwitcher extends VBox {

    private ToggleGroup toggleGroup;
    private RadioButton rbAg2;
    private RadioButton rbPhosphorus1;
    private RadioButton rbPhosphorus2;

    public SimulationOptionSwitcher() {
        setPadding(new Insets(5, 5, 5, 5));
        setSpacing(5);
        setAlignment(Pos.CENTER);
        getStyleClass().add("bg-general-container");
        toggleGroup = new ToggleGroup();
        rbAg2 = new RadioButton("AG2");
        rbPhosphorus1 = new RadioButton("Ph-1");
        rbPhosphorus2 = new RadioButton("Ph-2");

        rbAg2.setToggleGroup(toggleGroup);
        rbPhosphorus1.setToggleGroup(toggleGroup);
        rbPhosphorus2.setToggleGroup(toggleGroup);

        rbAg2.setTooltip(new Tooltip("Ejecuci\u00d3n con algoritmos AG2"));
        rbPhosphorus1.setTooltip(new Tooltip("Ejecui\u00d3n con algoritmos Phosphorus"));
        rbPhosphorus2.setTooltip(new Tooltip("Ejecui\u00d3n con algoritmos \u00d3ptimos Phosphorus"));

        rbAg2.setSelected(true);
        getChildren().addAll(rbPhosphorus1, rbPhosphorus2, rbAg2);

        rbPhosphorus1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
//                SimulationOptionSwictherController.getInstance().setOptionSimulation(OptionSimulation.PHOSPHORUS);
            }
        });

        rbAg2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
//                SimulationOptionSwictherController.getInstance().setOptionSimulation(OptionSimulation.AG2);
            }
        });

        rbPhosphorus2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
//                SimulationOptionSwictherController.getInstance().setOptionSimulation(OptionSimulation.PHOSPHORUS_OPTIMIZE);
            }
        });
    }

    public void loadSimulationOptionBeforeRun() {
        if (rbAg2.isSelected()) {
            SimulationOptionSwictherController.getInstance().setOptionSimulation(OptionSimulation.AG2);
            //System.out.println("################    VIA AG2   ################");
        } else if (rbPhosphorus1.isSelected()) {
            SimulationOptionSwictherController.getInstance().setOptionSimulation(OptionSimulation.PHOSPHORUS);
            //System.out.println("################    VIA PHOSPHORUS   ################");
        } else if (rbPhosphorus2.isSelected()) {
            SimulationOptionSwictherController.getInstance().setOptionSimulation(OptionSimulation.PHOSPHORUS_OPTIMIZE);
            //System.out.println("################   VIA PHOSPHORUS_OPTIMIZE  ################");
        }
    }
}