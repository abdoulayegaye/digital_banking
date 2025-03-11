package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ITransferService;
import com.banking.digitalbankingproject.service.impl.TransferServiceImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;

import java.awt.event.MouseEvent;

public class TransferController {

    private ITransferService transferService = new TransferServiceImpl();

    @FXML
    private ComboBox<Compte> sourceCombo;

    @FXML
    private ComboBox<Compte> destinationCombo;

    @FXML
    private TextField montantTfd;

    @FXML
    void initialize() {
        sourceCombo.getItems().setAll(new com.banking.digitalbankingproject.service.impl.CompteImpl().getAllComptes());
        destinationCombo.getItems().setAll(new com.banking.digitalbankingproject.service.impl.CompteImpl().getAllComptes());

        sourceCombo.setCellFactory(param -> new ListCell<Compte>() {
            @Override
            protected void updateItem(Compte compte, boolean empty) {
                super.updateItem(compte, empty);
                if (empty || compte == null || compte.getClient() == null) {
                    setText(null);
                } else {
                    setText(formatCompteDisplay(compte));
                }
            }
        });

        destinationCombo.setCellFactory(param -> new ListCell<Compte>() {
            @Override
            protected void updateItem(Compte compte, boolean empty) {
                super.updateItem(compte, empty);
                if (empty || compte == null || compte.getClient() == null) {
                    setText(null);
                } else {
                    setText(formatCompteDisplay(compte));
                }
            }
        });

        sourceCombo.setButtonCell(new ListCell<Compte>() {
            @Override
            protected void updateItem(Compte compte, boolean empty) {
                super.updateItem(compte, empty);
                if (empty || compte == null || compte.getClient() == null) {
                    setText(null);
                } else {
                    setText(formatCompteDisplay(compte));
                }
            }
        });

        destinationCombo.setButtonCell(new ListCell<Compte>() {
            @Override
            protected void updateItem(Compte compte, boolean empty) {
                super.updateItem(compte, empty);
                if (empty || compte == null || compte.getClient() == null) {
                    setText(null);
                } else {
                    setText(formatCompteDisplay(compte));
                }
            }
        });
    }

    @FXML
    void effectuerTransfer(ActionEvent event) {
        Compte source = sourceCombo.getSelectionModel().getSelectedItem();
        Compte destination = destinationCombo.getSelectionModel().getSelectedItem();
        String montantStr = montantTfd.getText().trim();

        if (source == null || destination == null || montantStr.isEmpty()) {
            Notification.NotifError("Erreur", "Veuillez sélectionner le compte source, le compte destination et saisir le montant");
            return;
        }

        if (source.getId() == destination.getId()) {
            Notification.NotifError("Erreur", "Les comptes source et destination doivent être différents");
            return;
        }

        if ("FERME".equals(source.getStatut())) {
            Notification.NotifError("Erreur", "Le compte source est fermé. Les virements sont refusés pour les comptes fermés.");
            return;
        }

        if ("FERME".equals(destination.getStatut())) {
            Notification.NotifError("Erreur", "Le compte destination est fermé. Les virements sont refusés pour les comptes fermés.");
            return;
        }

        try {
            double montant = Double.parseDouble(montantStr);
            if (transferService.transferer(source, destination, montant)) {
                Notification.NotifSuccess("Succès", "Virement effectué avec succès");
                Outils.load(event, "Gestion des Comptes", "/fxml/operations.fxml");
            }
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Le montant doit être un nombre valide");
        }
    }

    @FXML
    void retour(ActionEvent event) {
        Outils.load(event, "operation", "/fxml/operations.fxml");
    }

    private String formatCompteDisplay(Compte compte) {
        if (compte != null && compte.getClient() != null) {
            return compte.getClient().getNom() + " " + compte.getClient().getPrenom() + " (" + compte.getNumero() + ")";
        } else {
            return "Compte invalide";
        }
    }

}