package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ClientController {

    @FXML
    private Button btnAjouterClient;
    @FXML
    private Button btnModifierClient;
    @FXML
    private Button btnSupprimerClient;
    @FXML
    private Button btnRetour;

    @FXML
    private void initialize() {
        btnAjouterClient.setOnAction(event -> allerAjouterClient(event));
        btnModifierClient.setOnAction(event -> allerModifierClient(event));
        btnSupprimerClient.setOnAction(event -> allerSupprimerClient(event));
        btnRetour.setOnAction(event -> retourAccueil(event));
    }

    private void allerAjouterClient(ActionEvent event) {
        try {
            Outils.load(event, "Ajouter un Client", "/fxml/ajouterClient.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void allerModifierClient(ActionEvent event) {
        try {
            Outils.load(event, "Modifier un Client", "/fxml/modifierClient.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void allerSupprimerClient(ActionEvent event) {
        try {
            Outils.load(event, "Supprimer un Client", "/fxml/supprimerClient.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void retourAccueil(ActionEvent event) {
        try {
            Outils.load(event, "Accueil", "/fxml/accueil.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
