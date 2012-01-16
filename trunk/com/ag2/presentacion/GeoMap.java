/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.ag2.presentacion;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;

/**
 *
 * @author Frank
 */
public class GeoMap extends Group {

    Group texts = new Group();    

    public GeoMap() {
        try {

            Color[] colors = new Color[]{Color.GREY};//, Color.RED, Color.ORANGE, Color.VIOLET, Color.CHOCOLATE, Color.YELLOW, Color.AZURE };
            int currentColor = 0;

            File file = new File("src\\maps\\110m_admin_0_countries.shp");
            FileDataStore store = FileDataStoreFinder.getDataStore(file);
            SimpleFeatureSource featureSource = store.getFeatureSource();
            SimpleFeatureCollection c = featureSource.getFeatures();
            SimpleFeatureIterator featuresIterator = c.features();
            Coordinate[] coords;
            Geometry polygon;
            Point centroid;
            Bounds bounds;

            int ii = 0;

            while (featuresIterator.hasNext()) {
                SimpleFeature o = featuresIterator.next();
                String name = (String) o.getAttribute("NAME");
                Object geometry = o.getDefaultGeometry();

                if (geometry instanceof MultiPolygon) {
                    MultiPolygon multiPolygon = (MultiPolygon) geometry;

                    centroid = multiPolygon.getCentroid();
                    final Text text = new Text(name);
                    bounds = text.getBoundsInLocal();
                    text.setFont(new Font(6));
                    text.getTransforms().add(new Translate(centroid.getX(), centroid.getY()));
                    text.getTransforms().add(new Scale(0.1, -0.1));
                    text.getTransforms().add(new Translate(-bounds.getWidth() / 2., bounds.getHeight() / 2.));
                    texts.getChildren().add(text);

                    //                
                    for (int geometryI = 0; geometryI < multiPolygon.getNumGeometries(); geometryI++) {
                        polygon = multiPolygon.getGeometryN(geometryI);

                        coords = polygon.getCoordinates();
                        Path path = new Path();
                        path.setStrokeWidth(0.05);
                        currentColor = (currentColor + 1) % colors.length;
                        path.setFill(colors[currentColor]);
                        path.getElements().add(new MoveTo(coords[0].x, coords[0].y));
                        for (int i = 1; i < coords.length; i++) {
                            path.getElements().add(new LineTo(coords[i].x, coords[i].y));
                        }
                        path.getElements().add(new LineTo(coords[0].x, coords[0].y));
                        getChildren().add(path);
                    }
                }
                ii++;
            }

            setStyle("  -fx-background-color:#B2BEFF;");
            getChildren().add(texts);
        } catch (IOException ex) {
            Logger.getLogger(GeoMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
