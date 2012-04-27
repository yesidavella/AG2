
package com.ag2.presentation.control;

public interface  ViewResultsPhosphorus {
    
   public abstract  void adicionarResultadoCliente(String tcCliente, String tcPeticionesEnviadas ,
            String tcTrabajosEnviados, String tcResultadosRecibidos, String tcPeticionesFallidas,
            String tcPorcentajeResultadosRecibidos );

   public abstract void adicionarResultadoRecurso(
           String tcRecurso, 
           String tcTrabajosRecibidos,
           String tcTrabajosEnviados,
           String relativeTrabajosEnviados,
           String tcFallasEnviadas,
           String relativeFallasEnviadas,
           String tcVecesNoCPUFree,           
           String tcVecesFallasNoEspacio);       
        
         
           

   

   public abstract void adicionarResultadoConmutador(
                                             String tcConmutador,
                                             String tcMensajesTrabajoConmutados,
                                             String tcMensajesTrabajoNoConmutados,
                                             String tcMensajesResultadosConmutados,
                                             String tcMensajesResultadosNoConmutados,
                                             String tcSwitchedJobRequest ,
                                             String tcNonSwitchedJobRequest ,
                                             String relDropJob,
                                             String relDropRes,
                                             String relDropReq,
                                             String reltotDrop);

   public void setExecutionPercentage(double Percentage,double simulationTime);
}
