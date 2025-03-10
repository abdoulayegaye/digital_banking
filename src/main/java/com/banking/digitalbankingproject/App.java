package com.banking.digitalbankingproject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;


public class App extends Application {

    private static Stage primaryStage;
    private static final String APP_NAME = "Digital Banking";
    private static final String APP_VERSION = "1.0.0";
    private static final int MIN_WIDTH = 800;
    private static final int MIN_HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        initializeMainWindow();
    }


    private void initializeMainWindow() throws IOException {
        // Charge la vue de connexion
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        
        primaryStage.setTitle(APP_NAME + " - Connexion");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.centerOnScreen();
        
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }


    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static String getAppName() {
        return APP_NAME;
    }


    public static String getAppVersion() {
        return APP_VERSION;
    }
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("Erreur lors du démarrage de l'application : " + e.getMessage());
            Platform.exit();
            System.exit(1);
        }
    }
}