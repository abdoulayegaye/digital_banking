package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AccueilController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private void redirectToPage(ActionEvent event, String fxmlPath) throws IOException {
        try {
            // Charge le fichier FXML spécifié
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            // Récupère la fenêtre (Stage) à partir de l'événement
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Crée une nouvelle scène avec le contenu chargé
            scene = new Scene(root);
            // Applique la nouvelle scène à la fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page : " + fxmlPath);
            e.printStackTrace();
        }
    }

    @FXML
    private void redirectToGestionClients(ActionEvent event) throws IOException {

        redirectToPage(event, "/fxml/clients.fxml");

    }

    @FXML
    private void redirectToGestionComptes(ActionEvent event) throws IOException {
        redirectToPage(event, "/fxml/comptes.fxml");
    }

    @FXML
    private void redirectToGestionOperations(ActionEvent event) throws IOException {
        redirectToPage(event, "/fxml/operations.fxml");
    }
}