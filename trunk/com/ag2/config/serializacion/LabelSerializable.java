/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.config.serializacion;

import java.io.Serializable;

/**
 *
 * @author Frank
 */
public class LabelSerializable extends javafx.scene.control.Label implements Serializable {

    public LabelSerializable(String nombre) {
        super(nombre); 
    }
    
}
