package com.ag2.presentacion.diseño;

import com.ag2.controlador.ControladorAbstractoAdminNodo;
import com.ag2.presentacion.IGU;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;

public abstract class NodoGrafico extends Group implements Serializable {

    private String nombre = null;
    protected  transient Image imagen = null;
    private ArrayList<NodoListener> nodosListener = new ArrayList<NodoListener>();
    private static NodoGrafico nodoAComodin = null;
    private transient Line enlaceComodin = new Line();
    private boolean estaEliminado = false;
    private transient ImageView imageView;
    private transient Label lblNombre;
    private boolean selecionado = false;
    private transient DropShadow dropShadow = new DropShadow();
    private transient VBox cuadroExteriorResaltado = new VBox();
    private double posX;
    private boolean arrastrando = false;
    private ControladorAbstractoAdminNodo controladorAbstractoAdminNodo;
    private short alto;
    private short ancho;
    public static boolean b = false;

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }
    
    
    public NodoGrafico(String nombre, String urlDeImagen, ControladorAbstractoAdminNodo controladorAbstractoAdminNodo) {
        this.controladorAbstractoAdminNodo = controladorAbstractoAdminNodo; 
//        setSelecionado(true);
        //  vBox.setStyle("-fx-background-color:#FA0606");
        setEffect(dropShadow);
        this.urlDeImagen = urlDeImagen;
        this.nombre = nombre;
        lblNombre = new Label(nombre);
        lblNombre.setTextFill(Color.BLACK);
        lblNombre.setTextAlignment(TextAlignment.CENTER);
        lblNombre.setStyle("-fx-font: bold 8pt 'Arial'; -fx-background-color:#CCD4EC");
     
        imagen = new Image(getClass().getResourceAsStream(urlDeImagen)); 
        imageView = new ImageView(imagen);
        cuadroExteriorResaltado.setAlignment(Pos.CENTER);
        cuadroExteriorResaltado.getChildren().addAll(imageView, lblNombre);

        this.getChildren().addAll(cuadroExteriorResaltado);
        //La escala a la mitad por q la imagen esta al 2X de tamaño deseado
        setScaleX(0.5);
        setScaleY(0.5);

        establecerEventoOnMouseClicked();

        establecerEventoOnMousePressed();

        establecerEventoOnMouseDragged();

        establecerEventoOnMouseReleased();

        establecerEventoOnMouseEntered();
        
        establecerEventoOnMouseExit();
        
    }


    @Override
    public boolean equals(Object obj) 
    {
        if(obj instanceof  NodoGrafico)
        {
            NodoGrafico nodoGrafico = (NodoGrafico)obj; 
             return nombre.equals( nodoGrafico.getNombre()); 
        }    
        return false; 
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
        return hash;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
        setLayoutX(posX);
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
        setLayoutY(posY);
    }
    private double posY;
    private String urlDeImagen;

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
        if (!selecionado) {
            dropShadow.setColor(Color.WHITESMOKE);
            dropShadow.setSpread(.2);
            dropShadow.setWidth(20);
            dropShadow.setHeight(20);
            cuadroExteriorResaltado.setStyle("-fx-border-width: 0");
        } else 
        {
            controladorAbstractoAdminNodo.consultarPropiedades(this); 
            cuadroExteriorResaltado.setStyle("-fx-border-color: #55FFF7;-fx-border-width: 2");
            this.toFront();
            dropShadow.setColor(Color.AQUAMARINE);
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
        setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {     

                TiposDeBoton tipoDeBotonSeleccionado = IGU.getEstadoTipoBoton();
                NodoGrafico nodoGrafico = (NodoGrafico) mouseEvent.getSource();

                if (tipoDeBotonSeleccionado == TiposDeBoton.ENLACE) {
                    setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                } else {
                    setCursor(tipoDeBotonSeleccionado.getImagenCursor());
                }
                
                if (b==false) {
                    nodoAComodin = null;
                }

                if (tipoDeBotonSeleccionado == TiposDeBoton.ENLACE
                        && nodoAComodin != null
                        && nodoAComodin != nodoGrafico && NodoGrafico.b) {

                    GrupoDeDiseno group = (GrupoDeDiseno) nodoGrafico.getParent();
                    group.getChildren().remove(enlaceComodin);

                    EnlaceGrafico enlaceGrafico = new EnlaceGrafico(group, nodoAComodin, nodoGrafico);
                    enlaceGrafico.addArcosInicialAlGrupo();

                    nodoAComodin.toFront();
                    nodoGrafico.toFront();

                } else if (tipoDeBotonSeleccionado == TiposDeBoton.ELIMINAR) {
                    setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                }
                nodoAComodin = null;
            }
        });
    }

    private void establecerEventoOnMousePressed() {
        setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                
                System.out.println("Pressed:"+getNombre());
                
                if (IGU.getEstadoTipoBoton() == TiposDeBoton.ENLACE) {

                    NodoGrafico nodoGrafico = (NodoGrafico)mouseEvent.getSource();
                    nodoAComodin = nodoGrafico;
                    double x = nodoGrafico.getLayoutX();
                    double y = nodoGrafico.getLayoutY();
                    enlaceComodin.setStartX(x + ancho/2);
                    enlaceComodin.setStartY(y + alto/2);
                    enlaceComodin.setEndX(x + ancho/2);
                    enlaceComodin.setEndY(y + alto/2);
                    Group group = (Group) nodoGrafico.getParent();
                    group.getChildren().add(enlaceComodin);
                    nodoGrafico.toFront();
                    
                    if(mouseEvent.isPrimaryButtonDown() && isHover()){
                        NodoGrafico.b = true;
                        System.out.println("Botn Primario opri y hover...");
                    }
                }

                setScaleX(1);
                setScaleY(1);
            }
        });
    }
    
        private void establecerEventoOnMouseDragged() {
        setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                System.out.println("Drangged:"+getNombre());
                NodoGrafico nodoGrafico = (NodoGrafico) mouseEvent.getSource();
                arrastrando = true;
                GrupoDeDiseno group = (GrupoDeDiseno)nodoGrafico.getParent();
                if (IGU.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    setLayoutX(getLayoutX() + mouseEvent.getX()-ancho/2);
                    setLayoutY(getLayoutY() + mouseEvent.getY()-alto/2);
                    updateNodoListener();
                } else if (IGU.getEstadoTipoBoton() == TiposDeBoton.ENLACE) {
                    enlaceComodin.setEndX(getLayoutX() + (mouseEvent.getX()));
                    enlaceComodin.setEndY(getLayoutY() + (mouseEvent.getY()));
                }
            }
        });
    }
    
    private void establecerEventoOnMouseReleased() {
        setOnMouseReleased(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                
                System.out.println("Solto:"+getNombre()+" -- B="+b);

                setScaleX(0.5);
                setScaleY(0.5);

                if (IGU.getEstadoTipoBoton() == TiposDeBoton.ENLACE) {
                    NodoGrafico nodoGrafico = (NodoGrafico) mouseEvent.getSource();
                    Group group = (Group) nodoGrafico.getParent();
                    group.getChildren().remove(enlaceComodin);
                    
                    
                }
            }
        });
    }

    private void establecerEventoOnMouseClicked() {
        setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                
                System.out.println("Clicked:"+getNombre());
                
                NodoGrafico nodoGrafico = (NodoGrafico) mouseEvent.getSource();
                GrupoDeDiseno group = (GrupoDeDiseno) nodoGrafico.getParent();

                if (IGU.getEstadoTipoBoton() == TiposDeBoton.ELIMINAR) {
                    nodoGrafico.setEliminado(true);
                    group.getChildren().remove(nodoGrafico);
                    group.eliminarNodeListaNavegacion(nodoGrafico);

                }
                if (IGU.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    NodoGrafico nodoGraficoSelecionado = group.getNodoGraficoSelecionado();
                    if (!arrastrando) 
                    {
                        if (nodoGraficoSelecionado == nodoGrafico) {
                            nodoGraficoSelecionado.setSelecionado(false);
                            group.setNodoGraficoSelecionado(null);
                        } else {
                            if (nodoGraficoSelecionado == null) {
                                nodoGrafico.setSelecionado(true);
                                group.setNodoGraficoSelecionado(nodoGrafico);
                            } else {
                                nodoGraficoSelecionado.setSelecionado(false);
                                nodoGrafico.setSelecionado(true);
                                group.setNodoGraficoSelecionado(nodoGrafico);
                            }
                        }

                    } else {
                        if (nodoGrafico != nodoGraficoSelecionado) {
                            if (nodoGraficoSelecionado == null) {
                                nodoGrafico.setSelecionado(true);
                                group.setNodoGraficoSelecionado(nodoGrafico);
                            } else {
                                nodoGraficoSelecionado.setSelecionado(false);
                                nodoGrafico.setSelecionado(true);
                                group.setNodoGraficoSelecionado(nodoGrafico);
                            }
                        }
                    }
                    arrastrando = false;

                }
                updateNodoListener();
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
        posX = this.getLayoutX();
        posY = this.getLayoutY();

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
    }

    private void readObject(ObjectInputStream inputStream) {
        try {
            inputStream.defaultReadObject();
            setLayoutX(posX);
            setLayoutY(posY);
            imagen = new Image(getClass().getResourceAsStream(urlDeImagen));
            imageView = new ImageView(imagen);
            cuadroExteriorResaltado = new VBox();
            lblNombre = new Label(nombre);
            lblNombre.setTextFill(Color.BLACK);
            lblNombre.setStyle("-fx-font: bold 8pt 'Arial'; -fx-background-color:#CCD4EC");
            cuadroExteriorResaltado.getChildren().addAll(imageView, lblNombre);
            cuadroExteriorResaltado.setAlignment(Pos.CENTER);
            this.getChildren().addAll(cuadroExteriorResaltado);
            enlaceComodin = new Line();
            setScaleX(0.5);
            setScaleY(0.5);
            dropShadow = new DropShadow();
            setSelecionado(false);
            setEffect(dropShadow);
            establecerEventoOnMouseClicked();
            establecerEventoOnMousePressed();
            establecerEventoOnMouseDragged();
            establecerEventoOnMouseReleased();
            establecerEventoOnMouseEntered();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public short getAlto() {
        return alto;
    }

    public void setAlto(short alto) {
        this.alto = alto;
    }

    public short getAncho() {
        return ancho;
    }

    public void setAncho(short ancho) {
        this.ancho = ancho;
    }

    private void establecerEventoOnMouseExit() {

        setOnMouseExited(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                System.out.println("Exit:" + getNombre());
                
                
                if (!mouseEvent.isPrimaryButtonDown()) {
                    NodoGrafico.b = false;
                    nodoAComodin = null;
                }

            }
        });
    }
    
}