package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class CompteController {

    @FXML
    private ComboBox<Client> clientComboBox;
    @FXML
    private TextField numCompteField, soldeField;
    @FXML
    private DatePicker dateOuverturePicker;
    @FXML
    private Button  btnRetourAccueil;
    @FXML
    private TableView<Compte> tableComptes;
    @FXML
    private TableColumn<Compte, String> colNumCompte;
    @FXML
    private TableColumn<Compte, String> colSolde;
    @FXML
    private TableColumn<Compte, String> colDateOuverture;
    @FXML
    private TableColumn<Compte, String> colClient;

    private ICompte compteService;
    private IClient clientService;
    private ObservableList<Compte> compteList = FXCollections.observableArrayList();

    public CompteController() throws SQLException {
        this.clientService = new ClientImpl(Db.getConnection());
        this.compteService = new CompteImpl(Db.getConnection());
    }

    private String genererNumeroCompte() {
        return "SN-" + System.currentTimeMillis();
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void viderChamps() {
        numCompteField.clear();
        soldeField.clear();
        dateOuverturePicker.setValue(null);
        clientComboBox.setValue(null);
    }
    private void remplirFormulaire(Compte compte) {
        //System.out.println(compte.getClient());
        if (compte != null) {
            numCompteField.setText(compte.getNumero());
            soldeField.setText(String.valueOf(compte.getBalance()));
            dateOuverturePicker.setValue(compte.getCreatedAt());
            clientComboBox.setValue(compte.getClient());
        }
    }

    @FXML
    public void initialize() {
        chargerClients();
        chargerComptes();

        // Générer un numéro de compte et l'afficher
        numCompteField.setText(genererNumeroCompte());

        // Initialiser la date d'ouverture avec la date du jour
        dateOuverturePicker.setValue(LocalDate.now());

        // Affichage "Prénom Nom" pour la ComboBox
        clientComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                setText((empty || client == null) ? null : client.getPrenom() + " " + client.getNom());
            }
        });

        clientComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Client client) {
                return (client != null) ? client.getPrenom() + " " + client.getNom() : "";
            }

            @Override
            public Client fromString(String string) {
                return null;
            }
        });

        colNumCompte.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumero()));
        colSolde.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getBalance())));
        colDateOuverture.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCreatedAt().toString()));

        colClient.setCellValueFactory(cellData -> {
            Client client = cellData.getValue().getClient();
            return new SimpleStringProperty(client != null ? client.getPrenom() + " " + client.getNom() : "Inconnu");
        });

        // Écouteur de sélection pour remplir le formulaire
        tableComptes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                remplirFormulaire(newSelection);
            }
        });
    }

    private void chargerClients() {
        List<Client> clients = clientService.getTousLesClients();
        if (clients != null) {
            clientComboBox.setItems(FXCollections.observableArrayList(clients));
        }
    }

    private void chargerComptes() {
        List<Compte> comptes = compteService.getTousLesComptes();
        if (comptes != null) {
            compteList.setAll(comptes);
            tableComptes.setItems(compteList);
        }
    }

    @FXML
    private void ajouterCompte() {
        String numero = numCompteField.getText();
        double solde = Double.parseDouble(soldeField.getText());
        LocalDate dateOuverture = dateOuverturePicker.getValue();
        Client client = clientComboBox.getValue();

        if (numero.isEmpty() || client == null || dateOuverture == null) {
            afficherAlerte("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Compte compte = new Compte(numero, solde, dateOuverture, client);
        compteService.ajouterCompte(compte);
        chargerComptes();
        viderChamps();
    }

    @FXML
    private void modifierCompte() {
        Compte selected = tableComptes.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setNumeroCompte(numCompteField.getText());
            selected.setBalance(Double.parseDouble(soldeField.getText()));
            selected.setCreatedAt(dateOuverturePicker.getValue());
            selected.setClient(clientComboBox.getValue());

            compteService.modifierCompte(selected);
            chargerComptes();
            viderChamps();
        } else {
            afficherAlerte("Erreur", "Sélectionnez un compte à modifier.");
        }
    }

    @FXML
    private void supprimerCompte() {
        Compte selected = tableComptes.getSelectionModel().getSelectedItem();
        if (selected != null) {
            compteService.supprimerCompte(selected.getId());
            chargerComptes();
            viderChamps();
        } else {
            afficherAlerte("Erreur", "Sélectionnez un compte à supprimer.");
        }
    }

    @FXML
    private void retourAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRetourAccueil.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void fermerCompte() {

    }
}
