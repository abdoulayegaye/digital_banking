package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class HistoriqueController {
    private int compteId;

    @FXML
    private Button retourBtn;
    @FXML
    private Button genererReleverBtn;
    @FXML
    private TableColumn<Operation, Double> amountCol;
    @FXML
    private TableColumn<Operation, String> dateCol;
    @FXML
    private TableView<Operation> historiqueTbl;
    @FXML
    private TableColumn<Operation, Integer> idCol;
    @FXML
    private TableColumn<Operation, String> numCompteCol;
    @FXML
    private Label clientLabel;
    @FXML
    private Label numCompteLabel;
    @FXML
    private Label typeCompteLabel;
    @FXML
    private TableColumn<Operation, String> typeOpCol;
    @FXML
    private Label dateDebutFxd;

    @FXML
    private Label dateFinFxd;

    @FXML
    void retour(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Comptes", "/FXML/interfaceComptes.fxml");
    }

    public void getData(Compte compte, String dateDebut, String dateFin) {
        if (compte != null) {
            this.compteId = compte.getId();
            numCompteLabel.setText(compte.getNumero());

            if (compte.getClient() != null) {
                clientLabel.setText(compte.getClient().getNom() + " " + compte.getClient().getPrenom());
            } else {
                clientLabel.setText("Aucun client associé");
            }

            if (compte.getTypeCompte() != null) {
                typeCompteLabel.setText(compte.getTypeCompte());
            } else {
                typeCompteLabel.setText("Type inconnu");
            }

            dateDebutFxd.setText(dateDebut);
            dateFinFxd.setText(dateFin);

            loadTable(dateDebut, dateFin);
        }
    }


    public void initialize() {
        LocalDate dateFin = LocalDate.now();
        LocalDate dateDebut = dateFin.minusDays(7);

        loadTable(dateDebut.toString(), dateFin.toString());
    }

    public void loadTable(String dateDebut, String dateFin) {
        IOperation iOperation = new OperationImpl();
        ObservableList<Operation> liste = FXCollections.observableArrayList(
                iOperation.consulterHistorique(compteId, dateDebut, dateFin)
        );

        historiqueTbl.setItems(liste);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                .withZone(ZoneId.systemDefault());

        dateCol.setCellValueFactory(cellData -> {
            Instant dateOp = cellData.getValue().getDateOp();
            String formattedDate = (dateOp != null) ? formatter.format(dateOp) : "N/A";
            return new SimpleStringProperty(formattedDate);
        });

        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeOpCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        /*numCompteCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCompte() != null ?
                                cellData.getValue().getCompte().getNumero() : "Non attribué"
                ));*/
    }

    @FXML
    void genererRelever(ActionEvent event) {
        if (historiqueTbl.getItems().isEmpty()) {
            Notification.NotifError("Erreur", "⚠ Aucun historique à générer !");
            return;
        }

        // Boîte de dialogue pour enregistrer le fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le relevé");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        fileChooser.setInitialFileName("Releve.pdf");

        Stage stage = (Stage) genererReleverBtn.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) return;

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            //Table pour le logo et l'adresse
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 2});

            try {
                String imagePath = "src/main/resources/images/Digital-Banking.png";
                Image logo = Image.getInstance(imagePath);
                logo.scaleToFit(80, 80);
                PdfPCell cellLogo = new PdfPCell(logo, false);
                cellLogo.setBorder(Rectangle.NO_BORDER);
                cellLogo.setHorizontalAlignment(Element.ALIGN_LEFT);
                headerTable.addCell(cellLogo);
                Paragraph nomBanque = new Paragraph("DIGITAL BANKING ");
                nomBanque.setAlignment(Element.ALIGN_LEFT);
            } catch (Exception e) {
                System.err.println("⚠ Erreur : Logo introuvable.");
                headerTable.addCell(new PdfPCell(new Phrase("DIGITAL BANKING")));
            }

            // Adresse à droite
            Font fontAdresse = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
            Paragraph adresse = new Paragraph("Avenue Georges Pompidou, Dakar, Sénégal\nTel: +221 33 820 12 34 - Fax: +221 33 820 12 35", fontAdresse);
            PdfPCell cellAdresse = new PdfPCell(adresse);
            cellAdresse.setBorder(Rectangle.NO_BORDER);
            cellAdresse.setHorizontalAlignment(Element.ALIGN_RIGHT);
            headerTable.addCell(cellAdresse);

            document.add(headerTable);

            //Ajout du titre principal
            Font fontTitre = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph titre = new Paragraph("\nRELEVÉ DE COMPTE\n", fontTitre);
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);

            Font fontBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Paragraph periode = new Paragraph("Du " + dateDebutFxd.getText() + " au " + dateFinFxd.getText(), fontBold);
            periode.setAlignment(Element.ALIGN_CENTER);
            document.add(periode);

            // Ajout du nom d'agence
            Font fontTitreAg = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.DARK_GRAY);
            Paragraph titreAg = new Paragraph("\nAgence Zac Mbao\n", fontTitreAg);
            titreAg.setAlignment(Element.ALIGN_LEFT);
            document.add(titreAg);
            Paragraph adresseZac = new Paragraph("Tel: +221 33 820 10 10 - Fax: +221 33 820 11 11", fontAdresse);
            adresseZac.setAlignment(Element.ALIGN_LEFT);
            document.add(adresseZac);

            //Infos du compte et client
            document.add(new Paragraph("Compte : " + numCompteLabel.getText()));
            document.add(new Paragraph("Type de Compte : " + typeCompteLabel.getText()));
            document.add(new Paragraph("Titulaire : " + clientLabel.getText()));
            document.add(new Paragraph("Gestionnaire : M. Abdou Ndiaye\n\n"));

            //Création du tableau
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            //En-têtes du tableau
            String[] headers = {"ID", "Date", "Montant", "Type"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            //Remplissage des lignes
            ObservableList<Operation> operations = historiqueTbl.getItems();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.systemDefault());

            for (Operation op : operations) {
                table.addCell(String.valueOf(op.getId()));
                table.addCell(op.getDateOp() != null ? formatter.format(op.getDateOp()) : "N/A");
                table.addCell(String.format("%.2f F CFA", op.getAmount()));
                table.addCell(op.getType().name());
            }

            document.add(table);

            // Calcul des totaux débit et crédit
            double[] totaux = calculerTotaux();
            double totalDebit = totaux[0];
            double totalCredit = totaux[1];

            // Ajout des totaux sous le tableau
            Paragraph totalParagraph = new Paragraph("\nTotal Débit : " + String.format("%.2f F CFA", totalDebit) +
                    "\nTotal Crédit : " + String.format("%.2f F CFA", totalCredit),
                    new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParagraph);

            //Texte en bas du relevé
            Font fontPied = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.DARK_GRAY);
            Paragraph texteBas = new Paragraph("\nCe relevé est généré électroniquement et ne nécessite pas de signature.\nVeuillez contacter votre agence en cas de question.", fontPied);
            texteBas.setAlignment(Element.ALIGN_CENTER);
            document.add(texteBas);

            document.close();

            Notification.NotifSuccess("Succès", "Relevé Bancaire généré avec succès !");

        } catch (Exception e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Erreur lors de la génération du Relevé Bancaire.");
        }
    }

    private double[] calculerTotaux() {
        double totalDebit = 0;
        double totalCredit = 0;

        ObservableList<Operation> operations = historiqueTbl.getItems();

        for (Operation op : operations) {
            if (op.getType().equals(TypeOperation.RETRAIT) ||
                    op.getType().equals(TypeOperation.VIREMENT_SORTANT)) {
                totalDebit += op.getAmount();
            }
            else if (op.getType().equals(TypeOperation.DEPOT) ||
                    op.getType().equals(TypeOperation.VIREMENT_ENTRANT) ||
                    op.getType().equals(TypeOperation.VERSEMENT)) {
                totalCredit += op.getAmount();
            }
        }

        return new double[]{totalDebit, totalCredit};
    }
}
