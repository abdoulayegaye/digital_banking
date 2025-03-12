package com.banking.digitalbankingproject.controller;
import com.banking.digitalbankingproject.dao.CompteDAO;
import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;

import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public class OperationController {
    @FXML
    private TextField numeroCompteField;

    @FXML
    private Button  btnRetourAccueil;

    @FXML
    private TextField montantField;

    @FXML
    private ComboBox<String> operationTypeComboBox;

    //@FXML private TableView<?> historiqueTableView;

    //@FXML private TextField numeroCompteField;
    // @FXML private TextField montantField;
    // @FXML private ComboBox<String> operationTypeComboBox;
    @FXML private Button btnEffectuerOperation;
    @FXML private TableView<Operation> historiqueTableView;
    @FXML private TableColumn<Operation, Instant> colDate;
    @FXML private TableColumn<Operation, Double> colMontant;
    @FXML private TableColumn<Operation, String> colType;

    private ICompte compteDAO;
    private IOperation operationDAO;
    private Compte compte;

    public OperationController() throws SQLException {
        this.compteDAO = new CompteImpl(Db.getConnection());
        this.operationDAO = new OperationImpl(Db.getConnection());
    }

    // Initialisation de la vue : Récupérer un compte et afficher l'historique des opérations
    @FXML
    public void initialize() {
        operationTypeComboBox.getItems().addAll("Dépôt", "Retrait");
        operationTypeComboBox.getSelectionModel().selectFirst(); // Sélectionner "Dépôt" par défaut

        // Initialisation de la table d'historique des opérations
        colDate.setCellValueFactory(cellData -> cellData.getValue().getDateOpProperty());
        colMontant.setCellValueFactory(cellData -> cellData.getValue().getAmountProperty().asObject());
        colType.setCellValueFactory(cellData -> cellData.getValue().getTypeProperty());

        compte = compteDAO.getCompteByNumero("SN-1741566609742");
        // Récupérer la liste des opérations pour un compte et la convertir en ObservableList
        List<Operation> operations = operationDAO.getOperationsByCompte(compte);
        ObservableList<Operation> observableOperations = FXCollections.observableArrayList(operations);

// Afficher les opérations dans la TableView
        historiqueTableView.setItems(observableOperations);

        // Event listener pour les opérations
        btnEffectuerOperation.setOnAction(event -> effectuerOperation());
    }

    // Méthode pour rechercher un compte à partir du numéro
    @FXML
    public void rechercherCompte() {
        String numeroCompte = numeroCompteField.getText();
        if (numeroCompte.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un numéro de compte.");
            return;
        }

        compte = compteDAO.getCompteByNumero(numeroCompte);

        if (compte == null) {
            showAlert(Alert.AlertType.ERROR, "Compte non trouvé", "Aucun compte trouvé pour ce numéro.");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Compte trouvé", "Compte trouvé pour " + compte.getClient().getPrenom() + " " + compte.getClient().getNom());
            // Charger l'historique des opérations
            List<Operation> historique = operationDAO.consulterHistorique(compte);
            historiqueTableView.getItems().setAll(historique);
        }
    }

    // Méthode pour effectuer une opération (Dépôt ou Retrait)
    @FXML
    private void effectuerOperation() {
        if (compte == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez d'abord rechercher un compte.");
            return;
        }

        String montantText = montantField.getText();
        if (montantText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un montant.");
            return;
        }

        double montant = Double.parseDouble(montantText);
        String operationType = operationTypeComboBox.getValue();

        // Vérifier le type d'opération
        if ("Dépôt".equals(operationType)) {
            // Effectuer un dépôt
            operationDAO.effectuerDepot(compte, montant);
        } else if ("Retrait".equals(operationType)) {
            // Effectuer un retrait
            if (compte.getBalance() < montant) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Solde insuffisant pour ce retrait.");
                return;
            }
            operationDAO.effectuerRetrait(compte, montant);
            compte.setBalance(compte.getBalance() + montant);
            compteDAO.updateCompte(compte);
        }

        // Mettre à jour l'affichage du solde et de l'historique
        List<Operation> historique = operationDAO.consulterHistorique(compte);
        historiqueTableView.getItems().setAll(historique);
        showAlert(Alert.AlertType.INFORMATION, "Opération effectuée", "Opération réussie. Solde actuel: " + compte.getBalance());
    }


    // Méthode pour afficher des alertes
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
}
