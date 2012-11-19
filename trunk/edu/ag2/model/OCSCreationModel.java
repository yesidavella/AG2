package edu.ag2.model;

import Grid.Entity;
import Grid.OCS.OCSRoute;

public class OCSCreationModel extends LinkCreationAbstractModel{

    @Override
    public Object createLink(Entity entityA, Entity entityB) {
        
        OCSRoute ocsRoute = UtilModel.createOCS(entityA, entityB, SimulationBase.getInstance().getGridSimulatorModel(), true);
        return ocsRoute;
    }
}
