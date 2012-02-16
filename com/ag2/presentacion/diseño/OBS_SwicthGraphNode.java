package com.ag2.presentacion.diseño;

import com.ag2.controlador.LinkAdminAbstractController;
import com.ag2.controlador.NodeAdminAbstractController;
import java.io.ObjectInputStream;

public class OBS_SwicthGraphNode extends SwitchGraphNode
{
    private   static short contadorNodo = 0;
    public OBS_SwicthGraphNode(GraphDesignGroup grupoDeDiseno,NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace) {
        super(grupoDeDiseno,"Enrutador_Rafaga_"+(++contadorNodo), "../../../../recursos/imagenes/enrutador_rafaga_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
    }
    private void readObject(ObjectInputStream inputStream)
    {
        try
        {
           inputStream.defaultReadObject();
           contadorNodo++; 
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

}
