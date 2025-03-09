package com.banking.digitalbankingproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CompteController {

    @FXML
    private TableColumn<?, ?> clientCol;

    @FXML
    private ComboBox<?> clientCombo;

    @FXML
    private TableView<?> compteTable;

    @FXML
    private TableColumn<?, ?> dateCol;

    @FXML
    private DatePicker dateOuverturePicker;

    @FXML
    private Button effacerBtn;

    @FXML
    private Button enregistrerBtn;

    @FXML
    private Button fermerBtn;

    @FXML
    private TableColumn<?, ?> numCol;

    @FXML
    private TextField numCompteTfd;

    @FXML
    private Button retourBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchTfd;

    @FXML
    private TableColumn<?, ?> soldeCol;

    @FXML
    private TextField soldeTfd;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    void effacer(ActionEvent event) {

    }

    @FXML
    void enregistrer(ActionEvent event) {

    }

    @FXML
    void fermerCompte(ActionEvent event) {

    }

}
