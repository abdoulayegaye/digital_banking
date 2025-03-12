package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;

public class AjouterCompteController {

    @FXML
    private TextField txtNumero;

    @FXML
    private TextField txtSolde;

    @FXML
    private ComboBox<Client> cmbClient;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnRetour;

    private final ICompte compteService = new CompteImpl();

    @FXML
    public void initialize() {
        try {
            List<Client> clients = compteService.getAllClients();
            System.out.println("Nombre de clients récupérés : " + clients.size()); // Débogage
            if (clients.isEmpty()) {
                // Outils.showWarning("Attention", "Aucun client disponible. Veuillez d'abord ajouter des clients.");
            }
            cmbClient.getItems().addAll(clients);
            cmbClient.setCellFactory(param -> new javafx.scene.control.ListCell<Client>() {
                @Override
                protected void updateItem(Client item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getNomComplet());
                }
            });
            cmbClient.setPromptText("Sélectionner un client");
        } catch (Exception e) {
            Outils.showError("Erreur", "Impossible de charger les clients : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterCompte(ActionEvent event) {
        System.out.println("Début de ajouterCompte");
        try {
            String numero = txtNumero.getText().trim();
            String soldeText = txtSolde.getText().trim();
            Client client = cmbClient.getValue();

            System.out.println("Numéro: " + numero + ", Solde: " + soldeText + ", Client: " + (client != null ? client.getNomComplet() : "null"));

            if (numero.isEmpty()) {
                Outils.showError("Erreur", "Le numéro du compte est obligatoire.");
                return;
            }
            if (compteService.accountNumberExists(numero)) {
                Outils.showError("Erreur", "Ce numéro de compte existe déjà.");
                return;
            }
            if (soldeText.isEmpty()) {
                Outils.showError("Erreur", "Le solde initial est obligatoire.");
                return;
            }
            if (client == null) {
                Outils.showError("Erreur", "Veuillez sélectionner un client.");
                return;
            }

            double solde = Double.parseDouble(soldeText);
            if (solde < 0) {
                Outils.showError("Erreur", "Le solde initial ne peut pas être négatif.");
                return;
            }

            // Suppression de 'actif' dans la création du compte
            Compte compte = new Compte(numero, solde, Instant.now(), client);
            System.out.println("Avant création du compte");
            boolean success = compteService.createCompte(compte);
            System.out.println("Après création du compte, succès: " + success);

            if (success) {
                Outils.showSuccess("Succès", "Compte " + numero + " ajouté avec succès.");
                clearFields();
                retourGestionComptes(event);
            } else {
                Outils.showError("Erreur", "Échec de la création du compte.");
            }
        } catch (NumberFormatException e) {
            Outils.showError("Erreur", "Le solde doit être un nombre valide (ex. 1000.50).");
        } catch (Exception e) {
            System.out.println("Exception dans ajouterCompte: " + e.getMessage());
            e.printStackTrace();
            Outils.showError("Erreur", "Une erreur inattendue est survenue : " + e.getMessage());
        }
        System.out.println("Fin de ajouterCompte");
    }

    @FXML
    private void retourGestionComptes(ActionEvent event) {
        System.out.println("Début de retourGestionComptes");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/gestionComptes.fxml"));
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Comptes");
            stage.show();
            System.out.println("Fin de retourGestionComptes");
        } catch (IOException e) {
            System.out.println("Erreur dans retourGestionComptes: " + e.getMessage());
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la vue Gestion des Comptes : " + e.getMessage());
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

    private void clearFields() {
        txtNumero.clear();
        txtSolde.clear();
        cmbClient.getSelectionModel().clearSelection();
    }
}