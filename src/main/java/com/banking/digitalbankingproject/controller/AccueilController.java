package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;

import java.io.IOException;

public class AccueilController {

    @FXML
    private Button btnClients; // Correspond à fx:id="btnClients" dans le FXML
    @FXML
    private Button btnComptes; // Correspond à fx:id="btnComptes" dans le FXML
    @FXML
    private Button btnOperations; // Correspond à fx:id="btnOperations" dans le FXML
    @FXML
    private Button btnLogout; // Correspond à fx:id="btnLogout" dans le FXML

    @FXML
    private void initialize() {
        // Initialisation facultative
    }

    @FXML
    private void allerGestionClients(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Clients", "/fxml/clients.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerGestionComptes(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Comptes", "/fxml/gestionComptes.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerGestionOperations(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Opérations", "/fxml/gestionOperations.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deconnexion(ActionEvent event) {
        try {
            Outils.load(event, "Connexion", "/fxml/login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}