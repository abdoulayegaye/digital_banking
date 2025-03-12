package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;

public class ClientController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Client> clientsTable;

    @FXML
    private TableColumn<Client, Integer> idColumn;

    @FXML
    private TableColumn<Client, String> nomColumn;

    @FXML
    private TableColumn<Client, String> prenomColumn;

    @FXML
    private TableColumn<Client, String> emailColumn;

    @FXML
    private TableColumn<Client, Void> actionsColumn;

    private IClient clientService = new ClientImpl();

    @FXML
    void handleNewClientAction(ActionEvent event) {
        try {
            Outils.load(event, "Nouveau Client", "/fxml/nouveau-client.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Notification.showNotification("Erreur", "Erreur lors de l'ouverture du formulaire", Notification.NotificationType.ERROR);
        }
    }

    @FXML
    void handleRetourAction(ActionEvent event) {
        try {
            Outils.load(event, "Digital Banking", "/fxml/accueil.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSearchAction(ActionEvent event) {
        String searchTerm = searchField.getText().trim().toLowerCase();
        List<Client> allClients = clientService.getAllClients();
        List<Client> filteredClients = allClients.stream()
                .filter(client -> 
                    client.getNom().toLowerCase().contains(searchTerm) ||
                    client.getPrenom().toLowerCase().contains(searchTerm) ||
                    client.getEmail().toLowerCase().contains(searchTerm))
                .toList();
        clientsTable.setItems(FXCollections.observableArrayList(filteredClients));
    }

    @FXML
    void initialize() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        // Configuration de la colonne d'actions
        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Supprimer");
            private final Button editBtn = new Button("Modifier");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);

            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                
                deleteBtn.setOnAction(event -> {
                    Client client = getTableView().getItems().get(getIndex());
                    handleDeleteClient(client);
                });
                
                editBtn.setOnAction(event -> {
                    Client client = getTableView().getItems().get(getIndex());
                    handleEditClient(client);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });

        // Chargement des données
        refreshTable();
    }

    private void refreshTable() {
        List<Client> clients = clientService.getAllClients();
        clientsTable.setItems(FXCollections.observableArrayList(clients));
    }

    private void handleDeleteClient(Client client) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Suppression de client");
        alert.setContentText("Voulez-vous vraiment supprimer ce client ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (clientService.deleteClient(client.getId())) {
                Notification.showNotification("Succès", "Client supprimé avec succès", Notification.NotificationType.SUCCESS);
                refreshTable();
            } else {
                Notification.showNotification("Erreur", "Erreur lors de la suppression du client", Notification.NotificationType.ERROR);
            }
        }
    }

    private void handleEditClient(Client client) {
        // TODO: Implémenter la modification du client
        Notification.showNotification("Info", "Fonctionnalité à venir", Notification.NotificationType.INFO);
    }
}
