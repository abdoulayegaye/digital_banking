package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableRow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    
    private IClient clientDao = new ClientImpl();
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    
    @FXML
    private TextField nomTfd;
    
    @FXML
    private TextField prenomTfd;
    
    @FXML
    private TextField emailTfd;
    
    @FXML
    private TableView<Client> clientTable;
    
    @FXML
    private TableColumn<Client, Integer> idCol;
    
    @FXML
    private TableColumn<Client, String> nomCol;
    
    @FXML
    private TableColumn<Client, String> prenomCol;
    
    @FXML
    private TableColumn<Client, String> emailCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialisation du ClientController...");
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        idCol.setStyle("-fx-alignment: CENTER;");
        nomCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 10;");
        prenomCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 10;");
        emailCol.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 10;");
        
        clientTable.setStyle("-fx-font-size: 14px;");
        
        clientTable.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setStyle("-fx-background-color: transparent;");
            row.setOnMouseEntered(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #f0f0f0;");
                }
            });
            row.setOnMouseExited(event -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: transparent;");
                }
            });
            return row;
        });
        
        loadClients();
        System.out.println("Initialisation terminée");
    }
    
    @FXML
    void goToAccueil(ActionEvent event) throws IOException {
        Outils.load(event, "Digital Banking - Accueil", "/fxml/accueil.fxml");
    }
    
    @FXML
    void addClient(ActionEvent event) {
        String nom = nomTfd.getText().trim();
        String prenom = prenomTfd.getText().trim();
        String email = emailTfd.getText().trim();
        
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            return;
        }
        
        Client client = new Client(nom, prenom, email);
        try {
            clientDao.save(client);
            clearFields();
            loadClients();
            Notification.NotifSuccess("Succès", "Client ajouté avec succès");
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    
    @FXML
    void updateClient(ActionEvent event) {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client");
            return;
        }
        
        String nom = nomTfd.getText().trim();
        String prenom = prenomTfd.getText().trim();
        String email = emailTfd.getText().trim();
        
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            return;
        }
        
        selectedClient.setNom(nom);
        selectedClient.setPrenom(prenom);
        selectedClient.setEmail(email);
        
        try {
            clientDao.update(selectedClient);
            clearFields();
            loadClients();
            Notification.NotifSuccess("Succès", "Client modifié avec succès");
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    
    @FXML
    void deleteClient(ActionEvent event) {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un client");
            return;
        }
        
        try {
            clientDao.delete(selectedClient.getId());
            clearFields();
            loadClients();
            Notification.NotifSuccess("Succès", "Client supprimé avec succès");
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    
    @FXML
    void selectClient(javafx.scene.input.MouseEvent event) {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            nomTfd.setText(selectedClient.getNom());
            prenomTfd.setText(selectedClient.getPrenom());
            emailTfd.setText(selectedClient.getEmail());
        }
    }
    
    private void loadClients() {
        try {
            clients.clear();
            clients.addAll(clientDao.getAll());
            clientTable.setItems(clients);
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    
    private void clearFields() {
        nomTfd.clear();
        prenomTfd.clear();
        emailTfd.clear();
    }
}
