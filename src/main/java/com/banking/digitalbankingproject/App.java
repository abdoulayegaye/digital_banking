package com.banking.digitalbankingproject;

import com.banking.digitalbankingproject.tools.Utils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Page de connexion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
       launch();
        //String password = "123456"; // Remplace par ton mot de passe
        //String hashedPassword = Utils.hashPassword(password);
       // System.out.println("Mot de passe hashé : " + hashedPassword);
    }

}