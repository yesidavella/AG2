/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion.diseño;

import com.ag2.config.TipoDePropiedadesPhosphorus;
import com.ag2.presentacion.VistaNodosGraficos;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Frank
 */

public class TablaPropiedadesDispositivo extends TableView<PropiedadeNodo> implements VistaNodosGraficos
{
  
    public TablaPropiedadesDispositivo()
    {
        setPrefHeight(200);
        setPrefWidth(400);
        
        TableColumn nombrePropiedadDispositivo = new TableColumn("PROPIEDAD");
        nombrePropiedadDispositivo.setCellValueFactory(new PropertyValueFactory<PropiedadeNodo, String>("nombre"));

        TableColumn valorPropiedadDispositivo = new TableColumn("VALOR");
        valorPropiedadDispositivo.setCellValueFactory(new PropertyValueFactory<PropiedadeNodo, Control>("control"));

        TableColumn tituloTblDispositivo = new TableColumn("PROPIEDADES DISPOSITIVO");
        tituloTblDispositivo.getColumns().addAll(nombrePropiedadDispositivo, valorPropiedadDispositivo);

        getColumns().add(tituloTblDispositivo);
     
    }

    public void cargarPropiedades(ArrayList<PropiedadeNodo> propiedadeNodos)
    {
          ObservableList datosPropiedades = FXCollections.observableArrayList();
        for(PropiedadeNodo propiedadeNodo : propiedadeNodos)
        {
          
            datosPropiedades.add(propiedadeNodo);
        }
           setItems(datosPropiedades);
       
    }
    
}
