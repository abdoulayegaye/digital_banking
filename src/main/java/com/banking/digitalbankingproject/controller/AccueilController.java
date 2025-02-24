package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class AccueilController {

    @FXML
    private Button clientsBtn;

    @FXML
    private Button compteBtn;

    @FXML
    private Button operationsBtn;

    @FXML
    private Button seDeconnecterBtn;

    @FXML
    private TextField tatalClientsTfd;

    @FXML
    private TextField totalComptesTfd;

    @FXML
    void pageClients(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Clients", "/fxml/InterfaceClients.fxml");
    }

    @FXML
    void pageCompte(ActionEvent event) {

    }

    @FXML
    void pageOperations(ActionEvent event) {

    }

    @FXML
    void seDeconnecter(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText("Se déconnecter");
        alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

        // Attendre la réponse de l'utilisateur
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Db db = new Db();
            db.closeConnection();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
                Parent root = loader.load();

                // Remplacer la scène actuelle par la page de connexion
                seDeconnecterBtn.getScene().setRoot(root);

                Notification.NotifSuccess("Déconnexion", "Vous avez été déconnecté avec succès !");
            } catch (IOException e) {
                e.printStackTrace();
                Notification.NotifError("Erreur", "Impossible de retourner à la page de connexion !");
            }
        }
    }


}
