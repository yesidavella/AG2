package edu.ag2.presentation.control;

import Grid.Utilities.HtmlWriter;
import edu.ag2.presentation.images.ImageHelper;
import edu.ag2.util.Utils;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class PhosphosrusHTMLResults {

    private ImageView leftEndImageView = new ImageView(new Image(ImageHelper.getResourceInputStream("mini_izquierda_fin.png")));
    private ImageView leftImageView = new ImageView(new Image(ImageHelper.getResourceInputStream("mini_izquierda.png")));
    private ImageView rightImageView = new ImageView(new Image(ImageHelper.getResourceInputStream("mini_derecha.png")));
    private ImageView rightEndImageView = new ImageView(new Image(ImageHelper.getResourceInputStream("mini_derecha_fin.png")));
    private Button btnLeftEnd = new Button();
    private Button btnLeft = new Button();
    private Button btnRight = new Button();
    private Button btnRightEnd = new Button();
    private TextField txtPage = new TextField();
    private String RESULTS_FOLDER = "Results_HTML";
    private int totalPage = 0;
    private int currentPage = 0;
    private String[] filesHTML;
    private WebView wvBrowser = new WebView();
    private WebEngine webEngine = wvBrowser.getEngine();
    private String nameFolder;
    private VBox vBox = new VBox();
    private HBox hBox = new HBox();
    private static short SIZE_X  = 16;
    private static short SIZE_Y  = 16;
    
    private VBox vBoxMain;

    public PhosphosrusHTMLResults(VBox vBoxMain) {

        this.vBoxMain = vBoxMain;
        btnLeftEnd.setGraphic(leftEndImageView);
        btnLeft.setGraphic(leftImageView);
        btnRight.setGraphic(rightImageView);
        btnRightEnd.setGraphic(rightEndImageView);

        btnLeftEnd.setMaxSize(20, 18);
        btnLeft.setMaxSize(20, 18);
        btnRight.setMaxSize(20, 18);
        btnRightEnd.setMaxSize(20, 18);
        hBox.getChildren().addAll(btnLeftEnd, btnLeft, txtPage, btnRight, btnRightEnd);
        
       btnLeftEnd.setMinSize(SIZE_X, SIZE_Y);
       btnLeftEnd.setMaxSize(SIZE_X, SIZE_Y);
       btnLeftEnd.setPrefSize(SIZE_X, SIZE_Y);
       
       btnLeft.setMinSize(SIZE_X, SIZE_Y);
       btnLeft.setMaxSize(SIZE_X, SIZE_Y);
       btnLeft.setPrefSize(SIZE_X, SIZE_Y);
       
       btnRight.setMinSize(SIZE_X, SIZE_Y);
       btnRight.setMaxSize(SIZE_X, SIZE_Y);
       btnRight.setPrefSize(SIZE_X, SIZE_Y);
       
       btnRightEnd.setMinSize(SIZE_X, SIZE_Y);
       btnRightEnd.setMaxSize(SIZE_X, SIZE_Y);
       btnRightEnd.setPrefSize(SIZE_X, SIZE_Y);
       
   

        hBox.setAlignment(Pos.CENTER);

      
                    loadFilesHTMLs();
              
    }

    public void lookToNextExecution() {
    }

    private void loadFilesHTMLs() {


        String currentFolder = new File("").getAbsolutePath();
        nameFolder = currentFolder + File.separator + RESULTS_FOLDER + "_" + (HtmlWriter.countObject - 2);
        File file = new File(nameFolder);

        if (file.exists()) {
            filesHTML = file.list();
            totalPage = filesHTML.length;
        }

        txtPage.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                txtPage.setText("");
            }
        });


        txtPage.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                String valuePage = txtPage.getText();
                Pattern p = Pattern.compile("\\d+");
                Matcher m = p.matcher(valuePage);

                if (m.matches()) {
                    currentPage = Integer.parseInt(txtPage.getText());

                    if (currentPage > totalPage || currentPage < 1) {
                        currentPage = 1;
                        txtPage.setText(currentPage + "/" + totalPage);
                        webEngine.load("file:///" + nameFolder + File.separator + filesHTML[currentPage - 1]);
                        enableButtons();
                    } else {
                        txtPage.setText(currentPage + "/" + totalPage);
                        webEngine.load("file:///" + nameFolder + File.separator + filesHTML[currentPage - 1]);
                        enableButtons();
                    }
                }
            }
        });

        txtPage.setMaxWidth(50);


        if (filesHTML != null && filesHTML.length >= 1) {
            currentPage = 1;
            txtPage.setText(currentPage + "/" + totalPage);
            webEngine.load("file:///" + nameFolder + File.separator + filesHTML[0]);
            if (!vBox.getChildren().contains(wvBrowser)) {
                vBox.getChildren().addAll(wvBrowser);
            }
            if (!vBox.getChildren().contains(hBox)) {
                vBox.getChildren().addAll(hBox);
            }
        } else {
            txtPage.setText("--");
            if (!vBox.getChildren().contains(hBox)) {
                vBox.getChildren().addAll(hBox);
            }
            btnRight.setDisable(true);
            btnRightEnd.setDisable(true);
        }

        vBoxMain.getChildren().add( vBox);
        establishEventBtnLeftEnd(btnLeftEnd);
        establishEventBtnRightEnd(btnRightEnd);
        establishEventBtnLeft(btnLeft);
        establishEventBtnRight(btnRight);

        if(filesHTML.length >= 2){            
        
        btnLeft.setDisable(true);
        btnLeftEnd.setDisable(true);
        }
        enableButtons();
    }

    private void enableButtons() {
        if (currentPage == 1) {
            btnLeft.setDisable(true);
            btnLeftEnd.setDisable(true);
        } else {
            btnLeft.setDisable(false);
            btnLeftEnd.setDisable(false);
        }

        if (currentPage == totalPage) {
            btnRight.setDisable(true);
            btnRightEnd.setDisable(true);
        } else {
            btnRight.setDisable(false);
            btnRightEnd.setDisable(false);
        }

    }

    private void establishEventBtnLeft(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                currentPage -= 1;
                txtPage.setText(currentPage + "/" + totalPage);
                webEngine.load("file:///" + nameFolder + File.separator + filesHTML[currentPage - 1]);
                enableButtons();
            }
        });

    }

    private void establishEventBtnRight(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {

                currentPage += 1;
                txtPage.setText(currentPage + "/" + totalPage);
                webEngine.load("file:///" + nameFolder + File.separator + filesHTML[currentPage - 1]);
                enableButtons();

            }
        });

    }

    private void establishEventBtnLeftEnd(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                currentPage = 1;
                txtPage.setText(currentPage + "/" + totalPage);
                webEngine.load("file:///" + nameFolder + File.separator + filesHTML[0]);
                enableButtons();
            }
        });

    }

    private void establishEventBtnRightEnd(Button button) {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                currentPage = filesHTML.length;
                txtPage.setText(filesHTML.length + "/" + totalPage);
                webEngine.load("file:///" + nameFolder + File.separator + filesHTML[filesHTML.length - 1]);
                enableButtons();
            }
        });

    }
}