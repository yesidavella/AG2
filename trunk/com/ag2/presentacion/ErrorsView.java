package com.ag2.presentacion;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ErrorsView extends Stage {

    private Button btnAccept;
    private ArrayList<Label> errosToShow;
    private BorderPane bPnWindowsLayout;

    public ErrorsView() {
        super(StageStyle.UTILITY);
        errosToShow = new ArrayList<Label>();
        initModality(Modality.APPLICATION_MODAL);
        
        bPnWindowsLayout = new BorderPane();
        Scene reportScene = new Scene(bPnWindowsLayout, 500, 400);
        setScene(reportScene);
        
        configWindow();
    }

    public boolean addErrorToShow(String descriptionOfError) {
        Label errorLabel = new Label(descriptionOfError);
        return errosToShow.add(errorLabel);
    }

    public boolean removeErrorToShow(String descriptionOfError) {

        for (Label errorLabel : errosToShow) {

            if (errorLabel.getText().equalsIgnoreCase(descriptionOfError)) {
                return errosToShow.remove(errorLabel);
            }
        }
        return false;
    }

    public void showReport() {
        VBox vbxErrorscontainer = new VBox();
        
        for (Label errorLabel : errosToShow) {
            System.out.println(errorLabel.getText());
            vbxErrorscontainer.getChildren().add(errorLabel);
        }
        
        bPnWindowsLayout.setCenter(vbxErrorscontainer);
        centerOnScreen();
        show();
    }

    private void configWindow() {
        Label lbTitle = new Label("LOS ERRORES ENCONTRADOS EN LA CONFIGURACIÓN DE LA RED SON LOS SIGUIENTES:");
        bPnWindowsLayout.setTop(lbTitle);
        
        btnAccept = new Button("Aceptar");
        bPnWindowsLayout.setBottom(btnAccept);
        
        btnAccept.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent arg0) {
                ErrorsView.this.close();
            }
        });
    }
}
