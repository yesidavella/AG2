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

    public void setGrupoDeDiseño(final Group group) {

        setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouEvent) {
                Boton botonOrigen = (Boton) mouEvent.getSource();

                if (botonOrigen.isSelected()) {

                    IGU.setEstadoTipoBoton(botonOrigen.getTipoDeBoton());


                    if (botonOrigen.tipoDeBoton == TiposDeBoton.PUNTERO) {
                        group.setCursor(Cursor.DEFAULT);
                    } else {
                        if (tipoDeBoton.getImagenCursor() != null) {
                            group.setCursor(tipoDeBoton.getImagenCursor());
                        } else {
                            group.setCursor(Cursor.DEFAULT);
                        }
                    }
                } else {
                    botonOrigen.setSelected(true);
                }
            }
        });
    }
}