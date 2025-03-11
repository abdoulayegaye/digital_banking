package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;

public class LoginController {
    @FXML private TextField usernameTfd;
    @FXML private PasswordField passwordTfd;
    @FXML private Button loginBtn;

    @FXML
    private void initialize() {
        // Initialisation facultative
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameTfd.getText();
        String password = passwordTfd.getText();
        if (username.isEmpty() || password.isEmpty()) {
            Notification.NotifError("Erreur", "Veuillez remplir tous les champs.");
        } else {
            // Logique de connexion ici
            Notification.NotifSuccess("Succès", "Connexion réussie !");
            try {
                Outils.load(event, "Accueil", "/fxml/accueil.fxml");
            } catch (Exception e) {
                e.printStackTrace();
                Outils.showError("Erreur", "Impossible de charger la page Accueil.");
            }
        }
    }
}
