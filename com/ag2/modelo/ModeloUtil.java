package com.ag2.modelo;

import Grid.Entity;
import Grid.GridSimulation;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Port.GridInPort;
import Grid.Port.GridOutPort;
import Grid.Utilities.Config;
import Grid.Utilities.IllegalEdgeException;
import Grid.Utilities.Util;

public class ModeloUtil extends Util {

    /**
     * Creates a one way link between from and to.
     *
     * @param from The first end of the link
     * @param to The second end of the link.
     * @throws IllegalEdgeException
     */
    public static void createLink(Entity from, Entity to) throws IllegalEdgeException {
        if (from.supportsOBS() == to.supportsOBS() && from.supportsOCS() == to.supportsOCS()) {
            
            StringBuffer buffer = new StringBuffer(from.getId());
            buffer.append("-");
            buffer.append(to.getId());
            
            double customLinkSpeed = getCustomLinkSpeed(from);
            
            GridOutPort out = new GridOutPort(buffer.toString(), from,
                    GridSimulation.configuration.getDoubleProperty(
                    Config.ConfigEnum.switchingSpeed),
                    customLinkSpeed,
                    GridSimulation.configuration.getIntProperty(
                    Config.ConfigEnum.defaultWavelengths));
            GridInPort in = new GridInPort(buffer.toString(), to);
            out.setTarget(in);
            in.setSource(out);
            from.addOutPort(out);
            to.addInPort(in);
            
        } else {
            throw new IllegalEdgeException("Cannot connect two entities which do not share the same swithcing protocols "
                    + from.getId() + " -->" + to.getId());
        }
    }
    
    /**
     * Create a bi directional link between from and to
     * @param from The first end of the link
     * @param to The second end of the link.
     */
    public static void createBiDirectionalLink(Entity from, Entity to) {
        try {
            ModeloUtil.createLink(from, to);
            ModeloUtil.createLink(to, from);
        } catch (IllegalEdgeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static double getCustomLinkSpeed(Entity from) {
        
        double currenteLinkSpeed = GridSimulation.configuration.getDoubleProperty(Config.ConfigEnum.linkSpeed);
        double customLinkSpeed = -1;
        
        if(from instanceof ClientNode || from instanceof ResourceNode || from instanceof ServiceNode){
            customLinkSpeed = currenteLinkSpeed/2;
        }else{
            customLinkSpeed = currenteLinkSpeed;
        }

        return customLinkSpeed;
    }

    static EnlacePhosphorous crearEnlaceBiDireccional(Entity from, Entity to) {
        
        GridOutPort puertoSalidaNodoA = null;
        GridOutPort puertoSalidaNodoB = null;
        
        try {
            puertoSalidaNodoA = ModeloUtil.crearEnlace(from, to);
            puertoSalidaNodoB = ModeloUtil.crearEnlace(to, from);
        } catch (IllegalEdgeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        
        return new EnlacePhosphorous(from, puertoSalidaNodoA, to, puertoSalidaNodoB);
    }

    /**
     * Creates a one way link between from and to.
     *
     * @param from The first end of the link
     * @param to The second end of the link.
     * @throws IllegalEdgeException
     */
    public static GridOutPort crearEnlace(Entity from, Entity to) throws IllegalEdgeException {
        if (from.supportsOBS() == to.supportsOBS() && from.supportsOCS() == to.supportsOCS()) {
            
            StringBuffer buffer = new StringBuffer(from.getId());
            buffer.append("-");
            buffer.append(to.getId());
            
            double customLinkSpeed = getCustomLinkSpeed(from);
            
            GridOutPort out = new GridOutPort(buffer.toString(), from,
                    GridSimulation.configuration.getDoubleProperty(
                    Config.ConfigEnum.switchingSpeed),
                    customLinkSpeed,
                    GridSimulation.configuration.getIntProperty(
                    Config.ConfigEnum.defaultWavelengths));
            GridInPort in = new GridInPort(buffer.toString(), to);
            out.setTarget(in);
            in.setSource(out);
            from.addOutPort(out);
            to.addInPort(in);
            
            return out;
            
        } else {
            throw new IllegalEdgeException("Cannot connect two entities which do not share the same swithcing protocols "
                    + from.getId() + " -->" + to.getId());
        }
    }
}
