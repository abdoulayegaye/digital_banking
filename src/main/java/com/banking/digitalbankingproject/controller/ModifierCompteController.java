package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ModifierCompteController {

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtSolde;

    @FXML
    private CheckBox chkActif;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnRetour;

    private Compte compte;
    private ICompte compteService = new CompteImpl();

    @FXML
    private void modifierCompte(ActionEvent event) {
        if (compte == null) {
            Outils.showError("Erreur", "Aucun compte sélectionné.");
            return;
        }

        try {
            double solde = Double.parseDouble(txtSolde.getText());
            boolean actif = chkActif.isSelected();

            // Vérifier que les champs ne sont pas vides
            if (txtSolde.getText().isEmpty()) {
                Outils.showError("Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            // Mettre à jour le compte
            compte.setBalance(solde);
            compteService.updateCompte(compte);

            // Afficher un message de succès
            Outils.showSuccess("Succès", "Compte modifié avec succès.");

            // Revenir à la vue de gestion des comptes
            retourGestionComptes(event);
        } catch (NumberFormatException e) {
            Outils.showError("Erreur", "Le solde doit être un nombre valide.");
        }
    }

    @FXML
    private void retourGestionComptes(ActionEvent event) {
        try {
            //Parent root = FXMLLoader.load(getClass().getResource("/fxml/gestionComptes.fxml"));
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            //stage.setScene(new Scene(root));
            //stage.setTitle("Gestion des Comptes");
            stage.hide();
            //Outils.load(event,"Gestion des Compte","/fxml/gestionComptes.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
        txtNumero.setText(compte.getNumero());
        txtSolde.setText(String.valueOf(compte.getBalance()));
    }
}