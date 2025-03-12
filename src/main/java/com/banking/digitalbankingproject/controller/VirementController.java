package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class VirementController {

    @FXML
    private ComboBox<Compte> comboCompteSource;
    @FXML
    private ComboBox<Compte> comboCompteDestinataire;
    @FXML
    private TextField txtMontant;
    @FXML
    private Button btnVirement;
    @FXML
    private Button btnRetour;

    private ICompte compteService = new CompteImpl();
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        chargerComptes();
        comboCompteSource.setConverter(new StringConverter<Compte>() {
            @Override
            public String toString(Compte compte) {
                return compte == null ? "" : String.format("%s - %s %s", compte.getNumero(), compte.getClient().getPrenom(), compte.getClient().getNom());
            }

            @Override
            public Compte fromString(String string) {
                return null;
            }
        });

        comboCompteDestinataire.setConverter(new StringConverter<Compte>() {
            @Override
            public String toString(Compte compte) {
                return compte == null ? "" : String.format("%s - %s %s", compte.getNumero(), compte.getClient().getPrenom(), compte.getClient().getNom());
            }

            @Override
            public Compte fromString(String string) {
                return null;
            }
        });
    }

    private void chargerComptes() {
        List<Compte> allComptes = compteService.getAllComptes();
        List<Compte> activeAttributedComptes = allComptes.stream()
                .filter(compte -> compte.isActif() && compte.getClient() != null)
                .collect(Collectors.toList());
        comptesList.setAll(activeAttributedComptes);
        comboCompteSource.setItems(comptesList);
        comboCompteDestinataire.setItems(comptesList);
    }

    @FXML
    private void effectuerVirement(ActionEvent event) {
        Compte compteSource = comboCompteSource.getSelectionModel().getSelectedItem();
        Compte compteDestinataire = comboCompteDestinataire.getSelectionModel().getSelectedItem();
        String montantStr = txtMontant.getText();

        if (compteSource == null || compteDestinataire == null) {
            Outils.showError("Erreur", "Veuillez sélectionner les comptes source et destinataire.");
            return;
        }

        if (montantStr == null || montantStr.isEmpty()) {
            Outils.showError("Erreur", "Veuillez saisir un montant.");
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(montantStr);
        } catch (NumberFormatException e) {
            Outils.showError("Erreur", "Le montant saisi n'est pas valide.");
            return;
        }

        try {
            compteService.virement(compteSource, compteDestinataire, montant);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Virement effectué avec succès.");
            alert.showAndWait();
            retourGestionOperations(event);
        } catch (IllegalArgumentException e) {
            Outils.showError("Erreur", e.getMessage());
        }
    }

    @FXML
    private void retourGestionOperations(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Opérations", "/fxml/gestionOperations.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filtrerComptesDestinataire(ActionEvent event) {
        // Implement the logic to filter destination accounts if needed
    }
}