package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    private Db db = new Db();
    private boolean isTableVisible = true;
    private int IdClient;

    @FXML private VBox clientForm;
    @FXML private TableView<Client> clientsTable;
    @FXML private TableColumn<Client, String> emailCol;
    @FXML private TextField emailField;
    @FXML private TableColumn<Client, String> firstNameCol;
    @FXML private TextField firstNameField;
    @FXML private TableColumn<Client, Integer> idCol;
    @FXML private TableColumn<Client, String> lastNameCol;
    @FXML private TextField lastNameField;
    @FXML private Button CancelBtn;
    @FXML private Button DeleteBtn;
    @FXML private Button EffacerBtn;
    @FXML private Button NewClientBtn;
    @FXML private Button SaveBtn;
    @FXML private Button UpdateBtn;
    @FXML private TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LoadTable();
        updateButtonStates(true);
    }


    public void toggleForm() {
        isTableVisible = !isTableVisible;
        clientsTable.setVisible(isTableVisible);
        clientForm.setVisible(!isTableVisible);
    }

    public void saveClient() {
        if (!validateInput()) return;
        String sql = "INSERT INTO clients VALUES(NULL, ?, ?, ?)";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, firstNameField.getText());
            db.getPstm().setString(2, lastNameField.getText());
            db.getPstm().setString(3, emailField.getText());
            db.executeMaj();
            LoadTable();
            clearForm();
            cancelForm();

            Notification.NotifSuccess("Ajout Client", "Client saved successfully");
        } catch (SQLException e) {
            Notification.NotifError("Erreur", "Échec de l'enregistrement : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }

    public void cancelForm() {
        toggleForm();
        clearForm();
        updateButtonStates(true);
    }

    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
    }

    @FXML
    void AddClient(ActionEvent event) {
        toggleForm();
        updateButtonStates(false);
        SaveBtn.setDisable(false);
        DeleteBtn.setDisable(true);
        NewClientBtn.setDisable(true);
        UpdateBtn.setDisable(true);
    }

    // Delete selected client
    @FXML
    void DeleteClient(ActionEvent event) {
        String sql = "DELETE FROM clients WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, IdClient);
            int ok = db.executeMaj();
            LoadTable();
            clearForm();
            cancelForm();
            if (ok == 1) {
                Notification.NotifSuccess("Suppression", "Client deleted successfully");
            }
        } catch (SQLException e) {
            Notification.NotifError("Erreur", "Échec de la suppression : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }

    // Reset form
    @FXML
    void ListClient(ActionEvent event) {
        clearForm();
    }

    // Update selected client
    @FXML
    void UpdateClient(ActionEvent event) {
        if (!validateInput()) return;
        String sql = "UPDATE clients SET nom = ?, prenom = ?, email = ? WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, firstNameField.getText());
            db.getPstm().setString(2, lastNameField.getText());
            db.getPstm().setString(3, emailField.getText());
            db.getPstm().setInt(4, IdClient);
            int ok = db.executeMaj();
            LoadTable();
            clearForm();
            cancelForm();
            if (ok == 1) {
                Notification.NotifSuccess("Modification", "Client updated successfully");
            }
        } catch (SQLException e) {
            Notification.NotifError("Erreur", "Échec de la modification : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
    }

    // Load selected client data into form
    @FXML
    void getData(MouseEvent event) {
        Client client = clientsTable.getSelectionModel().getSelectedItem();
        if (client != null) {
            toggleForm();
            IdClient = client.getId();
            firstNameField.setText(client.getNom());
            lastNameField.setText(client.getPrenom());
            emailField.setText(client.getEmail());
            updateButtonStates(false);
            DeleteBtn.setDisable(false);
            UpdateBtn.setDisable(false);
            SaveBtn.setDisable(true);
        }
    }

    // Filter clients based on search input
    @FXML
    void filterClients(ActionEvent event) {
        String searchText = searchField.getText().trim().toLowerCase();
        ObservableList<Client> filteredList = FXCollections.observableArrayList();
        for (Client client : getClients()) {
            if (client.getNom().toLowerCase().contains(searchText) ||
                    client.getPrenom().toLowerCase().contains(searchText) ||
                    client.getEmail().toLowerCase().contains(searchText)) {
                filteredList.add(client);
            }
        }
        clientsTable.setItems(filteredList);
    }

    // Load all clients into table
    public void LoadTable() {
        clientsTable.setItems(getClients());
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    // Retrieve clients from database
    public ObservableList<Client> getClients() {
        ObservableList<Client> clients = FXCollections.observableArrayList();
        String sql = "SELECT * FROM clients ORDER BY nom ASC";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect(sql);
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
                clients.add(client);
            }
        } catch (SQLException e) {
            Notification.NotifError("Erreur", "Échec du chargement des clients : " + e.getMessage());
        } finally {
            db.closeConnection();
        }
        return clients;
    }

    // Validate form input
    private boolean validateInput() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                !emailField.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Notification.NotifError("Validation", "Veuillez remplir tous les champs correctement (email invalide).");
            return false;
        }
        return true;
    }

    // Update button states
    private void updateButtonStates(boolean isNewClientMode) {
        NewClientBtn.setDisable(!isNewClientMode);
        SaveBtn.setDisable(!isNewClientMode);
        UpdateBtn.setDisable(isNewClientMode);
        DeleteBtn.setDisable(isNewClientMode);
    }

    // Return to home screen
    @FXML
    public void Retour(ActionEvent event) throws IOException {
        Outils.load(event, "Deconnexion", "/fxml/accueil.fxml");
    }
}