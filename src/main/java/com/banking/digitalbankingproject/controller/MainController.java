package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
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

public class MainController {

    @FXML
    private Button gestionClientsButton;

    @FXML
    private Button gestionComptesButton;

    @FXML
    private Button transactionsButton;

    @FXML
    private ComboBox<Compte> compteComboBox;

    @FXML
    private TableView<Operation> transactionTable;

    @FXML
    private TableColumn<Operation, String> dateColumn;

    @FXML
    private TableColumn<Operation, Double> montantColumn;

    @FXML
    private TableColumn<Operation, String> typeColumn;

    private DatabaseService databaseService;

    @FXML
    private void initialize() {
        databaseService = new DatabaseService();
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOp"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        loadComptes();
    }

    private void loadComptes() {
        try {
            List<Compte> comptes = databaseService.getAllComptes();
            compteComboBox.setItems(FXCollections.observableArrayList(comptes));
        } catch (SQLException e) {
            showError("Erreur lors du chargement des comptes : " + e.getMessage());
        }
    }

    @FXML
    private void loadTransactionsForCompte() {
        Compte selectedCompte = compteComboBox.getSelectionModel().getSelectedItem();
        if (selectedCompte != null) {
            try {
                List<Operation> operations = databaseService.getOperationsByCompte(selectedCompte);
                transactionTable.setItems(FXCollections.observableArrayList(operations));
            } catch (SQLException e) {
                showError("Erreur lors du chargement des opérations : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleExporterHistoriquePDF() {
        Compte selectedCompte = compteComboBox.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            showError("Veuillez sélectionner un compte.");
            return;
        }

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer l'historique PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File file = fileChooser.showSaveDialog(gestionClientsButton.getScene().getWindow());
            if (file != null) {
                String dest = file.getAbsolutePath();
                PdfWriter writer = new PdfWriter(dest);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Historique des transactions - Compte : " + selectedCompte.getNumero()).setFontSize(18).setBold());
                Table table = new Table(new float[]{150, 100, 100});
                table.addHeaderCell("Date");
                table.addHeaderCell("Montant");
                table.addHeaderCell("Type");

                for (Operation operation : transactionTable.getItems()) {
                    table.addCell(operation.getDateOp().toString());
                    table.addCell(String.valueOf(operation.getAmount()));
                    table.addCell(operation.getType().toString());
                }

                document.add(table);
                document.close();
                showInfo("Succès", "Historique exporté en PDF : " + dest);
            }
        } catch (Exception e) {
            showError("Erreur lors de l'exportation en PDF : " + e.getMessage());
        }
    }

    @FXML
    private void handleAccueil() {
        loadView("accueil.fxml", "Accueil - Banque AM International");
    }

    @FXML
    private void handleGestionClients() {
        loadView("clients.fxml", "Gestion des Clients");
    }

    @FXML
    private void handleGestionComptes() {
        loadView("comptes.fxml", "Gestion des Comptes");
    }

    @FXML
    private void handleTransactions() {
        loadView("operations.fxml", "Gestion des Opérations");
    }

    @FXML
    private void handleFaireVirement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/virement.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Effectuer un virement");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du chargement de la vue : " + e.getMessage());
        }
    }

    @FXML
    private void handleDeposerArgent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/depot.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Déposer de l'argent");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du chargement de la vue : " + e.getMessage());
        }
    }

    @FXML
    private void handleConsulterReleves() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/releves.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Consulter les relevés");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            showError("Erreur lors du chargement de la vue : " + e.getMessage());
        }
    }

    @FXML
    private void handleDeconnexion() {
        loadView("login.fxml", "Connexion");
    }

    private void loadView(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Stage stage = (Stage) gestionClientsButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de la vue : " + e.getMessage());
        }
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