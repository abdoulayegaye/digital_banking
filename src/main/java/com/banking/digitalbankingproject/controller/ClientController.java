package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.impl.DatabaseService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ClientController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

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

    private DatabaseService databaseService;

    @FXML
    private void initialize() {
        databaseService = new DatabaseService();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        loadClients();
        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nomField.setText(newSelection.getNom());
                prenomField.setText(newSelection.getPrenom());
                emailField.setText(newSelection.getEmail());
            }
        });
    }

    private void loadClients() {
        try {
            List<Client> clients = databaseService.getAllClients();
            clientTable.setItems(FXCollections.observableArrayList(clients));
        } catch (SQLException e) {
            showError("Erreur lors du chargement des clients : " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterClient() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        Client client = new Client(0, nom, prenom, email);
        try {
            databaseService.addClient(client);
            loadClients();
            clearFields();
        } catch (SQLException e) {
            showError("Erreur lors de l'ajout du client : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifierClient() {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showError("Veuillez sélectionner un client à modifier.");
            return;
        }

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        selectedClient.setNom(nom);
        selectedClient.setPrenom(prenom);
        selectedClient.setEmail(email);

        try {
            databaseService.updateClient(selectedClient);
            loadClients();
            clearFields();
        } catch (SQLException e) {
            showError("Erreur lors de la modification du client : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimerClient() {
        Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showError("Veuillez sélectionner un client à supprimer.");
            return;
        }

        try {
            databaseService.deleteClient(selectedClient.getId());
            loadClients();
            clearFields();
        } catch (SQLException e) {
            showError("Erreur lors de la suppression du client : " + e.getMessage());
        }
    }

    @FXML
    private void exportClientsToPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le rapport PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File file = fileChooser.showSaveDialog(clientTable.getScene().getWindow());
            if (file != null) {
                String dest = file.getAbsolutePath();
                PdfWriter writer = new PdfWriter(dest);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Liste des Clients").setFontSize(18).setBold());
                Table table = new Table(new float[]{50, 150, 150, 200});
                table.addHeaderCell("ID");
                table.addHeaderCell("Nom");
                table.addHeaderCell("Prénom");
                table.addHeaderCell("Email");

                for (Client client : clientTable.getItems()) {
                    table.addCell(String.valueOf(client.getId()));
                    table.addCell(client.getNom());
                    table.addCell(client.getPrenom());
                    table.addCell(client.getEmail());
                }

                document.add(table);
                document.close();
                showInfo("Succès", "Le rapport des clients a été généré : " + dest);
            }
        } catch (Exception e) {
            showError("Erreur lors de l'exportation en PDF : " + e.getMessage());
        }
    }

    @FXML
    private void handleRetour() {
        loadView("MainView.fxml", "Banque AM International");
    }

    private void loadView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Stage stage = (Stage) clientTable.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la vue : " + e.getMessage());
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}