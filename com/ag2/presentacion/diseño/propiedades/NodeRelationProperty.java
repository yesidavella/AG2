/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion.diseño.propiedades;

import com.ag2.presentacion.diseño.GraphNode;
import java.io.Serializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author Frank
 */
public class NodeRelationProperty extends EntityProperty implements Serializable{

    private transient ObservableList observableListNodes = FXCollections.observableArrayList();
    private String nombre;
    
    public NodeRelationProperty(String id, String nombre) {
        super(id, nombre, TipoDePropiedadNodo.LISTA_TEXTO, false);        
        ChoiceBox  choiceBox =  ((ChoiceBox) control); 
        choiceBox.setItems(observableListNodes);            
    }
    
    public String toString() {
        return nombre.toString();
    }

    public ObservableList getObservableListNodes() 
    {
        return observableListNodes;
    }
    
    public void setPrimerValor(GraphNode nodoGrafico){
         ((ChoiceBox) control).getSelectionModel().select(nodoGrafico);
    }
}
