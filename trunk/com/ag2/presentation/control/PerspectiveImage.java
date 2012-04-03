/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ag2.presentation.control;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Parent;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.effect.ReflectionBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public  class PerspectiveImage extends Parent {
        public static final double REFLECTION_SIZE = 0.25;
        public static final double WIDTH = 80;
        public static final double HEIGHT = WIDTH + (WIDTH*REFLECTION_SIZE);
        public static final double RADIUS_H = WIDTH / 2;
        public static final double BACK = WIDTH / 10;
        public PerspectiveTransform transform = new PerspectiveTransform();
        /** Angle Property */
        public final DoubleProperty angle = new SimpleDoubleProperty(45) {
            @Override protected void invalidated() {
                // when angle changes calculate new transform
                double lx = (RADIUS_H - Math.sin(Math.toRadians(angle.get())) * RADIUS_H - 1);
                double rx = (RADIUS_H + Math.sin(Math.toRadians(angle.get())) * RADIUS_H + 1);
                double uly = (-Math.cos(Math.toRadians(angle.get())) * BACK);
                double ury = -uly;
                transform.setUlx(lx);
                transform.setUly(uly);
                transform.setUrx(rx);
                transform.setUry(ury);
                transform.setLrx(rx);
                transform.setLry(HEIGHT + uly);
                transform.setLlx(lx);
                transform.setLly(HEIGHT + ury);
            }
        };
        public final double getAngle() { return angle.getValue(); }
        public final void setAngle(double value) { angle.setValue(value); }
        public final DoubleProperty angleModel() { return angle; }

        public PerspectiveImage(Image image) {
            ImageView imageView = new ImageView(image);
            imageView.setEffect(ReflectionBuilder.create().fraction(REFLECTION_SIZE).build());
            setEffect(transform);
            getChildren().addAll(imageView);
        }
    }