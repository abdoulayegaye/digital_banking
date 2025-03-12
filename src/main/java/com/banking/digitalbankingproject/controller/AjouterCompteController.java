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
import java.security.SecureRandom;
import java.time.Instant;

public class AjouterCompteController {

    @FXML
    private TextField txtSolde;

    @FXML
    private CheckBox chkActif;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnRetour;

    private ICompte compteService = new CompteImpl();

    @FXML
    private void ajouterCompte(ActionEvent event) {
        try {
            double solde = Double.parseDouble(txtSolde.getText());
            boolean actif = chkActif.isSelected();

            // Vérifier que les champs ne sont pas vides
            if (txtSolde.getText().isEmpty()) {
                Outils.showError("Erreur", "Tous les champs doivent être remplis.");
                return;
            }

            // Générer un numéro de compte alphanumérique de 10 caractères
            String numero = generateAccountNumber();

            // Créer un nouveau compte sans client associé
            Compte compte = new Compte(numero, solde, actif, Instant.now(), null);
            compteService.createCompte(compte);

            // Afficher un message de succès
            Outils.showSuccess("Succès", "Compte ajouté avec succès.");

            // Revenir à la vue de gestion des comptes
            retourGestionComptes(event);
        } catch (NumberFormatException e) {
            Outils.showError("Erreur", "Le solde doit être un nombre valide.");
        }
    }

    private String generateAccountNumber() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    @FXML
    private void retourGestionComptes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/gestionComptes.fxml"));
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Comptes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}