package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.service.impl.UserImpl;
import com.banking.digitalbankingproject.util.AlertUtil;
import com.banking.digitalbankingproject.tools.Outils;
import com.banking.digitalbankingproject.tools.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class UserController {
    private IUser userDao = new UserImpl();

    @FXML
    private PasswordField passwordTfd;
    @FXML
    private TextField usernameTfd;

    @FXML
    void login(ActionEvent event) {
        String username = usernameTfd.getText().trim();
        String password = passwordTfd.getText().trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            AlertUtil.showError("Champs requis", "Tous les champs sont obligatoires");
            return;
        }

        try {
            User user = userDao.getUserByUsername(username);
            if (user == null || !Utils.checkPassword(password, user.getPassword())) {
                AlertUtil.showError("Authentification échouée", "Nom d'utilisateur et/ou mot de passe incorrects");
                return;
            }

            AlertUtil.showSuccess("Connexion réussie", "Bienvenue dans Digital Banking");
            Outils.load(event, "Digital Banking", "/fxml/accueil.fxml");
        } catch (Exception e) {
            AlertUtil.showError("Erreur système", "Une erreur est survenue lors de la connexion");
            e.printStackTrace();
        }
    }
}
