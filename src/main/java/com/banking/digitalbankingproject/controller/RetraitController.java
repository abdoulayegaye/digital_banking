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
import javafx.util.StringConverter;
import java.util.List;
import java.util.stream.Collectors;

public class RetraitController {
    @FXML private ComboBox<Compte> comboCompte;
    @FXML private TextField txtMontant;
    @FXML private Button btnRetrait;
    @FXML private Button btnRetour;

    private ICompte compteService = new CompteImpl();
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        chargerComptes();
        comboCompte.setConverter(new StringConverter<Compte>() {
            @Override
            public String toString(Compte compte) {
                return compte == null ? "" : String.format("%s - %s %s", compte.getNumero(),
                        compte.getClient().getPrenom(), compte.getClient().getNom());
            }
            @Override
            public Compte fromString(String string) {
                return null;
            }
        });
    }

    private void chargerComptes() {
        List<Compte> allComptes = compteService.getAllComptes();
        System.out.println("🔍 Nombre de comptes trouvés : " + allComptes.size()); // Log

        List<Compte> comptesActifs = allComptes.stream()
                .filter(compte -> compte.isActif() && compte.getClient() != null)
                .toList();

        if (comptesActifs.isEmpty()) {
            System.out.println("⚠️ Aucun compte actif trouvé !");
        } else {
            comptesActifs.forEach(c -> System.out.println("✅ Compte : " + c.getNumero() + " - Client : " + c.getClient().getNom()));
        }

        comptesList.setAll(comptesActifs);
        comboCompte.setItems(comptesList);
    }


    @FXML
    private void effectuerRetrait(ActionEvent event) {
        Compte compteSelectionne = comboCompte.getSelectionModel().getSelectedItem();
        if (compteSelectionne == null) {
            Outils.showError("Erreur", "Veuillez sélectionner un compte.");
            return;
        }
        double montant;
        try {
            montant = Double.parseDouble(txtMontant.getText());
        } catch (NumberFormatException e) {
            Outils.showError("Erreur", "Veuillez entrer un montant valide.");
            return;
        }
        if (compteSelectionne.getBalance() < montant) {
            Outils.showError("Erreur", "Le solde du compte est insuffisant.");
            return;
        }
        compteSelectionne.setBalance(compteSelectionne.getBalance() - montant);
        compteService.updateCompte(compteSelectionne);
        Outils.showSuccess("Succès", "Retrait effectué avec succès.");
        retourGestionOperations(event);
    }

    @FXML
    private void retourGestionOperations(ActionEvent event) {
        try {
            Outils.load(event, "Gestion des Opérations", "/fxml/gestionOperations.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            Outils.showError("Erreur", "Impossible de retourner à la page Gestion des Opérations.");
        }
    }
}
