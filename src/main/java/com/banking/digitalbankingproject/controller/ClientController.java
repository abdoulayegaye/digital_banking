package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;

public class ClientController
{

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtPrenom;
    @FXML
    private TextField txtEmail;
    @FXML
    private TableView<?> tableClients;
    @FXML
    private TableColumn<?, ?> colNom;
    @FXML
    private TableColumn<?, ?> colPrenom;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;

    // Méthode pour ajouter un client
    @FXML
    private void ajouterClient() {
        // TODO: Implémenter l'ajout d'un client
    }

    // Méthode pour modifier un client
    @FXML
    private void modifierClient() {
        // TODO: Implémenter la modification d'un client
    }

    // Méthode pour supprimer un client
    @FXML
    private void supprimerClient() {
        // TODO: Implémenter la suppression d'un client
    }
}

