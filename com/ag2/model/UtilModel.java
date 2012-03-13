package com.ag2.model;

import Distributions.ConstantDistribution;
import Distributions.DDNegExp;
import Grid.Entity;
import Grid.GridSimulator;
import Grid.Interfaces.ClientNode;
import Grid.Interfaces.ResourceNode;
import Grid.Interfaces.ServiceNode;
import Grid.Interfaces.Switch;
import Grid.Nodes.Hybrid.Parallel.*;
import Grid.Nodes.OutputResourceNode;
import Grid.Port.GridInPort;
import Grid.Port.GridOutPort;
import Grid.Utilities.IllegalEdgeException;
import Grid.Utilities.Util;
import com.ag2.config.PropertyPhosphorusTypeEnum;
import simbase.SimBaseSimulator;
import simbase.Time;

public class UtilModel extends Util {

    private static void insertOptionsForClient(ClientNode client, GridSimulator simulator) {
        client.getState().setJobInterArrival(new DDNegExp(simulator,
                PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.DEFAULT_JOB_IAT)));
        client.getState().setFlops(new DDNegExp(simulator,
                PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.DEFAULT_FLOP_SIZE)));
        client.getState().setMaxDelayInterval(new DDNegExp(simulator,
                PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.MAX_DELAY)));

        client.getState().setSizeDistribution(new DDNegExp(simulator,
                PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.DEFAULT_DATA_SIZE)));
        double ackSize = PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.ACK_SIZE);
        if (ackSize == 0) {
            client.getState().setAckSizeDistribution(new ConstantDistribution(ackSize));
        } else {
            client.getState().setAckSizeDistribution(new DDNegExp((SimBaseSimulator) simulator, ackSize));
        }
    }

    public static ClientNode createHybridClient(String id, GridSimulator simulator, ServiceNode service) {

        ClientNode client = new HybridClientNodeImpl(id, simulator, service);
        insertOptionsForClient(client, simulator);
        simulator.register(client);
        return client;
    }

    public static ClientNode createHybridClient(String id, GridSimulator simulator) {
        ClientNode client = new HybridClientNodeImpl(id, simulator);
        insertOptionsForClient(client, simulator);
        simulator.register(client);
        return client;
    }

    private static void insertOptionsForResource(ResourceNode resource, GridSimulator simulator) {
        resource.setCpuCapacity(PropertyPhosphorusTypeEnum.getDoubleProperty(
                PropertyPhosphorusTypeEnum.DEFAULT_CPU_CAPACITY));
        resource.setQueueSize(PropertyPhosphorusTypeEnum.getIntProperty(
                PropertyPhosphorusTypeEnum.DEFAULT_QUEUE_SIZE));
        resource.setCpuCount(PropertyPhosphorusTypeEnum.getIntProperty(
                PropertyPhosphorusTypeEnum.DEFAULT_CPU_COUNT), PropertyPhosphorusTypeEnum.getDoubleProperty(
                PropertyPhosphorusTypeEnum.DEFAULT_CPU_CAPACITY));
    }

    public static ResourceNode createHyridResourceNode(String id, GridSimulator simulator) {
        ResourceNode resource = new HybridResourceNode(id, simulator);
        insertOptionsForResource(resource, simulator);
        simulator.register(resource);
        return resource;
    }

    /**
     * Creates a Hybrid Switch,
     *
     * @param id The id of this switch
     * @param simulator The simulator to which this swithc belongs.
     * @return
     */
    public static Switch createHybridSwitch(String id, GridSimulator simulator) {
        Switch sw = new HybridSwitchImpl(id, simulator);
        //sw.setHandleDelay(new Time(PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.OBS_HANDLE_TIME)));
        simulator.register(sw);
        return sw;
    }

    /**
     * Creates a Hybrid Output Switch
     */
    public static Switch createHybridOutputSwitch(String id, GridSimulator simulator) {
        Switch sw = new OuputSwitchForHybridCase(id, simulator);
        //sw.setHandleDelay(new Time(PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.OBS_HANDLE_TIME)));
        simulator.register(sw);
        return sw;
    }

    /**
     * Creates an Outputresource node : for testing queuing systems
     *
     * @param id
     * @param simulator
     * @return A new outputresource
     */
    public static ResourceNode createOutputResource(
            String id, GridSimulator simulator) {
        ResourceNode resource = new OutputResourceNode(id, simulator);
        simulator.register(resource);
        return resource;
    }

    public static ServiceNode createHybridServiceNode(String id, GridSimulator sim) {
        ServiceNode service = new HybridServiceNode(id, sim);

        sim.register(service);
        return service;
    }

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
                    PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.SWITCHING_SPEED),
                    customLinkSpeed,
                    PropertyPhosphorusTypeEnum.getIntProperty(
                    PropertyPhosphorusTypeEnum.DEFAULT_WAVELENGTHS));
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
     *
     * @param from The first end of the link
     * @param to The second end of the link.
     */
    public static void createBiDirectionalLink(Entity from, Entity to) {
        try {
            UtilModel.createLink(from, to);
            UtilModel.createLink(to, from);
        } catch (IllegalEdgeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static double getCustomLinkSpeed(Entity from) {

        double currenteLinkSpeed = PropertyPhosphorusTypeEnum.getDoubleProperty(PropertyPhosphorusTypeEnum.LINK_SPEED);
        double customLinkSpeed = -1;

        if (from instanceof ClientNode || from instanceof ResourceNode || from instanceof ServiceNode) {
            customLinkSpeed = currenteLinkSpeed / 2;
        } else {
            customLinkSpeed = currenteLinkSpeed;
        }

        return customLinkSpeed;
    }

    static PhosphorusLinkModel crearEnlaceBiDireccional(Entity from, Entity to) {

        GridOutPort puertoSalidaNodoA = null;
        GridOutPort puertoSalidaNodoB = null;

        try {
            puertoSalidaNodoA = UtilModel.crearEnlace(from, to);
            puertoSalidaNodoB = UtilModel.crearEnlace(to, from);
        } catch (IllegalEdgeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return new PhosphorusLinkModel(from, puertoSalidaNodoA, to, puertoSalidaNodoB);
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
                    PropertyPhosphorusTypeEnum.getDoubleProperty(
                    PropertyPhosphorusTypeEnum.SWITCHING_SPEED),
                    customLinkSpeed,
                    PropertyPhosphorusTypeEnum.getIntProperty(
                    PropertyPhosphorusTypeEnum.DEFAULT_WAVELENGTHS));
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