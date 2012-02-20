package com.ag2.presentation.design.property;

import com.ag2.presentation.GUI;
import com.ag2.presentation.design.property.EntityProperty.PropertyType;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class EntityProperty {

    public enum PropertyType
    {
        TEXT, NUMBER, BOOLEAN, TEXT_LIST, LABEL
    };
    private String id;
    private String name;
    private ArrayList<String> valueArrayList = new ArrayList<String>();
    private PropertyType propertyType;
    protected transient Control control;
    private EntityPropertyTable entityPropertyTable;
    private boolean subProperty = false;

    public void setDisable(boolean  isDisable)
    {
        control.setDisable(isDisable);
    }

    public boolean isSubProperty() {
        return subProperty;
    }

    public void setSubProperty(boolean subProperty) {
        this.subProperty = subProperty;
    }

    public EntityProperty(final String id, final String name, PropertyType propertyType, boolean isSubProperty)
    {

        this.name = name;
        this.propertyType = propertyType;
        this.id = id;
        this.subProperty = isSubProperty;

        switch (propertyType)
        {
            case BOOLEAN: {
                control = new CheckBox();
                ((CheckBox) control).setOnAction(new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent actionEvent)
                    {
                        CheckBox checkBox = (CheckBox)actionEvent.getSource();

                        String valor="_ON";
                        if(!checkBox.isSelected())
                        {
                            valor="_OFF";
                        }
                        entityPropertyTable.updateProperty(subProperty,id+"_"+name,name+valor );
                    }
                });
                break;
            }
            case TEXT_LIST: {
                control = new ChoiceBox<String>();
                ((ChoiceBox) control).getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

                    public void changed(ObservableValue ov, Object t, Object t1) {
                        ChoiceBox choiceBox = (ChoiceBox) control;
                        choiceBox.getSelectionModel().getSelectedItem().toString();
                        if (entityPropertyTable != null) {
                            entityPropertyTable.updateProperty(subProperty, id, choiceBox.getSelectionModel().getSelectedItem().toString());
                        }
                    }
                });
                break;
            }
            case LABEL: {
                control = new Label();
                break;
            }
            case NUMBER:case TEXT : {
                control = new TextField();
                establishEventOnKeyTyped(id);
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public ArrayList<String> getValueArrayList() {
        return valueArrayList;
    }

    public void setFirstValue(String value) {

        switch (propertyType)
        {
            case BOOLEAN:
            {
                if (value.equalsIgnoreCase("true")) {
                    ((CheckBox) control).setSelected(true);
                }
                break;
            }
            case LABEL:
                ((Label)control).setText(value);
                break;
            case NUMBER:
            case TEXT:
            {
                ((TextField) control).setText(value);
                break;
            }
        }
        valueArrayList.add(0, value);
    }

    public String getFirstValue() {
        return valueArrayList.get(0);
    }

    public EntityPropertyTable getEntityPropertyTable() {
        return entityPropertyTable;
    }

    public void setEntityPropertyTable(EntityPropertyTable entityPropertyTable) {
        this.entityPropertyTable = entityPropertyTable;
    }

    private void establishEventOnKeyTyped(final String id) {
        final TextField textField = ((TextField) control);

        textField.setOnMouseExited(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                GUI.getInstance().getGraphDesignGroup().getGroup().requestFocus();
            }
        });
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> textControl, Boolean beforeStateFocus, Boolean currentStateFocus) {

                if (beforeStateFocus == true && currentStateFocus == false) {
                    entityPropertyTable.updateProperty(subProperty, id, textField.getText());
                }
            }
        });
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

}