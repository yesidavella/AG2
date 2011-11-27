/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion;

import javafx.scene.control.CheckBox;

/**
 *
 * @author CAMACHO ROLDAN
 */
public class ObjetoConPropiedades {
    
    private String propiedad1;
    private CheckBox propiedad2;

    /**
     * Get the value of propiedad2
     *
     * @return the value of propiedad2
     */
    
    public ObjetoConPropiedades(String propiedad1,CheckBox propiedad2) {
        this.propiedad1 = propiedad1;
        this.propiedad2 = propiedad2;
    }
    
    public CheckBox getPropiedad2() {
        return propiedad2;
    }

    /**
     * Set the value of propiedad2
     *
     * @param propiedad2 new value of propiedad2
     */
    public void setPropiedad2(CheckBox propiedad2) {
        this.propiedad2 = propiedad2;
    }

    /**
     * Get the value of Propiedad
     *
     * @return the value of Propiedad
     */
    
    
    public String getPropiedad1() {
        return propiedad1;
    }

    /**
     * Set the value of Propiedad
     *
     * @param Propiedad new value of Propiedad
     */
    public void setPropiedad1(String propiedad1) {
        this.propiedad1 = propiedad1;
    }

}
