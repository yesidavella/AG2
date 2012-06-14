/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.design;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import org.apache.bcel.generic.LLOAD;

/**
 *
 * @author Frank
 */
public class GraphOCS {

    private Line line;
    private GraphNode graphNodeSource;
    private GraphNode graphNodeDestination;
    private GraphDesignGroup graphDesignGroup;
    private Label lblCountOCS;
    private int countOCS;
    private int countOCS_Inverted = 0;
    private Group group;
    int posStartX;
    int posStartY;
    int posEndX;
    int posEndY;

    public GraphOCS(final GraphNode graphNodeSource, final GraphNode graphNodeDestination, GraphDesignGroup graphDesignGroup, int countInstanceOCS) {

        group = new Group();
        line = new Line();
        countOCS = 1;
        lblCountOCS = new Label();
        this.graphNodeSource = graphNodeSource;
        this.graphNodeDestination = graphNodeDestination;
        this.graphDesignGroup = graphDesignGroup;
        int desfaseX = 24;
        int desfaseY = 34;

        DropShadow dropShadow = new DropShadow();
        dropShadow.setSpread(0.5);
        dropShadow.setWidth(10);
        dropShadow.setHeight(10);

        line.setEffect(dropShadow);


        line.setStroke(Color.web("#5B88E0"));
        dropShadow.setColor(Color.BLACK);



        posStartX = (int) graphNodeSource.getLayoutX() + desfaseX + (graphNodeSource.getWidth() / 2);
        posStartY = (int) graphNodeSource.getLayoutY() + desfaseY + (graphNodeSource.getHeight() / 2);

        posEndX = (int) graphNodeDestination.getLayoutX() + desfaseX + graphNodeSource.getWidth() / 2;
        posEndY = (int) graphNodeDestination.getLayoutY() + desfaseY + (graphNodeSource.getHeight() / 2);

        lblCountOCS.setScaleX(0.9);
        lblCountOCS.setScaleY(-.9);
        lblCountOCS.setStyle(" -fx-text-fill: red;-fx-font: bold 6pt 'Arial'; -fx-background-color:#FFFFFF");
        lblCountOCS.setVisible(false);
        DropShadow dropShadow1 = new DropShadow();
        lblCountOCS.setEffect(dropShadow1);
        
        lblCountOCS.setOnMouseEntered(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                lblCountOCS.setScaleX(1.7);
                lblCountOCS.setScaleY(-1.7);
            }
        });
         lblCountOCS.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                 lblCountOCS.setScaleX(0.9);
        lblCountOCS.setScaleY(-.9);
            }
        });
        
         lblCountOCS.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                lblCountOCS.setVisible(false);
            }
        });

        line.setStartX(posStartX);
        line.setStartY(posStartY);
        line.setEndX(posEndX);
        line.setEndY(posEndY);




        line.setStrokeWidth(3);



        group.getChildren().addAll(line);
        graphDesignGroup.getGroup().getChildren().addAll(group, lblCountOCS);

        group.toFront();
        graphNodeDestination.getGroup().toFront();
        graphNodeSource.getGroup().toFront();

        line.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                GraphOCS.this.showLabel(arg0.getX(), arg0.getY());
            }
        });
    }
    public void showLabel(double x, double y)
    {
          lblCountOCS.setVisible(true);
                lblCountOCS.setLayoutX(x - 10);
                lblCountOCS.setLayoutY(y - 10);
                lblCountOCS.setText(graphNodeSource.getName() + "-" + graphNodeDestination.getName() + " -> λSP: " + countOCS + "\n"
                        + graphNodeDestination.getName() + "-" + graphNodeSource.getName() + " -> λSP: " + countOCS_Inverted);
                lblCountOCS.toFront();
    }

    public void remove() {
//        System.out.println("Elminando linea  "+graphNodeSource.getName()+" -  "+graphNodeDestination.getName());
        graphDesignGroup.getGroup().getChildren().removeAll(group, lblCountOCS);
        graphDesignGroup.getLinesOCS().remove(group);
        graphDesignGroup.getLabelsOCS().remove(lblCountOCS);

    }

    public Group getGroup() {
        return group;
    }

    public void addInstanceOCS() {
        countOCS++;
    }

    public void addInstanceOCS_Inverted() {
        if (countOCS_Inverted == 0) {
            addDirection();
        }

        countOCS_Inverted++;


    }

    private void addDirection() {


        Line lineMiddle = new Line();
        lineMiddle.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                GraphOCS.this.showLabel(arg0.getX(), arg0.getY());
            }
        });
        lineMiddle.setStroke(Color.web("#000000"));
        lineMiddle.setStartX(posStartX);
        lineMiddle.setStartY(posStartY);
        lineMiddle.setEndX(posEndX);
        lineMiddle.setEndY(posEndY);
        line.setStrokeWidth(7);
        group.getChildren().addAll(lineMiddle);
    }

    public Label getLblCountOCS() {
        return lblCountOCS;
    }
    
}
