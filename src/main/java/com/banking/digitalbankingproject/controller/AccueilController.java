package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AccueilController {

    @FXML
    private Button btnClient;

    @FXML
    private Button btnGestiondescompte;

    @FXML
    private Button btnOperation;

    @FXML
    private void gestion_des_clients(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des clients", "/fxml/clients.fxml");
    }

    @FXML
    void gestion_des_comptes(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des comptes", "/fxml/comptes.fxml");
    }

    @FXML
    void operations(ActionEvent event) throws IOException {
        Outils.load(event, "Opérations bancaires", "/fxml/operations.fxml");
    }
}
