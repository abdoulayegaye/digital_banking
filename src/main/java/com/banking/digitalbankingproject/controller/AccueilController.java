package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;

public class AccueilController {
    @FXML
    private Button btnClients;
    @FXML
    private Button btnComptes;
    @FXML
    private Button btnOperations;

    @FXML
    public void initialize() {
        btnClients.setOnAction(event -> openWindow("/fxml/clients.fxml", "Gestion des Clients"));
        btnComptes.setOnAction(event -> openWindow("/fxml/comptes.fxml", "Gestion des Comptes"));
        btnOperations.setOnAction(event -> openWindow("/fxml/operations.fxml", "Opérations Bancaires"));
    }

    private void openWindow(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible d'ouvrir la fenêtre " + title);
            alert.showAndWait();
        }
    }
}
