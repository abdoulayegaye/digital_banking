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
import javafx.scene.control.Label;
import java.io.IOException;

public class UserController {

    private IUser userDao = new UserImpl();

    @FXML
    private PasswordField passwordTfd;

    @FXML
    private TextField usernameTfd;

    @FXML
    private Label errorLabel;

    @FXML
    void login(ActionEvent event) {
        try {
            String username = usernameTfd.getText().trim();
            String password = passwordTfd.getText().trim();
            
            System.out.println("=== Début de la tentative de connexion ===");
            System.out.println("Username saisi: " + username);
            
            User user = userDao.getUserByUsername(username);
            System.out.println("Utilisateur trouvé: " + (user != null));
            
            if (user != null) {
                String storedPassword = user.getPassword();
                System.out.println("Mot de passe stocké: " + storedPassword);
                
                // Si le mot de passe stocké est en clair (première connexion)
                if (storedPassword.equals(password)) {
                    // Hash le mot de passe et met à jour la base de données
                    String hashedPassword = Utils.hashPassword(password);
                    user.setPassword(hashedPassword);
                    userDao.updateUserPassword(user);
                    
                    System.out.println("Premier login : mot de passe hashé et mis à jour");
                    Notification.showNotification("Succès", "Connexion réussie !", Notification.NotificationType.SUCCESS);
                    Outils.load(event, "Bienvenue à Digital Banking", "/fxml/accueil.fxml");
                    return;
                }
                
                // Sinon, vérifie le mot de passe hashé
                boolean passwordMatch = Utils.checkPassword(password, storedPassword);
                System.out.println("Vérification du hash: " + passwordMatch);
                
                if (passwordMatch) {
                    Notification.showNotification("Succès", "Connexion réussie !", Notification.NotificationType.SUCCESS);
                    Outils.load(event, "Bienvenue à Digital Banking", "/fxml/accueil.fxml");
                } else {
                    errorLabel.setText("Mot de passe incorrect");
                    errorLabel.setVisible(true);
                }
            } else {
                errorLabel.setText("Utilisateur non trouvé");
                errorLabel.setVisible(true);
            }
            System.out.println("=== Fin de la tentative de connexion ===");
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement de la page d'accueil: " + e.getMessage());
            errorLabel.setText("Erreur système. Veuillez réessayer.");
            errorLabel.setVisible(true);
        } catch (Exception e) {
            System.err.println("Erreur inattendue: " + e.getMessage());
            errorLabel.setText("Erreur système. Veuillez réessayer.");
            errorLabel.setVisible(true);
        }
    }

}
