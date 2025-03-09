package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import static com.banking.digitalbankingproject.tools.Notification.NotifSuccess;

public class AjouterClientController {

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtEmail;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnRetour;

    private IClient clientService = new ClientImpl();

    @FXML
    private void initialize() {
        btnAjouter.setOnAction(event -> ajouterClient());
        btnRetour.setOnAction(event -> retourGestionClients(new ActionEvent()));
    }

    @FXML
    private void ajouterClient() {
        String nom = txtNom.getText();
        String prenom = txtPrenom.getText();
        String email = txtEmail.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            com.banking.digitalbankingproject.tools.Notification.NotifError("Erreur", "Tous les champs doivent être remplis");
            return;
        }

        Client client = new Client(nom, prenom, email);
        clientService.ajouterClient(client);
        NotifSuccess("Succès", "Client ajouté avec succès");
        retourGestionClients(new ActionEvent());
    }

    @FXML
    private void retourGestionClients(ActionEvent event) {
        try {
            // Charger la vue de gestion des clients
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/fxml/gestionClients.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) btnRetour.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root));
            stage.setTitle("Gestion des Clients");
            stage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void retourAccueil(ActionEvent actionEvent) {
    }
}