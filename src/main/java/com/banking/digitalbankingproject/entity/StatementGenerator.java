package com.banking.digitalbankingproject.entity;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.tools.Notification;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatementGenerator {

    public static void generateReleve(Compte compte, List<Operation> operations, LocalDate dateDebut, LocalDate dateFin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String filename = "releve_compte_" + compte.getNumero() + "_" + dateDebut + "_a_" + dateFin + ".pdf";

        Document document = new Document();
        try {
            // Créer un fichier PDF
            PdfWriter.getInstance(document, new FileOutputStream(filename));
            document.open();

            // Ajouter un en-tête avec le logo (remplacez "logo.png" par le chemin de votre logo)
            //Image logo = Image.getInstance("src/main/resources/images/logo.png"); // Chemin vers le logo
            //logo.scaleToFit(100, 100); // Redimensionner le logo
            //logo.setAlignment(Element.ALIGN_CENTER);
            //document.add(logo);

            // Titre du relevé
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("DIGITAL BANKING - RELEVÉ BANCAIRE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Informations du compte
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            document.add(new Paragraph("Nom et Prenom : " + compte.getClient().getNom() + " " +compte.getClient().getPrenom(), headerFont));
            document.add(new Paragraph("Compte : " + compte.getNumero(), headerFont));
            document.add(new Paragraph("Période : " + dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " à " + dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), contentFont));
            document.add(new Paragraph("Solde actuel : " + compte.getBalance() + " FRANS CFA", contentFont));
            document.add(new Paragraph(" ")); // Espacement

            // Tableau des opérations
            PdfPTable table = new PdfPTable(3); // 3 colonnes : Date, Type, Montant
            table.setWidthPercentage(100); // Largeur du tableau à 100%
            table.setSpacingBefore(10);
            table.setSpacingAfter(10);

            // En-têtes du tableau
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            PdfPCell cell;

            cell = new PdfPCell(new Paragraph("Date", tableHeaderFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Type", tableHeaderFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("Montant", tableHeaderFont));
            cell.setBackgroundColor(BaseColor.DARK_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            // Remplir le tableau avec les opérations
            for (Operation op : operations) {
                LocalDate opDate = op.getDateOp().atZone(ZoneId.systemDefault()).toLocalDate();
                if (!opDate.isBefore(dateDebut) && !opDate.isAfter(dateFin)) {
                    table.addCell(op.getDateOp().atZone(ZoneId.systemDefault()).format(formatter));
                    table.addCell(String.valueOf(op.getType()));
                    table.addCell(op.getAmount() + " FRANS CFA");
                }
            }

            document.add(table);

            // Pied de page
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, BaseColor.GRAY);
            Paragraph footer = new Paragraph("Merci de faire confiance à Digital Banking.", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(20);
            document.add(footer);

            document.close();
            Notification.NotifSuccess("Succès", "Relevé généré dans le fichier : " + filename);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Échec de la génération du relevé : " + e.getMessage());
        }
    }
}