/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ag2.presentation.images;

import java.io.InputStream;

/**
 *
 * @author Frank
 */
public class ImageHelper 
{
    public static InputStream getResourceInputStream(String name)
    {
        return ImageHelper.class.getResourceAsStream(name); 
    }         
            
    

}
