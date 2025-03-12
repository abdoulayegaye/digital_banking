package com.banking.digitalbankingproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import com.banking.digitalbankingproject.model.Client;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;

public class ClientController {

    @FXML
    private TextField nomField, prenomField, emailField, idField;

    @FXML
    private TableView<Client> clientTableView;

    @FXML
    private TableColumn<Client, Integer> idClientColumn;

    @FXML
    private TableColumn<Client, String> nomClientColumn;

    @FXML
    private TableColumn<Client, String> prenomClientColumn;

    @FXML
    private TableColumn<Client, String> emailClientColumn;

    private ObservableList<Client> clientList = FXCollections.observableArrayList();
    private Client clientSelectionne;

    @FXML
    public void initialize() {
        idClientColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomClientColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomClientColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailClientColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        clientTableView.setItems(clientList);

        clientTableView.setOnMouseClicked(event -> selectionnerClient());
    }

    @FXML
    public void ajouterClient() {
        try {
            int id = Integer.parseInt(idField.getText());
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String email = emailField.getText();

            for (Client c : clientList) {
                if (c.getId() == id) {
                    System.out.println("Un client avec cet ID existe déjà !");
                    return;
                }
            }

            Client nouveauClient = new Client(id, nom, prenom, email);

            clientList.add(nouveauClient);

            resetChamps();

        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un ID valide.");
        }
    }
    @FXML
    public void selectionnerClient() {
        clientSelectionne = clientTableView.getSelectionModel().getSelectedItem();
        if (clientSelectionne != null) {
            System.out.println("Client sélectionné : " + clientSelectionne.getNom()); // Debug
            idField.setText(String.valueOf(clientSelectionne.getId()));
            nomField.setText(clientSelectionne.getNom());
            prenomField.setText(clientSelectionne.getPrenom());
            emailField.setText(clientSelectionne.getEmail());
        } else {
            System.out.println("Aucun client sélectionné.");
        }
    }
    @FXML
    public void modifierClient() {
        if (clientSelectionne != null) {
            System.out.println("Modification en cours pour : " + clientSelectionne.getNom());

            int index = clientList.indexOf(clientSelectionne);

            if (index != -1) {
                clientSelectionne.setNom(nomField.getText());
                clientSelectionne.setPrenom(prenomField.getText());
                clientSelectionne.setEmail(emailField.getText());

                clientList.set(index, clientSelectionne);

                clientTableView.refresh();
            }

            resetChamps();
        } else {
            System.out.println("Aucun client sélectionné.");
        }
    }
    @FXML
    public void supprimerClient() {
        Client clientASupprimer = clientTableView.getSelectionModel().getSelectedItem();
        if (clientASupprimer != null) {
            clientList.remove(clientASupprimer);
            resetChamps();
        } else {
            System.out.println("Aucun client sélectionné.");
        }
    }
    private void resetChamps() {
        idField.clear();
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        clientSelectionne = null;
    }

    @FXML
    private Button retourButton;

    @FXML
    public void retourPagePrecedente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) retourButton.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de la page précédente.");
        }
    }
}
