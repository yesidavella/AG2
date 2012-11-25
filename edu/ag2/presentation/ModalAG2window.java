package edu.ag2.presentation;

import edu.ag2.util.Utils;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public abstract class ModalAG2window extends Stage {

    private Button btnAccept;
    private BorderPane brpWindowLayout;
    protected StackPane modalDimmer;

    public ModalAG2window(String title) {
        super(StageStyle.UTILITY);
        setTitle(title);
        initModality(Modality.APPLICATION_MODAL);
        setResizable(true);
        initStyle(StageStyle.UNDECORATED);
        brpWindowLayout = new BorderPane();
        brpWindowLayout.setPadding(new Insets(10, 10, 10, 10));

        Scene scene = new Scene(brpWindowLayout, 515, 400);//#FFEB8C
        setGradientEffect(scene);
        scene.getStylesheets().add( Main.class.getResource("ag2.css").toExternalForm());
        brpWindowLayout.getStyleClass().add("modal-AG2-window");

        btnAccept = new Button("Cerrar");
        btnAccept.getStyleClass().add("button-ag2");
        btnAccept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                hideModalMessage();
                ModalAG2window.this.close();
            }
        });

        setScene(scene);
        configWindow();
        modalDimmer = GUI.getInstance().getModalDimmer();
        showModalMessage();
    }

    /**
     * Show the given node as a floating dialog over the whole application, with
     * the rest of the application dimmed out and blocked from mouse events.
     *
     * @param message
     */
    public void showModalMessage() {
//        modalDimmer.getChildren().add(message);
        modalDimmer.setOpacity(0);
        modalDimmer.setVisible(true);
        modalDimmer.setCache(true);
        TimelineBuilder.create().keyFrames(
                new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        modalDimmer.setCache(false);
                    }
                },
                new KeyValue(modalDimmer.opacityProperty(), 1, Interpolator.EASE_BOTH))).build().play();
    }

    /**
     * Hide any modal message that is shown
     */
    public void hideModalMessage() {
        modalDimmer.setCache(true);
        TimelineBuilder.create().keyFrames(
                new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        modalDimmer.setCache(false);
                        modalDimmer.setVisible(false);
                        modalDimmer.getChildren().clear();
                    }
                },
                new KeyValue(modalDimmer.opacityProperty(), 0, Interpolator.EASE_BOTH))).build().play();
    }

    private void setGradientEffect(Scene reportScene) {
        Stop[] stops = new Stop[]{
            new Stop(0.2, Color.LIGHTSTEELBLUE),
            new Stop(0.5, Color.WHITESMOKE),
            new Stop(0.8, Color.LIGHTSTEELBLUE)
        };
        LinearGradient linearGradient = new LinearGradient(0, 0, 1, 0.25, true, CycleMethod.NO_CYCLE, stops);
        reportScene.setFill(linearGradient);
    }

    public BorderPane getBrPnWindowsLayout() {
        return brpWindowLayout;
    }

    public Button getBtnAccept() {
        return btnAccept;
    }

    public abstract void configWindow();
}