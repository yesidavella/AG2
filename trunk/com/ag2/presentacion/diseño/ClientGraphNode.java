package com.ag2.presentacion.diseño;

import com.ag2.controlador.LinkAdminAbstractController;
import com.ag2.controlador.NodeAdminAbstractController;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;

public class ClientGraphNode extends GraphNode{

    private static int contadorNodo = 0;
  
    
    public ClientGraphNode(GrupoDeDiseno grupoDeDiseno, NodeAdminAbstractController controladorAbstractoAdminNodo,LinkAdminAbstractController ctrlAbsAdminEnlace)
    {
        
        super(grupoDeDiseno,"Cliente_"+(++contadorNodo), "../../../../recursos/imagenes/cliente_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        
        setAlto((short)50);

        if(contadorNodo<10){
            setAncho((short)40);
        }else{
            setAncho((short)44);
        }
        
        pasoDeSaltoLinea = 9;
    }

    @Override
    public boolean puedeGenerarEnlaceCon(GraphNode nodoInicioDelEnlace) {
        return (nodoInicioDelEnlace instanceof SwitchGraphNode) && getCantidadDeEnlaces()<1;
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