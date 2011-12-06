package com.ag2.presentacion.controles;

import com.ag2.presentacion.IGU;
import com.ag2.presentacion.TiposDeBoton;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Boton extends ToggleButton {

    private final static double ANCHO = 36;
    private final static double ALTO = 34;
    private TiposDeBoton tipoDeBoton;

    public Boton(TiposDeBoton tipoDeBoton) {

        this.tipoDeBoton = tipoDeBoton;

        ImageView visorDeImagen = new ImageView(tipoDeBoton.getImagenBoton());
        setGraphic(visorDeImagen);
        setMaxWidth(ANCHO);
        setMaxHeight(ALTO);
        
    }

    public TiposDeBoton getTipoDeBoton() {
        return tipoDeBoton;
    }

    public void setTipoDeBoton(TiposDeBoton tipoDeBoton) {
        this.tipoDeBoton = tipoDeBoton;
    }
    
    public void setGropoDeDiseño(final Group grGrupoDeDiseño) {

        setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouEvent) {
                Boton botonOrigen = (Boton)mouEvent.getSource();

                if (botonOrigen.isSelected()){
                    
                    IGU.setEstadoTipoBoton(botonOrigen.getTipoDeBoton());
                    
                    switch (botonOrigen.tipoDeBoton) {

                        case PUNTERO:
                            grGrupoDeDiseño.setCursor(Cursor.DEFAULT);
                            break;
                        case MANO:
                            grGrupoDeDiseño.setCursor(tipoDeBoton.getImagenCursor());
                            break;
                        case ADICIONAR_VERTICE:
                            grGrupoDeDiseño.setCursor(tipoDeBoton.getImagenCursor());
                            break;
                        case ELIMINAR:
                            grGrupoDeDiseño.setCursor(tipoDeBoton.getImagenCursor());
                            break;
                            
                        case CLIENTE:
                            grGrupoDeDiseño.setCursor(tipoDeBoton.getImagenCursor());
                            break;
                        case NODO_DE_SERVICIO:
                            grGrupoDeDiseño.setCursor(tipoDeBoton.getImagenCursor());
                            break;
                        case ENRUTADOR_HIBRIDO:
                            grGrupoDeDiseño.setCursor(tipoDeBoton.getImagenCursor());
                            break;
                        case ENRUTADOR_OPTICO:
                            grGrupoDeDiseño.setCursor(tipoDeBoton.getImagenCursor());
                            break;
                        case ENRUTADOR_RAFAGA:
                            grGrupoDeDiseño.setCursor(tipoDeBoton.getImagenCursor());
                            break;
                        case ENLACE:
                            grGrupoDeDiseño.setCursor(tipoDeBoton.getImagenCursor());
                            break;
                        case RECURSO:
                            grGrupoDeDiseño.setCursor(tipoDeBoton.getImagenCursor());
                            break;
                    }
                } else {
                    grGrupoDeDiseño.setCursor(Cursor.DEFAULT);
                }
            }
        });
    }
}