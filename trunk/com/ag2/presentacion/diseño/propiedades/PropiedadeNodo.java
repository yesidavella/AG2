package com.ag2.presentacion.diseño.propiedades;

import com.ag2.presentacion.IGU;
import com.ag2.presentacion.diseño.propiedades.PropiedadeNodo.TipoDePropiedadNodo;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class PropiedadeNodo {

    public enum TipoDePropiedadNodo 
    {
        TEXTO, NUMERO, BOLEANO, LISTA_TEXTO, ETIQUETA
    };
    private String id;
    private String nombre;
    private ArrayList<String> valorArrayList = new ArrayList<String>();
    private TipoDePropiedadNodo tipoDePropiedadNodo;
    protected transient Control control;
    private TablaPropiedadesDispositivo tablaPropiedadesDispositivo;
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

    public PropiedadeNodo(final String id, final String nombre, TipoDePropiedadNodo tipoDePropiedadNodo, boolean isSubProperty) 
    {
        
        this.nombre = nombre;
        this.tipoDePropiedadNodo = tipoDePropiedadNodo;
        this.id = id;
        this.subProperty = isSubProperty; 

        switch (tipoDePropiedadNodo) 
        {
            case BOLEANO: {
                control = new CheckBox();
                ((CheckBox) control).setOnAction(new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent actionEvent) 
                    {
                        CheckBox checkBox = (CheckBox)actionEvent.getSource(); 
                      
                        String valor="_ON"; 
                        if(!checkBox.isSelected())
                        {    
                            valor="OFF";                             
                        }
                        tablaPropiedadesDispositivo.updatePropiedad(subProperty,id+"_"+nombre,nombre+valor );
                        checkBox.setDisable(true);
                    }
                });
                break;
            }
            case LISTA_TEXTO: {
                control = new ChoiceBox<String>();
                ((ChoiceBox) control).getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

                    public void changed(ObservableValue ov, Object t, Object t1) {
                        ChoiceBox choiceBox = (ChoiceBox) control;
                        choiceBox.getSelectionModel().getSelectedItem().toString();
                        if (tablaPropiedadesDispositivo != null) {
                            tablaPropiedadesDispositivo.updatePropiedad(subProperty, id, choiceBox.getSelectionModel().getSelectedItem().toString());
                        }
                    }
                });
                break;
            }
            case ETIQUETA: {
                control = new Label();
                break;
            }
            case NUMERO:case TEXTO : {
                control = new TextField();
                establecerEventoOnKeyTyped(id);
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getValorArrayList() {
        return valorArrayList;
    }

    public void setPrimerValor(String valor) {

        switch (tipoDePropiedadNodo) 
        {
            case BOLEANO:
            {
                if (valor.equalsIgnoreCase("true")) {
                    ((CheckBox) control).setSelected(true);
                }
                break;
            }
            case ETIQUETA:
                ((Label)control).setText(valor);
                break;
            case NUMERO:
            case TEXTO:
            {
                ((TextField) control).setText(valor);
                break;
            }
        }
        valorArrayList.add(0, valor);
    }

    public String setPrimerValor() {
        return valorArrayList.get(0);
    }

    public TablaPropiedadesDispositivo getTablaPropiedadesDispositivo() {
        return tablaPropiedadesDispositivo;
    }

    public void setTablaPropiedadesDispositivo(TablaPropiedadesDispositivo tablaPropiedadesDispositivo) {
        this.tablaPropiedadesDispositivo = tablaPropiedadesDispositivo;
    }
    
    private void establecerEventoOnKeyTyped(final String id) {
        final TextField textField = ((TextField) control);

        textField.setOnMouseExited(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                IGU.getInstance().getGrGrupoDeDiseño().requestFocus();
            }
        });
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> textControl, Boolean beforeStateFocus, Boolean currentStateFocus) {

                if (beforeStateFocus == true && currentStateFocus == false) {
                    tablaPropiedadesDispositivo.updatePropiedad(subProperty, id, textField.getText());
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