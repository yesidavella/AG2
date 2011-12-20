
package com.ag2.presentacion;

import com.ag2.presentacion.diseño.NodoGrafico;
import com.ag2.presentacion.diseño.propiedades.PropiedadeNodo;
import java.util.ArrayList;

public interface VistaNodosGraficos 
{
    public void cargarPropiedades(ArrayList<PropiedadeNodo> propiedadeNodos ); 
    public void updatePropiedad(NodoGrafico nodoGrafico,String id, String valor);
    
}
