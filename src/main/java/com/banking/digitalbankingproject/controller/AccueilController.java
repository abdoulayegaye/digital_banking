package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AccueilController {

    @FXML
    private Button ClientBtn;

    @FXML
    private Button CompteBtn;

    @FXML
    private Button OperationBtn;

    @FXML
    void GestionClient(ActionEvent event) throws IOException {
        Outils.load(event,"Gestion des clients","/fxml/clients.fxml");
    }

    @FXML
    void GestionCompte(ActionEvent event) throws IOException {
        Outils.load(event,"Gestion des comptes","/fxml/comptes.fxml");

    }

    @FXML
    void GestionOperation(ActionEvent event) throws IOException {
        Outils.load(event,"Gestion des operations","/fxml/operations.fxml");

    }
    @FXML
    private void handleLogout(ActionEvent event) throws IOException {
        Outils.load(event,"Deconnexion","/fxml/login.fxml");
    }

}