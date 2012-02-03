package com.ag2.presentacion;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ErrorsView extends Stage {

    private Button btnAccept;
    private ArrayList<Text> errosToShow;
    private BorderPane borPnWindowsLayout;

    public ErrorsView() {
        super(StageStyle.UTILITY);
        errosToShow = new ArrayList<Text>();
        initModality(Modality.APPLICATION_MODAL);
        
        borPnWindowsLayout = new BorderPane();
        Scene reportScene = new Scene(borPnWindowsLayout, 500, 400);
        setScene(reportScene);
        
        configWindow();
    }

    public boolean addErrorToShow(String descriptionOfError) {
        Text errorText = new Text(descriptionOfError);
        return errosToShow.add(errorText);
    }

//    public boolean removeErrorToShow(String descriptionOfError) {
//
//        for (Label errorLabel : errosToShow) {
//
//            if (errorLabel.getText().equalsIgnoreCase(descriptionOfError)) {
//                return errosToShow.remove(errorLabel);
//            }
//        }
//        return false;
//    }

    public void showReport() {
        VBox vbxErrorscontainer = new VBox();
        
        for (Text errorText : errosToShow) {
            vbxErrorscontainer.getChildren().add(errorText);
        }
        
        borPnWindowsLayout.setCenter(vbxErrorscontainer);
        centerOnScreen();
        show();
    }

    private void configWindow() {
        Text txTitle = TextBuilder.create().text("LOS ERRORES ENCONTRADOS EN LA\nCONFIGURACIÓN DE LA RED:").
                font(Font.font("Tahoma",FontWeight.BOLD, 20)).textAlignment(TextAlignment.CENTER).build();

        borPnWindowsLayout.setTop(txTitle);
        
        btnAccept = new Button("Aceptar");
        borPnWindowsLayout.setBottom(btnAccept);
        
        btnAccept.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent arg0) {
                ErrorsView.this.close();
            }
        });
    }
}
