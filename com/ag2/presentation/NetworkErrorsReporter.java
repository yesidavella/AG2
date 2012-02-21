package com.ag2.presentation;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NetworkErrorsReporter extends Stage {

    private Button btnAccept;
    private ArrayList<Text> errosToShow;
    private BorderPane brPnWindowsLayout;

    public NetworkErrorsReporter() {
        super(StageStyle.UTILITY);
        setResizable(true);
        errosToShow = new ArrayList<Text>();
        initModality(Modality.APPLICATION_MODAL);
        setResizable(false);

        brPnWindowsLayout = new BorderPane();
        brPnWindowsLayout.setPadding(new Insets(10, 10, 10, 10));
        
        Scene reportScene = new Scene(brPnWindowsLayout, 510, 400);//#FFEB8C
        setGradientEffect(reportScene);
        
        setScene(reportScene);
        configWindow();
    }

    private void setGradientEffect(Scene reportScene) {
        Stop[] stops = new Stop[] { 
            new Stop(0.2, Color.LIGHTSTEELBLUE),
            new Stop(0.5, Color.WHITESMOKE),
            new Stop(0.8, Color.LIGHTSTEELBLUE)
        };
        LinearGradient linearGradient = new LinearGradient(0,0,1,0.25, true, CycleMethod.NO_CYCLE, stops);
        reportScene.setFill(linearGradient);
    }

    private void configWindow() {
        final int WARNING_WIDTH = 60;
        HBox hbTitle = new HBox();
        hbTitle.setPadding(new Insets(0, 0, 10, 0));
        hbTitle.setAlignment(Pos.CENTER);
        
        ImageView ivWarning = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/warning.jpg")));
        double proportionXYivWarning = ivWarning.getBoundsInParent().getWidth()/ivWarning.getBoundsInParent().getHeight();
        ivWarning.setFitHeight(WARNING_WIDTH);
        ivWarning.setFitWidth(WARNING_WIDTH*proportionXYivWarning);
        
        Label lbTitle = LabelBuilder.create().text("LOS ERRORES ENCONTRADOS EN LA CONFIGURACIÓN DE LA RED:").
                font(Font.font("Cambria", FontWeight.BOLD, 20)).textAlignment(TextAlignment.CENTER).
                graphic(ivWarning).graphicTextGap(-60).build();
        lbTitle.setWrapText(true);
        lbTitle.setPrefWidth(480);

        hbTitle.getChildren().add(lbTitle);
        brPnWindowsLayout.setTop(hbTitle);

        btnAccept = new Button("Aceptar");
        Label lbAdvise = LabelBuilder.create().text("¡¡¡Soluciones estos errores y vuelva a intentarlo!!!").
                font(Font.font("Cambria", FontWeight.BOLD,FontPosture.ITALIC, 12)).build();
        HBox hbBottom = new HBox(10);
        hbBottom.setPadding(new Insets(10, 0, 0, 0));
        hbBottom.setAlignment(Pos.CENTER_RIGHT);
        hbBottom.getChildren().addAll(lbAdvise, btnAccept);
        brPnWindowsLayout.setBottom(hbBottom);

        btnAccept.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                NetworkErrorsReporter.this.close();
            }
        });
    }

    public boolean addErrorToShow(String descriptionOfError) {
        Text errorText = new Text(descriptionOfError);
        return errosToShow.add(errorText);
    }

    public void showReport() {
        ScrollPane scrPnErrorList = new ScrollPane();
        VBox vbxErrorsContainer = new VBox(10);
        vbxErrorsContainer.setPadding(new Insets(10));
        scrPnErrorList.setContent(vbxErrorsContainer);
        brPnWindowsLayout.setCenter(scrPnErrorList);
        centerOnScreen();

        for (Text errorText : errosToShow) {
            errorText.setWrappingWidth(465);
            vbxErrorsContainer.getChildren().add(errorText);
        }
        show();
    }
}