/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

/**
 *
 * @author Frank
 */
public class ChartsResult 
{
    private Tab tab;
    private ScrollPane scrollPane;
    private VBox vBox;
        

    public ChartsResult(Tab tab) 
    {
        this.tab = tab;
        scrollPane = new ScrollPane();
        vBox = new VBox();
        vBox.getChildren().add(new Label("rere"));
        scrollPane.setContent(vBox);
        tab.setContent(scrollPane);
    }
        
    
}
