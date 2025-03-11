package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.entity.StatementGenerator;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HistoriqueController {

    private IOperation operationDao = new OperationImpl();
    private Compte compte;

    @FXML
    private Label compteInfoLabel;

    @FXML
    private TableView<Operation> operationsTable;

    @FXML
    private TableColumn<Operation, Integer> idCol;

    @FXML
    private TableColumn<Operation, String> dateCol;

    @FXML
    private TableColumn<Operation, Double> amountCol;

    @FXML
    private TableColumn<Operation, String> typeCol;

    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private DatePicker dateFinPicker;

    @FXML
    void initialize() {
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        dateCol.setCellValueFactory(cellData -> {
            Operation operation = cellData.getValue();
            if (operation.getDateOp() != null) {
                LocalDateTime dateTime = LocalDateTime.ofInstant(operation.getDateOp(), ZoneId.systemDefault());
                String formattedDate = formatter.format(dateTime);
                return new SimpleStringProperty(formattedDate);
            } else {
                return new SimpleStringProperty("N/A");
            }
        });
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
        if (compte != null && compte.getClient() != null) {
            String compteInfo = compte.getClient().getNom() + " " + compte.getClient().getPrenom() + " (" + compte.getNumero() + ")";
            compteInfoLabel.setText(compteInfo);
        } else {
            compteInfoLabel.setText("Aucun compte sélectionné");
        }
        loadOperations();
    }

    private void loadOperations() {
        if (compte != null) {
            operationsTable.setItems(FXCollections.observableArrayList(
                    operationDao.getOperationsByCompteId(compte.getId())
            ));
        } else {
            Notification.NotifError("Erreur", "Aucun compte sélectionné pour afficher l'historique");
        }
    }

    @FXML
    void filtrerParDate(ActionEvent event) {
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();

        if (dateDebut != null && dateFin != null && compte != null) {
            if (dateDebut.isAfter(dateFin)) {
                Notification.NotifError("Erreur", "La date de début doit être antérieure à la date de fin.");
                return;
            }

            List<Operation> filteredOperations = operationDao.getOperationsByCompteId(compte.getId())
                    .stream()
                    .filter(op -> {
                        LocalDate opDate = op.getDateOp().atZone(ZoneId.systemDefault()).toLocalDate();
                        return !opDate.isBefore(dateDebut) && !opDate.isAfter(dateFin);
                    })
                    .collect(Collectors.toList());

            operationsTable.setItems(FXCollections.observableArrayList(filteredOperations));
        } else {
            Notification.NotifError("Erreur", "Veuillez sélectionner une plage de dates et un compte.");
        }
    }

    @FXML
    void retour(ActionEvent event) {
        Outils.load(event, "Gestion des Comptes", "/fxml/comptes.fxml");
    }

    @FXML
    void genererReleve(ActionEvent event) {
        LocalDate dateDebut = dateDebutPicker.getValue();
        LocalDate dateFin = dateFinPicker.getValue();

        if (compte != null && dateDebut != null && dateFin != null) {
            if (dateDebut.isAfter(dateFin)) {
                Notification.NotifError("Erreur", "La date de début doit être antérieure à la date de fin.");
                return;
            }

            List<Operation> operations = operationDao.getOperationsByCompteId(compte.getId())
                    .stream()
                    .filter(op -> {
                        LocalDate opDate = op.getDateOp().atZone(ZoneId.systemDefault()).toLocalDate();
                        return !opDate.isBefore(dateDebut) && !opDate.isAfter(dateFin);
                    })
                    .collect(Collectors.toList());

            StatementGenerator.generateReleve(compte, operations, dateDebut, dateFin);
        } else {
            Notification.NotifError("Erreur", "Veuillez sélectionner une plage de dates et un compte.");
        }
    }
}