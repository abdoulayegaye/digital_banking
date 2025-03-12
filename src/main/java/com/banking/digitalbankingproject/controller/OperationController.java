package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.banking.digitalbankingproject.entity.Operation; // Importez la classe Operation

public class OperationController {

    @FXML
    private TextField txtOperationId;
    @FXML
    private TextField txtCompteId;
    @FXML
    private TextField txtMontant;
    @FXML
    private TextField txtTypeOperation;

    @FXML
    private ListView<Operation> operationListView; // Utilisez Operation comme type

    private ObservableList<Operation> operations = FXCollections.observableArrayList(); // Renommez en operations

    @FXML
    public void initialize() {
        operationListView.setItems(operations); // Utilisez operations
    }

    @FXML
    private void addOperation() {
        try {
            int id = Integer.parseInt(txtOperationId.getText().trim());
            int compteId = Integer.parseInt(txtCompteId.getText().trim());
            double montant = Double.parseDouble(txtMontant.getText().trim());
            String typeOperation = txtTypeOperation.getText().trim();

            // Créez un nouvel objet Operation
            Operation operation = new Operation(id, compteId, montant, typeOperation);
            operations.add(operation); // Ajoutez l'opération à la liste
            clearFields();
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer des valeurs valides pour l'ID, le montant et l'ID du compte.");
        }
    }

    @FXML
    private void deleteOperation() {
        Operation selectedOperation = operationListView.getSelectionModel().getSelectedItem();
        if (selectedOperation != null) {
            operations.remove(selectedOperation); // Supprimez l'opération sélectionnée
        }
    }

    private void clearFields() {
        txtOperationId.clear();
        txtCompteId.clear();
        txtMontant.clear();
        txtTypeOperation.clear();
    }
}