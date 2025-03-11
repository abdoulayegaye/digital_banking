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

    private Client selectedClient = null;

    @FXML
    void initialize() {
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadClients();

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

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            return;
        }

        if (!Outils.isEmailValid(email)) {
            Notification.NotifError("Erreur", "L'email n'est pas valide");
            return;
        }

        if (clientDao.clientExists(nom, prenom, email)) {
            Notification.NotifError("Erreur", "Ce client existe déjà");
            return;
        }

        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);

        if (clientDao.createClient(client)) {
            Notification.NotifSuccess("Succès", "Client ajouté avec succès");
            loadClients();
            clearFields();
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

        String nom = nomTfd.getText().trim();
        String prenom = prenomTfd.getText().trim();
        String email = emailTfd.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            return;
        }

        if (!Outils.isEmailValid(email)) {
            Notification.NotifError("Erreur", "L'email n'est pas valide");
            return;
        }

        if (nom.equals(selectedClient.getNom())
                && prenom.equals(selectedClient.getPrenom())
                && email.equals(selectedClient.getEmail())) {
            Notification.NotifSuccess("Information", "Aucune modification à faire");
            return;
        }

        selectedClient.setNom(nom);
        selectedClient.setPrenom(prenom);
        selectedClient.setEmail(email);

        if (clientDao.updateClient(selectedClient)) {
            Notification.NotifSuccess("Succès", "Client modifié avec succès");

            clientsTable.refresh();

            clearFields();

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
        Outils.load(event, "Accueil", "/fxml/accueil.fxml");
    }

    @FXML
    void deleteClient() {
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();

        if (selectedClient == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client à supprimer");
            return;
        }

        if (clientDao.hasLinkedAccounts(selectedClient.getId())) {
            Notification.NotifError("Erreur", "Impossible de supprimer ce client : il est lié à un ou plusieurs comptes.");
            return;
        }

        if (clientDao.deleteClient(selectedClient.getId())) {
            Notification.NotifSuccess("Succès", "Client supprimé avec succès");
            loadClients();
            clearFields();
        } else {
            Notification.NotifError("Erreur", "Échec de la suppression du client");
        }
    }
}
