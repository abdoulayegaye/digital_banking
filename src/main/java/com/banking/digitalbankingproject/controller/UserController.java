package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.User;
import com.banking.digitalbankingproject.service.impl.UserImpl;
import com.banking.digitalbankingproject.util.Session;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import com.banking.digitalbankingproject.enums.Role;
import com.banking.digitalbankingproject.util.PasswordUtil;

public class UserController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Label errorLabel;
    
    private UserImpl userService;
    
    public UserController() {
        this.userService = new UserImpl();
    }
    
    @FXML
    public void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        System.out.println("Tentative de connexion avec:");
        System.out.println("Username: " + username);
        System.out.println("Password entré: " + password);
        
        // Validation des champs
        if (username.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }
        
        try {
            User user = userService.getUserByUsername(username);
            if (user != null) {
                // UNIQUEMENT POUR LE DÉBOGAGE - comparaison simple
                if (password.equals(user.getPassword())) {
                    System.out.println("Connexion réussie pour: " + username);
                    Session.setCurrentUser(user);
                    
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.show();
                } else {
                    System.out.println("Mot de passe incorrect pour: " + username);
                    showError("Nom d'utilisateur ou mot de passe incorrect");
                }
            } else {
                System.out.println("Utilisateur non trouvé: " + username);
                showError("Utilisateur non trouvé");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        System.out.println("Erreur: " + message);
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
