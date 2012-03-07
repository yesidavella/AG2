
package com.ag2.controller;

public class ResultsController  extends ResultsAbstractController
{

    @Override
    public void addClientResult(String tcCliente, String tcPeticionesEnviadas, String tcTrabajosEnviados, String tcResultadosRecibidos, String tcPeticionesFallidas, String tcPorcentajeResultadosRecibidos) {

        if(viewResultsPhosphorus!=null)
        {
            viewResultsPhosphorus.adicionarResultadoCliente(tcCliente, tcPeticionesEnviadas, tcTrabajosEnviados, tcResultadosRecibidos, tcPeticionesFallidas, tcPorcentajeResultadosRecibidos);
        }

    }

    @Override
    public void adicionarResultadoRecurso(String tcRecurso, String tcTrabajosRecibidos, String tcFallasNoEspacio, String tcFallasEnviadas)
    {
        if(viewResultsPhosphorus!=null)
        {
            viewResultsPhosphorus.adicionarResultadoRecurso(tcRecurso, tcTrabajosRecibidos, tcFallasNoEspacio, tcFallasEnviadas);
        }
    }

        @Override
    public void adicionarResultadoConmutador(String tcConmutador, String tcMensajesTrabajoConmutados, String tcMensajesTrabajoNoConmutados, String tcMensajesResultadosConmutados, String tcMensajesResultadosNoConmutados, String relDropJob, String relDropRes, String reltotDrop)
    {
        if(viewResultsPhosphorus!=null)
        {
            viewResultsPhosphorus.adicionarResultadoConmutador(tcConmutador, tcMensajesTrabajoConmutados, tcMensajesTrabajoNoConmutados, tcMensajesResultadosConmutados, tcMensajesResultadosNoConmutados, relDropJob, relDropRes, reltotDrop);
        }

    }

    @Override
    public void setExecutionPercentage(double Percentage)
    {
        viewResultsPhosphorus.setExecutionPercentage(Percentage);
    }
}