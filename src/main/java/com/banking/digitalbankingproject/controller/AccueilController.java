package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import com.banking.digitalbankingproject.util.Session;
import javafx.scene.control.Alert;

import java.io.IOException;

public class AccueilController {

    @FXML
    private BorderPane mainContainer;

    @FXML
    public void initialize() {
        // Code d'initialisation
    }

    @FXML
    public void naviguerVersClients() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/fxml/client.fxml"));
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de navigation");
            alert.setContentText("Impossible d'accéder à la page des clients: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void naviguerVersComptes() {
        // Temporairement désactivé
        System.out.println("Navigation vers Comptes - Fonctionnalité en développement");
        // Afficher un message à l'utilisateur
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Fonctionnalité en développement");
        alert.setContentText("La gestion des comptes sera disponible dans une prochaine version.");
        alert.showAndWait();
    }

    @FXML
    public void naviguerVersOperations() {
        // Temporairement désactivé
        System.out.println("Navigation vers Opérations - Fonctionnalité en développement");
        // Afficher un message à l'utilisateur
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Fonctionnalité en développement");
        alert.setContentText("La gestion des opérations sera disponible dans une prochaine version.");
        alert.showAndWait();
    }

    @FXML
    public void naviguerVersRapports() {
        // Temporairement désactivé
        System.out.println("Navigation vers Rapports - Fonctionnalité en développement");
        // Afficher un message à l'utilisateur
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Fonctionnalité en développement");
        alert.setContentText("Les rapports seront disponibles dans une prochaine version.");
        alert.showAndWait();
    }

    @FXML
    public void naviguerVersAccueil() {
        // Rafraîchir la page actuelle
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/fxml/accueil.fxml"));
            Scene scene = new Scene(view);
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour la déconnexion utilisée dans le FXML actuel
     */
    @FXML
    public void deconnexion() {
        try {
            // Effacer les données de session
            Session.clear();
            
            // Rediriger vers la page de login
            Parent view = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(view);
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            
            System.out.println("Déconnexion réussie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Méthode alternative pour la déconnexion (pour compatibilité)
     */
    @FXML
    public void seDeconnecter() {
        deconnexion();
    }
}
