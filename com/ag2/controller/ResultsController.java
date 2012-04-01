package com.ag2.controller;

import javafx.application.Platform;

public class ResultsController extends ResultsAbstractController {

    @Override
    public void addClientResult(final String tcCliente, final String tcPeticionesEnviadas, final String tcTrabajosEnviados, final String tcResultadosRecibidos, final String tcPeticionesFallidas, final String tcPorcentajeResultadosRecibidos) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                viewResultsPhosphorus.adicionarResultadoCliente(tcCliente, tcPeticionesEnviadas, tcTrabajosEnviados, tcResultadosRecibidos, tcPeticionesFallidas, tcPorcentajeResultadosRecibidos);
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void adicionarResultadoRecurso(final String tcRecurso, final String tcTrabajosRecibidos, final String tcFallasNoEspacio, final String tcFallasEnviadas) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                viewResultsPhosphorus.adicionarResultadoRecurso(tcRecurso, tcTrabajosRecibidos, tcFallasNoEspacio, tcFallasEnviadas);
            }
        };
        Platform.runLater(runnable);

    }

    @Override
    public void adicionarResultadoConmutador(final String tcConmutador, final String tcMensajesTrabajoConmutados, final String tcMensajesTrabajoNoConmutados, final String tcMensajesResultadosConmutados, final String tcMensajesResultadosNoConmutados, final String relDropJob, final String relDropRes, final String reltotDrop) {
       
          Runnable runnable = new Runnable() {
            @Override
            public void run() {
               viewResultsPhosphorus.adicionarResultadoConmutador(tcConmutador, tcMensajesTrabajoConmutados, tcMensajesTrabajoNoConmutados, tcMensajesResultadosConmutados, tcMensajesResultadosNoConmutados, relDropJob, relDropRes, reltotDrop);
            }
        };
        Platform.runLater(runnable);
    }

    @Override
    public void setExecutionPercentage(final double Percentage) 
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                viewResultsPhosphorus.setExecutionPercentage(Percentage);
            }
        };

        Platform.runLater(runnable);
    }
}