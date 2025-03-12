package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);
                document.add(new Paragraph("Historique des opérations"));
                document.add(new Paragraph("Compte: " + compteSelectionne.getNumero()));
                document.add(new Paragraph("Client: " + compteSelectionne.getClient().getPrenom() + " " + compteSelectionne.getClient().getNom()));
                document.add(new Paragraph("Date de génération: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
                document.add(new Paragraph("\n"));
                Table table = new Table(4);
                table.addCell("Date");
                table.addCell("Description");
                table.addCell("Montant");
                table.addCell("Solde");
                List<Operation> operations = compteService.getOperations(compteSelectionne);
                for (Operation operation : operations) {
                    table.addCell(operation.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    table.addCell(operation.getDescription());
                    table.addCell(String.valueOf(operation.getMontant()));
                    table.addCell(String.valueOf(operation.getSolde()));
                }
                document.add(table);
                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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