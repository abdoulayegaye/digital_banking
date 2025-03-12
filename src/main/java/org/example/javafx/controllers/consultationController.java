package org.example.javafx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.javafx.dao.DBConnexion;

public class consultationController {

    @FXML
    private TextField numeroCompteField;

    @FXML
    private Label soldeLabel;

    private DBConnexion db = new DBConnexion();

    @FXML
    private void consulterSolde() {
        String numeroCompte = numeroCompteField.getText();


        if (numeroCompte.isEmpty()) {
            soldeLabel.setText("Veuillez saisir un numéro de compte.");
            return;
        }


        double solde = getSoldeFromDatabase(numeroCompte);

        if (solde >= 0) {
            soldeLabel.setText("Solde : " + solde + " XOF");
        } else {
            soldeLabel.setText("Compte introuvable.");
        }
    }

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