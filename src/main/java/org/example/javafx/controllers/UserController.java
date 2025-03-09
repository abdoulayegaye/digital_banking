package org.example.javafx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.javafx.entities.User;
import org.example.javafx.service.IUser;
import org.example.javafx.service.impl.UserImpl;
import org.example.javafx.tools.Notification;
import org.example.javafx.tools.Outils;

public class UserController {

    @FXML
    private Button ConnecterTfd;

    @FXML
    private PasswordField MotDePasseTfd;

    @FXML
    private TextField utilisateurTfd;
    IUser dao = new UserImpl();
    @FXML
    void getLogin(ActionEvent event) {
        String login = utilisateurTfd.getText();
        String password = MotDePasseTfd.getText();
        if (login.equals("") || password.equals("")) {
            Notification.NotifError("Error","Tous les champs sont obligatoires !");
        }else {
            User user=dao.SeConnecter(login, password);
           try {
               if (user != null) {
                   Notification.NotifSuccess("Succes","Connecter");
                   Outils.load(event, "Digital","/fxml/accueil.fxml");
               }else{
                   Notification.NotifError("Error","Impossible de se connecter !");
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
        }
    }
}

