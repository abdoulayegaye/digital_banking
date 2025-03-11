package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OperationController {
    @FXML
    private ComboBox<Compte> comboCompteSource;
    @FXML
    private ComboBox<Compte> comboCompteDestination;
    @FXML
    private TextField txtMontant;
    
    @FXML
    private TableView<Operation> tableOperations;
    @FXML
    private TableColumn<Operation, String> colDate;
    @FXML
    private TableColumn<Operation, String> colType;
    @FXML
    private TableColumn<Operation, Double> colMontant;
    @FXML
    private TableColumn<Operation, String> colCompte;
    
    @FXML
    private Button btnDepot;
    @FXML
    private Button btnRetrait;
    @FXML
    private Button btnVirement;
    @FXML
    private Button btnRetour;
    
    private final IOperation operationService = new OperationImpl();
    private final ICompte compteService = new CompteImpl();
    private ObservableList<Operation> operationsList = FXCollections.observableArrayList();
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colDate.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getDateOp().atZone(java.time.ZoneId.systemDefault()).format(formatter)
            );
        });
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colCompte.setCellValueFactory(cellData -> {
            Compte compte = cellData.getValue().getCompte();
            return javafx.beans.binding.Bindings.createStringBinding(
                () -> compte != null ? compte.getNumero() : ""
            );
        });

        // Chargement des données
        chargerComptes();
        chargerOperations();
    }

    private void chargerComptes() {
        List<Compte> comptes = compteService.getAllComptes();
        comptesList.setAll(comptes);
        comboCompteSource.setItems(comptesList);
        comboCompteDestination.setItems(comptesList);
        
        // Personnalisation de l'affichage des comptes dans les ComboBox
        comboCompteSource.setConverter(new javafx.util.StringConverter<Compte>() {
            @Override
            public String toString(Compte compte) {
                return compte != null ? compte.getNumero() + " - " + compte.getClient().getNom() : "";
            }

            @Override
            public Compte fromString(String string) {
                return null;
            }
        });
        comboCompteDestination.setConverter(comboCompteSource.getConverter());
    }

    private void chargerOperations() {
        List<Operation> operations = operationService.getAllOperations();
        operationsList.setAll(operations);
        tableOperations.setItems(operationsList);
    }

    @FXML
    void effectuerDepot(ActionEvent event) {
        if (comboCompteSource.getValue() == null || txtMontant.getText().isEmpty()) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte et saisir un montant");
            return;
        }

        try {
            double montant = Double.parseDouble(txtMontant.getText());
            if (montant <= 0) {
                Notification.NotifError("Erreur", "Le montant doit être positif");
                return;
            }

            if (operationService.effectuerDepot(comboCompteSource.getValue().getId(), montant)) {
                Notification.NotifSuccess("Succès", "Dépôt effectué avec succès");
                chargerOperations();
                reinitialiserChamps();
            } else {
                Notification.NotifError("Erreur", "Erreur lors du dépôt");
            }
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Le montant doit être un nombre valide");
        }
    }

    @FXML
    void effectuerRetrait(ActionEvent event) {
        if (comboCompteSource.getValue() == null || txtMontant.getText().isEmpty()) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte et saisir un montant");
            return;
        }

        try {
            double montant = Double.parseDouble(txtMontant.getText());
            if (montant <= 0) {
                Notification.NotifError("Erreur", "Le montant doit être positif");
                return;
            }

            if (operationService.effectuerRetrait(comboCompteSource.getValue().getId(), montant)) {
                Notification.NotifSuccess("Succès", "Retrait effectué avec succès");
                chargerOperations();
                reinitialiserChamps();
            } else {
                Notification.NotifError("Erreur", "Solde insuffisant ou erreur lors du retrait");
            }
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Le montant doit être un nombre valide");
        }
    }

    @FXML
    void effectuerVirement(ActionEvent event) {
        if (comboCompteSource.getValue() == null || comboCompteDestination.getValue() == null || txtMontant.getText().isEmpty()) {
            Notification.NotifError("Erreur", "Veuillez sélectionner les comptes et saisir un montant");
            return;
        }

        if (comboCompteSource.getValue().equals(comboCompteDestination.getValue())) {
            Notification.NotifError("Erreur", "Les comptes source et destination doivent être différents");
            return;
        }

        try {
            double montant = Double.parseDouble(txtMontant.getText());
            if (montant <= 0) {
                Notification.NotifError("Erreur", "Le montant doit être positif");
                return;
            }

            if (operationService.effectuerVirement(
                    comboCompteSource.getValue().getId(),
                    comboCompteDestination.getValue().getId(),
                    montant)) {
                Notification.NotifSuccess("Succès", "Virement effectué avec succès");
                chargerOperations();
                reinitialiserChamps();
            } else {
                Notification.NotifError("Erreur", "Solde insuffisant ou erreur lors du virement");
            }
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Le montant doit être un nombre valide");
        }
    }

    @FXML
    void retourAccueil(ActionEvent event) throws IOException {
        Outils.load(event, "Accueil", "/fxml/accueil.fxml");
    }

    private void reinitialiserChamps() {
        comboCompteSource.setValue(null);
        comboCompteDestination.setValue(null);
        txtMontant.clear();
    }
}
