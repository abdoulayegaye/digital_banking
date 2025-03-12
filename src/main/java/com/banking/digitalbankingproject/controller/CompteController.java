package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;

import java.io.IOException; // Ajout de l'importation manquante

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void allerModifierCompte(ActionEvent event) {
        try {
            Outils.load(event, "Modifier un Compte", "/fxml/modifierCompte.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void allerSupprimerCompte(ActionEvent event) {
        try {
            Outils.load(event, "Supprimer un Compte", "/fxml/supprimerCompte.fxml");
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
