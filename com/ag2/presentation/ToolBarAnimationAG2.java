package com.ag2.presentation;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ToolBarAnimationAG2 {

    private VBox animationContainer;
    private Region regLight;
    private ParallelTransition childParalTransBegin;
    private ParallelTransition childParalTransEnd;
    private SequentialTransition parentSeqTrans;
    private CubicCurveTo cubicCurveTo;
    private ToolBar tobWindow;
    private double widthTitleAndButtonsContainers = 0;
    private double pathRouteWidth = 0;
    private double pathRouteHeight = 0;

    public ToolBarAnimationAG2(ToolBar tobWindow) {

        this.tobWindow = tobWindow;

        for (Node node : tobWindow.getItems()) {
            if (node.getId() != null && node.getId().equalsIgnoreCase("animation-container")) {
                animationContainer = (VBox) node;
            } else {
                widthTitleAndButtonsContainers += node.getBoundsInParent().getWidth();
            }
        }
        
        pathRouteWidth = animationContainer.getBoundsInParent().getWidth();
        pathRouteHeight = animationContainer.getBoundsInParent().getHeight();

        regLight = new Region();
        regLight.setId("animation-light");
        regLight.setPrefSize(10, 10);
        regLight.setMinSize(10, 10);
        regLight.setMaxSize(10, 10);
        
        regLight.setTranslateX(0-regLight.getPrefWidth()/2);
        regLight.setTranslateY(pathRouteHeight-1.5*regLight.getPrefHeight());
        animationContainer.getChildren().add(regLight);

        childParalTransBegin = new ParallelTransition();
        childParalTransEnd = new ParallelTransition();
        parentSeqTrans = new SequentialTransition();
        parentSeqTrans.setCycleCount(Timeline.INDEFINITE);
        parentSeqTrans.setAutoReverse(true);

//        Se configuran las transiciones q ejecuta al inicio
        RotateTransition rotateTransBegin = new RotateTransition(Duration.seconds(2), regLight);
        rotateTransBegin.setByAngle(180f);
        rotateTransBegin.setCycleCount(2);
        rotateTransBegin.setAutoReverse(true);

        ScaleTransition scaleTransBegin = new ScaleTransition(Duration.seconds(1), regLight);
        scaleTransBegin.setToX(2f);
        scaleTransBegin.setToY(2f);
        scaleTransBegin.setCycleCount(2);
        scaleTransBegin.setAutoReverse(true);

        childParalTransBegin.getChildren().addAll(rotateTransBegin, scaleTransBegin);

//        Luego la transicion de desplazamiento con curva

        Path path = new Path();
        path.setId("path-light");
        path.getElements().add(new MoveTo(5, pathRouteHeight-10));
        cubicCurveTo = new CubicCurveTo(pathRouteWidth/3,10 , 2*pathRouteWidth/3,40 , pathRouteWidth,10);
        path.getElements().add(cubicCurveTo);
//        path.setStroke(Color.AQUAMARINE);
//        path.getStrokeDashArray().setAll(5d, 5d);

        animationContainer.getChildren().add(path);

        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.seconds(2));
        pathTransition.setPath(path);
        pathTransition.setNode(regLight);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(1);

//        Al final la ultima transicion
        RotateTransition rotateTransEnd = new RotateTransition(Duration.seconds(2), regLight);
        rotateTransEnd.setByAngle(180f);
        rotateTransEnd.setCycleCount(2);
        rotateTransEnd.setAutoReverse(true);

        ScaleTransition scaleTransEnd = new ScaleTransition(Duration.seconds(1), regLight);
        scaleTransEnd.setToX(2f);
        scaleTransEnd.setToY(2f);
        scaleTransEnd.setCycleCount(2);
        scaleTransEnd.setAutoReverse(true);

        childParalTransEnd.getChildren().addAll(rotateTransEnd, scaleTransEnd);

        parentSeqTrans.getChildren().addAll(childParalTransBegin, pathTransition, childParalTransEnd);

        parentSeqTrans.setDelay(Duration.seconds(3));
        parentSeqTrans.play();
    }

    public void resize(Stage stage) {

        parentSeqTrans.stop();

        pathRouteWidth = stage.getWidth() - widthTitleAndButtonsContainers;

        cubicCurveTo.setX(pathRouteWidth-40);
        cubicCurveTo.setControlX1(pathRouteWidth / 3);
        cubicCurveTo.setControlX2(2 * pathRouteWidth / 3);

        parentSeqTrans.play();
    }
}