package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
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
import java.util.Arrays;
import java.util.List;

public class OperationController {

    @FXML
    private TextField montantField;

    @FXML
    private ComboBox<TypeOperation> typeComboBox;

    @FXML
    private ComboBox<Compte> compteComboBox;

    @FXML
    private TableView<Operation> operationTable;

    @FXML
    private TableColumn<Operation, Integer> idColumn;

    @FXML
    private TableColumn<Operation, LocalDateTime> dateColumn;

    @FXML
    private TableColumn<Operation, Double> montantColumn;

    @FXML
    private TableColumn<Operation, TypeOperation> typeColumn;

    @FXML
    private TableColumn<Operation, Compte> compteColumn;

    private DatabaseService databaseService;

    @FXML
    private void initialize() {
        databaseService = new DatabaseService();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOp"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        compteColumn.setCellValueFactory(new PropertyValueFactory<>("compte"));
        typeComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(TypeOperation.values())));
        loadComptes();
        loadOperations();
        operationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                montantField.setText(String.valueOf(newSelection.getAmount()));
                typeComboBox.getSelectionModel().select(newSelection.getType());
                compteComboBox.getSelectionModel().select(newSelection.getCompte());
            }
        });
    }

    private void loadComptes() {
        try {
            List<Compte> comptes = databaseService.getAllComptes();
            compteComboBox.setItems(FXCollections.observableArrayList(comptes));
        } catch (SQLException e) {
            showError("Erreur lors du chargement des comptes : " + e.getMessage());
        }
    }

    private void loadOperations() {
        try {
            List<Operation> operations = databaseService.getAllOperations();
            operationTable.setItems(FXCollections.observableArrayList(operations));
        } catch (SQLException e) {
            showError("Erreur lors du chargement des opérations : " + e.getMessage());
        }
    }

    @FXML
    private void handleAjouterOperation() {
        String montantText = montantField.getText();
        TypeOperation type = typeComboBox.getSelectionModel().getSelectedItem();
        Compte compte = compteComboBox.getSelectionModel().getSelectedItem();

        if (montantText.isEmpty() || type == null || compte == null) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(montantText);
        } catch (NumberFormatException e) {
            showError("Le montant doit être un nombre valide.");
            return;
        }

        Operation operation = new Operation(0, LocalDateTime.now(), montant, type, compte);
        try {
            databaseService.addOperation(operation);
            loadOperations();
            clearFields();
        } catch (SQLException e) {
            showError("Erreur lors de l'ajout de l'opération : " + e.getMessage());
        }
    }

    @FXML
    private void handleModifierOperation() {
        Operation selectedOperation = operationTable.getSelectionModel().getSelectedItem();
        if (selectedOperation == null) {
            showError("Veuillez sélectionner une opération à modifier.");
            return;
        }

        String montantText = montantField.getText();
        TypeOperation type = typeComboBox.getSelectionModel().getSelectedItem();
        Compte compte = compteComboBox.getSelectionModel().getSelectedItem();

        if (montantText.isEmpty() || type == null || compte == null) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(montantText);
        } catch (NumberFormatException e) {
            showError("Le montant doit être un nombre valide.");
            return;
        }

        selectedOperation.setAmount(montant);
        selectedOperation.setType(type);
        selectedOperation.setCompte(compte);

        try {
            databaseService.updateOperation(selectedOperation);
            loadOperations();
            clearFields();
        } catch (SQLException e) {
            showError("Erreur lors de la modification de l'opération : " + e.getMessage());
        }
    }

    @FXML
    private void handleSupprimerOperation() {
        Operation selectedOperation = operationTable.getSelectionModel().getSelectedItem();
        if (selectedOperation == null) {
            showError("Veuillez sélectionner une opération à supprimer.");
            return;
        }

        try {
            databaseService.deleteOperation(selectedOperation.getId());
            loadOperations();
            clearFields();
        } catch (SQLException e) {
            showError("Erreur lors de la suppression de l'opération : " + e.getMessage());
        }
    }

    @FXML
    private void exportOperationsToPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le rapport PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File file = fileChooser.showSaveDialog(operationTable.getScene().getWindow());
            if (file != null) {
                String dest = file.getAbsolutePath();
                PdfWriter writer = new PdfWriter(dest);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Liste des Opérations").setFontSize(18).setBold());
                Table table = new Table(new float[]{50, 150, 100, 100, 150});
                table.addHeaderCell("ID");
                table.addHeaderCell("Date");
                table.addHeaderCell("Montant");
                table.addHeaderCell("Type");
                table.addHeaderCell("Compte");

                for (Operation operation : operationTable.getItems()) {
                    table.addCell(String.valueOf(operation.getId()));
                    table.addCell(operation.getDateOp().toString());
                    table.addCell(String.valueOf(operation.getAmount()));
                    table.addCell(operation.getType().toString());
                    table.addCell(operation.getCompte().getNumero());
                }

                document.add(table);
                document.close();
                showInfo("Succès", "Le rapport des opérations a été généré : " + dest);
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
            Stage stage = (Stage) operationTable.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException e) {
            showError("Erreur lors du chargement de la vue : " + e.getMessage());
        }
    }

    private void clearFields() {
        montantField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        compteComboBox.getSelectionModel().clearSelection();
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