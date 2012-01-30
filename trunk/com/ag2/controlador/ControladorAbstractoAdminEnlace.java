package com.ag2.controlador;

import com.ag2.modelo.EnlacePhosphorous;
import com.ag2.modelo.ModeloAbstractoCrearEnlace;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import com.ag2.presentacion.diseño.propiedades.TablaPropiedadesDispositivo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class ControladorAbstractoAdminEnlace implements Serializable {
    
    protected ArrayList<ModeloAbstractoCrearEnlace> modelosRegistrados;
    protected TablaPropiedadesDispositivo tblPropiedadesDispositivo;
    protected Hashtable<EnlaceGrafico,EnlacePhosphorous> parejasDeEnlacesExistentes;
    
    public ControladorAbstractoAdminEnlace(){
        modelosRegistrados = new ArrayList<ModeloAbstractoCrearEnlace>();
        parejasDeEnlacesExistentes = ContenedorParejasObjetosExistentes.getInstanciaParejasDeEnlacesExistentes();
    }
    
    public boolean addModelo(ModeloAbstractoCrearEnlace modelo) {
        return modelosRegistrados.add(modelo) && modelo.addControlador(this);
    }

    public boolean removeModelo(ControladorAbstractoAdminEnlace controlador) {
        return modelosRegistrados.remove(controlador) && controlador.removeModelo(this);
    }
    
    public abstract void crearEnlace(EnlaceGrafico enlaceGrafico);
    public abstract void consultarPropiedades(EnlaceGrafico enlaceGrafico);
    public abstract void updatePropiedad(EnlaceGrafico enlaceGrafico,String id,String valor);

    public void addVistaEnlace(TablaPropiedadesDispositivo propiedadesDispositivoTbl) {
        this.tblPropiedadesDispositivo = propiedadesDispositivoTbl;
    }
    
    public abstract void reCreatePhosphorousLinks();
}
