package com.ag2.controller;

import javafx.application.Platform;

public class ResultsController extends ResultsAbstractController {

    @Override
    public void addClientResult(
            final String tcCliente, final String tcPeticionesEnviadas, final String tcTrabajosEnviados, final String tcResultadosRecibidos, final String tcPeticionesFallidas, final String tcPorcentajeResultadosRecibidos) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsPhosphorus.adicionarResultadoCliente(tcCliente, tcPeticionesEnviadas, tcTrabajosEnviados, tcResultadosRecibidos, tcPeticionesFallidas, tcPorcentajeResultadosRecibidos);
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void adicionarResultadoRecurso( 
           final String tcRecurso, 
           final String tcTrabajosRecibidos,
           final String tcTrabajosEnviados,
           final String relativeTrabajosEnviados,
           final String tcFallasEnviadas,
           final String relativeFallasEnviadas,
           final String tcVecesNoCPUFree,           
           final String tcVecesFallasNoEspacio) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                viewResultsPhosphorus.adicionarResultadoRecurso( tcRecurso, 
           tcTrabajosRecibidos,
           tcTrabajosEnviados,
           relativeTrabajosEnviados,
           tcFallasEnviadas,
           relativeFallasEnviadas,
           tcVecesNoCPUFree,           
           tcVecesFallasNoEspacio);
            }
        };
        Platform.runLater(runnable);

    }

    @Override
    public void adicionarResultadoConmutador(
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
                                            final String reltotDrop)
    {
       
          Runnable runnable = new Runnable() {
            @Override
            public void run() {
               viewResultsPhosphorus.adicionarResultadoConmutador(
                       tcConmutador,
                       tcMensajesTrabajoConmutados, 
                       tcMensajesTrabajoNoConmutados, 
                       tcMensajesResultadosConmutados, 
                       tcMensajesResultadosNoConmutados,
                       tcSwitchedJobRequest,
                       tcNonSwitchedJobRequest,
                       relDropJob, relDropRes,relDropReq , reltotDrop);
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void setExecutionPercentage(final double Percentage,final double simulationTime) 
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                viewResultsPhosphorus.setExecutionPercentage(Percentage,simulationTime);
            }
        };

        Platform.runLater(runnable);
    }
}