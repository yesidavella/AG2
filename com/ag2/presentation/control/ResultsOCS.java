/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;
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
    private TableView tvSumaryOCS = new TableView();
    private ObservableList<SummaryOCSData> dataSummaryOCS = FXCollections.observableArrayList();
    private Label lblSummaryOCS;

    public ResultsOCS(Tab tab) {
        this.tab = tab;
        scrollPane = new ScrollPane();
        vBoxMain = new VBox();
        vBoxMain.setPadding(new Insets(10, 10, 10, 10));
        vBoxMain.setAlignment(Pos.CENTER);
        vBoxMain.setSpacing(10);


        tvSumaryOCS.setMaxHeight(300);

        lblSummaryOCS = new Label(" Resumen de Caminos Conmutados de Etiquetas de la capa optica (λSP) ");
        lblSummaryOCS.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TableColumn tableColumn1 = new TableColumn();
        tableColumn1.setText("Origen/Destino");
        tableColumn1.setMinWidth(130);
        tableColumn1.setCellValueFactory(new PropertyValueFactory("sourceDestination"));

        TableColumn tableColumn2 = new TableColumn();
        tableColumn2.setText("OCS Solicitudes ");
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
        
        for (int i = 0; i < 10; i++) {
            
       
        
        SummaryOCSData data = new SummaryOCSData();
        
        
        data.setSourceDestination("Nodo 1  - Nodo 2");
        data.setCountFault("5");
        data.setRequestOCS("34");
        data.setCreateOCS("40");
        data.setTimeDuration("407899 ms");

        dataSummaryOCS.add(data);

        tvSumaryOCS.setItems(dataSummaryOCS);
         }
        
        
       
        tvSumaryOCS.getColumns().addAll(tableColumn1, tableColumn2, tableColumn3, tableColumn4, tableColumn5, tableColumn6);

        vBoxMain.getChildren().addAll(lblSummaryOCS, tvSumaryOCS);
        scrollPane.setContent(vBoxMain);
        tab.setContent(scrollPane);

    }

    public static class SummaryOCSData {

        private String sourceDestination;
        private String requestOCS;
        private String createOCS;
        private String countFault;
        private String timeDuration;
        private Button btnViewDetails;
        

        public SummaryOCSData() {
            btnViewDetails = new Button("Ver detalles");
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

        public String getSourceDestination() {
            return sourceDestination;
        }

        public void setSourceDestination(String sourceDestination) {
            this.sourceDestination = sourceDestination;
        }

        public String getTimeDuration() {
            return timeDuration;
        }

        public void setTimeDuration(String timeDuration) {
            this.timeDuration = timeDuration;
        }
    }
}
