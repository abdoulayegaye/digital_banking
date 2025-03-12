package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RetraitController {
    @FXML
    private TextField montantTfd;
    @FXML
    private TextField numCompteTfd;
    @FXML
    private Button retourBtn;
    @FXML
    private Button validerBtn;

    @FXML
    void retour(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Opérations", "/fxml/interfaceOperations.fxml");
    }

    @FXML
    void valider(ActionEvent event) throws IOException {
        ICompte iCompte = new CompteImpl();
        IOperation iOperation = new OperationImpl();

        String numeroCompte = numCompteTfd.getText();
        String montantText = montantTfd.getText();
        if (numeroCompte.isEmpty() || montantText.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(montantText);
            if (montant <= 0) {
                Notification.NotifError("Erreur", "Le montant doit être positif !");
                return;
            }
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Montant invalide !");
            return;
        }

        Compte compte = iCompte.getCompteByNumero(numeroCompte);
        if (compte == null) {
            Notification.NotifError("Erreur", "Compte introuvable !");
            return;
        }

        boolean success = iOperation.retrait(compte.getId(), montant);
        if (success) {
            Notification.NotifSuccess("Succès", "Retrait effectué avec succès !");
            Outils.load(event, "Gestion des Opérations", "/fxml/interfaceOperations.fxml");
        } else {
            Notification.NotifError("Erreur", "Échec du retrait !");
        }
    }
}
