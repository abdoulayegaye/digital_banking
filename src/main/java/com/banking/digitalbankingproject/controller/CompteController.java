package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
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
import java.time.LocalDateTime;
import java.util.List;

public class CompteController {

    @FXML
    private TextField numeroField;

    @FXML
    private TextField balanceField;

    @FXML
    private ComboBox<Client> clientComboBox;

    @FXML
    private TableView<Compte> compteTable;

    @FXML
    private TableColumn<Compte, Integer> idColumn;

    @FXML
    private TableColumn<Compte, String> numeroColumn;

    @FXML
    private TableColumn<Compte, Double> balanceColumn;

    @FXML
    private TableColumn<Compte, Client> clientColumn;

    private DatabaseService databaseService;

    @FXML
    private void initialize() {
        databaseService = new DatabaseService();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        loadClients();
        loadComptes();
        compteTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                numeroField.setText(newSelection.getNumero());
                balanceField.setText(String.valueOf(newSelection.getBalance()));
                clientComboBox.getSelectionModel().select(newSelection.getClient());
            }
        });
    }

    private void loadClients() {
        try {
            List<Client> clients = databaseService.getAllClients();
            clientComboBox.setItems(FXCollections.observableArrayList(clients));
        } catch (SQLException e) {
            showError("Erreur lors du chargement des clients : " + e.getMessage());
        }
    }

    private void loadComptes() {
        try {
            List<Compte> comptes = databaseService.getAllComptes();
            compteTable.setItems(FXCollections.observableArrayList(comptes));
        } catch (SQLException e) {
            showError("Erreur lors du chargement des comptes : " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterCompte() {
        String numero = numeroField.getText();
        String balanceText = balanceField.getText();
        Client client = clientComboBox.getSelectionModel().getSelectedItem();

        if (numero.isEmpty() || balanceText.isEmpty() || client == null) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceText);
        } catch (NumberFormatException e) {
            showError("Le solde doit être un nombre valide.");
            return;
        }

        Compte compte = new Compte(0, numero, balance, LocalDateTime.now(), client);
        try {
            databaseService.addCompte(compte);
            loadComptes();
            clearFields();
        } catch (SQLException e) {
            showError("Erreur lors de l'ajout du compte : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifierCompte() {
        Compte selectedCompte = compteTable.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            showError("Veuillez sélectionner un compte à modifier.");
            return;
        }

        String numero = numeroField.getText();
        String balanceText = balanceField.getText();
        Client client = clientComboBox.getSelectionModel().getSelectedItem();

        if (numero.isEmpty() || balanceText.isEmpty() || client == null) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceText);
        } catch (NumberFormatException e) {
            showError("Le solde doit être un nombre valide.");
            return;
        }

        selectedCompte.setNumero(numero);
        selectedCompte.setBalance(balance);
        selectedCompte.setClient(client);

        try {
            databaseService.updateCompte(selectedCompte);
            loadComptes();
            clearFields();
        } catch (SQLException e) {
            showError("Erreur lors de la modification du compte : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimerCompte() {
        Compte selectedCompte = compteTable.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            showError("Veuillez sélectionner un compte à supprimer.");
            return;
        }

        try {
            databaseService.deleteCompte(selectedCompte.getId());
            loadComptes();
            clearFields();
        } catch (SQLException e) {
            showError("Erreur lors de la suppression du compte : " + e.getMessage());
        }
    }

    @FXML
    private void exportComptesToPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le rapport PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File file = fileChooser.showSaveDialog(compteTable.getScene().getWindow());
            if (file != null) {
                String dest = file.getAbsolutePath();
                PdfWriter writer = new PdfWriter(dest);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Liste des Comptes").setFontSize(18).setBold());
                Table table = new Table(new float[]{50, 150, 100, 200});
                table.addHeaderCell("ID");
                table.addHeaderCell("Numéro");
                table.addHeaderCell("Solde");
                table.addHeaderCell("Client");

                for (Compte compte : compteTable.getItems()) {
                    table.addCell(String.valueOf(compte.getId()));
                    table.addCell(compte.getNumero());
                    table.addCell(String.valueOf(compte.getBalance()));
                    table.addCell(compte.getClient().getNom() + " " + compte.getClient().getPrenom());
                }

                document.add(table);
                document.close();
                showInfo("Succès", "Le rapport des comptes a été généré : " + dest);
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
            Stage stage = (Stage) compteTable.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la vue : " + e.getMessage());
        }
    }

    private void clearFields() {
        numeroField.clear();
        balanceField.clear();
        clientComboBox.getSelectionModel().clearSelection();
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