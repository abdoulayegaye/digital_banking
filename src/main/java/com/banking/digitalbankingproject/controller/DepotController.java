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

public class DepotController {

    @FXML
    private ComboBox<Compte> compteComboBox;

    @FXML
    private TextField montantField;

    @FXML
    private Button imprimerButton;

    private DatabaseService databaseService;
    private Operation lastDepot;

    @FXML
    private void initialize() {
        databaseService = new DatabaseService();
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
    private void handleEffectuerDepot() {
        Compte compte = compteComboBox.getSelectionModel().getSelectedItem();
        String montantText = montantField.getText();

        if (compte == null || montantText.isEmpty()) {
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

        if (montant <= 0) {
            showError("Le montant doit être supérieur à 0.");
            return;
        }

        try {
            lastDepot = new Operation(0, LocalDateTime.now(), montant, TypeOperation.DEPOT, compte);
            databaseService.addOperation(lastDepot);
            showInfo("Succès", "Dépôt effectué avec succès !");
            imprimerButton.setDisable(false);
        } catch (SQLException e) {
            showError("Erreur lors du dépôt : " + e.getMessage());
        }
    }

    @FXML
    private void handleImprimerRecu() {
        if (lastDepot == null) {
            showError("Aucun dépôt à imprimer.");
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

                document.add(new Paragraph("Reçu de dépôt").setFontSize(18).setBold());
                document.add(new Paragraph("Compte : " + lastDepot.getCompte().getNumero()));
                document.add(new Paragraph("Montant : " + lastDepot.getAmount()));
                document.add(new Paragraph("Type : " + lastDepot.getType()));
                document.add(new Paragraph("Date : " + lastDepot.getDateOp().toString()));

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