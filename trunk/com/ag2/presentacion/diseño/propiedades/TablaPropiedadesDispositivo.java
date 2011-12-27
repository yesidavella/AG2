/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion.diseño.propiedades;

import com.ag2.config.TipoDePropiedadesPhosphorus;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.VistaNodosGraficos;
import com.ag2.presentacion.diseño.NodoGrafico;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class TablaPropiedadesDispositivo extends TableView<PropiedadeNodo> implements VistaNodosGraficos
{
    private ControladorAbstractoAdminNodo controladorAbstractoAdminNodo;
    
    public void addControladorAbstractoAdminNodo(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo)
    {
        this.controladorAbstractoAdminNodo = controladorAbstractoAdminNodo;
    }
  
    public TablaPropiedadesDispositivo()
    {
       
        
        setPrefHeight(200);
        setPrefWidth(500);
        
        TableColumn nombrePropiedadDispositivo = new TableColumn("PROPIEDAD");
        nombrePropiedadDispositivo.setCellValueFactory(new PropertyValueFactory<PropiedadeNodo, String>("nombre"));
        nombrePropiedadDispositivo.setMinWidth(300);
        
        TableColumn valorPropiedadDispositivo = new TableColumn("VALOR");
        valorPropiedadDispositivo.setCellValueFactory(new PropertyValueFactory<PropiedadeNodo, Control>("control"));
        valorPropiedadDispositivo.setMinWidth(200);
        
        TableColumn tituloTblDispositivo = new TableColumn("PROPIEDADES DISPOSITIVO");
        tituloTblDispositivo.getColumns().addAll(nombrePropiedadDispositivo, valorPropiedadDispositivo);

        getColumns().add(tituloTblDispositivo);
     
    }

    public void cargarPropiedades(ArrayList<PropiedadeNodo> propiedadeNodos)
    {
       ObservableList datosPropiedades = FXCollections.observableArrayList();
        for(PropiedadeNodo propiedadeNodo : propiedadeNodos)
        {
           propiedadeNodo.setTablaPropiedadesDispositivo(this);
            datosPropiedades.add(propiedadeNodo);
        }
        setItems(datosPropiedades);       
    }

    public void updatePropiedad( String id, String valor) 
    {
        System.out.println("prop "+valor);
        if(controladorAbstractoAdminNodo!=null)
        {
            controladorAbstractoAdminNodo.updatePropiedad( id, valor);
        }
     }

    
}
