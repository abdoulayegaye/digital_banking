package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import java.lang.Exception;

public class AccueilController {

    @FXML
    private Button btnClients;
    @FXML
    private Button btnComptes;
    @FXML
    private Button btnOperations;
    @FXML
    private Button btnLogout;

    @FXML
    private void initialize() {
        // Initialisation facultative
    }

    @FXML
    private void allerGestionClients(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Clients", "/fxml/gestionClients.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page Gestion des Clients.");
        }
    }

    @FXML
    private void allerGestionComptes(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Comptes", "/fxml/gestionComptes.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page Gestion des Comptes.");
        }
    }

    @FXML
    private void allerGestionOperations(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Opérations", "/fxml/gestionOperations.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page Gestion des Opérations.");
        }
    }

    @FXML
    private void deconnexion(ActionEvent event) {
        try {
            Outils.load(event, "Connexion", "/fxml/login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page de connexion.");
        }
    }
}
