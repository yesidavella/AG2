package com.ag2.presentacion.diseño;

import com.ag2.controlador.LinkAdminAbstractController;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;

public class OCS_SwicthGraphNode extends SwitchGraphNode{

   private   static short contadorNodo = 0;
    public OCS_SwicthGraphNode(GrupoDeDiseno grupoDeDiseno,ControladorAbstractoAdminNodo controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace) {
        super(grupoDeDiseno,"Enrutador_Optico_"+(++contadorNodo),"../../../../recursos/imagenes/enrutador_optico_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
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
