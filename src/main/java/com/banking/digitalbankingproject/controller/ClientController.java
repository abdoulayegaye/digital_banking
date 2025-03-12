package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ClientController {
    
    @FXML
    private BorderPane mainContainer;
    
    @FXML
    private TextField nomField;
    
    @FXML
    private TextField prenomField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField telephoneField;
    
    @FXML
    private TextArea adresseField;
    
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
    private TableColumn<Client, String> telephoneColumn;
    
    private ClientImpl clientService;
    private ObservableList<Client> clientData = FXCollections.observableArrayList();
    private Client selectedClient;
    
    @FXML
    public void initialize() {
        clientService = new ClientImpl();
        
        // Configurer les colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        
        // Sélection de client
        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedClient = newSelection;
                nomField.setText(selectedClient.getNom());
                prenomField.setText(selectedClient.getPrenom());
                emailField.setText(selectedClient.getEmail());
                telephoneField.setText(selectedClient.getTelephone());
                adresseField.setText(selectedClient.getAdresse());
            }
        });
        
        // Charger les clients
        actualiserClients();
    }
    
    @FXML
    public void ajouterClient() {
        if (validateInput()) {
            try {
                Client client = new Client();
                client.setNom(nomField.getText());
                client.setPrenom(prenomField.getText());
                client.setEmail(emailField.getText());
                client.setTelephone(telephoneField.getText());
                client.setAdresse(adresseField.getText());
                
                boolean success = clientService.add(client);
                
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Client ajouté avec succès");
                    viderFormulaire();
                    actualiserClients();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout du client");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue: " + e.getMessage());
            }
        }
    }
    
    @FXML
    public void supprimerClient() {
        selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez sélectionner un client à supprimer");
            return;
        }
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation");
        confirmDialog.setHeaderText("Supprimer le client");
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer ce client?");
        
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean success = clientService.delete(selectedClient.getId());
                
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Client supprimé avec succès");
                    viderFormulaire();
                    actualiserClients();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la suppression du client");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue: " + e.getMessage());
            }
        }
    }
    
    @FXML
    public void actualiserClients() {
        try {
            List<Client> clients = clientService.getAll();
            clientData.clear();
            clientData.addAll(clients);
            clientTable.setItems(clientData);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les clients: " + e.getMessage());
        }
    }
    
    @FXML
    public void viderFormulaire() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
        adresseField.clear();
        selectedClient = null;
        clientTable.getSelectionModel().clearSelection();
    }
    
    private boolean validateInput() {
        StringBuilder errorMsg = new StringBuilder();
        
        if (nomField.getText().isEmpty()) {
            errorMsg.append("Le nom est requis\n");
        }
        
        if (prenomField.getText().isEmpty()) {
            errorMsg.append("Le prénom est requis\n");
        }
        
        if (emailField.getText().isEmpty()) {
            errorMsg.append("L'email est requis\n");
        } else if (!emailField.getText().matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            errorMsg.append("Format d'email invalide\n");
        }
        
        if (telephoneField.getText().isEmpty()) {
            errorMsg.append("Le téléphone est requis\n");
        }
        
        if (errorMsg.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation", errorMsg.toString());
            return false;
        }
        
        return true;
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    public void retourAccueil() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/fxml/accueil.fxml"));
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void deconnexion() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
