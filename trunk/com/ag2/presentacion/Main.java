package com.ag2.presentacion;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
    
    private static Stage stgEscenario;

    public static Stage getStgEscenario() {
        return stgEscenario;
    }
    
    @Override
    public void start(Stage stgEscenario) throws Exception {

        stgEscenario.setTitle("Modelo AG2- Simulador Grafico");
        this.stgEscenario=stgEscenario;//FIXME: Esto es una mierdaaaaaa
        stgEscenario.setScene(IGU.getInstancia());
        stgEscenario.show();
    }
    
    public static void main(String[] args){
        Application.launch(args);
    }

}
