package com.ag2.presentation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class ModalAG2window extends Stage {

    private Button btnAccept;
    private BorderPane brPnWindowsLayout;

    public ModalAG2window(String title) {
        super(StageStyle.UTILITY);
        setTitle(title);
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true);
        initStyle(StageStyle.UNDECORATED);
        brPnWindowsLayout = new BorderPane();
        brPnWindowsLayout.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(brPnWindowsLayout, 510, 400);//#FFEB8C
        setGradientEffect(scene);

        btnAccept = new Button("Aceptar");
        btnAccept.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                ModalAG2window.this.close();
            }
        });

        setScene(scene);
        configWindow();
    }

    private void setGradientEffect(Scene reportScene) {
        Stop[] stops = new Stop[] {
            new Stop(0.2, Color.LIGHTSTEELBLUE),
            new Stop(0.5, Color.WHITESMOKE),
            new Stop(0.8, Color.LIGHTSTEELBLUE)
        };
        LinearGradient linearGradient = new LinearGradient(0,0,1,0.25, true, CycleMethod.NO_CYCLE, stops);
        reportScene.setFill(linearGradient);
    }

    public BorderPane getBrPnWindowsLayout() {
        return brPnWindowsLayout;
    }

    public Button getBtnAccept() {
        return btnAccept;
    }

    public abstract void configWindow();

}