package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
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
import java.security.SecureRandom;
import java.time.Instant;

public class AjouterCompteController {
    @FXML private TextField txtSolde;
    @FXML private CheckBox chkActif;
    @FXML private TextField txtClientNom;   // Champ pour le nom du client
    @FXML private TextField txtClientEmail; // Champ pour l'email du client
    @FXML private Button btnAjouter;
    @FXML private Button btnRetour;

    private ICompte compteService = new CompteImpl();
    private IClient clientService = new ClientImpl();

    @FXML
    private void ajouterCompte(ActionEvent event) {
        try {
            if (txtSolde.getText().isEmpty() || txtClientNom.getText().isEmpty() || txtClientEmail.getText().isEmpty()) {
                Outils.showError("Erreur", "Tous les champs doivent être remplis.");
                return;
            }
            double solde = Double.parseDouble(txtSolde.getText());
            boolean actif = chkActif.isSelected();

            // Création du client à partir des champs renseignés
            String nomClient = txtClientNom.getText();
            String emailClient = txtClientEmail.getText();
            Client client = new Client(nomClient, "", emailClient);
            clientService.ajouterClient(client);

            // Génération d'un numéro de compte alphanumérique
            String numero = generateAccountNumber();

            // Création du compte avec le client associé
            Compte compte = new Compte(numero, solde, actif, Instant.now(), client);
            compteService.createCompte(compte);

            Outils.showSuccess("Succès", "Compte ajouté avec succès.");
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
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de retourner à la page Gestion des Comptes.");
        }
    }
}
