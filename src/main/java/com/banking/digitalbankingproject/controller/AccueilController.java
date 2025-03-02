package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.Optional;

public class AccueilController {
    @FXML
    private Button clientsBtn;
    @FXML
    private Button compteBtn;
    @FXML
    private Button operationsBtn;
    @FXML
    private Button seDeconnecterBtn;
    @FXML
    private Label labelTotalClients;
    @FXML
    private Label labelTotalClientsCompte;
    @FXML
    private Label labelTotalComptesActif;
    @FXML
    private Label labelTotalComptesferme;

    @FXML
    void pageClients(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Clients", "/fxml/interfaceClients.fxml");
    }

    @FXML
    void pageCompte(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Comptes", "/fxml/interfaceComptes.fxml");
    }

    @FXML
    void pageOperations(ActionEvent event) throws IOException {
        Outils.load(event, "Gestion des Opérations", "/fxml/interfaceOperations.fxml");
    }

    private IClient iClient = new ClientImpl();
    private ICompte iCompte = new CompteImpl();

    public void initialize() {
        int totalClients = iClient.countClients();
        int totalClientsCompte = iCompte.countClientsComptes();
        int totalComptesActif = iCompte.countComptesActif();
        int totalFerme = iCompte.countComptesFerme();
        labelTotalClients.setText(""+totalClients);
        labelTotalClientsCompte.setText(""+totalClientsCompte);
        labelTotalComptesActif.setText(""+totalComptesActif);
        labelTotalComptesferme.setText(""+totalFerme);
    }

    @FXML
    void seDeconnecter(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText("Se déconnecter");
        alert.setContentText("Voulez-vous vraiment vous déconnecter ?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Db db = new Db();
            db.closeConnection();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
                Parent root = loader.load();

                seDeconnecterBtn.getScene().setRoot(root);

                Notification.NotifSuccess("Déconnexion", "Vous avez été déconnecté avec succès !");
            } catch (IOException e) {
                e.printStackTrace();
                Notification.NotifError("Erreur", "Impossible de retourner à la page de connexion !");
            }
        }
    }
}
