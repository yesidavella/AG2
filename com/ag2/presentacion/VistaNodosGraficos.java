
package com.ag2.presentacion;

import com.ag2.presentacion.diseño.propiedades.PropiedadeNodo;
import java.util.ArrayList;

public interface VistaNodosGraficos 
{
    public void cargarPropiedades(ArrayList<PropiedadeNodo> propiedadeNodos ); 
    public void updatePropiedad(String id, String valor);
    public void enableDisign(); 
    
}
