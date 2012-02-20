/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.controller;

import com.ag2.model.OutputterModel;
import com.ag2.model.SimulationBase;
import com.ag2.presentation.control.ViewResultsPhosphorus;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ResultsAbstractController  implements Serializable
{
   protected  ViewResultsPhosphorus viewResultsPhosphorus;
   protected  OutputterModel outputterModel = new OutputterModel(SimulationBase.getInstance().getSimulador());

    public void setViewResultsPhosphorus(ViewResultsPhosphorus viewResultsPhosphorus)
    {
        this.viewResultsPhosphorus = viewResultsPhosphorus;
    }

   public abstract  void addClientResult(String tcCliente, String tcPeticionesEnviadas ,
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






}
