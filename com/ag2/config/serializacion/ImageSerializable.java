/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.config.serializacion;

import java.io.InputStream;
import java.io.Serializable;

/**
 *
 * @author Frank
 */
public class ImageSerializable extends javafx.scene.image.Image implements Serializable
{
    public ImageSerializable(InputStream inputStream)
    {
        super(inputStream); 
    }        
    
    
}
