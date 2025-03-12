package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.impl.DatabaseService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class VirementController {

    @FXML
    private ComboBox<Compte> compteSourceComboBox;

    @FXML
    private ComboBox<Compte> compteDestComboBox;

    @FXML
    private TextField montantField;

    @FXML
    private Button imprimerButton;

    private DatabaseService databaseService;
    private Operation lastRetrait;
    private Operation lastDepot;

    @FXML
    private void initialize() {
        databaseService = new DatabaseService();
        loadComptes();
    }

    private void loadComptes() {
        try {
            List<Compte> comptes = databaseService.getAllComptes();
            compteSourceComboBox.setItems(FXCollections.observableArrayList(comptes));
            compteDestComboBox.setItems(FXCollections.observableArrayList(comptes));
        } catch (SQLException e) {
            showError("Erreur lors du chargement des comptes : " + e.getMessage());
        }
    }

    @FXML
    private void handleEffectuerVirement() {
        Compte compteSource = compteSourceComboBox.getSelectionModel().getSelectedItem();
        Compte compteDest = compteDestComboBox.getSelectionModel().getSelectedItem();
        String montantText = montantField.getText();

        if (compteSource == null || compteDest == null || montantText.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        if (compteSource.equals(compteDest)) {
            showError("Le compte source et le compte destinataire doivent être différents.");
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(montantText);
        } catch (NumberFormatException e) {
            showError("Le montant doit être un nombre valide.");
            return;
        }

        if (montant <= 0) {
            showError("Le montant doit être supérieur à 0.");
            return;
        }

        if (compteSource.getBalance() < montant) {
            showError("Solde insuffisant sur le compte source.");
            return;
        }

        try {
            lastRetrait = new Operation(0, LocalDateTime.now(), montant, TypeOperation.RETRAIT, compteSource);
            databaseService.addOperation(lastRetrait);

            lastDepot = new Operation(0, LocalDateTime.now(), montant, TypeOperation.DEPOT, compteDest);
            databaseService.addOperation(lastDepot);

            showInfo("Succès", "Virement effectué avec succès !");
            imprimerButton.setDisable(false);
        } catch (SQLException e) {
            showError("Erreur lors du virement : " + e.getMessage());
        }
    }

    @FXML
    private void handleImprimerRecu() {
        if (lastRetrait == null || lastDepot == null) {
            showError("Aucun virement à imprimer.");
            return;
        }

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le reçu PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
            File file = fileChooser.showSaveDialog(imprimerButton.getScene().getWindow());
            if (file != null) {
                String dest = file.getAbsolutePath();
                PdfWriter writer = new PdfWriter(dest);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Reçu de virement").setFontSize(18).setBold());
                document.add(new Paragraph("Compte source : " + lastRetrait.getCompte().getNumero()));
                document.add(new Paragraph("Compte destinataire : " + lastDepot.getCompte().getNumero()));
                document.add(new Paragraph("Montant : " + lastRetrait.getAmount()));
                document.add(new Paragraph("Date : " + lastRetrait.getDateOp().toString()));

                document.close();
                showInfo("Succès", "Reçu généré : " + dest);
                closeWindow();
            }
        } catch (Exception e) {
            showError("Erreur lors de l'impression du reçu : " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) montantField.getScene().getWindow();
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