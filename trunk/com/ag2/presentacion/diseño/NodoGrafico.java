package com.ag2.presentacion.diseño;

import com.ag2.presentacion.Main;
import com.ag2.presentacion.TiposDeBoton;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

public abstract class NodoGrafico extends ImageView {

    private String nombre = null;
    private Image imagen = null;
    ArrayList<NodoListener> nodosListener = new ArrayList<NodoListener>();
    private static NodoGrafico nodoAComodin = null;
    Line enlaceComodin = new Line();
    public static final byte CENTRO_IMAGEN_NODO_GRAFICO = 15; 
    
    private boolean estaEliminado = false; 
    
    
    @Override
    public String toString()
    {
        return nombre; 
    }

    public NodoGrafico(String nombre, String urlDeImagen) {
        this.nombre = nombre;
        imagen = new Image(getClass().getResourceAsStream(urlDeImagen));
        this.setImage(imagen);
        
        //La escala a la mitad por q la imagen esta al 2X de tamaño deseado
        setScaleX(0.5);
        setScaleY(0.5);
        
        
        setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouseEvent)
            {
                if (Main.getEstadoTipoBoton() == TiposDeBoton.ELIMINAR) 
                {
                    NodoGrafico nodoGrafico = (NodoGrafico)mouseEvent.getSource();
                    Group group = (Group) nodoGrafico.getParent();                    
                    nodoGrafico.setEliminado(true);
                    updateNodoListener();
                    group.getChildren().remove(nodoGrafico); 
                   
                } 
            }
        });
        
        setOnMousePressed(new EventHandler<MouseEvent>() {
            
            public void handle(MouseEvent mouseEvent) 
            {
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
                if (Main.getEstadoTipoBoton() == TiposDeBoton.PUNTERO) 
                {
                    setLayoutX(getLayoutX()+mouseEvent.getX()-CENTRO_IMAGEN_NODO_GRAFICO);
                    setLayoutY(getLayoutY()+mouseEvent.getY()-CENTRO_IMAGEN_NODO_GRAFICO);

                    updateNodoListener();
                } 
                else if (Main.getEstadoTipoBoton() == TiposDeBoton.ENLACE)
                {
                    enlaceComodin.setEndX(getLayoutX()+ (mouseEvent.getX()));
                    enlaceComodin.setEndY(getLayoutY()+ (mouseEvent.getY()));
                }
            }
        });

        setOnMouseReleased(new EventHandler<MouseEvent>() {
            

            public void handle(MouseEvent mouseEvent) {

                setScaleX(0.5);
                setScaleY(0.5);
                
                if(Main.getEstadoTipoBoton() == TiposDeBoton.ENLACE) {
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
                
                if (tipoDeBotonSeleccionado == TiposDeBoton.ENLACE){
                    setCursor(tipoDeBotonSeleccionado.getImagenSobreObjetoCursor());
                }else{
                    setCursor(tipoDeBotonSeleccionado.getImagenCursor());
                }
                
                if (tipoDeBotonSeleccionado == TiposDeBoton.ENLACE
                        && nodoAComodin != null
                        && nodoAComodin != nodoGrafico) {
                    
                    Group group = (Group) nodoGrafico.getParent();
                    group.getChildren().remove(enlaceComodin);

                    EnlaceGrafico enlaceGrafico = new EnlaceGrafico(group,nodoAComodin, nodoGrafico);
                    enlaceGrafico.addArcosInicialAlGrupo(); 
                   
                    nodoAComodin.toFront();
                    nodoGrafico.toFront();
                    
                }else if(tipoDeBotonSeleccionado == TiposDeBoton.ELIMINAR){
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