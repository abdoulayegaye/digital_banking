package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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

    private final ICompte compteService = new CompteImpl();
    private final ObservableList<Compte> comptesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        chargerComptes();
        comboCompteSource.setConverter(new StringConverter<Compte>() {
            @Override
            public String toString(Compte compte) {
                return compte == null ? "" : String.format("%s - %s %s", compte.getNumero(),
                        compte.getClient().getPrenom(), compte.getClient().getNom());
            }

            @Override
            public Compte fromString(String string) {
                return null; // Pas nécessaire pour un affichage uniquement
            }
        });

        comboCompteDestinataire.setConverter(new StringConverter<Compte>() {
            @Override
            public String toString(Compte compte) {
                return compte == null ? "" : String.format("%s - %s %s", compte.getNumero(),
                        compte.getClient().getPrenom(), compte.getClient().getNom());
            }

            @Override
            public Compte fromString(String string) {
                return null; // Pas nécessaire pour un affichage uniquement
            }
        });
    }

    private void chargerComptes() {
        List<Compte> allComptes = compteService.getAllComptes();
        List<Compte> attributedComptes = allComptes.stream()
                .filter(compte -> compte.getClient() != null) // Filtrer uniquement sur client non null
                .collect(Collectors.toList());
        comptesList.setAll(attributedComptes);
        comboCompteSource.setItems(comptesList);
        comboCompteDestinataire.setItems(comptesList);
    }

    @FXML
    private void effectuerVirement(ActionEvent event) {
        Compte compteSource = comboCompteSource.getSelectionModel().getSelectedItem();
        Compte compteDestinataire = comboCompteDestinataire.getSelectionModel().getSelectedItem();
        String montantStr = txtMontant.getText().trim();

        // Vérifications des entrées
        if (compteSource == null || compteDestinataire == null) {
            Outils.showError("Erreur", "Veuillez sélectionner les comptes source et destinataire.");
            return;
        }
        if (compteSource.equals(compteDestinataire)) {
            Outils.showError("Erreur", "Le compte source et le compte destinataire ne peuvent pas être identiques.");
            return;
        }
        if (montantStr.isEmpty()) {
            Outils.showError("Erreur", "Veuillez saisir un montant.");
            return;
        }

        double montant;
        try {
            montant = Double.parseDouble(montantStr);
            if (montant <= 0) {
                Outils.showError("Erreur", "Le montant doit être supérieur à zéro.");
                return;
            }
        } catch (NumberFormatException e) {
            Outils.showError("Erreur", "Le montant saisi n'est pas valide (ex. 100.50).");
            return;
        }

        try {
            compteService.virement(compteSource, compteDestinataire, montant);
            Outils.showSuccess("Succès", "Virement effectué avec succès.");
            txtMontant.clear(); // Réinitialiser le champ montant
            chargerComptes(); // Recharger les comptes pour refléter les nouveaux soldes
        } catch (IllegalArgumentException e) {
            Outils.showError("Erreur", e.getMessage());
        } catch (Exception e) {
            Outils.showError("Erreur", "Une erreur inattendue est survenue : " + e.getMessage());
        }
    }

    @FXML
    private void retourGestionOperations(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Opérations", "/fxml/gestionOperations.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de charger la vue Gestion des Opérations : " + e.getMessage());
        }
    }

    @FXML
    private void filtrerComptesDestinataire(ActionEvent event) {
        // Optionnel : Filtrer les comptes destinataires pour exclure le compte source
        Compte compteSource = comboCompteSource.getSelectionModel().getSelectedItem();
        if (compteSource != null) {
            List<Compte> filteredComptes = comptesList.stream()
                    .filter(compte -> !compte.equals(compteSource))
                    .collect(Collectors.toList());
            comboCompteDestinataire.setItems(FXCollections.observableArrayList(filteredComptes));
        }
    }
}