package com.ag2.presentacion.diseño;

import com.ag2.controlador.AbsControllerAdminLink;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;

public class OBS_SwicthGraphNode extends SwitchGraphNode
{
    private   static short contadorNodo = 0;
    public OBS_SwicthGraphNode(GrupoDeDiseno grupoDeDiseno,ControladorAbstractoAdminNodo controladorAbstractoAdminNodo,AbsControllerAdminLink ctrlAbsAdminEnlace) {
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
