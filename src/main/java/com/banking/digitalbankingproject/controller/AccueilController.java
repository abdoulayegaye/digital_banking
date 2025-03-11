package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.SneakyThrows;

import java.io.IOException;

public class AccueilController {
    @FXML
    private HBox clientsHB;

    @FXML
    private HBox comptesHB;

    @FXML
    private HBox operationsHB;

    @FXML
    private HBox profileHB;

    @FXML
    private Label titrelbl;

    @FXML
    private BorderPane mainPane;


    @SneakyThrows
    @FXML
    void Deconexion(ActionEvent event) {
        Outils.load(event, "Page de connexion", "/fxml/login.fxml");
    }

    @FXML
    void GestionClients(MouseEvent event) throws IOException {
        Outils.loadI("GESTION BANCAIRE : CLIENTS", "/fxml/clients.fxml",titrelbl,mainPane);
    }

    @FXML
    void GestionComptes(MouseEvent event) throws IOException {
        Outils.loadI("GESTION BANCAIRE : COMPTES", "/fxml/comptes.fxml",titrelbl,mainPane);
    }

    @SneakyThrows
    @FXML
    void Operations(MouseEvent event) {
        Outils.loadI("GESTION BANCAIRE : OPERATIONS","/fxml/operations.fxml",titrelbl,mainPane);

    }

    @FXML
    void Profile(MouseEvent event) {

    }

}
