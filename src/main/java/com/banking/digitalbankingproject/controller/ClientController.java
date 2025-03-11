package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

public class ClientController {
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtEmail;
    @FXML
    private TableView<Client> tableClients;
    @FXML
    private TableColumn<Client, Integer> colId;
    @FXML
    private TableColumn<Client, String> colNom;
    @FXML
    private TableColumn<Client, String> colPrenom;
    @FXML
    private TableColumn<Client, String> colEmail;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;

    private IClient clientService;
    private ObservableList<Client> clientsList;

    @FXML
    private void initialize() {
        try {
            clientService = new ClientImpl();
            setupTableColumns();
            loadClients();
            setupButtons();
            setupTableSelection();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur de connexion", "Impossible de se connecter à la base de données");
        }
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadClients() {
        clientsList = FXCollections.observableArrayList(clientService.getAllClients());
        tableClients.setItems(clientsList);
    }

    private void setupButtons() {
        btnAjouter.setOnAction(e -> handleAjouterClient());
        btnModifier.setOnAction(e -> handleModifierClient());
        btnSupprimer.setOnAction(e -> handleSupprimerClient());
    }

    private void setupTableSelection() {
        tableClients.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNom.setText(newSelection.getNom());
                txtPrenom.setText(newSelection.getPrenom());
                txtEmail.setText(newSelection.getEmail());
            }
        });
    }

    private void handleAjouterClient() {
        if (validateInputs()) {
            Client newClient = new Client();
            newClient.setNom(txtNom.getText());
            newClient.setPrenom(txtPrenom.getText());
            newClient.setEmail(txtEmail.getText());

            try {
                Client savedClient = clientService.createClient(newClient);
                clientsList.add(savedClient);
                clearInputs();
                AlertUtil.showSuccess("Client ajouté", "Le client a été ajouté avec succès");
            } catch (Exception e) {
                AlertUtil.showError("Erreur", "Impossible d'ajouter le client");
            }
        }
    }

    private void handleModifierClient() {
        Client selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient != null && validateInputs()) {
            selectedClient.setNom(txtNom.getText());
            selectedClient.setPrenom(txtPrenom.getText());
            selectedClient.setEmail(txtEmail.getText());

            try {
                clientService.updateClient(selectedClient);
                tableClients.refresh();
                clearInputs();
                AlertUtil.showSuccess("Client modifié", "Le client a été modifié avec succès");
            } catch (Exception e) {
                AlertUtil.showError("Erreur", "Impossible de modifier le client");
            }
        } else {
            AlertUtil.showError("Sélection requise", "Veuillez sélectionner un client");
        }
    }

    private void handleSupprimerClient() {
        Client selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            if (AlertUtil.showConfirmation("Supprimer le client", 
                "Êtes-vous sûr de vouloir supprimer ce client ?")) {
                try {
                    clientService.deleteClient(selectedClient.getId());
                    clientsList.remove(selectedClient);
                    clearInputs();
                    AlertUtil.showSuccess("Client supprimé", "Le client a été supprimé avec succès");
                } catch (Exception e) {
                    AlertUtil.showError("Erreur", "Impossible de supprimer le client");
                }
            }
        } else {
            AlertUtil.showError("Sélection requise", "Veuillez sélectionner un client");
        }
    }

    private boolean validateInputs() {
        if (txtNom.getText().trim().isEmpty() || 
            txtPrenom.getText().trim().isEmpty() || 
            txtEmail.getText().trim().isEmpty()) {
            AlertUtil.showError("Champs requis", "Tous les champs sont obligatoires");
            return false;
        }
        if (!txtEmail.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            AlertUtil.showError("Format invalide", "Format d'email invalide");
            return false;
        }
        return true;
    }

    private void clearInputs() {
        txtNom.clear();
        txtPrenom.clear();
        txtEmail.clear();
        tableClients.getSelectionModel().clearSelection();
    }
}
