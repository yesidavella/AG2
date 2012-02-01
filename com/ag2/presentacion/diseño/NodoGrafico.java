package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminEnlace;
import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.IGU;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public abstract class NodoGrafico implements ObjetoSeleccionable, Serializable {

    
    private static ImageView IMG_VW_DENY_LINK = new ImageView(
            new Image(NodoGrafico.class.getResourceAsStream("../../../../recursos/imagenes/prohibido_enlace.png")));
    
    protected transient Image imagen = null;
    private transient Line enlaceComodin;
    private transient ImageView imageView;
    protected transient Label lblNombre;
    private transient DropShadow dropShadow = new DropShadow();
    protected transient VBox cuadroExteriorResaltado;
    private transient Group group;
    
    
    private String nombre = null;
    private ArrayList<NodoListener> nodosListener = new ArrayList<NodoListener>();
    private static NodoGrafico nodoAComodin = null;
    private boolean estaEliminado = false;
    private boolean selecionado = false;
  
    private String urlDeImagen;
    private boolean arrastrando = false;
    private ControladorAbstractoAdminNodo controladorAbstractoAdminNodo;
    private ControladorAbstractoAdminEnlace controladorAdminEnlace;
    private short alto;
    private short ancho;
    public static boolean inicioGeneracionDeEnlace = false;
    private short cantidadDeEnlaces = 0;
    private String nombreOriginal;
    protected short pasoDeSaltoLinea;
    private short altoInicial = 0;
    private HashMap<String, String> propertiesNode;   
    private HashMap<String, String> subPropertiesNode;
    private GrupoDeDiseno grupoDeDiseno;    
    private double layoutX; 
    private double layoutY; 

    public NodoGrafico(GrupoDeDiseno grupoDeDiseno, String nombre, String urlDeImagen, ControladorAbstractoAdminNodo controladorAbstractoAdminNodo, ControladorAbstractoAdminEnlace ctrlAbsAdminEnlace) 
    {
        
        this.grupoDeDiseno = grupoDeDiseno;
        this.controladorAbstractoAdminNodo = controladorAbstractoAdminNodo;
        this.controladorAdminEnlace = ctrlAbsAdminEnlace;    
        this.urlDeImagen = urlDeImagen;
        this.nombre = nombre;
        this.nombreOriginal = nombre;
        propertiesNode = new HashMap<String, String>(); 
        subPropertiesNode = new HashMap<String, String>();       
       
        initTransientObjects();

        
    }
   public void initTransientObjects()
   {
        group = new Group();
        group.setEffect(dropShadow);
        lblNombre = new Label(formatearNombre(nombre));
        lblNombre.setTextFill(Color.BLACK);
        lblNombre.setTextAlignment(TextAlignment.CENTER);
        lblNombre.setStyle("-fx-font: bold 8pt 'Arial'; -fx-background-color:#CCD4EC");
        
        
        enlaceComodin = new Line(); 
        cuadroExteriorResaltado = new VBox();
        imagen = new Image(getClass().getResourceAsStream(urlDeImagen));
        imageView = new ImageView(imagen);
        cuadroExteriorResaltado.setAlignment(Pos.CENTER);
        cuadroExteriorResaltado.getChildren().addAll(imageView, lblNombre);
        
        group.getChildren().addAll(cuadroExteriorResaltado);
        group.setScaleX(0.5);
        group.setScaleY(-0.5);

        establecerEventoOnMouseClicked();
        establecerEventoOnMousePressed();
        establecerEventoOnMouseDragged();
        establecerEventoOnMouseReleased();
        establecerEventoOnMouseEntered();
        establecerEventoOnMouseExit();
   }
    
    

    public HashMap<String, String> getNodeProperties() {
        return propertiesNode;
    }

    public void setAltoInicial(short altoInicial) {
        this.altoInicial = altoInicial;
    }

    public short getAltoInicial() {
        return altoInicial;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NodoGrafico) {
            NodoGrafico nodoGrafico = (NodoGrafico) obj;
            return nombreOriginal.equals(nodoGrafico.getNombreOriginal());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.nombreOriginal != null ? this.nombreOriginal.hashCode() : 0);
        return hash;
    }

    
   

    public boolean isSelecionado() {
        return selecionado;
    }

    public void seleccionar(boolean selecionado) {
        this.selecionado = selecionado;
        if (!selecionado) {
            dropShadow.setColor(Color.WHITESMOKE);
            dropShadow.setSpread(.2);
            dropShadow.setWidth(20);
            dropShadow.setHeight(20);

            cuadroExteriorResaltado.getStyleClass().remove("nodoSeleccionado");
            cuadroExteriorResaltado.getStyleClass().add("nodoNoSeleccionado");
        } else {
            controladorAbstractoAdminNodo.consultarPropiedades(this);
            cuadroExteriorResaltado.getStyleClass().remove("nodoNoSeleccionado");
            cuadroExteriorResaltado.getStyleClass().add("nodoSeleccionado");

            group.toFront();
            dropShadow.setColor(Color.web("#44FF00"));
            dropShadow.setSpread(.2);
            dropShadow.setWidth(25);
            dropShadow.setHeight(25);
        }
    }

    @Override
    public String toString() {
        return nombre;
    }

    private void establecerEventoOnMouseEntered() {
        group.setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                TiposDeBoton tipoDeBotonSeleccionado = IGU.getEstadoTipoBoton();
                NodoGrafico nodoGrafico =NodoGrafico.this ;

                if (tipoDeBotonSeleccionado == TiposDeBoton.ENLACE) {
                    group.setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                } else {
                    group.setCursor(tipoDeBotonSeleccionado.getImagenCursor());
                }

                if (inicioGeneracionDeEnlace == false) {
                    nodoAComodin = null;
                }

                if (tipoDeBotonSeleccionado == TiposDeBoton.ENLACE) {

                    if (nodoAComodin != null && nodoAComodin != nodoGrafico && NodoGrafico.inicioGeneracionDeEnlace) {

                        if (nodoGrafico.puedeGenerarEnlaceCon(nodoAComodin)) {



                            grupoDeDiseno.remove(enlaceComodin);

                            EnlaceGrafico enlaceGrafico = new EnlaceGrafico(grupoDeDiseno, nodoAComodin, nodoGrafico, controladorAdminEnlace);
                            enlaceGrafico.addArcosInicialAlGrupo();

                            nodoAComodin.setCantidadDeEnlaces((short) (nodoAComodin.getCantidadDeEnlaces() + 1));
                            nodoGrafico.setCantidadDeEnlaces((short) (nodoGrafico.getCantidadDeEnlaces() + 1));

                            nodoAComodin.getGroup().toFront();
                            nodoGrafico.getGroup().toFront();
                        } else {
                            nodoGrafico.playDenyLinkAnimation();
                        }
                    }

                } else if (tipoDeBotonSeleccionado == TiposDeBoton.ELIMINAR) {
                    group.setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                }
                nodoAComodin = null;
            }
        });
    }

    private void establecerEventoOnMousePressed() {
        group.setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                setAncho((short) cuadroExteriorResaltado.getWidth());
                setAlto((short) cuadroExteriorResaltado.getHeight());

                if (IGU.getEstadoTipoBoton() == TiposDeBoton.ENLACE) {

                    NodoGrafico nodoGrafico = NodoGrafico.this; 
                    nodoAComodin = nodoGrafico;
                   
                    double x = nodoGrafico.getLayoutX();
                    double y = nodoGrafico.getLayoutY();
                    enlaceComodin.setStartX(x + ancho / 2);
                    enlaceComodin.setStartY(y + alto / 2);
                    enlaceComodin.setEndX(x + ancho / 2);
                    enlaceComodin.setEndY(y + alto / 2);                    
                    grupoDeDiseno.add(enlaceComodin);
                    enlaceComodin.toFront(); 
                    nodoGrafico.group.toFront();

                    if (mouseEvent.isPrimaryButtonDown() && group.isHover()) {
                        NodoGrafico.inicioGeneracionDeEnlace = true;
                    }
                }
                group.setScaleX(1);
                group.setScaleY(-1);
            }
        });
    }

    private void establecerEventoOnMouseDragged() {
        group.setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
               

                if (IGU.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    setLayoutX(getLayoutX() + mouseEvent.getX() - ancho / 2);
                    setLayoutY(getLayoutY() - (mouseEvent.getY() - alto / 2));
                    updateNodoListener();
                } else if (IGU.getEstadoTipoBoton() == TiposDeBoton.ENLACE)
                {
                     arrastrando = true;
                    enlaceComodin.setEndX(getLayoutX() + (mouseEvent.getX()));
                    enlaceComodin.setEndY(getLayoutY() + alto - (mouseEvent.getY()));
                }
            }
        });
    }

    private void establecerEventoOnMouseReleased() {
        group.setOnMouseReleased(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                group.setScaleX(0.5);
                group.setScaleY(-0.5);

                if (IGU.getEstadoTipoBoton() == TiposDeBoton.ENLACE) {
                                    
                    grupoDeDiseno.remove(enlaceComodin);

                }
            }
        });
    }

    private void establecerEventoOnMouseClicked() {
        group.setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {


                NodoGrafico nodoGrafico = NodoGrafico.this; 


                if (IGU.getEstadoTipoBoton() == TiposDeBoton.ELIMINAR) {

                    nodoGrafico.setEliminado(true);
                    grupoDeDiseno.remove(nodoGrafico);
                    grupoDeDiseno.eliminarNodeListaNavegacion(nodoGrafico);
                    controladorAbstractoAdminNodo.removeNodo(nodoGrafico);

                }
                if (IGU.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {

                    ObjetoSeleccionable objSeleccionado = grupoDeDiseno.getObjetoGraficoSelecionado();

                    if (!arrastrando) {
                        if (objSeleccionado == nodoGrafico) {
                            objSeleccionado.seleccionar(false);
                            grupoDeDiseno.setObjetoGraficoSelecionado(null);
                        } else {
                            if (objSeleccionado == null) {
                                nodoGrafico.seleccionar(true);
                                grupoDeDiseno.setObjetoGraficoSelecionado(nodoGrafico);
                            } else {
                                objSeleccionado.seleccionar(false);
                                nodoGrafico.seleccionar(true);
                                grupoDeDiseno.setObjetoGraficoSelecionado(nodoGrafico);
                            }
                        }

                    } else {
                        if (nodoGrafico != objSeleccionado) {
                            if (objSeleccionado == null) {
                                nodoGrafico.seleccionar(true);
                                grupoDeDiseno.setObjetoGraficoSelecionado(nodoGrafico);
                            } else {
                                objSeleccionado.seleccionar(false);
                                nodoGrafico.seleccionar(true);
                                grupoDeDiseno.setObjetoGraficoSelecionado(nodoGrafico);
                            }
                        }
                    }
                    arrastrando = false;
                }
                updateNodoListener();
            }
        });
    }

    private void establecerEventoOnMouseExit() {

        group.setOnMouseExited(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                //System.out.println("Exit:" + getNombre());

                if (!mouseEvent.isPrimaryButtonDown()) {
                    NodoGrafico.inicioGeneracionDeEnlace = false;
                    nodoAComodin = null;
                }

            }
        });
    }

    public void addNodoListener(NodoListener listenerNodo) {
        nodosListener.add(listenerNodo);
    }

    public void removeNodoListener(NodoListener listenerNodo) {
        nodosListener.remove(listenerNodo);
    }

    public void updateNodoListener() {
        layoutX = this.getLayoutX();
        layoutY = this.getLayoutY();

        for (NodoListener nodoListener : nodosListener) {
            nodoListener.update();
        }
    }

    public Image getImagen() {
        return imagen;
    }

    public boolean isEliminado() {
        return estaEliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.estaEliminado = eliminado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        lblNombre.setText(formatearNombreConSaltoDeLineas(nombre));
    }

   
    private void readObject(ObjectInputStream inputStream)
    {
        try
        {
            inputStream.defaultReadObject();
            initTransientObjects();
            group.setLayoutX(layoutX);
            group.setLayoutY(layoutY);
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public short getAltoActual() {
        return alto;
    }

    public void setAlto(short alto) {

        if (altoInicial == 0) {
            setAltoInicial(alto);
        }

        this.alto = alto;
    }

    public short getAnchoActual() {
        return ancho;
    }

    public void setAncho(short ancho) {
        this.ancho = ancho;
    }

    private String formatearNombre(String nombre) {

        if (nombre.startsWith("Enrutador")) {
            return nombre.substring(0, "Enrutador".length()) + "\n" + nombre.substring("Enrutador".length() + 1, nombre.length());
        }
        return nombre;
    }

    public short getCantidadDeEnlaces() {
        return cantidadDeEnlaces;
    }

    public void setCantidadDeEnlaces(short cantidadDeEnlaces) {
        this.cantidadDeEnlaces = cantidadDeEnlaces;
    }

    public boolean isInicioGeneracionDeEnlace() {
        return inicioGeneracionDeEnlace;
    }

    public void setinicioGeneracionDeEnlace(boolean inicioGeneracionDeEnlace) {
        this.inicioGeneracionDeEnlace = inicioGeneracionDeEnlace;
    }

    public String formatearNombreConSaltoDeLineas(String nombre) {

        StringBuilder nombreModificado = new StringBuilder();

        nombre = nombre.trim();
        int tamaño = nombre.length();
        int i = 0;

        while (tamaño >= pasoDeSaltoLinea) {

            nombreModificado.append(nombre.substring(i * pasoDeSaltoLinea, (i * pasoDeSaltoLinea) + pasoDeSaltoLinea)).append("\n");

            tamaño = nombre.substring(((i * pasoDeSaltoLinea) + pasoDeSaltoLinea)).length();

            if (tamaño > 0 && tamaño < pasoDeSaltoLinea) {
                nombreModificado.append(nombre.substring(((i * pasoDeSaltoLinea) + pasoDeSaltoLinea)));
            }

            i++;
        }
        setAlto((short) cuadroExteriorResaltado.getHeight());
        setAncho((short) cuadroExteriorResaltado.getWidth());
        updateNodoListener();
        return (nombreModificado.length() == 0) ? nombre : nombreModificado.toString();
    }

    public Line getEnlaceComodin() {
        return enlaceComodin;
    }

    public HashMap<String, String> getSubPropertiesNode() {
        return subPropertiesNode;
    }

    public Group getGroup() {
        return group;
    }

    public double getLayoutX() {
        return layoutX;
    }

    public void setLayoutX(double layoutX) {
        this.layoutX = layoutX;
        group.setLayoutX(layoutX);
    }

    public double getLayoutY() {
        return layoutY;
    }

    public void setLayoutY(double layoutY) {
        this.layoutY = layoutY;
        group.setLayoutY(layoutY);
    }
    

    private void playDenyLinkAnimation() {

        IMG_VW_DENY_LINK.setLayoutX(getLayoutX() + getAnchoActual() / 2 - IMG_VW_DENY_LINK.getBoundsInParent().getWidth() / 2);
        IMG_VW_DENY_LINK.setLayoutY(getLayoutY() + 0.75 * getAltoActual() - (getAltoInicial() / 4 + IMG_VW_DENY_LINK.getBoundsInParent().getHeight() / 2));


        grupoDeDiseno.add(IMG_VW_DENY_LINK);

        FadeTransition fadeImgDenyLink = new FadeTransition(Duration.millis(800), IMG_VW_DENY_LINK);
        fadeImgDenyLink.setFromValue(1.0);
        fadeImgDenyLink.setToValue(0);
        fadeImgDenyLink.play();

        fadeImgDenyLink.setOnFinished(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent arg0) {
                grupoDeDiseno.remove(IMG_VW_DENY_LINK);
            }
        });
    }

    public abstract boolean puedeGenerarEnlaceCon(NodoGrafico nodoInicioDelEnlace);
}
