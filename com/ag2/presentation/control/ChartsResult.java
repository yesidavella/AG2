/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;

import com.ag2.util.ResourcesPath;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Frank
 */
public class ChartsResult  implements ViewResultsChart
{
    private Tab tab;
    private ScrollPane scrollPane;
    private VBox vBox;
    private HBox hBoxClient;
    
        

    public ChartsResult(Tab tab) 
    {
        this.tab = tab;
        scrollPane = new ScrollPane();
        vBox = new VBox();
        
        scrollPane.setContent(vBox);
        tab.setContent(scrollPane);
       
    }
    public void play()
    {
        hBoxClient =  new HBox();        
        vBox.getChildren().addAll(hBoxClient);        
    }
            
    public void createPieChart()
    {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
             new PieChart.Data("Sun", 20),
             new PieChart.Data("IBM", 12),
             new PieChart.Data("HP", 25),
             new PieChart.Data("Dell", 22),
             new PieChart.Data("Apple", 30)
         );
        PieChart chart = new PieChart(pieChartData);
        chart.setClockwise(false);
        vBox.getChildren().add(chart);
    }
        
    @Override
    public void  createClientResult(String clientName, double relativeResultReceive, double relativeJobsNoSent )
    {
        System.out.println(" ######  "+clientName+" "+relativeResultReceive+"  "+relativeJobsNoSent);
        
        double relativeNoResultReceive = 100 -relativeResultReceive;
    
         ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
             new PieChart.Data("Resultado no recibidos", relativeNoResultReceive),
             new PieChart.Data("Resultado  recibidos", relativeResultReceive)             
         );
        PieChart chart = new PieChart(pieChartData);
        chart.setTitle(ResourcesPath.findGraphicalName(clientName) );
        chart.setClockwise(false);
        hBoxClient.getChildren().add(chart);
        
        
    }
    
        
    
}
