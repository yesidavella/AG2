package edu.ag2.presentation;

import edu.ag2.presentation.images.ImageHelper;
import edu.ag2.util.Utils;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

    public enum ActionTypeEmun {
        RUN("ejecutar"),
        STOP("parar"),

        POINTER("puntero",null,0,0,"puntero_sobre_enlace",1,21),
        HAND("mano","mano_abierta_cursor",0,0,"mano_cerrada_cursor",0,0),
        ADD_LINK_SEPARATOR("adicionar_vertice","adicionar_vertice_cursor",3,3,"adicionar_vertice_sobre_elemento_cursor",3,5),
        DELETED("eliminar","eliminar_cursor",0,0,"eliminar_sobre_elemento_cursor",0,0),
        ZOOM_PLUS("lupa_mas","lupa_mas_cursor",10,10),
        ZOOM_MINUS("lupa_menos","lupa_menos_cursor",10,10),

        CLIENT("cliente","cliente_cursor",14,14),
        PCE_SWITCH("PCE","PCE_cursor",10,13),
        //OBS_SWITCH("enrutador_rafaga","enrutador_rafaga_cursor",10,13),
        HRYDRID_SWITCH("enrutador","enrutador_cursor",10,13),
        BROKER("nodo_servicio","nodo_servicio_cursor",15,16),
        RESOURCE("recurso","recurso_cursor",13,16),
        LINK("fiber_link","fiber_cursor",3,4,"fiber_add_cursor",3,8),
        OCS_CIRCUIT("OCS_link","OCS_cursor",2,2,"OCS_add_cursor",2,7);

    private Image buttonImage;
    private ImageCursor cursorImage;
    private ImageCursor overCursorImage;
    private Point2D point2D; 

        private ActionTypeEmun(String imageName) {
            point2D = new Point2D(0,0);
            buttonImage = new Image(ImageHelper.getResourceInputStream( imageName + ".png"));
        }

        private ActionTypeEmun(String buttonImageName, String cursorImageName, double posImageX, double posImageY) {

            buttonImage = new Image(ImageHelper.getResourceInputStream( buttonImageName + ".png"));

            if (cursorImageName != null) {
                Image image = new Image(ImageHelper.getResourceInputStream( cursorImageName + ".png"));
                point2D = new Point2D(posImageX, posImageY);
                cursorImage = new ImageCursor(image,posImageX,posImageY);
            }
        }

        private ActionTypeEmun(String buttonImageName, String cursorImageName,double posImageX,double posImageY,String overCursorImageName,double posImage2X,double posImage2Y) {

            buttonImage = new Image(ImageHelper.getResourceInputStream( buttonImageName + ".png"));

            if (cursorImageName != null) {
                Image image = new Image(ImageHelper.getResourceInputStream(cursorImageName + ".png"));
                point2D = new Point2D(posImageX, posImageY);
                cursorImage = new ImageCursor(image,posImageX,posImageY);
            }

            if (overCursorImageName != null) {
                Image image = new Image(ImageHelper.getResourceInputStream( overCursorImageName + ".png"));
                overCursorImage = new ImageCursor(image,posImage2X,posImage2Y);
            }
        }

        public Image getButtonImage() {
            return buttonImage;
        }

        public ImageCursor getCursorImage() {
            return cursorImage;
        }

        public ImageCursor getOverCursorImage() {
            return overCursorImage;
        }

        public Point2D getPoint2D() {
            return point2D;
        }
    }