/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;

/**
 *
 * @author Frank
 */
public interface ViewResultsClientChart
{
    public void  createClientResult( 
            final String clientName,
            final double requestSent, 
            final double jobNosent,
            final double jobSent,
            final double jobreceive );
    
}
