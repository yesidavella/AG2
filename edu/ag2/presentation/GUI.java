package edu.ag2.presentation;

import edu.ag2.config.PropertyPhosphorusTypeEnum;
import edu.ag2.controller.MatchCoupleObjectContainer;
import edu.ag2.model.PhosphorusLinkModel;
import edu.ag2.presentation.control.ChartsResultClient;
import edu.ag2.presentation.control.ChartsResultResource;
import edu.ag2.presentation.control.ChartsResultsBroker;
import edu.ag2.presentation.control.ChartsResultsBuffer;
import edu.ag2.presentation.control.ChartsResultsCPU;
import edu.ag2.presentation.control.ChartsResultsSwitch;
import edu.ag2.presentation.control.LogViewHTML;
import edu.ag2.presentation.control.PhosphosrusHTMLResults;
import edu.ag2.presentation.control.PhosphosrusResults;
import edu.ag2.presentation.control.ResultsOCS;
import edu.ag2.presentation.control.SimulationOptionSwitcher;
import edu.ag2.presentation.control.ToggleButtonAg2;
import edu.ag2.presentation.control.WindowButtons;
import edu.ag2.presentation.control.WindowResizeButton;
import edu.ag2.presentation.design.GraphDesignGroup;
import edu.ag2.presentation.design.GraphLink;
import edu.ag2.presentation.design.GraphNode;
import edu.ag2.presentation.design.SwitchGraphNode;
import edu.ag2.presentation.design.property.EntityPropertyTableView;
import edu.ag2.presentation.images.ImageHelper;
import edu.ag2.util.CSVWritter;
import Grid.Nodes.Coeficiente;
import Grid.Nodes.MultiCostMarkovAnalyzer;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
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

public class GUI extends Scene {
    //Static object

    private static GUI iguAG2;
    private static ActionTypeEmun actionTypeEmun = ActionTypeEmun.POINTER;
    private static Stage stage;
    private VBox vbLogos = createProjectsLogos();
    private GraphDesignGroup graphDesignGroup = new GraphDesignGroup();
    private ToggleGroup tgTools;
    private GridPane gpMapNavegation;
    private ExecutePane executePane;
    private ToggleButtonAg2 btnHand;
    private ToggleButtonAg2 btnClient = new ToggleButtonAg2(ActionTypeEmun.CLIENT);
    private ToggleButtonAg2 btnBroker = new ToggleButtonAg2(ActionTypeEmun.BROKER);
    private ToggleButtonAg2 btnPCE_Switch = new ToggleButtonAg2(ActionTypeEmun.PCE_SWITCH);
    private ToggleButtonAg2 btnHybridSwitch = new ToggleButtonAg2(ActionTypeEmun.HRYDRID_SWITCH);
    private ToggleButtonAg2 btnResource = new ToggleButtonAg2(ActionTypeEmun.RESOURCE);
    private ToggleButtonAg2 btnLink = new ToggleButtonAg2(ActionTypeEmun.LINK);
    private ToggleButtonAg2 btnOCSCircuit = new ToggleButtonAg2(ActionTypeEmun.OCS_CIRCUIT);
    private EntityPropertyTableView entityPropertyTable;
    private GridPane gpTools;
    private ScrollPane scpWorld;
    private ProgressIndicator progressIndicator;
    private boolean isPrincipalKeyPressed = false;
    private ActionTypeEmun beforeActionTypeEmun;
    private Cursor beforeEventCursor;
    private PhosphosrusResults phosphosrusResults;
    private ResultsOCS resultsOCS;
    private Main main;
    private ToggleButtonAg2 btnSelection;
    private ToggleButtonAg2 btnPointSeparator;
    private ToggleButtonAg2 btnDeleted;
    private ToggleButtonAg2 btnMinusZoom;
    private ToggleButtonAg2 btnPlusZoom;
    private Group grRootWorld = new Group();
    private Tab tabSimulation = new Tab();
//    private Tab tabViewOCS = new Tab();
    private Tab tabResults = new Tab();
    private Tab tabResultsOCS = new Tab();
    private Tab tabChartsResourceCPU = new Tab();
    private Tab tabChartsResourceBuffer = new Tab();
    private Tab tabChartsClientResults = new Tab();
    private Tab tabChartsBrokerResults = new Tab();
    private Tab tabChartsSwitchResults = new Tab();
    private Tab tabChartsrResourceResults = new Tab();
    private Tab tabResultsResource = new Tab();
    private TabPane tbpMain = new TabPane();
    private TabPane tbResultsResource = new TabPane();
    private ChartsResultClient chartsResultClient;
    private ChartsResultResource chartsResultResource;
    private ChartsResultsBroker chartsResultsBroker;
    private ChartsResultsSwitch chartsResultsSwitch;
    private TableView<String> tbvSimulationProperties = new TableView<String>();
    private ToolBar tobWindow;
    private double mouseDragOffsetX = 0;
    private double mouseDragOffsetY = 0;
    private SplitPane splitPane;
    private WindowButtons windowButtons;
    private StackPane stpLayer;
    private BorderPane brpRoot;
    private StackPane modalDimmer;
    private HBox hBoxProgressIndicator = new HBox();
    private VBox vbxBottomRight = new VBox(10);
    private ChartsResultsCPU chartsResultsCPU;
    private ChartsResultsBuffer chartsResultsBuffer;
    private ScrollPane scpnProperties;
    private Timeline tilProperties = new Timeline();
    private boolean showProperties = false;
    private ScrollPane scpMenuTools;
    private SimulationOptionSwitcher simulationOptionSwitcher = new SimulationOptionSwitcher();
    private WindowResizeButton windowResizeButton;
    private Timeline tilPropertiesSmall = new Timeline();
    private boolean hideAgainProperties = false;
    private Button btnDownUp = new Button();
    private transient ToolBarAnimationAG2 animationHeaderAG2;
    private VBox vbHeaderAnimationContainer;
    private Button btnShowLog = new Button("Ver registros");
    private boolean isPlaying = false;
    private boolean OCSView = false;
    private double totalIncrement = 0;
    private static final int NUM_EXECUTES_MAX = 3;
    public static int executes = 0;
    private CSVWritter csvWritter;
    public static Coeficiente Cx;
    public static Coeficiente Cy;
    public static Coeficiente percentGrid;
    public static boolean reEjecutarAutonomamente = true;

    public ToggleButtonAg2 getBtnDeleted() {
        return btnDeleted;
    }

    public void setBtnDeleted(ToggleButtonAg2 btnDeleted) {
        this.btnDeleted = btnDeleted;
    }

    private GUI(StackPane stpLayer, double width, double height) {
        super(stpLayer, width, height);

        getStylesheets().add(Main.class.getResource("ag2.css").toExternalForm());

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
        modalDimmer.setId("modalDimmer");
        modalDimmer.setVisible(false);
        stpLayer.getChildren().add(brpRoot);
        stpLayer.getChildren().add(modalDimmer);
        btnShowLog.setDisable(true);
        btnShowLog.getStyleClass().add("button-ag2");

        if (!Main.IS_APPLET) {
            brpRoot.getChildren().add(windowResizeButton);
        }
    }

    public static GUI getInstance() {

        if (iguAG2 == null) {
            iguAG2 = new GUI(new StackPane(), 1000, 720);//new BorderPane()
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
        contenedorHerramietas.setAlignment(Pos.TOP_CENTER);
        scpMenuTools.setFitToWidth(true);
        scpMenuTools.setFitToHeight(true);

        settingupProgressIndicator();

        contenedorHerramietas.getChildren().addAll(executePane, gpTools, simulationOptionSwitcher, hBoxProgressIndicator, vbLogos);
        scpMenuTools.setContent(contenedorHerramietas);
        brpRoot.setLeft(scpMenuTools);

        //Diseño central
        scpnProperties = createRightDesign();

        splitPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        splitPane.setOrientation(Orientation.HORIZONTAL);
        setSplitPaneAnimation();

        createTabs();
        brpRoot.setCenter(tbpMain);
    }

    private void setSplitPaneAnimation() {


        KeyFrame keyFrameSmall = new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {

                double widthProperties = scpnProperties.getBoundsInParent().getWidth();
                double increment;
                if (showProperties) {
                    increment = -0.00003;

                    if (!(widthProperties <= 401)) {
                        tilPropertiesSmall.stop();
                        return;
                    }
                } else {
                    increment = 0.00003;

                    if (!(widthProperties > 39)) {

                        tilPropertiesSmall.stop();
                        return;
                    }
                }

                totalIncrement += increment;
                splitPane.setDividerPosition(0, totalIncrement);
            }
        });

        KeyFrame keyFrame = new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {

                double widthProperties = scpnProperties.getBoundsInParent().getWidth();
                double increment;
                if (showProperties) {
                    increment = -0.0005;

                    if (!(widthProperties <= 350)) {
                        tilProperties.stop();
                        tilPropertiesSmall.play();

                        return;
                    }
                } else {
                    increment = 0.0003;

                    if (!(widthProperties > 90)) {
                        tilProperties.stop();
                        tilPropertiesSmall.play();
                        return;
                    }
                }
                //  System.out.println(" BIG ANTES "+splitPane.getDividerPositions()[0]);

                totalIncrement += increment;
                splitPane.setDividerPosition(0, totalIncrement);
                //    System.out.println(" BIG DESPUES  "+splitPane.getDividerPositions()[0]);
            }
        });

        tilProperties.setCycleCount(50000);
        tilProperties.getKeyFrames().add(keyFrame);

        tilPropertiesSmall.setCycleCount(50000);
        tilPropertiesSmall.getKeyFrames().add(keyFrameSmall);

        brpRoot.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, final Number arg1, final Number arg2) {

                if (showProperties) {

                    final double y = (arg2.doubleValue() * 0.0318342D) + 24.48D;

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            splitPane.setDividerPosition(0, y / 100);
                        }
                    };
                    Platform.runLater(runnable);

                } else {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (arg2.doubleValue() > arg1.doubleValue()) {
                                splitPane.setDividerPosition(0, .95);
                                totalIncrement = 0.95;
                                tilPropertiesSmall.play();
                            }
                        }
                    };
                    Platform.runLater(runnable);
                }
            }
        });

        scpWorld.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                if (hideAgainProperties && showProperties) {
                    showProperties = !showProperties;
                    tilProperties.play();
                    hideAgainProperties = false;

                    btnDownUp.getStyleClass().remove("btn-show-minus");
                    btnDownUp.getStyleClass().add("btn-show-more");
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
        tobWindow.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

                if (animationHeaderAG2 != null) {
                    animationHeaderAG2.resize(stage);
                }
            }
        });

        Label lblTitle = new Label("Simulador de infraestructura de grillas ópticas AG2");
        lblTitle.setId("titleApplication");

        Reflection reflecEfect = new Reflection();
        reflecEfect.setTopOffset(-4);
        lblTitle.setEffect(reflecEfect);

        vbHeaderAnimationContainer = new VBox();
        vbHeaderAnimationContainer.setId("animation-container");
        HBox.setHgrow(vbHeaderAnimationContainer, Priority.ALWAYS);

        tobWindow.setPrefHeight(50);
        tobWindow.setMinHeight(50);
        tobWindow.setMaxHeight(50);
        stage.initStyle(StageStyle.UNDECORATED);
        windowButtons = new WindowButtons(stage, stpLayer);

        tobWindow.getItems().addAll(lblTitle, vbHeaderAnimationContainer, windowButtons);

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
        btnOCSCircuit.setGraphDesignGroup(graphDesignGroup.getGroup());
        executePane.setGroup(graphDesignGroup.getGroup());
        graphDesignGroup.setScrollPane(scpWorld);

        createMapNavigationPanel(vbxBottomRight);
        addScene(this);

        scpWorld.setHvalue(0.27151447890809266);
        scpWorld.setVvalue(0.4661207267437006);
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

        HBox hbBigContainerMenuBar = new HBox();
        hbBigContainerMenuBar.setPadding(new Insets(3, 0, 3, 3));
        hbBigContainerMenuBar.getStyleClass().add("bg-bigContainer-menuBar");

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
                        null, "¿Desea guardar los cambios efectuados en la simulación?", "Simulador AG2", JOptionPane.YES_NO_CANCEL_OPTION);

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
                        null, "¿Desea guardar los cambios efectuados en la simulación?", "Simulador AG2", JOptionPane.YES_NO_CANCEL_OPTION);

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

        hbBigContainerMenuBar.getChildren().add(mbarMainMenuBar);

        vboxMainBar.getChildren().add(hbBigContainerMenuBar);
        brpRoot.setTop(vboxMainBar);
    }

    private GridPane createToolsBar() {

        GridPane gridPane = new GridPane();
        // gridPane.setMaxWidth(120);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(4);
        gridPane.getStyleClass().add("bg-general-container");
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

        btnHand.setTooltip(new Tooltip("Mueva el mapa a su gusto con el raton (Selecci\u00f3n rapida:Alt)."));
        btnSelection.setTooltip(new Tooltip("Seleccione cualquier objeto"));
        btnPointSeparator.setTooltip(new Tooltip("Adici\u00f3nele vertices a un enlace"));
        btnDeleted.setTooltip(new Tooltip("Elimine un objeto"));
        btnMinusZoom.setTooltip(new Tooltip("Disminuya el zoom del mapa en donde\nrealize el click (Selecci\u00f3n rapida:Ctrl)"));
        btnPlusZoom.setTooltip(new Tooltip("Aumente el zoom del mapa en donde\nrealize el click (Selecci\u00f3n rapida:Shift)"));

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
        separadorHerramientas.getStyleClass().add("separator-toolbar");

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
        btnOCSCircuit.setToggleGroup(tgTools);

        btnClient.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnBroker.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnPCE_Switch.setGraphDesignGroup(graphDesignGroup.getGroup());
        //   btnOBS_Switch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnHybridSwitch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnResource.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnLink.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnOCSCircuit.setGraphDesignGroup(graphDesignGroup.getGroup());

        btnClient.setTooltip(new Tooltip("Nodo cliente"));
        btnBroker.setTooltip(new Tooltip("Nodo de servicio(Middleware)"));
        btnPCE_Switch.setTooltip(new Tooltip("Nodo PCE (Path Computation Element)"));
        //     btnOBS_Switch.setTooltip(new Tooltip("Enrutador de RÃ¡faga"));
        btnHybridSwitch.setTooltip(new Tooltip("Nodo Enrutador \u00d3ptico"));
        btnResource.setTooltip(new Tooltip("Nodo Cluster (Recurso de procesamiento) "));
        btnLink.setTooltip(new Tooltip("Enlace \u00d3ptico (Fibra)"));
        btnOCSCircuit.setTooltip(new Tooltip("Circuito \u00d3ptico (λSP)"));

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

        GridPane.setConstraints(btnOCSCircuit, 0, 7);
        grdPnToolsBar.getChildren().add(btnOCSCircuit);

    }

    private void createTabs() {

        tabSimulation.setClosable(false);
        tabResults.setClosable(false);
        tabChartsClientResults.setClosable(false);
        tabChartsrResourceResults.setClosable(false);
        tabChartsResourceCPU.setClosable(false);
        tabChartsResourceBuffer.setClosable(false);
        tabChartsBrokerResults.setClosable(false);
        tabChartsSwitchResults.setClosable(false);
        tabResultsResource.setClosable(false);
        tabResultsOCS.setClosable(false);

        tabSimulation.setText("Simulación");
        tabResults.setText("Resultados totales");
        tabChartsClientResults.setText("Gráficos de Clientes");
        tabChartsrResourceResults.setText("Gráficos de recursos");
        tabChartsResourceCPU.setText("Gráficos de recursos (CPU)");
        tabChartsResourceBuffer.setText("Gráficos de recursos (buffer)");
        tabChartsBrokerResults.setText("Gráficos de Agendador");
        tabChartsSwitchResults.setText("Gráficos de Conmutador");
        tabResultsResource.setText("Gráficos de recursos");
        tabResultsOCS.setText("Resultados λSP");

        phosphosrusResults = new PhosphosrusResults(tabResults);

        executePane.setPhosphosrusResults(phosphosrusResults);

        graphDesignGroup.setScrollPane(scpWorld);

        grRootWorld.getChildren().add(graphDesignGroup.getGroup());
        scpWorld.setContent(grRootWorld);
        VBox vbxSimulation = new VBox();

        final ToggleButton tbNormalView = new ToggleButton("Vista diseño");
        tbNormalView.setId("pill-left");
        final ToggleButton tbLspView = new ToggleButton("Vista λSP");
        tbLspView.setId("pill-right");

        ToggleGroup group = new ToggleGroup();
        tbNormalView.setToggleGroup(group);
        tbLspView.setToggleGroup(group);

        group.selectToggle(tbNormalView);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(tbNormalView, tbLspView);
        hBox.setId("pill-fondo");

        VBox.setVgrow(scpWorld, Priority.ALWAYS);
        vbxSimulation.getChildren().addAll(hBox, scpWorld);

        splitPane.getItems().addAll(vbxSimulation, scpnProperties);
        splitPane.setDividerPosition(0, 0.95);
        totalIncrement = 0.95;

        tabSimulation.setContent(splitPane);

        chartsResultClient = new ChartsResultClient(tabChartsClientResults);
        chartsResultResource = new ChartsResultResource(tabChartsrResourceResults);
        chartsResultsCPU = new ChartsResultsCPU(tabChartsResourceCPU);
        chartsResultsBuffer = new ChartsResultsBuffer(tabChartsResourceBuffer);
        chartsResultsBroker = new ChartsResultsBroker(tabChartsBrokerResults);
        chartsResultsSwitch = new ChartsResultsSwitch(tabChartsSwitchResults);

        resultsOCS = new ResultsOCS(tabResultsOCS);

        tbResultsResource.setSide(Side.LEFT);
        tbResultsResource.getTabs().addAll(tabChartsResourceCPU, tabChartsResourceBuffer, tabChartsrResourceResults);
        tabResultsResource.setContent(tbResultsResource);

        tbpMain.getTabs().addAll(tabSimulation);


        tbNormalView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                GUI.this.showNormalView();
            }
        });


        tbLspView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                GUI.this.showLspView();
            }
        });

        tabSimulation.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event arg0) {
                if (tabSimulation.isSelected()) {
                    if (tbNormalView.isSelected()) {
                        GUI.this.showNormalView();
                    } else if (tbLspView.isSelected()) {
                        GUI.this.showLspView();
                    }
                }
            }
        });



    }

    private void showNormalView() {
        OCSView = false;

        if (!isPlaying) {
            disableButtons(false);
        }

//        tabViewOCS.setContent(null);
//        tabSimulation.setContent(splitPane);
        graphDesignGroup.showMap();
        for (GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {
            if (graphNode instanceof SwitchGraphNode) {
                graphNode.hideSimpleNode();
            } else {
                graphNode.getGroup().setVisible(true);
            }
        }

        HashMap<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer();

        for (GraphLink graphLink : linkMatchCoupleObjectContainer.keySet()) {
            graphLink.setVisible(true);
        }
        graphDesignGroup.hideLineOCS();
    }

    public boolean isOCSView() {
        return OCSView;
    }

    private void showLspView() {
        OCSView = true;
        disableButtons(true);
//        tabSimulation.setContent(null);
//        tabViewOCS.setContent(splitPane);
        graphDesignGroup.hideMap();

        for (GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {
            if (graphNode instanceof SwitchGraphNode) {
                graphNode.showSimpleNode();
            } else {
                graphNode.getGroup().setVisible(false);
            }
        }

        HashMap<GraphLink, PhosphorusLinkModel> linkMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceLinkMatchCoupleObjectContainer();

        for (GraphLink graphLink : linkMatchCoupleObjectContainer.keySet()) {
            graphLink.setVisible(false);
        }

        graphDesignGroup.showLineOCS();
    }

    private ScrollPane createRightDesign() {

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("bg-general-container");

        VBox vbxProperties = new VBox(5);
        vbxProperties.setAlignment(Pos.TOP_LEFT);

        TabPane tbpProperties = new TabPane();
        tbpProperties.setSide(Side.LEFT);
        tbpProperties.setId("tabpaneProperties");
        Tab tabNodeProperties = new Tab("Propiedades de dispositivo");
        Tab tabSimulationProperties = new Tab("Propiedades de simulación");

        tabNodeProperties.setClosable(false);
        tabSimulationProperties.setClosable(false);

        tbpProperties.getTabs().addAll(tabSimulationProperties, tabNodeProperties);

        entityPropertyTable = new EntityPropertyTableView();
        TableView<String> tbSimulationProperties = createSimulationPropertiesTb();

        entityPropertyTable.setId("tbvEntityProperties");

        tabNodeProperties.setContent(entityPropertyTable);
        tabSimulationProperties.setContent(tbSimulationProperties);

        VBox.setMargin(vbxBottomRight, new Insets(0, 0, 0, 32));
        vbxBottomRight.setPadding(new Insets(5));
//        vbxBottomRight.getStyleClass().add("bg-map-navigation-container");

        createMapNavigationPanel(vbxBottomRight);
//        vbxBottomRight.getChildren().add(gpMapNavegation);

        btnDownUp.setId("btn-show-hide-properties");
        btnDownUp.getStyleClass().add("btn-show-more");
        btnDownUp.setTooltip(new Tooltip("Muestra/oculta las propiedades de la simulaci\u00d3n/dispositivo."));

        btnDownUp.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {

                showProperties = !showProperties;

                totalIncrement = splitPane.getDividerPositions()[0];
//                System.out.println("Init totalIncrement" + totalIncrement);
                tilProperties.play();
                if (showProperties) {
                    if (btnDownUp.getStyleClass().contains("btn-show-minus")) {
                        btnDownUp.getStyleClass().remove("btn-show-more");
                    } else {
                        btnDownUp.getStyleClass().remove("btn-show-more");
                        btnDownUp.getStyleClass().add("btn-show-minus");
                    }
                } else {

                    if (btnDownUp.getStyleClass().contains("btn-show-more")) {
                        btnDownUp.getStyleClass().remove("btn-show-minus");
                    } else {
                        btnDownUp.getStyleClass().remove("btn-show-minus");
                        btnDownUp.getStyleClass().add("btn-show-more");
                    }
                }
            }
        });

        tbpProperties.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                if (!showProperties) {
                    showProperties = !showProperties;
                    tilProperties.play();
                    hideAgainProperties = true;

                    btnDownUp.getStyleClass().remove("btn-show-more");
                    btnDownUp.getStyleClass().add("btn-show-minus");
                }
            }
        });

        btnShowLog.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent arg0) {
                LogViewHTML logViewHTML = new LogViewHTML();
                PhosphosrusHTMLResults phosphosrusHTMLResults = new PhosphosrusHTMLResults(logViewHTML.getvBoxMain());
                executePane.setPhosphosrusHTMLResults(phosphosrusHTMLResults);
                logViewHTML.show();
            }
        });
        HBox hBox = new HBox();
        hBox.setSpacing(280);

        hBox.getChildren().addAll(btnDownUp, btnShowLog);

        vbxProperties.getChildren().addAll(tbpProperties, vbxBottomRight, hBox);

        scrollPane.setContent(vbxProperties);

        return scrollPane;
    }

    private VBox createProjectsLogos() {

//        ImageView ivAG2 = new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "logoAG2.png"));
//        double proportionXYAG2 = ivAG2.getBoundsInParent().getWidth() / ivAG2.getBoundsInParent().getHeight();
//        ivAG2.setFitHeight(35);
//        ivAG2.setFitWidth(35 * proportionXYAG2);
//
//        ImageView ivInternetIntel = new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "logoInterInt.png"));
//        double proportionXYInternetIn = ivInternetIntel.getBoundsInParent().getWidth() / ivInternetIntel.getBoundsInParent().getHeight();
//        ivInternetIntel.setFitHeight(25);
//        ivInternetIntel.setFitWidth(25 * proportionXYInternetIn);
//
//        ImageView ivPhosphorus = new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "phosphorus.jpg"));
//        double proportionXYphosphorus = ivPhosphorus.getBoundsInParent().getWidth() / ivPhosphorus.getBoundsInParent().getHeight();
//        ivPhosphorus.setFitHeight(30);
//        ivPhosphorus.setFitWidth(30 * proportionXYphosphorus);
//
//        ImageView ivDistritalUniv = new ImageView(new Image(ResourcesPath.ABS_PATH_IMGS + "escudo_udistrital.jpg"));
//        double proportionXYdistritalUniv = ivDistritalUniv.getBoundsInParent().getWidth() / ivDistritalUniv.getBoundsInParent().getHeight();
//        ivDistritalUniv.setFitHeight(35);
//        ivDistritalUniv.setFitWidth(35 * proportionXYdistritalUniv);
//
//        Hyperlink linkAG2 = new Hyperlink();
//        Hyperlink linkInterIntel = new Hyperlink();
//        Hyperlink linkPhosphorus = new Hyperlink();
//        Hyperlink linkDistritalUniv = new Hyperlink();
//
//        linkAG2.setTooltip(new Tooltip("Visite la pagina web del Grupo de InvestigaciÃ³n en \"Colciencias\""));
//        linkInterIntel.setTooltip(new Tooltip("Visite la pagina web del Grupo de InvestigaciÃ³n \"Internet Inteligente\""));
//        linkPhosphorus.setTooltip(new Tooltip("Visite la pagina web del proyecto \"FÃ³sforo\""));
//        linkDistritalUniv.setTooltip(new Tooltip("Visite la pagina web de la \"Universidad Distrital FJC\""));
//
//        linkAG2.setGraphic(ivAG2);
//        linkInterIntel.setGraphic(ivInternetIntel);
//        linkPhosphorus.setGraphic(ivPhosphorus);
//        linkDistritalUniv.setGraphic(ivDistritalUniv);
//
//        setOnLunchBrowser(linkAG2, "http://201.234.78.173:8080/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000003328");
//        setOnLunchBrowser(linkInterIntel, "http://gemini.udistrital.edu.co/comunidad/grupos/internetinteligente/");
//        setOnLunchBrowser(linkPhosphorus, "http://www.ist-phosphorus.eu/");
//        setOnLunchBrowser(linkDistritalUniv, "www.udistrital.edu.co");
//
//        vbLogos = new VBox(5);
//        vbLogos.setAlignment(Pos.CENTER);
//        vbLogos.setPadding(new Insets(3, 3, 3, 3));
//
//        vbLogos.getChildren().addAll(linkAG2, linkInterIntel, linkPhosphorus, linkDistritalUniv);
//
//
//        vbLogos.getStyleClass().add("boxLogos");
//
////        DropShadow dropShadow = new DropShadow();
////        vbLogos.setEffect(dropShadow);

        vbLogos = new VBox(5);
        vbLogos.setAlignment(Pos.CENTER);
        vbLogos.setPadding(new Insets(3, 3, 3, 3));

        ImageView ivAG2 = new ImageView(new Image(ImageHelper.getResourceInputStream("logos.gif")));
        ivAG2.setEffect(new DropShadow());
        vbLogos.getChildren().addAll(ivAG2);
        ivAG2.setFitHeight(90);
        ivAG2.setFitWidth(90);
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

        tbvSimulationProperties.setId("tbvSimulationProperties");

        TableColumn tbcolPropName = new TableColumn("PROPIEDAD");
        TableColumn tbcolPropValue = new TableColumn("VALOR");

        tbcolPropName.setMinWidth(200);
        tbcolPropName.setPrefWidth(200);
        tbcolPropValue.setMinWidth(140);
        tbcolPropValue.setPrefWidth(140);

        tbcolPropName.setSortable(false);
        tbcolPropValue.setSortable(false);

        tbcolPropName.setCellValueFactory(new PropertyValueFactory<PropertyPhosphorusTypeEnum, String>("visualNameOnTb"));
        tbcolPropValue.setCellValueFactory(new PropertyValueFactory<PropertyPhosphorusTypeEnum, Control>("control"));

        tbvSimulationProperties.getColumns().addAll(tbcolPropName, tbcolPropValue);
        tbvSimulationProperties.setMinHeight(400);

        return tbvSimulationProperties;
    }

    public void createMapNavigationPanel(VBox vbxBottomRight) {

        gpMapNavegation.setPadding(new Insets(3, 5, 5, 5));
        gpMapNavegation.setAlignment(Pos.BASELINE_CENTER);
        gpMapNavegation.setVgap(3);
        gpMapNavegation.setHgap(4);
//        gpMapNavegation.getStyleClass().addAll("bg-map-navigation", "bg-map-navigation-container");
        gpMapNavegation.getStyleClass().addAll("bg-map-navigation", "bg-map-navigation-container");
//        gpMapNavegation.setGridLinesVisible(true);

        Label lbTitle = new Label("LISTAS DE NAVEGACION");
        lbTitle.setId("title-nav-map");
        lbTitle.setFont(Font.font("Arial", FontWeight.NORMAL, 12));

        final ChoiceBox cbClients = new ChoiceBox(graphDesignGroup.getClientsObservableList());
        final ChoiceBox cbResources = new ChoiceBox(graphDesignGroup.getResourcesObservableList());
        final ChoiceBox cbSwicthes = new ChoiceBox(graphDesignGroup.getSwitchesObservableList());
        final ChoiceBox cbServiceNodes = new ChoiceBox(graphDesignGroup.getBrokersObservableList());

        cbClients.setMinWidth(150);
        cbResources.setMinWidth(150);
        cbSwicthes.setMinWidth(150);
        cbServiceNodes.setMinWidth(150);

        Button btnIrClients = new Button();
        Button btnIrResources = new Button();
        Button btnIrSwichtes = new Button();
        Button btnIrServiceNodes = new Button();

        btnIrClients.getStyleClass().add("go-btn");
        btnIrResources.getStyleClass().add("go-btn");
        btnIrSwichtes.getStyleClass().add("go-btn");
        btnIrServiceNodes.getStyleClass().add("go-btn");

        Label lbRouters = new Label("Enrutadores:");
        lbRouters.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        Label lbClientes = new Label("Clientes:");
        lbClientes.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        Label lbRecursos = new Label("Recursos:");
        lbRecursos.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        Label lbNodosServicio = new Label("Agendadores:");
        lbNodosServicio.setFont(Font.font("Arial", FontWeight.NORMAL, 11));

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

        if (!vbxBottomRight.getChildren().contains(gpMapNavegation)) {
            vbxBottomRight.getChildren().add(gpMapNavegation);
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

        createHeaderAnimation(tobWindow);
//        windowButtons.toogleMaximized();


        new AboutAG2project();
    }

    public GraphDesignGroup getGraphDesignGroup() {
        return graphDesignGroup;
    }

    public EntityPropertyTableView getEntityPropertyTb() {
        return entityPropertyTable;
    }

    public void disableButtons(boolean disable) {

        if (disable) {
            GUI.setActionTypeEmun(ActionTypeEmun.POINTER);
            graphDesignGroup.getGroup().setCursor(ActionTypeEmun.POINTER.getCursorImage());
            gpTools.setOpacity(.8);
        } else {

            gpTools.setOpacity(1);
        }

        btnClient.setDisable(disable);
        btnBroker.setDisable(disable);
        btnPCE_Switch.setDisable(disable);
        btnHybridSwitch.setDisable(disable);
        btnResource.setDisable(disable);
        btnLink.setDisable(disable);
        btnOCSCircuit.setDisable(disable);

        btnPointSeparator.setDisable(disable);
        btnDeleted.setDisable(disable);
    }

    public void stop() {
        isPlaying = false;
        enable();
    }

    public void play() {
        isPlaying = true;
        disable();
    }

    private void enable() {
        GUI.setActionTypeEmun(beforeActionTypeEmun);
        graphDesignGroup.getGroup().setCursor(beforeEventCursor);
        if (!isPlaying) {
            disableButtons(false);
        }

        gpTools.setOpacity(1);
        hBoxProgressIndicator.setVisible(false);
        graphDesignGroup.getGroup().setOpacity(1);
        chartsResultsCPU.stop();
        chartsResultsBuffer.stop();
        resultsOCS.showResults();
        btnShowLog.setDisable(false);
        System.out.println("Stop en GUI");

        if (reEjecutarAutonomamente) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        if (nextCombination()) {

                            Thread.sleep(2000);
                            System.out.println("Re-playALL() en GUI." + executes);
                            executePane.playAll();

                        } else {

                            csvWritter.closeFile();
                            csvWritter = null;
                            System.out.println("Fin de ciclos de simulaciones¡¡¡" + executes);
                            executes = 0;
                            Cx = null;
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                private boolean nextCombination() {

                    if (Cx.hasNext()) {

                        //Dos variables
                        if (Cy.hasNext()) {

                            if (percentGrid.hasNext()) {
                                percentGrid.next();
                                return true;
                            } else {
                                percentGrid.reset();
                                Cy.next();
                                return true;
                            }
                        } else {//forzo ultima itera de Callocate
                            if (percentGrid.hasNext()) {
                                percentGrid.next();
                                return true;
                            } else {
                                percentGrid.reset();
                                Cy.reset();
                                Cx.next();
                                return true;
                            }
                        }
                        //Dos variables
                    } else {//Forzo la ultima itera de cx

                        if (Cy.hasNext()) {

                            if (percentGrid.hasNext()) {
                                percentGrid.next();
                                return true;
                            } else {
                                percentGrid.reset();
                                Cy.next();
                                return true;
                            }
                        } else {//forzo ultima itera de Callocate
                            if (percentGrid.hasNext()) {
                                percentGrid.next();
                                return true;
                            } else {
                                return false;
                            }
                        }
                    }
                }//Metodo
            };
            Platform.runLater(runnable);
        }//if
//        graphDesignGroup.hideLineOCS();
    }

    private void disable() {

        beforeActionTypeEmun = GUI.getActionTypeEmun();
        beforeEventCursor = graphDesignGroup.getGroup().getCursor();

        GUI.setActionTypeEmun(ActionTypeEmun.HAND);
        graphDesignGroup.getGroup().setCursor(ActionTypeEmun.HAND.getCursorImage());

        hBoxProgressIndicator.setVisible(true);

        disableButtons(true);

        gpTools.setOpacity(0.8);

        graphDesignGroup.getGroup().setOpacity(0.8);

        if (!tbpMain.getTabs().contains(tabResults)) {
            tbpMain.getTabs().addAll(
                    tabResultsResource,
                    tabChartsClientResults,
                    tabResults,
                    tabResultsOCS,
                    tabChartsBrokerResults,
                    tabChartsSwitchResults);

        }

        chartsResultsCPU.play();
        chartsResultsBuffer.play();
        chartsResultClient.play();
        chartsResultResource.play();
        chartsResultsBroker.play();
        chartsResultsSwitch.play();
//        tbpMain.getSelectionModel().select(tabResults);
        resultsOCS.play();
        btnShowLog.setDisable(true);

    }

    public ExecutePane getExecutePane() {
        return executePane;
    }

    public PhosphosrusResults getPhosphosrusResults() {
        return phosphosrusResults;
    }

    public TableView<String> getTbwSimulationProperties() {
        return tbvSimulationProperties;
    }

    public StackPane getModalDimmer() {
        return modalDimmer;
    }

    public void createHeaderAnimation(ToolBar tobWindow) {
        animationHeaderAG2 = new ToolBarAnimationAG2(tobWindow);
    }

    public ToolBarAnimationAG2 getToolBarAnimationAG2() {
        return animationHeaderAG2;
    }

    public ChartsResultClient getChartsResultClient() {
        return chartsResultClient;
    }

    public ChartsResultResource getChartsResultResource() {
        return chartsResultResource;
    }

    public ResultsOCS getResultsOCS() {
        return resultsOCS;
    }

    public ChartsResultsBroker getChartsResultsBroker() {
        return chartsResultsBroker;
    }

    public ChartsResultsSwitch getChartsResultsSwitch() {
        return chartsResultsSwitch;
    }

    public VBox getVbLogos() {
        return vbLogos;
    }

    public ToggleButtonAg2 getBtnPointSeparator() {
        return btnPointSeparator;
    }

    public boolean isIsPlaying() {
        return isPlaying;
    }

    public CSVWritter getCsvWritter() {
        return csvWritter;
    }

    public void setCsvWritter(CSVWritter csvWritter) {
        this.csvWritter = csvWritter;
    }
}
