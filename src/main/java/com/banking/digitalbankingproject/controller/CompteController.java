package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

public class CompteController {
    @FXML
    private TableView<Compte> tableComptes;
    @FXML
    private TableColumn<Compte, String> colNumero;
    @FXML
    private TableColumn<Compte, Double> colSolde;
    @FXML
    private TableColumn<Compte, String> colClient;
    @FXML
    private TableColumn<Compte, Instant> colDateCreation;
    
    @FXML
    private ComboBox<Client> comboClients;
    @FXML
    private TextField txtNumero;
    @FXML
    private TextField txtSoldeInitial;
    
    @FXML
    private Button btnCreer;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnRetour;
    
    private final ICompte compteService = new CompteImpl();
    private final IClient clientService = new ClientImpl();
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colSolde.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colDateCreation.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colClient.setCellValueFactory(cellData -> {
            Client client = cellData.getValue().getClient();
            return javafx.beans.binding.Bindings.createStringBinding(
                () -> client != null ? client.getNom() + " " + client.getPrenom() : ""
            );
        });

        // Chargement des données
        chargerComptes();
        chargerClients();
    }

    private void chargerComptes() {
        List<Compte> comptes = compteService.getAllComptes();
        comptesList.setAll(comptes);
        tableComptes.setItems(comptesList);
    }

    private void chargerClients() {
        List<Client> clients = clientService.getAllClients();
        clientsList.setAll(clients);
        comboClients.setItems(clientsList);
    }

    @FXML
    void creerCompte(ActionEvent event) {
        if (txtNumero.getText().isEmpty() || txtSoldeInitial.getText().isEmpty() || comboClients.getValue() == null) {
            Notification.NotifError("Erreur", "Veuillez remplir tous les champs");
            return;
        }

        try {
            double soldeInitial = Double.parseDouble(txtSoldeInitial.getText());
            if (soldeInitial < 0) {
                Notification.NotifError("Erreur", "Le solde initial ne peut pas être négatif");
                return;
            }

            Compte nouveauCompte = new Compte();
            nouveauCompte.setNumero(txtNumero.getText());
            nouveauCompte.setBalance(soldeInitial);
            nouveauCompte.setClientId(comboClients.getValue().getId());
            nouveauCompte.setCreatedAt(Instant.now());

            if (compteService.createCompte(nouveauCompte)) {
                Notification.NotifSuccess("Succès", "Compte créé avec succès");
                chargerComptes();
                reinitialiserChamps();
            } else {
                Notification.NotifError("Erreur", "Erreur lors de la création du compte");
            }
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Le solde initial doit être un nombre valide");
        }
    }

    @FXML
    void supprimerCompte(ActionEvent event) {
        Compte compteSelectionne = tableComptes.getSelectionModel().getSelectedItem();
        if (compteSelectionne == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte à supprimer");
            return;
        }

        if (compteService.deleteCompte(compteSelectionne.getId())) {
            Notification.NotifSuccess("Succès", "Compte supprimé avec succès");
            chargerComptes();
        } else {
            Notification.NotifError("Erreur", "Erreur lors de la suppression du compte");
        }
    }

    @FXML
    void retourAccueil(ActionEvent event) throws IOException {
        Outils.load(event, "Accueil", "/fxml/accueil.fxml");
    }

    private void reinitialiserChamps() {
        txtNumero.clear();
        txtSoldeInitial.clear();
        comboClients.setValue(null);
    }
}
