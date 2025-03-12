package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AccueilController {

    @FXML
    private Button clientsBtn;

    @FXML
    private Button comptesBtn;

    @FXML
    private Button operationsBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    void handleClientsAction(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Clients", "/fxml/clients.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleComptesAction(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Comptes", "/fxml/comptes.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleOperationsAction(ActionEvent event) {
        try {
            Outils.load(event, "Opérations", "/fxml/operations.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogoutAction(ActionEvent event) {
        try {
            Outils.load(event, "Connexion", "/fxml/login.fxml");
            Notification.showNotification("Succès", "Vous avez été déconnecté avec succès", Notification.NotificationType.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
