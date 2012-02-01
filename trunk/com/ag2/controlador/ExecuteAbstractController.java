/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.controlador;

import com.ag2.presentacion.ExecuteView;
import java.io.Serializable;

/**
 *
 * @author Frank
 */
public abstract class ExecuteAbstractController implements Serializable {

    ExecuteView executeView;

    public void setExecuteView(ExecuteView executeView) {
        this.executeView = executeView;
    }

    /**
     * Verifica que la red si este bien armada, sin grafos disconexos.
     */
    public abstract boolean isWellFormedNetwork();

    public abstract void run();

    public abstract void stop();
}
