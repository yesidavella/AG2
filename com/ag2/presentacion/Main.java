package com.ag2.presentacion;

import com.ag2.config.serializacion.Serializador;
import com.ag2.config.TipoDePropiedadesPhosphorus;
import com.ag2.presentacion.controles.Boton;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import com.ag2.presentacion.controles.ResultadosPhosphorousHTML;
import com.ag2.presentacion.controles.ResustadosPhosphorus;
import com.ag2.presentacion.diseño.NodoDeRecursoGrafico;
import com.ag2.presentacion.diseño.NodoGrafico;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {

    ScrollPane spZonaDeDiseño = new ScrollPane();
    GrupoDeDiseno grGrupoDeDiseño = new GrupoDeDiseno(spZonaDeDiseño);
    Image iImagenFondo = new Image(getClass().getResourceAsStream("../../../recursos/imagenes/mapaMundi.jpg"));
    ImageView ivImagenFondo = new ImageView(iImagenFondo);
    ToggleGroup tgEjecucion = new ToggleGroup();
    ToggleGroup tgHerramientas = new ToggleGroup();
    Rectangle rectangle = new Rectangle(25000, 18750, Color.LIGHTBLUE);
    public static final Point2D ESQUINA_SUPERIOR_IZQ_MAPA = new Point2D(12500 - 5000, 9375 - 2617);
    GridPane gpNavegacionMapa = new GridPane();
    private Boton btnMoverEscena;
    private Boton btnCliente = new Boton(TiposDeBoton.CLIENTE);
    Boton btnNodoDeServicio = new Boton(TiposDeBoton.NODO_DE_SERVICIO);
    Boton btnEnrutadorOptico = new Boton(TiposDeBoton.ENRUTADOR_OPTICO);
    Boton btnEnrutadorDeRafaga = new Boton(TiposDeBoton.ENRUTADOR_RAFAGA);
    Boton btnEnrutadorHibrido = new Boton(TiposDeBoton.ENRUTADOR_HIBRIDO);
    Boton btnRecurso = new Boton(TiposDeBoton.RECURSO);
    Boton btnEnlace = new Boton(TiposDeBoton.ENLACE);
    private static TiposDeBoton estadoTipoBoton = TiposDeBoton.PUNTERO;
    Slider sliderZoom = new Slider();
    
    private boolean estaBloqueadoEventoDeTeclado = false;
    private TiposDeBoton estadoAnteriorDeBtnAEventoTcld;
    private Cursor cursorAnteriorAEventoTcld;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static TiposDeBoton getEstadoTipoBoton() {
        return estadoTipoBoton;
    }

    public static void setEstadoTipoBoton(TiposDeBoton tiposDeBoton) {
        estadoTipoBoton = tiposDeBoton;
    }

    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Modelo AG2- Simulador Grafico");

        BorderPane layOutVentanaPrincipal = new BorderPane();//Layout de toda la aplicacion
        layOutVentanaPrincipal.getStyleClass().add("ventanaPrincipal");

        Scene scene = new Scene(layOutVentanaPrincipal, 1280, 720);
        adicionarEventoDeTecladoAEscena(scene);
        scene.getStylesheets().add(Main.class.getResource("../../../recursos/css/IGUPrincipal.css").toExternalForm());

       //Diseño superior
        crearBarraDeMenus(layOutVentanaPrincipal, primaryStage);

        //Diseño izquierdo(contenedor de Ejecucion y herramientas)
        TilePane barraDeEjecucion = creacionBarraDeEjecucion();
        GridPane barraHerramientas = creacionBarraDeHerramientas();

        VBox contenedorHerramietas = new VBox();

        contenedorHerramietas.getChildren().addAll(barraDeEjecucion, barraHerramientas);
        layOutVentanaPrincipal.setLeft(contenedorHerramietas);

        //Diseño central
        crearLienzoDePestañas(layOutVentanaPrincipal);

        //layOutVentanaPrincipal.setRight(v);

        //Diseño inferior
        HBox cajaInferiorHor = crearImagenesYTablasDePropiedades();
        layOutVentanaPrincipal.setBottom(cajaInferiorHor);

        primaryStage.setScene(scene);
        primaryStage.show();
        
        tgHerramientas.selectToggle(btnCliente);
        Main.setEstadoTipoBoton(TiposDeBoton.CLIENTE);
        grGrupoDeDiseño.setCursor(TiposDeBoton.CLIENTE.getImagenCursor());
        
    }

    private void crearBarraDeMenus(BorderPane diseñoVentana, final Stage primaryStage) {

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

            public void handle(ActionEvent t) 
            {
                Serializador serializador = new Serializador(primaryStage); 
                serializador.guardar(grGrupoDeDiseño);
                System.out.print("cl");
//                FileChooser fileChooser = new FileChooser(); 
//                fileChooser.showOpenDialog(primaryStage); 
//               
            }
        });
        itemAbrir.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) 
            {
                Serializador serializador = new Serializador(primaryStage); 
                GrupoDeDiseno deDiseno=  serializador.cargar(); 
                
                System.out.print("cl");
//                FileChooser fileChooser = new FileChooser(); 
//                fileChooser.showOpenDialog(primaryStage); 
//               
            }
        });
       

        //La barra de menus
        MenuBar mnuBarBarraDeMenus = new MenuBar();
        mnuBarBarraDeMenus.getMenus().addAll(menuArchivo, menuAyuda);
        mnuBarBarraDeMenus.getStyleClass().add("barraDeMenus");

        hBoxContenedorDeMenu.getChildren().add(mnuBarBarraDeMenus);
        diseñoVentana.setTop(hBoxContenedorDeMenu);
    }

    private TilePane creacionBarraDeEjecucion() {

        TilePane tlPnBarraDeEjecucion = new TilePane();
        tlPnBarraDeEjecucion.getStyleClass().add("barraDeHerramientas");
        tlPnBarraDeEjecucion.setPadding(new Insets(10, 10, 10, 10));
        tlPnBarraDeEjecucion.setHgap(4);
        tlPnBarraDeEjecucion.setPrefColumns(2);

        Boton btnEjecutar = new Boton(TiposDeBoton.EJECUTAR);
        Tooltip tTipBtnEjecutar = new Tooltip("Ejecutar simulación");
        btnEjecutar.setTooltip(tTipBtnEjecutar);
        btnEjecutar.setToggleGroup(tgEjecucion);

        Boton btnParar = new Boton(TiposDeBoton.PARAR);
        Tooltip tTipBtnParar = new Tooltip("Parar simulación");
        btnParar.setTooltip(tTipBtnParar);
        btnParar.setToggleGroup(tgEjecucion);

        tlPnBarraDeEjecucion.getChildren().addAll(btnEjecutar, btnParar);

        return tlPnBarraDeEjecucion;
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

        btnMoverEscena.setGropoDeDiseño(grGrupoDeDiseño);
        btnSeleccion.setGropoDeDiseño(grGrupoDeDiseño);
        btnDividirEnlaceCuadrado.setGropoDeDiseño(grGrupoDeDiseño);
        btnEliminar.setGropoDeDiseño(grGrupoDeDiseño);

        btnMoverEscena.setToggleGroup(tgHerramientas);
        btnSeleccion.setToggleGroup(tgHerramientas);
        btnDividirEnlaceCuadrado.setToggleGroup(tgHerramientas);
        btnEliminar.setToggleGroup(tgHerramientas);

        btnMoverEscena.setTooltip(new Tooltip("Mueva el mapa a su gusto con el raton."));
        btnSeleccion.setTooltip(new Tooltip("Seleccione cualquier objeto"));
        btnDividirEnlaceCuadrado.setTooltip(new Tooltip("Añadale vertices a un enlace"));

        btnEliminar.setTooltip(new Tooltip("Elimine un objeto"));

        GridPane.setConstraints(btnSeleccion, 0, 0);
        grdPnBarraHerramientas.getChildren().add(btnSeleccion);

        GridPane.setConstraints(btnMoverEscena, 1, 0);
        grdPnBarraHerramientas.getChildren().add(btnMoverEscena);

        GridPane.setConstraints(btnDividirEnlaceCuadrado, 0, 1);
        grdPnBarraHerramientas.getChildren().add(btnDividirEnlaceCuadrado);

        GridPane.setConstraints(btnEliminar, 1, 1);
        grdPnBarraHerramientas.getChildren().add(btnEliminar);

        Separator separadorHerramientas = new Separator(Orientation.HORIZONTAL);
        separadorHerramientas.getStyleClass().add("separadorBarraDeHerramientas");

        GridPane.setConstraints(separadorHerramientas, 0, 2);
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

        btnCliente.setGropoDeDiseño(grGrupoDeDiseño);
        btnNodoDeServicio.setGropoDeDiseño(grGrupoDeDiseño);
        btnEnrutadorOptico.setGropoDeDiseño(grGrupoDeDiseño);
        btnEnrutadorDeRafaga.setGropoDeDiseño(grGrupoDeDiseño);
        btnEnrutadorHibrido.setGropoDeDiseño(grGrupoDeDiseño);
        btnRecurso.setGropoDeDiseño(grGrupoDeDiseño);
        btnEnlace.setGropoDeDiseño(grGrupoDeDiseño);

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

    private void crearLienzoDePestañas(BorderPane diseñoVentana) {

        TabPane cajaDePestañas = new TabPane();
        
        Tab pestañaSimulacion = new Tab();
        pestañaSimulacion.setText("Simulación");
        pestañaSimulacion.setClosable(false);

        Tab pestañaResultados = new Tab();
        Tab pestañaResultadosHTML = new Tab();
        pestañaResultados.setText("Resultados Phosphorus");
        pestañaResultadosHTML.setText("Resultado Phosphorus HTML");

        ResustadosPhosphorus resustadosPhosphorus = new ResustadosPhosphorus(pestañaResultados);
        resustadosPhosphorus.adicionarResultadoCliente("Cliente1 ", "12", "34", "45", "67", "89");
        resustadosPhosphorus.adicionarResultadoCliente("Cliente1 ", "12", "34", "45", "67", "89");
        resustadosPhosphorus.adicionarResultadoCliente("Cliente1 ", "12", "34", "45", "67", "89");
        resustadosPhosphorus.adicionarResultadoCliente("Cliente1 ", "12", "34", "45", "67", "89");
        resustadosPhosphorus.adicionarResultadoCliente("Cliente1 ", "12", "34", "45", "67", "89");
        resustadosPhosphorus.adicionarResultadoCliente("Cliente1 ", "12", "34", "45", "67", "89");

        resustadosPhosphorus.adicionarResultadoRecurso("Recurso 1", "09", "87", "56");
        resustadosPhosphorus.adicionarResultadoConmutador("Comutador ", "xx", "xx", "xx", "xx", "xx", "xx", "xx");


        ResultadosPhosphorousHTML resultadosPhosphorousHTML = new ResultadosPhosphorousHTML(pestañaResultadosHTML);

        ivImagenFondo.setLayoutX(ESQUINA_SUPERIOR_IZQ_MAPA.getX());
        ivImagenFondo.setLayoutY(ESQUINA_SUPERIOR_IZQ_MAPA.getY());
        grGrupoDeDiseño.getChildren().addAll(rectangle, ivImagenFondo);

        spZonaDeDiseño.setContent(grGrupoDeDiseño);

        pestañaSimulacion.setContent(spZonaDeDiseño);

        cajaDePestañas.getTabs().addAll(pestañaSimulacion, pestañaResultados, pestañaResultadosHTML);

        diseñoVentana.setCenter(cajaDePestañas);
        
        Thread thread = new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                spZonaDeDiseño.setHvalue(0.4);
                spZonaDeDiseño.setVvalue(0.513);
                
//                spZonaDeDiseño.setMaxWidth(200);
//                spZonaDeDiseño.setMaxHeight(200);
                
               spZonaDeDiseño.setFitToHeight(true);
               spZonaDeDiseño.setFitToWidth(true);
                             
       
               spZonaDeDiseño.setPrefViewportHeight(200);
            }
        });
        thread.start();
    }

    private HBox crearImagenesYTablasDePropiedades() {

        HBox hBoxCajaInferior = new HBox();
        hBoxCajaInferior.getStyleClass().add("cajaInferior");
        hBoxCajaInferior.setSpacing(10);

        TilePane tlPnLogos = creacionImagenesDeProyectos();

        TableView<ObjetoConPropiedades> tblPropiedadesDispositivo = crearTablaDePropiedadesDeDispositivo();

        TableView<String> tblPropiedadesSimulacion = crearTablaDePropiedadesDeSimulacion();

        SplitPane divisorDeTablas = new SplitPane();
        divisorDeTablas.getItems().addAll(tblPropiedadesDispositivo, tblPropiedadesSimulacion);

        HBox hBox = new HBox();
        Label lbSliderZoom = new Label(" % Nivel zoom ");


        sliderZoom.setOrientation(Orientation.HORIZONTAL);
        sliderZoom.setMin(10);
        sliderZoom.setMax(250);
        sliderZoom.setValue(100);
        sliderZoom.setShowTickLabels(true);
        sliderZoom.setMajorTickUnit(10);
        sliderZoom.setMinorTickCount(10);
        sliderZoom.setBlockIncrement(10);
        sliderZoom.setMinWidth(240);
        sliderZoom.setSnapToTicks(true);
        sliderZoom.setShowTickMarks(true);

        sliderZoom.valueProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                grGrupoDeDiseño.setScaleX(sliderZoom.getValue() / 100);
                grGrupoDeDiseño.setScaleY(sliderZoom.getValue() / 100);
                System.out.println(" slider :" + sliderZoom.getValue() + " " + sliderZoom.getValue() / 100 + " " + sliderZoom.getValue() / 100);
                System.out.println(" sp :" + spZonaDeDiseño.getHvalue() + "   " + spZonaDeDiseño.getVvalue());
            }
        });


        hBox.getChildren().addAll(lbSliderZoom, sliderZoom);
        VBox vBox = new VBox();
        vBox.getChildren().add(hBox);
        crearPanelDeNavegacionMapa(vBox);
        hBoxCajaInferior.getChildren().addAll(tlPnLogos, divisorDeTablas, vBox);
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

    private TableView<ObjetoConPropiedades> crearTablaDePropiedadesDeDispositivo() {
        //Tabla de propiedades del dispositivo
        TableView<ObjetoConPropiedades> propiedadesDispositivoTbl = new TableView<ObjetoConPropiedades>();

        propiedadesDispositivoTbl.setPrefHeight(200);
        propiedadesDispositivoTbl.setPrefWidth(400);

        TableColumn nombrePropiedadDispositivo = new TableColumn("PROPIEDAD");
        nombrePropiedadDispositivo.setCellValueFactory(new PropertyValueFactory<ObjetoConPropiedades, String>("propiedad1"));

        TableColumn valorPropiedadDispositivo = new TableColumn("VALOR");
        valorPropiedadDispositivo.setCellValueFactory(new PropertyValueFactory<ObjetoConPropiedades, CheckBox>("propiedad2"));

        TableColumn tituloTblDispositivo = new TableColumn("PROPIEDADES DISPOSITIVO");
        tituloTblDispositivo.getColumns().addAll(nombrePropiedadDispositivo, valorPropiedadDispositivo);

        propiedadesDispositivoTbl.getColumns().add(tituloTblDispositivo);

        ObservableList<ObjetoConPropiedades> datosPropiedadesDispositivo = FXCollections.observableArrayList(
                new ObjetoConPropiedades("obj1Prop1", new CheckBox("Toes")),
                new ObjetoConPropiedades("obj2Prop1", new CheckBox("hhhh")));

        propiedadesDispositivoTbl.setItems(datosPropiedadesDispositivo);

        return propiedadesDispositivoTbl;
    }

    private TableView<String> crearTablaDePropiedadesDeSimulacion() {

        TableView<String> propiedadesSimulacionTbl = new TableView<String>();
        propiedadesSimulacionTbl.setPrefHeight(200);
        propiedadesSimulacionTbl.setPrefWidth(400);

        TableColumn tcNombrePropiedad = new TableColumn("PROPIEDAD");
        tcNombrePropiedad.setMinWidth(150);
        tcNombrePropiedad.setCellValueFactory(new PropertyValueFactory<TipoDePropiedadesPhosphorus, String>("nombrePropiedad"));
        TableColumn tcValorPropiedad = new TableColumn("VALOR");
        tcValorPropiedad.setMinWidth(250);
        tcValorPropiedad.setCellValueFactory(new PropertyValueFactory<TipoDePropiedadesPhosphorus, Control>("control"));
        TableColumn tituloSimCol = new TableColumn("PROPIEDADES SIMULACIÓN");
        tituloSimCol.getColumns().addAll(tcNombrePropiedad, tcValorPropiedad);
        propiedadesSimulacionTbl.getColumns().addAll(tituloSimCol);
        propiedadesSimulacionTbl.setItems(TipoDePropiedadesPhosphorus.getDatos());
        return propiedadesSimulacionTbl;
    }

    public void crearPanelDeNavegacionMapa(VBox vBox) {

        gpNavegacionMapa.setPadding(new Insets(10, 10, 10, 10));
        gpNavegacionMapa.setVgap(5);
        gpNavegacionMapa.setHgap(4);
        gpNavegacionMapa.getStyleClass().add("barraDeHerramientas");

        final ChoiceBox cbContinentes = new ChoiceBox(FXCollections.observableArrayList(Continentes.values()));

        final ChoiceBox cbClientes = new ChoiceBox(grGrupoDeDiseño.getListaClientes());
        final ChoiceBox cbRecursos = new ChoiceBox(grGrupoDeDiseño.getListaRecursos());
        final ChoiceBox cbSwicthes = new ChoiceBox(grGrupoDeDiseño.getListaSwitches());
        final ChoiceBox cbNodosServicio = new ChoiceBox(grGrupoDeDiseño.getListaNodoServicio());

        cbContinentes.setMinWidth(150);
        cbClientes.setMinWidth(150);
        cbRecursos.setMinWidth(150);
        cbSwicthes.setMinWidth(150);
        cbNodosServicio.setMinWidth(150);


        Button btnIrContinentes = new Button("ir");
        Button btnIrClientes = new Button("ir");
        Button btnIrRecursos = new Button("ir");
        Button btnIrSwichtes = new Button("ir");
        Button btnIrNodoServicio = new Button("ir");

        Label lbContinentes = new Label("Continentes");
        Label lbRouters = new Label("Enrutadores");
        Label lbClientes = new Label("Clientes");
        Label lbRecursos = new Label("Recursos");
        Label lbNodosServicio = new Label("Agendadores");


        GridPane.setConstraints(lbContinentes, 0, 0);
        gpNavegacionMapa.getChildren().add(lbContinentes);
        GridPane.setConstraints(cbContinentes, 1, 0);
        gpNavegacionMapa.getChildren().add(cbContinentes);
        GridPane.setConstraints(btnIrContinentes, 2, 0);
        gpNavegacionMapa.getChildren().add(btnIrContinentes);

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

        btnIrContinentes.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {

                if (cbContinentes.getSelectionModel().getSelectedItem() instanceof Continentes) {
                    Continentes continente = (Continentes) cbContinentes.getSelectionModel().getSelectedItem();
                    System.out.println(continente.toString());
                    switch (continente) {
                        case AFRICA: {
                            sliderZoom.setValue(30);
                            spZonaDeDiseño.setHvalue(0.500d);
                            spZonaDeDiseño.setVvalue(0.503d);

                            break;
                        }
                        case ASIA: {
                            sliderZoom.setValue(30);
                            spZonaDeDiseño.setHvalue(0.529d);
                            spZonaDeDiseño.setVvalue(0.496d);
                            break;
                        }
                        case CENTRO_AMERICA: {
                            sliderZoom.setValue(60);
                            spZonaDeDiseño.setHvalue(0.427d);
                            spZonaDeDiseño.setVvalue(0.503d);
                            break;
                        }
                        case EUROPA: {
                            sliderZoom.setValue(65);
                            spZonaDeDiseño.setHvalue(0.501d);
                            spZonaDeDiseño.setVvalue(0.469d);

                            break;
                        }
                        case NORTE_AMERICA: {
                            sliderZoom.setValue(40);
                            spZonaDeDiseño.setHvalue(0.443d);
                            spZonaDeDiseño.setVvalue(0.484d);
                            break;
                        }
                        case OCEANIA: {
                            sliderZoom.setValue(50);
                            spZonaDeDiseño.setHvalue(0.569d);
                            spZonaDeDiseño.setVvalue(0.542d);
                            break;
                        }
                        case SUR_AMERICA: {
                            sliderZoom.setValue(30);
                            spZonaDeDiseño.setHvalue(0.476d);
                            spZonaDeDiseño.setVvalue(0.515d);
                            break;
                        }
                    }
                    grGrupoDeDiseño.setScaleX(sliderZoom.getValue() / 100);
                    grGrupoDeDiseño.setScaleY(sliderZoom.getValue() / 100);
                }
            }
        });


        establecerEventoBotonesIr(btnIrClientes, cbClientes);
        establecerEventoBotonesIr(btnIrNodoServicio, cbNodosServicio);
        establecerEventoBotonesIr(btnIrRecursos, cbRecursos);
        establecerEventoBotonesIr(btnIrSwichtes, cbSwicthes);

        cbRecursos.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                if (cbRecursos.getSelectionModel().getSelectedItem() instanceof NodoDeRecursoGrafico) {
                    NodoDeRecursoGrafico nodoDeRecursoGrafico = (NodoDeRecursoGrafico) cbRecursos.getSelectionModel().getSelectedItem();
                    System.out.println(nodoDeRecursoGrafico.getLayoutX() + " " + nodoDeRecursoGrafico.getLayoutY());
                }
            }
        });
    }

    private void establecerEventoBotonesIr(Button button, final ChoiceBox cbClientes) 
    {
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                if (cbClientes.getSelectionModel().getSelectedItem() instanceof NodoGrafico) {
                    NodoGrafico nodoGrafico = (NodoGrafico) cbClientes.getSelectionModel().getSelectedItem();

                    sliderZoom.setValue(100);
                    spZonaDeDiseño.setHvalue(nodoGrafico.getLayoutX() / 25000);
                    spZonaDeDiseño.setVvalue(nodoGrafico.getLayoutY() / 18750);
                    grGrupoDeDiseño.setScaleX(sliderZoom.getValue() / 100);
                    grGrupoDeDiseño.setScaleY(sliderZoom.getValue() / 100);
                }
            }
        });
    }

    private void adicionarEventoDeTecladoAEscena(final Scene escena) {
        
        escena.setOnKeyPressed(new EventHandler<KeyEvent>(){

            public void handle(KeyEvent event) {

                if(estaBloqueadoEventoDeTeclado==false && event.isControlDown() && grGrupoDeDiseño.isHover() ){
                    estaBloqueadoEventoDeTeclado=true;
                    
                    estadoAnteriorDeBtnAEventoTcld = Main.getEstadoTipoBoton();
                    cursorAnteriorAEventoTcld = grGrupoDeDiseño.getCursor();
                    
                    Main.setEstadoTipoBoton(TiposDeBoton.MANO);
                    grGrupoDeDiseño.setCursor(TiposDeBoton.MANO.getImagenCursor());
                }   
            }
        });
        
        escena.setOnKeyReleased(new EventHandler<KeyEvent>(){

            public void handle(KeyEvent event) {
                
                if(estaBloqueadoEventoDeTeclado=true){
                    estaBloqueadoEventoDeTeclado=false;
                    Main.setEstadoTipoBoton(estadoAnteriorDeBtnAEventoTcld);
                    grGrupoDeDiseño.setCursor(cursorAnteriorAEventoTcld);
                }
            }
        });
    }
}