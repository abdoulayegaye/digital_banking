package com.banking.digitalbankingproject.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;

import static com.banking.digitalbankingproject.database.Constants.*;

public class OperationController {

    @FXML
    private SplitMenuButton comptesCorrespondant;

    @FXML
    private Button operationsDepot;

    @FXML
    private TextField operationsDepotField;

    @FXML
    private Button operationsHistorique;

    @FXML
    private Button operationsReleve;

    @FXML
    private Button operationsRetrait;

    @FXML
    private TextField operationsRetraitField;

    // Vérifier si un compte est sélectionné
    private boolean compteSelectionne() {
        return comptesCorrespondant.getText() != null && !comptesCorrespondant.getText().isEmpty();
    }


    @FXML
    void allerOperationsDepotField(ActionEvent event) {

    }


    @FXML
    void allerOperationsDepot(ActionEvent event) {
        if (!compteSelectionne()) {
            afficherMessage("Erreur", "Veuillez sélectionner un compte correspondant.");
            return;
        }

        String montantText = operationsDepotField.getText();
        if (montantText.isEmpty()) {
            afficherMessage("Erreur", "Veuillez saisir un montant pour le dépôt.");
            return;
        }

        try {
            double montant = Double.parseDouble(montantText);
            if (montant <= 0) {
                afficherMessage("Erreur", "Le montant doit être supérieur à zéro.");
                return;
            }
            // Logique pour ajouter le montant au compte (simulation)
            afficherMessage("Succès", "Dépôt de " + montant + " effectué avec succès !");
        } catch (NumberFormatException e) {
            afficherMessage("Erreur", "Veuillez entrer un montant valide.");
        }
    }

    @FXML
    void allerOperationsRetrait(ActionEvent event) {
        if (!compteSelectionne()) {
            afficherMessage("Erreur", "Veuillez sélectionner un compte correspondant.");
            return;
        }

        String montantText = operationsRetraitField.getText();
        if (montantText.isEmpty()) {
            afficherMessage("Erreur", "Veuillez saisir un montant pour le retrait.");
            return;
        }

        try {
            double montant = Double.parseDouble(montantText);
            if (montant <= 0) {
                afficherMessage("Erreur", "Le montant doit être supérieur à zéro.");
                return;
            }
            // Logique pour effectuer un retrait (simulation)
            afficherMessage("Succès", "Retrait de " + montant + " effectué avec succès !");
        } catch (NumberFormatException e) {
            afficherMessage("Erreur", "Veuillez entrer un montant valide.");
        }
    }

    private void afficherMessage(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void allerOperationsHistorique(ActionEvent event) {
        try {
            // Charger le fichier FXML de la fenêtre d'historique
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HistoriqueTransactions.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur et lui passer des données (si nécessaire)
            HistoriqueController historiqueController = loader.getController();
            int selectedCompteId = 0;
            historiqueController.setCompteId(selectedCompteId); // Passe l'ID du compte

            // Afficher la nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Historique des Transactions");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void allerOperationsReleve(ActionEvent event) {
        // Création d'un FileChooser pour choisir où enregistrer le fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le relevé bancaire");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));

        // Définir un fichier par défaut
    fileChooser.setInitialFileName("Relevé_bancaire.pdf");

        // Ouvrir la boîte de dialogue pour choisir l'emplacement du fichier
        Stage stage = new Stage();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/digital_banking_db", "root", "")) {
                // Récupération des informations du compte
                String sql = "SELECT c.nom, c.prenom, c.email, cp.numero, cp.balance, cp.created_at " +
                        "FROM clients c " +
                        "JOIN comptes cp ON c.id = cp.client_id " +
                        "WHERE cp.numero = ?";


                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "123456789");  // Remplace par la valeur sélectionnée dans l'interface

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String compte = rs.getString("numero_compte");
                    double solde = rs.getDouble("solde");
                    String date = java.time.LocalDate.now().toString();

                    // Création du PDF
                    PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                    PdfDocument pdf = new PdfDocument(writer);
                    Document document = new Document(pdf);

                    document.add(new Paragraph("Relevé Bancaire"));
                    document.add(new Paragraph("Nom du client : " + nom + " " + prenom));
                    document.add(new Paragraph("Numéro de compte : " + compte));
                    document.add(new Paragraph("Solde : " + solde + " FCFA"));
                    document.add(new Paragraph("Date : " + date));

                    // Fermer le document
                    document.close();
                    System.out.println("PDF généré avec succès !");
                } else {
                    System.out.println("Aucune donnée trouvée pour ce compte.");
                }

            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void allerOperationsRetraitField(ActionEvent event) {

    }


    private String compteSelectionne; // Stocker le compte sélectionné


    @FXML
    void allerComptesCorrespondant(ActionEvent event) {
        comptesCorrespondant.getItems().clear(); // Nettoyer la liste avant d'ajouter

        String sql = "SELECT numero, client_id FROM comptes;";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String compte = rs.getString("numero") + " - " + rs.getString("client_id");

                MenuItem item = new MenuItem(compte);
                item.setOnAction(e -> {
                    comptesCorrespondant.setText(compte); // Afficher le compte sélectionné
                    compteSelectionne = compte; // Stocker le compte sélectionné
                });

                comptesCorrespondant.getItems().add(item);
            }
        } catch (Exception e) {
            afficherMessage("Erreur", "Impossible de charger les comptes !");
            e.printStackTrace();
        }
    }

    // Vérifier si un compte est sélectionné avant d'effectuer une opération
    private boolean compteEstSelectionne() {
        return compteSelectionne != null && !compteSelectionne.isEmpty();
    }
}
