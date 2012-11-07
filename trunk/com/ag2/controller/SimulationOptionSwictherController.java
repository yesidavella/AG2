package com.ag2.controller;

import Grid.Nodes.AbstractServiceNode;
import Grid.Sender.Sender;
import com.ag2.model.AG2_ResourceSelectorModel;
import com.ag2.presentation.design.BrokerGrahpNode;
import com.ag2.presentation.design.GraphNode;

public class SimulationOptionSwictherController {

    public enum OptionSimulation {
        AG2, PHOSPHORUS, PHOSPHORUS_OPTIMIZE
    };
    
    private static SimulationOptionSwictherController simulationOptionSwictherController;
    private OptionSimulation optionSimulation;

    public static SimulationOptionSwictherController getInstance() {
        if (simulationOptionSwictherController == null) {
            simulationOptionSwictherController = new SimulationOptionSwictherController();
        }
        return simulationOptionSwictherController;
    }

    public OptionSimulation getOptionSimulation() {
        return optionSimulation;
    }

    public void setOptionSimulation(OptionSimulation optionSimulation) {
        this.optionSimulation = optionSimulation;

        for (GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {
            
            if (graphNode instanceof BrokerGrahpNode) {
                BrokerGrahpNode brokerGrahpNode = (BrokerGrahpNode) graphNode;
                AbstractServiceNode abstractServiceNode = (AbstractServiceNode) MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(brokerGrahpNode);

                switch (optionSimulation) {
                    case AG2: {
                        AG2_ResourceSelectorModel ag2_ResourceSelectorModel = new AG2_ResourceSelectorModel(abstractServiceNode.getResources());
                        abstractServiceNode.setResourceSelector(ag2_ResourceSelectorModel);
                        Sender.setAg2ResourceSelectorSelected(true);
                        break;
                    }
                    case PHOSPHORUS: {
                        abstractServiceNode.loadDefaultResourceSelector();
                        Sender.setAg2ResourceSelectorSelected(false);
                        break;
                    }
                    case PHOSPHORUS_OPTIMIZE: {
                        abstractServiceNode.loadStandarSelector();
                        Sender.setAg2ResourceSelectorSelected(false);
                        break;
                    }
                }
            }
        }
    }

    private SimulationOptionSwictherController() {
    }
}
