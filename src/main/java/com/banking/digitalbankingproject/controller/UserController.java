package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.service.impl.UserImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import com.banking.digitalbankingproject.tools.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    private final IUser userDao = new UserImpl();

    @FXML
    private PasswordField passwordTfd;

    @FXML
    private TextField usernameTfd;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Style des champs
        usernameTfd.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; " +
                           "-fx-border-color: #dee2e6; -fx-background-color: white; -fx-padding: 8;");
        passwordTfd.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; " +
                           "-fx-border-color: #dee2e6; -fx-background-color: white; -fx-padding: 8;");
    }

    @FXML
    private void login(ActionEvent event) {
        String username = usernameTfd.getText().trim();
        String password = passwordTfd.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            return;
        }
        
        try {
            User user = userDao.getUserByUsername(username);
            if (user == null) {
                Notification.NotifError("Erreur", "Nom d'utilisateur ou mot de passe incorrect");
                return;
            }
            
            if (!Utils.checkPassword(password, user.getPassword())) {
                Notification.NotifError("Erreur", "Nom d'utilisateur ou mot de passe incorrect");
                return;
            }
            
            try {
                Outils.load(event, "Digital Banking - Accueil", "/fxml/accueil.fxml");
            } catch (IOException e) {
                Notification.NotifError("Erreur", "Impossible de charger la page d'accueil");
                e.printStackTrace();
            }
        } catch (Exception e) {
            Notification.NotifError("Erreur", "Une erreur est survenue lors de la connexion");
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFields() {
        usernameTfd.clear();
        passwordTfd.clear();
        usernameTfd.requestFocus();
    }
}
