package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Client;
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

import java.io.IOException;

public class ModifierCompteController {
    @FXML private TextField txtNumero;
    @FXML private TextField txtSolde;
    @FXML private CheckBox chkActif;
    @FXML private TextField txtClientNom;   // Champ pour le nom du client
    @FXML private TextField txtClientEmail; // Champ pour l'email du client
    @FXML private Button btnModifier;
    @FXML private Button btnRetour;

    private Compte compte;
    private ICompte compteService = new CompteImpl();
    private IClient clientService = new ClientImpl();

    @FXML
    private void modifierCompte(ActionEvent event) {
        if (compte == null) {
            Outils.showError("Erreur", "Aucun compte sélectionné.");
            return;
        }
        try {
            if (txtSolde.getText().isEmpty() || txtClientNom.getText().isEmpty() || txtClientEmail.getText().isEmpty()) {
                Outils.showError("Erreur", "Tous les champs doivent être remplis.");
                return;
            }
            double solde = Double.parseDouble(txtSolde.getText());
            boolean actif = chkActif.isSelected();
            compte.setBalance(solde);
            compte.setActif(actif);
            // Mise à jour des informations du client associé
            Client client = compte.getClient();
            if (client == null) {
                client = new Client();
            }
            client.setNom(txtClientNom.getText());
            client.setEmail(txtClientEmail.getText());
            if (client.getId() == 0) {
                clientService.ajouterClient(client);
            } else {
                clientService.modifierClient(client);
            }
            compte.setClient(client);
            compteService.updateCompte(compte);
            Outils.showSuccess("Succès", "Compte modifié avec succès.");
            retourGestionComptes(event);
        } catch (NumberFormatException e) {
            Outils.showError("Erreur", "Le solde doit être un nombre valide.");
        }
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

    public void setCompte(Compte compte) {
        this.compte = compte;
        txtNumero.setText(compte.getNumero());
        txtSolde.setText(String.valueOf(compte.getBalance()));
        chkActif.setSelected(compte.isActif());
        if (compte.getClient() != null) {
            txtClientNom.setText(compte.getClient().getNom());
            txtClientEmail.setText(compte.getClient().getEmail());
        }
    }
}
