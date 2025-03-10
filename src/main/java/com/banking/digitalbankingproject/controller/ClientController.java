package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class ClientController {

    @FXML
    private TableView<Client> clientTable;

    @FXML
    private Button effacerBtn;

    @FXML
    private TableColumn<Client, String> emailCol;

    @FXML
    private TextField emailTfd;

    @FXML
    private Button enregistrerBtn;

    @FXML
    private TableColumn<Client, Integer> idCol;

    @FXML
    private Button modifierBtn;

    @FXML
    private TableColumn<Client, String> nomCol;

    @FXML
    private TextField nomTfd;

    @FXML
    private TableColumn<Client, String> prenomCol;

    @FXML
    private TextField prenomTfd;

    @FXML
    private Button retourBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchTfd;

    @FXML
    private Button supprimerBtn;

    private final IClient clientService = new ClientImpl();
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialisation des colonnes de la table
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        loadClients();
    }

    private void loadClients() {
        clientsList.clear();
        List<Client> clients = clientService.getAllClients();
        if (clients != null) {
            clientsList.addAll(clients);
        }
        clientTable.setItems(clientsList);
    }

    @FXML
    void effacer(ActionEvent event) {
        clearFields();
    }

    @FXML
    void enregistrer(ActionEvent event) {
        String nom = nomTfd.getText().trim();
        String prenom = prenomTfd.getText().trim();
        String email = emailTfd.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        Client client = new Client(nom, prenom, email);
        int result = clientService.addClient(client);

        if (result > 0) {
            showAlert("Succès", "Client ajouté avec succès !");
            clearFields();
            loadClients();
        } else {
            showAlert("Erreur", "Échec de l'ajout du client !");
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert("Erreur", "Veuillez sélectionner un client à modifier !");
            return;
        }

        String nom = nomTfd.getText().trim();
        String prenom = prenomTfd.getText().trim();
        String email = emailTfd.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        selectedClient.setNom(nom);
        selectedClient.setPrenom(prenom);
        selectedClient.setEmail(email);

        int result = clientService.updateClient(selectedClient);

        if (result > 0) {
            showAlert("Succès", "Client modifié avec succès !");
            clearFields();
            loadClients();
        } else {
            showAlert("Erreur", "Échec de la modification du client !");
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert("Erreur", "Veuillez sélectionner un client à supprimer !");
            return;
        }

        int result = clientService.deleteClient(selectedClient);

        if (result > 0) {
            showAlert("Succès", "Client supprimé avec succès !");
            loadClients();
        } else {
            showAlert("Erreur", "Échec de la suppression du client !");
        }
    }

    @FXML
    void searchClients(ActionEvent event) {
        String searchText = searchTfd.getText().trim();
        if (searchText.isEmpty()) {
            loadClients();
        } else {
            List<Client> searchResults = clientService.searchClientsByName(searchText);
            if (searchResults.isEmpty()) {
                showAlert("Information", "Aucun client trouvé avec ce nom.");
            } else {
                clientsList.clear();
                clientsList.addAll(searchResults);
                clientTable.setItems(clientsList);
            }
        }
    }

    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
            Parent root = loader.load();

            Scene scene = retourBtn.getScene();

            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'écran précédent.");
        }
    }

    private void clearFields() {
        nomTfd.clear();
        prenomTfd.clear();
        emailTfd.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}