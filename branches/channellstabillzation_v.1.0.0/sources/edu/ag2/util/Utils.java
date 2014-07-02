package edu.ag2.util;

import edu.ag2.controller.MatchCoupleObjectContainer;
import edu.ag2.presentation.design.GraphNode;

public class Utils {

   
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
