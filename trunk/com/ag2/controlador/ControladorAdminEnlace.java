package com.ag2.controlador;

import Grid.Entity;
import Grid.Port.GridInPort;
import Grid.Port.GridOutPort;
import com.ag2.modelo.PhosphorusLinkModel;
import com.ag2.modelo.LinkCreationAbstractModel;
import com.ag2.modelo.ModeloCrearEnlace;
import com.ag2.modelo.SimulacionBase;
import com.ag2.presentacion.diseño.GraphLink;
import com.ag2.presentacion.diseño.GraphNode;
import com.ag2.presentacion.diseño.propiedades.PropiedadeNodo;
import java.util.ArrayList;
import java.util.Hashtable;
import simbase.Port.SimBaseInPort;
import simbase.Port.SimBaseOutPort;
import simbase.SimBaseEntity;

public class ControladorAdminEnlace extends LinkAdminAbstractController {

    private Hashtable<GraphLink, PhosphorusLinkModel> contenedorParejasEnlacesExistentes = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObject();
    private Hashtable<GraphNode, Entity> contenedorParejasNodosExistentes = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObject();

    @Override
    public void crearEnlace(GraphLink enlaceGrafico) {

        Entity nodoPhosphorousA;
        Entity nodoPhosphorousB;

        for (LinkCreationAbstractModel modelo : registeredModels) {

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

        SimulacionBase.getInstance().setControladorAdminEnlace(this);
    }

    @Override
    public void consultarPropiedades(GraphLink enlaceGrafico) {

        ArrayList<PropiedadeNodo> propiedadesDeEnlace = new ArrayList<PropiedadeNodo>();
        PhosphorusLinkModel enlacePhosSeleccionado = (PhosphorusLinkModel) contenedorParejasEnlacesExistentes.get(enlaceGrafico);
        GridOutPort puertoSalidaNodoA = enlacePhosSeleccionado.getPuertoSalidaNodoPhosA();
        GridOutPort puertoSalidaNodoB = enlacePhosSeleccionado.getPuertoSalidaNodoPhosB();
        /*
         **Enlace de nodo Phosphorous de A hacia B (A->B)
         */
        //===========================================================================================================
        PropiedadeNodo propNombreDireccionCanalAB = new PropiedadeNodo("direcciónCanalAB", "Dirección del Canal:", PropiedadeNodo.TipoDePropiedadNodo.ETIQUETA, false);
        propNombreDireccionCanalAB.setPrimerValor(enlaceGrafico.getNodoGraficoA().getNombre() + "-->" + enlaceGrafico.getNodoGraficoB().getNombre());
        propiedadesDeEnlace.add(propNombreDireccionCanalAB);

        PropiedadeNodo propVelEnlaceAB = new PropiedadeNodo("linkSpeedAB", "Vel. del Enlace:", PropiedadeNodo.TipoDePropiedadNodo.NUMERO, false);
        propVelEnlaceAB.setPrimerValor(Double.toString(puertoSalidaNodoA.getLinkSpeed()));
        propiedadesDeEnlace.add(propVelEnlaceAB);
        //===========================================================================================================

        /*
         **Enlace de nodo Phosphorous de B hacia A (B->A)
         */
        //===========================================================================================================
        PropiedadeNodo propNombreDireccionCanalBA = new PropiedadeNodo("direcciónCanalBA", "Dirección del Canal:", PropiedadeNodo.TipoDePropiedadNodo.ETIQUETA, false);
        propNombreDireccionCanalBA.setPrimerValor(enlaceGrafico.getNodoGraficoB().getNombre() + "-->" + enlaceGrafico.getNodoGraficoA().getNombre());
        propiedadesDeEnlace.add(propNombreDireccionCanalBA);

        PropiedadeNodo propVelEnlaceBA = new PropiedadeNodo("linkSpeedBA", "Vel. del Enlace:", PropiedadeNodo.TipoDePropiedadNodo.NUMERO, false);
        propVelEnlaceBA.setPrimerValor(Double.toString(puertoSalidaNodoB.getLinkSpeed()));
        propiedadesDeEnlace.add(propVelEnlaceBA);
        //===========================================================================================================

        /*
         **Propiedades comunes en ambas direcciones del canal.
         */
        PropiedadeNodo propVelConmutacion = new PropiedadeNodo("switchingSpeed", "Vel. de Conmutación:", PropiedadeNodo.TipoDePropiedadNodo.NUMERO, false);
        propVelConmutacion.setPrimerValor((puertoSalidaNodoA.getSwitchingSpeed() == puertoSalidaNodoB.getSwitchingSpeed()) ? Double.toString(puertoSalidaNodoB.getSwitchingSpeed()) : "Problema leyendo Vel. de conmutación.");
        propiedadesDeEnlace.add(propVelConmutacion);

        PropiedadeNodo propWavelengths = new PropiedadeNodo("defaultWavelengths", "Cantidad de λs:", PropiedadeNodo.TipoDePropiedadNodo.NUMERO, false);
        propWavelengths.setPrimerValor((puertoSalidaNodoA.getMaxNumberOfWavelengths() == puertoSalidaNodoB.getMaxNumberOfWavelengths()) ? Integer.toString(puertoSalidaNodoB.getMaxNumberOfWavelengths()) : "Problema leyendo el numero de λ.");
        propiedadesDeEnlace.add(propWavelengths);

        tblPropiedadesDispositivo.cargarPropiedades(propiedadesDeEnlace);
    }

    @Override
    public void updatePropiedad(GraphLink enlaceGrafico, String id, String valor) {

        enlaceGrafico.getProperties().put(id, valor);

        PhosphorusLinkModel enlacePhosSeleccionado = (PhosphorusLinkModel) MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObject().get(enlaceGrafico);

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

        for (GraphLink enlaceGrafico : parejasDeEnlacesExistentes.keySet()) {
            crearEnlace(enlaceGrafico);
        }

        for (GraphLink enlaceGrafico : parejasDeEnlacesExistentes.keySet()) {

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
