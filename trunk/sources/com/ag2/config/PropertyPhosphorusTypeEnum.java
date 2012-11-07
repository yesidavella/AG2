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

//****************************SIMULATION PROPERTIES*****************************
    SIMULATION_TIME("simulationTime", "Tiempo de Simulación:(s)", new TextField()),
    /**
     * Paso ciclico de tiempo en el que el simulador verifica terminar la
     * simulacion.
     */
    STOP_EVENT_OFF_SETTIME("stopEventOffSetTime", "Paso ciclico de fin simulacion:(s)", new TextField()),
    //    ROUTED_VIA_JUNG("routedViaJUNG", "Enrutar via:(T)Jung (F)ShortesPath", new CheckBox()),
    /**
     * Se desea que genere o no archivos HTML del paso a paso de la simulacion.
     */
    /// OUTPUT("output", "output(Franklin)", new CheckBox()),
    OUTPUT_FILE_NAME("outputFileName", "Nombre archivo de log:", new TextField()),
    //*****************************LINK PROPERTIES******************************
    /**
     * Cantidad de lambdas en la fibra optica.
     */
    DEFAULT_WAVELENGTHS("defaultWavelengths", "Número de lambdas:", new TextField()),
    /**
     * Tiempo necesario en alcanzar la otra punta del enlace de un mensaje.
     */
    LINK_SPEED("linkSpeed", "Velocidad del enlace:(Mbps)", new TextField()),
    //****************************SWITCH PROPERTIES*****************************
    /**
     * El costo de busqueda de una longitud de onda mientras el OCS es
     * establecido.
     */
    OCS_SETUP_FIND_COMMON_WAVELENGHT("findCommonWavelenght", "Tiempo buscar λ en creación OCS:(s)", new TextField()),
    /**
     * El costo de alojar una longitud de onda en el OCS mientras es establecido el OCS
     */
    OCS_SETUP_ALLOCATE_WAVELENGHT("allocateWavelenght", "Tiempo alojar λ en creación OCS:(s)", new TextField()),
    CONFIRM_OCS_DELAY("confirmOCSDelay", "Tiempo de retardo confirmación OCS:(s)", new TextField()),
    /**
     * Tiempo de retardo al conmutar un msg, solo usado en OBSSwitchImpl, NO en
     * conmutadores hibridos. Esto es la propiedad HandleDelay.
     */
    //    OBS_HANDLE_TIME("OBSHandleTime", "Retardo conmutación OBS(s):", new TextField()),
    /**
     * Retraso por nodo de un flujo sobre un circuito optico establecido
     */
    OCS_SWITCH_DELAY("OCS_SwitchingDelay", "Retardo de conmutación en OCS:(s)", new TextField()),
    SWITCHING_SPEED("switchingSpeed", "Velocidad de conmutación:(Mbps)", new TextField()),
    //***************************RESOURCE PROPERTIES****************************
    DEFAULT_CPU_CAPACITY("defaultCpuCapacity", "Capacidad de CPU:(MHz)", new TextField()),//Antes se llamaba DEFAULT_CAPACITY
    DEFAULT_CPU_COUNT("defaultCPUCount", "Número de CPUs:", new TextField()),
    /**
     * Cantidad de trabajos a encolar en el buffer del nodo recurso
     */
    DEFAULT_QUEUE_SIZE("defaultQueueSize", "Tamaño Buffer de Trabajos/cluster:", new TextField()),
    //***********************CLIENT PROPERTIES**********************************
    /**
     * Toma la prioridad del trafico q se le asigna a un Cliente.
     */
    CLIENT_TRAFFIC_PRIORITY("clientTrafficPriority", "Prioridad Tráfico/cliente(1,2,...10)", new TextField()),
    /**
     * Cada vez q se cumpla este intervalo se generara una nueva solicitud para
     * generar un requestMsg.
     */
    DEFAULT_JOB_IAT("defaultJobIAT", "Intervalo de generación de trabajo:(s)", new TextField()),
    //The size of control messages. If control messages are 0, the are being send immediately
    ACK_SIZE("ACKsize", "Tamaño de msg confirmación:(MB)", new TextField()),
    /**
     * Tamaño de cada trabajo jobMsg.
     */
    DEFAULT_DATA_SIZE("defaultDataSize", "Tamaño de Trabajo:(MB)", new TextField()),
    /**
     * Determina el tiempo q tarde ejecutando un trabajo en el Nodo de recurso
     * segun su capacidad. double executionTime = job.getMsg().getFlops() /
     * cpu.getCpuCapacity();
     */
    DEFAULT_FLOP_SIZE("defaultFlopSize", "FLOPS de Trabajo(Mflops):", new TextField());
    /**
     * Promedio de la exp. neg. del retraso maximo en la solicitud del trabajo
     * Al parecer nunca se utiliza en serio.SOlo se utiliza en getMaxEndTime(),
     * pero este ultimo metodo nunca es utilizado.
     */
//    MAX_DELAY("maxDelay", "Retraso máx/Trab_Req(s):", new TextField());
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
                        routing = new RoutingViaJung(SimulationBase.getInstance().getGridSimulatorModel(), "routing");
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

    public void writeProperty(String value) {
        phosphorusPropertyEditor.setPropertyValue(this, value);
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