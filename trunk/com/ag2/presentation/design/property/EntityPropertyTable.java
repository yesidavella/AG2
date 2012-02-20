package com.ag2.presentation.design.property;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.presentation.IGU;
import com.ag2.presentation.GraphNodesView;
import com.ag2.presentation.design.GraphLink;
import com.ag2.presentation.design.GraphNode;
import com.ag2.presentation.design.Selectable;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EntityPropertyTable extends TableView<EntityProperty> implements GraphNodesView, Serializable
{
    private NodeAdminAbstractController controladorAbstractoAdminNodo;
    private LinkAdminAbstractController controladorAdminEnlace;

    public void setControladorAbstractoAdminNodo(NodeAdminAbstractController controladorAbstractoAdminNodo)
    {
        this.controladorAbstractoAdminNodo = controladorAbstractoAdminNodo;
    }

    public void setControladorAdminEnlace(LinkAdminAbstractController controladorAdminEnlace){
        this.controladorAdminEnlace = controladorAdminEnlace;
    }

    public EntityPropertyTable()
    {
        TableColumn tbColNombrePropDispositivo = new TableColumn("PROPIEDAD");
        tbColNombrePropDispositivo.setCellValueFactory(new PropertyValueFactory<EntityProperty, String>("nombre"));
        tbColNombrePropDispositivo.setMinWidth(110);
        tbColNombrePropDispositivo.setPrefWidth(175);

        TableColumn tbColValorPropDispositivo = new TableColumn("VALOR");
        tbColValorPropDispositivo.setCellValueFactory(new PropertyValueFactory<EntityProperty, Control>("control"));
        tbColValorPropDispositivo.setMinWidth(200);
        tbColValorPropDispositivo.setPrefWidth(215);

        TableColumn tbColTituloTbDispositivo = new TableColumn("PROPIEDADES DE DISPOSITIVO SELECCIONADO");
        tbColTituloTbDispositivo.getColumns().addAll(tbColNombrePropDispositivo, tbColValorPropDispositivo);

        getColumns().add(tbColTituloTbDispositivo);

        setMinWidth(tbColTituloTbDispositivo.getMinWidth());
        setPrefWidth(435);

        setPrefHeight(200);
    }

    public void loadProperties(ArrayList<EntityProperty> propiedadeNodos) {

        ObservableList datosPropiedades = FXCollections.observableArrayList();

        for(EntityProperty propiedadeNodo : propiedadeNodos){
           propiedadeNodo.setTablaPropiedadesDispositivo(this);
           datosPropiedades.add(propiedadeNodo);
        }
        setItems(datosPropiedades);
    }
    public void clearData()
    {
        ObservableList datosPropiedades = FXCollections.observableArrayList();
         setItems(datosPropiedades);
    }

    public void updateProperty(boolean isSubProperty, String id, String valor)
    {
        Selectable objetoSeleccionado = IGU.getInstance().getGrGrupoDeDiseño().getSelectable();

        if(objetoSeleccionado != null){
            if (objetoSeleccionado instanceof GraphNode) {
                controladorAbstractoAdminNodo.updateProperty(isSubProperty,true,id, valor);
            } else if (objetoSeleccionado instanceof GraphLink) {
                controladorAdminEnlace.updatePropiedad((GraphLink)objetoSeleccionado,id, valor);
            }
        }
     }

    public void enableDisign() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }
}
