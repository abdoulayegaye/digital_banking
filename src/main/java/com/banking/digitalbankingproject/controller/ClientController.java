package com.banking.digitalbankingproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ClientController {

    @FXML
    private TableView<?> clientTable;

    @FXML
    private Button effacerBtn;

    @FXML
    private TableColumn<?, ?> emailCol;

    @FXML
    private TextField emailTfd;

    @FXML
    private Button enregistrerBtn;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private Button modifierBtn;

    @FXML
    private TableColumn<?, ?> nomCol;

    @FXML
    private TextField nomTfd;

    @FXML
    private TableColumn<?, ?> prenomCol;

    @FXML
    private TextField prenomTfd;

    @FXML
    private Button retourBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchTfd;

    @FXML
    private Button supprimerBtn;

    @FXML
    void effacer(ActionEvent event) {

    }

    @FXML
    void enregistrer(ActionEvent event) {

    }

    @FXML
    void modifier(ActionEvent event) {

    }

    @FXML
    void supprimer(ActionEvent event) {

    }

}
