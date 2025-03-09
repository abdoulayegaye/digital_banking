package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class GestionComptesController {

    @FXML
    private TableView<Compte> tableComptes;

    @FXML
    private TableColumn<Compte, String> columnNumero;

    @FXML
    private TableColumn<Compte, Double> columnSolde;

    @FXML
    private TableColumn<Compte, String> columnProprietaire;

    private ICompte compteService = new CompteImpl();
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize the table columns
        columnNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        columnSolde.setCellValueFactory(new PropertyValueFactory<>("solde"));
        columnProprietaire.setCellValueFactory(new PropertyValueFactory<>("proprietaire"));

        // Load account data
        chargerComptes();
    }

    public void chargerComptes() {
        comptesList.setAll(compteService.getAllComptes());
        tableComptes.setItems(comptesList);
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ajouterCompte.fxml"));
            Parent root = loader.load();

            // Pass the reference of this controller to the AjouterCompteController
            AjouterCompteController ajouterCompteController = loader.getController();
            ajouterCompteController.setGestionComptesController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter un Compte");
            stage.showAndWait();
            chargerComptes(); // Refresh the table after adding a new account
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSupprimer(ActionEvent event) {
        Compte selectedCompte = tableComptes.getSelectionModel().getSelectedItem();
        if (selectedCompte != null) {
            compteService.supprimerCompte(selectedCompte);
            chargerComptes();
            System.out.println("Supprimer un compte");
        }
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        Compte selectedCompte = tableComptes.getSelectionModel().getSelectedItem();
        if (selectedCompte != null) {
            // Example modification
            selectedCompte.setSolde(selectedCompte.getSolde() + 500.0);
            compteService.modifierCompte(selectedCompte);
            chargerComptes();
            System.out.println("Modifier un compte");
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        chargerComptes();
        System.out.println("Rafraîchir la liste des comptes");
    }

    public TableView<Compte> getTableViewComptes() {
        return tableComptes;
    }
}