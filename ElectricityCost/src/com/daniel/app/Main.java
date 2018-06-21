package com.daniel.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URLDecoder;

public class Main extends Application {

    private String decodePath = null;

    public Main(){
        try {
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            this.decodePath = URLDecoder.decode(path, "UTF-8");
        }catch(Exception e){

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/windowView.fxml"));
        root.setStyle("-fx-background-image: url('/draw.png')");
        primaryStage.setTitle("Electricity cost");
        primaryStage.setScene(new Scene(root, 600, 430));
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }

    public String getPath(){
        return  decodePath;
    }
}
