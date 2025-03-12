package com.banking.digitalbankingproject.controller;
import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;

public class ClientController {
    @FXML
    private TableView<Client> tableClients;
    @FXML
    private TableColumn<Client, String> colNom;
    @FXML
    private TableColumn<Client, String> colPrenom;
    @FXML
    private TableColumn<Client, String> colEmail;
    @FXML
    private TextField nomField, prenomField, emailField;
    @FXML
    private Button btnAjouter, btnModifier, btnSupprimer, btnRetourAccueil;

    private IClient clientService;
    private ObservableList<Client> clientList = FXCollections.observableArrayList();

    /*public ClientController() {
        try (Connection connection = Db.getConnection()) {
            this.clientService = new ClientImpl(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    public ClientController() throws SQLException {
        this.clientService = new ClientImpl(Db.getConnection());
    }
    @FXML
    public void initialize() {
        if (colNom == null || colPrenom == null || colEmail == null) {
            System.out.println("⚠️ Une colonne est NULL !");
        }

        colNom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        colPrenom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        chargerClients();
    }

    private void chargerClients() {
        List<Client> clients = clientService.getTousLesClients();
        if (clients != null) {
            clientList.setAll(clients);
            tableClients.setItems(clientList);
        }
    }

    @FXML
    private void ajouterClient() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();

        if (!nom.isEmpty() && !prenom.isEmpty() && !email.isEmpty()) {
            clientService.ajouterClient(new Client(0, nom, prenom, email));
            chargerClients();
        }
        // Vider les champs après l'ajout
        viderChamps();
    }

    private void viderChamps() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
    }

    @FXML
    private void supprimerClient() {
        // Récupérer l'élément sélectionné dans la table
        Client selected = tableClients.getSelectionModel().getSelectedItem();

        // Vérifier si un client est sélectionné
        if (selected != null) {
            // Appeler le service pour supprimer le client
            clientService.supprimerClient(selected.getId());

            // Rafraîchir la table après la suppression
            chargerClients();
        }
    }


    @FXML
    private void modifierClient() {
        // Récupérer l'élément sélectionné dans la table
        Client selected = tableClients.getSelectionModel().getSelectedItem();

        // Vérifier si un client est sélectionné
        if (selected != null) {
            // Mettre à jour les informations du client à partir des champs
            selected.setNom(nomField.getText());
            selected.setPrenom(prenomField.getText());
            selected.setEmail(emailField.getText());

            // Appeler le service pour enregistrer les modifications
            clientService.modifierClient(selected);

            // Rafraîchir la table pour afficher les modifications
            chargerClients();

            // Effacer les champs après la modification
            nomField.clear();
            prenomField.clear();
            emailField.clear();
        }
    }

    @FXML
    private void handleTableSelection() {
        Client selected = tableClients.getSelectionModel().getSelectedItem();

        if (selected != null) {
            nomField.setText(selected.getNom());
            prenomField.setText(selected.getPrenom());
            emailField.setText(selected.getEmail());
        }

        setBtnState(selected != null);
    }

    private void setBtnState(boolean isSelected) {
        btnModifier.setDisable(!isSelected);
        btnSupprimer.setDisable(!isSelected);
    }

    @FXML
    private void retourAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRetourAccueil.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
