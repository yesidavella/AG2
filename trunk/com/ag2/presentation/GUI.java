package com.ag2.presentation;

import com.ag2.config.PropertyPhosphorusTypeEnum;
import com.ag2.presentation.control.PhosphosrusHTMLResults;
import com.ag2.presentation.control.PhosphosrusResults;
import com.ag2.presentation.control.ToggleButtonAg2;
import com.ag2.presentation.design.GraphDesignGroup;
import com.ag2.presentation.design.GraphNode;
import com.ag2.presentation.design.property.EntityPropertyTable;
import java.awt.Desktop;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
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
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class GUI extends Scene implements Serializable {

    private static ActionTypeEmun actionTypeEmun = ActionTypeEmun.POINTER;

    private GraphDesignGroup graphDesignGroup = new GraphDesignGroup();
    private ToggleGroup tgTools;
    private GridPane gpMapNavegation;
    private ExecutePane executePane;
    private ToggleButtonAg2 btnHand;
    private ToggleButtonAg2 btnClient = new ToggleButtonAg2(ActionTypeEmun.CLIENT);
    private ToggleButtonAg2 btnBroker = new ToggleButtonAg2(ActionTypeEmun.BROKER);
    private ToggleButtonAg2 btnOCS_Switch = new ToggleButtonAg2(ActionTypeEmun.OCS_SWITCH);
    private ToggleButtonAg2 btnOBS_Switch = new ToggleButtonAg2(ActionTypeEmun.OBS_SWITCH);
    private ToggleButtonAg2 btnHybridSwitch = new ToggleButtonAg2(ActionTypeEmun.HRYDRID_SWITCH);
    private ToggleButtonAg2 btnResource = new ToggleButtonAg2(ActionTypeEmun.RESOURCE);
    private ToggleButtonAg2 btnLink = new ToggleButtonAg2(ActionTypeEmun.LINK);
    private EntityPropertyTable entityPropertyTable;
    private GridPane gpTools;
    private ScrollPane scPnWorld;
    private ProgressBar pgBrExecutionProgress;
    private boolean isPrincipalKeyPressed = false;
    private ActionTypeEmun beforeActionTypeEmun;
    private Cursor beforeEventCursor;
    private PhosphosrusResults phosphosrusResults;
    private static GUI iguAG2;
    private Main main;
    private StackPane stPnDeviceProperties = new StackPane();
    private ToggleButtonAg2 btnSelection;
    private ToggleButtonAg2 btnPointSeparator;
    private ToggleButtonAg2 btnDeleted;
    private ToggleButtonAg2 btnMinusZoom;
    private ToggleButtonAg2 btnPlusZoom;
    private Group grRoot = new Group();
    private VBox vbNavegation = new VBox();
    private Tab tabSimulation = new Tab();
    private Tab tabResults = new Tab();
    private Tab tabResultsHTML = new Tab();
    private TabPane tpBox = new TabPane();
    private TableView<String> tbwSimulationProperties = new TableView<String>();
    private transient Stage stgEscenario;

     private GUI(BorderPane borderPane, double width, double height) {
        super(borderPane, width, height);
        scPnWorld = new ScrollPane();
        tgTools = new ToggleGroup();
        gpMapNavegation = new GridPane();
        executePane = new ExecutePane();
        executePane.setGroup(graphDesignGroup.getGroup());

        addScene(this);
        getStylesheets().add(GUI.class.getResource("../../../resource/css/MainGUI.css").toExternalForm());

        borderPane.getStyleClass().add("ventanaPrincipal");

        //Diseño superior
        creationMenuBar(borderPane, stgEscenario);

        //Diseño izquierdo(contenedor de Ejecucion y herramientas)
        gpTools = createToolsBar();

        VBox contenedorHerramietas = new VBox();
        contenedorHerramietas.getChildren().addAll(executePane, gpTools);
        borderPane.setLeft(contenedorHerramietas);
        //Diseño central
        createTabs();
        borderPane.setCenter(tpBox);
        //Diseño inferior
        HBox cajaInferiorHor = createDesignButtons();
        borderPane.setBottom(cajaInferiorHor);
        
    }
        public static GUI getInstance() {

        if (iguAG2 == null) {
            iguAG2 = new GUI(new BorderPane(), 1100, 700);
        }
        return iguAG2;
    }

    public ScrollPane getScPnWorld() {
        return scPnWorld;
    }

    public EntityPropertyTable getEntityPropertyTable() {
        return entityPropertyTable;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void loadGraphDesignGroup(GraphDesignGroup graphDesignGroup) {

        grRoot.getChildren().remove(this.graphDesignGroup.getGroup());
        this.graphDesignGroup = graphDesignGroup;
        grRoot.getChildren().add(graphDesignGroup.getGroup());

        executePane.setGroup(graphDesignGroup.getGroup());
        btnHand.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnSelection.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnPointSeparator.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnDeleted.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnMinusZoom.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnPlusZoom.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnClient.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnBroker.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnOCS_Switch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnOBS_Switch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnHybridSwitch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnResource.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnLink.setGraphDesignGroup(graphDesignGroup.getGroup());
        executePane.setGroup(graphDesignGroup.getGroup());
        graphDesignGroup.setScrollPane(scPnWorld);

        createMapNavigationPanel(vbNavegation);
        addScene(this);
    }



    public void setStage(Stage stage) {
        this.stgEscenario = stage;
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

    private void creationMenuBar(BorderPane borderPane, final Stage stage) {

        //Panel de menus
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(3, 0, 3, 3));
        hBox.getStyleClass().add("contenedorDeMenus");

        //Items de menus y menus
        Menu fileMenu = new Menu("Archivo");

        Menu helpFile = new Menu("Ayuda");

        MenuItem newNenuItem = new MenuItem("Nuevo Proyecto");
        MenuItem openMenuItem = new MenuItem("Abrir");
        MenuItem saveMenuItem = new MenuItem("Guardar");
        MenuItem closeMenuItem = new MenuItem("Cerrar");

        MenuItem helpMenuItem = new MenuItem("Ayuda");
        MenuItem aboutMenuItem = new MenuItem("Acerca del Proyecto AG2...");

        fileMenu.getItems().addAll(newNenuItem, new SeparatorMenuItem(), openMenuItem, saveMenuItem, new SeparatorMenuItem(), closeMenuItem);
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
        newNenuItem.setOnAction(new EventHandler<ActionEvent>() {

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
        MenuBar mnuBarBarraDeMenus = new MenuBar();
        mnuBarBarraDeMenus.getMenus().addAll(fileMenu, helpFile);
        mnuBarBarraDeMenus.getStyleClass().add("barraDeMenus");

        hBox.getChildren().add(mnuBarBarraDeMenus);
        borderPane.setTop(hBox);
    }

    private GridPane createToolsBar() {

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(4);
        gridPane.getStyleClass().add("barraDeHerramientas");
        createUtilButtons(gridPane);
        createNodeButtons(gridPane);

        return gridPane;
    }

    private void createUtilButtons(GridPane grdPnBarraHerramientas) {

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

        btnHand.setTooltip(new Tooltip("Mueva el mapa a su gusto con el raton (Selección rapida:Alt)."));
        btnSelection.setTooltip(new Tooltip("Seleccione cualquier objeto"));
        btnPointSeparator.setTooltip(new Tooltip("Añadale vertices a un enlace"));
        btnDeleted.setTooltip(new Tooltip("Elimine un objeto"));
        btnMinusZoom.setTooltip(new Tooltip("Disminuya el zoom del mapa en donde\nrealize el click (Selección rapida:Ctrl)"));
        btnPlusZoom.setTooltip(new Tooltip("Aumente el zoom del mapa en donde\nrealize el click (Selección rapida:Shift)"));

        GridPane.setConstraints(btnSelection, 0, 0);
        grdPnBarraHerramientas.getChildren().add(btnSelection);

        GridPane.setConstraints(btnHand, 1, 0);
        grdPnBarraHerramientas.getChildren().add(btnHand);

        GridPane.setConstraints(btnPointSeparator, 0, 1);
        grdPnBarraHerramientas.getChildren().add(btnPointSeparator);

        GridPane.setConstraints(btnDeleted, 1, 1);
        grdPnBarraHerramientas.getChildren().add(btnDeleted);

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

    private void createNodeButtons(GridPane grdPnBarraHerramientas) {

        btnClient.setToggleGroup(tgTools);
        btnBroker.setToggleGroup(tgTools);
        btnOCS_Switch.setToggleGroup(tgTools);
        btnOBS_Switch.setToggleGroup(tgTools);
        btnHybridSwitch.setToggleGroup(tgTools);
        btnResource.setToggleGroup(tgTools);
        btnLink.setToggleGroup(tgTools);

        btnClient.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnBroker.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnOCS_Switch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnOBS_Switch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnHybridSwitch.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnResource.setGraphDesignGroup(graphDesignGroup.getGroup());
        btnLink.setGraphDesignGroup(graphDesignGroup.getGroup());

        btnClient.setTooltip(new Tooltip("Nodo cliente"));
        btnBroker.setTooltip(new Tooltip("Nodo de servicio(Middleware)"));
        btnOCS_Switch.setTooltip(new Tooltip("Enrutador Optico"));
        btnOBS_Switch.setTooltip(new Tooltip("Enrutador de Ráfaga"));
        btnHybridSwitch.setTooltip(new Tooltip("Enrutador Hibrido"));
        btnResource.setTooltip(new Tooltip("Clúster (Recurso de almacenamiento y procesamiento) "));
        btnLink.setTooltip(new Tooltip("Enlace Optico"));

        GridPane.setConstraints(btnClient, 0, 4);
        grdPnBarraHerramientas.getChildren().add(btnClient);

        GridPane.setConstraints(btnBroker, 1, 4);
        grdPnBarraHerramientas.getChildren().add(btnBroker);

        GridPane.setConstraints(btnOCS_Switch, 0, 5);
        grdPnBarraHerramientas.getChildren().add(btnOCS_Switch);

        GridPane.setConstraints(btnOBS_Switch, 1, 5);
        grdPnBarraHerramientas.getChildren().add(btnOBS_Switch);

        GridPane.setConstraints(btnHybridSwitch, 0, 6);
        grdPnBarraHerramientas.getChildren().add(btnHybridSwitch);

        GridPane.setConstraints(btnResource, 1, 6);
        grdPnBarraHerramientas.getChildren().add(btnResource);

        GridPane.setConstraints(btnLink, 0, 7);
        grdPnBarraHerramientas.getChildren().add(btnLink);

    }

    private void createTabs() {

        tabSimulation.setClosable(false);
        tabSimulation.setText("Simulación");
        tabSimulation.setClosable(false);
        tabResults.setText("Resultados Phosphorus");
        tabResults.setClosable(false);

        tabResultsHTML.setText("Resultado Phosphorus HTML");
        tabResultsHTML.setClosable(false);

        phosphosrusResults = new PhosphosrusResults(tabResults);
        PhosphosrusHTMLResults resultadosPhosphorousHTML = new PhosphosrusHTMLResults(tabResultsHTML);
        executePane.setPhosphosrusHTMLResults(resultadosPhosphorousHTML);
        executePane.setPhosphosrusResults(phosphosrusResults);

        graphDesignGroup.setScrollPane(scPnWorld);

        grRoot.getChildren().add(graphDesignGroup.getGroup());
        scPnWorld.setContent(grRoot);
        tabSimulation.setContent(scPnWorld);

        tpBox.getTabs().addAll(tabSimulation);


    }

    private HBox createDesignButtons() {

        HBox hboxAllBottom = new HBox();
        hboxAllBottom.getStyleClass().add("cajaInferior");
        hboxAllBottom.setSpacing(10);

        VBox vbLogos = createProjectsLogos();

        entityPropertyTable = new EntityPropertyTable();
        stPnDeviceProperties.getChildren().add(entityPropertyTable);

        TableView<String> tbSimulationProperties = createSimulationPropertiesTab();
        StackPane stPnSimulationProperties = new StackPane();
        stPnSimulationProperties.getChildren().add(tbSimulationProperties);

        SplitPane splPnPropertiesTbs = new SplitPane();
        splPnPropertiesTbs.getItems().addAll(stPnDeviceProperties, stPnSimulationProperties);
        splPnPropertiesTbs.setDividerPositions(0.525f);

        VBox vbxBottomRight = new VBox(10);
        VBox vbxExecuteIndicatorPane = createExecuteIndicatorPane();
        createMapNavigationPanel(vbNavegation);
        vbxBottomRight.getChildren().addAll(vbxExecuteIndicatorPane,vbNavegation);

        hboxAllBottom.getChildren().addAll(vbLogos, splPnPropertiesTbs, vbxBottomRight);
        return hboxAllBottom;
    }

    private VBox createProjectsLogos() {

        ImageView ivAG2 = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/logoAG2.png")));
        double proportionXYAG2 = ivAG2.getBoundsInParent().getWidth()/ivAG2.getBoundsInParent().getHeight();
        ivAG2.setFitHeight(40);
        ivAG2.setFitWidth(40*proportionXYAG2);
        
        ImageView ivInternetIntel = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/logoInterInt.png")));
        double proportionXYInternetIn = ivInternetIntel.getBoundsInParent().getWidth()/ivInternetIntel.getBoundsInParent().getHeight();
        ivInternetIntel.setFitHeight(35);
        ivInternetIntel.setFitWidth(35*proportionXYInternetIn);

        ImageView ivPhosphorus = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/phosphorus.jpg")));
        double proportionXYphosphorus = ivPhosphorus.getBoundsInParent().getWidth()/ivPhosphorus.getBoundsInParent().getHeight();
        ivPhosphorus.setFitHeight(45);
        ivPhosphorus.setFitWidth(45*proportionXYphosphorus);

        ImageView ivDistritalUniv = new ImageView(new Image(getClass().getResourceAsStream("../../../resource/image/escudo_udistrital.jpg")));
        double proportionXYdistritalUniv = ivDistritalUniv.getBoundsInParent().getWidth()/ivDistritalUniv.getBoundsInParent().getHeight();
        ivDistritalUniv.setFitHeight(55);
        ivDistritalUniv.setFitWidth(55*proportionXYdistritalUniv);

        Hyperlink linkAG2 = new Hyperlink();
        Hyperlink linkInterIntel = new Hyperlink();
        Hyperlink linkPhosphorus = new Hyperlink();
        Hyperlink linkDistritalUniv = new Hyperlink();

        linkAG2.setTooltip(new Tooltip("Visite la página web del Grupo de Investigación en \"Colciencias\""));
        linkInterIntel.setTooltip(new Tooltip("Visite la página web del Grupo de Investigación \"Internet Inteligente\""));
        linkPhosphorus.setTooltip(new Tooltip("Visite la página web del proyecto \"Fósforo\""));
        linkDistritalUniv.setTooltip(new Tooltip("Visite la página web de la \"Universidad Distrital FJC\""));

        linkAG2.setGraphic(ivAG2);
        linkInterIntel.setGraphic(ivInternetIntel);
        linkPhosphorus.setGraphic(ivPhosphorus);
        linkDistritalUniv.setGraphic(ivDistritalUniv);

        setOnLunchBrowser(linkAG2,"http://201.234.78.173:8080/gruplac/jsp/visualiza/visualizagr.jsp?nro=00000000003328");
        setOnLunchBrowser(linkInterIntel,"http://gemini.udistrital.edu.co/comunidad/grupos/internetinteligente/");
        setOnLunchBrowser(linkPhosphorus,"http://www.ist-phosphorus.eu/");
        setOnLunchBrowser(linkDistritalUniv,"www.udistrital.edu.co");

        VBox vbLogos = new VBox(10);
        vbLogos.setAlignment(Pos.CENTER);
        vbLogos.setPadding(new Insets(3,5,3,3));
        vbLogos.getChildren().addAll(linkAG2,linkInterIntel,linkPhosphorus,linkDistritalUniv);
        vbLogos.getStyleClass().add("boxLogosHorizontalGradient");
        return vbLogos;
    }

    public void setOnLunchBrowser(Hyperlink link,final String URLToGo) {
        link.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(URLToGo));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }catch (IOException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);}
                }
            });
    }

    private TableView<String> createSimulationPropertiesTab() {

        TableColumn tbColPropNombre = new TableColumn("PROPIEDAD");
        tbColPropNombre.setMinWidth(145);
        tbColPropNombre.setPrefWidth(155);
        tbColPropNombre.setCellValueFactory(new PropertyValueFactory<PropertyPhosphorusTypeEnum, String>("phosphorusPropertyName"));

        TableColumn tbColPropValor = new TableColumn("VALOR");
        tbColPropValor.setMinWidth(150);
        tbColPropValor.setPrefWidth(185);
        tbColPropValor.setCellValueFactory(new PropertyValueFactory<PropertyPhosphorusTypeEnum, Control>("control"));

        TableColumn tbColTituloTbSim = new TableColumn("PROPIEDADES SIMULACIÓN");
        tbColTituloTbSim.getColumns().addAll(tbColPropNombre, tbColPropValor);
        tbwSimulationProperties.getColumns().addAll(tbColTituloTbSim);

        tbwSimulationProperties.setMinWidth(tbColTituloTbSim.getMinWidth() + 13);
        tbwSimulationProperties.setPrefWidth(345);

        tbwSimulationProperties.setPrefHeight(200);

        return tbwSimulationProperties;
    }

    public void createMapNavigationPanel(VBox vBox) {

        gpMapNavegation.setPadding(new Insets(10, 10, 10, 10));
        gpMapNavegation.setVgap(5);
        gpMapNavegation.setHgap(4);
        gpMapNavegation.getStyleClass().addAll("boxLogosVerticalGradient");

        Label lbTitle = new Label("LISTAS DE NAVEGACIÓN");
        lbTitle.setFont(Font.font("Cambria", FontWeight.BOLD, 14));

        final ChoiceBox cbClients = new ChoiceBox(graphDesignGroup.getClientsObservableList());
        final ChoiceBox cbResources = new ChoiceBox(graphDesignGroup.getResourcesObservableList());
        final ChoiceBox cbSwicthes = new ChoiceBox(graphDesignGroup.getSwitchesObservableList());
        final ChoiceBox cbServiceNodes = new ChoiceBox(graphDesignGroup.getBrokersObservableList());

        cbClients.setMinWidth(100);
        cbResources.setMinWidth(100);
        cbSwicthes.setMinWidth(100);
        cbServiceNodes.setMinWidth(100);

        Button btnIrClients = new Button("ir");
        Button btnIrResources = new Button("ir");
        Button btnIrSwichtes = new Button("ir");
        Button btnIrServiceNodes = new Button("ir");

        Label lbRouters = new Label("Enrutadores");
        lbRouters.setFont(Font.font("Cambria", FontWeight.BOLD, 12));
        Label lbClientes = new Label("Clientes");
        lbClientes.setFont(Font.font("Cambria", FontWeight.BOLD, 12));
        Label lbRecursos = new Label("Recursos");
        lbRecursos.setFont(Font.font("Cambria", FontWeight.BOLD, 12));
        Label lbNodosServicio = new Label("Agendadores");
        lbNodosServicio.setFont(Font.font("Cambria", FontWeight.BOLD, 12));

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

                    double maxErrorInX = scPnWorld.getViewportBounds().getWidth() / 2;
                    double funcToCalculateXError = (-2 * maxErrorInX * posXInPercentage) + maxErrorInX;
                    double percentageXError = funcToCalculateXError / worldWidth;

                    double maxErrorInY = scPnWorld.getViewportBounds().getHeight() / 2;
                    double funcToCalculateYError = (-2 * maxErrorInY * posYInPercentage) + maxErrorInY;
                    double percentageYError = funcToCalculateYError / worldHeight;

                    double percentImgHeightCorrecX = (selectedNode.getWidth()) / worldWidth;
                    double percentImgHeightCorrecY = (selectedNode.getHeight()) / worldHeight;

                    scPnWorld.setHvalue(posXInPercentage - percentageXError + percentImgHeightCorrecX);
                    scPnWorld.setVvalue(posYInPercentage - percentageYError - percentImgHeightCorrecY);

                    if(graphDesignGroup.getSelectable()!=null)
                    {
                        graphDesignGroup.getSelectable().select(false);
                    }

                    graphDesignGroup.setSelectable(selectedNode);
                    selectedNode.select(true);
                }
            }
        });
    }

    private void addScene(final Scene escena) {

        escena.setOnKeyPressed(new EventHandler<KeyEvent>() {

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

        escena.setOnKeyReleased(new EventHandler<KeyEvent>() {

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
        scPnWorld.setHvalue(0.27151447890809266);
        scPnWorld.setVvalue(0.4661207267437006);
        ModalAG2window a = new AboutAG2project();

    }

    public GraphDesignGroup getGraphDesignGroup() {
        return graphDesignGroup;
    }

    public EntityPropertyTable getEntityPropertyTb() {
        return entityPropertyTable;
    }

    private VBox createExecuteIndicatorPane() {

        VBox vBoxCajaContenedoraIndicadores = new VBox(10);
        vBoxCajaContenedoraIndicadores.getStyleClass().add("boxLogosVerticalGradient");
        vBoxCajaContenedoraIndicadores.setPadding(new Insets(10, 10, 10, 10));
        vBoxCajaContenedoraIndicadores.setAlignment(Pos.CENTER);

        Label lblIndicadoraEjec = new Label("Ejecución:");
        lblIndicadoraEjec.setFont(new Font("Arial Bold", 10));

        pgBrExecutionProgress = new ProgressBar(0);
        pgBrExecutionProgress.getStyleClass().add("progress-bar");
        pgBrExecutionProgress.setPrefWidth(150);
        pgBrExecutionProgress.setTooltip(new Tooltip("Muestra el estado de ejecución de la simulación"));

        vBoxCajaContenedoraIndicadores.getChildren().addAll(lblIndicadoraEjec, pgBrExecutionProgress);

        return vBoxCajaContenedoraIndicadores;
    }

    public void enable() {

        GUI.setActionTypeEmun(beforeActionTypeEmun);
        graphDesignGroup.getGroup().setCursor(beforeEventCursor);
        gpTools.setDisable(false);
        gpTools.setOpacity(1);
        //prgBarBarraProgresoEjec.setProgress(0);
        pgBrExecutionProgress.setVisible(false);
        graphDesignGroup.getGroup().setOpacity(1);


    }

    public void disable() {
        beforeActionTypeEmun = GUI.getActionTypeEmun();
        beforeEventCursor = graphDesignGroup.getGroup().getCursor();

        GUI.setActionTypeEmun(ActionTypeEmun.HAND);
        graphDesignGroup.getGroup().setCursor(ActionTypeEmun.HAND.getCursorImage());

        pgBrExecutionProgress.setVisible(true);
        gpTools.setDisable(true);
        gpTools.setOpacity(0.8);
        pgBrExecutionProgress.setProgress(-1);
        graphDesignGroup.getGroup().setOpacity(0.8);
        if(!tpBox.getTabs().contains(tabResultsHTML)){
        tpBox.getTabs().addAll(  tabResultsHTML, tabResults);
        }

        tpBox.getSelectionModel().select(tabResults);

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


}
