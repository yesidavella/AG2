
package com.ag2.presentation.design.property;

import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;


public class NodeDistributionProperty extends EntityProperty {

    public enum DistributionType {

        CONSTANT("Constante"),
        ER_LANG("Erlang"),
        HYPER_EXPONENTIAL("Hiper-exponencial"),
        NEGATIVE_EXPONENTIAL("Exponencial-negativa"),
        NORMAL("Normal"),
        POISSON_PROCESS("Possion"),
        UNMIFORM("Uniforme");
        private String name;

        private DistributionType(String name) {
            this.name = name;

        }

        @Override
        public String toString() {
            return name.toString();
        }
    };

    public NodeDistributionProperty(String id, String name) {
        super(id, name, PropertyType.TEXT_LIST, false );
        ((ChoiceBox) control).setItems(FXCollections.observableArrayList(DistributionType.values()));
    }

    public void setFirstValue(DistributionType distributionType) {
        ((ChoiceBox) control).getSelectionModel().select(distributionType);
    }
}
