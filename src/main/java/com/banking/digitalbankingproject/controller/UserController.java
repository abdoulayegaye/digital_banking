package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import java.io.IOException;

public class UserController {

    @FXML
    private Button login;

    @FXML
    private TextField usernameTfd;

    @FXML
    private PasswordField passwordTfd;

    @FXML
    private Label errorLabel;

    @FXML
    private void login() {
        String username = usernameTfd.getText();
        String password = passwordTfd.getText();

        if ("admin".equals(username) && "admin".equals(password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) usernameTfd.getScene().getWindow();

                stage.setScene(new Scene(root));
                stage.setTitle("Accueil");
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Erreur lors du chargement de la page.");
            }
        } else {
            errorLabel.setText("Nom d'utilisateur ou mot de passe incorrect.");
        }
    }
}