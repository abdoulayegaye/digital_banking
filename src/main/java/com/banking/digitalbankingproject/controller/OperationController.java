package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OperationController {
    @FXML private Button btnDepot;
    @FXML private Button btnRetrait;
    @FXML private Button btnVirement;
    @FXML private Button btnRetour;

    @FXML
    private void initialize() {
        btnDepot.setOnAction(event -> allerDepot(event));
        btnRetrait.setOnAction(event -> allerRetrait(event));
        btnVirement.setOnAction(event -> allerVirement(event));
        btnRetour.setOnAction(event -> retourAccueil(event));
    }

    private void allerDepot(ActionEvent event) {
        try {
            Outils.load(event, "Dépôt", "/fxml/depot.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page Dépôt.");
        }
    }

    private void allerRetrait(ActionEvent event) {
        try {
            Outils.load(event, "Retrait", "/fxml/retrait.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page Retrait.");
        }
    }

    private void allerVirement(ActionEvent event) {
        try {
            Outils.load(event, "Virement", "/fxml/virement.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page Virement.");
        }
    }

    private void retourAccueil(ActionEvent event) {
        try {
            Outils.load(event, "Accueil", "/fxml/accueil.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de retourner à la page Accueil.");
        }
    }
}
