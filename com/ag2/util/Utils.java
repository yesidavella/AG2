package com.ag2.util;

import Grid.Entity;
import com.ag2.controller.MatchCoupleObjectContainer;
import com.ag2.presentation.design.GraphNode;
import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    public static String ABS_PATH_CONFIG_AG2 = ""; //URLUtil.fileToURL(new File("resources/")).toString().replaceFirst("file:/","");
    public static String ABS_PATH_IMGS = "";// URLUtil.fileToURL(new File("resources/images/")).toString();
    public static String ABS_PATH_CSS = "";//URLUtil.fileToURL(new File("resources/css/")).toString();

   protected static HashMap<GraphNode, Entity> nodeMatchCoupleObjectContainer = MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer();
    static {
        try {
            ABS_PATH_CONFIG_AG2 = new File("resources/").toURL().toString().replaceFirst("file:", "");
            ABS_PATH_IMGS = new File("resources/images/").toURL().toString();
            ABS_PATH_CSS = new File("resources/css/").toURL().toString();

        } catch (MalformedURLException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
       public static String findGraphicalName(String originalName) {
        String name = originalName;
        for (GraphNode graphNode : nodeMatchCoupleObjectContainer.keySet()) {
            if (graphNode.getOriginalName().equalsIgnoreCase(originalName)) {
                name = graphNode.getName();
            }
        }
        return name;

    }

}
