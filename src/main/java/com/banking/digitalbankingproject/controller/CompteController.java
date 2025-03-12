package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CompteController {
    @FXML
    private TextField idField;
    @FXML
    private TextField numeroField;
    @FXML
    private TextField balanceField;
    @FXML
    private TextField createdAtField;
    @FXML
    private TextField clientIdField;
    @FXML
    private Label messageLabel;
    @FXML
    private TableView<Compte> compteTable;
    @FXML
    private TableColumn<Compte, Integer> idColumn;
    @FXML
    private TableColumn<Compte, String> numeroColumn;
    @FXML
    private TableColumn<Compte, Double> balanceColumn;
    @FXML
    private TableColumn<Compte, String> createdAtColumn;
    @FXML
    private TableColumn<Compte, Integer> clientIdColumn;

    private ICompte compteService = new CompteImpl(); // Utilisation de l'interface
    private ObservableList<Compte> compteList = FXCollections.observableArrayList();
    @FXML
    private void handleHomeButton(ActionEvent event) {
        try {
            // Charger la vue de gestion des clients
            Outils.load(event, "Retour ", "/fxml/accueil.fxml");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement  : " + e.getMessage());
        }
    }
    @FXML
    public void initialize() {
        // Associer les colonnes aux propriétés du modèle Compte
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        clientIdColumn.setCellValueFactory(new PropertyValueFactory<>("clientId"));

        // Charger la liste des comptes dans la TableView
        refreshCompteList();
    }

    // Créer un compte
    @FXML
    public void createCompte() {
        String numero = numeroField.getText();
        double balance = Double.parseDouble(balanceField.getText());
        String createdAt = createdAtField.getText();
        int clientId = Integer.parseInt(clientIdField.getText());

        if (numero.isEmpty() || createdAt.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        Compte compte = new Compte();
        compte.setNumero(numero);
        compte.setBalance(balance);
        compte.setCreatedAt(createdAt);
        compte.setClientId(clientId);

        if (compteService.createCompte(compte)) {
            messageLabel.setText("Compte créé avec succès !");
            refreshCompteList();
            clearFields();
        } else {
            messageLabel.setText("Erreur lors de la création du compte.");
        }
    }

    // Modifier un compte
    @FXML
    public void updateCompte() {
        try {
            int id = Integer.parseInt(idField.getText());
            String numero = numeroField.getText();
            double balance = Double.parseDouble(balanceField.getText());
            String createdAt = createdAtField.getText();
            int clientId = Integer.parseInt(clientIdField.getText());

            if (compteService.updateCompte(id, numero, balance, createdAt, clientId)) {
                messageLabel.setText("Compte modifié avec succès !");
                refreshCompteList();
                clearFields();
            } else {
                messageLabel.setText("Compte non trouvé.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("ID invalide.");
        }
    }

    // Supprimer un compte
    @FXML
    public void deleteCompte() {
        try {
            int id = Integer.parseInt(idField.getText());

            if (compteService.deleteCompte(id)) {
                messageLabel.setText("Compte supprimé avec succès !");
                refreshCompteList();
                clearFields();
            } else {
                messageLabel.setText("Compte non trouvé.");
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("ID invalide.");
        }
    }

    // Rafraîchir la liste des comptes
    @FXML
    public void refreshCompteList() {
        compteList.setAll(compteService.getAllComptes());
        compteTable.setItems(compteList);
    }

    // Effacer les champs du formulaire
    private void clearFields() {
        idField.clear();
        numeroField.clear();
        balanceField.clear();
        createdAtField.clear();
        clientIdField.clear();
    }
}