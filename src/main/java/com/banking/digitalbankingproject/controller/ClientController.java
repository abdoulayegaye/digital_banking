package com.banking.digitalbankingproject.controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private final Db db = new Db();

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
    private TextField nomField, prenomField, emailField, rechercheField;

    @FXML
    private Button ajouterButton, modifierButton, supprimerButton, rechercherButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTable();

        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {


            if (newSelection != null) {
                chargerClientSelectionne();
            }
        });

    }

    public ObservableList<Client> getClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String sql = "SELECT * FROM clients ORDER BY nom ASC";

        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();

            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setEmail(rs.getString("email"));
                clients.add(c);
            }

            rs.close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de charger les clients : " + e.getMessage());
        }

        return clients;
    }

    public void loadTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        clientTable.setItems(getClients());
    }

    @FXML
    void ajouterClient(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs !");
            return;
        }

        String sql = "INSERT INTO clients (nom, prenom, email) VALUES (?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, nom);
            db.getPstm().setString(2, prenom);
            db.getPstm().setString(3, email);
            db.executeMaj();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Client ajouté avec succès !");
            clearFields();
            loadTable();
            redirectToComptes();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ajouter le client : " + e.getMessage());
        }
    }
    private void redirectToComptes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/comptes.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ajouterButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la page des comptes !");
        }
    }

    @FXML
    void modifierClient(ActionEvent event) {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();

        if (selectedClient == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un client à modifier !");
            return;
        }

        String nouveauNom = nomField.getText();
        String nouveauPrenom = prenomField.getText();
        String nouvelEmail = emailField.getText();

        if (nouveauNom.isEmpty() || nouveauPrenom.isEmpty() || nouvelEmail.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir tous les champs !");
            return;
        }

        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";

        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, nouveauNom);
            db.getPstm().setString(2, nouveauPrenom);
            db.getPstm().setString(3, nouvelEmail);
            db.getPstm().setInt(4, selectedClient.getId());
            db.executeMaj();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Client modifié avec succès !");
            clearFields();
            loadTable();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Impossible de modifier le client : " + e.getMessage());
        }
    }

    @FXML
    void rechercherClient(ActionEvent event) {
        String searchKeyword = rechercheField.getText().trim();
        if (searchKeyword.isEmpty()) {
            loadTable();
            return;
        }

        ObservableList<Client> filteredClients = FXCollections.observableArrayList();
        String sql = "SELECT * FROM clients WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, "%" + searchKeyword + "%");
            db.getPstm().setString(2, "%" + searchKeyword + "%");
            db.getPstm().setString(3, "%" + searchKeyword + "%");
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Client c = new Client();
                c.setId(rs.getInt("id"));
                c.setNom(rs.getString("nom"));
                c.setPrenom(rs.getString("prenom"));
                c.setEmail(rs.getString("email"));
                filteredClients.add(c);
            }
            rs.close();
            clientTable.setItems(filteredClients);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la recherche des clients : " + e.getMessage());
        }
    }

    @FXML
    void supprimerClient(ActionEvent event) {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un client à supprimer.");
            return;
        }

        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, selectedClient.getId());
            db.executeMaj();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Client supprimé avec succès !");
            clearFields();
            loadTable();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de supprimer le client : " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
    }

    private void chargerClientSelectionne() {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            nomField.setText(selectedClient.getNom());
            prenomField.setText(selectedClient.getPrenom());
            emailField.setText(selectedClient.getEmail());
        }
    }


    @FXML
    private Button homeButton; // Lier avec le bouton du FXML

    @FXML
    private void goToHome(ActionEvent event) {
        try {
            // Charger la page d'accueil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle et la remplacer
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir l'accueil !");
        }
    }


}
