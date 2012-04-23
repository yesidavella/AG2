package com.ag2.presentation.design.property;

import com.ag2.controller.LinkAdminAbstractController;
import com.ag2.controller.NodeAdminAbstractController;
import com.ag2.presentation.GUI;
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

public class EntityPropertyTableView extends TableView<EntityProperty> implements GraphNodesView, Serializable {

    private NodeAdminAbstractController nodeAdminAbstractController;
    private LinkAdminAbstractController linkAdminAbstractController;

    public EntityPropertyTableView() {
        TableColumn propertyTableColumn = new TableColumn("PROPIEDAD");
        propertyTableColumn.setCellValueFactory(new PropertyValueFactory<EntityProperty, String>("name"));
        propertyTableColumn.setMinWidth(200);
        propertyTableColumn.setPrefWidth(200);

        TableColumn valueTableColumn = new TableColumn("VALOR");
        valueTableColumn.setCellValueFactory(new PropertyValueFactory<EntityProperty, Control>("control"));
        valueTableColumn.setMinWidth(160);
        valueTableColumn.setPrefWidth(160);
        

      
        

        getColumns().addAll(propertyTableColumn, valueTableColumn );

       
        
    }

    public void setControladorAbstractoAdminNodo(NodeAdminAbstractController nodeAdminAbstractController) {
        this.nodeAdminAbstractController = nodeAdminAbstractController;
    }

    public void setLinkAdminAbstractController(LinkAdminAbstractController linkAdminAbstractController) {
        this.linkAdminAbstractController = linkAdminAbstractController;
    }

    @Override
    public void loadProperties(ArrayList<EntityProperty> entityProperties) {

        ObservableList dataObservableList = FXCollections.observableArrayList();

        for (EntityProperty entityProperty : entityProperties) {
            entityProperty.setEntityPropertyTable(this);
            dataObservableList.add(entityProperty);
        }
        setItems(dataObservableList);
    }

    public void clearData() {
        ObservableList dataObservableList = FXCollections.observableArrayList();
        setItems(dataObservableList);
    }

    @Override
    public void updateProperty(boolean isSubProperty, String id, String value) {
        Selectable selectable = GUI.getInstance().getGraphDesignGroup().getSelectable();

        if (selectable != null) {
            if (selectable instanceof GraphNode) {
                nodeAdminAbstractController.updateProperty(isSubProperty, true, id, value);
            } else if (selectable instanceof GraphLink) {
                linkAdminAbstractController.updatePropiedad((GraphLink) selectable, id, value);
            }
        }
    }

    public void enableDisign() {
    }
}