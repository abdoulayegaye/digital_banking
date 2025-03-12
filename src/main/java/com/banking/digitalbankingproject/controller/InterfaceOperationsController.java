package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.io.IOException;

public class InterfaceOperationsController {
    @FXML
    private TableColumn<?, ?> dateCol;
    @FXML
    private Button depotBtn;
    @FXML
    private Button homeBtn;
    @FXML
    private TableColumn<?, ?> idCol;
    @FXML
    private TableColumn<?, ?> montantCol;
    @FXML
    private TableColumn<?, ?> numCompteCol;
    @FXML
    private Button rechargeBtn;
    @FXML
    private Button rechercheBtn;
    @FXML
    private Button retraitBtn;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<?, ?> soldeCol;
    @FXML
    private TableView<?> transactionsTbl;
    @FXML
    private TableColumn<?, ?> typeOpCol;
    @FXML
    private Button virementBtn;

    @FXML
    void depot(ActionEvent event) throws IOException {
        Outils.load(event, "Opération Dépôt", "/fxml/depot.fxml");
    }

    @FXML
    void home(ActionEvent event) throws IOException {
        Outils.load(event, "Bienvenue à Digital Banking", "/fxml/accueil.fxml");
    }

    @FXML
    void recharge(ActionEvent event) {
    }

    @FXML
    void retrait(ActionEvent event) throws IOException {
        Outils.load(event, "Opération Dépôt", "/fxml/retrait.fxml");
    }

    @FXML
    void searchHistorique(ActionEvent event) {

    }

    @FXML
    void virement(ActionEvent event) throws IOException {
        Outils.load(event, "Opération Dépôt", "/fxml/virement.fxml");
    }
}
