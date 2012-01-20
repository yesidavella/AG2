/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion;

import com.ag2.controlador.ExecuteAbstractController;
import com.ag2.presentacion.controles.Boton;
import com.ag2.presentacion.controles.ResultadosPhosphorousHTML;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;

/**
 *
 * @author Frank
 */
    public class ExecutePane extends TilePane implements ExecuteView {

    Boton btnEjecutar = new Boton(TiposDeBoton.EJECUTAR);
    Boton btnParar = new Boton(TiposDeBoton.PARAR);
    ToggleGroup tgEjecucion = new ToggleGroup();
    ResultadosPhosphorousHTML resultadosPhosphorousHTML;
    
    ExecuteAbstractController  executeAbstractController;

    public void setExecuteAbstractController(ExecuteAbstractController executeAbstractController) 
    {
        this.executeAbstractController = executeAbstractController;
        executeAbstractController.setExecuteView(this);
    }

    public void setResultadosPhosphorousHTML(ResultadosPhosphorousHTML resultadosPhosphorousHTML) {
        this.resultadosPhosphorousHTML = resultadosPhosphorousHTML;
    }
    

    public ExecutePane()
    {
        getStyleClass().add("barraDeHerramientas");
        setPadding(new Insets(10, 10, 10, 10));
        setHgap(4);
        setPrefColumns(2);

        Tooltip tTipBtnEjecutar = new Tooltip("Ejecutar simulación");
        btnEjecutar.setTooltip(tTipBtnEjecutar);
        btnEjecutar.setToggleGroup(tgEjecucion);
        btnEjecutar.setGrupoDeDiseño(null);
        
        btnEjecutar.setOnMouseClicked(new  EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) 
            {
               if(executeAbstractController!=null)
               {
                   if(resultadosPhosphorousHTML!=null)
                   {
                       resultadosPhosphorousHTML.deleteHTMLFiles();
                   }
                   
                   executeAbstractController.run();
               
                   
               }
            }
        });
        

        
        Tooltip tTipBtnParar = new Tooltip("Parar simulación");
        btnParar.setTooltip(tTipBtnParar);
        btnParar.setToggleGroup(tgEjecucion);
        btnParar.setGrupoDeDiseño(null);
        btnParar.setSelected(true);
        getChildren().addAll(btnEjecutar, btnParar);
    }
    
    
}
