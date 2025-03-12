package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;

import java.io.IOException;

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

    }

    @FXML
    private void allerGestionClients(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Clients", "/fxml/gestionClients.fxml");
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

    public void onclick(ActionEvent actionEvent) {

    }
}