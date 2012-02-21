package com.ag2.presentation;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AboutAG2project extends ModalAG2window {

    BorderPane brPnWindowsLayout;
    Button btnAccept;
    
    public AboutAG2project() {
        super("Acerca del proyecto AG2...");
    }

    @Override
    public void configWindow() {
        brPnWindowsLayout = getBrPnWindowsLayout();
        btnAccept = getBtnAccept();
        
        setWidth(400);
        setHeight(530);
        
        topDesing();
        centerDesing();
        
        brPnWindowsLayout.setBottom(btnAccept);
        
        show();
    }
    
    private void topDesing() {
    
        HBox hbxTop = new HBox();
        Label lbTitle = new Label("Acerca del Proyecto AG2...");
        lbTitle.setFont(Font.font("Cambria", FontWeight.BOLD, 16));
        lbTitle.setWrapText(true);
        hbxTop.getChildren().add(lbTitle);
        hbxTop.setAlignment(Pos.CENTER);
        brPnWindowsLayout.setTop(hbxTop);
    }

    private void centerDesing() {
        
        HBox hbxMajorContainer = new HBox(10);
        VBox vbxLogos = new VBox(5);
        VBox vbxDescription = new VBox(5);
        
        hbxMajorContainer.setPadding(new Insets(3,5,3,3));
        vbxLogos.setAlignment(Pos.CENTER);
        vbxDescription.setAlignment(Pos.CENTER_LEFT);
        
        hbxMajorContainer.getChildren().addAll(vbxLogos,vbxDescription);
        
        createLogos(vbxLogos);
        createDescriptions(vbxDescription);
        
        brPnWindowsLayout.setCenter(hbxMajorContainer);
    }
    
    private void createLogos(VBox vbxLogos) {
        final int AG2_HEIGHT = 80;
        final int INTER_INTEL_HEIGHT = 70;
        final int PHOSPHORUS_HEIGHT = 80;
        final int UNIV_DIST_HEIGHT = 80;
        
        ImageView ivAG2 = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/logoAG2.png")));
        double proportionXYAG2 = ivAG2.getBoundsInParent().getWidth()/ivAG2.getBoundsInParent().getHeight();
        ivAG2.setFitHeight(AG2_HEIGHT);
        ivAG2.setFitWidth(AG2_HEIGHT*proportionXYAG2);
        
        ImageView ivInternetIntel = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/logoInterInt.png")));
        double proportionXYInternetIn = ivInternetIntel.getBoundsInParent().getWidth()/ivInternetIntel.getBoundsInParent().getHeight();
        ivInternetIntel.setFitHeight(INTER_INTEL_HEIGHT);
        ivInternetIntel.setFitWidth(INTER_INTEL_HEIGHT*proportionXYInternetIn);

        ImageView ivPhosphorus = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/phosphorus.jpg")));
        double proportionXYphosphorus = ivPhosphorus.getBoundsInParent().getWidth()/ivPhosphorus.getBoundsInParent().getHeight();
        ivPhosphorus.setFitHeight(PHOSPHORUS_HEIGHT);
        ivPhosphorus.setFitWidth(PHOSPHORUS_HEIGHT*proportionXYphosphorus);

        ImageView ivDistritalUniv = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/escudo_udistrital.jpg")));
        double proportionXYdistritalUniv = ivDistritalUniv.getBoundsInParent().getWidth()/ivDistritalUniv.getBoundsInParent().getHeight();
        ivDistritalUniv.setFitHeight(UNIV_DIST_HEIGHT);
        ivDistritalUniv.setFitWidth(UNIV_DIST_HEIGHT*proportionXYdistritalUniv);

        Hyperlink linkAG2 = new Hyperlink();
        Hyperlink linkInterIntel = new Hyperlink();
        Hyperlink linkPhosphorus = new Hyperlink();
        Hyperlink linkDistritalUniv = new Hyperlink();

        linkAG2.setTooltip(new Tooltip("Visite la página web del Grupo de Investigación en \"Colciencias\""));
        linkInterIntel.setTooltip(new Tooltip("Visite la página web del Grupo de Investigación \"Internet Inteligente\""));
        linkPhosphorus.setTooltip(new Tooltip("Visite la página web del proyecto \"Fósforo\""));
        linkDistritalUniv.setTooltip(new Tooltip("Visite la página web de la \"Universidad Distrital FJC\""));

        linkAG2.setGraphic(ivAG2);
        linkInterIntel.setGraphic(ivInternetIntel);
        linkPhosphorus.setGraphic(ivPhosphorus);
        linkDistritalUniv.setGraphic(ivDistritalUniv);

        GUI.getInstance().setOnLunchBrowser(linkAG2,"http://201.234.78.173:8080/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000003328");
        GUI.getInstance().setOnLunchBrowser(linkInterIntel,"http://gemini.udistrital.edu.co/comunidad/grupos/internetinteligente/");
        GUI.getInstance().setOnLunchBrowser(linkPhosphorus,"http://www.ist-phosphorus.eu/");
        GUI.getInstance().setOnLunchBrowser(linkDistritalUniv,"www.udistrital.edu.co");
        
        vbxLogos.getChildren().addAll(linkAG2,linkInterIntel,linkPhosphorus,linkDistritalUniv);
    }

    private void createDescriptions(VBox vbxDescription) {
        TextArea ta1 = new TextArea("Holaaaaaaaa");
        TextArea ta2 = new TextArea("Holaaaaaaaa");
        TextArea ta3 = new TextArea("Holaaaaaaaa");
        TextArea ta4 = new TextArea("Holaaaaaaaa");
        
        vbxDescription.getChildren().addAll(ta1,ta2,ta3,ta4);
    }

}