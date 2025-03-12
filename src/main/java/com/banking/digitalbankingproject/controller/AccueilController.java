package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AccueilController {

    private IClient clientService = new ClientImpl();
    private ICompte compteService = new CompteImpl();

    @FXML
    private void initialize() {
        // Charger les statistiques
        chargerStatistiques();
    }

    private void chargerStatistiques() {
        // Logique de chargement des statistiques (si nécessaire)
    }

    @FXML
    private void allerGestionClients(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/gestionClients.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Clients");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerGestionComptes(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/gestionComptes.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Comptes");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allerGestionOperations(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/gestionOperations.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Opérations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deconnexion(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}