package com.ag2.config;

import com.ag2.controller.ExecuteController;
import com.ag2.presentation.IGU;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public enum PropertyPhosphorusTypeEnum {

    SIMULATION_TIME("simulationTime", new TextField()),
    OUTPUT("output", new CheckBox()),
    STOP_EVENT_OFF_SETTIME("stopEventOffSetTime", new TextField()),
    DEFAULT_LINK_SPEED("defaultLinkSpeed", new TextField()),
    DEFAULT_WAVELENGTHS("defaultWavelengths", new TextField()),
    ACK_SIZE("ACKsize", new TextField()),
    OBS_HANDLE_TIME("OBSHandleTime", new TextField()),
    DEFAULT_CAPACITY("defaultCapacity", new TextField()),
    DEFAULT_CPU_COUNT("defaultCPUCount", new TextField()),
    DEFAULT_QUEUE_SIZE("defaultQueueSize", new TextField()),
    DEFAULT_FLOP_SIZE("defaultFlopSize", new TextField()),
    DEFAULT_DATA_SIZE("defaultDataSize", new TextField()),
    DEFAULT_JOB_IAT("defaultJobIAT", new TextField()),
    MAX_DELAY("maxDelay", new TextField()),
    SWITCHING_SPEED("switchingSpeed", new TextField()),
    LINK_SPEED("linkSpeed", new TextField()),
    OUTPUT_FILE_NAME("outputFileName", new TextField());

    private ExecuteController executeController;
    private String phosphorusPropertyName;
    private Control control;
    private PhosphorusPropertyEditor phosphorusPropertyEditor = PhosphorusPropertyEditor.getUniqueInstance();

    
    private PropertyPhosphorusTypeEnum(String nombre, Control control) {
        phosphorusPropertyName = nombre;
        this.control = control;
        if (control instanceof TextField) {
            setPropertyEvent((TextField) control);

        } else if (control instanceof CheckBox) {
            setEventProperty(((CheckBox) control));

        }
    }
    public void setExecuteController(ExecuteController executeController) {
        this.executeController = executeController;
    }
    
    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;

    }
    

    public String getPropertyName() {
        return this.toString().replace("_", " ");

    }

    

    private void setEventProperty(CheckBox checkBox) {

        if (phosphorusPropertyEditor.getPropertyValue(this).equalsIgnoreCase("true")) {
            checkBox.setSelected(true);
        }
        checkBox.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent actionEvent) {
                CheckBox checkBox = (CheckBox) actionEvent.getSource();
                if (checkBox.isSelected()) {
                    PropertyPhosphorusTypeEnum.this.writeProperty(Boolean.TRUE.toString());
                } else {
                    PropertyPhosphorusTypeEnum.this.writeProperty(Boolean.FALSE.toString());
                }
            }
        });
    }

    private void setPropertyEvent(final TextField textField) {

        textField.setText(phosphorusPropertyEditor.getPropertyValue(this));

        textField.setOnMouseExited(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                IGU.getInstance().getGrGrupoDeDiseño().getGroup().requestFocus();
            }
        });
        
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> textControl, Boolean beforeStateFocus, Boolean currentStateFocus) {

                if (beforeStateFocus == true && currentStateFocus == false) {
                    String value = textField.getText();
                    PropertyPhosphorusTypeEnum.this.writeProperty(value);
                    executeController.reLoadConfigFile();
                }
            }
        });
    }

    public String getNombrePropiedadPhosphorus() {
        return phosphorusPropertyName;
    }

    public void writeProperty(String valor) {
        phosphorusPropertyEditor.setPropertyValue(this, valor);

    }

    public static ObservableList getData(ExecuteController executeController) 
    {
        for(PropertyPhosphorusTypeEnum propertyPhosphorusTypeEnum: values())
        {
            propertyPhosphorusTypeEnum.setExecuteController(executeController);
        }

        ObservableList observableList = FXCollections.observableArrayList(PropertyPhosphorusTypeEnum.values());
        return observableList;

    }
}
