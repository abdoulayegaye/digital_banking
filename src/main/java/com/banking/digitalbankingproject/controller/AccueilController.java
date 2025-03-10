package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AccueilController {

    // Injection des éléments FXML
    @FXML private Button clientBtn;
    @FXML private Text clientCount;
    @FXML private Button compteBtn;
    @FXML private Text compteCount;
    @FXML private Button deconnexionBtn;
    @FXML private Button transactionBtn;
    @FXML private Text transactionCount;

    // Services métier
    private final IClient clientService = new ClientImpl();
    private final ICompte compteService = new CompteImpl();
    private final IOperation operationService = new OperationImpl();

    @FXML
    public void initialize() {
        updateDashboardStats();
    }

    private void updateDashboardStats() {
        try {
            clientCount.setText(String.valueOf(clientService.countClients()));
            compteCount.setText(String.valueOf(compteService.countComptesActif()));
            transactionCount.setText(String.valueOf(operationService.countTransactionsRecent()));
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les statistiques");
            e.printStackTrace();
        }
    }

    @FXML
    void deconnecter(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/login.fxml")));
            Stage stage = (Stage) deconnexionBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion - DIGITAL BANKING");
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger l'écran de connexion");
            e.printStackTrace();
        }
    }

    @FXML
    void ouvrirGestionClients(ActionEvent event) throws IOException {

        Outils.load(event,"Gestion des Clients","/fxml/clients.fxml");
    }

    @FXML
    void ouvrirGestionComptes(ActionEvent event) throws IOException {
        Outils.load(event,"Gestion des Comptes","/fxml/comptes.fxml");
    }

    @FXML
    void ouvrirGestionTransactions(ActionEvent event) throws IOException {
        Outils.load(event,"Gestion des Transactions","/fxml/operations.fxml");
    }

    private void loadView(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title + " - SN Bank");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la vue : " + title);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}