package com.ag2.controlador;

import Grid.Entity;
import Grid.Port.GridInPort;
import Grid.Port.GridOutPort;
import com.ag2.modelo.PhosphorusLinkModel;
import com.ag2.modelo.LinkCreationAbstractModel;
import com.ag2.modelo.ModeloCrearEnlace;
import com.ag2.modelo.SimulationBase;
import com.ag2.presentacion.diseño.GraphLink;
import com.ag2.presentacion.diseño.GraphNode;
import com.ag2.presentacion.diseño.propiedades.EntityProperty;
import java.util.ArrayList;
import java.util.Hashtable;
import simbase.Port.SimBaseInPort;
import simbase.Port.SimBaseOutPort;
import simbase.SimBaseEntity;

public class ControladorAdminEnlace extends LinkAdminAbstractController {

    private Hashtable<GraphLink, PhosphorusLinkModel> contenedorParejasEnlacesExistentes = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer();
    private Hashtable<GraphNode, Entity> contenedorParejasNodosExistentes = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer();

    @Override
    public void createLink(GraphLink enlaceGrafico) {

        Entity nodoPhosphorousA;
        Entity nodoPhosphorousB;

        for (LinkCreationAbstractModel modelo : linkCreationAbstractModels) {

            if (modelo instanceof ModeloCrearEnlace) {

                GraphNode nodoGraficoA = enlaceGrafico.getNodoGraficoA();
                GraphNode nodoGraficoB = enlaceGrafico.getNodoGraficoB();

                nodoPhosphorousA = (Entity) contenedorParejasNodosExistentes.get(nodoGraficoA);
                nodoPhosphorousB = (Entity) contenedorParejasNodosExistentes.get(nodoGraficoB);

                if (nodoPhosphorousA != null && nodoPhosphorousB != null) {
                    PhosphorusLinkModel nuevoEnlacePhosphorous = modelo.createPhosphorusLink(nodoPhosphorousA, nodoPhosphorousB);
                    contenedorParejasEnlacesExistentes.put(enlaceGrafico, nuevoEnlacePhosphorous);
                } else {
                    System.out.println("Algun nodo PHOSPHOROUS esta NULL, NO se creo el ENLACE en PHOPHOROUS.");
                }
            }
        }

        SimulationBase.getInstance().setControladorAdminEnlace(this);
    }

    @Override
    public void queryProperty(GraphLink enlaceGrafico) {

        ArrayList<EntityProperty> propiedadesDeEnlace = new ArrayList<EntityProperty>();
        PhosphorusLinkModel enlacePhosSeleccionado = (PhosphorusLinkModel) contenedorParejasEnlacesExistentes.get(enlaceGrafico);
        GridOutPort puertoSalidaNodoA = enlacePhosSeleccionado.getPuertoSalidaNodoPhosA();
        GridOutPort puertoSalidaNodoB = enlacePhosSeleccionado.getPuertoSalidaNodoPhosB();
        /*
         **Enlace de nodo Phosphorous de A hacia B (A->B)
         */
        //===========================================================================================================
        EntityProperty propNombreDireccionCanalAB = new EntityProperty("direcciónCanalAB", "Dirección del Canal:", EntityProperty.TipoDePropiedadNodo.ETIQUETA, false);
        propNombreDireccionCanalAB.setPrimerValor(enlaceGrafico.getNodoGraficoA().getNombre() + "-->" + enlaceGrafico.getNodoGraficoB().getNombre());
        propiedadesDeEnlace.add(propNombreDireccionCanalAB);

        EntityProperty propVelEnlaceAB = new EntityProperty("linkSpeedAB", "Vel. del Enlace:", EntityProperty.TipoDePropiedadNodo.NUMERO, false);
        propVelEnlaceAB.setPrimerValor(Double.toString(puertoSalidaNodoA.getLinkSpeed()));
        propiedadesDeEnlace.add(propVelEnlaceAB);
        //===========================================================================================================

        /*
         **Enlace de nodo Phosphorous de B hacia A (B->A)
         */
        //===========================================================================================================
        EntityProperty propNombreDireccionCanalBA = new EntityProperty("direcciónCanalBA", "Dirección del Canal:", EntityProperty.TipoDePropiedadNodo.ETIQUETA, false);
        propNombreDireccionCanalBA.setPrimerValor(enlaceGrafico.getNodoGraficoB().getNombre() + "-->" + enlaceGrafico.getNodoGraficoA().getNombre());
        propiedadesDeEnlace.add(propNombreDireccionCanalBA);

        EntityProperty propVelEnlaceBA = new EntityProperty("linkSpeedBA", "Vel. del Enlace:", EntityProperty.TipoDePropiedadNodo.NUMERO, false);
        propVelEnlaceBA.setPrimerValor(Double.toString(puertoSalidaNodoB.getLinkSpeed()));
        propiedadesDeEnlace.add(propVelEnlaceBA);
        //===========================================================================================================

        /*
         **Propiedades comunes en ambas direcciones del canal.
         */
        EntityProperty propVelConmutacion = new EntityProperty("switchingSpeed", "Vel. de Conmutación:", EntityProperty.TipoDePropiedadNodo.NUMERO, false);
        propVelConmutacion.setPrimerValor((puertoSalidaNodoA.getSwitchingSpeed() == puertoSalidaNodoB.getSwitchingSpeed()) ? Double.toString(puertoSalidaNodoB.getSwitchingSpeed()) : "Problema leyendo Vel. de conmutación.");
        propiedadesDeEnlace.add(propVelConmutacion);

        EntityProperty propWavelengths = new EntityProperty("defaultWavelengths", "Cantidad de λs:", EntityProperty.TipoDePropiedadNodo.NUMERO, false);
        propWavelengths.setPrimerValor((puertoSalidaNodoA.getMaxNumberOfWavelengths() == puertoSalidaNodoB.getMaxNumberOfWavelengths()) ? Integer.toString(puertoSalidaNodoB.getMaxNumberOfWavelengths()) : "Problema leyendo el numero de λ.");
        propiedadesDeEnlace.add(propWavelengths);

        entityPropertyTable.loadProperties(propiedadesDeEnlace);
    }

    @Override
    public void updatePropiedad(GraphLink enlaceGrafico, String id, String valor) {

        enlaceGrafico.getProperties().put(id, valor);

        PhosphorusLinkModel enlacePhosSeleccionado = (PhosphorusLinkModel) MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer().get(enlaceGrafico);

        if (id.equalsIgnoreCase("linkSpeedAB")) {
            enlacePhosSeleccionado.getPuertoSalidaNodoPhosA().setLinkSpeed(Double.valueOf(valor));
        } else if (id.equalsIgnoreCase("linkSpeedBA")) {
            enlacePhosSeleccionado.getPuertoSalidaNodoPhosB().setLinkSpeed(Double.valueOf(valor));
        } else if (id.equalsIgnoreCase("switchingSpeed")) {
            enlacePhosSeleccionado.getPuertoSalidaNodoPhosA().setSwitchingSpeed(Double.valueOf(valor));
            enlacePhosSeleccionado.getPuertoSalidaNodoPhosB().setSwitchingSpeed(Double.valueOf(valor));
        } else if (id.equalsIgnoreCase("defaultWavelengths")) {
            enlacePhosSeleccionado.getPuertoSalidaNodoPhosA().setMaxNumberOfWavelengths(Integer.parseInt(valor));
            enlacePhosSeleccionado.getPuertoSalidaNodoPhosB().setMaxNumberOfWavelengths(Integer.parseInt(valor));
        }
    }

    @Override
    public void reCreatePhosphorousLinks() {

        for (GraphLink enlaceGrafico : linkMatchCoupleObjectContainer.keySet()) {
            createLink(enlaceGrafico);
        }

        for (GraphLink enlaceGrafico : linkMatchCoupleObjectContainer.keySet()) {

            for (String id : enlaceGrafico.getProperties().keySet()) {
                updatePropiedad(enlaceGrafico, id, enlaceGrafico.getProperties().get(id));
            }
        }
    }

    @Override
    public boolean removeLink(GraphLink graphLink) {

        PhosphorusLinkModel phosLink = contenedorParejasEnlacesExistentes.get(graphLink);
                
        boolean canRemovePortsInPhosNodeA = removeInAndOutPort(phosLink.getPuertoSalidaNodoPhosA());
        boolean canRemovePortsInPhosNodeB = removeInAndOutPort(phosLink.getPuertoSalidaNodoPhosB());

        contenedorParejasEnlacesExistentes.remove(graphLink);

        return (canRemovePortsInPhosNodeA && canRemovePortsInPhosNodeB);
    }

    private boolean removeInAndOutPort(GridOutPort outPort) {
        //Remuevo el puerto de salida y de entrada
        SimBaseInPort inPort = outPort.getTarget();
        
        SimBaseEntity source = outPort.getOwner();
        SimBaseEntity target = inPort.getOwner();

        boolean canRemoveInPort = target.getInPorts().remove(inPort);
        boolean canRemoveOutPort = source.getOutPorts().remove(outPort);

        return canRemoveInPort && canRemoveOutPort;
    }
}
