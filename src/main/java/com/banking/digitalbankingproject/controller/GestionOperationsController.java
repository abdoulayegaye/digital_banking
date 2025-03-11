package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.lang.Exception;

public class GestionOperationsController {
    @FXML private Button btnDepot;
    @FXML private Button btnRetrait;
    @FXML private Button btnVirement;
    @FXML private Button btnHistorique;
    @FXML private Button btnRetour;

    @FXML
    private void allerDepot(ActionEvent event) {
        try {
            Outils.load(event, "Dépôt", "/fxml/depot.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page Dépôt.");
        }
    }

    @FXML
    private void allerRetrait(ActionEvent event) {
        try {
            Outils.load(event, "Retrait", "/fxml/retrait.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page Retrait.");
        }
    }

    @FXML
    private void allerVirement(ActionEvent event) {
        try {
            Outils.load(event, "Virement", "/fxml/virement.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page Virement.");
        }
    }

    @FXML
    private void allerHistorique(ActionEvent event) {
        try {
            Outils.load(event, "Historique des Transactions", "/fxml/historique.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la page Historique.");
        }
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            Outils.load(event, "Accueil", "/fxml/accueil.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de retourner à la page Accueil.");
        }
    }
}
