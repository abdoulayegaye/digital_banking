package com.banking.digitalbankingproject.controller;

import javafx.geometry.Insets;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ListeClientController {

    private final IClient clientService = new ClientImpl(); // Service client

    @FXML
    private TableView<Client> tableView;

    @FXML
    private TableColumn<Client, Integer> colId;
    @FXML
    private TableColumn<Client, String> colNom;
    @FXML
    private TableColumn<Client, String> colPrenom;
    @FXML
    private TableColumn<Client, String> colEmail;

    @FXML
    private TextField recherche;

    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnRetour;

    private ObservableList<Client> clientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Associer les colonnes aux propriétés de Client
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Charger les clients dans la table
        chargerClients();

        // Ajouter un écouteur pour la recherche
        recherche.textProperty().addListener((observable, oldValue, newValue) -> filtrerClients(newValue));
    }

    /**
     * Charger la liste des clients.
     */
    private void chargerClients() {
        List<Client> clients = clientService.getAllClients();
        clientList.setAll(clients);
        tableView.setItems(clientList);
    }

    /**
     * Filtrer les clients selon la recherche.
     */
    private void filtrerClients(String motCle) {
        ObservableList<Client> clientsFiltres = FXCollections.observableArrayList();

        if (motCle == null || motCle.trim().isEmpty()) {
            tableView.setItems(clientList);
            return;
        }

        String rechercheLower = motCle.toLowerCase();

        for (Client client : clientList) {
            if (client.getNom().toLowerCase().contains(rechercheLower) ||
                    client.getPrenom().toLowerCase().contains(rechercheLower) ||
                    client.getEmail().toLowerCase().contains(rechercheLower)) {
                clientsFiltres.add(client);
            }
        }

        tableView.setItems(clientsFiltres);
    }

    /**
     * Modifier un client.
     */
    @FXML
    void btnModifier(ActionEvent event) {
        System.out.println("Bouton Modifier cliqué !");

        Client clientSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (clientSelectionne == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client à modifier.");
            return;
        }

        // Ouvrir la fenêtre de modification
        ouvrirFenetreModification(clientSelectionne);
    }

    private void ouvrirFenetreModification(Client client) {
        Stage fenetre = new Stage();
        fenetre.setTitle("Modifier le Client");

        // Création des champs de modification
        TextField txtNom = new TextField(client.getNom());
        TextField txtPrenom = new TextField(client.getPrenom());
        TextField txtEmail = new TextField(client.getEmail());

        Button btnEnregistrer = new Button("Enregistrer");
        btnEnregistrer.setOnAction(e -> {
            // Mettre à jour le client
            client.setNom(txtNom.getText());
            client.setPrenom(txtPrenom.getText());
            client.setEmail(txtEmail.getText());

            // Fermer la fenêtre
            fenetre.close();

            // Afficher un message de confirmation
            Notification.NotifSuccess("Succès", "Client modifié avec succès !");

            // Rafraîchir la table
            tableView.refresh();
        });

        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setOnAction(e -> fenetre.close());

        // Organisation des éléments
        VBox vbox = new VBox(10, new Label("Nom :"), txtNom,
                new Label("Prénom :"), txtPrenom,
                new Label("Email :"), txtEmail,
                btnEnregistrer, btnAnnuler);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 300, 250);
        fenetre.setScene(scene);
        fenetre.show();
    }



    /**
     * Supprimer un client.
     */
    @FXML
    void btnSupprimer(ActionEvent event) {
        System.out.println("Bouton Supprimer cliqué !");

        Client clientSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (clientSelectionne == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client à supprimer.");
            return;
        }

        boolean confirmation = Notification.ConfirmDialog("Confirmation", "Voulez-vous vraiment supprimer ce client ?");
        if (!confirmation) return;

        clientService.supprimerClient(clientSelectionne.getId());
        Notification.NotifSuccess("Succès", "Client supprimé avec succès");

        chargerClients(); // Mise à jour après suppression
    }

    /**
     * Retourner à la page précédente.
     */
    @FXML
    void btnRetour(ActionEvent event) throws IOException {
        Outils.load(event, "Ajoutee un client", "/fxml/clients.fxml");
    }
}
