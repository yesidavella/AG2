package com.ag2.presentation.control;

public interface ViewResultsClientChart
{
    public void  createClientResult( 
            final String clientName,
            final double requestSent, 
            final double jobNosent,
            final double jobSent,
            final double jobreceive );
    
}
