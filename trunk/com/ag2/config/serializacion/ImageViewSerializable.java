/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.config.serializacion;

import java.io.Serializable;
import javafx.scene.image.ImageView;

/**
 *
 * @author Frank
 */
public class ImageViewSerializable extends ImageView implements Serializable {

    public ImageViewSerializable(ImageSerializable imagen) {
        super(imagen); 
    }
    
}
