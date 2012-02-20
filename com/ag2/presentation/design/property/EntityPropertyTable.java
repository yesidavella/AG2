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
    private NodeAdminAbstractController nodeAdminAbstractController;
    private LinkAdminAbstractController linkAdminAbstractController;


     public EntityPropertyTable()
    {
        TableColumn propertyTableColumn = new TableColumn("PROPIEDAD");
        propertyTableColumn.setCellValueFactory(new PropertyValueFactory<EntityProperty, String>("nombre"));
        propertyTableColumn.setMinWidth(110);
        propertyTableColumn.setPrefWidth(175);

        TableColumn valueTableColumn = new TableColumn("VALOR");
        valueTableColumn.setCellValueFactory(new PropertyValueFactory<EntityProperty, Control>("control"));
        valueTableColumn.setMinWidth(200);
        valueTableColumn.setPrefWidth(215);

        TableColumn titleTableColumn = new TableColumn("PROPIEDADES DE DISPOSITIVO SELECCIONADO");
        titleTableColumn.getColumns().addAll(propertyTableColumn, valueTableColumn);

        getColumns().add(titleTableColumn);

        setMinWidth(titleTableColumn.getMinWidth());
        setPrefWidth(435);

        setPrefHeight(200);
    }

    public void setControladorAbstractoAdminNodo(NodeAdminAbstractController nodeAdminAbstractController)
    {
        this.nodeAdminAbstractController = nodeAdminAbstractController;
    }

    public void setLinkAdminAbstractController(LinkAdminAbstractController linkAdminAbstractController){
        this.linkAdminAbstractController = linkAdminAbstractController;
    }



    public void loadProperties(ArrayList<EntityProperty> entityPropertys) {

        ObservableList dataObservableList = FXCollections.observableArrayList();

        for(EntityProperty entityProperty : entityPropertys){
           entityProperty.setEntityPropertyTable(this);
           dataObservableList.add(entityProperty);
        }
        setItems(dataObservableList);
    }
    public void clearData()
    {
        ObservableList dataObservableList = FXCollections.observableArrayList();
         setItems(dataObservableList);
    }

    public void updateProperty(boolean isSubProperty, String id, String value)
    {
        Selectable selectable = IGU.getInstance().getGraphDesignGroup().getSelectable();

        if(selectable != null){
            if (selectable instanceof GraphNode) {
                nodeAdminAbstractController.updateProperty(isSubProperty,true,id, value);
            } else if (selectable instanceof GraphLink) {
                linkAdminAbstractController.updatePropiedad((GraphLink)selectable,id, value);
            }
        }
     }

    public void enableDisign() {
    }
}
