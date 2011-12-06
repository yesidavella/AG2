package com.ag2.presentacion;

import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;

    public enum TiposDeBoton {
        EJECUTAR("ejecutar"),
        PARAR("parar"),
        
        PUNTERO("puntero"),
        MANO("mano","mano_abierta_cursor",0,0,"mano_cerrada_cursor",0,0),
        ADICIONAR_VERTICE("adicionar_vertice","adicionar_vertice_cursor",3,3,"adicionar_vertice_sobre_elemento_cursor",3,5),
        ELIMINAR("eliminar","eliminar_cursor",0,0,"eliminar_sobre_elemento_cursor",0,0),
        
        CLIENTE("cliente","cliente_cursor",14,14),
        ENRUTADOR_OPTICO("enrutador_optico","enrutador_optico_cursor",10,13),
        ENRUTADOR_RAFAGA("enrutador_rafaga","enrutador_rafaga_cursor",10,13),
        ENRUTADOR_HIBRIDO("enrutador_hibrido","enrutador_hibrido_cursor",10,13),
        NODO_DE_SERVICIO("nodo_servicio","nodo_servicio_cursor",15,16),
        RECURSO("recurso","recurso_cursor",13,16),
        ENLACE("enlace","enlace_cursor",2,2,"enlace_adicionar_cursor",2,7);
    
    private Image imagenBoton;
    private ImageCursor imagenCursor;
    private ImageCursor imagenSobreObjetoCursor;
    private Point2D posicionImagenDeCursorEnXyY;
    
        private TiposDeBoton(String nombreImagenBoton)
        {
            posicionImagenDeCursorEnXyY = new Point2D(0,0);
            imagenBoton = new Image(getClass().getResourceAsStream("../../../recursos/imagenes/" + nombreImagenBoton + ".png"));
        }

        private TiposDeBoton(String nombreImagenBoton, String nombreImagenCursor, double posicionImgX, double posicionImgY) {
            
            imagenBoton = new Image(getClass().getResourceAsStream("../../../recursos/imagenes/" + nombreImagenBoton + ".png"));

            if (nombreImagenCursor != null) {
                Image imgCursor = new Image(getClass().getResourceAsStream("../../../recursos/imagenes/" + nombreImagenCursor + ".png"));
                posicionImagenDeCursorEnXyY = new Point2D(posicionImgY, posicionImgY);
                imagenCursor = new ImageCursor(imgCursor,posicionImgX,posicionImgY);
            }
        }
        
        private TiposDeBoton(String nombreImagenBoton, String nombreImagenCursor,double posicionImgX,double posicionImgY,String nombreImagenSobreObjetoCursor,double posicionImg2X,double posicionImg2Y) {
            
            imagenBoton = new Image(getClass().getResourceAsStream("../../../recursos/imagenes/" + nombreImagenBoton + ".png"));

            if (nombreImagenCursor != null) {
                Image imgCursor = new Image(getClass().getResourceAsStream("../../../recursos/imagenes/" + nombreImagenCursor + ".png"));
                posicionImagenDeCursorEnXyY = new Point2D(posicionImgY, posicionImgY);
                imagenCursor = new ImageCursor(imgCursor,posicionImgX,posicionImgY);
            }
            
            if (nombreImagenSobreObjetoCursor != null) {
                Image imgSobreObjetoCursor = new Image(getClass().getResourceAsStream("../../../recursos/imagenes/" + nombreImagenSobreObjetoCursor + ".png"));
                imagenSobreObjetoCursor = new ImageCursor(imgSobreObjetoCursor,posicionImg2X,posicionImg2Y);
            }    
        }
        
        public Image getImagenBoton() {
            return imagenBoton;
        }

        public ImageCursor getImagenCursor() {
            return imagenCursor;
        }
        
        public ImageCursor getImagenSobreObjetoCursor() {
            return imagenSobreObjetoCursor;
        }
       
        public Point2D getPosicionImagenDeCursorEnXyY() {
            return posicionImagenDeCursorEnXyY;
        }

    };