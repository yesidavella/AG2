package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;

public class EnrutadorHibridoGrafico extends EnrutadorGrafico{

    private static short contadorNodo = 0;
    
    public EnrutadorHibridoGrafico(GrupoDeDiseno grupoDeDiseno, ControladorAbstractoAdminNodo controladorAbstractoAdminNodo,ControladorAbstractoAdminEnlace ctrlAbsAdminEnlace) 
    {
        super(grupoDeDiseno,"Enrutador_Hibrido_"+(++contadorNodo) ,"../../../../recursos/imagenes/enrutador_hibrido_mapa.png", controladorAbstractoAdminNodo,ctrlAbsAdminEnlace);
        
        if(contadorNodo>9){
            setAncho((short)46);
        }
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
