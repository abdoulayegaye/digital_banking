package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ModifierClientController {
    private Client client;

    @FXML
    private TextField emailTfd;

    @FXML
    private Button modifierBtn;

    @FXML
    private TextField nomTfd;

    @FXML
    private TextField prenomTfd;

    @FXML
    private Button retourBtn;

    @FXML
    void retour(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Clients", "/FXML/InterfaceClients.fxml");
    }

    @FXML
    void update(ActionEvent event) {
        IClient iClient = new ClientImpl();

        String nom = nomTfd.getText();
        String prenom = prenomTfd.getText();
        String email = emailTfd.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        // Vérifier si un client est bien sélectionné
        if (client == null) {
            Notification.NotifError("Erreur", "Aucun client sélectionné !");
            return;
        }

        // Mettre à jour les informations du client existant
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);

        try {
            int ok = iClient.updateClient(client);

            if (ok > 0) {
                Notification.NotifSuccess("Succès", "Client modifié avec succès !");
                Outils.load(event, "Gestion des Clients", "/fxml/InterfaceClients.fxml");
            } else {
                Notification.NotifError("Erreur", "Échec de la modification du client !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Une erreur est survenue lors de la modification !");
        }
    }

    public void getData(Client client) {
        if (client != null) {
            this.client = client;

            nomTfd.setText(client.getNom());
            prenomTfd.setText(client.getPrenom());
            emailTfd.setText(client.getEmail());
        }
    }

}
