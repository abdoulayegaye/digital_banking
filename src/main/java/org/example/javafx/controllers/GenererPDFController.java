package org.example.javafx.controllers;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.javafx.dao.DBConnexion;
import org.example.javafx.entities.Client;
import org.example.javafx.entities.Compte;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GenererPDFController {

    @FXML
    private TextField numeroCompteField;

    @FXML
    private Label resultLabel; // Label pour afficher le résultat

    private DBConnexion db = new DBConnexion();

    // Méthode pour générer le PDF
    @FXML
    private void genererPDF() {
        String numeroCompte = numeroCompteField.getText(); // Récupérer le numéro de compte saisi

        // Vérifier si le champ est vide
        if (numeroCompte.isEmpty()) {
            resultLabel.setText("Veuillez saisir un numéro de compte.");
            return;
        }

        // Récupérer les informations du compte depuis la base de données
        Compte compte = getCompteFromDatabase(numeroCompte);

        if (compte != null) {
            // Générer le PDF
            boolean success = generatePDF(compte);
            if (success) {
                resultLabel.setText("Relevé généré avec succès !");
            } else {
                resultLabel.setText("Erreur lors de la génération du PDF.");
            }
        } else {
            resultLabel.setText("Compte introuvable.");
        }
    }

    // Méthode pour récupérer les informations du compte
    private Compte getCompteFromDatabase(String numeroCompte) {
        String sql = "SELECT * FROM Comptes WHERE numero = ?"; // Assurez-vous que la colonne s'appelle "numero"
        Compte compte = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numeroCompte);
            var rs = db.executeSelect();
            if (rs.next()) {
                // Récupérer les informations du client
                Client client = getClientFromDatabase(rs.getInt("client_id"));

                // Utilisation des setters
                compte = new Compte();
                compte.setId(rs.getInt("id"));
                compte.setNumero(rs.getString("numero"));
                compte.setSolde(rs.getDouble("solde"));
                compte.setDate_ouverture(rs.getTimestamp("date_ouverture")); // Date d'ouverture
                compte.setClient(client); // Objet Client
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return compte;
    }

    // Méthode pour générer le PDF
    private boolean generatePDF(Compte compte) {
        // Nom du fichier PDF
        String fileName = "releve_" + compte.getNumero() + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
        Document document = new Document();

        try {
            // Créer le fichier PDF
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Ajouter des informations au PDF
            document.add(new Paragraph("Relevé Bancaire"));
            document.add(new Paragraph("Numéro de compte : " + compte.getNumero()));
            document.add(new Paragraph("Solde : " + compte.getSolde() + " XOF"));
            document.add(new Paragraph("Date d'ouverture : " + compte.getDate_ouverture()));
            document.add(new Paragraph("Client : " + compte.getClient().getNom() + " " + compte.getClient().getPrenom()));

            document.close();
            return true; // Succès
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Échec
        }
    }
    private Client getClientFromDatabase(int clientId) {
        String sql = "SELECT * FROM Clients WHERE id_client = ?";
        Client client = null;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, clientId);
            var rs = db.executeSelect();
            if (rs.next()) {
                client = new Client();
                client.setId(rs.getInt("id_client"));
                client.setNom(rs.getString("nom"));
                client.setPrenom(rs.getString("prenom"));
                client.setEmail(rs.getString("email"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return client;
    }
}