package com.ag2.presentacion.diseño;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PuertoGrafico extends Group{
    
    private Image imgImagen;
    private ImageView imvVisorImagen;
    private double posicionX;
    private double posicionY;
    private int angulo = 0;
    
    public PuertoGrafico(double posicionX,double posicionY, int angulo){
        imgImagen = new Image(getClass().getResourceAsStream("../../../../recursos/imagenes/puerto.png")); 
        imvVisorImagen = new ImageView(imgImagen);
        
        this.posicionX = posicionX;
        setLayoutX(posicionX);
        
        this.posicionY = posicionY;
        setLayoutY(posicionY);
        
        this.angulo = angulo;
        setRotate(angulo);
        
        getChildren().add(imvVisorImagen);
    }

    public int getAngulo() {
        return angulo;
    }

    public void setAngulo(short angulo) {
        this.angulo = angulo;
        setRotate(angulo);
    }

    public double getPosicionX() {
        return posicionX;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
        setLayoutX(posicionX);
    }

    public double getPosicionY() {
        return posicionY;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
        setLayoutY(posicionY);
    }
    
}
