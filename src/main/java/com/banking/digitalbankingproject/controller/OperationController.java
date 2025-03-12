package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.util.AlertUtil;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class OperationController {
    private IOperation operationService = new OperationImpl();
    private ICompte compteService = new CompteImpl() {
        @Override
        public List<Compte> getAllComptes() throws SQLException {
            return List.of();
        }

        @Override
        public Compte getCompteById(int id) throws SQLException {
            return null;
        }

        @Override
        public Compte getCompteByNumero(String numero) throws SQLException {
            return null;
        }

        @Override
        public List<Compte> getComptesByClientId(int clientId) throws SQLException {
            return List.of();
        }

        @Override
        public boolean updateBalance(int compteId, double newBalance) throws SQLException {
            return false;
        }

        @Override
        public boolean deleteCompte(int id) throws SQLException {
            return false;
        }
    };
    
    @FXML
    private ComboBox<Compte> compteSourceCombo;
    @FXML
    private ComboBox<Compte> compteDestinationCombo;
    @FXML
    private TextField montantField;
    
    @FXML
    private BorderPane mainContainer;
    
    @FXML
    void initialize() {
        chargerComptes();
    }
    
    private void chargerComptes() {
        try {
            List<Compte> comptes = compteService.getAllComptes();
            compteSourceCombo.setItems(FXCollections.observableArrayList(comptes));
            compteDestinationCombo.setItems(FXCollections.observableArrayList(comptes));
            
            // Configuration de l'affichage des comptes dans les ComboBox
            compteSourceCombo.setCellFactory(param -> new ListCell<Compte>() {
                @Override
                protected void updateItem(Compte compte, boolean empty) {
                    super.updateItem(compte, empty);
                    if (empty || compte == null) {
                        setText(null);
                    } else {
                        setText(compte.getNumero() + " - " + 
                               (compte.getClient() != null ? compte.getClient().getNom() + " " + compte.getClient().getPrenom() : ""));
                    }
                }
            });
            
            compteDestinationCombo.setCellFactory(param -> new ListCell<Compte>() {
                @Override
                protected void updateItem(Compte compte, boolean empty) {
                    super.updateItem(compte, empty);
                    if (empty || compte == null) {
                        setText(null);
                    } else {
                        setText(compte.getNumero() + " - " + 
                               (compte.getClient() != null ? compte.getClient().getNom() + " " + compte.getClient().getPrenom() : ""));
                    }
                }
            });
        } catch (Exception e) {
            AlertUtil.showError("Erreur de chargement", "Impossible de charger la liste des comptes");
            e.printStackTrace();
        }
    }
    
    @FXML
    void effectuerDepot(ActionEvent event) {
        if (!validerChampsDepotRetrait()) {
            return;
        }
        
        try {
            Compte compte = compteSourceCombo.getValue();
            double montant = Double.parseDouble(montantField.getText().trim());
            
            operationService.effectuerOperation(compte.getId(), montant, TypeOperation.DEPOT);
            AlertUtil.showSuccess("Succès", "Dépôt effectué avec succès");
            viderChamps();
        } catch (Exception e) {
            AlertUtil.showError("Erreur d'opération", "Impossible d'effectuer le dépôt");
            e.printStackTrace();
        }
    }
    
    @FXML
    void effectuerRetrait(ActionEvent event) {
        if (!validerChampsDepotRetrait()) {
            return;
        }
        
        try {
            Compte compte = compteSourceCombo.getValue();
            double montant = Double.parseDouble(montantField.getText().trim());
            
            if (montant > compte.getBalance()) {
                AlertUtil.showError("Solde insuffisant", "Le montant du retrait dépasse le solde disponible");
                return;
            }
            
            operationService.effectuerOperation(compte.getId(), montant, TypeOperation.RETRAIT);
            AlertUtil.showSuccess("Succès", "Retrait effectué avec succès");
            viderChamps();
        } catch (Exception e) {
            AlertUtil.showError("Erreur d'opération", "Impossible d'effectuer le retrait");
            e.printStackTrace();
        }
    }
    
    @FXML
    void effectuerVirement(ActionEvent event) {
        if (!validerChampsVirement()) {
            return;
        }
        
        try {
            Compte compteSource = compteSourceCombo.getValue();
            Compte compteDestination = compteDestinationCombo.getValue();
            double montant = Double.parseDouble(montantField.getText().trim());
            
            if (montant > compteSource.getBalance()) {
                AlertUtil.showError("Solde insuffisant", "Le montant du virement dépasse le solde disponible");
                return;
            }
            
            operationService.effectuerVirement(compteSource.getId(), compteDestination.getId(), montant);
            AlertUtil.showSuccess("Succès", "Virement effectué avec succès");
            viderChamps();
        } catch (Exception e) {
            AlertUtil.showError("Erreur d'opération", "Impossible d'effectuer le virement");
            e.printStackTrace();
        }
    }
    
    @FXML
    void genererReleve(ActionEvent event) {
        if (compteSourceCombo.getValue() == null) {
            AlertUtil.showError("Sélection requise", "Veuillez sélectionner un compte");
            return;
        }
        
        try {
            Compte compte = compteSourceCombo.getValue();
            // Stocker l'ID du compte pour le relevé
            Outils.setCompteId(compte.getId());
            Outils.load(event, "Relevé bancaire", "/fxml/releve.fxml");
        } catch (Exception e) {
            AlertUtil.showError("Erreur de navigation", "Impossible de générer le relevé");
            e.printStackTrace();
        }
    }
    
    private boolean validerChampsDepotRetrait() {
        if (compteSourceCombo.getValue() == null || montantField.getText().trim().isEmpty()) {
            AlertUtil.showError("Champs requis", "Veuillez sélectionner un compte et saisir un montant");
            return false;
        }
        
        try {
            double montant = Double.parseDouble(montantField.getText().trim());
            if (montant <= 0) {
                AlertUtil.showError("Valeur invalide", "Le montant doit être supérieur à zéro");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showError("Format invalide", "Le montant doit être un nombre valide");
            return false;
        }
        
        return true;
    }
    
    private boolean validerChampsVirement() {
        if (compteSourceCombo.getValue() == null || 
            compteDestinationCombo.getValue() == null || 
            montantField.getText().trim().isEmpty()) {
            AlertUtil.showError("Champs requis", "Tous les champs sont obligatoires");
            return false;
        }
        
        if (compteSourceCombo.getValue().equals(compteDestinationCombo.getValue())) {
            AlertUtil.showError("Comptes identiques", "Les comptes source et destination ne peuvent pas être identiques");
            return false;
        }
        
        try {
            double montant = Double.parseDouble(montantField.getText().trim());
            if (montant <= 0) {
                AlertUtil.showError("Valeur invalide", "Le montant doit être supérieur à zéro");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showError("Format invalide", "Le montant doit être un nombre valide");
            return false;
        }
        
        return true;
    }
    
    private void viderChamps() {
        compteSourceCombo.setValue(null);
        compteDestinationCombo.setValue(null);
        montantField.clear();
    }
    
    @FXML
    public void retourAccueil() {
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
