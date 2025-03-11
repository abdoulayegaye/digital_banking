package com.banking.digitalbankingproject.controller;

import javafx.scene.control.cell.PropertyValueFactory;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
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
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import java.io.File;
import java.io.FileOutputStream;
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
                return compte == null ? "" : String.format("%s - %s %s", compte.getNumero(),
                        compte.getClient().getPrenom(), compte.getClient().getNom());
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
        String fileName = "DB_" + compteSelectionne.getNumero() + "_" +
                compteSelectionne.getClient().getPrenom() + "_" +
                compteSelectionne.getClient().getNom() + ".pdf";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le relevé");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName(fileName);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                genererPdfReleve(file, compteSelectionne, operationsList);
                Outils.showSuccess("Succès", "Le relevé a été généré avec succès.");
            } catch (IOException e) {
                Outils.showError("Erreur", "Erreur lors de la génération du relevé.");
            }
        }
    }

    private void genererPdfReleve(File file, Compte compte, List<Operation> operations) throws IOException {
        PdfWriter writer = new PdfWriter(new FileOutputStream(file));
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Ajout du logo
        String logoPath = getClass().getResource("/images/logo.png").toExternalForm();
        Image logo = new Image(com.itextpdf.io.image.ImageDataFactory.create(logoPath));
        logo.setFixedPosition(20, 750);
        logo.scaleToFit(50, 50);
        document.add(logo);

        // Nom de la banque
        document.add(new Paragraph("Digital Banking").setBold().setFontSize(18).setTextAlignment(TextAlignment.LEFT).setMarginTop(-40));

        // Adresse
        document.add(new Paragraph("KM1 Route de Joal\nMbour, Thiès\nSénégal").setTextAlignment(TextAlignment.RIGHT));

        // Informations du compte avec le client
        document.add(new Paragraph("Relevé de compte").setBold().setFontSize(14));
        document.add(new Paragraph("Compte: " + compte.getNumero()));
        document.add(new Paragraph("Client: " + compte.getClient().getPrenom() + " " + compte.getClient().getNom() + " - " + compte.getClient().getEmail()));
        document.add(new Paragraph("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        // Tableau des opérations
        float[] columnWidths = {150F, 150F, 150F, 150F};
        Table table = new Table(columnWidths);
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Date")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Description")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Montant")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new com.itextpdf.layout.element.Cell().add(new Paragraph("Solde")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        double totalMontant = 0;
        double soldeFinal = 0;
        for (Operation operation : operations) {
            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(operation.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))));
            String description = operation.getDescription();
            if (description.toLowerCase().contains("virement")) {
                description += operation.getMontant() > 0 ? " (Virement entrant)" : " (Virement sortant)";
            }
            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(description)));
            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(operation.getMontant()))));
            table.addCell(new com.itextpdf.layout.element.Cell().add(new Paragraph(String.valueOf(operation.getSolde()))));
            totalMontant += operation.getMontant();
            soldeFinal = operation.getSolde();
        }
        document.add(table);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Total des opérations: " + totalMontant).setBold());
        document.add(new Paragraph("Solde final: " + soldeFinal).setBold());
        document.add(new Paragraph("\n\nSignature du Directeur:").setBold().setMarginTop(50));
        document.add(new Paragraph("____________________________").setMarginTop(10));

        Barcode128 barcode = new Barcode128(pdfDoc);
        barcode.setCode(compte.getNumero());
        Image barcodeImage = new Image(barcode.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdfDoc));
        barcodeImage.setWidth(200);
        barcodeImage.setHeight(50);
        barcodeImage.setFixedPosition(pdfDoc.getDefaultPageSize().getWidth() - 250, 20);
        document.add(barcodeImage);

        document.close();
    }

    @FXML
    private void retourGestionOperations(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Opérations", "/fxml/gestionOperations.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de retourner à la page Gestion des Opérations.");
        }
    }

    public void actualiserHistorique() {
        afficherHistorique(new ActionEvent());
    }
}
