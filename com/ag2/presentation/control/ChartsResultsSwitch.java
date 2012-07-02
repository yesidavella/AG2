package com.ag2.presentation.control;

import Grid.GridSimulation;
import Grid.Nodes.Hybrid.Parallel.HybridSwitchImpl;
import com.ag2.model.SimulationBase;
import com.ag2.util.Utils;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import simbase.SimBaseEntity;
import simbase.SimBaseSimulator;

public class ChartsResultsSwitch implements ViewResultsSwitchChart {

    private Tab tabChartsSwitchResults;
    private ScrollPane scpAllPage;
    private VBox vbxAllPage;
    private HBox hbxPerExecution;
    private StackedBarChart<String, Number> barChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series<String, Number> uniqueSerie;
//    private XYChart.Series<String, Number> serieNoFreeResouce;
    private XYChart.Series<String, Number> serieReqAckSent;
//    private XYChart.Series<String, Number> serieSendingFailed;
//    private XYChart.Series<String, Number> serieResultsSwiched;
//    private XYChart.Series<String, Number> serieResultsNoSwitched;
    private int countPlays = 1;
    
        final static String austria = "Austria";
    final static String brazil = "Brazil";
    final static String france = "France";
    final static String italy = "Italy";
    final static String usa = "USA";
    
    ArrayList<SimBaseEntity> switchesList;
        ArrayList<String> switchesNameList;

    public ChartsResultsSwitch(Tab tabChartsSwitchResults) {
        this.tabChartsSwitchResults = tabChartsSwitchResults;
        scpAllPage = new ScrollPane();
        vbxAllPage = new VBox();
        vbxAllPage.setPadding(new Insets(10, 10, 10, 10));
        vbxAllPage.setSpacing(10);
        scpAllPage.setContent(vbxAllPage);
        tabChartsSwitchResults.setContent(scpAllPage);
        
    }

    public void play() {
        
        
        switchesList = SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(HybridSwitchImpl.class);
        
        switchesNameList = new ArrayList<String>();
        
        for (SimBaseEntity entity:switchesList) {
            switchesNameList.add(entity.getId());
        }
        
        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        xAxis.setCategories(FXCollections.observableArrayList(switchesNameList));

//        xAxis.setLabel("Nombre Conmutadores");
//        yAxis.setLabel("titulo en y");
        barChart = new StackedBarChart<String, Number>(xAxis, yAxis);
//        barChart.setTitle("Trafico en los enrutadores. Ejecución numero:" + countPlays);
        countPlays++;

        hbxPerExecution = new HBox(10);
        hbxPerExecution.getChildren().addAll(barChart);
        hbxPerExecution.getStyleClass().add("boxChart");
        vbxAllPage.getChildren().add(0, hbxPerExecution);

        uniqueSerie = new XYChart.Series<String, Number>();
        serieReqAckSent = new XYChart.Series<String, Number>();
//        uniqueSerie.setName("");


        
        System.out.print("Play¡¡¡");
    }

    @Override
    public void createSwitchResults(String switchName, double jobsSwitched, double jobsNoSwitched,
            double resultsSwiched, double resultsNoSwitched, double requestsSwitched, double requestsNoSwitched,
            double relativeNojobsNoSwitched, double relativeResultsNoSwitched, double relativeRequestsNoSwitched,
            double relativeAllNoSwitched) {

        String graficalName = Utils.findGraphicalName(switchName);

//        switchesList.add(graficalName);
        
        uniqueSerie.getData().add(new XYChart.Data<String, Number>(switchName, jobsSwitched));
        serieReqAckSent.getData().add(new XYChart.Data<String, Number>(switchName,resultsSwiched));
        

        if(barChart.getData().isEmpty()){
            barChart.getData().addAll(uniqueSerie,serieReqAckSent);
            System.out.print("Es vacio¡¡¡¡¡¡¡¡¡");
        }
        
        System.out.print("Creo");
    }
}
