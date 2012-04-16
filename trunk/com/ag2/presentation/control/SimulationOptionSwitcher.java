/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;

import com.ag2.controller.SimulationOptionSwictherController;
import com.ag2.controller.SimulationOptionSwictherController.OptionSimulation;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author INFORCOL
 */
public class SimulationOptionSwitcher extends HBox
{
    private ToggleGroup toggleGroup;
    private ToggleButton tbAg2;
    private ToggleButton tbPhosphorus;

    public SimulationOptionSwitcher()
    {
        setPadding(new Insets(5, 5, 5, 5));
        setSpacing(5);
        setAlignment(Pos.CENTER);
        getStyleClass().add("barraDeHerramientas");
        toggleGroup = new ToggleGroup();
        tbAg2 = new ToggleButton("AG-2");
        tbPhosphorus = new ToggleButton("P");

        tbAg2.setToggleGroup(toggleGroup);
        tbPhosphorus.setToggleGroup(toggleGroup);

        tbPhosphorus.setSelected(true);
       getChildren().addAll(tbPhosphorus, tbAg2);


       tbPhosphorus.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0)
            {
                tbPhosphorus.setDisable(true);
                tbAg2.setDisable(false);
                SimulationOptionSwictherController.getInstance().setOptionSimulation( OptionSimulation.PHOSPHOSRUS );
            }
        });

        tbAg2.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0)
            {
                tbPhosphorus.setDisable(false);
                tbAg2.setDisable(true);
                SimulationOptionSwictherController.getInstance().setOptionSimulation( OptionSimulation.AG2 );
            }
        });
    }

}
