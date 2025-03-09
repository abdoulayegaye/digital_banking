package com.banking.digitalbankingproject.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class OperationController {

    @FXML
    private ComboBox<?> compteCibleCombo;

    @FXML
    private TableColumn<?, ?> compteCol;

    @FXML
    private ComboBox<?> compteSourceCombo;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private DatePicker dateOperationPicker;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> montantCol;

    @FXML
    private TextField montantField;

    @FXML
    private Button pdfBtn;

    @FXML
    private Button retourBtn;

    @FXML
    private TableView<?> transactionsTable;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private ComboBox<?> typeOperationCombo;

    @FXML
    private Button validerBtn;

    @FXML
    void genererPDF(ActionEvent event) {

    }

    @FXML
    void retour(ActionEvent event) {

    }

    @FXML
    void validerOperation(ActionEvent event) {

    }

}
