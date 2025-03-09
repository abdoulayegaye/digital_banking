package org.example.javafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.javafx.dao.DBConnexion;

public class FermetureController {

    @FXML private TextField compteIdField;
    @FXML private Label soldeLabel, resultLabel;

    private DBConnexion db = new DBConnexion();

    @FXML
    public void verifierSolde() {
        try {
            int compteId = Integer.parseInt(compteIdField.getText());
            double solde = getSolde(compteId);
            if (solde >= 0) {
                soldeLabel.setText("Solde : " + solde + " €");
            } else {
                soldeLabel.setText("Compte introuvable !");
            }
        } catch (NumberFormatException e) {
            soldeLabel.setText("ID invalide !");
        }
    }

    @FXML
    public void fermerCompte() {
        try {
            int compteId = Integer.parseInt(compteIdField.getText());
            double solde = getSolde(compteId);

            if (solde == 0) {
                int result = closeAccount(compteId);
                if (result > 0) {
                    resultLabel.setText("Compte fermé avec succès !");
                } else {
                    resultLabel.setText("Échec de la fermeture !");
                }
            } else if (solde > 0) {
                resultLabel.setText("Impossible : Solde non nul (" + solde + "€)");
            } else {
                resultLabel.setText("Compte introuvable !");
            }
        } catch (NumberFormatException e) {
            resultLabel.setText("ID invalide !");
        }
    }

    // Méthode pour récupérer le solde
    public double getSolde(int compteId) {
        String sql = "SELECT solde FROM Comptes WHERE id = ?";
        double solde = -1;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
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

    // Méthode pour fermer le compte
    public int closeAccount(int compteId) {
        String sql = "DELETE FROM Comptes WHERE id = ? AND solde = 0";
        int ok = 0;
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            ok = db.executeMaj();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return ok;
    }

}




