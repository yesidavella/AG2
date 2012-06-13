/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.design;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
    private Group group;

    public GraphOCS(GraphNode graphNodeSource, GraphNode graphNodeDestination, GraphDesignGroup graphDesignGroup, int countInstanceOCS, boolean directionCreated) {

        group = new Group();
        line = new Line();
        countOCS = 1;
        lblCountOCS = new Label("λSP: " + countOCS);
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

        if (!directionCreated) 
        {
            line.setStroke(Color.RED);
            dropShadow.setColor(Color.BLACK);
        } else 
        {
            line.setStroke(Color.BLUE);
            dropShadow.setColor(Color.BLACK);
            desfaseX = 12;
            desfaseY = 24;
        }

        int posStartX = (int) graphNodeSource.getLayoutX() + desfaseX + (graphNodeSource.getWidth() / 2);
        int posStartY = (int) graphNodeSource.getLayoutY() + desfaseY + (graphNodeSource.getHeight() / 2);

        int posEndX = (int) graphNodeDestination.getLayoutX() + desfaseX + graphNodeSource.getWidth() / 2;
        int posEndY = (int) graphNodeDestination.getLayoutY() + desfaseY + (graphNodeSource.getHeight() / 2);

        lblCountOCS.setScaleX(1.3);
        lblCountOCS.setScaleY(-1.3);
        
        line.setStartX(posStartX);
        line.setStartY(posStartY);
        line.setEndX(posEndX);
        line.setEndY(posEndY);

        lblCountOCS.setLayoutX(posStartX + posStartX - posEndX);
        lblCountOCS.setLayoutY(posStartY + posStartY - posEndY);

        line.setStrokeWidth(2);

       
        
        group.getChildren().addAll(line, lblCountOCS);
        graphDesignGroup.getGroup().getChildren().add(group);
        
        group.toFront();
        graphNodeDestination.getGroup().toFront();
        graphNodeSource.getGroup().toFront();
    }

    public void remove() {
//        System.out.println("Elminando linea  "+graphNodeSource.getName()+" -  "+graphNodeDestination.getName());
        graphDesignGroup.getGroup().getChildren().remove(group);
        graphDesignGroup.getLinesOCS().remove(group);
    }

    public Group getGroup() {
        return group;
    }
    public void addInstanceOCS()
    {
        countOCS++;
          lblCountOCS.setText("λSP: " + countOCS);
    }
}
