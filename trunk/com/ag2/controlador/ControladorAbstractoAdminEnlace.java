package com.ag2.controlador;

import Grid.Port.GridOutPort;
import com.ag2.modelo.ModeloAbstractoCrearEnlace;
import com.ag2.presentacion.diseño.EnlaceGrafico;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class ControladorAbstractoAdminEnlace {
    
    protected ArrayList<ModeloAbstractoCrearEnlace> modelosRegistrados;
    
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
    
    
}
