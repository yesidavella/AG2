package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminNodo;

public  abstract class EnrutadorGrafico extends NodoGrafico{      
    
    public EnrutadorGrafico(String nombre, String urlDeImagen, ControladorAbstractoAdminNodo controladorAbstractoAdminNodo)
    {        
        super(nombre, urlDeImagen, controladorAbstractoAdminNodo);
        centroImagenX = imagen.getHeight()/2  +15; 
        centroImagenY = imagen.getWidth()/2; 
    }    
    
}
