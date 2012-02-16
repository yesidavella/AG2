package com.ag2.presentacion.controles;

import com.ag2.presentacion.IGU;
import com.ag2.presentacion.ActionTypeEmun;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ToggleButtonAg2 extends ToggleButton {

    private final static double WIDTH = 36;
    private final static double HEIGHT = 34;
    private ActionTypeEmun actionTypeEmun;

    public ToggleButtonAg2(ActionTypeEmun actionTypeEmun) {

        this.actionTypeEmun = actionTypeEmun;

        ImageView imageView = new ImageView(actionTypeEmun.getImagenBoton());
        setGraphic(imageView);
        setMaxWidth(WIDTH);
        setMaxHeight(HEIGHT);

    }

    public ActionTypeEmun getActionTypeEmun() {
        return actionTypeEmun;
    }

    public void setActionTypeEmun(ActionTypeEmun actionTypeEmun) {
        this.actionTypeEmun = actionTypeEmun;
    }

    public void setGraphDesignGroup(final Group group) {

        setOnMouseClicked(new EventHandler<MouseEvent>() {

            public void handle(MouseEvent mouEvent) {
                ToggleButtonAg2 toggleButtonAg2 = (ToggleButtonAg2) mouEvent.getSource();

                if (toggleButtonAg2.isSelected()) {

                    IGU.setEstadoTipoBoton(toggleButtonAg2.getActionTypeEmun());


                    if (toggleButtonAg2.actionTypeEmun == ActionTypeEmun.PUNTERO) {
                        group.setCursor(Cursor.DEFAULT);
                    } else {
                        if (actionTypeEmun.getImagenCursor() != null) {
                            group.setCursor(actionTypeEmun.getImagenCursor());
                        } else {
                            group.setCursor(Cursor.DEFAULT);
                        }
                    }
                } else {
                    toggleButtonAg2.setSelected(true);
                }
            }
        });
    }
}