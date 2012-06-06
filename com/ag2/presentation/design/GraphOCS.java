/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.design;

import java.security.acl.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Frank
 */
public class GraphOCS {

    private Line line;
    private GraphNode graphNodeSource;
    private GraphNode graphNodeDestination;
    private GraphDesignGroup graphDesignGroup;
    public GraphOCS(GraphNode graphNodeSource, GraphNode graphNodeDestination, GraphDesignGroup graphDesignGroup, int countInstanceOCS) {

        line = new Line();
        this.graphNodeSource = graphNodeSource;
        this.graphNodeDestination = graphNodeDestination;
        this.graphDesignGroup = graphDesignGroup; 
        int desfaseX =18;
        int desfaseY =30;
        line.setStartX(graphNodeSource.getLayoutX()  + desfaseX+ (graphNodeSource.getWidth() / 2)-  (1*countInstanceOCS) );
        line.setStartY(graphNodeSource.getLayoutY() + desfaseY+  (graphNodeSource.getHeight() / 2)-  (1*countInstanceOCS) );

        line.setEndX(graphNodeDestination.getLayoutX() +desfaseX+ graphNodeSource.getWidth() / 2-  (1*countInstanceOCS));
        line.setEndY(graphNodeDestination.getLayoutY() +desfaseY+ (graphNodeSource.getHeight() / 2)-  (1*countInstanceOCS));
        
         int blue = 10;
         int red = 255;
         int green = 10;
         
        if( countInstanceOCS<50 )
        {
             blue = (int) Math.round(countInstanceOCS*(5));
             red = (int) Math.round(255-(countInstanceOCS*(5)));
        }
        if(countInstanceOCS>50 && countInstanceOCS<100 )
        {
            blue = (int) Math.round( 255- ((countInstanceOCS -50)*(5)));
            red = (int) Math.round( (countInstanceOCS-50) *(5) ) ;
            green=10;
        }
        if( countInstanceOCS>100 && countInstanceOCS<150  )
        {
            blue = 255;
            red = 0;
            green = (int) Math.round( (countInstanceOCS -100)*(5)) ;
           
        }
            
        
        line.setStroke(Color.rgb(red, green, blue));
        
        line.setStrokeWidth(0.5);
//        line.toBack();
//        System.out.print("Dibujando  linea  "+graphNodeSource.getName()+" -  "+graphNodeDestination.getName());
        graphDesignGroup.getGroup().getChildren().add(line);
        graphNodeDestination.getGroup().toFront();
        graphNodeSource.getGroup().toFront();
        
        
//        DropShadow dropShadow = new DropShadow();
//        dropShadow.setColor(Color.PINK);
//        dropShadow.setSpread(0.1);
//        dropShadow.setWidth(10);
//        dropShadow.setHeight(10);
//        line.setEffect(dropShadow);
        
    }
    public void remove()
    {
//        System.out.println("Elminando linea  "+graphNodeSource.getName()+" -  "+graphNodeDestination.getName());
        graphDesignGroup.getGroup().getChildren().remove(line);
        graphDesignGroup.getLinesOCS().remove(line);
    }

    public Line getLine() {
        return line;
    }
    
    
    
}
