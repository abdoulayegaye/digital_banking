package org.example.javafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.javafx.dao.DBConnexion;

public class consultationController {

    @FXML
    private TextField numeroCompteField; // Champ de saisie du numéro de compte

    @FXML
    private Label soldeLabel; // Label pour afficher le solde

    private DBConnexion db = new DBConnexion();

    // Méthode pour consulter le solde
    @FXML
    private void consulterSolde() {
        String numeroCompte = numeroCompteField.getText(); // Récupérer le numéro de compte saisi

        // Vérifier si le champ est vide
        if (numeroCompte.isEmpty()) {
            soldeLabel.setText("Veuillez saisir un numéro de compte.");
            return;
        }

        // Récupérer le solde depuis la base de données
        double solde = getSoldeFromDatabase(numeroCompte);

        if (solde >= 0) {
            soldeLabel.setText("Solde : " + solde + " XOF"); // Afficher le solde
        } else {
            soldeLabel.setText("Compte introuvable."); // Message d'erreur
        }
    }

    // Méthode pour récupérer le solde depuis la base de données
    private double getSoldeFromDatabase(String numeroCompte) {
        String sql = "SELECT solde FROM Comptes WHERE numero = ?";
        double solde = -1;
        try {
            db.initPrepar(sql);
            db.getPstm().setString(1, numeroCompte);
            var rs = db.executeSelect();
            if (rs.next()) {
                solde = rs.getDouble("solde");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return solde;
    }
}