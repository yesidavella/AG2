
package com.ag2.presentation.design.property;

import com.ag2.presentation.Main;
import com.ag2.presentation.design.GraphArc;
import com.ag2.presentation.design.GraphNode;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;

public class NodeRelationProperty extends EntityProperty implements Serializable{

    private transient ObservableList nodesObservableList = FXCollections.observableArrayList();
    private String name;

    public NodeRelationProperty(String id, String name) {
        super(id, name, PropertyType.TEXT_LIST, false);
        ChoiceBox  choiceBox =  ((ChoiceBox) control);
        choiceBox.setItems(nodesObservableList);
    }

    public String toString() {
        return name.toString();
    }

    public ObservableList getObservableListNodes()
    {
        return nodesObservableList;
    }

    public void setFirstValue(GraphNode graphNode){
         ((ChoiceBox) control).getSelectionModel().select(graphNode);
    }
    
     private void writeObject(ObjectOutputStream stream) {
        try {
            stream.defaultWriteObject();
            Main.countObject++;
            //System.out.println("Writing: " + Main.countObject + "  " + this.getClass().getCanonicalName());
        } catch (IOException ex) {
            Logger.getLogger(GraphArc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
