package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;

import java.sql.Timestamp;

public class OperationController {

    @FXML private ComboBox<Compte> compteSourceComboBox;
    @FXML private ComboBox<Compte> compteDestComboBox;
    @FXML private TextField montantField;
    @FXML private TableView<Operation> operationTable;
    @FXML private TableColumn<Operation, String> typeCol;
    @FXML private TableColumn<Operation, Double> montantCol;
    @FXML private TableColumn<Operation, Timestamp> dateCol;
    @FXML private TableColumn<Operation, Integer> compteCol;

    private ICompte compteService = new CompteImpl();
    private IOperation operationService = new OperationImpl();

    @FXML
    public void initialize() {
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        montantCol.setCellValueFactory(cellData -> cellData.getValue().montantProperty().asObject());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateOperationProperty());
        compteCol.setCellValueFactory(cellData -> cellData.getValue().compteIdProperty().asObject());

        // Charger les comptes dans les ComboBox
        compteSourceComboBox.setItems(FXCollections.observableArrayList(compteService.listerComptes()));
        compteDestComboBox.setItems(FXCollections.observableArrayList(compteService.listerComptes()));
        compteSourceComboBox.setCellFactory(param -> new ListCell<Compte>() {
            @Override
            protected void updateItem(Compte compte, boolean empty) {
                super.updateItem(compte, empty);
                setText(empty || compte == null ? null : compte.getNumero());
            }
        });
        compteSourceComboBox.setButtonCell(compteSourceComboBox.getCellFactory().call(null));
        compteDestComboBox.setCellFactory(compteSourceComboBox.getCellFactory());
        compteDestComboBox.setButtonCell(compteSourceComboBox.getCellFactory().call(null));
    }

    @FXML
    private void effectuerDepot(ActionEvent event) {
        Compte compte = compteSourceComboBox.getValue();
        String montantText = montantField.getText();
        if (compte == null || montantText.isEmpty()) {
            showAlert("Erreur", "Sélectionnez un compte et entrez un montant.");
            return;
        }
        try {
            double montant = Double.parseDouble(montantText);
            if (montant <= 0) {
                showAlert("Erreur", "Le montant doit être positif.");
                return;
            }
            compteService.depot(compte.getId(), montant);
            operationService.creerOperation(new Operation(0, "DÉPÔT", montant, new Timestamp(System.currentTimeMillis()), compte.getId()));
            consulterHistorique(event);
            montantField.clear();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Montant invalide.");
        }
    }

    @FXML
    private void effectuerRetrait(ActionEvent event) {
        Compte compte = compteSourceComboBox.getValue();
        String montantText = montantField.getText();
        if (compte == null || montantText.isEmpty()) {
            showAlert("Erreur", "Sélectionnez un compte et entrez un montant.");
            return;
        }
        try {
            double montant = Double.parseDouble(montantText);
            if (montant <= 0) {
                showAlert("Erreur", "Le montant doit être positif.");
                return;
            }
            compteService.retrait(compte.getId(), montant);
            operationService.creerOperation(new Operation(0, "RETRAIT", montant, new Timestamp(System.currentTimeMillis()), compte.getId()));
            consulterHistorique(event);
            montantField.clear();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Montant invalide.");
        }
    }

    @FXML
    private void effectuerVirement(ActionEvent event) {
        Compte source = compteSourceComboBox.getValue();
        Compte dest = compteDestComboBox.getValue();
        String montantText = montantField.getText();
        if (source == null || dest == null || montantText.isEmpty()) {
            showAlert("Erreur", "Sélectionnez les deux comptes et entrez un montant.");
            return;
        }
        if (source.getId() == dest.getId()) {
            showAlert("Erreur", "Les comptes doivent être différents.");
            return;
        }
        try {
            double montant = Double.parseDouble(montantText);
            if (montant <= 0) {
                showAlert("Erreur", "Le montant doit être positif.");
                return;
            }
            compteService.virement(source.getId(), dest.getId(), montant);
            operationService.creerOperation(new Operation(0, "RETRAIT", montant, new Timestamp(System.currentTimeMillis()), source.getId()));
            operationService.creerOperation(new Operation(0, "DÉPÔT", montant, new Timestamp(System.currentTimeMillis()), dest.getId()));
            consulterHistorique(event);
            montantField.clear();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Montant invalide.");
        }
    }

    @FXML
    private void consulterHistorique(ActionEvent event) {
        Compte compte = compteSourceComboBox.getValue();
        if (compte == null) {
            showAlert("Erreur", "Sélectionnez un compte pour voir l'historique.");
            return;
        }
        operationTable.setItems(FXCollections.observableArrayList(operationService.listerOperationsParCompte(compte.getId())));
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}