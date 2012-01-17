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

/**
 *
 * @author Frank
 */
public class PropiedadeNodo {

    private void establecerEventoOnKeyTyped(final String id) 
    {
        final TextField textField = ((TextField) control); 
//        textField.setOnKeyTyped(new EventHandler<KeyEvent>()
//        {
//            public void handle(KeyEvent keyEvent) 
//            {
////                TextField textField = (TextField) keyEvent.getSource();
////                tablaPropiedadesDispositivo.updatePropiedad(id, textField.getText() + keyEvent.getCharacter());
//            }
//        });
//        
//        textField.setOnKeyPressed(new EventHandler<KeyEvent>()
//        {
//            public void handle(KeyEvent keyEvent)
//            {             
//                if (keyEvent.getCode().equals(KeyCode.DELETE)) 
//                {                                   
////                    String valor = ((TextField) keyEvent.getSource()).getText();
////                    tablaPropiedadesDispositivo.updatePropiedad(id, valor);
//                }
//            }
//        });
        
        textField.setOnMouseExited(new EventHandler<MouseEvent>(){

            public void handle(MouseEvent event) {
                IGU.getInstanciaIGUAg2().getGrGrupoDeDiseño().requestFocus();
            }
        });
        textField.focusedProperty().addListener(new ChangeListener<Boolean>(){

            public void changed(ObservableValue<? extends Boolean> textControl, Boolean beforeStateFocus, Boolean currentStateFocus) {
                
                if(beforeStateFocus==true && currentStateFocus==false){
                    tablaPropiedadesDispositivo.updatePropiedad(id, textField.getText());
                }
            }
        });
    }

    public enum TipoDePropiedadNodo 
    {
        TEXTO, NUMERO, BOLEANO, LISTA_TEXTO, ETIQUETA
    };
    private String id;
    private String nombre;
    private ArrayList<String> valorArrayList = new ArrayList<String>();
    private TipoDePropiedadNodo tipoDePropiedadNodo;
    protected Control control;
    private TablaPropiedadesDispositivo tablaPropiedadesDispositivo;

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public PropiedadeNodo(final String id, final String nombre, TipoDePropiedadNodo tipoDePropiedadNodo) 
    {
        
        this.nombre = nombre;
        this.tipoDePropiedadNodo = tipoDePropiedadNodo;
        this.id = id;

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
                        tablaPropiedadesDispositivo.updatePropiedad(id,nombre+valor );
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
                            tablaPropiedadesDispositivo.updatePropiedad(id, choiceBox.getSelectionModel().getSelectedItem().toString());
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
}