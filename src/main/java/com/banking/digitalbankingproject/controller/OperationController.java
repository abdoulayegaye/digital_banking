package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Notification;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class OperationController implements Initializable {

    @FXML private TableView<Operation> operationsTable;
    @FXML private TableColumn<Operation, Integer> idCol;
    @FXML private TableColumn<Operation, String> dateCol;
    @FXML private TableColumn<Operation, Double> amountCol;
    @FXML private TableColumn<Operation, String> typeCol;
    @FXML private TableColumn<Operation, String> accountCol;

    @FXML private ComboBox<String> accountCombo;
    @FXML private ComboBox<String> destAccountCombo;
    @FXML private TextField amountField;
    @FXML private TextField searchField;
    @FXML private VBox transferForm;
    @FXML private Button transferBtn;
    @FXML private Button depotBtn;
    @FXML private Button retraitBtn;
    @FXML private Button exportBtn;

    private OperationImpl operationService = new OperationImpl();
    private CompteImpl compteService = new CompteImpl();
    private Db db = new Db();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTableColumns();
        toggleTransferForm(false);
        refreshAccountNumbers();
        loadAllOperations();
    }

    private void loadAllOperations() {
        operationsTable.setItems(getAllOperations());
    }

    private void configureTableColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(cellData -> {
            Instant instant = cellData.getValue().getDateOp();
            return new SimpleStringProperty(instant != null ? formatDate(instant) : "Inconnue");
        });
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        accountCol.setCellValueFactory(cellData -> {
            Compte compte = cellData.getValue().getCompte();
            return new SimpleStringProperty(compte != null ? compte.getNumero() : "Inconnu");
        });
    }

    public ObservableList<Operation> getAllOperations() {
        ObservableList<Operation> operations = FXCollections.observableArrayList();
        String sql = "SELECT * FROM operations ORDER BY date_op DESC";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect(sql);
            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                Timestamp timestamp = rs.getTimestamp("date_op");
                operation.setDateOp(timestamp != null ? timestamp.toInstant() : null);
                operation.setAmount(rs.getDouble("amount"));
                operation.setType(TypeOperation.valueOf(rs.getString("type")));
                int compteId = rs.getInt("compte_id");
                operation.setCompte(compteService.getCompteById(compteId));
                operations.add(operation);
            }
        } catch (SQLException e) {
            Notification.NotifError("Erreur", "Échec du chargement des opérations : " + e.getMessage());
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
        return operations;
    }

    @FXML
    public void refreshAccountNumbers() {
        ObservableList<String> accounts = FXCollections.observableArrayList(compteService.getAllAccountNumbers());
        accountCombo.setItems(accounts);
        destAccountCombo.setItems(accounts);
    }

    @FXML
    private void handleDeposit() {
        if (!validateInput()) return;
        try {
            String account = accountCombo.getValue();
            double amount = Double.parseDouble(amountField.getText());
            int result = operationService.deposit(account, amount);
            if (result > 0) {
                Notification.NotifSuccess("Dépôt", "Dépôt effectué avec succès");
                refreshOperations(account);
                clearFields();
            }
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleWithdraw() {
        if (!validateInput()) return;
        try {
            String account = accountCombo.getValue();
            double amount = Double.parseDouble(amountField.getText());
            int result = operationService.withdraw(account, amount);
            if (result > 0) {
                Notification.NotifSuccess("Retrait", "Retrait effectué avec succès");
                refreshOperations(account);
                clearFields();
            }
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }

    @FXML
    private void handleTransfer() {
        if (!validateTransferInput()) return;
        try {
            String source = accountCombo.getValue();
            String dest = destAccountCombo.getValue();
            double amount = Double.parseDouble(amountField.getText());
            int result = operationService.transfer(source, dest, amount);
            if (result > 0) {
                Notification.NotifSuccess("Virement", "Virement effectué avec succès");
                refreshOperations(source);
                toggleTransferForm(false);
                clearFields();
                depotBtn.setDisable(false);
                retraitBtn.setDisable(false);
            }
        } catch (Exception e) {
            Notification.NotifError("Erreur", e.getMessage());
        }
    }

    @FXML
    private void showTransferForm() {
        toggleTransferForm(true);
        depotBtn.setDisable(true);
        retraitBtn.setDisable(true);
    }

    @FXML
    private void cancelTransfer() {
        toggleTransferForm(false);
        clearFields();
        depotBtn.setDisable(false);
        retraitBtn.setDisable(false);
    }

    @FXML
    private void filterOperations(KeyEvent event) {
        String searchText = searchField.getText().trim().toLowerCase();
        ObservableList<Operation> filteredList = FXCollections.observableArrayList();
        for (Operation op : getAllOperations()) {
            if (String.valueOf(op.getId()).contains(searchText) ||
                    (op.getDateOp() != null && formatDate(op.getDateOp()).contains(searchText)) ||
                    String.valueOf(op.getAmount()).contains(searchText) ||
                    op.getType().name().toLowerCase().contains(searchText) ||
                    (op.getCompte() != null && op.getCompte().getNumero().toLowerCase().contains(searchText))) {
                filteredList.add(op);
            }
        }
        operationsTable.setItems(filteredList);
    }

    private void refreshOperations(String accountNumber) {
        refreshAccountNumbers();
        operationsTable.setItems(FXCollections.observableArrayList(operationService.getOperationsByAccount(accountNumber)));
    }

    private boolean validateInput() {
        if (accountCombo.getValue() == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte source.");
            return false;
        }
        if (amountField.getText().isEmpty()) {
            Notification.NotifError("Erreur", "Veuillez entrer un montant.");
            return false;
        }
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                Notification.NotifError("Erreur", "Le montant doit être positif.");
                return false;
            }
        } catch (NumberFormatException e) {
            Notification.NotifError("Erreur", "Montant invalide.");
            return false;
        }
        return true;
    }

    private boolean validateTransferInput() {
        if (!validateInput()) return false;
        if (destAccountCombo.getValue() == null) {
            Notification.NotifError("Erreur", "Veuillez sélectionner un compte destinataire.");
            return false;
        }
        if (accountCombo.getValue().equals(destAccountCombo.getValue())) {
            Notification.NotifError("Erreur", "Le compte source et destinataire ne peuvent pas être identiques.");
            return false;
        }
        return true;
    }

    private void toggleTransferForm(boolean show) {
        transferForm.setVisible(show);
        transferBtn.setDisable(!show);


    }

    @FXML
    private void clearFields() {
        amountField.clear();
        accountCombo.getSelectionModel().clearSelection();
        destAccountCombo.getSelectionModel().clearSelection();
    }

    private String formatDate(Instant instant) {
        if (instant == null) return "Inconnue";
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault()).format(instant);
    }

    @FXML
    public void Retour(ActionEvent event) throws IOException {
        Outils.load(event, "Déconnexion", "/fxml/accueil.fxml");
    }
}