/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion.controles;

/**
 *
 * @author Frank
 */
public interface  ViewResultsPhosphorus
{
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
   
   
    
}
