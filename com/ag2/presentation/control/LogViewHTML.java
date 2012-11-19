package com.ag2.presentation.control;

import com.ag2.presentation.ModalAG2window;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class LogViewHTML extends ModalAG2window {

 
    private BorderPane brPnWindowsLayout;
    private Button btnAccept;
    private VBox vBoxMain; 
    public LogViewHTML() 
    {
        super("Acerca del proyecto AG2...");
       
    }
    
   
    @Override
    public void configWindow() {
        
        brPnWindowsLayout = getBrPnWindowsLayout();
        btnAccept = getBtnAccept();
        vBoxMain = new VBox();
        setWidth(800);
        setHeight(635);

        topDesing();
        centerDesing();
        bottomDesing();

        show();
    }

    private void topDesing() {

        HBox hbxTop = new HBox();
        Label lbTitle = new Label("Registro de eventos HTML");
        lbTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        lbTitle.setWrapText(true);
        hbxTop.getChildren().add(lbTitle);
        hbxTop.setAlignment(Pos.CENTER);
        brPnWindowsLayout.setTop(hbxTop);
    }

    public VBox getvBoxMain() {
        return vBoxMain;
    }

    
    private void centerDesing() {

        HBox hbxMajorContainer = new HBox(10);
      

        hbxMajorContainer.setPadding(new Insets(3, 5, 3, 3));
       
        ColumnConstraints colLogosConst = new ColumnConstraints();
        colLogosConst.setHalignment(HPos.CENTER);
        ColumnConstraints colDescriptionConst = new ColumnConstraints();
        colDescriptionConst.setHalignment(HPos.LEFT);
       
        brPnWindowsLayout.setCenter(hbxMajorContainer);
        hbxMajorContainer.getChildren().add(vBoxMain);
    }

  

    private void bottomDesing() {
        HBox hbxBottom = new HBox(10);
        hbxBottom.setPadding(new Insets(5, 5, 0, 5));
        hbxBottom.setAlignment(Pos.CENTER);

        hbxBottom.getChildren().add(btnAccept);

        brPnWindowsLayout.setBottom(hbxBottom);
    }

   
}