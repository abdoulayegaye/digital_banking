package com.banking.digitalbankingproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.impl.ClientImpl;

import java.io.IOException;

public class ClientController {

    @FXML
    private TextField NomTfd;

    @FXML
    private TextField PrenomTfd;

    @FXML
    private TextField EmailTfd;

    @FXML
    private Button RetourBtn;




    @FXML
    private TableView<Client> clientTable;

    @FXML
    private TableColumn<Client, String> nomCol;

    @FXML
    private TableColumn<Client, String> prenomCol;

    @FXML
    private TableColumn<Client, String> emailCol;

    private ObservableList<Client> clients = FXCollections.observableArrayList();

    private IClient clientService = new ClientImpl();

    @FXML
    public void initialize() {
        nomCol.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prenomCol.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        clients.addAll(clientService.listerClients());

        clientTable.setItems(clients);
    }

    @FXML
    private void save(ActionEvent event) {
        // Ajouter un nouveau client
        String nom = NomTfd.getText();
        String prenom = PrenomTfd.getText();
        String email = EmailTfd.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Client nouveauClient = new Client(0, nom, prenom, email);
        Client clientAjoute = clientService.ajouterClient(nouveauClient);

        if (clientAjoute != null) {
            clients.add(clientAjoute);
        } else {
            showAlert("Erreur", "Un client avec cet email existe déjà.");
        }
    }

    @FXML
    private void edit(ActionEvent event) {
        // Modifier un client existant
        Client clientSelectionne = clientTable.getSelectionModel().getSelectedItem();

        if (clientSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un client à modifier.");
            return;
        }

        String nom = NomTfd.getText();
        String prenom = PrenomTfd.getText();
        String email = EmailTfd.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        clientSelectionne.setNom(nom);
        clientSelectionne.setPrenom(prenom);
        clientSelectionne.setEmail(email);

        Client clientModifie = clientService.modifierClient(clientSelectionne);

        if (clientModifie != null) {
            clientTable.refresh();
        } else {
            showAlert("Erreur", "Le client n'a pas pu être modifié.");
        }
    }

    @FXML
    private void show(ActionEvent event) {
        Client clientSelectionne = clientTable.getSelectionModel().getSelectedItem();

        if (clientSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un client.");
            return;
        }

        NomTfd.setText(clientSelectionne.getNom());
        PrenomTfd.setText(clientSelectionne.getPrenom());
        EmailTfd.setText(clientSelectionne.getEmail());
    }

    @FXML
    private void delete(ActionEvent event) {
        Client clientSelectionne = clientTable.getSelectionModel().getSelectedItem();

        if (clientSelectionne == null) {
            showAlert("Erreur", "Veuillez sélectionner un client à supprimer.");
            return;
        }

        clientService.supprimerClient(clientSelectionne.getEmail());
        clients.remove(clientSelectionne);

    }


    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    }
