package com.ag2.model;

import Grid.Entity;

public class ClientCreationModel extends NodeCreationModel{

    @Override
    public Entity createPhosphorusNode(String id) {
        //System.out.println("Creo Cliente con monbre:"+nombreNodoGrafico);
        return Grid.Utilities.Util.createHybridClient(id,simulationBase.getSimulador());
    }

}
