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
import java.util.logging.Logger;

public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());
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
            logger.warning("Tentative de connexion avec des champs vides");
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            return;
        }
        
        try {
            logger.info("Tentative de connexion pour l'utilisateur: " + username);
            
            // Vérifier si l'utilisateur existe
            User user = userDao.getUserByUsername(username);
            if (user == null) {
                logger.warning("Utilisateur non trouvé: " + username);
                Notification.NotifError("Erreur", "Nom d'utilisateur et/ou mot de passe incorrects !");
                return;
            }
            
            logger.info("Utilisateur trouvé: " + username + ", vérification du mot de passe...");
            
            // Pour le débogage uniquement - À supprimer en production
            logger.info("Mot de passe saisi: " + password);
            logger.info("Mot de passe stocké: " + user.getPassword());
            
            // Vérifier si le mot de passe correspond
            if (password.equals(user.getPassword())) {
                // Connexion réussie avec mot de passe en clair
                logger.info("Connexion réussie avec mot de passe en clair pour l'utilisateur: " + username);
                Notification.NotifSuccess("Succès", "Connexion réussie !");
                
                // Charger la page d'accueil
                Outils.load(event, "Bienvenue à Digital Banking - " + user.getPrenom() + " " + user.getNom(), "/fxml/accueil.fxml");
            } else if (Utils.checkPassword(password, user.getPassword())) {
                // Connexion réussie avec mot de passe hashé
                logger.info("Connexion réussie avec mot de passe hashé pour l'utilisateur: " + username);
                Notification.NotifSuccess("Succès", "Connexion réussie !");
                
                // Charger la page d'accueil
                Outils.load(event, "Bienvenue à Digital Banking - " + user.getPrenom() + " " + user.getNom(), "/fxml/accueil.fxml");
            } else {
                // Mot de passe incorrect
                logger.warning("Mot de passe incorrect pour l'utilisateur: " + username);
                Notification.NotifError("Erreur", "Nom d'utilisateur et/ou mot de passe incorrects !");
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la connexion: " + e.getMessage());
            e.printStackTrace();
            Notification.NotifError("Erreur", "Une erreur est survenue lors de la connexion: " + e.getMessage());
        }
    }
}
