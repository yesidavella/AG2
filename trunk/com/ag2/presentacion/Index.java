package com.ag2.presentacion;

import javafx.application.Application;
import javafx.stage.Stage;

public class Index extends Application{
    
    @Override
    public void start(Stage stgEscenario) throws Exception {

        stgEscenario.setTitle("Modelo AG2- Simulador Grafico");
        
        stgEscenario.setScene(IGU.getInstancia());
        stgEscenario.show();
    }
    
    public static void main(String[] args){
        Application.launch(args);
    }

}
