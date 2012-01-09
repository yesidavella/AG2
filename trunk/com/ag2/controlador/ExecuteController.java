/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.controlador;

import com.ag2.modelo.SimulacionBase;

/**
 *
 * @author Frank
 */
public class ExecuteController extends  ExecuteAbstractController
{  
    public void run()
    {
       System.out.println("RUN");
       SimulacionBase.getInstance().run();
    }
    
}
