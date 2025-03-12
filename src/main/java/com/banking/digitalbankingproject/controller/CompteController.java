package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Client;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.enums.TypeCompte;
import com.banking.digitalbankingproject.service.IClient;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.impl.ClientImpl;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.util.AlertUtil;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.time.Instant;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class CompteController {
    private ICompte compteService = new CompteImpl();
    private IClient clientService = new ClientImpl();
    
    @FXML
    private TableView<Compte> compteTable;
    @FXML
    private TableColumn<Compte, String> numeroColumn;
    @FXML
    private TableColumn<Compte, Double> balanceColumn;
    @FXML
    private TableColumn<Compte, Instant> dateCreationColumn;
    @FXML
    private TableColumn<Compte, String> clientColumn;
    
    @FXML
    private ComboBox<TypeCompte> typeCompteCombo;
    @FXML
    private ComboBox<Client> clientCombo;
    @FXML
    private TextField soldeInitialField;
    
    private Compte selectedCompte;
    private ObservableList<Compte> compteList = FXCollections.observableArrayList();
    
    @FXML
    private BorderPane mainContainer;
    
    @FXML
    void initialize() {
        // Configuration des colonnes
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("numero"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
        dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        clientColumn.setCellValueFactory(cellData -> {
            Client client = cellData.getValue().getClient();
            return javafx.beans.binding.Bindings.createStringBinding(
                () -> client != null ? client.getNom() + " " + client.getPrenom() : ""
            );
        });
        
        // Initialisation des ComboBox
        typeCompteCombo.setItems(FXCollections.observableArrayList(TypeCompte.values()));
        chargerClients();
        
        // Chargement des comptes
        chargerComptes();
        
        // Listener pour la sélection d'un compte
        compteTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCompte = newSelection;
            }
        });
    }
    
    private void chargerClients() {
        try {
            List<Client> clients = clientService.getAllClients();
            clientCombo.setItems(FXCollections.observableArrayList(clients));
        } catch (Exception e) {
            AlertUtil.showError("Erreur de chargement", "Impossible de charger la liste des clients");
            e.printStackTrace();
        }
    }
    
    private void chargerComptes() {
        try {
            List<Compte> comptes = compteService.getAllComptes();
            compteList.clear();
            compteList.addAll(comptes);
            compteTable.setItems(compteList);
        } catch (Exception e) {
            AlertUtil.showError("Erreur de chargement", "Impossible de charger la liste des comptes");
            e.printStackTrace();
        }
    }
    
    @FXML
    void creerCompte(ActionEvent event) {
        if (!validerChamps()) {
            return;
        }
        
        try {
            Compte compte = new Compte();
            compte.setNumero(genererNumeroCompte());
            compte.setBalance(Double.parseDouble(soldeInitialField.getText().trim()));
            compte.setCreatedAt(Instant.now());
            compte.setClient(clientCombo.getValue());
            
            compteService.saveCompte(compte, typeCompteCombo.getValue());
            AlertUtil.showSuccess("Succès", "Compte créé avec succès");
            viderChamps();
            chargerComptes();
        } catch (Exception e) {
            AlertUtil.showError("Erreur de création", "Impossible de créer le compte");
            e.printStackTrace();
        }
    }
    
    private String genererNumeroCompte() {
        // Logique de génération de numéro de compte
        return "CPT" + System.currentTimeMillis();
    }
    
    @FXML
    void consulterSolde(ActionEvent event) {
        if (selectedCompte == null) {
            AlertUtil.showError("Sélection requise", "Veuillez sélectionner un compte");
            return;
        }
        
        AlertUtil.showInfo("Solde du compte", 
                "Numéro: " + selectedCompte.getNumero() + 
                "\nSolde actuel: " + selectedCompte.getBalance() + " €");
    }
    
    @FXML
    void consulterHistorique(ActionEvent event) {
        if (selectedCompte == null) {
            AlertUtil.showError("Sélection requise", "Veuillez sélectionner un compte");
            return;
        }
        
        try {
            // Stocker l'ID du compte sélectionné pour l'historique
            Outils.setCompteId(selectedCompte.getId());
            Outils.load(event, "Historique du compte", "/fxml/historique.fxml");
        } catch (Exception e) {
            AlertUtil.showError("Erreur de navigation", "Impossible d'accéder à l'historique");
            e.printStackTrace();
        }
    }
    
    private boolean validerChamps() {
        if (typeCompteCombo.getValue() == null || 
            clientCombo.getValue() == null || 
            soldeInitialField.getText().trim().isEmpty()) {
            AlertUtil.showError("Champs requis", "Tous les champs sont obligatoires");
            return false;
        }
        
        try {
            double solde = Double.parseDouble(soldeInitialField.getText().trim());
            if (solde < 0) {
                AlertUtil.showError("Valeur invalide", "Le solde initial ne peut pas être négatif");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showError("Format invalide", "Le solde doit être un nombre valide");
            return false;
        }
        
        return true;
    }
    
    private void viderChamps() {
        typeCompteCombo.setValue(null);
        clientCombo.setValue(null);
        soldeInitialField.clear();
        selectedCompte = null;
        compteTable.getSelectionModel().clearSelection();
    }
    
    @FXML
    void retourAccueil() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/fxml/accueil.fxml"));
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void deconnexion() {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(view);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            Stage stage = (Stage) mainContainer.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
