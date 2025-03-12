package com.banking.digitalbankingproject;

import com.banking.digitalbankingproject.tools.Notification;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;

/**
 * Classe principale de l'application Digital Banking
 */
public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        try {
            primaryStage = stage;
            
            // Charger la vue de connexion
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            
            // Ajouter la feuille de style
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            
            // Configurer la fenêtre principale
            stage.setTitle("Digital Banking");
            
            // Gestion sécurisée de l'icône
            try {
                InputStream iconStream = App.class.getResourceAsStream("/images/icon.png");
                if (iconStream != null) {
                    stage.getIcons().add(new Image(iconStream));
                }
            } catch (Exception e) {
                System.out.println("Impossible de charger l'icône de l'application");
            }
            
            stage.setScene(scene);
            stage.setResizable(false);
            
            // Gérer la fermeture de l'application
            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });
            
            // Afficher la fenêtre
            stage.show();
            
            // Afficher un message de bienvenue
            Notification.NotifInfo("Bienvenue", "Bienvenue dans Digital Banking");
            
        } catch (IOException e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Impossible de démarrer l'application");
        }
    }

    /**
     * Retourne la fenêtre principale de l'application
     * 
     * @return Stage principal
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Point d'entrée de l'application
     * 
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        try {
            // Vérifier la version de Java
            String version = System.getProperty("java.version");
            if (!version.startsWith("17")) {
                System.err.println("Cette application nécessite Java 17");
                System.exit(1);
            }
            
            // Lancer l'application JavaFX
            launch(args);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Redémarre l'application
     */
    public static void restart() {
        Platform.runLater(() -> {
            try {
                // Fermer la fenêtre actuelle
                primaryStage.close();
                
                // Créer une nouvelle instance
                new App().start(new Stage());
                
            } catch (Exception e) {
                Notification.NotifError("Erreur", "Impossible de redémarrer l'application");
                e.printStackTrace();
            }
        });
    }
}