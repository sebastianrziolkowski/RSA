package main;

import javafx.application.Application;
import javafx.stage.Stage;

import main.Interface.RSA_interface;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        RSA_interface rsa = new RSA_interface();
        primaryStage = rsa.mainStage;
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
