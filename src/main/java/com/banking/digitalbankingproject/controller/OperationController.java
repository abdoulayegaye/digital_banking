package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Collections;
import java.util.List;

public class OperationController {
    @FXML
    private TextField numeroCompteField;
    @FXML
    private TextField montantField;
    @FXML
    private TextField compteDestinationField;
    @FXML
    private Label messageLabel;
    @FXML
    private TableView<Operation> operationTable;
    @FXML
    private TableColumn<Operation, String> typeColumn;
    @FXML
    private TableColumn<Operation, Double> montantColumn;
    @FXML
    private TableColumn<Operation, String> numeroCompteColumn;
    @FXML
    private TableColumn<Operation, String> dateColumn;

    private IOperation operationService = new OperationImpl(); // Utilisation de l'interface
    private ObservableList<Operation> operationList = FXCollections.observableArrayList();
    @FXML
    private void handleHomeButton(ActionEvent event) {
        try {
            // Charger la vue de gestion des clients
            Outils.load(event, "Retour ", "/fxml/accueil.fxml");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement  : " + e.getMessage());
        }
    }
    @FXML
    public void initialize() {
        // Associer les colonnes aux propriétés du modèle Operation
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        numeroCompteColumn.setCellValueFactory(new PropertyValueFactory<>("numeroCompte"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Charger la liste des opérations dans la TableView
        refreshOperationList();
    }

    // Effectuer un dépôt
    @FXML
    public void deposer() {
        String numeroCompte = numeroCompteField.getText();
        double montant = Double.parseDouble(montantField.getText());

        if (numeroCompte.isEmpty() || montant <= 0) {
            messageLabel.setText("Veuillez remplir tous les champs correctement.");
            return;
        }

        if (operationService.deposer(numeroCompte, montant)) {
            messageLabel.setText("Dépôt effectué avec succès !");
            refreshOperationList();
            clearFields();
        } else {
            messageLabel.setText("Erreur lors du dépôt.");
        }
    }

    // Effectuer un retrait
    @FXML
    public void retirer() {
        String numeroCompte = numeroCompteField.getText();
        double montant = Double.parseDouble(montantField.getText());

        if (numeroCompte.isEmpty() || montant <= 0) {
            messageLabel.setText("Veuillez remplir tous les champs correctement.");
            return;
        }

        if (operationService.retirer(numeroCompte, montant)) {
            messageLabel.setText("Retrait effectué avec succès !");
            refreshOperationList();
            clearFields();
        } else {
            messageLabel.setText("Erreur lors du retrait.");
        }
    }

    // Effectuer un virement
    @FXML
    public void virement() {
        String compteSource = numeroCompteField.getText();
        String compteDestination = compteDestinationField.getText();
        double montant = Double.parseDouble(montantField.getText());

        if (compteSource.isEmpty() || compteDestination.isEmpty() || montant <= 0) {
            messageLabel.setText("Veuillez remplir tous les champs correctement.");
            return;
        }

        if (operationService.virement(compteSource, compteDestination, montant)) {
            messageLabel.setText("Virement effectué avec succès !");
            refreshOperationList();
            clearFields();
        } else {
            messageLabel.setText("Erreur lors du virement.");
        }
    }

    // Consulter l'historique des transactions
    @FXML
    public void consulterHistorique() {
        String numeroCompte = numeroCompteField.getText();
        if (numeroCompte.isEmpty()) {
            messageLabel.setText("Veuillez entrer un numéro de compte.");
            return;
        }

        List<Operation> operations = operationService.getOperationsByCompte(numeroCompte);
        operationList.setAll((Operation) Collections.singleton(operations));
        operationTable.setItems(operationList);
    }

    // Rafraîchir la liste des opérations
    @FXML
    public void refreshOperationList() {
        operationList.setAll(operationService.getAllOperations());
        operationTable.setItems(operationList);
    }

    // Effacer les champs du formulaire
    private void clearFields() {
        numeroCompteField.clear();
        montantField.clear();
        compteDestinationField.clear();
    }
}