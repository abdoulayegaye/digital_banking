package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class CompteController {

    @FXML
    private TableColumn<Compte, String> clientCol;

    @FXML
    private ComboBox<Client> clientCombo;

    @FXML
    private TableView<Compte> compteTable;

    @FXML
    private TableColumn<Compte, LocalDate> dateCol;

    @FXML
    private DatePicker dateOuverturePicker;

    @FXML
    private Button effacerBtn;

    @FXML
    private Button enregistrerBtn;

    @FXML
    private Button fermerBtn;

    @FXML
    private TableColumn<Compte, String> numCol;

    @FXML
    private TextField numCompteTfd;

    @FXML
    private Button retourBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchTfd;

    @FXML
    private TableColumn<Compte, Double> soldeCol;

    @FXML
    private TextField soldeTfd;

    private final ICompte compteService = new CompteImpl();
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();
    private ObservableList<Client> clientsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        numCompteTfd.setDisable(true);
        numCompteTfd.setEditable(false);
        numCol.setCellValueFactory(new PropertyValueFactory<>("numero"));
        soldeCol.setCellValueFactory(new PropertyValueFactory<>("balance"));
        dateCol.setCellValueFactory(cellData -> {
            Compte compte = cellData.getValue();
            LocalDate localDate = compte.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
            return new SimpleObjectProperty<>(localDate);
        });

        clientCol.setCellValueFactory(cellData -> {
            Compte compte = cellData.getValue();
            return new SimpleStringProperty(compte.getClient().getNom());
        });

        loadComptes();

        loadClients();
    }

    private void loadComptes() {
        comptesList.clear();
        comptesList.addAll(compteService.getAllComptes());
        compteTable.setItems(comptesList);
    }

    private void loadClients() {
        clientsList.clear();
        clientsList.addAll(compteService.loadClients());
        clientCombo.setItems(clientsList);
    }

    @FXML
    void effacer(ActionEvent event) {
        clearFields();
    }

    @FXML
    void enregistrer(ActionEvent event) {
        String numero = compteService.generateAccountNumber();
        numCompteTfd.setText(numero);

        double solde;
        try {
            solde = Double.parseDouble(soldeTfd.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le solde doit être un nombre valide !");
            return;
        }

        LocalDate dateOuverture = dateOuverturePicker.getValue();
        Client client = clientCombo.getSelectionModel().getSelectedItem();

        if (client == null || dateOuverture == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        Compte compte = new Compte();
        compte.setNumero(numero);
        compte.setBalance(solde);
        compte.setCreatedAt(dateOuverture.atStartOfDay(ZoneId.systemDefault()).toInstant());
        compte.setClient(client);

        int result = compteService.addCompte(compte);

        if (result > 0) {
            showAlert("Succès", "Compte ajouté avec succès !");
            clearFields();
            loadComptes();
        } else {
            showAlert("Erreur", "Échec de l'ajout du compte !");
        }
    }

    @FXML
    void fermerCompte(ActionEvent event) {
        Compte selectedCompte = compteTable.getSelectionModel().getSelectedItem();
        if (selectedCompte == null) {
            showAlert("Erreur", "Veuillez sélectionner un compte à fermer !");
            return;
        }

        int result = compteService.fermerCompte(selectedCompte.getId());

        if (result > 0) {
            showAlert("Succès", "Compte fermé avec succès !");
            loadComptes();
        } else {
            showAlert("Erreur", "Échec de la fermeture du compte !");
        }
    }

    @FXML
    void searchComptes(ActionEvent event) {
        String searchText = searchTfd.getText().trim();
        if (searchText.isEmpty()) {
            loadComptes();
        } else {
            List<Compte> searchResults = compteService.searchCompteByNumero(searchText);
            if (searchResults.isEmpty()) {
                showAlert("Information", "Aucun compte trouvé avec ce numéro.");
            } else {
                comptesList.clear();
                comptesList.addAll(searchResults);
                compteTable.setItems(comptesList);
            }
        }
    }

    @FXML
    void retour(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
            Parent root = loader.load();
            Scene scene = retourBtn.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'écran précédent.");
        }
    }

    private void clearFields() {
        numCompteTfd.clear();
        soldeTfd.clear();
        dateOuverturePicker.setValue(null);
        clientCombo.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}