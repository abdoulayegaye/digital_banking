package com.banking.digitalbankingproject.controller;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AccueilController {

    @FXML
    private Button AccueilTfx;

    @FXML
    private Button Consulter_releveTfx;

    @FXML
    private Button Faire_un_virementTfx;

    @FXML
    private Button Gestion_ClientsTfx;

    @FXML
    private Button Gestion_ComptesFfx;

    @FXML
    private ListView<?> HistoriqueListTfx;

    @FXML
    private Button ProfilTfx;

    @FXML
    private Button ServicesTfx;

    @FXML
    private Button TransactionTfx;

    @FXML
    private Button deconexionTfx;

    @FXML
    private Button depot_argentTfx1;

    @FXML
    private Button retrait_argentTfx;

    @FXML
    void Accueil(ActionEvent event) {
        // Votre code ici
    }

    @FXML
    void Consulter_releve(ActionEvent event) {
        // Votre code ici
    }

    @FXML
    void Faire_un_virement(ActionEvent event) {
        // Votre code ici
    }
    @FXML
    void Gestion_Clients(ActionEvent event) {
        try {
            Outils.load(event, "Gestion Clients", "/fxml/clients.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Erreur de chargement");
            errorAlert.setContentText("Impossible de charger la page de gestion des clients.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    void Gestion_Comptes(ActionEvent event) {
        try {
            Outils.load(event, "Gestion Comptes", "/fxml/comptes.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Erreur de chargement");
            errorAlert.setContentText("Impossible de charger la page de gestion des comptes.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    void Transaction(ActionEvent event) {
        try {
            Outils.load(event, "Transactions", "/fxml/operation.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText("Erreur de chargement");
            errorAlert.setContentText("Impossible de charger la page des opérations.");
            errorAlert.showAndWait();
        }
    }

    @FXML
    void HistoriqueList(ActionEvent event) {
        // Votre code ici
    }

    @FXML
    void Profil(ActionEvent event) {
        // Votre code ici
    }

    @FXML
    void Services(ActionEvent event) {
        // Votre code ici
    }

    @FXML
    void deconexion(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText("Voulez-vous vraiment vous déconnecter ?");
        alert.setContentText("Cliquez sur 'OK' pour confirmer.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Outils.load(event, "Déconnexion", "/fxml/login.fxml");
                } catch (IOException e) {
                    e.printStackTrace(); // Affiche l'erreur dans la console
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText("Erreur de chargement");
                    errorAlert.setContentText("Impossible de charger la page de déconnexion.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    public static void load(ActionEvent event, String title, String url) throws IOException {
        new Outils().loadPage(event, title, url);
    }

    @FXML
    void depot_argent(ActionEvent event) {
        // Votre code ici
    }

    @FXML
    void retrait_argent(ActionEvent event) {
        // Votre code ici
    }
}