package com.ag2.presentacion.diseño.propiedades;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.IGU;
import com.ag2.presentacion.VistaNodosGraficos;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import com.ag2.presentacion.diseño.NodoGrafico;
import com.ag2.presentacion.diseño.ObjetoSeleccionable;
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
        TableColumn tbColNombrePropDispositivo = new TableColumn("PROPIEDAD");
        tbColNombrePropDispositivo.setCellValueFactory(new PropertyValueFactory<PropiedadeNodo, String>("nombre"));
        tbColNombrePropDispositivo.setMinWidth(110);
        tbColNombrePropDispositivo.setPrefWidth(175);
        
        TableColumn tbColValorPropDispositivo = new TableColumn("VALOR");
        tbColValorPropDispositivo.setCellValueFactory(new PropertyValueFactory<PropiedadeNodo, Control>("control"));
        tbColValorPropDispositivo.setMinWidth(200);
        tbColValorPropDispositivo.setPrefWidth(215);
        
        TableColumn tbColTituloTbDispositivo = new TableColumn("PROPIEDADES DE DISPOSITIVO SELECCIONADO");
        tbColTituloTbDispositivo.getColumns().addAll(tbColNombrePropDispositivo, tbColValorPropDispositivo);

        getColumns().add(tbColTituloTbDispositivo);
        
        setMinWidth(tbColTituloTbDispositivo.getMinWidth());
        setPrefWidth(435);
        
        setPrefHeight(200);
    }

    public void cargarPropiedades(ArrayList<PropiedadeNodo> propiedadeNodos) {
        
        ObservableList datosPropiedades = FXCollections.observableArrayList();
        
        for(PropiedadeNodo propiedadeNodo : propiedadeNodos){ 
           propiedadeNodo.setTablaPropiedadesDispositivo(this);
           datosPropiedades.add(propiedadeNodo);
        }
        setItems(datosPropiedades);       
    }

    public void updatePropiedad(boolean isSubProperty, String id, String valor) 
    {
        ObjetoSeleccionable objetoSeleccionado = IGU.getInstance().getGrGrupoDeDiseño().getObjetoGraficoSelecionado();
        
        if(objetoSeleccionado != null){
            if (objetoSeleccionado instanceof NodoGrafico) {
                controladorAbstractoAdminNodo.updatePropiedad(isSubProperty,true,id, valor);
            } else if (objetoSeleccionado instanceof EnlaceGrafico) {
                controladorAdminEnlace.updatePropiedad((EnlaceGrafico)objetoSeleccionado,id, valor);
            }
        }
     }

    public void enableDisign() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
