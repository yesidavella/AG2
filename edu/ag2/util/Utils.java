package edu.ag2.util;

import edu.ag2.controller.MatchCoupleObjectContainer;
import edu.ag2.presentation.design.GraphNode;
import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    public static String ABS_PATH_CONFIG_AG2 = ""; //URLUtil.fileToURL(new File("resources/")).toString().replaceFirst("file:/","");
    public static String ABS_PATH_IMGS = "";// URLUtil.fileToURL(new File("resources/images/")).toString();
    public static String ABS_PATH_CSS = "";//URLUtil.fileToURL(new File("resources/css/")).toString();

    static {
        try {
            ABS_PATH_CONFIG_AG2 = new File("configFiles/").toURL().toString().replaceFirst("file:", "");
            ABS_PATH_IMGS = new File("sources/edu/ag2/presentation/images/").toURL().toString();
            ABS_PATH_CSS = new File("sources/edu/ag2/presentation/").toURL().toString();

        } catch (MalformedURLException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String findGraphicalName(String originalName) {
        String name = originalName;
        for (GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {
            if (graphNode.getOriginalName().equalsIgnoreCase(originalName)) {
                name = graphNode.getName();
            }
        }
        return name;
    }

    
    public static GraphNode findNodeGraphByOriginalName(String originalName) {
       
        for (GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) {
            if (graphNode.getOriginalName().equalsIgnoreCase(originalName)) {
                return graphNode;
            }
        }
        return null;
    }
//    public static GraphNode findGraphicalNode(Entity entity) {
//     
//        for (GraphNode graphNode : MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().keySet()) 
//        {
//            
//           if(entity == MatchCoupleObjectContainer.getInstanceNodeMatchCoupleObjectContainer().get(graphNode))
//           {
//               return graphNode;
//           }
//           
//        }
//        return null;
//    }
}
