/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;

import com.ag2.presentation.design.GraphNode;
import java.text.DecimalFormat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Frank
 */
public class ResultsOCS {

    private transient Tab tab;
    private ScrollPane scrollPane;
    private VBox vBoxMain;
    private TableView tvSummaryOCS ;
    private TableView tvInstaceOCS ;
    private ObservableList<SummaryOCSData> dataSummaryOCS = FXCollections.observableArrayList();
    private ObservableList<InstanceOCSData> dataInstanceOCS = FXCollections.observableArrayList();
    private Label lblSummaryOCS;
    private Label lblInstanceOCS;
    private DecimalFormat decimalFormat = new DecimalFormat("#");
    
    private ResultsOCSController resultsOCSController ;

    public ResultsOCS(Tab tab) {
        this.tab = tab;
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        vBoxMain = new VBox();
        vBoxMain.setFillWidth(true);
        vBoxMain.setPadding(new Insets(10, 10, 10, 10));
        vBoxMain.setAlignment(Pos.CENTER);
        vBoxMain.setSpacing(10);           

        lblSummaryOCS = new Label(" Resumen de Caminos Conmutados de Etiquetas de la capa optica (λSP) ");
        lblInstanceOCS = new Label();
        lblSummaryOCS.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lblInstanceOCS.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        createTVSummaryOCS();
        createTVInstanceOCS();
                
        vBoxMain.getChildren().addAll(lblSummaryOCS, tvSummaryOCS, lblInstanceOCS, tvInstaceOCS);
        scrollPane.setContent(vBoxMain);
        tab.setContent(scrollPane);
    }
    public void play()
    {
        dataInstanceOCS.clear();
        dataSummaryOCS.clear();
        resultsOCSController.clean();
    }
    
    public void showResults()
    {
        dataSummaryOCS.clear();       
        
        for (int i = 0; i < resultsOCSController.sizeSummaryOCS(); i++) 
        {            
            resultsOCSController.loadOCS_SummaryByIndex(i);
            SummaryOCSData summaryOCSData = new  SummaryOCSData();
            GraphNode graphNodeSource = resultsOCSController.getGnSummarySource();
            GraphNode graphNodeDestion = resultsOCSController.getGnSummaryDestination();
            
            summaryOCSData.setGraphNodeSource(graphNodeSource);
            summaryOCSData.setGraphNodeDestination(graphNodeDestion);
            
            summaryOCSData.setSource(" "+graphNodeSource.getName());
            summaryOCSData.setDestination(" "+graphNodeDestion.getName());
            
            summaryOCSData.setRequestOCS("   "+decimalFormat.format(resultsOCSController.getRequestedSummaryOCS()));
            summaryOCSData.setCreateOCS("   "+decimalFormat.format(resultsOCSController.getCreatedSummaryOCS()));
            summaryOCSData.setCountFault("   "+decimalFormat.format(resultsOCSController.getFaultSummaryOCS()));            
            summaryOCSData.setTimeDuration("   "+decimalFormat.format(resultsOCSController.getDurationTimeInstanceOCS()));           
              
            dataSummaryOCS.add(summaryOCSData);            
            
        }        
        
    }
    
    public void loadInstancesOCS(GraphNode graphNodeSource, GraphNode graphNodeDestination)
    {
        dataInstanceOCS.clear();
      
        lblInstanceOCS.setText("Camino entre "+graphNodeSource.getName()+" y "+graphNodeDestination.getName());
        
        for (int i = 0; i < resultsOCSController.sizeInstanceOCS(graphNodeSource, graphNodeDestination); i++)
        {
            InstanceOCSData instanceOCSData = new InstanceOCSData();            
            resultsOCSController.loadOCS_InstanceByIndex(graphNodeSource, graphNodeDestination, i);
            
            String path =""; 
            String separator ="";
            int breaker=0; 
            for(GraphNode graphNode:  resultsOCSController.getPathInstaceOCS())
            {
                breaker++;
                path += separator+ graphNode.getName();
                separator=" - ";
                if(breaker>=4)
                {
                    breaker=0;
                    path +="\n";
                    separator ="";
                }
                
            }          
            String wavelengthIDs = "";
            separator ="";
            for ( Integer w : resultsOCSController.getListWavelengthID() ) {
                
                wavelengthIDs += separator+ w;
                separator =" , ";
            }
            
            
            instanceOCSData.setPath(path);            
            instanceOCSData.setRequestTime("   "+resultsOCSController.getRequestTimeInstanceOCS());
            instanceOCSData.setLambda("   "+wavelengthIDs);
            instanceOCSData.setSetupTime("   "+resultsOCSController.getSetupTimeInstanceOCS());
            instanceOCSData.setDurationTime("   "+resultsOCSController.getDurationTimeInstanceOCS());
            instanceOCSData.setTearDownTime("   "+resultsOCSController.getTearDownTimeInstanceOCS());
            instanceOCSData.setTraffic("   "+resultsOCSController.getTrafficInstanceOCS());
            
            if(resultsOCSController.getNodeErrorInstanceOCS()==null)
            {    
                instanceOCSData.setErrorNodo("Sin problemas");
            }
            else
            {
                instanceOCSData.setErrorNodo("Nodo: "+resultsOCSController.getNodeErrorInstanceOCS().getName()+ "\n"+resultsOCSController.getProblemInstanceOCS());
            }            
            
            dataInstanceOCS.add(instanceOCSData);
            
        }
        
    }
    
    
    private void createTVInstanceOCS()
    {
        tvInstaceOCS= new TableView();
        tvInstaceOCS.setMaxHeight(250);
        tvInstaceOCS.setMinHeight(250);

        TableColumn tableColumn1 = new TableColumn();
        tableColumn1.setText("Camino");
        tableColumn1.setMinWidth(230);
        tableColumn1.setCellValueFactory(new PropertyValueFactory("path"));

        TableColumn tableColumn8 = new TableColumn();
        tableColumn8.setText("Longitud  \nde onda (λ)");
        tableColumn8.setMinWidth(100);
        tableColumn8.setCellValueFactory(new PropertyValueFactory("lambda"));
        
                
        TableColumn tableColumn2 = new TableColumn();
        tableColumn2.setText("Tiempo de la \nsolicitud λSP");
        tableColumn2.setMinWidth(110);
        tableColumn2.setCellValueFactory(new PropertyValueFactory("requestTime"));

        TableColumn tableColumn3 = new TableColumn();
        tableColumn3.setText("Tiempo de \nestablecimiento λSP");
        tableColumn3.setMinWidth(130);
        tableColumn3.setCellValueFactory(new PropertyValueFactory("setupTime"));

        TableColumn tableColumn4 = new TableColumn();
        tableColumn4.setText("Duracion λSP");
        tableColumn4.setMinWidth(110);
        tableColumn4.setCellValueFactory(new PropertyValueFactory("durationTime"));

        TableColumn tableColumn5 = new TableColumn();
        tableColumn5.setText("Tiempo de\nliberacion λSP");
        tableColumn5.setMinWidth(130);
        tableColumn5.setCellValueFactory(new PropertyValueFactory("tearDownTime"));

        TableColumn tableColumn6 = new TableColumn();
        tableColumn6.setText("Trafico");
        tableColumn6.setMinWidth(110);
        tableColumn6.setCellValueFactory(new PropertyValueFactory("traffic"));  
        
        TableColumn tableColumn7 = new TableColumn();
        tableColumn7.setText("Problemas");
        tableColumn7.setMinWidth(320);
        tableColumn7.setCellValueFactory(new PropertyValueFactory("errorNodo")); 

        tvInstaceOCS.setItems(dataInstanceOCS);       
        tvInstaceOCS.getColumns().addAll(
                tableColumn1, 
                tableColumn2, 
                tableColumn8,
                tableColumn3,
                tableColumn4, 
                tableColumn5, 
                tableColumn6,
                tableColumn7);
 
        
    }
    private void createTVSummaryOCS() {
        tvSummaryOCS= new TableView();
        tvSummaryOCS.setMaxHeight(250);
        tvSummaryOCS.setMinHeight(250);

        TableColumn tableColumn1 = new TableColumn();
        tableColumn1.setText("Origen");
        tableColumn1.setMinWidth(220);
        tableColumn1.setCellValueFactory(new PropertyValueFactory("source"));
        
        TableColumn tableColumn7 = new TableColumn();
        tableColumn7.setText("Destino");
        tableColumn7.setMinWidth(220);
        tableColumn7.setCellValueFactory(new PropertyValueFactory("destination"));

        TableColumn tableColumn2 = new TableColumn();
        tableColumn2.setText("OCS Solicitudes");
        tableColumn2.setMinWidth(130);
        tableColumn2.setCellValueFactory(new PropertyValueFactory("requestOCS"));

        TableColumn tableColumn3 = new TableColumn();
        tableColumn3.setText("OCS Creados");
        tableColumn3.setMinWidth(130);
        tableColumn3.setCellValueFactory(new PropertyValueFactory("createOCS"));

        TableColumn tableColumn4 = new TableColumn();
        tableColumn4.setText("OCS Fallidos");
        tableColumn4.setMinWidth(130);
        tableColumn4.setCellValueFactory(new PropertyValueFactory("countFault"));

        TableColumn tableColumn5 = new TableColumn();
        tableColumn5.setText("Duracion promedio");
        tableColumn5.setMinWidth(130);
        tableColumn5.setCellValueFactory(new PropertyValueFactory("timeDuration"));

        TableColumn tableColumn6 = new TableColumn();
        tableColumn6.setText("Detalles");
        tableColumn6.setMinWidth(130);
        tableColumn6.setCellValueFactory(new PropertyValueFactory("btnViewDetails"));  
        

        tvSummaryOCS.setItems(dataSummaryOCS);       
        tvSummaryOCS.getColumns().addAll(tableColumn1,tableColumn7 ,tableColumn2, tableColumn3, tableColumn4, tableColumn5, tableColumn6);
    }
    public static class InstanceOCSData{
        
         String source;
         String destination;
         String path;
         String requestTime;
         String setupTime;         
         String durationTime;
         String tearDownTime;
         String errorNodo;
         String traffic;
         String lambda;

        public String getLambda() {
            return lambda;
        }

        public void setLambda(String lambda) {
            this.lambda = lambda;
        }

        public String getDurationTime() {
            return durationTime;
        }

        public void setDurationTime(String durationTime) {
            this.durationTime = durationTime;
        }

        public String getErrorNodo() {
            return errorNodo;
        }

        public void setErrorNodo(String errorNodo) {
            this.errorNodo = errorNodo;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getRequestTime() {
            return requestTime;
        }

        public void setRequestTime(String requestTime) {
            this.requestTime = requestTime;
        }

        public String getSetupTime() {
            return setupTime;
        }

        public void setSetupTime(String setupTime) {
            this.setupTime = setupTime;
        }

   
        public String getTearDownTime() {
            return tearDownTime;
        }

        public void setTearDownTime(String tearDownTime) {
            this.tearDownTime = tearDownTime;
        }

        public String getTraffic() {
            return traffic;
        }

        public void setTraffic(String traffic) {
            this.traffic = traffic;
        }
                  
    }

    public  class SummaryOCSData {

        private String source;
        private String destination;
        private String requestOCS;
        private String createOCS;
        private String countFault;
        private String durationTime;
        private Button btnViewDetails;
        private GraphNode graphNodeSource;
        private GraphNode graphNodeDestination;
        
        

        public SummaryOCSData() {
            btnViewDetails = new Button("Ver detalles");
            btnViewDetails.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent arg0) {
                   ResultsOCS.this.loadInstancesOCS(graphNodeSource, graphNodeDestination);
                }
            });
        }

        public Button getBtnViewDetails() {
            return btnViewDetails;
        }

        public String getCountFault() {
            return countFault;
        }

        public void setCountFault(String countFault) {
            this.countFault = countFault;
        }

        public String getCreateOCS() {
            return createOCS;
        }

        public void setCreateOCS(String createOCS) {
            this.createOCS = createOCS;
        }

        public String getRequestOCS() {
            return requestOCS;
        }

        public void setRequestOCS(String requestOCS) {
            this.requestOCS = requestOCS;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

      

        public String getDurationTime() {
            return durationTime;
        }

        public void setTimeDuration(String timeDuration) {
            this.durationTime = timeDuration;
        }

        public GraphNode getGraphNodeDestination() {
            return graphNodeDestination;
        }

        public void setGraphNodeDestination(GraphNode graphNodeDestination) {
            this.graphNodeDestination = graphNodeDestination;
        }

        public GraphNode getGraphNodeSource() {
            return graphNodeSource;
        }

        public void setGraphNodeSource(GraphNode graphNodeSource) {
            this.graphNodeSource = graphNodeSource;
        }
        
        
    }

    public ResultsOCSController getResultsOCSController() {
        return resultsOCSController;
    }

    public void setResultsOCSController(ResultsOCSController resultsOCSController) {
        this.resultsOCSController = resultsOCSController;
    }
    
    
}
