package com.ag2.presentacion.diseño;

import com.ag2.controlador.AbsControllerAdminLink;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;

public class ResourceGraphNode extends GraphNode{

    private   static short contadorNodo = 0;
    public ResourceGraphNode(GrupoDeDiseno grupoDeDiseno,ControladorAbstractoAdminNodo controladorAbstractoAdminNodo,AbsControllerAdminLink ctrlAbsAdminEnlace) {
        super(grupoDeDiseno, "Cluster_"+(++contadorNodo),"../../../../recursos/imagenes/recurso_cursor_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        setAlto((short)67);
        setAncho((short)49);
                
        pasoDeSaltoLinea = 11;
    }

    @Override
    public boolean puedeGenerarEnlaceCon(GraphNode nodoComienzoEnlace) {
        return (nodoComienzoEnlace instanceof SwitchGraphNode) ;
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
