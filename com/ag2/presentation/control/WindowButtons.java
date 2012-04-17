/*
 * Copyright (c) 2008, 2011 Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.ag2.presentation.control;

import com.ag2.presentation.Main;
import com.ag2.util.ResourcesPath;
import com.sun.javafx.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * Vertical box with 3 small buttons for window close, minimize and maximize.
 */
public class WindowButtons extends VBox {

    private Stage stage;
    private Rectangle2D backupWindowBounds = null;
    private boolean maximized = false;
    private Main main;
    Button closeBtn = new Button();
    StackPane layerPane;

    public void setMain(Main main) {
        this.main = main;
        closeBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {

                int result = JOptionPane.showConfirmDialog(
                        null, "¿Desea guardar los cambios efectuados en la simulación?", "Simulador AG2", JOptionPane.YES_NO_CANCEL_OPTION);

                if (result == JOptionPane.NO_OPTION) {
                    System.exit(0);
                } else if (result == JOptionPane.YES_OPTION) {
                    WindowButtons.this.main.save(true);
                }

                //Platform.exit();
            }
        });
    }

    public WindowButtons(final Stage stage, final StackPane layerPane) {
        super(0);
        this.stage = stage;
        // create buttons
        this.layerPane = layerPane;
        setScaleX(1.3);
        setScaleY(1.3);
        closeBtn.setGraphic(new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "window-close.png")));
        closeBtn.setId("window-close");
        closeBtn.setMaxWidth(4);



        Button minBtn = new Button();
        minBtn.setGraphic(new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "window-min.png")));
        minBtn.setId("window-min");
        minBtn.setMaxWidth(4);
        minBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                stage.setIconified(true);
            }
        });
        Button maxBtn = new Button();
        maxBtn.setId("window-max");
        maxBtn.setMaxWidth(4);
        maxBtn.setGraphic(new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "window-expand.png")));

        maxBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {
                toogleMaximized();
            }
        });
        getChildren().addAll(closeBtn, minBtn, maxBtn);
    }

    public void toogleMaximized() {
        
        try {
            final Screen screen = Screen.getPrimary().getScreensForRectangle(stage.getX(), stage.getY(), 1, 1).get(0);
            if (maximized) {
                maximized = false;
                if (backupWindowBounds != null) {
                    stage.setX(backupWindowBounds.getMinX());
                    stage.setY(backupWindowBounds.getMinY());
                    stage.setWidth(backupWindowBounds.getWidth());
                    stage.setHeight(backupWindowBounds.getHeight());

                    layerPane.setPrefHeight(backupWindowBounds.getHeight());
                    layerPane.setMaxHeight(backupWindowBounds.getHeight());
                    layerPane.setMinHeight(backupWindowBounds.getHeight());

                    layerPane.setPrefWidth(backupWindowBounds.getWidth());
                    layerPane.setMaxWidth(backupWindowBounds.getWidth());
                    layerPane.setMinWidth(backupWindowBounds.getWidth());
                }
            } else {
                maximized = true;
                backupWindowBounds = new Rectangle2D(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
                stage.setX(screen.getVisualBounds().getMinX());
                stage.setY(screen.getVisualBounds().getMinY());
                stage.setWidth(screen.getVisualBounds().getWidth());
                stage.setHeight(screen.getVisualBounds().getHeight());

                layerPane.setPrefHeight(screen.getVisualBounds().getHeight());
                layerPane.setMaxHeight(screen.getVisualBounds().getHeight());
                layerPane.setMinHeight(screen.getVisualBounds().getHeight());

                layerPane.setPrefWidth(screen.getVisualBounds().getWidth());
                layerPane.setMaxWidth(screen.getVisualBounds().getWidth());
                layerPane.setMinWidth(screen.getVisualBounds().getWidth());
            }
        } catch (Exception ex) {
        }
    }

    public boolean isMaximized() {
        return maximized;
    }
}
