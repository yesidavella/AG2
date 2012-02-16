
package com.ag2.presentacion;

import com.ag2.presentacion.diseño.propiedades.EntityProperty;
import java.util.ArrayList;

public interface GraphNodesView 
{
    public void loadProperties(ArrayList<EntityProperty> entityPropertys ); 
    public void updateProperty(boolean isSubProperty,String id, String value);
    public void enableDisign(); 
    
}
