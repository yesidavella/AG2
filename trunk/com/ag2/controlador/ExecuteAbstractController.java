/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.controlador;

import com.ag2.presentacion.ExecuteView;

/**
 *
 * @author Frank
 */
public abstract class ExecuteAbstractController
{
        ExecuteView executeView;

    public void setExecuteView(ExecuteView executeView) {
        this.executeView = executeView;
    }
        
        public abstract  void run(); 
       public abstract  void stop(); 
    
}
