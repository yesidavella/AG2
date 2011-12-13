/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion.diseño.propiedades;

import com.ag2.presentacion.Continentes;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;

/**
 *
 * @author Frank
 */
public class PropiedadNodoDistribuciones  extends PropiedadeNodo
{
    public enum TipoDeDistribucion
    {CONSTANT("Constante"),
    ER_LANG("Erlang"),
    HYPER_EXPONENTIAL("Hiper-exponencial"), 
    NEGATIVE_EXPONENTIAL("Exponencial-negativa"),
    NORMAL("Normal"),
    POISSON_PROCESS("Possion"),
    UNMIFORM("Uniforme");

        private String nombre; 
        
        private TipoDeDistribucion(String nombre)
        {
            this.nombre= nombre; 
        }   
        @Override
        public String toString() {
            return nombre.toString();
        }
    }; 
    
    public PropiedadNodoDistribuciones(String nombre)
    {
        super(nombre, TipoDePropiedadNodo.LISTA_TEXTO); 
        ((ChoiceBox)control).setItems(FXCollections.observableArrayList(TipoDeDistribucion.values()));           
    }
     public void setPrimerValor(TipoDeDistribucion tipoDeDistribucion) 
    {
            ((ChoiceBox)control).getSelectionModel().select(tipoDeDistribucion);  
    }
    
}
