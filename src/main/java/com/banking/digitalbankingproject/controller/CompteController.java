package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CompteController {

    private ICompte compteDao = new CompteImpl();

    @FXML
    private TextField numeroTfd;

    @FXML
    private TextField balanceTfd;

    @FXML
    private ComboBox<String> typeCombo;

    @FXML
    private ComboBox<Client> clientCombo;

    @FXML
    private TextField searchTfd;

    @FXML
    private TableView<Compte> comptesTable;

    @FXML
    private TableColumn<Compte, Integer> idCol;

    @FXML
    private TableColumn<Compte, String> numeroCol;

    @FXML
    private TableColumn<Compte, Double> balanceCol;

    @FXML
    private TableColumn<Compte, String> typeCol;

    @FXML
    private TableColumn<Compte, String> clientCol;

    @FXML
    private ComboBox<String> statutCombo;

    @FXML
    private TableColumn<Compte, String> statutCol;

    @FXML
    private DatePicker dateOuverturePicker;

    @FXML
    private TableColumn<Compte, LocalDate> dateOuvertureCol;

    @FXML
    void initialize() {
        numeroCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("typeCompte"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        dateOuvertureCol.setCellValueFactory(new PropertyValueFactory<>("dateOuverture"));

        clientCol.setCellValueFactory(cellData -> {
            Client client = cellData.getValue().getClient();
            return new SimpleStringProperty(client != null ? client.getNom() + " " + client.getPrenom() : "N/A");
        });

        typeCombo.getItems().setAll("COURANT", "EPARGNE");

        statutCombo.getItems().setAll("ACTIF", "INACTIF");

        clientCombo.setCellFactory(param -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getNom() + " " + client.getPrenom() + " (" + client.getEmail() + ")");
                }
            }
        });

        clientCombo.setButtonCell(new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                if (empty || client == null) {
                    setText(null);
                } else {
                    setText(client.getNom() + " " + client.getPrenom() + " (" + client.getEmail() + ")");
                }
            }
        });

        clientCombo.getItems().setAll(new ClientImpl().getAllClients());
        loadComptes();
    }

    @FXML
    void addCompte() {
        String numero = numeroTfd.getText().trim();
        String balanceText = balanceTfd.getText().trim();
        String selectedType = typeCombo.getSelectionModel().getSelectedItem();
        String selectedStatut = statutCombo.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = dateOuverturePicker.getValue();
        Client selectedClient = clientCombo.getSelectionModel().getSelectedItem();

        if (numero.isEmpty() || balanceText.isEmpty() || selectedType == null || selectedStatut == null || selectedDate == null) {
            Notification.NotifError("Erreur", "Tous les champs (Numéro, Solde, Type, Statut, Date d'ouverture) sont obligatoires");
        } else {
            try {
                double balance = Double.parseDouble(balanceText);
                Compte compte = new Compte();
                compte.setNumero(numero);
                compte.setBalance(balance);
                compte.setTypeCompte(selectedType);
                compte.setStatut(selectedStatut);
                compte.setDateOuverture(selectedDate);
                if (selectedClient != null) {
                    compte.setClient(selectedClient);
                }
                if (compteDao.createCompte(compte)) {
                    Notification.NotifSuccess("Succès", "Compte ajouté avec succès");
                    loadComptes();
                    clearFields();
                } else {
                    Notification.NotifError("Erreur", "Échec de l'ajout du compte");
                }
            } catch (NumberFormatException e) {
                Notification.NotifError("Erreur", "Le solde doit être un nombre valide");
            }
        }
    }

    @FXML
    void searchCompte() {
        String keyword = searchTfd.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            loadComptes();
        } else {
            List<Compte> filteredComptes = compteDao.getAllComptes().stream()
                    .filter(c -> c.getNumero().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
            comptesTable.setItems(FXCollections.observableArrayList(filteredComptes));
        }
    }

    private void loadComptes() {
        comptesTable.setItems(FXCollections.observableArrayList(compteDao.getAllComptes()));
    }

    private void clearFields() {
        numeroTfd.clear();
        balanceTfd.clear();
        typeCombo.getSelectionModel().clearSelection();
        clientCombo.getSelectionModel().clearSelection();
        searchTfd.clear();
    }

    @FXML
    void historiqueCompte(ActionEvent event) {
        // Récupérer le compte sélectionné
        Compte selectedCompte = comptesTable.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte pour consulter son historique");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/historiques.fxml"));
            Parent root = loader.load();

            HistoriqueController historiqueController = loader.getController();
            historiqueController.setCompte(selectedCompte);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Historique du Compte");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Impossible de charger l'historique : " + e.getMessage());
        }
    }

    @FXML
    void retour(ActionEvent event) {
        Outils.load(event, "Accueil", "/fxml/accueil.fxml");
    }
    @FXML
    void fermerCompte() {
        Compte selectedCompte = comptesTable.getSelectionModel().getSelectedItem();

        if (selectedCompte == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte à fermer");
            return;
        }

        if (compteDao.fermerCompte(selectedCompte.getId())) {
            Notification.NotifSuccess("Succès", "Compte fermé avec succès");
            loadComptes(); // Recharger la liste des comptes
        } else {
            Notification.NotifError("Erreur", "Échec de la fermeture du compte");
        }
    }

    @FXML
    void ouvrirCompte() {
        Compte selectedCompte = comptesTable.getSelectionModel().getSelectedItem();

        if (selectedCompte == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte à ouvrir");
            return;
        }

        if (compteDao.ouvrirCompte(selectedCompte.getId())) {
            Notification.NotifSuccess("Succès", "Compte ouvert avec succès");
            loadComptes();
        } else {
            Notification.NotifError("Erreur", "Échec de l'ouverture du compte");
        }
    }
}