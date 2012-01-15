package com.ag2.controlador;

import com.ag2.modelo.ModeloAbstractoCrearEnlace;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import com.ag2.presentacion.diseño.propiedades.TablaPropiedadesDispositivo;
import java.util.ArrayList;

public abstract class ControladorAbstractoAdminEnlace {
    
    protected ArrayList<ModeloAbstractoCrearEnlace> modelosRegistrados;
    protected TablaPropiedadesDispositivo tblPropiedadesDispositivo;
    
    public ControladorAbstractoAdminEnlace(){
        modelosRegistrados = new ArrayList<ModeloAbstractoCrearEnlace>();
    }
    
    public boolean addModelo(ModeloAbstractoCrearEnlace modelo) {
        return modelosRegistrados.add(modelo) && modelo.addControlador(this);
    }

    public boolean removeModelo(ControladorAbstractoAdminEnlace controlador) {
        return modelosRegistrados.remove(controlador) && controlador.removeModelo(this);
    }
    
    public abstract void crearEnlace(EnlaceGrafico enlaceGrafico);
    public abstract void consultarPropiedades(EnlaceGrafico enlaceGrafico);

    public void addVistaEnlace(TablaPropiedadesDispositivo propiedadesDispositivoTbl) {
        this.tblPropiedadesDispositivo = propiedadesDispositivoTbl;
    }
}
