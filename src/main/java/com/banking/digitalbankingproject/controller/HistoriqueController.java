package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class HistoriqueController {

    @FXML
    private TableColumn<Operation, Timestamp> date;

    @FXML
    private TableColumn<Operation, Double> montant;

    @FXML
    private TableColumn<Operation, String> num_c;

    @FXML
    private TableView<Operation> tableViewHistorique;

    @FXML
    private TableColumn<Operation, TypeOperation> type;

    private IOperation operationService = new OperationImpl();
    @Getter
    private static Operation operation = null;
    private List<Operation> operationList;

    @FXML
    private void initialize() {
        operationList = operationService.getAllOperations();
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        date.setCellValueFactory(new PropertyValueFactory<>("dateOp"));
        montant.setCellValueFactory(new PropertyValueFactory<>("amount"));
        num_c.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompte().getNumero()));

        tableViewHistorique.getItems().setAll(FXCollections.observableList(operationList));
    }

    @FXML
    void retour_o(ActionEvent event) {
        try {
            Outils.load(event, "Operations", "/fxml/operations.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
