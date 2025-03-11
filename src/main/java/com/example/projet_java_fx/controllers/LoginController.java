package com.example.projet_java_fx.controllers;

import com.example.projet_java_fx.service.IUser;
import com.example.projet_java_fx.service.impl.UserImpl;
import com.example.projet_java_fx.entity.Users;
import com.example.projet_java_fx.tools.Notification;
import com.example.projet_java_fx.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private Button connexionBtn;

    @FXML
    private TextField emailTfd;

    @FXML
    private TextField passwordTfd;

    IUser userdao = new UserImpl();

    @FXML
    void getlgin(ActionEvent event) {
        String email = emailTfd.getText();
        String password = passwordTfd.getText();
        if (email.equals("") || password.equals("")) {
            Notification.NotifError("Attention","Tous les champs sont obligatoires !");
        }else {
           try {
               Users user = userdao.Login(email, password);
               if (user == null) {
                   Notification.NotifError("Error","Email ou password incorrect !");
               }else {
                   Notification.NotifSuccess("Success","Connexion reussi avec succes");
                   Outils.load(event,"Welcome to Cripto Bank","/pages/accueil.fxml");
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
        }
    }

}
