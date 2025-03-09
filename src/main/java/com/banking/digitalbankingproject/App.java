package com.banking.digitalbankingproject;

import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.service.impl.UserImpl;
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
        stage.setTitle("Connexion");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Vérifie si l'utilisateur 'admin' existe déjà avant de l'ajouter
        IUser userService = new UserImpl();
        User existingUser = userService.getUserByUsername("admin");

        if (existingUser == null) {
            // Crée un nouvel utilisateur 'admin' avec un mot de passe haché
            User user = new User();
            user.setUsername("admin");
            user.setPassword(Utils.hashPassword("admin")); // Hache le mot de passe
            boolean ok = userService.createUser(user);

            if (ok) {
                System.out.println("Utilisateur 'admin' créé avec succès.");
            } else {
                System.out.println("Échec de la création de l'utilisateur 'admin'.");
            }
        } else {
            System.out.println("L'utilisateur 'admin' existe déjà.");
        }

        // Lancer l'application JavaFX
        launch(args);
    }
}