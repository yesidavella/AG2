/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ag2.presentation.maps;

import edu.ag2.presentation.images.*;
import java.io.InputStream;

/**
 *
 * @author Frank
 */
public class MapHelper 
{
    public static InputStream getResourceInputStream(String name)
    {
        return MapHelper.class.getResourceAsStream(name); 
    }         
            
    

}
