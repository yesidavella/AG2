package edu.ag2.presentation;

import edu.ag2.util.Utils;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;

public class NetworkErrorsReporter extends ModalAG2window {

    private ArrayList<Text> errosToShow;

    public NetworkErrorsReporter() {
        super("Errores encontrados...");
        errosToShow = new ArrayList<Text>();
    }

    @Override
    public void configWindow() {
        final int WARNING_WIDTH = 60;
        HBox hbTitle = new HBox();
        hbTitle.setPadding(new Insets(0, 0, 10, 0));
        hbTitle.setAlignment(Pos.CENTER);
        
        ImageView ivWarning = new ImageView(new Image(Utils.ABS_PATH_IMGS+"warning.png"));
        double proportionXYivWarning = ivWarning.getBoundsInParent().getWidth()/ivWarning.getBoundsInParent().getHeight();
        ivWarning.setFitHeight(WARNING_WIDTH);
        ivWarning.setFitWidth(WARNING_WIDTH*proportionXYivWarning);
        
        Label lbTitle = LabelBuilder.create().text("LOS ERRORES ENCONTRADOS EN LA CONFIGURACIÓN DE LA RED:").
                font(Font.font("Arial", FontWeight.BOLD, 20)).textAlignment(TextAlignment.CENTER).
                graphic(ivWarning).graphicTextGap(-60).build();
        lbTitle.setWrapText(true);
        lbTitle.setPrefWidth(480);

        hbTitle.getChildren().add(lbTitle);
        getBrPnWindowsLayout().setTop(hbTitle);

        Label lbAdvise = LabelBuilder.create().text("¡¡¡Soluciones estos errores y vuelva a intentarlo!!!").
                font(Font.font("Arial", FontWeight.BOLD,FontPosture.ITALIC, 12)).build();
        HBox hbBottom = new HBox(10);
        hbBottom.setPadding(new Insets(10, 0, 0, 0));
        hbBottom.setAlignment(Pos.CENTER_RIGHT);
        hbBottom.getChildren().addAll(lbAdvise, getBtnAccept());
        getBrPnWindowsLayout().setBottom(hbBottom);
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
        getBrPnWindowsLayout().setCenter(scrPnErrorList);
        centerOnScreen();

        for (Text errorText : errosToShow) {
            errorText.setWrappingWidth(465);
            vbxErrorsContainer.getChildren().add(errorText);
        }
        show();
    }
}