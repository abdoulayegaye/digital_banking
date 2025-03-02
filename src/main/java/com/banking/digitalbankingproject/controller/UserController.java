package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.IUser;
import com.banking.digitalbankingproject.service.impl.UserImpl;
import com.banking.digitalbankingproject.tools.Notification;
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
            Notification.NotifError("Error", "Tous les champs sont obligatoires");
        } else {
            try {
                User user = userDao.getUserByUsername(username);
                if (user == null) {
                    Notification.NotifError("Error", "Username et/ou Password incorrects !");
                } else if(Utils.checkPassword(password, user.getPassword())) {
                    Notification.NotifSuccess("Success", "Connexion réussie !");
                    Outils.load(event, "Bienvenue à Digital Banking", "/fxml/accueil.fxml");
                }
            }catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
