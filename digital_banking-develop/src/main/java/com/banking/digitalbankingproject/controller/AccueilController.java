package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class AccueilController {

    @FXML
    private Text BienvenueTfd;

    @FXML
    private Button CrudClientsCol;

    @FXML
    private TextField CrudClientsTfd;

    @FXML
    private Button CrudComptesCol;

    @FXML
    private TextField CrudComptesTfd;

    @FXML
    private Button CrudOperationsCol;

    @FXML
    private TextField CrudOperationsTfd;


    @FXML
    void Clients(ActionEvent event) throws IOException {
        Outils.load(event, "Bienvenue à Digital Banking", "/fxml/clients.fxml");
    }

    @FXML
    void Comptes(ActionEvent event) throws IOException {
        Outils.load(event, "Bienvenue à Digital Banking", "/fxml/comptes.fxml");
    }

    @FXML
    void Operations(ActionEvent event) throws IOException {
        Outils.load(event, "Bienvenue à Digital Banking", "/fxml/operations.fxml");
    }

}
