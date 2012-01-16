package com.ag2.presentacion.diseño.propiedades;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.VistaNodosGraficos;
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
    private ControladorAbstractoAdminEnlace controladorAdminEnlace;
    
    public void addControladorAbstractoAdminNodo(ControladorAbstractoAdminNodo controladorAbstractoAdminNodo)
    {
        this.controladorAbstractoAdminNodo = controladorAbstractoAdminNodo;
    }
    
    public void addControladorAdminEnlace(ControladorAbstractoAdminEnlace controladorAdminEnlace){
        this.controladorAdminEnlace = controladorAdminEnlace;
    }    
  
    public TablaPropiedadesDispositivo()
    {
        setPrefHeight(200);
        setPrefWidth(500);
        
        TableColumn nombrePropiedadDispositivo = new TableColumn("PROPIEDAD");
        nombrePropiedadDispositivo.setCellValueFactory(new PropertyValueFactory<PropiedadeNodo, String>("nombre"));
        nombrePropiedadDispositivo.setMinWidth(150);
        nombrePropiedadDispositivo.setPrefWidth(150);
        
        TableColumn valorPropiedadDispositivo = new TableColumn("VALOR");
        valorPropiedadDispositivo.setCellValueFactory(new PropertyValueFactory<PropiedadeNodo, Control>("control"));
        valorPropiedadDispositivo.setMinWidth(200);
        valorPropiedadDispositivo.setPrefWidth(250);
        
        TableColumn tituloTblDispositivo = new TableColumn("PROPIEDADES DE DISPOSITIVO SELECCIONADO");
        tituloTblDispositivo.getColumns().addAll(nombrePropiedadDispositivo, valorPropiedadDispositivo);

        getColumns().add(tituloTblDispositivo);
    }

    public void cargarPropiedades(ArrayList<PropiedadeNodo> propiedadeNodos) {
        
        ObservableList datosPropiedades = FXCollections.observableArrayList();
        
        for(PropiedadeNodo propiedadeNodo : propiedadeNodos){ 
           propiedadeNodo.setTablaPropiedadesDispositivo(this);
           datosPropiedades.add(propiedadeNodo);
        }
        setItems(datosPropiedades);       
    }

    public void updatePropiedad( String id, String valor) 
    {
       
        if(controladorAbstractoAdminNodo!=null)
        {
            controladorAbstractoAdminNodo.updatePropiedad( id, valor);
        }
     }
    
}
