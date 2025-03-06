package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HistoriqueController {

    @FXML private TableView<Operation> tableHistorique;
    @FXML private TableColumn<Operation, LocalDateTime> colDate;
    @FXML private TableColumn<Operation, String> colDescription;
    @FXML private TableColumn<Operation, Double> colMontant, colSolde;
    @FXML private ComboBox<Compte> comboCompte;
    @FXML private Button btnRetour, btnAfficher, btnGenerer;

    private ICompte compteService = new CompteImpl();
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();
    private ObservableList<Operation> operationsList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        chargerComptes();
        comboCompte.setConverter(new StringConverter<Compte>() {
            @Override
            public String toString(Compte compte) {
                return compte == null ? "" : String.format("%s - %s %s", compte.getNumero(), compte.getClient().getPrenom(), compte.getClient().getNom());
            }
            @Override
            public Compte fromString(String string) {
                return null;
            }
        });
        configurerTableHistorique();
    }

    private void chargerComptes() {
        List<Compte> allComptes = compteService.getAllComptes();
        List<Compte> activeAttributedComptes = allComptes.stream()
                .filter(compte -> compte.isActif() && compte.getClient() != null)
                .collect(Collectors.toList());
        comptesList.setAll(activeAttributedComptes);
        comboCompte.setItems(comptesList);
    }

    private void configurerTableHistorique() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("solde"));
        tableHistorique.setItems(operationsList);
    }

    @FXML
    private void afficherHistorique(ActionEvent event) {
        Compte compteSelectionne = comboCompte.getSelectionModel().getSelectedItem();
        if (compteSelectionne != null) {
            List<Operation> operations = compteService.getOperations(compteSelectionne);
            operationsList.setAll(operations);
        } else {
            Outils.showError("Erreur", "Veuillez sélectionner un compte.");
        }
    }

    @FXML
    private void genererPdf(ActionEvent event) {
        Compte compteSelectionne = comboCompte.getSelectionModel().getSelectedItem();
        if (compteSelectionne == null) {
            Outils.showError("Erreur", "Veuillez sélectionner un compte.");
            return;
        }
        Dialog<Pair<LocalDate, LocalDate>> dialog = new Dialog<>();
        dialog.setTitle("Intervalle de Dates");
        dialog.setHeaderText("Veuillez sélectionner l'intervalle de dates");
        ButtonType generateButtonType = new ButtonType("Générer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(generateButtonType, ButtonType.CANCEL);
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Date de début:"), 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(new Label("Date de fin:"), 0, 1);
        grid.add(endDatePicker, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == generateButtonType) {
                return new Pair<>(startDatePicker.getValue(), endDatePicker.getValue());
            }
            return null;
        });
        Optional<Pair<LocalDate, LocalDate>> result = dialog.showAndWait();
        result.ifPresent(interval -> {
            LocalDate startDate = interval.getKey();
            LocalDate endDate = interval.getValue();
            if (ChronoUnit.MONTHS.between(startDate, endDate) < 1 || endDate.isAfter(LocalDate.now())) {
                Outils.showError("Erreur", "Intervalle invalide.");
                return;
            }
        });
    }

    @FXML
    private void retourGestionOperations(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Opérations", "/fxml/gestionOperations.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualiserHistorique() {
        afficherHistorique(new ActionEvent());
    }
}
