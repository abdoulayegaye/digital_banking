package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;

public class AccueilController {

    @FXML
    private Label soldeLabel;
    @FXML
    private Button gestionClients;
    @FXML
    private Button gestionComptes;
    @FXML
    private Button operations;

    @FXML
    protected void goToClients() throws IOException {
        changeScene("/fxml/clients.fxml", "Gestion des Clients");
    }

    @FXML
    protected void goToComptes() throws IOException {
        changeScene("/fxml/comptes.fxml", "Gestion des Comptes");
    }

    @FXML
    protected void goToOperations() throws IOException {
        changeScene("/fxml/operations.fxml", "Opérations Bancaires");
    }

    private void changeScene(String fxml, String title) throws IOException {
        Stage stage = (Stage) soldeLabel.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        stage.setScene(new Scene(root));
        stage.setTitle(title);
    }
}



