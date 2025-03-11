package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AccueilController implements Initializable {

    @FXML
    private Label clientsCountLabel;
    @FXML
    private Label comptesCountLabel;
    @FXML
    private Label operationsCountLabel;

    private IClient clientDao = new ClientImpl();
    private ICompte compteDao = new CompteImpl();
    private IOperation operationDao = new OperationImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshStats();
    }

    @FXML
    void refreshStats() {
        int clientsCount = clientDao.countClients();
        clientsCountLabel.setText("Clients : " + clientsCount);

        int comptesCount = compteDao.countComptes();
        comptesCountLabel.setText("Comptes : " + comptesCount);

        int operationsCount = operationDao.countOperations();
        operationsCountLabel.setText("Opérations : " + operationsCount);
    }

    @FXML
    void goToClients(ActionEvent event) {
        Outils.load(event, "Gestion des Clients", "/fxml/clients.fxml");
    }

    @FXML
    void goToComptes(ActionEvent event) {
        Outils.load(event, "Gestion des Comptes", "/fxml/comptes.fxml");
    }

    @FXML
    void goToOperations(ActionEvent event) {
        Outils.load(event, "Gestion des Opérations", "/fxml/operations.fxml");
    }

    @FXML
    void deconnecter(ActionEvent event) {
        Outils.load(event, "Connexion", "/fxml/login.fxml");
    }
}