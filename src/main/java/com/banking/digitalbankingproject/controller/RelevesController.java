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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class RelevesController {

    @FXML
    private ComboBox<Compte> compteComboBox;

    @FXML
    private TableView<Operation> relevesTable;

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
    private void loadReleves() {
        Compte selectedCompte = compteComboBox.getSelectionModel().getSelectedItem();
        if (selectedCompte != null) {
            try {
                List<Operation> operations = databaseService.getOperationsByCompte(selectedCompte);
                relevesTable.setItems(FXCollections.observableArrayList(operations));
            } catch (SQLException e) {
                showError("Erreur lors du chargement des relevés : " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleExporterPDF() {
        Compte selectedCompte = compteComboBox.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            showError("Veuillez sélectionner un compte.");
            return;
        }

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer les relevés PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File file = fileChooser.showSaveDialog(relevesTable.getScene().getWindow());
            if (file != null) {
                String dest = file.getAbsolutePath();
                PdfWriter writer = new PdfWriter(dest);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Relevés du compte : " + selectedCompte.getNumero()).setFontSize(18).setBold());
                Table table = new Table(new float[]{150, 100, 100});
                table.addHeaderCell("Date");
                table.addHeaderCell("Montant");
                table.addHeaderCell("Type");

                for (Operation operation : relevesTable.getItems()) {
                    table.addCell(operation.getDateOp().toString());
                    table.addCell(String.valueOf(operation.getAmount()));
                    table.addCell(operation.getType().toString());
                }

                document.add(table);
                document.close();
                showInfo("Succès", "Relevés exportés en PDF : " + dest);
            }
        } catch (Exception e) {
            showError("Erreur lors de l'exportation en PDF : " + e.getMessage());
        }
    }

    @FXML
    private void handleFermer() {
        Stage stage = (Stage) relevesTable.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}