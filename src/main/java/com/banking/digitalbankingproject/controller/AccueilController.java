package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.net.URL;
import java.util.ResourceBundle;


public class AccueilController implements Initializable {

    @FXML
    private Label title; // Assurez-vous que l'ID du label dans FXML correspond à "title"

    public void initialize(URL location, ResourceBundle resources) {
        // Appliquer une police et une couleur au label
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2E86C1")); // Couleur bleue
    }
    // Références aux boutons définis dans le fichier FXML
    @FXML
    private Button btnGestionClients;

    @FXML
    private Button btnGestionComptes;

    @FXML
    private Button btnGestionOperations;

    @FXML
    void handleGestionClients(ActionEvent event) {
        try {
            // Charger la vue de gestion des clients
            Outils.load(event, "Gestion des Clients", "/fxml/clients.fxml");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de la vue Gestion des Clients : " + e.getMessage());
        }
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "Gestion des Comptes".
     */
    @FXML
    void handleGestionComptes(ActionEvent event) {
        try {
            // Charger la vue de gestion des comptes
            Outils.load(event, "Gestion des Comptes", "/fxml/comptes.fxml");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de la vue Gestion des Comptes : " + e.getMessage());
        }
    }

    /**
     * Méthode appelée lorsque l'utilisateur clique sur "Gestion des Opérations".
     */
    @FXML
    void handleGestionOperations(ActionEvent event) {
        try {
            // Charger la vue de gestion des opérations
            Outils.load(event, "Gestion des Opérations", "/fxml/operations.fxml");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de la vue Gestion des Opérations : " + e.getMessage());
        }
    }
}