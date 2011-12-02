package com.ag2.presentacion.diseño;

import com.ag2.config.serializacion.*;
import com.ag2.presentacion.Main;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
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

public abstract class NodoGrafico extends Group implements Serializable {

    private String nombre = null;
    private transient Image imagen = null;
    private ArrayList<NodoListener> nodosListener = new ArrayList<NodoListener>();
    private static NodoGrafico nodoAComodin = null;
    private transient Line enlaceComodin = new Line();
    public static final byte CENTRO_IMAGEN_NODO_GRAFICO = 15;
    private boolean estaEliminado = false;
    private transient ImageView imageView;
    private transient Label lblNombre;
    private boolean selecionado = false;
    private transient DropShadow dropShadow = new DropShadow();
    private transient VBox vBox = new VBox();
    private double posX;
    private boolean arrastrando = false;

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
            vBox.setStyle("-fx-border-width: 0");
        } else {

            vBox.setStyle("-fx-border-color: #55FFF7;-fx-border-width: 2");
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

    public NodoGrafico(String nombre, String urlDeImagen) {
        setSelecionado(true);
        //  vBox.setStyle("-fx-background-color:#FA0606");
        setEffect(dropShadow);
        this.urlDeImagen = urlDeImagen;
        this.nombre = nombre;
        lblNombre = new Label(nombre);
        lblNombre.setTextFill(Color.BLACK);
        lblNombre.setStyle("-fx-font: bold 8pt 'Arial'; -fx-background-color:#CCD4EC");

        imagen = new Image(getClass().getResourceAsStream(urlDeImagen));
        imageView = new ImageView(imagen);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(imageView, lblNombre);


        this.getChildren().addAll(vBox);
        //La escala a la mitad por q la imagen esta al 2X de tamaño deseado
        setScaleX(0.5);
        setScaleY(0.5);

        establecerEventoOnMouseClicked();

        establecerEventoOnMousePressed();

        establecerEventoOnMouseDragged();

        establecerEventoOnMouseReleased();

        establecerEventoOnMouseEntered();
    }

    private void establecerEventoOnMouseEntered() {
        setOnMouseEntered(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                TiposDeBoton tipoDeBotonSeleccionado = Main.getEstadoTipoBoton();
                NodoGrafico nodoGrafico = (NodoGrafico) mouseEvent.getSource();

                if (tipoDeBotonSeleccionado == TiposDeBoton.ENLACE) {
                    setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                } else {
                    setCursor(tipoDeBotonSeleccionado.getImagenCursor());
                }

                if (tipoDeBotonSeleccionado == TiposDeBoton.ENLACE
                        && nodoAComodin != null
                        && nodoAComodin != nodoGrafico) {

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

    private void establecerEventoOnMouseReleased() {
        setOnMouseReleased(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {

                setScaleX(0.5);
                setScaleY(0.5);

                if (Main.getEstadoTipoBoton() == TiposDeBoton.ENLACE) {
                    NodoGrafico nodoGrafico = (NodoGrafico) mouseEvent.getSource();
                    Group group = (Group) nodoGrafico.getParent();
                    group.getChildren().remove(enlaceComodin);
                }
            }
        });
    }

    private void establecerEventoOnMouseDragged() {
        setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                NodoGrafico nodoGrafico = (NodoGrafico) mouseEvent.getSource();
                arrastrando = true;
                GrupoDeDiseno group = (GrupoDeDiseno) nodoGrafico.getParent();
                if (Main.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
                    setLayoutX(getLayoutX() + mouseEvent.getX() - CENTRO_IMAGEN_NODO_GRAFICO);
                    setLayoutY(getLayoutY() + mouseEvent.getY() - CENTRO_IMAGEN_NODO_GRAFICO);
                    updateNodoListener();
                } else if (Main.getEstadoTipoBoton() == TiposDeBoton.ENLACE) {
                    enlaceComodin.setEndX(getLayoutX() + (mouseEvent.getX()));
                    enlaceComodin.setEndY(getLayoutY() + (mouseEvent.getY()));
                }
            }
        });
    }

    private void establecerEventoOnMousePressed() {
        setOnMousePressed(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                if (Main.getEstadoTipoBoton() == TiposDeBoton.ENLACE) {

                    NodoGrafico nodoGrafico = (NodoGrafico) mouseEvent.getSource();
                    nodoAComodin = nodoGrafico;
                    double x = nodoGrafico.getLayoutX();
                    double y = nodoGrafico.getLayoutY();
                    enlaceComodin.setStartX(x + CENTRO_IMAGEN_NODO_GRAFICO);
                    enlaceComodin.setStartY(y + CENTRO_IMAGEN_NODO_GRAFICO);
                    enlaceComodin.setEndX(x + CENTRO_IMAGEN_NODO_GRAFICO);
                    enlaceComodin.setEndY(y + CENTRO_IMAGEN_NODO_GRAFICO);
                    Group group = (Group) nodoGrafico.getParent();
                    group.getChildren().add(enlaceComodin);
                    nodoGrafico.toFront();
                }

                setScaleX(1);
                setScaleY(1);
            }
        });
    }

    private void establecerEventoOnMouseClicked() {
        setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                NodoGrafico nodoGrafico = (NodoGrafico) mouseEvent.getSource();
                GrupoDeDiseno group = (GrupoDeDiseno) nodoGrafico.getParent();

                if (Main.getEstadoTipoBoton() == TiposDeBoton.ELIMINAR) {
                    nodoGrafico.setEliminado(true);
                    group.getChildren().remove(nodoGrafico);
                    group.eliminarNodeListaNavegacion(nodoGrafico);

                }
                if (Main.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) {
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
            vBox = new VBox();
            lblNombre = new Label(nombre);
            lblNombre.setTextFill(Color.BLACK);
            lblNombre.setStyle("-fx-font: bold 8pt 'Arial'; -fx-background-color:#CCD4EC");
            vBox.getChildren().addAll(imageView, lblNombre);
            vBox.setAlignment(Pos.CENTER);
            this.getChildren().addAll(vBox);
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
}