package org.example.javafx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.javafx.entities.Operation;
import org.example.javafx.enums.TypeOperation;
import org.example.javafx.service.IOperation;
import org.example.javafx.service.impl.OperationImpl;
import org.example.javafx.tools.Notification;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OperationController implements Initializable {
    @FXML private TextField depotCompteField;
    @FXML private TextField depotMontantField;

    @FXML private TextField retraitCompteField;
    @FXML private TextField retraitMontantField;

    @FXML private TextField virementSourceField;
    @FXML private TextField virementDestinationField;
    @FXML private TextField virementMontantField;

    @FXML private TextField historiqueCompteField;
    @FXML private TableView<Operation> historiqueTable;

    @FXML private TableColumn<Operation, String> dateColumn;
    @FXML private TableColumn<Operation, String> typeColumn;
    @FXML private TableColumn<Operation, Double> montantColumn;

    private IOperation operationService = new OperationImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateOp"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    @FXML
    private void handleDepot() {
        String compte = depotCompteField.getText();
        double montant = Double.parseDouble(depotMontantField.getText());
        int result = operationService.effectuerDepot(compte, montant);

        if (result > 0) {
            Notification.NotifSuccess("Succès", "Dépôt effectué avec succès");
        } else {
            Notification.NotifError("Erreur", "Échec du dépôt");
        }
    }

    @FXML
    private void handleRetrait() {
        String compte = retraitCompteField.getText();
        double montant = Double.parseDouble(retraitMontantField.getText());
        int result = operationService.effectuerRetrait(compte, montant);

        if (result > 0) {
            Notification.NotifSuccess("Succès", "Retrait effectué avec succès");
        } else {
            Notification.NotifError("Erreur", "Échec du retrait");
        }
    }

    @FXML
    private void handleVirement() {
        String source = virementSourceField.getText();
        String destination = virementDestinationField.getText();
        double montant = Double.parseDouble(virementMontantField.getText());
        int result = operationService.effectuerVirement(source, destination, montant);

        if (result > 0) {
            Notification.NotifSuccess("Succès", "Virement effectué avec succès");
        } else {
            Notification.NotifError("Erreur", "Échec du virement");
        }
    }

    @FXML
    private void handleConsulterHistorique() {
        String compte = historiqueCompteField.getText();
        List<Operation> operations = operationService.consulterHistorique(compte);

        ObservableList<Operation> observableList = FXCollections.observableArrayList(operations);

        historiqueTable.setItems(observableList);

        if (operations.isEmpty()) {
            Notification.NotifError("Information", "Aucune transaction trouvée pour ce compte");
        } else {
            Notification.NotifSuccess("Succès", "Historique chargé avec succès");
        }
    }
}