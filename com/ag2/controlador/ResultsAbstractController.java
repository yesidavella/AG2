/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.controlador;

import com.ag2.modelo.OutputterModel;
import com.ag2.modelo.SimulacionBase;
import com.ag2.presentacion.controles.ViewResultsPhosphorus;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frank
 */
public abstract class ResultsAbstractController  implements Serializable
{
   protected  ViewResultsPhosphorus viewResultsPhosphorus;
   protected  transient OutputterModel outputterModel = new OutputterModel(SimulacionBase.getInstance().getSimulador()); 
    
    public void setViewResultsPhosphorus(ViewResultsPhosphorus viewResultsPhosphorus) 
    {
        this.viewResultsPhosphorus = viewResultsPhosphorus;
    }       
    
   public abstract  void adicionarResultadoCliente(String tcCliente, String tcPeticionesEnviadas , 
            String tcTrabajosEnviados, String tcResultadosRecibidos, String tcPeticionesFallidas, 
            String tcPorcentajeResultadosRecibidos );
   
   public abstract void adicionarResultadoRecurso(String tcRecurso, String tcTrabajosRecibidos,
           String tcFallasNoEspacio, String tcFallasEnviadas ); 
   
   public abstract void adicionarResultadoConmutador(
                                            String tcConmutador,
                                            String tcMensajesTrabajoConmutados,
                                            String tcMensajesTrabajoNoConmutados,
                                            String tcMensajesResultadosConmutados, 
                                            String tcMensajesResultadosNoConmutados,
                                            String relDropJob, 
                                            String relDropRes,
                                            String reltotDrop); 
   
    public abstract void setExecutionPercentage(double Percentage); 
    
    
     private void readObject(ObjectInputStream inputStream) {
        try
        {
            inputStream.defaultReadObject();
            outputterModel = new OutputterModel(SimulacionBase.getInstance().getSimulador()); 
            
        } catch (IOException ex) {
            Logger.getLogger(ResultsAbstractController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ResultsAbstractController.class.getName()).log(Level.SEVERE, null, ex);
        }
     
     }
            
    
    
}
