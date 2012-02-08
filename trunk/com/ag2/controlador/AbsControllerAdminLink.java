package com.ag2.controlador;

import com.ag2.modelo.EnlacePhosphorous;
import com.ag2.modelo.ModeloAbstractoCrearEnlace;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import com.ag2.presentacion.diseño.propiedades.TablaPropiedadesDispositivo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class AbsControllerAdminLink implements Serializable {

    protected ArrayList<ModeloAbstractoCrearEnlace> registeredModels;
    protected TablaPropiedadesDispositivo tblPropiedadesDispositivo;
    protected Hashtable<EnlaceGrafico, EnlacePhosphorous> parejasDeEnlacesExistentes;

    public AbsControllerAdminLink() {
        registeredModels = new ArrayList<ModeloAbstractoCrearEnlace>();
        parejasDeEnlacesExistentes = ContenedorParejasObjetosExistentes.getInstanciaParejasDeEnlacesExistentes();
    }

    public boolean addModelo(ModeloAbstractoCrearEnlace modelo) {
        return registeredModels.add(modelo) && modelo.addControlador(this);
    }

    public boolean removeModelo(AbsControllerAdminLink controlador) {
        return registeredModels.remove(controlador) && controlador.removeModelo(this);
    }

    public void setVistaEnlace(TablaPropiedadesDispositivo propiedadesDispositivoTbl) {
        this.tblPropiedadesDispositivo = propiedadesDispositivoTbl;
    }

    public abstract void crearEnlace(EnlaceGrafico enlaceGrafico);
    
    public abstract boolean removeLink(EnlaceGrafico graphLink);

    public abstract void consultarPropiedades(EnlaceGrafico enlaceGrafico);

    public abstract void updatePropiedad(EnlaceGrafico enlaceGrafico, String id, String valor);

    public abstract void reCreatePhosphorousLinks();
}
