package com.ag2.presentacion;

import com.ag2.config.TipoDePropiedadesPhosphorus;
import com.ag2.presentacion.controles.Boton;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import com.ag2.presentacion.controles.ResultadosPhosphorousHTML;
import com.ag2.presentacion.controles.ResultadosPhosphorus;
import com.ag2.presentacion.diseño.NodoGrafico;
import com.ag2.presentacion.diseño.propiedades.TablaPropiedadesDispositivo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class IGU extends Scene implements Serializable {

    private GrupoDeDiseno grGrupoDeDiseño = new GrupoDeDiseno();
    private transient ToggleGroup tgHerramientas; 
    private transient GridPane gpNavegacionMapa;
    private ExecutePane executePane = new ExecutePane(grGrupoDeDiseño);
    private transient Boton btnMoverEscena;
    private transient Boton btnCliente = new Boton(TiposDeBoton.CLIENTE);
    private transient Boton btnNodoDeServicio = new Boton(TiposDeBoton.NODO_DE_SERVICIO);
    private transient Boton btnEnrutadorOptico = new Boton(TiposDeBoton.ENRUTADOR_OPTICO);
    private transient Boton btnEnrutadorDeRafaga = new Boton(TiposDeBoton.ENRUTADOR_RAFAGA);
    private transient Boton btnEnrutadorHibrido = new Boton(TiposDeBoton.ENRUTADOR_HIBRIDO);
    private transient Boton btnRecurso = new Boton(TiposDeBoton.RECURSO);
    private transient Boton btnEnlace = new Boton(TiposDeBoton.ENLACE);
    private static TiposDeBoton estadoTipoBoton = TiposDeBoton.PUNTERO;
    private transient TablaPropiedadesDispositivo tbPropiedadesDispositivo;
    private transient GridPane barraHerramientas;
    private transient ScrollPane scPnWorld;
    private transient ProgressBar prgBarBarraProgresoEjec;
    private boolean estaTeclaPrincipalOprimida = false;
    private transient TiposDeBoton estadoAnteriorDeBtnAEvento;
    private transient Cursor cursorAnteriorAEvento;
    private transient ResultadosPhosphorus resultadosPhosphorus;
    private static IGU iguAG2;
    private Main main;
    private transient StackPane stPnCajaPropDispositivo = new StackPane();

    public ScrollPane getScPnWorld() {
        return scPnWorld;
    }
    private Stage stgEscenario;

    public static IGU getInstance() {

        if (iguAG2 == null) {
            iguAG2 = new IGU(new BorderPane(), 1200, 800);
        }
        return iguAG2;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    private IGU(BorderPane layOutVentanaPrincipal, double anchoVentana, double altoVentana) 
    {
        super(layOutVentanaPrincipal, anchoVentana, altoVentana);
        scPnWorld = new ScrollPane();
        tgHerramientas = new ToggleGroup();
        gpNavegacionMapa = new GridPane();
        
        adicionarEventoDeTecladoAEscena(this);
        getStylesheets().add(IGU.class.getResource("../../../recursos/css/IGUPrincipal.css").toExternalForm());

        layOutVentanaPrincipal.getStyleClass().add("ventanaPrincipal");

        //Diseño superior
        crearBarraDeMenus(layOutVentanaPrincipal, stgEscenario);

        //Diseño izquierdo(contenedor de Ejecucion y herramientas)
        barraHerramientas = creacionBarraDeHerramientas();
        VBox vBoxContenedorIndicadoresEjec = crearElemsIndicadoresEjecucion();

        VBox contenedorHerramietas = new VBox();
        contenedorHerramietas.getChildren().addAll(executePane, barraHerramientas, vBoxContenedorIndicadoresEjec);
        layOutVentanaPrincipal.setLeft(contenedorHerramietas);

        //Diseño central
        TabPane cajaDetabs = crearLienzoDeTabs();
        layOutVentanaPrincipal.setCenter(cajaDetabs);
        //Diseño inferior
        HBox cajaInferiorHor = crearImagenesYTablasDePropiedades();
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

                main.save();

            }
        });
        itemAbrir.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {

                main.load();
                //  GrupoDeDiseno grupoDeDiseno = serializador.cargar();
                //grupoDeDiseno.getChildren().addAll(rectangle, ivImagenFondo);
                //   ivImagenFondo.toBack();
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
        Boton btnSeleccion = new Boton(TiposDeBoton.PUNTERO);
        Boton btnDividirEnlaceCuadrado = new Boton(TiposDeBoton.ADICIONAR_VERTICE);
        Boton btnEliminar = new Boton(TiposDeBoton.ELIMINAR);
        Boton btnMinusZoom = new Boton(TiposDeBoton.ZOOM_MINUS);
        Boton btnPlusZoom = new Boton(TiposDeBoton.ZOOM_PLUS);

        btnMoverEscena.setGrupoDeDiseño(grGrupoDeDiseño);
        btnSeleccion.setGrupoDeDiseño(grGrupoDeDiseño);
        btnDividirEnlaceCuadrado.setGrupoDeDiseño(grGrupoDeDiseño);
        btnEliminar.setGrupoDeDiseño(grGrupoDeDiseño);
        btnMinusZoom.setGrupoDeDiseño(grGrupoDeDiseño);
        btnPlusZoom.setGrupoDeDiseño(grGrupoDeDiseño);

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

        btnCliente.setGrupoDeDiseño(grGrupoDeDiseño);
        btnNodoDeServicio.setGrupoDeDiseño(grGrupoDeDiseño);
        btnEnrutadorOptico.setGrupoDeDiseño(grGrupoDeDiseño);
        btnEnrutadorDeRafaga.setGrupoDeDiseño(grGrupoDeDiseño);
        btnEnrutadorHibrido.setGrupoDeDiseño(grGrupoDeDiseño);
        btnRecurso.setGrupoDeDiseño(grGrupoDeDiseño);
        btnEnlace.setGrupoDeDiseño(grGrupoDeDiseño);

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

    private TabPane crearLienzoDeTabs() {

        TabPane cajaDetabs = new TabPane();

        Tab tabSimulacion = new Tab();
        Tab tabResultados = new Tab();
        Tab tabResultadosHTML = new Tab();

        tabSimulacion.setClosable(false);
        tabSimulacion.setText("Simulación");
        tabResultados.setText("Resultados Phosphorus");
        tabResultadosHTML.setText("Resultado Phosphorus HTML");

        Group grRoot = new Group();

        resultadosPhosphorus = new ResultadosPhosphorus(tabResultados);
        ResultadosPhosphorousHTML resultadosPhosphorousHTML = new ResultadosPhosphorousHTML(tabResultadosHTML);
        executePane.setResultadosPhosphorousHTML(resultadosPhosphorousHTML);
        executePane.setResultadosPhosphorus(resultadosPhosphorus);

        grGrupoDeDiseño.setScrollPane(scPnWorld);

        grRoot.getChildren().add(grGrupoDeDiseño);
        scPnWorld.setContent(grRoot);
        tabSimulacion.setContent(scPnWorld);

        cajaDetabs.getTabs().addAll(tabSimulacion, tabResultados, tabResultadosHTML);

        return cajaDetabs;
    }

    private HBox crearImagenesYTablasDePropiedades() {

        HBox hBoxCajaInferior = new HBox();
        hBoxCajaInferior.getStyleClass().add("cajaInferior");
        hBoxCajaInferior.setSpacing(10);

        TilePane tlPnLogos = creacionImagenesDeProyectos();

        tbPropiedadesDispositivo = new TablaPropiedadesDispositivo();
        ///  StackPane stPnCajaPropDispositivo = new StackPane();
        stPnCajaPropDispositivo.getChildren().add(tbPropiedadesDispositivo);

        TableView<String> tblPropiedadesSimulacion = crearTablaDePropiedadesDeSimulacion();
        StackPane stPnCajaPropSimulacion = new StackPane();
        stPnCajaPropSimulacion.getChildren().add(tblPropiedadesSimulacion);

        SplitPane splPnCajaTablasDeProp = new SplitPane();
        splPnCajaTablasDeProp.getItems().addAll(stPnCajaPropDispositivo, stPnCajaPropSimulacion);
        splPnCajaTablasDeProp.setDividerPositions(0.525f);

        VBox vbCajaNavegacion = new VBox();
        crearPanelDeNavegacionMapa(vbCajaNavegacion);
        hBoxCajaInferior.getChildren().addAll(tlPnLogos, splPnCajaTablasDeProp, vbCajaNavegacion);
        return hBoxCajaInferior;
    }

    private TilePane creacionImagenesDeProyectos() {

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

    private TableView<String> crearTablaDePropiedadesDeSimulacion() {

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

        final ChoiceBox cbClientes = new ChoiceBox(grGrupoDeDiseño.getListaClientes());
        final ChoiceBox cbRecursos = new ChoiceBox(grGrupoDeDiseño.getListaRecursos());
        final ChoiceBox cbSwicthes = new ChoiceBox(grGrupoDeDiseño.getListaSwitches());
        final ChoiceBox cbNodosServicio = new ChoiceBox(grGrupoDeDiseño.getListaNodoServicio());

        cbClientes.setMinWidth(150);
        cbRecursos.setMinWidth(150);
        cbSwicthes.setMinWidth(150);
        cbNodosServicio.setMinWidth(150);

        Button btnIrClientes = new Button("ir");
        Button btnIrRecursos = new Button("ir");
        Button btnIrSwichtes = new Button("ir");
        Button btnIrNodoServicio = new Button("ir");

        Label lbRouters = new Label("Enrutadores");
        Label lbClientes = new Label("Clientes");
        Label lbRecursos = new Label("Recursos");
        Label lbNodosServicio = new Label("Agendadores");

        GridPane.setConstraints(lbClientes, 0, 2);
        gpNavegacionMapa.getChildren().add(lbClientes);
        GridPane.setConstraints(cbClientes, 1, 2);
        gpNavegacionMapa.getChildren().add(cbClientes);
        GridPane.setConstraints(btnIrClientes, 2, 2);
        gpNavegacionMapa.getChildren().add(btnIrClientes);

        GridPane.setConstraints(lbRecursos, 0, 3);
        gpNavegacionMapa.getChildren().add(lbRecursos);
        GridPane.setConstraints(cbRecursos, 1, 3);
        gpNavegacionMapa.getChildren().add(cbRecursos);
        GridPane.setConstraints(btnIrRecursos, 2, 3);
        gpNavegacionMapa.getChildren().add(btnIrRecursos);

        GridPane.setConstraints(lbRouters, 0, 4);
        gpNavegacionMapa.getChildren().add(lbRouters);
        GridPane.setConstraints(cbSwicthes, 1, 4);
        gpNavegacionMapa.getChildren().add(cbSwicthes);
        GridPane.setConstraints(btnIrSwichtes, 2, 4);
        gpNavegacionMapa.getChildren().add(btnIrSwichtes);

        GridPane.setConstraints(lbNodosServicio, 0, 5);
        gpNavegacionMapa.getChildren().add(lbNodosServicio);
        GridPane.setConstraints(cbNodosServicio, 1, 5);
        gpNavegacionMapa.getChildren().add(cbNodosServicio);
        GridPane.setConstraints(btnIrNodoServicio, 2, 5);
        gpNavegacionMapa.getChildren().add(btnIrNodoServicio);

        vBox.getChildren().add(gpNavegacionMapa);

        establecerEventoBotonesIr(btnIrClientes, cbClientes);
        establecerEventoBotonesIr(btnIrNodoServicio, cbNodosServicio);
        establecerEventoBotonesIr(btnIrRecursos, cbRecursos);
        establecerEventoBotonesIr(btnIrSwichtes, cbSwicthes);

    }

    private void establecerEventoBotonesIr(Button goButton, final ChoiceBox chobNodes) {

        goButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                NodoGrafico selectedNode = (NodoGrafico) chobNodes.getSelectionModel().getSelectedItem();

                if (selectedNode != null) {

                    Scale sclEscalaDeZoom = grGrupoDeDiseño.getSclEscalaDeZoom();
                    sclEscalaDeZoom.setX(1.5);
                    sclEscalaDeZoom.setY(-1.5);

                    double worldWidth = grGrupoDeDiseño.getBoundsInParent().getWidth();
                    double worldHeight = grGrupoDeDiseño.getBoundsInParent().getHeight();
                    //La posicion (0,0) esta en la esquina superior izquierda
                    double posXNewCoords = (selectedNode.getPosX() * 1.5) + (worldWidth / 2);
                    double posYNewCoords = (selectedNode.getPosY() * (-1.5)) + (worldHeight / 2);

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
                        && estaTeclaPrincipalOprimida == false && grGrupoDeDiseño.isHover()) {

                    estaTeclaPrincipalOprimida = true;
                    estadoAnteriorDeBtnAEvento = IGU.getEstadoTipoBoton();
                    cursorAnteriorAEvento = grGrupoDeDiseño.getCursor();

                    if (event.isAltDown()) {
                        IGU.setEstadoTipoBoton(TiposDeBoton.MANO);
                        grGrupoDeDiseño.setCursor(TiposDeBoton.MANO.getImagenCursor());
                    } else if (event.isShiftDown()) {
                        IGU.setEstadoTipoBoton(TiposDeBoton.ZOOM_PLUS);
                        grGrupoDeDiseño.setCursor(TiposDeBoton.ZOOM_PLUS.getImagenCursor());
                    } else if (event.isControlDown()) {
                        IGU.setEstadoTipoBoton(TiposDeBoton.ZOOM_MINUS);
                        grGrupoDeDiseño.setCursor(TiposDeBoton.ZOOM_MINUS.getImagenCursor());
                    }
                }
            }
        });

        escena.setOnKeyReleased(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent event) {

                if (estaTeclaPrincipalOprimida == true) {
                    estaTeclaPrincipalOprimida = false;
                    IGU.setEstadoTipoBoton(estadoAnteriorDeBtnAEvento);
                    grGrupoDeDiseño.setCursor(cursorAnteriorAEvento);
                }
            }
        });
    }

    public void inicializarEstadoDeIGU() {

        btnCliente.setSelected(true);
        IGU.setEstadoTipoBoton(TiposDeBoton.CLIENTE);
        grGrupoDeDiseño.setCursor(TiposDeBoton.CLIENTE.getImagenCursor());
        scPnWorld.setHvalue(0.27151447890809266);
        scPnWorld.setVvalue(0.4661207267437006);

    }

    public GrupoDeDiseno getGrGrupoDeDiseño() {
        return grGrupoDeDiseño;
    }

    public TablaPropiedadesDispositivo getPropiedadesDispositivoTbl() {
        return tbPropiedadesDispositivo;
    }

    private VBox crearElemsIndicadoresEjecucion() {

        VBox vBoxCajaContenedoraIndicadores = new VBox(10);
        vBoxCajaContenedoraIndicadores.getStyleClass().add("barraDeHerramientas");
        vBoxCajaContenedoraIndicadores.setPadding(new Insets(10, 10, 10, 10));
        vBoxCajaContenedoraIndicadores.setAlignment(Pos.CENTER);

        Label lblIndicadoraEjec = new Label("Ejecución:");
        lblIndicadoraEjec.setFont(new Font("Arial Bold", 10));

        prgBarBarraProgresoEjec = new ProgressBar(0);
        prgBarBarraProgresoEjec.setPrefWidth(70);
        prgBarBarraProgresoEjec.setTooltip(new Tooltip("Muestra el estado de ejecución de la simulación"));

        vBoxCajaContenedoraIndicadores.getChildren().addAll(lblIndicadoraEjec, prgBarBarraProgresoEjec);

        return vBoxCajaContenedoraIndicadores;
    }

    public void habilitar() {

        IGU.setEstadoTipoBoton(estadoAnteriorDeBtnAEvento);
        grGrupoDeDiseño.setCursor(cursorAnteriorAEvento);
        barraHerramientas.setDisable(false);
        barraHerramientas.setOpacity(1);
        //prgBarBarraProgresoEjec.setProgress(0);
        prgBarBarraProgresoEjec.setVisible(false);
        grGrupoDeDiseño.setOpacity(1);
    }

    public void deshabilitar() {
        estadoAnteriorDeBtnAEvento = IGU.getEstadoTipoBoton();
        cursorAnteriorAEvento = grGrupoDeDiseño.getCursor();

        IGU.setEstadoTipoBoton(TiposDeBoton.MANO);
        grGrupoDeDiseño.setCursor(TiposDeBoton.MANO.getImagenCursor());

        prgBarBarraProgresoEjec.setVisible(true);
        barraHerramientas.setDisable(true);
        barraHerramientas.setOpacity(0.8);
        prgBarBarraProgresoEjec.setProgress(-1);
        grGrupoDeDiseño.setOpacity(0.8);
    }

    public ExecutePane getExecutePane() {
        return executePane;
    }

    public ResultadosPhosphorus getResustadosPhosphorus() {
        return resultadosPhosphorus;
    }

    private void readObject(ObjectInputStream inputStream) {
        try 
        {
            inputStream.defaultReadObject();
            tbPropiedadesDispositivo = new TablaPropiedadesDispositivo();
            stPnCajaPropDispositivo = new StackPane();
            stPnCajaPropDispositivo.getChildren().add(tbPropiedadesDispositivo);
            scPnWorld = new ScrollPane(); 
            tgHerramientas = new ToggleGroup();
            gpNavegacionMapa = new GridPane();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
     private void writeObject(ObjectOutputStream   objectOutputStream)
    {
        try {
            objectOutputStream.defaultWriteObject();
        } catch (IOException ex) {
            Logger.getLogger(GrupoDeDiseno.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
