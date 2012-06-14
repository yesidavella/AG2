/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.controller;

import Grid.Entity;
import Grid.OCS.stats.ManagerOCS;
import Grid.OCS.stats.NotificableOCS;
import com.ag2.controller.MatchCoupleObjectContainer.GraphSourceDestination;
import com.ag2.presentation.design.GraphDesignGroup;
import com.ag2.presentation.design.GraphNode;
import com.ag2.presentation.design.GraphOCS;
import com.ag2.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Platform;

/**
 *
 * @author Frank
 */
public class NotifyControllerOCS implements NotificableOCS, Serializable {

    private GraphDesignGroup graphDesignGroup;

    public GraphDesignGroup getGraphDesignGroup() {
        return graphDesignGroup;
    }

    public void setGraphDesignGroup(GraphDesignGroup graphDesignGroup) {
        this.graphDesignGroup = graphDesignGroup;
    }

    public NotifyControllerOCS() {
        ManagerOCS.getInstance().setNotificableOCS(this);
    }
    //FIXME: Poner interface

    @Override
    public void notifyNewCreatedOCS(final Entity entitySource, final Entity entityDestination, final int countInstanceOCS) {


        Runnable runnable = new Runnable() {

            @Override
            public void run() {

                GraphNode graphNodeSource = Utils.findNodeGraphByOriginalName(entitySource.getId());
                GraphNode graphNodeDestination = Utils.findNodeGraphByOriginalName(entityDestination.getId());

                if (!MatchCoupleObjectContainer.containsIntanceOCS(graphNodeSource, graphNodeDestination)) {
                    
                    boolean directionCreated = MatchCoupleObjectContainer.containsIntanceOCS(graphNodeDestination,graphNodeSource);
                    
                    if(directionCreated)
                    {
                        MatchCoupleObjectContainer.getGraphOCS(graphNodeDestination, graphNodeSource).addInstanceOCS_Inverted();
                    }
                    else
                    {
                        GraphOCS graphOCS = graphDesignGroup.addOCSLine(graphNodeSource, graphNodeDestination, countInstanceOCS);
                        MatchCoupleObjectContainer.putInstanceOCS(graphNodeSource, graphNodeDestination, graphOCS);
                    }
                    

                }
                else
                {
                        
                   MatchCoupleObjectContainer.getGraphOCS(graphNodeSource, graphNodeDestination).addInstanceOCS();
                  System.out.println(" YAAAAAA : Creado  LSP " + graphNodeSource.getName() + " - " + graphNodeDestination.getName());
                }
//             
            }
        };
        Platform.runLater(runnable);

    }

    @Override
    public void clean() {
//        HashMap<MatchCoupleObjectContainer.GraphSourceDestination, ArrayList<GraphOCS>> OCSMatchCoupleObjectContainer =
//                MatchCoupleObjectContainer.getOCSMatchCoupleObjectContainer();
//
//        for (GraphSourceDestination graphSourceDestination : OCSMatchCoupleObjectContainer.keySet()) {
//            for (GraphOCS graphOCS : MatchCoupleObjectContainer.getListGraphOCS(graphSourceDestination)) {
//                graphOCS.remove();
//            }
//        }
        for(GraphSourceDestination graphSourceDestination : MatchCoupleObjectContainer.getOCSMatchCoupleObjectContainer().keySet())
        {
            MatchCoupleObjectContainer.getOCSMatchCoupleObjectContainer().get(graphSourceDestination).remove();
        }
        
        

        MatchCoupleObjectContainer.cleanOCSMatchCoupleObjectContainer();

    }
}
