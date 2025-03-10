package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import com.banking.digitalbankingproject.tools.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.ResourceBundle;

public class OperationController implements Initializable {
    
    @FXML private ComboBox<Compte> compteSourceCombo;
    @FXML private ComboBox<Compte> compteSourceVirementCombo;
    @FXML private ComboBox<Compte> compteDestCombo;
    @FXML private TextField montantTfd;
    @FXML private TextField montantVirementTfd;
    @FXML private TableView<Operation> operationTable;
    @FXML private TableColumn<Operation, Instant> dateCol;
    @FXML private TableColumn<Operation, String> typeCol;
    @FXML private TableColumn<Operation, Double> montantCol;
    @FXML private TableColumn<Operation, String> compteCol;
    
    private final IOperation operationDao = new OperationImpl();
    private final ICompte compteDao = new CompteImpl();
    private final ObservableList<Operation> operations = FXCollections.observableArrayList();
    private final ObservableList<Compte> comptesDepotRetrait = FXCollections.observableArrayList();
    private final ObservableList<Compte> comptesVirementSource = FXCollections.observableArrayList();
    private final ObservableList<Compte> comptesVirementDest = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initialisation du contrôleur des opérations...");
        
        dateCol.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("formattedType"));
        montantCol.setCellValueFactory(new PropertyValueFactory<>("formattedMontant"));
        compteCol.setCellValueFactory(new PropertyValueFactory<>("compteNumero"));
        
        montantCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        dateCol.setStyle("-fx-alignment: CENTER;");
        typeCol.setStyle("-fx-alignment: CENTER;");
        
        System.out.println("Chargement initial des données...");
        loadComptes();
        loadOperations();
        
        System.out.println("État initial de la TableView:");
        System.out.println("Nombre de colonnes: " + operationTable.getColumns().size());
        System.out.println("Nombre d'opérations chargées: " + operations.size());
    }

    @FXML
    void depot(ActionEvent event) {
        System.out.println("=== DÉBUT DE L'OPÉRATION DE DÉPÔT ===");
        Compte compte = compteSourceCombo.getValue();
        String montantStr = montantTfd.getText().trim();
        
        System.out.println("Compte sélectionné: " + (compte != null ? compte.getNumero() + " (ID: " + compte.getId() + ")" : "null"));
        System.out.println("Montant saisi: " + montantStr);
        
        if (compte == null || montantStr.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            System.out.println("Erreur: Champs obligatoires manquants");
            return;
        }
        
        try {
            double montant = Double.parseDouble(montantStr);
            System.out.println("Montant converti: " + montant);
            
            if (!Utils.isValidAmount(montant)) {
                Notification.NotifError("Erreur", "Le montant doit être positif");
                System.out.println("Erreur: Montant invalide");
                return;
            }
            
            System.out.println("Appel de la méthode depot avec compte=" + compte.getNumero() + ", montant=" + montant);
            int result = operationDao.depot(compte, montant);
            System.out.println("Résultat de l'opération de dépôt: " + result);
            
            clearFields();
            
            System.out.println("Rechargement des opérations...");
            loadOperations();
            
            System.out.println("Rechargement des comptes...");
            loadComptes(); // Recharge les comptes pour mettre à jour les soldes
            
            Notification.NotifSuccess("Succès", "Dépôt effectué avec succès");
            System.out.println("=== FIN DE L'OPÉRATION DE DÉPÔT ===");
        } catch (NumberFormatException e) {
            System.err.println("Erreur de format de nombre: " + e.getMessage());
            Notification.NotifError("Erreur", "Le montant doit être un nombre");
        } catch (Exception e) {
            System.err.println("Erreur lors du dépôt: " + e.getMessage());
            e.printStackTrace();
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    

    @FXML
    void retrait(ActionEvent event) {
        Compte compte = compteSourceCombo.getValue();
        String montantStr = montantTfd.getText().trim();
        
        if (compte == null || montantStr.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            return;
        }
        
        try {
            double montant = Double.parseDouble(montantStr);
            if (!Utils.isValidAmount(montant)) {
                Notification.NotifError("Erreur", "Le montant doit être positif");
                return;
            }
            
            operationDao.retrait(compte, montant);
            clearFields();
            loadOperations();
            loadComptes(); // Recharge les comptes pour mettre à jour les soldes
            Notification.NotifSuccess("Succès", "Retrait effectué avec succès");
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Le montant doit être un nombre");
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    @FXML
    void virement(ActionEvent event) {
        Compte source = compteSourceVirementCombo.getValue();
        Compte destination = compteDestCombo.getValue();
        String montantStr = montantVirementTfd.getText().trim();
        
        if (source == null || destination == null || montantStr.isEmpty()) {
            Notification.NotifError("Erreur", "Tous les champs sont obligatoires");
            return;
        }
        
        if (source.equals(destination)) {
            Notification.NotifError("Erreur", "Les comptes source et destination doivent être différents");
            return;
        }
        
        try {
            double montant = Double.parseDouble(montantStr);
            if (!Utils.isValidAmount(montant)) {
                Notification.NotifError("Erreur", "Le montant doit être positif");
                return;
            }
            
            operationDao.virement(source, destination, montant);
            clearFields();
            loadOperations();
            loadComptes(); // Recharge les comptes pour mettre à jour les soldes
            Notification.NotifSuccess("Succès", "Virement effectué avec succès");
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Le montant doit être un nombre");
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    @FXML
    void genererReleve(ActionEvent event) {
        Compte compte = compteSourceCombo.getValue();
        if (compte == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte");
            return;
        }
        
        try {
            List<Operation> operationsCompte = operationDao.getOperationsCompte(compte);
            
            operations.clear();
            operations.addAll(operationsCompte);
            operationTable.setItems(operations);
            
            Notification.NotifSuccess("Succès",
                String.format("Relevé généré avec succès : %d opération(s) trouvée(s) pour le compte %s", 
                    operationsCompte.size(), compte.getNumero()));
                    
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération du relevé : " + e.getMessage());
            e.printStackTrace();
            Notification.NotifError("Erreur", "Impossible de générer le relevé : " + e.getMessage());
        }
    }
    @FXML
    void goToAccueil(ActionEvent event) throws IOException {
        Outils.load(event, "Digital Banking", "/fxml/accueil.fxml");
    }
    private void loadOperations() {
        System.out.println("=== DÉBUT DU CHARGEMENT DES OPÉRATIONS ===");
        try {
            System.out.println("Nettoyage de la liste des opérations existantes");
            operations.clear();
            
            System.out.println("Récupération de toutes les opérations depuis la base de données");
            List<Operation> allOps = operationDao.getAll();
            System.out.println("Nombre d'opérations récupérées: " + allOps.size());
            
            System.out.println("Ajout des opérations à la liste observable");
            operations.addAll(allOps);
            
            System.out.println("Mise à jour de la TableView avec " + operations.size() + " opérations");
            operationTable.setItems(operations);
            operationTable.refresh();
            
            System.out.println("=== FIN DU CHARGEMENT DES OPÉRATIONS ===");
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des opérations : " + e.getMessage());
            e.printStackTrace();
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    

    private void loadComptes() {
        try {
            System.out.println("Début du chargement des comptes...");
            List<Compte> allComptes = compteDao.getAll();
            System.out.println("Nombre de comptes chargés depuis la BD : " + allComptes.size());
            
            comptesDepotRetrait.clear();
            comptesVirementSource.clear();
            comptesVirementDest.clear();
            
            comptesDepotRetrait.addAll(allComptes);
            comptesVirementSource.addAll(allComptes);
            comptesVirementDest.addAll(allComptes);
            
            compteSourceCombo.setItems(comptesDepotRetrait);
            compteSourceVirementCombo.setItems(comptesVirementSource);
            compteDestCombo.setItems(comptesVirementDest);
            
            System.out.println("ComboBox mises à jour avec les comptes");
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des comptes : " + e.getMessage());
            e.printStackTrace();
            Notification.NotifError("Erreur", e.getMessage());
        }
    }
    private void clearFields() {
        compteSourceCombo.setValue(null);
        compteSourceVirementCombo.setValue(null);
        compteDestCombo.setValue(null);
        montantTfd.clear();
        montantVirementTfd.clear();
    }
}
