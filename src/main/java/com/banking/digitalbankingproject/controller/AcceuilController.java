package com.banking.digitalbankingproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AcceuilController {

    @FXML
    private Button btnGestionClients;
    @FXML
    private Button btnGestionComptes;
    @FXML
    private Button btnGestionOperations;
    @FXML
    private Button btnHistoriqueReleves;
    @FXML
    private Button btnParametres;
    @FXML
    private Button btnDeconnexion;

    @FXML
    private void handleGestionClients(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/gestionClients.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnGestionClients.getScene().getWindow();
            stage.setTitle("Gestion des Clients");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation", 
                    "Impossible d'ouvrir la page de gestion des clients: " + e.getMessage());
        }
    }

    @FXML
    private void handleGestionComptes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/gestionComptes.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnGestionComptes.getScene().getWindow();
            stage.setTitle("Gestion des Comptes");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation", 
                    "Impossible d'ouvrir la page de gestion des comptes: " + e.getMessage());
        }
    }

    @FXML
    private void handleGestionOperations(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/gestionOperations.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnGestionOperations.getScene().getWindow();
            stage.setTitle("Gestion des Opérations");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation", 
                    "Impossible d'ouvrir la page de gestion des opérations: " + e.getMessage());
        }
    }

    @FXML
    private void handleHistoriqueReleves(ActionEvent event) {
        try {
            // Rediriger vers l'onglet historique des opérations
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/gestionOperations.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnHistoriqueReleves.getScene().getWindow();
            stage.setTitle("Historique et Relevés");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation", 
                    "Impossible d'ouvrir la page d'historique et relevés: " + e.getMessage());
        }
    }

    @FXML
    private void handleParametres(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Paramètres", "Fonctionnalité à venir", 
                "La page des paramètres sera disponible dans une prochaine version.");
    }

    @FXML
    private void handleDeconnexion(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnDeconnexion.getScene().getWindow();
            stage.setTitle("Connexion");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de déconnexion", 
                    "Impossible de se déconnecter: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}