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

public class AjoutClientController {

    @FXML
    private Button ajouterBtn;

    @FXML
    private TextField emailTfd;

    @FXML
    private TextField nomTfd;

    @FXML
    private TextField prenomTfd;

    @FXML
    void retour(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Clients", "/FXML/InterfaceClients.fxml");
    }

    @FXML
    void ajout(ActionEvent event) {
        IClient iClient = new ClientImpl();

        String nom = nomTfd.getText();
        String prenom = prenomTfd.getText();
        String email = emailTfd.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        Client client = new Client(0, nom, prenom, email);

        try {
            int ok = iClient.addClient(client);

            if (ok > 0) {
                Notification.NotifSuccess("Succès", "Client ajouté avec succès !");
                clearFields();
                Outils.load(event, "Gestion des Clients", "/fxml/InterfaceClients.fxml");
            } else {
                Notification.NotifError("Erreur", "Échec de l'ajout du client !");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Une erreur est survenue lors de l'ajout !");
        }
    }

    public void clearFields(){
        prenomTfd.setText("");
        nomTfd.setText("");
        emailTfd.setText("");
    }

}
