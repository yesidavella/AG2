package com.ag2.presentacion.diseño;

import com.ag2.presentacion.Main;
import com.ag2.presentacion.TiposDeBoton;
import com.ag2.presentacion.controles.GrupoDeDiseno;
import com.sun.scenario.effect.Color4f;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public abstract class NodoGrafico extends Group {

    private String nombre = null;
    private Image imagen = null;
    public ArrayList<NodoListener> nodosListener = new ArrayList<NodoListener>();
    private static NodoGrafico nodoAComodin = null;
    public Line enlaceComodin = new Line();
    public static final byte CENTRO_IMAGEN_NODO_GRAFICO = 15;
    private boolean estaEliminado = false;
    private ImageView imageView;
    private Label lblNombre;
    public boolean selecionado = false;
    DropShadow dropShadow = new DropShadow();

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
        if (!selecionado) 
        {
            dropShadow.setColor(Color.WHITESMOKE);
            dropShadow.setSpread(.2);
            dropShadow.setWidth(15);
            dropShadow.setHeight(15);
        }
        else
        {
            dropShadow.setColor(Color.AQUAMARINE);
            dropShadow.setSpread(.5);
            dropShadow.setWidth(60);
            dropShadow.setHeight(60);
        }
    }

    @Override
    public String toString() {
        return nombre;
    }

    public NodoGrafico(String nombre, String urlDeImagen) {
        setSelecionado(true);

        VBox vBox = new VBox();
        //  vBox.setStyle("-fx-background-color:#FA0606");
        setEffect(dropShadow);

        this.nombre = nombre;
        lblNombre = new Label(nombre);
        lblNombre.setTextFill(Color.BLACK);

        lblNombre.setStyle("-fx-font: bold 8pt 'Arial'; -fx-background-color:#CCD4EC");


        imagen = new Image(getClass().getResourceAsStream(urlDeImagen));
        imageView = new ImageView(imagen);
        // Rectangle rectangle = new Rectangle(imagen.getWidth(), imagen.getWidth()); 
        vBox.getChildren().addAll(imageView, lblNombre);


        this.getChildren().addAll(vBox);
        //La escala a la mitad por q la imagen esta al 2X de tamaño deseado
        setScaleX(0.5);
        setScaleY(0.5);

        setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
                NodoGrafico nodoGrafico = (NodoGrafico) mouseEvent.getSource();
                GrupoDeDiseno group = (GrupoDeDiseno) nodoGrafico.getParent();

                if (Main.getEstadoTipoBoton() == TiposDeBoton.ELIMINAR) 
                {
                    nodoGrafico.setEliminado(true);
                    group.getChildren().remove(nodoGrafico);
                    group.eliminarNodeListaNavegacion(nodoGrafico);

                }
                if (Main.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) 
                {
                    if(group.getNodoGraficoSelecionado()!=null)
                    {    
                        group.getNodoGraficoSelecionado().setSelecionado(false);
                    }                                             
                    nodoGrafico.setSelecionado(true);
                    group.setNodoGraficoSelecionado(nodoGrafico); 
                }
                updateNodoListener();
            }
        });

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

        setOnMouseDragged(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent) {
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

                    Group group = (Group) nodoGrafico.getParent();
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

    public void addNodoListener(NodoListener listenerNodo) {
        nodosListener.add(listenerNodo);
    }

    public void removeNodoListener(NodoListener listenerNodo) {
        nodosListener.remove(listenerNodo);
    }

    public void updateNodoListener() {
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
}