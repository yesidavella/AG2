package com.ag2.config;

import Grid.Routing.Routing;
import Grid.Routing.RoutingViaJung;
import Grid.Routing.ShortesPathRouting;
import com.ag2.controller.ExecuteController;
import com.ag2.model.SimulationBase;
import com.ag2.presentation.GUI;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * Carga el archivo .cfg con el valor de las constantes de la simulación.
 */
public enum PropertyPhosphorusTypeEnum {

    /*
     * Tiempo de simulacion en milisegundos.
     */
    SIMULATION_TIME("simulationTime", "Tiempo de Simulación(ms):", new TextField()),
    /**
     * Se desea que genere o no archivos HTML del paso a paso de la simulacion.
     */
    /// OUTPUT("output", "output(Franklin)", new CheckBox()),
    STOP_EVENT_OFF_SETTIME("stopEventOffSetTime", "stopEventOffSetTime", new TextField()),
    SWITCHING_SPEED("switchingSpeed", "Vel. de conmutación(B/ms):", new TextField()),
    /**
     * Cantidad de lambdas en la fibra optica.
     */
    DEFAULT_WAVELENGTHS("defaultWavelengths", "Numero de lambdas:", new TextField()),
    //The size of control messages. If control messages are 0, the are being send immediately
    ACK_SIZE("ACKsize", "Prom. Tamaño AKC(B):", new TextField()),
    DEFAULT_CPU_CAPACITY("defaultCpuCapacity", "Capacidad de CPUs/clúster(B/ms):", new TextField()),//Antes se llamaba DEFAULT_CAPACITY
    DEFAULT_CPU_COUNT("defaultCPUCount", "Número de CPUs/clúster:", new TextField()),
    /**
     * Cantidad de trabajos a encolar en el buffer del nodo recurso
     */
    DEFAULT_QUEUE_SIZE("defaultQueueSize", "Buffer de Trabajos/cluster:", new TextField()),
    /**
     * Determina el tiempo q tarde ejecutando un trabajo en el Nodo de recurso
     * segun su capacidad. double executionTime = job.getMsg().getFlops() /
     * cpu.getCpuCapacity();
     */
    DEFAULT_FLOP_SIZE("defaultFlopSize", "Prom. de FLOPS/Trabajo(B*ms):", new TextField()),
    /**
     * Promedio del tamaño de cada trabajo.
     */
    DEFAULT_DATA_SIZE("defaultDataSize", "Prom. del tamaño/Trabajo(B):", new TextField()),
    /**
     * Promedio de la exp. neg. de duracion del Intervalo sin q lleguen trabajos
     */
    DEFAULT_JOB_IAT("defaultJobIAT", "Prom. Inter LLegada/Trabajo(ms):", new TextField()),
    /**
     * Promedio de la exp. neg. del retraso maximo en la solicitud del trabajo
     * Al parecer nunca se utiliza en serio.SOlo se utiliza en getMaxEndTime(),
     * pero este ultimo metodo nunca es utilizado.
     */
    MAX_DELAY("maxDelay", "Prom. Retraso máx/Trab_Req(ms):", new TextField()),
    OUTPUT_FILE_NAME("outputFileName", "Nombre del archivo de traza:", new TextField()),
    /**
     * Tiempo de retardo al conmutar un msg, solo usado en OBSSwitchImpl, NO en
     * conmutadores hibridos. Esto es la propiedad HandleDelay.
     */
    OBS_HANDLE_TIME("OBSHandleTime", "Retardo OBS(ms):", new TextField()),
    /**
     * Tiempo q se demora en crear o eliminar un OCS. Solo usado en el
     * OCSEndSender.
     */
//    OCS_SETUP_HANDLE_TIME("OCSSetupHandleTime", "Tiempo crear/eliminar un OCS(ms):", new TextField()),
    
    /**
     * El costo de busqueda de una longitud de onda mientras el OCS es establecido.
     */
    OCS_SETUP_FIND_COMMON_WAVELENGHT("findCommonWavelenght","Costo buscar λ en creación OCS(ms):", new TextField()),
    
    /**
     * El costo de alojar una longitud de onda en el OCS mientras es establecido
     */
    OCS_SETUP_ALLOCATE_WAVELENGHT("allocateWavelenght","Costo alojar λ en creación OCS(ms):", new TextField()),
    /**
     * Tiempo necesario en alcanzar la otra punta del enlace de un mensaje.
     */
    LINK_SPEED("linkSpeed", "Vel. del enlace(ms):", new TextField()),
    //DEFAULT_LINK_SPEED("defaultLinkSpeed", "a", new TextField()),
    ROUTED_VIA_JUNG("routedViaJUNG", "Enrutar via:(T)Jung (F)ShortesPath", new CheckBox()),
    /**
     * Toma la prioridad del trafico q se le asigna a un Cliente.
     */
    CLIENT_TRAFFIC_PRIORITY("clientTrafficPriority","Prioridad Tráfico/cliente(1,2,...10)", new TextField()),
     /**
     * Retraso por nodo de un flujo sobre  un circuito optico establecido 
     */
    OCS_SWITCH_DELAY("OCS_SwitchingDelay","Retraso de conmutación en OCS (ms)", new TextField());
    
    
    private ExecuteController executeController;
    private String phosphorusPropertyName;
    private Control control;
    private PhosphorusPropertyEditor phosphorusPropertyEditor = PhosphorusPropertyEditor.getUniqueInstance();
    private String visualNameOnTb;

    public String getVisualNameOnTb() {
        return visualNameOnTb;
    }

    private PropertyPhosphorusTypeEnum(String name, String visualNameOnTableview, Control control) {
        this.visualNameOnTb = visualNameOnTableview;
        phosphorusPropertyName = name;
        this.control = control;
        if (control instanceof TextField) {
            setPropertyEvent((TextField) control);

        } else if (control instanceof CheckBox) {
            setEventProperty(((CheckBox) control));
        }
    }

    public void setExecuteController(ExecuteController executeController) {
        this.executeController = executeController;
    }

    public Control getControl() {
        return control;
    }

    public void setControl(Control control) {
        this.control = control;
    }

    public String getPropertyName() {
        return this.toString().replace("_", " ");
    }

    private void setEventProperty(CheckBox checkBox) {

        if (phosphorusPropertyEditor.getPropertyValue(this).equalsIgnoreCase("true")) {
            checkBox.setSelected(true);
        }

        checkBox.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent actionEvent) {
                CheckBox checkBox = (CheckBox) actionEvent.getSource();
                if (checkBox.isSelected()) {
                    PropertyPhosphorusTypeEnum.this.writeProperty(Boolean.TRUE.toString());
                } else {
                    PropertyPhosphorusTypeEnum.this.writeProperty(Boolean.FALSE.toString());
                }

                if (getPhosphorusPropertyName().equalsIgnoreCase("routedViaJUNG")) {
                    Routing routing;
                    if (checkBox.isSelected()) {
                        routing = new RoutingViaJung(SimulationBase.getInstance().getGridSimulatorModel());
                    } else {
                        routing = new ShortesPathRouting(SimulationBase.getInstance().getGridSimulatorModel());
                    }

                    SimulationBase.getInstance().getGridSimulatorModel().setRouting(routing);
                }
            }
        });
    }

    private void setPropertyEvent(final TextField textField) {

        textField.setText(phosphorusPropertyEditor.getPropertyValue(this));

        textField.setOnMouseExited(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent event) {
                GUI.getInstance().getGraphDesignGroup().getGroup().requestFocus();
            }
        });

        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

            public void changed(ObservableValue<? extends Boolean> textControl, Boolean beforeStateFocus, Boolean currentStateFocus) {

                if (beforeStateFocus == true && currentStateFocus == false) {
                    String value = textField.getText();
                    PropertyPhosphorusTypeEnum.this.writeProperty(value);
                    executeController.reLoadConfigFile();
                }
            }
        });
    }

    public String getPhosphorusPropertyName() {
        return phosphorusPropertyName;
    }

    public void writeProperty(String valor) {
        phosphorusPropertyEditor.setPropertyValue(this, valor);
    }

    public static ObservableList getData(ExecuteController executeController) {
        for (PropertyPhosphorusTypeEnum propertyPhosphorusTypeEnum : values()) {
            propertyPhosphorusTypeEnum.setExecuteController(executeController);
        }

        ObservableList observableList = FXCollections.observableArrayList(PropertyPhosphorusTypeEnum.values());
        return observableList;
    }

    public static double getDoubleProperty(PropertyPhosphorusTypeEnum key) {
        PhosphorusPropertyEditor phosPropEditor = PhosphorusPropertyEditor.getUniqueInstance();
        String propertie = phosPropEditor.getProperties().getProperty(key.getPhosphorusPropertyName());
        if (propertie == null) {
            throw new IllegalArgumentException(key.toString() + " no esta en el archivo de configuración.");
        }
        return Double.parseDouble(propertie);
    }

    public static boolean getBooleanProperty(PropertyPhosphorusTypeEnum key) {
        PhosphorusPropertyEditor phosPropEditor = PhosphorusPropertyEditor.getUniqueInstance();
        String propertie = phosPropEditor.getProperties().getProperty(key.getPhosphorusPropertyName());
        return Boolean.parseBoolean(propertie);
    }

    public static long getLongProperty(PropertyPhosphorusTypeEnum key) {
        PhosphorusPropertyEditor phosPropEditor = PhosphorusPropertyEditor.getUniqueInstance();
        String propertie = phosPropEditor.getProperties().getProperty(key.getPhosphorusPropertyName());
        return Long.parseLong(propertie);
    }

    public static int getIntProperty(PropertyPhosphorusTypeEnum key) {
        PhosphorusPropertyEditor phosPropEditor = PhosphorusPropertyEditor.getUniqueInstance();
        String propertie = phosPropEditor.getProperties().getProperty(key.getPhosphorusPropertyName());
        return Integer.parseInt(propertie);
    }
}