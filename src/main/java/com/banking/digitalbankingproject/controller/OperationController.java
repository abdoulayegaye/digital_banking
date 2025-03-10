package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OperationController {

    private IOperation operationDao = new OperationImpl();

    @FXML
    private TextField amountTfd;

    // ComboBox pour sélectionner le compte sur lequel porter l'opération
    @FXML
    private ComboBox<Compte> compteCombo;

    // ComboBox pour sélectionner le type d'opération
    @FXML
    private ComboBox<TypeOperation> typeOperationCombo;

    @FXML
    private TableView<Operation> operationsTable;

    @FXML
    private TableColumn<Operation, Integer> idCol;

    @FXML
    private TableColumn<Operation, String> dateCol;

    @FXML
    private TableColumn<Operation, Double> amountCol;

    @FXML
    private TableColumn<Operation, String> typeCol;

    @FXML
    private TableColumn<Operation, String> compteCol; // Nouvelle colonne pour le numéro de compte

    @FXML
    void initialize() {
        // Initialisation des colonnes de la TableView
        //idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateOp"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Configuration de la colonne typeCol pour afficher les numéros de compte pour les virements
        typeCol.setCellValueFactory(cellData -> {
            Operation operation = cellData.getValue();
            if (operation.getType() == TypeOperation.VIREMENT) {
                Compte compteSource = operation.getCompte();
                Compte compteDestination = operation.getCompteDestination();
                String sourceNumero = (compteSource != null) ? compteSource.getNumero() : "N/A";
                String destinationNumero = (compteDestination != null) ? compteDestination.getNumero() : "N/A";
                return new SimpleStringProperty(
                        "VIREMENT (De " + sourceNumero + " vers " + destinationNumero + ")"
                );
            } else {
                return new SimpleStringProperty(operation.getType().name());
            }
        });

        // Configuration de la colonne compteCol pour afficher le numéro de compte
        compteCol.setCellValueFactory(cellData -> {
            Operation operation = cellData.getValue();
            Compte compte = operation.getCompte();
            String numeroCompte = (compte != null) ? compte.getNumero() : "N/A";
            return new SimpleStringProperty(numeroCompte);
        });

        // Configuration de la colonne de date pour formater l'affichage
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        dateCol.setCellValueFactory(cellData -> {
            Operation operation = cellData.getValue();
            if (operation.getDateOp() != null) {
                LocalDateTime dateTime = LocalDateTime.ofInstant(operation.getDateOp(), ZoneId.systemDefault());
                String formattedDate = formatter.format(dateTime);
                return new SimpleStringProperty(formattedDate);
            } else {
                return new SimpleStringProperty("N/A");
            }
        });

        // Configuration du ComboBox pour afficher le nom, le prénom et le numéro de compte
        compteCombo.setCellFactory(param -> new ListCell<Compte>() {
            @Override
            protected void updateItem(Compte compte, boolean empty) {
                super.updateItem(compte, empty);
                if (empty || compte == null || compte.getClient() == null) {
                    setText(null);
                } else {
                    setText(formatCompteDisplay(compte));
                }
            }
        });

        // Configuration du ComboBox pour afficher le nom, le prénom et le numéro de compte dans la sélection
        compteCombo.setButtonCell(new ListCell<Compte>() {
            @Override
            protected void updateItem(Compte compte, boolean empty) {
                super.updateItem(compte, empty);
                if (empty || compte == null || compte.getClient() == null) {
                    setText(null);
                } else {
                    setText(formatCompteDisplay(compte));
                }
            }
        });

        // Charger la liste des comptes disponibles dans la ComboBox
        compteCombo.getItems().setAll(new CompteImpl().getAllComptes());

        // Charger les types d'opération (VERSEMENT et RETRAIT uniquement) dans la ComboBox
        typeOperationCombo.getItems().setAll(TypeOperation.VERSEMENT, TypeOperation.RETRAIT);

        // Charger les opérations dans la table
        loadOperations();
    }

    @FXML
    void addOperation() {
        String amountText = amountTfd.getText().trim();
        Compte selectedCompte = compteCombo.getSelectionModel().getSelectedItem();
        TypeOperation selectedType = typeOperationCombo.getSelectionModel().getSelectedItem();

        // Vérifier si le montant est vide
        if (amountText.isEmpty()) {
            Notification.NotifError("Erreur", "Le montant est obligatoire");
            return;
        }

        // Vérifier si un compte est sélectionné
        if (selectedCompte == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte");
            return;
        }

        // Vérifier si un type d'opération est sélectionné
        if (selectedType == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un type d'opération");
            return;
        }

        // Vérifier si le compte est fermé
        if ("FERME".equals(selectedCompte.getStatut())) {
            Notification.NotifError("Erreur", "Les opérations sont refusées pour les comptes fermés.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountText);
            Operation operation = new Operation();
            operation.setAmount(amount);
            operation.setType(selectedType);
            operation.setDateOp(java.time.Instant.now());
            operation.setCompte(selectedCompte);

            // Gestion spécifique pour les dépôts (VERSEMENT)
            if (selectedType == TypeOperation.VERSEMENT) {
                if (new CompteImpl().deposer(selectedCompte.getId(), amount)) {
                    Notification.NotifSuccess("Succès", "Dépôt effectué avec succès");
                } else {
                    Notification.NotifError("Erreur", "Échec du dépôt");
                    return;
                }
            }

            // Ajouter l'opération à la base de données
            if (operationDao.createOperation(operation)) {
                Notification.NotifSuccess("Succès", "Opération ajoutée avec succès");
                loadOperations();
                clearFields();
            } else {
                Notification.NotifError("Erreur", "Échec de l'ajout de l'opération");
            }
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Le montant doit être un nombre valide");
        }
    }

    private void loadOperations() {
        operationsTable.getItems().setAll(operationDao.getAllOperations());
    }

    private void clearFields() {
        amountTfd.clear();
        compteCombo.getSelectionModel().clearSelection();
        typeOperationCombo.getSelectionModel().clearSelection();
    }
    public void retour(ActionEvent event) {
        // Charge la page d'accueil par exemple
        Outils.load(event, "Accueil", "/fxml/accueil.fxml");
    }

    @FXML
    void ouvrirTransfer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transfer.fxml"));
            Parent root = loader.load();
            // Si besoin, vous pouvez récupérer le contrôleur TransferController et passer des paramètres
            // TransferController transferController = loader.getController();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Effectuer un virement");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Notification.NotifError("Erreur", "Impossible d'ouvrir le formulaire de transfert : " + e.getMessage());
        }
    }

    private String formatCompteDisplay(Compte compte) {
        if (compte != null && compte.getClient() != null) {
            return compte.getClient().getNom() + " " + compte.getClient().getPrenom() + " (" + compte.getNumero() + ")";
        } else {
            return "Compte invalide";
        }
    }

    @FXML
    private DatePicker dateSearchPicker; // Champ de sélection de date

    @FXML
    void searchOperationsByDate() {
        LocalDate selectedDate = dateSearchPicker.getValue();

        if (selectedDate == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner une date");
            return;
        }

        // Convertir LocalDate en Instant (pour correspondre au format de dateOp dans Operation)
        Instant startOfDay = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = selectedDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Charger les opérations pour la date sélectionnée
        List<Operation> operations = operationDao.getOperationsByDate(startOfDay, endOfDay);
        operationsTable.getItems().setAll(operations);
    }
}
