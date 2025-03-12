package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NouveauClientController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    private IClient clientService = new ClientImpl();

    @FXML
    void handleAnnulerAction(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Clients", "/fxml/clients.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleEnregistrerAction(ActionEvent event) {
        try {
            // Validation des champs
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String email = emailField.getText().trim();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
                Notification.showNotification("Erreur", "Tous les champs sont obligatoires", Notification.NotificationType.ERROR);
                return;
            }

            // Création du client
            Client client = new Client();
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setEmail(email);

            // Enregistrement dans la base de données
            if (clientService.createClient(client)) {
                Notification.showNotification("Succès", "Client ajouté avec succès", Notification.NotificationType.SUCCESS);
                Outils.load(event, "Gestion des Clients", "/fxml/clients.fxml");
            } else {
                Notification.showNotification("Erreur", "Erreur lors de l'ajout du client", Notification.NotificationType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Notification.showNotification("Erreur", "Une erreur est survenue", Notification.NotificationType.ERROR);
        }
    }
} 