/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion;

/**
 *
 * @author Frank
 */
public enum Continentes 
{
    NORTE_AMERICA("Norte America"), CENTRO_AMERICA("Centro America"), SUR_AMERICA("Sur America"),
    EUROPA("Europa"), AFRICA("Africa"), ASIA("Asia"), OCEANIA("Oceania");  
    
        String nombre; 
               
        private Continentes(String nombre)
        {
          this.nombre = nombre; 
        }        
        
        @Override
        public String toString()
        {
            return nombre; 
        }        
    
}
