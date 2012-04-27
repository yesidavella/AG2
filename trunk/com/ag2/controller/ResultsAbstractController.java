package com.ag2.controller;

import com.ag2.model.OutputterModel;
import com.ag2.model.SimulationBase;
import com.ag2.presentation.control.ViewResultsPhosphorus;
import java.io.Serializable;

public abstract class ResultsAbstractController  implements Serializable
{
   protected  ViewResultsPhosphorus viewResultsPhosphorus;
   protected  OutputterModel outputterModel = new OutputterModel(SimulationBase.getInstance().getGridSimulatorModel());

    public void setViewResultsPhosphorus(ViewResultsPhosphorus viewResultsPhosphorus)
    {
        this.viewResultsPhosphorus = viewResultsPhosphorus;
    }

   public abstract  void addClientResult(String tcCliente, String tcPeticionesEnviadas,
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
           String tcVecesFallasNoEspacio );

   public abstract void adicionarResultadoConmutador(
                                            final String tcConmutador,
                                            final String tcMensajesTrabajoConmutados,
                                            final String tcMensajesTrabajoNoConmutados,
                                            final String tcMensajesResultadosConmutados,
                                            final String tcMensajesResultadosNoConmutados,
                                            final String tcSwitchedJobRequest ,
                                            final String tcNonSwitchedJobRequest ,
                                            final String relDropJob,
                                            final String relDropRes,
                                            final String relDropReq,
                                            final String reltotDrop);

    public abstract void setExecutionPercentage(double Percentage,double simulationTime);
}
