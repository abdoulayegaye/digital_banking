package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ClientController {
    @FXML
    private TextField idField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private Label messageLabel;
    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TableColumn<Client, Integer> idColumn;
    @FXML
    private TableColumn<Client, String> nomColumn;
    @FXML
    private TableColumn<Client, String> prenomColumn;
    @FXML
    private TableColumn<Client, String> emailColumn;
    @FXML
    private Button homeButton;

    @FXML
    private void handleHomeButton(ActionEvent event) {
        try {
            // Charger la vue de gestion des clients
            Outils.load(event, "Retour ", "/fxml/accueil.fxml");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement  : " + e.getMessage());
        }
    }

    private IClient clientService = new ClientImpl(); // Utilisation de l'interface
    private ObservableList<Client> clientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Associer les colonnes aux propriétés du modèle Client
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Charger la liste des clients dans la TableView
        refreshClientList();
    }

    // Créer un client
    @FXML
    public void createClient() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);

        if (clientService.createClient(client)) {
            messageLabel.setText("Client créé avec succès !");
            refreshClientList();
            clearFields();
        } else {
            messageLabel.setText("Erreur lors de la création du client.");
        }
    }

    // Modifier un client
    @FXML
    public void updateClient() {
        try {
            int id = Integer.parseInt(idField.getText());
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();

            if (clientService.updateClient(id, nom, prenom, email)) {
                messageLabel.setText("Client modifié avec succès !");
                refreshClientList();
                clearFields();
            } else {
                messageLabel.setText("Client non trouvé.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("ID invalide.");
        }
    }

    // Supprimer un client
    @FXML
    public void deleteClient() {
        try {
            int id = Integer.parseInt(idField.getText());

            if (clientService.deleteClient(id)) {
                messageLabel.setText("Client supprimé avec succès !");
                refreshClientList();
                clearFields();
            } else {
                messageLabel.setText("Client non trouvé.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("ID invalide.");
        }
    }

    // Rafraîchir la liste des clients
    @FXML
    public void refreshClientList() {
        clientList.setAll(clientService.getAllClients());
        clientTable.setItems(clientList);
    }

    // Effacer les champs du formulaire
    private void clearFields() {
        idField.clear();
        nomField.clear();
        prenomField.clear();
        emailField.clear();
    }
}