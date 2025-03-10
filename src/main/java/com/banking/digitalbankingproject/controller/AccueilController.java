package com.banking.digitalbankingproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.banking.digitalbankingproject.tools.Outils;
import java.io.IOException;


public class AccueilController {
    @FXML
    void goToClients(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Clients", "/fxml/clients.fxml");
    }
    @FXML
    void goToComptes(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Comptes", "/fxml/comptes.fxml");
    }
    @FXML
    void goToOperations(ActionEvent event) throws IOException {
        Outils.load(event, "Opérations Bancaires", "/fxml/operations.fxml");
    }
    @FXML
    void logout(ActionEvent event) throws IOException {
        Outils.load(event, "Connexion", "/fxml/login.fxml");
    }
}
