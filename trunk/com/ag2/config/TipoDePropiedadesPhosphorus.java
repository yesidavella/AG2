package com.ag2.config;

import com.ag2.presentacion.IGU;
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

public enum TipoDePropiedadesPhosphorus {

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

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;

    }
    private String nombrePropiedadPhosphorus;
    private Control control;
    private EditorPropiedades editorPropiedades = EditorPropiedades.getUniqueInstance();

    public String getNombrePropiedad() {
        return this.toString().replace("_", " ");

    }

    private TipoDePropiedadesPhosphorus(String nombre, Control control) {
        nombrePropiedadPhosphorus = nombre;
        this.control = control;
        if (control instanceof TextField) {
            establecerEventoPropiedad((TextField) control);

        } else if (control instanceof CheckBox) {
            establecerEventoPropiedad(((CheckBox) control));

        }
    }

    private void establecerEventoPropiedad(CheckBox checkBox) {

        if (editorPropiedades.getValorPropiedad(this).equalsIgnoreCase("true")) {
            checkBox.setSelected(true);
        }
        checkBox.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent actionEvent) {
                CheckBox checkBox = (CheckBox) actionEvent.getSource();
                if (checkBox.isSelected()) {
                    TipoDePropiedadesPhosphorus.this.escribirPropiedad(Boolean.TRUE.toString());
                } else {
                    TipoDePropiedadesPhosphorus.this.escribirPropiedad(Boolean.FALSE.toString());
                }
            }
        });
    }

    private void establecerEventoPropiedad(final TextField textField) {

        textField.setText(editorPropiedades.getValorPropiedad(this));

        textField.setOnMouseExited(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                IGU.getInstanciaIGUAg2().getGrGrupoDeDiseño().requestFocus();
            }
        });
        
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> textControl, Boolean beforeStateFocus, Boolean currentStateFocus) {

                if (beforeStateFocus == true && currentStateFocus == false) {
                    String valor = textField.getText();
                    TipoDePropiedadesPhosphorus.this.escribirPropiedad(valor);
                }
            }
        });
    }

    public String getNombrePropiedadPhosphorus() {
        return nombrePropiedadPhosphorus;
    }

    public void escribirPropiedad(String valor) {
        editorPropiedades.setValorPropiedad(this, valor);

    }

    public static ObservableList getDatos() {

        ObservableList datosPropiedadesSimulacion = FXCollections.observableArrayList(TipoDePropiedadesPhosphorus.values());
        return datosPropiedadesSimulacion;

    }
}
