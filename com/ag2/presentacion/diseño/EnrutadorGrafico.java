package com.ag2.presentacion.diseño;

public  abstract class EnrutadorGrafico extends NodoGrafico{      
    
    public EnrutadorGrafico(String nombre, String urlDeImagen)
    {        
        super(nombre, urlDeImagen);
        centroImagenX = imagen.getHeight()/2  +15; 
        centroImagenY = imagen.getWidth()/2; 
    }    
    
}
