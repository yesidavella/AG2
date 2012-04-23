package com.ag2.presentation;

import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.presentation.control.*;
import com.ag2.presentation.design.GraphDesignGroup;
import com.ag2.presentation.design.GraphNode;
import com.ag2.presentation.design.property.EntityPropertyTableView;
import com.ag2.util.ResourcesPath;
import java.awt.Desktop;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.swing.JOptionPane;

public class GUI extends Scene implements Serializable {

    private static ActionTypeEmun actionTypeEmun = ActionTypeEmun.POINTER;
    private VBox vbLogos = createProjectsLogos();
    private GraphDesignGroup graphDesignGroup = new GraphDesignGroup();
    private ToggleGroup tgTools;
    private GridPane gpMapNavegation;
    private ExecutePane executePane;
    private ToggleButtonAg2 btnHand;
    private ToggleButtonAg2 btnClient = new ToggleButtonAg2(ActionTypeEmun.CLIENT);
    private ToggleButtonAg2 btnBroker = new ToggleButtonAg2(ActionTypeEmun.BROKER);
    private ToggleButtonAg2 btnPCE_Switch = new ToggleButtonAg2(ActionTypeEmun.PCE_SWITCH);
    //  private ToggleButtonAg2 btnOBS_Switch = new ToggleButtonAg2(ActionTypeEmun.OBS_SWITCH);
    private ToggleButtonAg2 btnHybridSwitch = new ToggleButtonAg2(ActionTypeEmun.HRYDRID_SWITCH);
    private ToggleButtonAg2 btnResource = new ToggleButtonAg2(ActionTypeEmun.RESOURCE);
    private ToggleButtonAg2 btnLink = new ToggleButtonAg2(ActionTypeEmun.LINK);
    private EntityPropertyTableView entityPropertyTable;
    private GridPane gpTools;
    private ScrollPane scpWorld;
    private ProgressIndicator progressIndicator;
    private boolean isPrincipalKeyPressed = false;
    private ActionTypeEmun beforeActionTypeEmun;
    private Cursor beforeEventCursor;
    private PhosphosrusResults phosphosrusResults;
    private static GUI iguAG2;
    private Main main;
    private ToggleButtonAg2 btnSelection;
    private ToggleButtonAg2 btnPointSeparator;
    private ToggleButtonAg2 btnDeleted;
    private ToggleButtonAg2 btnMinusZoom;
    private ToggleButtonAg2 btnPlusZoom;
    private Group grRootWorld = new Group();
    private VBox vbNavegation = new VBox();
    private Tab tabSimulation = new Tab();
    private Tab tabResults = new Tab();
    private Tab tabChartsResourceCPU = new Tab();
    private Tab tabChartsResourceBuffer = new Tab();
    private Tab tabResultsHTML = new Tab();
    private TabPane tabPane = new TabPane();
    private TableView<String> tbwSimulationProperties = new TableView<String>();
    private static Stage stage;
    private transient ToolBar tobWindow;
    private double mouseDragOffsetX = 0;
    private double mouseDragOffsetY = 0;
    private SplitPane splitPane;
    private WindowButtons windowButtons;
    private StackPane stpLayer;
    private BorderPane brpRoot;
    private StackPane modalDimmer;
    private HBox hBoxProgressIndicator = new HBox();
    private ChartsResultsCPU chartsResultsCPU;
    private ChartsResultsBuffer chartsResultsBuffer;
    private ScrollPane scpnProperties;
    private Timeline tlProperties = new Timeline();
    private boolean showProperties = false;
    private ScrollPane scpMenuTools;
    private SimulationOptionSwitcher simulationOptionSwitcher = new SimulationOptionSwitcher();
    private WindowResizeButton windowResizeButton;
    private Timeline tlPropertiesSmall = new Timeline();

    private GUI(StackPane stpLayer, double width, double height) {
        super(stpLayer, width, height);

        getStylesheets().add(ResourcesPath.ABS_PATH_CSS + "cssAG2.css");

        if (!Main.IS_APPLET) {
            stage.initStyle(StageStyle.UNDECORATED);
            // create window resize button
            windowResizeButton = new WindowResizeButton(stage, 800, 600);
            // create root
            brpRoot = new BorderPane() {

                @Override
                protected void layoutChildren() {
                    super.layoutChildren();
                    windowResizeButton.autosize();
                    windowResizeButton.setLayoutX(getWidth() - windowResizeButton.getLayoutBounds().getWidth());
                    windowResizeButton.setLayoutY(getHeight() - windowResizeButton.getLayoutBounds().getHeight());
                }
            };
            brpRoot.getStyleClass().add("ventanaPrincipal");
        } else {
            brpRoot = new BorderPane();
            brpRoot.getStyleClass().add("applet");
        }

        addScene(this);

        createSceneBody();

        modalDimmer = new StackPane();
        modalDimmer.setId("ModalDimmer");
        modalDimmer.setVisible(false);
        stpLayer.getChildren().add(brpRoot);
        stpLayer.getChildren().add(modalDimmer);

        if (!Main.IS_APPLET) {
            brpRoot.getChildren().add(windowResizeButton);
        }
    }

    public static GUI getInstance() {

        if (iguAG2 == null) {
            iguAG2 = new GUI(new StackPane(), 1100, 720);//new BorderPane()
        }
        return iguAG2;
    }

    private void createSceneBody() {
        //DiseÃ±o superior
        createTopDesign(brpRoot);

        scpWorld = new ScrollPane();
        tgTools = new ToggleGroup();
        splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);
        gpMapNavegation = new GridPane();
        executePane = new ExecutePane();
        executePane.setGroup(graphDesignGroup.getGroup());
        executePane.setSimulationOptionSwitcher(simulationOptionSwitcher);

        //DiseÃ±o izquierdo(contenedor de Ejecucion y herramientas)
        scpMenuTools = new ScrollPane();
        scpMenuTools.setId("scrollPane-menu-tools");
        VBox contenedorHerramietas = new VBox();
        gpTools = createToolsBar();

//        contenedorHerramietas.setMaxWidth(50);
//        contenedorHerramietas.setMinWidth(50);
//        contenedorHerramietas.setPrefWidth(50);
//        contenedorHerramietas.setPadding(new Insets(3));
        contenedorHerramietas.setAlignment(Pos.CENTER);
        scpMenuTools.setFitToWidth(true);
        scpMenuTools.setFitToHeight(true);

        settingupProgressIndicator();

        contenedorHerramietas.getChildren().addAll(executePane, gpTools, simulationOptionSwitcher, hBoxProgressIndicator, vbLogos);
        scpMenuTools.setContent(contenedorHerramietas);
        brpRoot.setLeft(scpMenuTools);

        //Diseño central
        scpnProperties = createBottomDesign();

        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        splitPane.setOrientation(Orientation.HORIZONTAL);
        setSplitPaneAnimation();

        createTabs();
        brpRoot.setCenter(tabPane);
    }

    private void setSplitPaneAnimation() {


       
      

        KeyFrame keyFrameSmall = new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {

                double widthProperties = scpnProperties.getBoundsInParent().getWidth();
                System.out.println(" small " + widthProperties + " showProperties " + showProperties);
                double increment;
                if (showProperties) {
                    increment = -0.0002;

                    if (!(widthProperties <= 402)) {
                        tlPropertiesSmall.stop();
                        return;
                    }
                } else {
                    increment = 0.00003;

                    if (!(widthProperties > 46)) {

                        tlPropertiesSmall.stop();
                        return;
                    }
                }
                splitPane.setDividerPosition(0, splitPane.getDividerPositions()[0] + increment);
            }
        });


        KeyFrame keyFrame = new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {

                double widthProperties = scpnProperties.getBoundsInParent().getWidth();
                System.out.println(" grand " + widthProperties + " showProperties " + showProperties);
                double increment;
                if (showProperties) {
                    increment = -0.0005;

                    if (!(widthProperties <= 350)) {
                        tlProperties.stop();
                        tlPropertiesSmall.play();

                        return;

                    }
                } else {
                    increment = 0.0003;

                    if (!(widthProperties > 90)) {
                        tlProperties.stop();
                        tlPropertiesSmall.play();
                        return;
                    }
                }
                splitPane.setDividerPosition(0, splitPane.getDividerPositions()[0] + increment);
            }
        });

        tlProperties.setCycleCount(50000);
        tlProperties.getKeyFrames().add(keyFrame);

        tlPropertiesSmall.setCycleCount(50000);
        tlPropertiesSmall.getKeyFrames().add(keyFrameSmall);
        
         brpRoot.widthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

                System.out.println(arg2.doubleValue());

                if (showProperties) 
                {
                    final double y = (-arg2.doubleValue() / 30) + 80;
                 
                    
                     Runnable runnable = new Runnable() {

                        @Override
                        public void run() 
                        {
                             System.out.println("slip show");
                             double value = 100-y;
                                System.out.println("y " + value );
                            splitPane.setDividerPosition(0, value/100);
                        }
                    };
                    Platform.runLater(runnable);

                } else 
                {               

                   
                    Runnable runnable = new Runnable() {

                        @Override
                        public void run() 
                        {
                             System.out.println("slip hide");
                            splitPane.setDividerPosition(0, .95);
                            tlPropertiesSmall.play();
                        }
                    };
                    Platform.runLater(runnable);
                    

                }

            }
        });

    }

    private void settingupProgressIndicator() {
        hBoxProgressIndicator.setPadding(new Insets(5));
        hBoxProgressIndicator.setAlignment(Pos.CENTER);
        hBoxProgressIndicator.getStyleClass().add("boxLogosHorizontalGradient");
        DropShadow dropShadow = new DropShadow();
        dropShadow.setSpread(0.5);
        dropShadow.setColor(Color.WHITESMOKE);
        progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(50, 50);
        progressIndicator.setMaxSize(50, 50);
        progressIndicator.setMinSize(50, 50);
        progressIndicator.setProgress(-1);
        progressIndicator.setEffect(dropShadow);
        hBoxProgressIndicator.setVisible(false);
        hBoxProgressIndicator.getChildren().add(progressIndicator);
    }

    public ScrollPane getScpMenuTools() {
        return scpMenuTools;
    }

    private void createTitleBar() {
        tobWindow = new ToolBar();
        tobWindow.setId("mainToolBar");

        Label lblTitle = new Label("Simulador de infraestructura de grillas ópticas AG2");
        lblTitle.setId("titleApplication");
        Reflection r = new Reflection();
        r.setTopOffset(-4);
        lblTitle.setEffect(r);
//        lblTitle.setTranslateX(400);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        tobWindow.getItems().addAll(lblTitle, spacer);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        tobWindow.getItems().add(spacer2);

        tobWindow.setPrefHeight(50);
        tobWindow.setMinHeight(50);
        tobWindow.setMaxHeight(50);
        stage.initStyle(StageStyle.UNDECORATED);
        windowButtons = new WindowButtons(stage, stpLayer);

        tobWindow.getItems().add(windowButtons);

        tobWindow.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    windowButtons.toogleMaximized();
                }
            }
        });
        // add window dragging
        tobWindow.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                mouseDragOffsetX = event.getSceneX();
                mouseDragOffsetY = event.getSceneY();

            }
        });
        tobWindow.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (!windowButtons.isMaximized()) {
                    stage.setX(event.getScreenX() - mouseDragOffsetX);
                    stage.setY(event.getScreenY() - mouseDragOffsetY);
                }
            }
        });

    }

    public ScrollPane getScPnWorld() {
        return scpWorld;
    }

    public EntityPropertyTableView getEntityPropertyTable() {
        return entityPropertyTable;
    }

    public void setMain(Main main) {
        this.main = main;
        windowButtons.setMain(main);
    }

    public void loadGraphDesignGroup(GraphDesignGroup graphDesignGroup) {

        grRootWorld.getChildren().remove(this.graphDesignGroup.getGroup());
        this.graphDesignGroup = graphDesignGroup;
        grRootWorld.getChildren().add(graphDesignGroup.getGroup());

        executePane.setGroup(graphDesignGroup.getGroup());
        btnHand.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnSelection.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnPointSeparator.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnDeleted.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnMinusZoom.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnPlusZoom.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnClient.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnBroker.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnPCE_Switch.setGraphDesignGroup(graphDesignGroup.getGroup());
        // btnOBS_Switch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnHybridSwitch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnResource.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnLink.setGraphDesignGroup(graphDesignGroup.getGroup());
        executePane.setGroup(graphDesignGroup.getGroup());
        graphDesignGroup.setScrollPane(scpWorld);

        createMapNavigationPanel(vbNavegation);
        addScene(this);
    }

    public static void setStage(Stage stage) {
        GUI.stage = stage;
    }

    public static ActionTypeEmun getActionTypeEmun() {
        if (actionTypeEmun == null) {
            actionTypeEmun = ActionTypeEmun.POINTER;
        }
        return actionTypeEmun;
    }

    public static void setActionTypeEmun(ActionTypeEmun actionTypeEmun) {
        GUI.actionTypeEmun = actionTypeEmun;
    }

    private void createTopDesign(BorderPane brpRoot) {

        createTitleBar();

        //Panel de menus
        VBox vboxMainBar = new VBox();
        vboxMainBar.getChildren().add(tobWindow);

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(3, 0, 3, 3));
        hBox.getStyleClass().add("contenedorDeMenus");

        //Items de menus y menus
        Menu fileMenu = new Menu("Archivo");
        Menu helpFile = new Menu("Ayuda");

        MenuItem newMenuItem = new MenuItem("Nuevo Proyecto");
        MenuItem openMenuItem = new MenuItem("Abrir");
        MenuItem saveMenuItem = new MenuItem("Guardar");
        MenuItem closeMenuItem = new MenuItem("Cerrar");

        newMenuItem.getStyleClass().add("menu-item-ag2");
        openMenuItem.getStyleClass().add("menu-item-ag2");
        saveMenuItem.getStyleClass().add("menu-item-ag2");
        closeMenuItem.getStyleClass().add("menu-item-ag2");

        MenuItem helpMenuItem = new MenuItem("Ayuda");
        MenuItem aboutMenuItem = new MenuItem("Acerca del Proyecto AG2...");

        helpMenuItem.getStyleClass().add("menu-item-ag2");
        aboutMenuItem.getStyleClass().add("menu-item-ag2");

        fileMenu.getItems().addAll(newMenuItem, new SeparatorMenuItem(), openMenuItem, saveMenuItem, new SeparatorMenuItem(), closeMenuItem);
        helpFile.getItems().addAll(helpMenuItem, new SeparatorMenuItem(), aboutMenuItem);

        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {

                main.save(false);

            }
        });

        openMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                main.load();
            }
        });

        newMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int result = JOptionPane.showConfirmDialog(
                        null, "¿Desea guardar los cambios efectuados en la simulación", "Simulador AG2", JOptionPane.YES_NO_CANCEL_OPTION);

                if (result == JOptionPane.NO_OPTION) {
                    main.loadFileBaseSimulation();
                } else if (result == JOptionPane.YES_OPTION) {
                    main.save(false);
                    main.loadFileBaseSimulation();
                }
            }
        });

        aboutMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent t) {
                AboutAG2project aboutAG2project = new AboutAG2project();
                aboutAG2project.show();
            }
        });

        closeMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                int result = JOptionPane.showConfirmDialog(
                        null, "¿Desea guardar los cambios efectuados en la simulación", "Simulador AG2", JOptionPane.YES_NO_CANCEL_OPTION);

                if (result == JOptionPane.NO_OPTION) {
                    System.exit(0);
                } else if (result == JOptionPane.YES_OPTION) {
                    main.save(true);
                }
            }
        });

        //La barra de menus
        MenuBar mbarMainMenuBar = new MenuBar();
        mbarMainMenuBar.setId("menu-bar");

        mbarMainMenuBar.getMenus().addAll(fileMenu, helpFile);

        hBox.getChildren().add(mbarMainMenuBar);

        vboxMainBar.getChildren().add(hBox);
        brpRoot.setTop(vboxMainBar);
    }

    private GridPane createToolsBar() {

        GridPane gridPane = new GridPane();
        // gridPane.setMaxWidth(120);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(4);
        gridPane.getStyleClass().add("barraDeHerramientas");
        createUtilButtons(gridPane);
        createNodeButtons(gridPane);

        return gridPane;
    }

    private void createUtilButtons(GridPane grdPnToolsBar) {

        btnHand = new ToggleButtonAg2(ActionTypeEmun.HAND);
        btnSelection = new ToggleButtonAg2(ActionTypeEmun.POINTER);
        btnPointSeparator = new ToggleButtonAg2(ActionTypeEmun.ADD_LINK_SEPARATOR);
        btnDeleted = new ToggleButtonAg2(ActionTypeEmun.DELETED);
        btnMinusZoom = new ToggleButtonAg2(ActionTypeEmun.ZOOM_MINUS);
        btnPlusZoom = new ToggleButtonAg2(ActionTypeEmun.ZOOM_PLUS);

        btnHand.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnSelection.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnPointSeparator.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnDeleted.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnMinusZoom.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnPlusZoom.setGraphDesignGroup(graphDesignGroup.getGroup());

        btnHand.setToggleGroup(tgTools);
        btnSelection.setToggleGroup(tgTools);
        btnPointSeparator.setToggleGroup(tgTools);
        btnDeleted.setToggleGroup(tgTools);
        btnMinusZoom.setToggleGroup(tgTools);
        btnPlusZoom.setToggleGroup(tgTools);

        btnHand.setTooltip(new Tooltip("Mueva el mapa a su gusto con el raton (SelecciÃ³n rapida:Alt)."));
        btnSelection.setTooltip(new Tooltip("Seleccione cualquier objeto"));
        btnPointSeparator.setTooltip(new Tooltip("AÃ±adale vertices a un enlace"));
        btnDeleted.setTooltip(new Tooltip("Elimine un objeto"));
        btnMinusZoom.setTooltip(new Tooltip("Disminuya el zoom del mapa en donde\nrealize el click (SelecciÃ³n rapida:Ctrl)"));
        btnPlusZoom.setTooltip(new Tooltip("Aumente el zoom del mapa en donde\nrealize el click (SelecciÃ³n rapida:Shift)"));

        GridPane.setConstraints(btnSelection, 0, 0);
        grdPnToolsBar.getChildren().add(btnSelection);

        GridPane.setConstraints(btnHand, 1, 0);
        grdPnToolsBar.getChildren().add(btnHand);

        GridPane.setConstraints(btnPointSeparator, 0, 1);
        grdPnToolsBar.getChildren().add(btnPointSeparator);

        GridPane.setConstraints(btnDeleted, 1, 1);
        grdPnToolsBar.getChildren().add(btnDeleted);

        GridPane.setConstraints(btnMinusZoom, 0, 2);
        grdPnToolsBar.getChildren().add(btnMinusZoom);

        GridPane.setConstraints(btnPlusZoom, 1, 2);
        grdPnToolsBar.getChildren().add(btnPlusZoom);

        Separator separadorHerramientas = new Separator(Orientation.HORIZONTAL);
        separadorHerramientas.getStyleClass().add("separadorBarraDeHerramientas");

        GridPane.setConstraints(separadorHerramientas, 0, 3);
        separadorHerramientas.setValignment(VPos.CENTER);

        GridPane.setColumnSpan(separadorHerramientas, 2);
        grdPnToolsBar.getChildren().add(separadorHerramientas);

    }

    private void createNodeButtons(GridPane grdPnToolsBar) {

        btnClient.setToggleGroup(tgTools);
        btnBroker.setToggleGroup(tgTools);
        btnPCE_Switch.setToggleGroup(tgTools);
        //    btnOBS_Switch.setToggleGroup(tgTools);
        btnHybridSwitch.setToggleGroup(tgTools);
        btnResource.setToggleGroup(tgTools);
        btnLink.setToggleGroup(tgTools);

        btnClient.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnBroker.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnPCE_Switch.setGraphDesignGroup(graphDesignGroup.getGroup());
        //   btnOBS_Switch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnHybridSwitch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnResource.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnLink.setGraphDesignGroup(graphDesignGroup.getGroup());

        btnClient.setTooltip(new Tooltip("Nodo cliente"));
        btnBroker.setTooltip(new Tooltip("Nodo de servicio(Middleware)"));
        btnPCE_Switch.setTooltip(new Tooltip("PCE (Path Computation Element)"));
        //     btnOBS_Switch.setTooltip(new Tooltip("Enrutador de RÃ¡faga"));
        btnHybridSwitch.setTooltip(new Tooltip("Enrutador Hibrido"));
        btnResource.setTooltip(new Tooltip("ClÃºster (Recurso de almacenamiento y procesamiento) "));
        btnLink.setTooltip(new Tooltip("Enlace Optico"));

        GridPane.setConstraints(btnClient, 0, 4);
        grdPnToolsBar.getChildren().add(btnClient);

        GridPane.setConstraints(btnBroker, 1, 4);
        grdPnToolsBar.getChildren().add(btnBroker);

        GridPane.setConstraints(btnPCE_Switch, 0, 5);
        grdPnToolsBar.getChildren().add(btnPCE_Switch);

//        GridPane.setConstraints(btnOBS_Switch, 1, 5);
//        grdPnToolsBar.getChildren().add(btnOBS_Switch);

        GridPane.setConstraints(btnHybridSwitch, 1, 5);
        grdPnToolsBar.getChildren().add(btnHybridSwitch);

        GridPane.setConstraints(btnResource, 0, 6);
        grdPnToolsBar.getChildren().add(btnResource);

        GridPane.setConstraints(btnLink, 1, 6);
        grdPnToolsBar.getChildren().add(btnLink);


    }

    private void createTabs() {

        tabSimulation.setClosable(false);
        tabSimulation.setText("Simulación");
        tabSimulation.setClosable(false);
        tabResults.setText("Resultados Phosphorus");
        tabResults.setClosable(false);
        tabResultsHTML.setText("Resultado Phosphorus HTML");
        tabResultsHTML.setClosable(false);

        tabChartsResourceCPU.setText("Graficos de recurso (CPU)");
        tabChartsResourceCPU.setClosable(false);

        tabChartsResourceBuffer.setText("Graficos de recurso (buffer)");
        tabChartsResourceBuffer.setClosable(false);

        phosphosrusResults = new PhosphosrusResults(tabResults);
        PhosphosrusHTMLResults phosphosrusHTMLResults = new PhosphosrusHTMLResults(tabResultsHTML);
        executePane.setPhosphosrusHTMLResults(phosphosrusHTMLResults);
        executePane.setPhosphosrusResults(phosphosrusResults);

        graphDesignGroup.setScrollPane(scpWorld);

        grRootWorld.getChildren().add(graphDesignGroup.getGroup());
        scpWorld.setContent(grRootWorld);
        splitPane.getItems().addAll(scpWorld, scpnProperties);
        tabSimulation.setContent(splitPane);
        chartsResultsCPU = new ChartsResultsCPU(tabChartsResourceCPU);
        chartsResultsBuffer = new ChartsResultsBuffer(tabChartsResourceBuffer);

        tabPane.getTabs().addAll(tabSimulation, tabChartsResourceCPU, tabChartsResourceBuffer);
    }

    private ScrollPane createBottomDesign() {

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("cajaInferior");

        VBox vBoxProperties = new VBox();
        vBoxProperties.setAlignment(Pos.TOP_LEFT);



        TabPane tpProperties = new TabPane();
        tpProperties.setSide(Side.LEFT);
        Tab tabNodeProperties = new Tab("Propiedades de dispositivo");
        Tab tabSimulationProperties = new Tab("Propiedades de simulación");
        Tab tabNavigation = new Tab("Panel de navegación");
        tabNodeProperties.setClosable(false);
        tabSimulationProperties.setClosable(false);
        tabNavigation.setClosable(false);
        tpProperties.getTabs().addAll(tabNodeProperties, tabSimulationProperties, tabNavigation);



        entityPropertyTable = new EntityPropertyTableView();
        TableView<String> tbSimulationProperties = createSimulationPropertiesTb();


        tabNodeProperties.setContent(entityPropertyTable);
        tabSimulationProperties.setContent(tbSimulationProperties);

        VBox vbxBottomRight = new VBox(10);
        vbxBottomRight.getStyleClass().add("cajaInferior");

        vbxBottomRight.setPadding(new Insets(10, 10, 10, 10));
        createMapNavigationPanel(vbNavegation);
        vbxBottomRight.getChildren().add(vbNavegation);



        tabNavigation.setContent(vbxBottomRight);


        final Button btnDownUp = new Button("<");

        btnDownUp.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {

                showProperties = !showProperties;

                tlProperties.play();
                if (showProperties) {
                    btnDownUp.setText(">");
                } else {
                    btnDownUp.setText("<");
                }
            }
        });

        tpProperties.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                if (!showProperties) {
                    showProperties = !showProperties;
                    tlProperties.play();
                    btnDownUp.setText(">");

                }
            }
        });


        vBoxProperties.getChildren().addAll(tpProperties, btnDownUp);

        scrollPane.setContent(vBoxProperties);
        return scrollPane;
    }

    private VBox createProjectsLogos() {

        ImageView ivAG2 = new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "logoAG2.png"));
        double proportionXYAG2 = ivAG2.getBoundsInParent().getWidth() / ivAG2.getBoundsInParent().getHeight();
        ivAG2.setFitHeight(35);
        ivAG2.setFitWidth(35 * proportionXYAG2);

        ImageView ivInternetIntel = new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "logoInterInt.png"));
        double proportionXYInternetIn = ivInternetIntel.getBoundsInParent().getWidth() / ivInternetIntel.getBoundsInParent().getHeight();
        ivInternetIntel.setFitHeight(25);
        ivInternetIntel.setFitWidth(25 * proportionXYInternetIn);

        ImageView ivPhosphorus = new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "phosphorus.jpg"));
        double proportionXYphosphorus = ivPhosphorus.getBoundsInParent().getWidth() / ivPhosphorus.getBoundsInParent().getHeight();
        ivPhosphorus.setFitHeight(30);
        ivPhosphorus.setFitWidth(30 * proportionXYphosphorus);

        ImageView ivDistritalUniv = new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "escudo_udistrital.jpg"));
        double proportionXYdistritalUniv = ivDistritalUniv.getBoundsInParent().getWidth() / ivDistritalUniv.getBoundsInParent().getHeight();
        ivDistritalUniv.setFitHeight(35);
        ivDistritalUniv.setFitWidth(35 * proportionXYdistritalUniv);

        Hyperlink linkAG2 = new Hyperlink();
        Hyperlink linkInterIntel = new Hyperlink();
        Hyperlink linkPhosphorus = new Hyperlink();
        Hyperlink linkDistritalUniv = new Hyperlink();

        linkAG2.setTooltip(new Tooltip("Visite la pÃ¡gina web del Grupo de InvestigaciÃ³n en \"Colciencias\""));
        linkInterIntel.setTooltip(new Tooltip("Visite la pÃ¡gina web del Grupo de InvestigaciÃ³n \"Internet Inteligente\""));
        linkPhosphorus.setTooltip(new Tooltip("Visite la pÃ¡gina web del proyecto \"FÃ³sforo\""));
        linkDistritalUniv.setTooltip(new Tooltip("Visite la pÃ¡gina web de la \"Universidad Distrital FJC\""));

        linkAG2.setGraphic(ivAG2);
        linkInterIntel.setGraphic(ivInternetIntel);
        linkPhosphorus.setGraphic(ivPhosphorus);
        linkDistritalUniv.setGraphic(ivDistritalUniv);

        setOnLunchBrowser(linkAG2, "http://201.234.78.173:8080/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000003328");
        setOnLunchBrowser(linkInterIntel, "http://gemini.udistrital.edu.co/comunidad/grupos/internetinteligente/");
        setOnLunchBrowser(linkPhosphorus, "http://www.ist-phosphorus.eu/");
        setOnLunchBrowser(linkDistritalUniv, "www.udistrital.edu.co");

        vbLogos = new VBox(5);
        vbLogos.setAlignment(Pos.CENTER);
        vbLogos.setPadding(new Insets(3, 3, 3, 3));

        vbLogos.getChildren().addAll(linkAG2, linkInterIntel, linkPhosphorus, linkDistritalUniv);


        vbLogos.getStyleClass().add("boxLogos");

//        DropShadow dropShadow = new DropShadow();
//        vbLogos.setEffect(dropShadow);

        return vbLogos;
    }

    public void setOnLunchBrowser(Hyperlink link, final String URLToGo) {
        link.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(URLToGo));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private TableView<String> createSimulationPropertiesTb() {

        TableColumn tbcolPropName = new TableColumn("PROPIEDAD");
        tbcolPropName.setMinWidth(200);
        tbcolPropName.setPrefWidth(200);
        tbcolPropName.setCellValueFactory(new PropertyValueFactory<PropertyPhosphorusTypeEnum, String>("visualNameOnTb"));

        TableColumn tbcolPropValue = new TableColumn("VALOR");
        tbcolPropValue.setMinWidth(160);
        tbcolPropValue.setPrefWidth(160);
        tbcolPropValue.setCellValueFactory(new PropertyValueFactory<PropertyPhosphorusTypeEnum, Control>("control"));

        tbwSimulationProperties.getColumns().addAll(tbcolPropName, tbcolPropValue);


        tbwSimulationProperties.setMinHeight(469);
//        tbwSimulationProperties.setPrefWidth(345);
//
//        tbwSimulationProperties.setPrefHeight(200);

        return tbwSimulationProperties;
    }

    public void createMapNavigationPanel(VBox vBox) {

        gpMapNavegation.setPadding(new Insets(10, 10, 10, 10));
        gpMapNavegation.setVgap(5);
        gpMapNavegation.setHgap(4);
        gpMapNavegation.getStyleClass().addAll("boxLogosVerticalGradient");

        Label lbTitle = new Label("LISTAS DE NAVEGACION");
        lbTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        final ChoiceBox cbClients = new ChoiceBox(graphDesignGroup.getClientsObservableList());
        final ChoiceBox cbResources = new ChoiceBox(graphDesignGroup.getResourcesObservableList());
        final ChoiceBox cbSwicthes = new ChoiceBox(graphDesignGroup.getSwitchesObservableList());
        final ChoiceBox cbServiceNodes = new ChoiceBox(graphDesignGroup.getBrokersObservableList());

        cbClients.setMinWidth(150);
        cbResources.setMinWidth(150);
        cbSwicthes.setMinWidth(150);
        cbServiceNodes.setMinWidth(150);

        Button btnIrClients = new Button("ir");
        Button btnIrResources = new Button("ir");
        Button btnIrSwichtes = new Button("ir");
        Button btnIrServiceNodes = new Button("ir");

        Label lbRouters = new Label("Enrutadores");
        lbRouters.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label lbClientes = new Label("Clientes");
        lbClientes.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label lbRecursos = new Label("Recursos");
        lbRecursos.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label lbNodosServicio = new Label("Agendadores");
        lbNodosServicio.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        lbRouters.setMinWidth(80);
        lbClientes.setMinWidth(80);
        lbRecursos.setMinWidth(80);
        lbNodosServicio.setMinWidth(80);

        GridPane.setConstraints(lbTitle, 0, 1);
        GridPane.setColumnSpan(lbTitle, 3);
        GridPane.setHalignment(lbTitle, HPos.CENTER);
        gpMapNavegation.getChildren().add(lbTitle);

        GridPane.setConstraints(lbClientes, 0, 2);
        gpMapNavegation.getChildren().add(lbClientes);
        GridPane.setConstraints(cbClients, 1, 2);
        gpMapNavegation.getChildren().add(cbClients);
        GridPane.setConstraints(btnIrClients, 2, 2);
        gpMapNavegation.getChildren().add(btnIrClients);

        GridPane.setConstraints(lbRecursos, 0, 3);
        gpMapNavegation.getChildren().add(lbRecursos);
        GridPane.setConstraints(cbResources, 1, 3);
        gpMapNavegation.getChildren().add(cbResources);
        GridPane.setConstraints(btnIrResources, 2, 3);
        gpMapNavegation.getChildren().add(btnIrResources);

        GridPane.setConstraints(lbRouters, 0, 4);
        gpMapNavegation.getChildren().add(lbRouters);
        GridPane.setConstraints(cbSwicthes, 1, 4);
        gpMapNavegation.getChildren().add(cbSwicthes);
        GridPane.setConstraints(btnIrSwichtes, 2, 4);
        gpMapNavegation.getChildren().add(btnIrSwichtes);

        GridPane.setConstraints(lbNodosServicio, 0, 5);
        gpMapNavegation.getChildren().add(lbNodosServicio);
        GridPane.setConstraints(cbServiceNodes, 1, 5);
        gpMapNavegation.getChildren().add(cbServiceNodes);
        GridPane.setConstraints(btnIrServiceNodes, 2, 5);
        gpMapNavegation.getChildren().add(btnIrServiceNodes);

        if (!vBox.getChildren().contains(gpMapNavegation)) {
            vBox.getChildren().add(gpMapNavegation);
        }

        setEventGoBtn(btnIrClients, cbClients);
        setEventGoBtn(btnIrServiceNodes, cbServiceNodes);
        setEventGoBtn(btnIrResources, cbResources);
        setEventGoBtn(btnIrSwichtes, cbSwicthes);

    }

    private void setEventGoBtn(Button goButton, final ChoiceBox cbxbNodes) {

        goButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent t) {
                GraphNode selectedNode = (GraphNode) cbxbNodes.getSelectionModel().getSelectedItem();

                if (selectedNode != null) {
                    //selectedNode.select(true);

                    Scale sclEscalaDeZoom = graphDesignGroup.getScZoom();
                    sclEscalaDeZoom.setX(1.5);
                    sclEscalaDeZoom.setY(-1.5);

                    double worldWidth = graphDesignGroup.getGroup().getBoundsInParent().getWidth();
                    double worldHeight = graphDesignGroup.getGroup().getBoundsInParent().getHeight();
                    //La posicion (0,0) esta en la esquina superior izquierda
                    double posXNewCoords = (selectedNode.getLayoutX() * 1.5) + (worldWidth / 2);
                    double posYNewCoords = (selectedNode.getLayoutY() * (-1.5)) + (worldHeight / 2);

                    double posXInPercentage = posXNewCoords / worldWidth;
                    double posYInPercentage = posYNewCoords / worldHeight;

                    double maxErrorInX = scpWorld.getViewportBounds().getWidth() / 2;
                    double funcToCalculateXError = (-2 * maxErrorInX * posXInPercentage) + maxErrorInX;
                    double percentageXError = funcToCalculateXError / worldWidth;

                    double maxErrorInY = scpWorld.getViewportBounds().getHeight() / 2;
                    double funcToCalculateYError = (-2 * maxErrorInY * posYInPercentage) + maxErrorInY;
                    double percentageYError = funcToCalculateYError / worldHeight;

                    double percentImgHeightCorrecX = (selectedNode.getWidth()) / worldWidth;
                    double percentImgHeightCorrecY = (selectedNode.getHeight()) / worldHeight;

                    scpWorld.setHvalue(posXInPercentage - percentageXError + percentImgHeightCorrecX);
                    scpWorld.setVvalue(posYInPercentage - percentageYError - percentImgHeightCorrecY);

                    if (graphDesignGroup.getSelectable() != null) {
                        graphDesignGroup.getSelectable().select(false);
                    }

                    graphDesignGroup.setSelectable(selectedNode);
                    selectedNode.select(true);
                }
            }
        });
    }

    private void addScene(final Scene scene) {

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent event) {

                if ((event.isAltDown() || event.isShiftDown() || event.isControlDown())
                        && isPrincipalKeyPressed == false && graphDesignGroup.getGroup().isHover()) {

                    isPrincipalKeyPressed = true;
                    beforeActionTypeEmun = GUI.getActionTypeEmun();
                    beforeEventCursor = graphDesignGroup.getGroup().getCursor();

                    if (event.isAltDown()) {
                        GUI.setActionTypeEmun(ActionTypeEmun.HAND);
                        graphDesignGroup.getGroup().setCursor(ActionTypeEmun.HAND.getCursorImage());
                    } else if (event.isShiftDown()) {
                        GUI.setActionTypeEmun(ActionTypeEmun.ZOOM_PLUS);
                        graphDesignGroup.getGroup().setCursor(ActionTypeEmun.ZOOM_PLUS.getCursorImage());
                    } else if (event.isControlDown()) {
                        GUI.setActionTypeEmun(ActionTypeEmun.ZOOM_MINUS);
                        graphDesignGroup.getGroup().setCursor(ActionTypeEmun.ZOOM_MINUS.getCursorImage());
                    }
                }
            }
        });

        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent event) {

                if (isPrincipalKeyPressed == true) {
                    isPrincipalKeyPressed = false;
                    GUI.setActionTypeEmun(beforeActionTypeEmun);
                    graphDesignGroup.getGroup().setCursor(beforeEventCursor);
                }
            }
        });
    }

    public void initStateGUI() {

        btnClient.setSelected(true);
        GUI.setActionTypeEmun(ActionTypeEmun.CLIENT);
        graphDesignGroup.getGroup().setCursor(ActionTypeEmun.CLIENT.getCursorImage());
        scpWorld.setHvalue(0.27151447890809266);
        scpWorld.setVvalue(0.4661207267437006);


//        windowButtons.toogleMaximized();

        new AboutAG2project();


    }

    public GraphDesignGroup getGraphDesignGroup() {
        return graphDesignGroup;
    }

    public EntityPropertyTableView getEntityPropertyTb() {
        return entityPropertyTable;
    }

    public void enable() {
        GUI.setActionTypeEmun(beforeActionTypeEmun);
        graphDesignGroup.getGroup().setCursor(beforeEventCursor);
        gpTools.setDisable(false);
        gpTools.setOpacity(1);
        hBoxProgressIndicator.setVisible(false);
        graphDesignGroup.getGroup().setOpacity(1);
        chartsResultsCPU.stop();
        chartsResultsBuffer.stop();
    }

    public void disable() {
        beforeActionTypeEmun = GUI.getActionTypeEmun();
        beforeEventCursor = graphDesignGroup.getGroup().getCursor();

        GUI.setActionTypeEmun(ActionTypeEmun.HAND);
        graphDesignGroup.getGroup().setCursor(ActionTypeEmun.HAND.getCursorImage());

        hBoxProgressIndicator.setVisible(true);
        gpTools.setDisable(true);
        gpTools.setOpacity(0.8);

        graphDesignGroup.getGroup().setOpacity(0.8);
        if (!tabPane.getTabs().contains(tabResultsHTML)) {
            tabPane.getTabs().addAll(tabResultsHTML, tabResults);
        }
        chartsResultsCPU.play();
        chartsResultsBuffer.play();
        tabPane.getSelectionModel().select(tabResults);

    }

    public ExecutePane getExecutePane() {
        return executePane;
    }

    public PhosphosrusResults getPhosphosrusResults() {
        return phosphosrusResults;
    }

    public TableView<String> getTbwSimulationProperties() {
        return tbwSimulationProperties;
    }

    public StackPane getModalDimmer() {
        return modalDimmer;
    }
}