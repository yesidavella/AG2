package edu.ag2.presentation.control;

import Grid.Nodes.Hybrid.Parallel.HybridSwitchImpl;
import edu.ag2.model.SimulationBase;
import edu.ag2.util.Utils;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import simbase.SimBaseEntity;

public class ChartsResultsSwitch implements ViewResultsSwitchChart {

    private Tab tabChartsSwitchResults;
    private ScrollPane scpAllPage;
    private VBox vbxAllPage;
    private HBox hbxPerExecution;
    private StackedBarChart<String, Number> barChart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private XYChart.Series<String, Number> serieJobsSwitched;
    private XYChart.Series<String, Number> serieJobsNoSwitched;
    private XYChart.Series<String, Number> serieResultsSwiched;
    private XYChart.Series<String, Number> serieResultsNoSwitched;
    private XYChart.Series<String, Number> serieRequestsSwitched;
    private XYChart.Series<String, Number> serieRequestsNoSwitched;
    private int countPlays = 1;
    ArrayList<SimBaseEntity> switchesList;
    ArrayList<String> switchesNameList;

    public ChartsResultsSwitch(Tab tabChartsSwitchResults) {
        this.tabChartsSwitchResults = tabChartsSwitchResults;
        scpAllPage = new ScrollPane();
        scpAllPage.getStyleClass().add("bg-general-container");
        vbxAllPage = new VBox();
        vbxAllPage.setFillWidth(true);
        vbxAllPage.setPadding(new Insets(10, 10, 10, 10));
        vbxAllPage.setSpacing(10);
        scpAllPage.setContent(vbxAllPage);
        tabChartsSwitchResults.setContent(scpAllPage);

    }

    public void play() {

        switchesList = SimulationBase.getInstance().getGridSimulatorModel().getEntitiesOfType(HybridSwitchImpl.class);
        switchesNameList = new ArrayList<String>();

        for (SimBaseEntity entity : switchesList) {
            switchesNameList.add(Utils.findGraphicalName(entity.getId()));
        }

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis();
        xAxis.setLabel("Nombre Conmutadores");
        yAxis.setLabel("Mensajes");

        barChart = new StackedBarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle("Trafico en los enrutadores. Ejecución numero:" + countPlays);
        barChart.setMinHeight(550);
        barChart.setMinWidth(1000);
        countPlays++;

        xAxis.setCategories(FXCollections.observableArrayList(switchesNameList));
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis));
        yAxis.setTickLabelsVisible(true);

        hbxPerExecution = new HBox(10);
        hbxPerExecution.getChildren().addAll(barChart);
        hbxPerExecution.getStyleClass().add("boxChart");
        vbxAllPage.getChildren().add(0, hbxPerExecution);

        serieJobsSwitched = new XYChart.Series<String, Number>();
        serieJobsNoSwitched = new XYChart.Series<String, Number>();
        serieResultsSwiched = new XYChart.Series<String, Number>();
        serieResultsNoSwitched = new XYChart.Series<String, Number>();
        serieRequestsSwitched = new XYChart.Series<String, Number>();
        serieRequestsNoSwitched = new XYChart.Series<String, Number>();

        serieJobsSwitched.setName("Trabajos Conmutados");
        serieJobsNoSwitched.setName("Trabajos No Conmutados");
        serieResultsSwiched.setName("Trabajos Atendidos Conmutados");
        serieResultsNoSwitched.setName("Trabajos Atendidos No Conmutados");
        serieRequestsSwitched.setName("Solicitudes de Agendamiento Conmutadas");
        serieRequestsNoSwitched.setName("Solicitudes de Agendamiento No Conmutadas");

    }

    @Override
    public void createSwitchResults(String switchName, double jobsSwitched, double jobsNoSwitched,
            double resultsSwiched, double resultsNoSwitched, double requestsSwitched, double requestsNoSwitched,
            double relativeNojobsNoSwitched, double relativeResultsNoSwitched, double relativeRequestsNoSwitched,
            double relativeAllNoSwitched) {

        String graficalName = Utils.findGraphicalName(switchName);

        Data<String, Number> dataJobsSwitched = new XYChart.Data<String, Number>(graficalName, jobsSwitched);
        Data<String, Number> datajobsNoSwitched = new XYChart.Data<String, Number>(graficalName, jobsNoSwitched);
        Data<String, Number> dataresultsSwiched = new XYChart.Data<String, Number>(graficalName, resultsSwiched);
        Data<String, Number> dataresultsNoSwitched = new XYChart.Data<String, Number>(graficalName, resultsNoSwitched);
        Data<String, Number> datarequestsSwitched = new XYChart.Data<String, Number>(graficalName, requestsSwitched);
        Data<String, Number> dataRequestsNoSwitched = new XYChart.Data<String, Number>(graficalName, requestsNoSwitched);

        serieJobsSwitched.getData().addAll(dataJobsSwitched);
        serieJobsNoSwitched.getData().addAll(datajobsNoSwitched);
        serieResultsSwiched.getData().addAll(dataresultsSwiched);
        serieResultsNoSwitched.getData().addAll(dataresultsNoSwitched);
        serieRequestsSwitched.getData().addAll(datarequestsSwitched);
        serieRequestsNoSwitched.getData().addAll(dataRequestsNoSwitched);

        if (barChart.getData().isEmpty()) {
            barChart.getData().addAll(serieRequestsSwitched, serieRequestsNoSwitched,
                    serieJobsSwitched, serieJobsNoSwitched, serieResultsSwiched, serieResultsNoSwitched);
        }
    }
}
