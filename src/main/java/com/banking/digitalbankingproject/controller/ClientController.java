package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ClientController {

    private IClient clientDao = new ClientImpl();

    @FXML
    private TextField nomTfd;

    @FXML
    private TextField prenomTfd;

    @FXML
    private TextField emailTfd;

    // Zone de recherche
    @FXML
    private TextField searchTfd;

    @FXML
    private TableView<Client> clientsTable;

    @FXML
    private TableColumn<Client, Integer> idCol;

    @FXML
    private TableColumn<Client, String> nomCol;

    @FXML
    private TableColumn<Client, String> prenomCol;

    @FXML
    private TableColumn<Client, String> emailCol;

    @FXML
    private Button updateBtn;

    // Client actuellement sélectionné dans la table (pour modification)
    private Client selectedClient = null;

    @FXML
    void initialize() {
        //idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadClients();

        // Sélection d'un client dans la table : pré-remplit les champs pour modification
        clientsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedClient = newSelection;
                nomTfd.setText(selectedClient.getNom());
                prenomTfd.setText(selectedClient.getPrenom());
                emailTfd.setText(selectedClient.getEmail());
            }
        });
    }

    @FXML
    void addClient() {
        String nom = nomTfd.getText().trim();
        String prenom = prenomTfd.getText().trim();
        String email = emailTfd.getText().trim();

        // Vérifier si les champs sont vides
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            return;
        }

        // Vérifier si l'email est valide
        if (!Outils.isEmailValid(email)) {
            Notification.NotifError("Erreur", "L'email n'est pas valide");
            return;
        }

        // Vérifier si le client existe déjà
        if (clientDao.clientExists(nom, prenom, email)) {
            Notification.NotifError("Erreur", "Ce client existe déjà");
            return;
        }

        // Créer et ajouter le client
        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);

        if (clientDao.createClient(client)) {
            Notification.NotifSuccess("Succès", "Client ajouté avec succès");
            loadClients(); // Recharger la liste des clients
            clearFields(); // Effacer les champs de saisie
        } else {
            Notification.NotifError("Erreur", "Échec de l'ajout du client");
        }
    }
    @FXML
    void updateClient() {
        if (selectedClient == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client à modifier");
            return;
        }

        // Récupérer les valeurs des champs de saisie
        String nom = nomTfd.getText().trim();
        String prenom = prenomTfd.getText().trim();
        String email = emailTfd.getText().trim();

        // Vérifier si les champs sont vides
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            return;
        }

        // Vérifier si l'email est valide
        if (!Outils.isEmailValid(email)) {
            Notification.NotifError("Erreur", "L'email n'est pas valide");
            return;
        }

        // Vérifier si les valeurs ont changé
        if (nom.equals(selectedClient.getNom())
                && prenom.equals(selectedClient.getPrenom())
                && email.equals(selectedClient.getEmail())) {
            Notification.NotifSuccess("Information", "Aucune modification à faire");
            return;
        }

        // Mettre à jour le client si des changements sont détectés
        selectedClient.setNom(nom);
        selectedClient.setPrenom(prenom);
        selectedClient.setEmail(email);

        // Appeler la méthode de mise à jour dans ClientImpl
        if (clientDao.updateClient(selectedClient)) {
            Notification.NotifSuccess("Succès", "Client modifié avec succès");

            // Rafraîchir la TableView
            clientsTable.refresh(); // Rafraîchir la TableView

            // Effacer les champs de saisie
            clearFields();

            // Réinitialiser le client sélectionné
            selectedClient = null;
        } else {
            Notification.NotifError("Erreur", "Échec de la modification du client");
        }
    }

    @FXML
    void searchClient() {
        String keyword = searchTfd.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadClients();
        } else {
            List<Client> filteredClients = clientDao.getAllClients().stream()
                    .filter(c -> c.getNom().toLowerCase().contains(keyword)
                            || c.getPrenom().toLowerCase().contains(keyword)
                            || c.getEmail().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
            clientsTable.setItems(FXCollections.observableArrayList(filteredClients));
        }
    }

    private void loadClients() {
        clientsTable.setItems(FXCollections.observableArrayList(clientDao.getAllClients()));
    }

    private void clearFields() {
        nomTfd.clear();
        prenomTfd.clear();
        emailTfd.clear();
        searchTfd.clear();
        clientsTable.getSelectionModel().clearSelection();
    }

    @FXML
    void retour(ActionEvent event) {
        // Navigation vers la page d'accueil
        Outils.load(event, "Accueil", "/fxml/accueil.fxml");
    }

    @FXML
    void deleteClient() {
        // Récupérer le client sélectionné dans la table
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();

        if (selectedClient == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client à supprimer");
            return;
        }

        // Vérifier si le client a des comptes associés
        if (clientDao.hasLinkedAccounts(selectedClient.getId())) {
            Notification.NotifError("Erreur", "Impossible de supprimer ce client : il est lié à un ou plusieurs comptes.");
            return;
        }

        // Appeler la méthode de suppression dans ClientImpl
        if (clientDao.deleteClient(selectedClient.getId())) {
            Notification.NotifSuccess("Succès", "Client supprimé avec succès");
            loadClients(); // Recharger la liste des clients
            clearFields(); // Effacer les champs de saisie
        } else {
            Notification.NotifError("Erreur", "Échec de la suppression du client");
        }
    }
}
