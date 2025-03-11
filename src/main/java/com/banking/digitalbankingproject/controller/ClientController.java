package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;

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
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page d'ajout de client.");
        }
    }

    private void allerModifierClient(ActionEvent event) {
        try {
            Outils.load(event, "Modifier un Client", "/fxml/modifierClient.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page de modification de client.");
        }
    }

    private void allerSupprimerClient(ActionEvent event) {
        try {
            Outils.load(event, "Supprimer un Client", "/fxml/supprimerClient.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page de suppression de client.");
        }
    }

    private void retourAccueil(ActionEvent event) {
        try {
            Outils.load(event, "Accueil", "/fxml/accueil.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de retourner à la page d'accueil.");
        }
    }
}
