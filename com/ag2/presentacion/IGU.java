package com.ag2.presentacion;

import com.ag2.config.TipoDePropiedadesPhosphorus;
import com.ag2.presentacion.controles.Boton;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import com.ag2.presentacion.controles.ResultadosPhosphorousHTML;
import com.ag2.presentacion.controles.ResultadosPhosphorus;
import com.ag2.presentacion.diseño.NodoGrafico;
import com.ag2.presentacion.diseño.propiedades.TablaPropiedadesDispositivo;
import java.io.Serializable;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class IGU extends Scene implements Serializable {

    private GrupoDeDiseno grGrupoDeDiseño = new GrupoDeDiseno();
    private ToggleGroup tgHerramientas;
    private GridPane gpNavegacionMapa;
    private ExecutePane executePane;
    private Boton btnMoverEscena;
    private Boton btnCliente = new Boton(TiposDeBoton.CLIENTE);
    private Boton btnNodoDeServicio = new Boton(TiposDeBoton.NODO_DE_SERVICIO);
    private Boton btnEnrutadorOptico = new Boton(TiposDeBoton.ENRUTADOR_OPTICO);
    private Boton btnEnrutadorDeRafaga = new Boton(TiposDeBoton.ENRUTADOR_RAFAGA);
    private Boton btnEnrutadorHibrido = new Boton(TiposDeBoton.ENRUTADOR_HIBRIDO);
    private Boton btnRecurso = new Boton(TiposDeBoton.RECURSO);
    private Boton btnEnlace = new Boton(TiposDeBoton.ENLACE);
    private static TiposDeBoton estadoTipoBoton = TiposDeBoton.PUNTERO;
    private TablaPropiedadesDispositivo tbDeviceProperties;
    private GridPane barraHerramientas;
    private ScrollPane scPnWorld;
    private ProgressBar prgBarExecProgress;
    private boolean estaTeclaPrincipalOprimida = false;
    private TiposDeBoton estadoAnteriorDeBtnAEvento;
    private Cursor cursorAnteriorAEvento;
    private ResultadosPhosphorus resultadosPhosphorus;
    private static IGU iguAG2;
    private Main main;
    private StackPane stPnDeviceProperties = new StackPane();
    private Boton btnSeleccion;
    private Boton btnDividirEnlaceCuadrado;
    private Boton btnEliminar;
    private Boton btnMinusZoom;
    private Boton btnPlusZoom;
    private Group grRoot = new Group();
    private VBox vbNavegation = new VBox();
    private Tab tabSimulacion = new Tab();
    private Tab tabResultados = new Tab();
    private Tab tabResultadosHTML = new Tab();
    private TabPane tpBox = new TabPane();

    public ScrollPane getScPnWorld() {
        return scPnWorld;
    }
    private transient Stage stgEscenario;

    public static IGU getInstance() {

        if (iguAG2 == null) {
            iguAG2 = new IGU(new BorderPane(), 1100, 700);
        }
        return iguAG2;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void loadGrupoDeDiseno(GrupoDeDiseno grupoDeDiseño) {

        grRoot.getChildren().remove(this.grGrupoDeDiseño.getGroup());
        this.grGrupoDeDiseño = grupoDeDiseño;
        grRoot.getChildren().add(grGrupoDeDiseño.getGroup());
        
        executePane.setGroup(grGrupoDeDiseño.getGroup());
        btnMoverEscena.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnSeleccion.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnDividirEnlaceCuadrado.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnEliminar.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnMinusZoom.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnPlusZoom.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnCliente.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnNodoDeServicio.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnEnrutadorOptico.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnEnrutadorDeRafaga.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnEnrutadorHibrido.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnRecurso.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnEnlace.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        executePane.setGroup(grGrupoDeDiseño.getGroup());
        grGrupoDeDiseño.setScrollPane(scPnWorld);       
      
        crearPanelDeNavegacionMapa(vbNavegation);
        adicionarEventoDeTecladoAEscena(this);
    }

    private IGU(BorderPane layOutVentanaPrincipal, double anchoVentana, double altoVentana) {
        super(layOutVentanaPrincipal, anchoVentana, altoVentana);
        scPnWorld = new ScrollPane();
        tgHerramientas = new ToggleGroup();
        gpNavegacionMapa = new GridPane();
        executePane = new ExecutePane();
        executePane.setGroup(grGrupoDeDiseño.getGroup());

        adicionarEventoDeTecladoAEscena(this);
        getStylesheets().add(IGU.class.getResource("../../../recursos/css/IGUPrincipal.css").toExternalForm());

        layOutVentanaPrincipal.getStyleClass().add("ventanaPrincipal");

        //Diseño superior
        crearBarraDeMenus(layOutVentanaPrincipal, stgEscenario);

        //Diseño izquierdo(contenedor de Ejecucion y herramientas)
        barraHerramientas = creacionBarraDeHerramientas();

        VBox contenedorHerramietas = new VBox();
        contenedorHerramietas.getChildren().addAll(executePane, barraHerramientas);
        layOutVentanaPrincipal.setLeft(contenedorHerramietas);
        //Diseño central
        crearLienzoDeTabs();
        layOutVentanaPrincipal.setCenter(tpBox);
        //Diseño inferior
        HBox cajaInferiorHor = createBottomDesign();
        layOutVentanaPrincipal.setBottom(cajaInferiorHor);

//        inicializarEstadoDeIGU();
    }

    public void setStage(Stage stage) {
        this.stgEscenario = stage;
    }

    public static TiposDeBoton getEstadoTipoBoton() {
        if (estadoTipoBoton == null) {
            estadoTipoBoton = TiposDeBoton.PUNTERO;
        }
        return estadoTipoBoton;
    }

    public static void setEstadoTipoBoton(TiposDeBoton tiposDeBoton) {
        estadoTipoBoton = tiposDeBoton;
    }

    private void crearBarraDeMenus(BorderPane diseñoVentana, final Stage stgEscenario) {

        //Panel de menus
        HBox hBoxContenedorDeMenu = new HBox();
        hBoxContenedorDeMenu.setPadding(new Insets(3, 0, 3, 3));
        hBoxContenedorDeMenu.getStyleClass().add("contenedorDeMenus");

        //Items de menus y menus
        Menu menuArchivo = new Menu("Archivo");

        Menu menuAyuda = new Menu("Ayuda");

        MenuItem itemNuevoPrj = new MenuItem("Nuevo Proyecto");
        MenuItem itemAbrir = new MenuItem("Abrir");
        MenuItem itemGuardar = new MenuItem("Guardar");
        MenuItem itemCerrar = new MenuItem("Cerrar");

        MenuItem itemAyuda = new MenuItem("Ayuda");
        MenuItem itemAcercaDe = new MenuItem("Acerca del Proyecto AG2...");

        menuArchivo.getItems().addAll(itemNuevoPrj, new SeparatorMenuItem(), itemAbrir, itemGuardar, new SeparatorMenuItem(), itemCerrar);
        menuAyuda.getItems().addAll(itemAyuda, new SeparatorMenuItem(), itemAcercaDe);

        itemGuardar.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {

                main.save(false);

            }
        });
        itemAbrir.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                main.load();
            }
        });
        itemNuevoPrj.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int result = JOptionPane.showConfirmDialog(
                        null, "¿Desea guardar los cambios efectuados en la simulación?", "Simulador AG2", JOptionPane.YES_NO_CANCEL_OPTION);

                if (result == JOptionPane.NO_OPTION) 
                {
                   main.loadFileBaseSimulation();
                } 
                else if (result == JOptionPane.YES_OPTION)
                {
                    main.save(false);
                    main.loadFileBaseSimulation();
                }

               

            }
        });

        itemCerrar.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int result = JOptionPane.showConfirmDialog(
                        null, "¿Desea guardar los cambios efectuados en la simulación?", "Simulador AG2", JOptionPane.YES_NO_CANCEL_OPTION);

                if (result == JOptionPane.NO_OPTION) {
                    System.exit(0);
                } else if (result == JOptionPane.YES_OPTION) {
                    main.save(true);
                }

               

            }
        });

        //La barra de menus
        MenuBar mnuBarBarraDeMenus = new MenuBar();
        mnuBarBarraDeMenus.getMenus().addAll(menuArchivo, menuAyuda);
        mnuBarBarraDeMenus.getStyleClass().add("barraDeMenus");

        hBoxContenedorDeMenu.getChildren().add(mnuBarBarraDeMenus);
        diseñoVentana.setTop(hBoxContenedorDeMenu);
    }

    private GridPane creacionBarraDeHerramientas() {

        GridPane grdPnBarraDeHerramientas = new GridPane();
        grdPnBarraDeHerramientas.setPadding(new Insets(10, 10, 10, 10));
        grdPnBarraDeHerramientas.setVgap(5);
        grdPnBarraDeHerramientas.setHgap(4);
        grdPnBarraDeHerramientas.getStyleClass().add("barraDeHerramientas");

        creacionDeBtnsDeUtilidades(grdPnBarraDeHerramientas);
        creacionBtnsDeNodos(grdPnBarraDeHerramientas);

        return grdPnBarraDeHerramientas;
    }

    private void creacionDeBtnsDeUtilidades(GridPane grdPnBarraHerramientas) {

        btnMoverEscena = new Boton(TiposDeBoton.MANO);
        btnSeleccion = new Boton(TiposDeBoton.PUNTERO);
        btnDividirEnlaceCuadrado = new Boton(TiposDeBoton.ADICIONAR_VERTICE);
        btnEliminar = new Boton(TiposDeBoton.ELIMINAR);
        btnMinusZoom = new Boton(TiposDeBoton.ZOOM_MINUS);
        btnPlusZoom = new Boton(TiposDeBoton.ZOOM_PLUS);

        btnMoverEscena.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnSeleccion.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnDividirEnlaceCuadrado.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnEliminar.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnMinusZoom.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnPlusZoom.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());

        btnMoverEscena.setToggleGroup(tgHerramientas);
        btnSeleccion.setToggleGroup(tgHerramientas);
        btnDividirEnlaceCuadrado.setToggleGroup(tgHerramientas);
        btnEliminar.setToggleGroup(tgHerramientas);
        btnMinusZoom.setToggleGroup(tgHerramientas);
        btnPlusZoom.setToggleGroup(tgHerramientas);

        btnMoverEscena.setTooltip(new Tooltip("Mueva el mapa a su gusto con el raton (Selección rapida:Alt)."));
        btnSeleccion.setTooltip(new Tooltip("Seleccione cualquier objeto"));
        btnDividirEnlaceCuadrado.setTooltip(new Tooltip("Añadale vertices a un enlace"));
        btnEliminar.setTooltip(new Tooltip("Elimine un objeto"));
        btnMinusZoom.setTooltip(new Tooltip("Disminuya el zoom del mapa en donde\nrealize el click (Selección rapida:Ctrl)"));
        btnPlusZoom.setTooltip(new Tooltip("Aumente el zoom del mapa en donde\nrealize el click (Selección rapida:Shift)"));

        GridPane.setConstraints(btnSeleccion, 0, 0);
        grdPnBarraHerramientas.getChildren().add(btnSeleccion);

        GridPane.setConstraints(btnMoverEscena, 1, 0);
        grdPnBarraHerramientas.getChildren().add(btnMoverEscena);

        GridPane.setConstraints(btnDividirEnlaceCuadrado, 0, 1);
        grdPnBarraHerramientas.getChildren().add(btnDividirEnlaceCuadrado);

        GridPane.setConstraints(btnEliminar, 1, 1);
        grdPnBarraHerramientas.getChildren().add(btnEliminar);

        GridPane.setConstraints(btnMinusZoom, 0, 2);
        grdPnBarraHerramientas.getChildren().add(btnMinusZoom);

        GridPane.setConstraints(btnPlusZoom, 1, 2);
        grdPnBarraHerramientas.getChildren().add(btnPlusZoom);

        Separator separadorHerramientas = new Separator(Orientation.HORIZONTAL);
        separadorHerramientas.getStyleClass().add("separadorBarraDeHerramientas");

        GridPane.setConstraints(separadorHerramientas, 0, 3);
        separadorHerramientas.setValignment(VPos.CENTER);

        GridPane.setColumnSpan(separadorHerramientas, 2);
        grdPnBarraHerramientas.getChildren().add(separadorHerramientas);

    }

    private void creacionBtnsDeNodos(GridPane grdPnBarraHerramientas) {

        btnCliente.setToggleGroup(tgHerramientas);
        btnNodoDeServicio.setToggleGroup(tgHerramientas);
        btnEnrutadorOptico.setToggleGroup(tgHerramientas);
        btnEnrutadorDeRafaga.setToggleGroup(tgHerramientas);
        btnEnrutadorHibrido.setToggleGroup(tgHerramientas);
        btnRecurso.setToggleGroup(tgHerramientas);
        btnEnlace.setToggleGroup(tgHerramientas);

        btnCliente.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnNodoDeServicio.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnEnrutadorOptico.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnEnrutadorDeRafaga.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnEnrutadorHibrido.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnRecurso.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());
        btnEnlace.setGrupoDeDiseño(grGrupoDeDiseño.getGroup());

        btnCliente.setTooltip(new Tooltip("Nodo cliente"));
        btnNodoDeServicio.setTooltip(new Tooltip("Nodo de servicio(Middleware)"));
        btnEnrutadorOptico.setTooltip(new Tooltip("Enrutador Optico"));
        btnEnrutadorDeRafaga.setTooltip(new Tooltip("Enrutador de Ráfaga"));
        btnEnrutadorHibrido.setTooltip(new Tooltip("Enrutador Hibrido"));
        btnRecurso.setTooltip(new Tooltip("Clúster (Recurso de almacenamiento y procesamiento) "));
        btnEnlace.setTooltip(new Tooltip("Enlace Optico"));

        GridPane.setConstraints(btnCliente, 0, 4);
        grdPnBarraHerramientas.getChildren().add(btnCliente);

        GridPane.setConstraints(btnNodoDeServicio, 1, 4);
        grdPnBarraHerramientas.getChildren().add(btnNodoDeServicio);

        GridPane.setConstraints(btnEnrutadorOptico, 0, 5);
        grdPnBarraHerramientas.getChildren().add(btnEnrutadorOptico);

        GridPane.setConstraints(btnEnrutadorDeRafaga, 1, 5);
        grdPnBarraHerramientas.getChildren().add(btnEnrutadorDeRafaga);

        GridPane.setConstraints(btnEnrutadorHibrido, 0, 6);
        grdPnBarraHerramientas.getChildren().add(btnEnrutadorHibrido);

        GridPane.setConstraints(btnRecurso, 1, 6);
        grdPnBarraHerramientas.getChildren().add(btnRecurso);

        GridPane.setConstraints(btnEnlace, 0, 7);
        grdPnBarraHerramientas.getChildren().add(btnEnlace);

    }

    private void crearLienzoDeTabs() {

        

       

        tabSimulacion.setClosable(false);
        tabSimulacion.setText("Simulación");
        tabSimulacion.setClosable(false);
        tabResultados.setText("Resultados Phosphorus");
        tabResultados.setClosable(false);
        
        tabResultadosHTML.setText("Resultado Phosphorus HTML");
        tabResultadosHTML.setClosable(false);
        
        resultadosPhosphorus = new ResultadosPhosphorus(tabResultados);
        ResultadosPhosphorousHTML resultadosPhosphorousHTML = new ResultadosPhosphorousHTML(tabResultadosHTML);
        executePane.setResultadosPhosphorousHTML(resultadosPhosphorousHTML);
        executePane.setResultadosPhosphorus(resultadosPhosphorus);

        grGrupoDeDiseño.setScrollPane(scPnWorld);

        grRoot.getChildren().add(grGrupoDeDiseño.getGroup());
        scPnWorld.setContent(grRoot);
        tabSimulacion.setContent(scPnWorld);

        tpBox.getTabs().addAll(tabSimulacion);

       
    }

    private HBox createBottomDesign() {

        HBox hboxAllBottom = new HBox();
        hboxAllBottom.getStyleClass().add("cajaInferior");
        hboxAllBottom.setSpacing(10);

        TilePane tlPnLogos = createProjectsLogos();

        tbDeviceProperties = new TablaPropiedadesDispositivo();
        stPnDeviceProperties.getChildren().add(tbDeviceProperties);

        TableView<String> tbSimulationProperties = createTbSimulationProperties();
        StackPane stPnSimulationProperties = new StackPane();
        stPnSimulationProperties.getChildren().add(tbSimulationProperties);

        SplitPane splPnPropertiesTbs = new SplitPane();
        splPnPropertiesTbs.getItems().addAll(stPnDeviceProperties, stPnSimulationProperties);
        splPnPropertiesTbs.setDividerPositions(0.525f);
        
        VBox vbxBottomRight = new VBox(10);
        VBox vbxExecuteIndicatorPane = createExecuteIndicatorPane();
        crearPanelDeNavegacionMapa(vbNavegation);
        vbxBottomRight.getChildren().addAll(vbxExecuteIndicatorPane,vbNavegation);
        
        hboxAllBottom.getChildren().addAll(tlPnLogos, splPnPropertiesTbs, vbxBottomRight);
        return hboxAllBottom;
    }

    private TilePane createProjectsLogos() {

        //Imagen proyecto AG2
        ImageView imagenAG2 = new ImageView(new Image(getClass().getResourceAsStream("../../../recursos/imagenes/logoAG2.png")));
        //Imagen proyecto phophorus
        ImageView imagenPhosphorus = new ImageView(new Image(getClass().getResourceAsStream("../../../recursos/imagenes/phosphorus.jpg")));
        imagenPhosphorus.setScaleX(1.4);
        imagenPhosphorus.setScaleY(1.4);

        TilePane tlPneLogos = new TilePane();
        tlPneLogos.setPrefColumns(1);
        tlPneLogos.setPadding(new Insets(5));
        tlPneLogos.setVgap(15);
        tlPneLogos.getChildren().addAll(imagenAG2, imagenPhosphorus);

        return tlPneLogos;
    }

    private TableView<String> createTbSimulationProperties() {

        TableView<String> tbVwPropSimulacion = new TableView<String>();

        TableColumn tbColPropNombre = new TableColumn("PROPIEDAD");
        tbColPropNombre.setMinWidth(145);
        tbColPropNombre.setPrefWidth(155);
        tbColPropNombre.setCellValueFactory(new PropertyValueFactory<TipoDePropiedadesPhosphorus, String>("nombrePropiedad"));

        TableColumn tbColPropValor = new TableColumn("VALOR");
        tbColPropValor.setMinWidth(150);
        tbColPropValor.setPrefWidth(185);
        tbColPropValor.setCellValueFactory(new PropertyValueFactory<TipoDePropiedadesPhosphorus, Control>("control"));

        TableColumn tbColTituloTbSim = new TableColumn("PROPIEDADES SIMULACIÓN");
        tbColTituloTbSim.getColumns().addAll(tbColPropNombre, tbColPropValor);
        tbVwPropSimulacion.getColumns().addAll(tbColTituloTbSim);

        tbVwPropSimulacion.setItems(TipoDePropiedadesPhosphorus.getDatos());

        tbVwPropSimulacion.setMinWidth(tbColTituloTbSim.getMinWidth() + 13);
        tbVwPropSimulacion.setPrefWidth(345);

        tbVwPropSimulacion.setPrefHeight(200);

        return tbVwPropSimulacion;
    }

    public void crearPanelDeNavegacionMapa(VBox vBox) {

        gpNavegacionMapa.setPadding(new Insets(10, 10, 10, 10));
        gpNavegacionMapa.setVgap(5);
        gpNavegacionMapa.setHgap(4);
        gpNavegacionMapa.getStyleClass().add("barraDeHerramientas");
        
        Label lbTitle = new Label("LISTAS DE NAVEGACIÓN");
        lbTitle.setFont(Font.font("Cambria", FontWeight.BOLD, 14));

        final ChoiceBox cbClients = new ChoiceBox(grGrupoDeDiseño.getListaClientes());
        final ChoiceBox cbResources = new ChoiceBox(grGrupoDeDiseño.getListaRecursos());
        final ChoiceBox cbSwicthes = new ChoiceBox(grGrupoDeDiseño.getListaSwitches());
        final ChoiceBox cbServiceNodes = new ChoiceBox(grGrupoDeDiseño.getListaNodoServicio());

        cbClients.setMinWidth(100);
        cbResources.setMinWidth(100);
        cbSwicthes.setMinWidth(100);
        cbServiceNodes.setMinWidth(100);

        Button btnIrClients = new Button("ir");
        Button btnIrResources = new Button("ir");
        Button btnIrSwichtes = new Button("ir");
        Button btnIrServiceNodes = new Button("ir");

        Label lbRouters = new Label("Enrutadores");
        Label lbClientes = new Label("Clientes");
        Label lbRecursos = new Label("Recursos");
        Label lbNodosServicio = new Label("Agendadores");
        
        lbRouters.setMinWidth(80);
        lbClientes.setMinWidth(80);
        lbRecursos.setMinWidth(80);
        lbNodosServicio.setMinWidth(80);

        GridPane.setConstraints(lbTitle, 0, 1);
        GridPane.setColumnSpan(lbTitle, 3);
        GridPane.setHalignment(lbTitle, HPos.CENTER);
        gpNavegacionMapa.getChildren().add(lbTitle);        
                
        GridPane.setConstraints(lbClientes, 0, 2);
        gpNavegacionMapa.getChildren().add(lbClientes);
        GridPane.setConstraints(cbClients, 1, 2);
        gpNavegacionMapa.getChildren().add(cbClients);
        GridPane.setConstraints(btnIrClients, 2, 2);
        gpNavegacionMapa.getChildren().add(btnIrClients);

        GridPane.setConstraints(lbRecursos, 0, 3);
        gpNavegacionMapa.getChildren().add(lbRecursos);
        GridPane.setConstraints(cbResources, 1, 3);
        gpNavegacionMapa.getChildren().add(cbResources);
        GridPane.setConstraints(btnIrResources, 2, 3);
        gpNavegacionMapa.getChildren().add(btnIrResources);

        GridPane.setConstraints(lbRouters, 0, 4);
        gpNavegacionMapa.getChildren().add(lbRouters);
        GridPane.setConstraints(cbSwicthes, 1, 4);
        gpNavegacionMapa.getChildren().add(cbSwicthes);
        GridPane.setConstraints(btnIrSwichtes, 2, 4);
        gpNavegacionMapa.getChildren().add(btnIrSwichtes);

        GridPane.setConstraints(lbNodosServicio, 0, 5);
        gpNavegacionMapa.getChildren().add(lbNodosServicio);
        GridPane.setConstraints(cbServiceNodes, 1, 5);
        gpNavegacionMapa.getChildren().add(cbServiceNodes);
        GridPane.setConstraints(btnIrServiceNodes, 2, 5);
        gpNavegacionMapa.getChildren().add(btnIrServiceNodes);

        if (!vBox.getChildren().contains(gpNavegacionMapa)) {
            vBox.getChildren().add(gpNavegacionMapa);
        }

        setEventGoBtn(btnIrClients, cbClients);
        setEventGoBtn(btnIrServiceNodes, cbServiceNodes);
        setEventGoBtn(btnIrResources, cbResources);
        setEventGoBtn(btnIrSwichtes, cbSwicthes);

    }

    private void setEventGoBtn(Button goButton, final ChoiceBox chobNodes) {

        goButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                NodoGrafico selectedNode = (NodoGrafico) chobNodes.getSelectionModel().getSelectedItem();

                if (selectedNode != null) {
                    //selectedNode.seleccionar(true);

                    Scale sclEscalaDeZoom = grGrupoDeDiseño.getSclEscalaDeZoom();
                    sclEscalaDeZoom.setX(1.5);
                    sclEscalaDeZoom.setY(-1.5);

                    double worldWidth = grGrupoDeDiseño.getGroup().getBoundsInParent().getWidth();
                    double worldHeight = grGrupoDeDiseño.getGroup().getBoundsInParent().getHeight();
                    //La posicion (0,0) esta en la esquina superior izquierda
                    double posXNewCoords = (selectedNode.getLayoutX() * 1.5) + (worldWidth / 2);
                    double posYNewCoords = (selectedNode.getLayoutY() * (-1.5)) + (worldHeight / 2);

                    double posXInPercentage = posXNewCoords / worldWidth;
                    double posYInPercentage = posYNewCoords / worldHeight;

                    double maxErrorInX = scPnWorld.getViewportBounds().getWidth() / 2;
                    double funcToCalculateXError = (-2 * maxErrorInX * posXInPercentage) + maxErrorInX;
                    double percentageXError = funcToCalculateXError / worldWidth;

                    double maxErrorInY = scPnWorld.getViewportBounds().getHeight() / 2;
                    double funcToCalculateYError = (-2 * maxErrorInY * posYInPercentage) + maxErrorInY;
                    double percentageYError = funcToCalculateYError / worldHeight;

                    double percentImgHeightCorrecX = (selectedNode.getAnchoActual()) / worldWidth;
                    double percentImgHeightCorrecY = (selectedNode.getAltoActual()) / worldHeight;

                    scPnWorld.setHvalue(posXInPercentage - percentageXError + percentImgHeightCorrecX);
                    scPnWorld.setVvalue(posYInPercentage - percentageYError - percentImgHeightCorrecY);
                }
            }
        });
    }

    private void adicionarEventoDeTecladoAEscena(final Scene escena) {

        escena.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent event) {

                if ((event.isAltDown() || event.isShiftDown() || event.isControlDown())
                        && estaTeclaPrincipalOprimida == false && grGrupoDeDiseño.getGroup().isHover()) {

                    estaTeclaPrincipalOprimida = true;
                    estadoAnteriorDeBtnAEvento = IGU.getEstadoTipoBoton();
                    cursorAnteriorAEvento = grGrupoDeDiseño.getGroup().getCursor();

                    if (event.isAltDown()) {
                        IGU.setEstadoTipoBoton(TiposDeBoton.MANO);
                        grGrupoDeDiseño.getGroup().setCursor(TiposDeBoton.MANO.getImagenCursor());
                    } else if (event.isShiftDown()) {
                        IGU.setEstadoTipoBoton(TiposDeBoton.ZOOM_PLUS);
                        grGrupoDeDiseño.getGroup().setCursor(TiposDeBoton.ZOOM_PLUS.getImagenCursor());
                    } else if (event.isControlDown()) {
                        IGU.setEstadoTipoBoton(TiposDeBoton.ZOOM_MINUS);
                        grGrupoDeDiseño.getGroup().setCursor(TiposDeBoton.ZOOM_MINUS.getImagenCursor());
                    }
                }
            }
        });

        escena.setOnKeyReleased(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent event) {

                if (estaTeclaPrincipalOprimida == true) {
                    estaTeclaPrincipalOprimida = false;
                    IGU.setEstadoTipoBoton(estadoAnteriorDeBtnAEvento);
                    grGrupoDeDiseño.getGroup().setCursor(cursorAnteriorAEvento);
                }
            }
        });
    }

    public void inicializarEstadoDeIGU() {

        btnCliente.setSelected(true);
        IGU.setEstadoTipoBoton(TiposDeBoton.CLIENTE);
        grGrupoDeDiseño.getGroup().setCursor(TiposDeBoton.CLIENTE.getImagenCursor());
        scPnWorld.setHvalue(0.27151447890809266);
        scPnWorld.setVvalue(0.4661207267437006);

    }

    public GrupoDeDiseno getGrGrupoDeDiseño() {
        return grGrupoDeDiseño;
    }

    public TablaPropiedadesDispositivo getPropiedadesDispositivoTbl() {
        return tbDeviceProperties;
    }

    private VBox createExecuteIndicatorPane() {

        VBox vBoxCajaContenedoraIndicadores = new VBox(10);
        vBoxCajaContenedoraIndicadores.getStyleClass().add("barraDeHerramientas");
        vBoxCajaContenedoraIndicadores.setPadding(new Insets(10, 10, 10, 10));
        vBoxCajaContenedoraIndicadores.setAlignment(Pos.CENTER);

        Label lblIndicadoraEjec = new Label("Ejecución:");
        lblIndicadoraEjec.setFont(new Font("Arial Bold", 10));

        prgBarExecProgress = new ProgressBar(0);
        prgBarExecProgress.setPrefWidth(150);
        prgBarExecProgress.setTooltip(new Tooltip("Muestra el estado de ejecución de la simulación"));

        vBoxCajaContenedoraIndicadores.getChildren().addAll(lblIndicadoraEjec, prgBarExecProgress);

        return vBoxCajaContenedoraIndicadores;
    }

    public void habilitar() {

        IGU.setEstadoTipoBoton(estadoAnteriorDeBtnAEvento);
        grGrupoDeDiseño.getGroup().setCursor(cursorAnteriorAEvento);
        barraHerramientas.setDisable(false);
        barraHerramientas.setOpacity(1);
        //prgBarBarraProgresoEjec.setProgress(0);
        prgBarExecProgress.setVisible(false);
        grGrupoDeDiseño.getGroup().setOpacity(1);
     
        
    }

    public void deshabilitar() {
        estadoAnteriorDeBtnAEvento = IGU.getEstadoTipoBoton();
        cursorAnteriorAEvento = grGrupoDeDiseño.getGroup().getCursor();

        IGU.setEstadoTipoBoton(TiposDeBoton.MANO);
        grGrupoDeDiseño.getGroup().setCursor(TiposDeBoton.MANO.getImagenCursor());

        prgBarExecProgress.setVisible(true);
        barraHerramientas.setDisable(true);
        barraHerramientas.setOpacity(0.8);
        prgBarExecProgress.setProgress(-1);
        grGrupoDeDiseño.getGroup().setOpacity(0.8);
        tpBox.getTabs().addAll(  tabResultadosHTML, tabResultados);
        tpBox.getSelectionModel().select(tabResultados);
        
    }

    public ExecutePane getExecutePane() {
        return executePane;
    }

    public ResultadosPhosphorus getResustadosPhosphorus() {
        return resultadosPhosphorus;
    }
//    private void readObject(ObjectInputStream inputStream) {
//        try 
//        {
//            inputStream.defaultReadObject();
//            tbPropiedadesDispositivo = new TablaPropiedadesDispositivo();
//            stPnCajaPropDispositivo = new StackPane();
//            stPnCajaPropDispositivo.getChildren().add(tbPropiedadesDispositivo);
//            scPnWorld = new ScrollPane(); 
//            tgHerramientas = new ToggleGroup();
//            gpNavegacionMapa = new GridPane();
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }
//    }
}
