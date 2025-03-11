package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;

public class CompteController {

    @FXML
    private Button btnAjouterCompte;
    @FXML
    private Button btnModifierCompte;
    @FXML
    private Button btnSupprimerCompte;
    @FXML
    private Button btnRetour;

    @FXML
    private void initialize() {
        btnAjouterCompte.setOnAction(event -> allerAjouterCompte(event));
        btnModifierCompte.setOnAction(event -> allerModifierCompte(event));
        btnSupprimerCompte.setOnAction(event -> allerSupprimerCompte(event));
        btnRetour.setOnAction(event -> retourAccueil(event));
    }

    private void allerAjouterCompte(ActionEvent event) {
        try {
            Outils.load(event, "Ajouter un Compte", "/fxml/ajouterCompte.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page d'ajout de compte.");
        }
    }

    private void allerModifierCompte(ActionEvent event) {
        try {
            Outils.load(event, "Modifier un Compte", "/fxml/modifierCompte.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page de modification de compte.");
        }
    }

    private void allerSupprimerCompte(ActionEvent event) {
        try {
            Outils.load(event, "Supprimer un Compte", "/fxml/supprimerCompte.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page de suppression de compte.");
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
