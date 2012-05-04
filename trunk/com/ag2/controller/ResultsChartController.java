/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.controller;

import javafx.application.Platform;

/**
 *
 * @author Frank
 */
public class ResultsChartController extends ResultsChartAbstractController {

    @Override
    public void createClientResult( 
           final String clientName,
           final double requestCreated, 
           final double requestSent,
           final double jobSent,
           final double resultReceive) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsClientChart.createClientResult(clientName, requestCreated, requestSent,jobSent, resultReceive );
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void createResourceResult(final String resourceName,final  double jobReceive,final double resultSent) 
    {
         Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsResourceChart.createResourceResult(resourceName,jobReceive, resultSent );
            }
        };
        Platform.runLater(runnable);
        
    }
}
