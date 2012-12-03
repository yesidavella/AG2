package edu.ag2.presentation.design.property;

import edu.ag2.controller.LinkAdminAbstractController;
import edu.ag2.controller.NodeAdminAbstractController;
import edu.ag2.presentation.GUI;
import edu.ag2.presentation.GraphNodesView;
import edu.ag2.presentation.design.GraphLink;
import edu.ag2.presentation.design.GraphNode;
import edu.ag2.presentation.design.Selectable;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EntityPropertyTableView extends TableView<EntityProperty> implements GraphNodesView {

    private NodeAdminAbstractController nodeAdminCtrl;
    private LinkAdminAbstractController linkAdminCtrl;

    public EntityPropertyTableView() {

        TableColumn propertyTableColumn = new TableColumn("PROPIEDAD");
        TableColumn valueTableColumn = new TableColumn("VALOR");

        propertyTableColumn.setCellValueFactory(new PropertyValueFactory<EntityProperty, String>("name"));
        valueTableColumn.setCellValueFactory(new PropertyValueFactory<EntityProperty, Control>("control"));

        propertyTableColumn.setSortable(false);
        valueTableColumn.setSortable(false);

        propertyTableColumn.setMinWidth(185);
        propertyTableColumn.setPrefWidth(185);
        valueTableColumn.setMinWidth(170);
        valueTableColumn.setPrefWidth(170);

        getColumns().addAll(propertyTableColumn, valueTableColumn);
    }

    public void setControladorAbstractoAdminNodo(NodeAdminAbstractController nodeAdminAbstractController) {
        this.nodeAdminCtrl = nodeAdminAbstractController;
    }

    public void setLinkAdminAbstractController(LinkAdminAbstractController linkAdminAbstractController) {
        this.linkAdminCtrl = linkAdminAbstractController;
    }

    @Override
    public void loadProperties(ArrayList<EntityProperty> entityProperties) {

        TableColumn propertyTableColumn = new TableColumn("PROPIEDAD");
        TableColumn valueTableColumn = new TableColumn("VALOR");
        getColumns().clear();
        getColumns().addAll(propertyTableColumn, valueTableColumn);

        propertyTableColumn.setMinWidth(185);
        propertyTableColumn.setPrefWidth(185);
        valueTableColumn.setMinWidth(170);
        valueTableColumn.setPrefWidth(170);

        propertyTableColumn.setCellValueFactory(new PropertyValueFactory<EntityProperty, String>("name"));
        valueTableColumn.setCellValueFactory(new PropertyValueFactory<EntityProperty, Control>("control"));

        propertyTableColumn.setSortable(false);
        valueTableColumn.setSortable(false);

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
                nodeAdminCtrl.updateProperty(isSubProperty, true, id, value);
            } else if (selectable instanceof GraphLink) {
                linkAdminCtrl.updatePropiedad((GraphLink) selectable, id, value);
            }
        }
    }

    public void enableDisign() {
    }
}