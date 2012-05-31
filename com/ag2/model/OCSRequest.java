package com.ag2.model;
import Grid.Entity;
import java.io.Serializable;

public class OCSRequest implements Serializable{

    private Entity source;
    private Entity destination;

    public OCSRequest(Entity source, Entity destination) {
        this.source = source;
        this.destination = destination;
    }

    public Entity getDestination() {
        return destination;
    }

    public void setDestination(Entity destination) {
        this.destination = destination;
    }

    public Entity getSource() {
        return source;
    }

    public void setSource(Entity source) {
        this.source = source;
    }
}