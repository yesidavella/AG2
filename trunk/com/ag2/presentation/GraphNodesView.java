
package com.ag2.presentation;

import com.ag2.presentation.design.property.EntityProperty;
import java.util.ArrayList;

public interface GraphNodesView 
{
    public void loadProperties(ArrayList<EntityProperty> entityPropertys ); 
    public void updateProperty(boolean isSubProperty,String id, String value);
    public void enableDisign(); 
    
}
