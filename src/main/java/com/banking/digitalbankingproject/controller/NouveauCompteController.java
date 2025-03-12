package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.controlsfx.control.Notifications;

import java.util.List;

public class NouveauCompteController {
    @FXML
    private ComboBox<Client> clientComboBox;
    @FXML
    private TextField numeroTextField;
    @FXML
    private TextField balanceTextField;

    private final IClient clientService;
    private final ICompte compteService;

    public NouveauCompteController() {
        this.clientService = new ClientImpl();
        this.compteService = new CompteImpl();
    }

    @FXML
    public void initialize() {
        // Charger la liste des clients
        List<Client> clients = clientService.getAllClients();
        clientComboBox.getItems().addAll(clients);
    }

    @FXML
    private void handleEnregistrerAction(ActionEvent event) {
        try {
            // Validation des champs
            if (clientComboBox.getValue() == null || 
                numeroTextField.getText().trim().isEmpty() || 
                balanceTextField.getText().trim().isEmpty()) {
                
                Notifications.create()
                    .title("Erreur")
                    .text("Tous les champs sont obligatoires")
                    .showError();
                return;
            }

            // Création du compte
            Compte compte = new Compte();
            compte.setNumero(numeroTextField.getText().trim());
            compte.setBalance(Double.parseDouble(balanceTextField.getText().trim()));
            compte.setClientId(clientComboBox.getValue().getId());

            // Sauvegarde du compte
            if (compteService.createCompte(compte)) {
                Notifications.create()
                    .title("Succès")
                    .text("Compte créé avec succès")
                    .showInformation();

                // Retour à la liste des comptes
                Outils.load(event, "Gestion des Comptes", "comptes");
            } else {
                Notifications.create()
                    .title("Erreur")
                    .text("Erreur lors de la création du compte")
                    .showError();
            }
        } catch (NumberFormatException e) {
            Notifications.create()
                .title("Erreur")
                .text("Le solde doit être un nombre valide")
                .showError();
        } catch (Exception e) {
            Notifications.create()
                .title("Erreur")
                .text("Une erreur est survenue: " + e.getMessage())
                .showError();
        }
    }

    @FXML
    private void handleAnnulerAction(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Comptes", "comptes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 