package com.ag2.presentation;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class AboutAG2project extends ModalAG2window {

    public enum Description_type {

        AG2, INTERNET_INTEL, PHOSPHORUS, DISTRITAL_UNIV
    };
    private BorderPane brPnWindowsLayout;
    private Button btnAccept;

    public AboutAG2project() {
        super("Acerca del proyecto AG2...");
    }

    @Override
    public void configWindow() {
        brPnWindowsLayout = getBrPnWindowsLayout();
        btnAccept = getBtnAccept();

        setWidth(600);
        setHeight(630);

        topDesing();
        centerDesing();
        bottomDesing();

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
        GridPane grpLogosAndDescription = new GridPane();

        hbxMajorContainer.setPadding(new Insets(3, 5, 3, 3));
        grpLogosAndDescription.setHgap(5);
        grpLogosAndDescription.setVgap(5);
        ColumnConstraints colLogosConst = new ColumnConstraints();
        colLogosConst.setHalignment(HPos.CENTER);
        ColumnConstraints colDescriptionConst = new ColumnConstraints();
        colDescriptionConst.setHalignment(HPos.LEFT);
        grpLogosAndDescription.getColumnConstraints().addAll(colLogosConst, colDescriptionConst);

        hbxMajorContainer.getChildren().addAll(grpLogosAndDescription);

        createLogosAndDescriptions(grpLogosAndDescription);

        brPnWindowsLayout.setCenter(hbxMajorContainer);
    }

    private void createLogosAndDescriptions(GridPane grpLogosAndDescription) {
        final int AG2_HEIGHT = 80;
        final int INTER_INTEL_HEIGHT = 70;
        final int PHOSPHORUS_HEIGHT = 80;
        final int UNIV_DIST_HEIGHT = 80;

        ImageView ivAG2 = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/logoAG2.png")));
        double proportionXYAG2 = ivAG2.getBoundsInParent().getWidth() / ivAG2.getBoundsInParent().getHeight();
        ivAG2.setFitHeight(AG2_HEIGHT);
        ivAG2.setFitWidth(AG2_HEIGHT * proportionXYAG2);

        ImageView ivInternetIntel = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/logoInterInt.png")));
        double proportionXYInternetIn = ivInternetIntel.getBoundsInParent().getWidth() / ivInternetIntel.getBoundsInParent().getHeight();
        ivInternetIntel.setFitHeight(INTER_INTEL_HEIGHT);
        ivInternetIntel.setFitWidth(INTER_INTEL_HEIGHT * proportionXYInternetIn);

        ImageView ivPhosphorus = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/phosphorus.jpg")));
        double proportionXYphosphorus = ivPhosphorus.getBoundsInParent().getWidth() / ivPhosphorus.getBoundsInParent().getHeight();
        ivPhosphorus.setFitHeight(PHOSPHORUS_HEIGHT);
        ivPhosphorus.setFitWidth(PHOSPHORUS_HEIGHT * proportionXYphosphorus);

        ImageView ivDistritalUniv = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/escudo_udistrital.jpg")));
        double proportionXYdistritalUniv = ivDistritalUniv.getBoundsInParent().getWidth() / ivDistritalUniv.getBoundsInParent().getHeight();
        ivDistritalUniv.setFitHeight(UNIV_DIST_HEIGHT);
        ivDistritalUniv.setFitWidth(UNIV_DIST_HEIGHT * proportionXYdistritalUniv);

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

        GUI.getInstance().setOnLunchBrowser(linkAG2, "http://201.234.78.173:8080/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000003328");
        GUI.getInstance().setOnLunchBrowser(linkInterIntel, "http://gemini.udistrital.edu.co/comunidad/grupos/internetinteligente/");
        GUI.getInstance().setOnLunchBrowser(linkPhosphorus, "http://www.ist-phosphorus.eu/");
        GUI.getInstance().setOnLunchBrowser(linkDistritalUniv, "www.udistrital.edu.co");

        DescriptionProject descripAG2 = new DescriptionProject(Description_type.AG2);
        DescriptionProject descripInterIntel = new DescriptionProject(Description_type.INTERNET_INTEL);
        DescriptionProject descripPhosphorus = new DescriptionProject(Description_type.PHOSPHORUS);
        DescriptionProject descripDistritalUniv = new DescriptionProject(Description_type.DISTRITAL_UNIV);

        GridPane.setConstraints(linkAG2, 0, 0);
        grpLogosAndDescription.getChildren().add(linkAG2);
        GridPane.setConstraints(descripAG2, 1, 0);
        grpLogosAndDescription.getChildren().add(descripAG2);

        GridPane.setConstraints(linkInterIntel, 0, 1);
        grpLogosAndDescription.getChildren().add(linkInterIntel);
        GridPane.setConstraints(descripInterIntel, 1, 1);
        grpLogosAndDescription.getChildren().add(descripInterIntel);

        GridPane.setConstraints(linkPhosphorus, 0, 2);
        grpLogosAndDescription.getChildren().add(linkPhosphorus);
        GridPane.setConstraints(descripPhosphorus, 1, 2);
        grpLogosAndDescription.getChildren().add(descripPhosphorus);

        GridPane.setConstraints(linkDistritalUniv, 0, 3);
        grpLogosAndDescription.getChildren().add(linkDistritalUniv);
        GridPane.setConstraints(descripDistritalUniv, 1, 3);
        grpLogosAndDescription.getChildren().add(descripDistritalUniv);

    }

    private void bottomDesing() {
        HBox hbxBottom = new HBox(10);
        hbxBottom.setPadding(new Insets(5, 5, 0, 5));
        hbxBottom.setAlignment(Pos.CENTER);

        hbxBottom.getChildren().add(btnAccept);

        brPnWindowsLayout.setBottom(hbxBottom);
    }

    public class DescriptionProject extends VBox {

        public DescriptionProject(Description_type description_type) {
            super(5);
            setPadding(new Insets(5));

            setStyle("-fx-border-radius: 5;"
                    + "-fx-border-color:lightgray;"
                    + "-fx-border-width:3px;"
                    + "-fx-background-color:whitesmoke;");
            loadDescription(description_type);
        }

        private void loadDescription(Description_type description_type) {

            HBox hbxName = new HBox(3);
            HBox hbxBriefDescrpt = new HBox(3);
            HBox hbxWebPage = new HBox(3);
            
            final int WIDTH_TITLE = 80;
            Label lbTitleName = LabelBuilder.create().text("Nombre:").font(Font.font("Arial", FontWeight.BOLD, 12)).minWidth(WIDTH_TITLE).alignment(Pos.CENTER_RIGHT).build();
            Label lbTitleBriefDescription = LabelBuilder.create().text("Descripción:").font(Font.font("Arial", FontWeight.BOLD, 12)).minWidth(WIDTH_TITLE).alignment(Pos.CENTER_RIGHT).build();
            Label lbTitleWebPage = LabelBuilder.create().text("Pagina Web:").font(Font.font("Arial", FontWeight.BOLD, 12)).minWidth(WIDTH_TITLE).alignment(Pos.CENTER_RIGHT).build();
                        
            Label lbName = LabelBuilder.create().textAlignment(TextAlignment.JUSTIFY).build();
            Label lbBriefDescription = LabelBuilder.create().textAlignment(TextAlignment.JUSTIFY).build();
            Hyperlink hlinkWebPage = HyperlinkBuilder.create().textAlignment(TextAlignment.JUSTIFY).build();
            
            hbxName.getChildren().addAll(lbTitleName,lbName);
            hbxBriefDescrpt.getChildren().addAll(lbTitleBriefDescription,lbBriefDescription);
            hbxWebPage.getChildren().addAll(lbTitleWebPage,hlinkWebPage);
            
            getChildren().addAll(hbxName,hbxBriefDescrpt,hbxWebPage);

            lbName.setPrefWidth(300);
            lbBriefDescription.setPrefWidth(300);
            hlinkWebPage.setPrefWidth(300);
            
            lbName.setWrapText(true);
            lbBriefDescription.setWrapText(true);
            hlinkWebPage.setWrapText(true);

            switch (description_type) {
                case AG2: {
                    lbName.setText("Simulador del Modelo arquitectural AG2.");
                    lbBriefDescription.setText("Simulador de redes opticas sobre Grillas computacionales para la "
                            + "generacion de λSP con QoS y de respaldo.");
                    hlinkWebPage.setText("http://201.234.78.173:8080/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000003328");
                    GUI.getInstance().setOnLunchBrowser(hlinkWebPage,"http://201.234.78.173:8080/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000003328");
                    break;
                }
                case INTERNET_INTEL: {
                    lbName.setText("Grupo de Investigacion \"Internet Inteligente\".");
                    lbBriefDescription.setText("Crear, promover, divulgar y fomentar críticamente con carácter "
                            + "pluralista la generación de conocimiento en los campos avanzados de las ciencias, "
                            + "la técnica y la tecnología en el campo de la informatica y las telecomunicaciones "
                            + "en La Universidad Distrital.");
                    hlinkWebPage.setText("http://gemini.udistrital.edu.co/comunidad/grupos/internetinteligente");
                    GUI.getInstance().setOnLunchBrowser(hlinkWebPage,"http://gemini.udistrital.edu.co/comunidad/grupos/internetinteligente");
                    break;
                }
                case PHOSPHORUS: {
                    lbName.setText("Proyecto Fosforo.");
                    lbBriefDescription.setText("Fuentes tomadas del simulador optico desarrollado por este proyecto.");
                    hlinkWebPage.setText("http://www.ist-phosphorus.eu");
                    GUI.getInstance().setOnLunchBrowser(hlinkWebPage,"http://www.ist-phosphorus.eu");
                    break;
                }
                case DISTRITAL_UNIV: {
                    lbName.setText("Universidad Distrital Francisco Jose de Caldas.");
                    lbBriefDescription.setText("Institución de educación superior del Distrito Capital de Bogotá "
                            + "y de la Región Central de la Republica de Colombia.");
                    hlinkWebPage.setText("http://www.udistrital.edu.co");
                    GUI.getInstance().setOnLunchBrowser(hlinkWebPage,"http://www.udistrital.edu.co/");
                    break;
                }
            }

        }
    }
}