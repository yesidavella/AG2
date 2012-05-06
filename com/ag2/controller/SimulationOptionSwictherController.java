/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.controller;

import Grid.Entity;
import Grid.Nodes.AbstractServiceNode;
import com.ag2.model.AG2_ResourceSelectorModel;
import com.ag2.presentation.design.BrokerGrahpNode;
import com.ag2.presentation.design.GraphNode;
import java.util.HashMap;

/**
 *
 * @author INFORCOL
 */
public class SimulationOptionSwictherController {

     
    public enum OptionSimulation {

        AG2, PHOSPHORUS, PHOSPHORUS_OPTIMIZE
    };
    private static SimulationOptionSwictherController simulationOptionSwictherController;

    public static SimulationOptionSwictherController getInstance() {
        if (simulationOptionSwictherController == null) {
            simulationOptionSwictherController = new SimulationOptionSwictherController();
        }
        return simulationOptionSwictherController;
    }
    private OptionSimulation optionSimulation;

    public OptionSimulation getOptionSimulation() {
        return optionSimulation;
    }

    public void setOptionSimulation(OptionSimulation optionSimulation) {
        this.optionSimulation = optionSimulation;

        for (GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {
            if (graphNode instanceof BrokerGrahpNode) {
                BrokerGrahpNode brokerGrahpNode = (BrokerGrahpNode) graphNode;
                AbstractServiceNode abstractServiceNode = (AbstractServiceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(brokerGrahpNode);

                switch( optionSimulation)
                {
                    case  AG2:
                    {
                        AG2_ResourceSelectorModel aG2_ResourceSelectorModel = new AG2_ResourceSelectorModel(abstractServiceNode.getResources());
                        abstractServiceNode.setResourceSelector(aG2_ResourceSelectorModel);
                        break;
                    }
                    case PHOSPHORUS:
                    {
                        abstractServiceNode.loadDefaultResourceSelector();
                        break;
                    }
                    case  PHOSPHORUS_OPTIMIZE:
                    {
                        abstractServiceNode.loadStandarSelector();
                        break;
                    }
                }
            }
        }
    }


    private SimulationOptionSwictherController() {
    }
}
