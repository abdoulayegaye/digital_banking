package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;

import java.io.IOException;

public class AssocierClientController {

    @FXML
    private ComboBox<Client> comboClient;

    @FXML
    private Button btnAssocier;
    @FXML
    private Button btnRetour;

    private Compte compte;
    private IClient clientService = new ClientImpl();
    private ICompte compteService = new CompteImpl();
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();
    private GestionComptesController gestionComptesController;

    @FXML
    private void initialize() {
        // Charger la liste des clients dans le ComboBox
        chargerClients();

        // Configurer le ComboBox pour afficher le prénom et le nom des clients
        comboClient.setCellFactory(param -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getNom() == null || item.getPrenom() == null) {
                    setText(null);
                } else {
                    setText(item.getPrenom() + " " + item.getNom());
                }
            }
        });

        comboClient.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getNom() == null || item.getPrenom() == null) {
                    setText(null);
                } else {
                    setText(item.getPrenom() + " " + item.getNom());
                }
            }
        });

        btnAssocier.setOnAction(event -> associerClient());
        btnRetour.setOnAction(event -> retourGestionComptes());
    }

    private void chargerClients() {
        clientsList.clear();
        clientsList.addAll(clientService.getAllClients());
        comboClient.setItems(clientsList);
    }

    @FXML
    private void associerClient() {
        Client clientSelectionne = comboClient.getSelectionModel().getSelectedItem();
        if (clientSelectionne == null) {
            Outils.showError("Erreur", "Veuillez sélectionner un client.");
            return;
        }

        // Associer le client au compte
        compte.setClient(clientSelectionne);
        compteService.updateCompte(compte);
        // Rafraîchir la liste des comptes dans le contrôleur principal
        if (gestionComptesController != null) {
            gestionComptesController.chargerComptes();
            gestionComptesController.getTableViewComptes().refresh();
        }
        // Afficher un message de succès et fermer la fenêtre
        Outils.showSuccess("Succès", "Client associé avec succès.");
        Stage stage = (Stage) btnRetour.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void retourGestionComptes() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Accueil.fxml"));
            Stage stage = (Stage) btnRetour.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }

    public void setGestionComptesController(GestionComptesController gestionComptesController) {
        this.gestionComptesController = gestionComptesController;
    }
}