package com.banking.digitalbankingproject;

import com.banking.digitalbankingproject.database.DbInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class App extends Application {
    
    private static final Logger logger = Logger.getLogger(App.class.getName());

    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Initialisation de la base de données
            boolean dbInitialized = DbInitializer.initializeDatabase();
            
            if (!dbInitialized) {
                // Afficher une alerte en cas d'échec d'initialisation de la base de données
                Alert alert = new Alert(Alert.AlertType.ERROR, 
                        "Impossible d'initialiser la base de données. Veuillez vérifier votre connexion MySQL.", 
                        ButtonType.OK);
                alert.setTitle("Erreur de base de données");
                alert.setHeaderText("Erreur d'initialisation");
                alert.showAndWait();
                
                // Quitter l'application si la base de données n'est pas initialisée
                System.exit(1);
            }
            
            logger.info("Base de données initialisée avec succès");
            
            // Charge le fichier FXML
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));

            // Crée la scène
            Scene scene = new Scene(root);
            
            // Applique le CSS
            String css = getClass().getResource("/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);

            // Configure la fenêtre
            stage.setTitle("Système de Gestion Bancaire - Connexion");
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
        } catch (Exception e) {
            logger.severe("Erreur lors du démarrage de l'application: " + e.getMessage());
            e.printStackTrace();
            
            // Afficher une alerte en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR, 
                    "Une erreur est survenue lors du démarrage de l'application: " + e.getMessage(), 
                    ButtonType.OK);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de démarrage");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        // Lance l'application
        launch(args);
    }
}