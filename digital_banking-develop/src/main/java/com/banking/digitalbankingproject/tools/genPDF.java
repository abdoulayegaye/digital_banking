package com.banking.digitalbankingproject.tools;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.banking.digitalbankingproject.tools.Notification;
import com.itextpdf.layout.element.Paragraph;
import lombok.Data;

import java.io.IOException;

public class genPDF{

public static void genpdf(String filename, Client client, Compte compte) {

    try {
        PdfWriter writer = new PdfWriter(filename);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("--------------------------BANK-DU-WALO------------------------------"));
        document.add(new Paragraph("Releve Bancaire"));
        document.add(new Paragraph("Nom: " + client.getNom()));
        document.add(new Paragraph("Email: " + client.getEmail()));
        document.add(new Paragraph("Compte: " + compte.getNumero()));

        document.add(new Paragraph("Solde: " + compte.getSolde() + " cfa"));
        document.add(new Paragraph("--------------------------merci de nous avoir choisi------------------------------"));

        // Fermer le document
        document.close();

        Notification.NotifSuccess("success","PDF généré avec succès  ");
    } catch ( IOException e) {
        System.err.println("Erreur lors de la génération du PDF : " + e.getMessage());
    }
}
}
