package edu.ag2.controller;

import edu.ag2.presentation.ExecuteView;
import java.io.Serializable;

public abstract class ExecuteAbstractController implements Serializable {

    private transient ExecuteView executeView;

    public void setExecuteView(ExecuteView executeView) {
        this.executeView = executeView;
    }

    /**
     * Verifica que la red si este bien configurada para ejecutar la .
     */
    public abstract boolean isWellFormedNetwork();

    public abstract void initNetwork();

    public abstract void run();

    public abstract void stop();
    
    public abstract void reLoadConfigFile(); 
            
}
