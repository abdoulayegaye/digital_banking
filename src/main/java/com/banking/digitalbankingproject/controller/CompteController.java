package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.sql.Timestamp;
import java.time.LocalDate;

public class CompteController {

    @FXML private TextField numeroField;
    @FXML private TextField soldeField;
    @FXML private DatePicker dateOuvertureField;
    @FXML private ComboBox<Client> clientComboBox;
    @FXML private TableView<Compte> compteTable;
    @FXML private TableColumn<Compte, String> numeroCol;
    @FXML private TableColumn<Compte, Double> soldeCol;
    @FXML private TableColumn<Compte, Timestamp> dateCol;
    @FXML private TableColumn<Compte, String> clientCol;

    private ObservableList<Compte> comptes = FXCollections.observableArrayList();
    private ICompte compteService = new CompteImpl();
    private IClient clientService = new ClientImpl();

    @FXML
    public void initialize() {
        numeroCol.setCellValueFactory(cellData -> cellData.getValue().numeroProperty());
        soldeCol.setCellValueFactory(cellData -> cellData.getValue().soldeProperty().asObject());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateOuvertureProperty());
        clientCol.setCellValueFactory(cellData -> cellData.getValue().clientNomProperty());


        clientComboBox.setItems(FXCollections.observableArrayList(clientService.listerClients()));
        clientComboBox.setCellFactory(param -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                setText(empty || client == null ? null : client.getNom() + " " + client.getPrenom());
            }
        });
        clientComboBox.setButtonCell(clientComboBox.getCellFactory().call(null));

        // Charger les comptes
        chargerComptes();
    }

    private void chargerComptes() {
        comptes.setAll(compteService.listerComptes());
        for (Compte compte : comptes) {
            Client client = clientService.consulterClient(String.valueOf(compte.getClientId()));
            compte.setClientNom(client != null ? client.getNom() + " " + client.getPrenom() : "N/A");
        }
        compteTable.setItems(comptes);
    }

    @FXML
    private void creerCompte(ActionEvent event) {
        String numero = numeroField.getText();
        String soldeText = soldeField.getText();
        LocalDate dateOuverture = dateOuvertureField.getValue();
        Client client = clientComboBox.getValue();

        if (numero.isEmpty() || soldeText.isEmpty() || dateOuverture == null || client == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            double solde = Double.parseDouble(soldeText);
            if (solde < 0) {
                showAlert("Erreur", "Le solde ne peut pas être négatif.");
                return;
            }

            Compte compte = new Compte(0, numero, solde, Timestamp.valueOf(dateOuverture.atStartOfDay()), client.getId());
            compteService.creerCompte(compte);
            chargerComptes();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le solde doit être un nombre valide.");
        }
    }

    @FXML
    private void consulterSolde(ActionEvent event) {
        Compte compte = compteTable.getSelectionModel().getSelectedItem();
        if (compte == null) {
            showAlert("Erreur", "Veuillez sélectionner un compte.");
            return;
        }
        numeroField.setText(compte.getNumero());
        soldeField.setText(String.valueOf(compte.getSolde()));
        dateOuvertureField.setValue(compte.getDateOuverture().toLocalDateTime().toLocalDate());
        clientComboBox.getSelectionModel().select(clientService.consulterClient(String.valueOf(compte.getClientId())));
    }

    @FXML
    private void fermerCompte(ActionEvent event) {
        Compte compte = compteTable.getSelectionModel().getSelectedItem();
        if (compte == null) {
            showAlert("Erreur", "Veuillez sélectionner un compte à fermer.");
            return;
        }
        compteService.fermerCompte(compte.getId());
        chargerComptes();
    }

    @FXML
    private void genererPdf(ActionEvent event) {
        Compte compte = compteTable.getSelectionModel().getSelectedItem();
        if (compte == null) {
            showAlert("Erreur", "Veuillez sélectionner un compte pour générer le PDF.");
            return;
        }
        compteService.genererPdf(compte.getId());
        showAlert("Succès", "PDF généré avec succès.");
    }

    private void clearFields() {
        numeroField.clear();
        soldeField.clear();
        dateOuvertureField.setValue(null);
        clientComboBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}