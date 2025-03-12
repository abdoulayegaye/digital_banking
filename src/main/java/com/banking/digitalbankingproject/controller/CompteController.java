package com.banking.digitalbankingproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class CompteController {

    @FXML
    private TextField numeroCompteField;

    @FXML
    private TextField soldeField;

    @FXML
    private DatePicker dateOuverturePicker;

    @FXML
    private ComboBox<String> clientComboBox;


    @FXML
    private TableView<Compte> tableView;

    @FXML
    private TableColumn<Compte, String> numeroCompteCol;

    @FXML
    private TableColumn<Compte, Double> soldeCol;

    @FXML
    private TableColumn<Compte, LocalDate> dateOuvertureCol;

    @FXML
    private TableColumn<Compte, String> clientCol;


    @FXML
    private Button consulterSolde;

    @FXML
    private Button fermerCompteBtn;


    private ObservableList<Compte> comptes = FXCollections.observableArrayList();


    @FXML
    public void initialize() {

        numeroCompteCol.setCellValueFactory(new PropertyValueFactory<>("numeroCompte"));
        soldeCol.setCellValueFactory(new PropertyValueFactory<>("solde"));
        dateOuvertureCol.setCellValueFactory(new PropertyValueFactory<>("dateOuverture"));
        clientCol.setCellValueFactory(new PropertyValueFactory<>("client"));

        tableView.setItems(comptes);

        clientComboBox.setItems(FXCollections.observableArrayList("Oumar", "Fallou", "Modou"));
    }

    @FXML
    private void creerCompteBtn() {
        String numeroCompte = numeroCompteField.getText();
        String soldeText = soldeField.getText();
        LocalDate dateOuverture = dateOuverturePicker.getValue();
        String client = clientComboBox.getValue();

        if (numeroCompte.isEmpty() || soldeText.isEmpty() || dateOuverture == null || client == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        double solde;
        try {
            solde = Double.parseDouble(soldeText);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le solde doit être un nombre valide !");
            return;
        }

        Compte compte = new Compte(numeroCompte, solde, dateOuverture, client);

        comptes.add(compte);

        clearForm();

        tableView.refresh();
    }

    private void clearForm() {
    }

    @FXML
    private void consulterSoldeBtn() {
        Compte compteSelectionne = tableView.getSelectionModel().getSelectedItem();

        if (compteSelectionne != null) {
            showAlert("Solde du compte", "Le solde du compte " + compteSelectionne.getNumeroCompte() + " est : " + compteSelectionne.getSolde() + " €");
        } else {
            // Aucun compte sélectionné
            showAlert("Aucun compte sélectionné", "Veuillez sélectionner un compte dans la table.");
        }
    }

    @FXML
    private void fermerCompteBtn() {
        Compte compteSelectionne = tableView.getSelectionModel().getSelectedItem();

        if (compteSelectionne != null) {
            comptes.remove(compteSelectionne);

            showAlert("Compte fermé", "Le compte " + compteSelectionne.getNumeroCompte() + " a été fermé.");

            tableView.refresh();
        } else {
            showAlert("Aucun compte sélectionné", "Veuillez sélectionner un compte à fermer.");
        }
    }

    private void FermerCompteBtn() {
        numeroCompteField.clear();
        soldeField.clear();
        dateOuverturePicker.setValue(null);
        clientComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    public void retourAccueilBtn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
            Parent root = loader.load();

            Button retourButton = null;
            Stage stage = (Stage) retourButton.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la page précédente.");
        }
    }


    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static class Compte {
        private String numeroCompte;
        private double solde;
        private LocalDate dateOuverture;
        private String client;


        public Compte(String numeroCompte, double solde, LocalDate dateOuverture, String client) {
            this.numeroCompte = numeroCompte;
            this.solde = solde;
            this.dateOuverture = dateOuverture;
            this.client = client;
        }


        public String getNumeroCompte() {
            return numeroCompte;
        }

        public double getSolde() {
            return solde;
        }

        public LocalDate getDateOuverture() {
            return dateOuverture;
        }

        public String getClient() {
            return client;
        }
    }
}