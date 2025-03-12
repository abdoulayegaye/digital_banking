package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class OperationController implements Initializable {
    private final IOperation operationService = new OperationImpl();
    private final Db db = new Db();
    public Button retourClientsButton;
    private int compteId = 0;

    @FXML private TableView<Operation> transactionTable;
    @FXML private TableColumn<Operation, String> typeColumn;
    @FXML private TableColumn<Operation, Double> montantColumn;
    @FXML private TableColumn<Operation, String> dateColumn;
    @FXML private TableColumn<Operation, String> numeroCompteColumn;
    @FXML private Label totalOperationsLabel;

    @FXML private TextField montantDepotRetraitField;
    @FXML private ComboBox<String> compteSourceDepotRetraitComboBox;
    @FXML private ComboBox<String> compteSourceVirementComboBox;
    @FXML private ComboBox<String> compteDestVirementComboBox;
    @FXML private TextField montantVirementField;
    @FXML private Button fermerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        numeroCompteColumn.setCellValueFactory(new PropertyValueFactory<>("numeroCompte"));

        Platform.runLater(() -> {
            loadComptes();
            loadHistorique();
        });

        fermerButton.setOnMouseEntered(event -> fermerButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;"));
        fermerButton.setOnMouseExited(event -> fermerButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;"));
    }

    private void loadComptes() {
        ObservableList<String> comptes = FXCollections.observableArrayList();
        String sql = "SELECT numero FROM comptes";
        try {
            db.initPrepar(sql);
            try (ResultSet rs = db.executeSelect()) {
                while (rs.next()) {
                    comptes.add(rs.getString("numero"));
                }
            }
            compteSourceDepotRetraitComboBox.setItems(comptes);
            compteSourceVirementComboBox.setItems(comptes);
            compteDestVirementComboBox.setItems(comptes);

        } catch (SQLException e) {
            showNotification("Erreur", "Impossible de charger les comptes : " + e.getMessage(), NotificationType.ERROR);
        }
    }

    private void loadHistorique() {
        ObservableList<Operation> transactionList;
        if (compteId > 0) {
            transactionList = operationService.getHistorique(compteId);
        } else {
            transactionList = operationService.getToutesOperations();
        }
        transactionTable.setItems(transactionList);
        totalOperationsLabel.setText(String.valueOf(transactionList.size()));
        transactionTable.refresh();
    }

    @FXML
    public void fermerFenetre() {
        Stage stage = (Stage) transactionTable.getScene().getWindow();
        stage.close();
    }


    private void showNotification(String title, String message, NotificationType type) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(type);
        tray.showAndDismiss(javafx.util.Duration.seconds(3));
    }

    @FXML
    public void effectuerDepot() {
        effectuerDepotRetrait("DÉPÔT");
    }

    @FXML
    public void effectuerRetrait() {
        effectuerDepotRetrait("RETRAIT");
    }

    private void effectuerDepotRetrait(String type) {
        try {
            double montant = Double.parseDouble(montantDepotRetraitField.getText());
            String compteNumero = compteSourceDepotRetraitComboBox.getValue();

            if (montant <= 0 || compteNumero == null) {
                showNotification("Erreur", "Montant invalide ou compte non sélectionné !", NotificationType.ERROR);
                return;
            }

            int compteId = getCompteIdByNumero(compteNumero);
            if (compteId == -1) {
                showNotification("Erreur", "Compte introuvable !", NotificationType.ERROR);
                return;
            }

            if (type.equals("RETRAIT") && !verifierSoldeSuffisant(compteId, montant)) {
                showNotification("Erreur", "Fonds insuffisants !", NotificationType.ERROR);
                return;
            }

            String sql = type.equals("DÉPÔT") ?
                    "UPDATE comptes SET balance = balance + ? WHERE id = ?" :
                    "UPDATE comptes SET balance = balance - ? WHERE id = ?";
            db.initPrepar(sql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setInt(2, compteId);
            int rowsUpdated = db.executeMaj();

            if (rowsUpdated > 0) {
                enregistrerOperation(type, montant, compteId);
                showNotification("Succès", type + " effectué !", NotificationType.SUCCESS);
                montantDepotRetraitField.clear();
                loadHistorique();
            }
        } catch (NumberFormatException e) {
            showNotification("Erreur", "Veuillez entrer un montant valide !", NotificationType.ERROR);
        } catch (SQLException e) {
            showNotification("Erreur SQL", "Erreur lors du " + type + " : " + e.getMessage(), NotificationType.ERROR);
        }
    }

    @FXML
    public void effectuerVirement() {
        try {
            double montant = Double.parseDouble(montantVirementField.getText());
            String sourceNumero = compteSourceVirementComboBox.getValue();
            String destNumero = compteDestVirementComboBox.getValue();

            if (montant <= 0 || sourceNumero == null || destNumero == null || sourceNumero.equals(destNumero)) {
                showNotification("Erreur", "Montant invalide ou comptes incorrects !", NotificationType.ERROR);
                return;
            }

            int compteSourceId = getCompteIdByNumero(sourceNumero);
            int compteDestId = getCompteIdByNumero(destNumero);
            if (compteSourceId == -1 || compteDestId == -1) {
                showNotification("Erreur", "Compte(s) introuvable(s) !", NotificationType.ERROR);
                return;
            }

            if (!verifierSoldeSuffisant(compteSourceId, montant)) {
                showNotification("Erreur", "Fonds insuffisants !", NotificationType.ERROR);
                return;
            }

            db.initPrepar("UPDATE comptes SET balance = balance - ? WHERE id = ?");
            db.getPstm().setDouble(1, montant);
            db.getPstm().setInt(2, compteSourceId);
            db.executeMaj();

            db.initPrepar("UPDATE comptes SET balance = balance + ? WHERE id = ?");
            db.getPstm().setDouble(1, montant);
            db.getPstm().setInt(2, compteDestId);
            db.executeMaj();

            enregistrerOperation("VIREMENT", montant, compteSourceId);
            enregistrerOperation("VIREMENT_REÇU", montant, compteDestId);
            showNotification("Succès", "Virement effectué !", NotificationType.SUCCESS);
            montantVirementField.clear();
            loadHistorique();
        } catch (NumberFormatException e) {
            showNotification("Erreur", "Veuillez entrer un montant valide !", NotificationType.ERROR);
        } catch (SQLException e) {
            showNotification("Erreur SQL", "Erreur lors du virement : " + e.getMessage(), NotificationType.ERROR);
        }
    }

    private boolean verifierSoldeSuffisant(int compteId, double montant) throws SQLException {
        String sql = "SELECT balance FROM comptes WHERE id = ?";
        db.initPrepar(sql);
        db.getPstm().setInt(1, compteId);
        try (ResultSet rs = db.executeSelect()) {
            return rs.next() && rs.getDouble("balance") >= montant;
        }
    }

    private int getCompteIdByNumero(String numero) throws SQLException {
        db.initPrepar("SELECT id FROM comptes WHERE numero = ?");
        db.getPstm().setString(1, numero);
        try (ResultSet rs = db.executeSelect()) {
            return rs.next() ? rs.getInt("id") : -1;
        }
    }

    private void enregistrerOperation(String type, double montant, int compteId) throws SQLException {
        String normalizedType = type.equals("VIREMENT_REÇU") ? "VIREMENT" : type;
        db.initPrepar("INSERT INTO operations (compte_id, type, amount, date_op) VALUES (?, ?, ?, NOW())");
        db.getPstm().setInt(1, compteId);
        db.getPstm().setString(2, normalizedType);
        db.getPstm().setDouble(3, montant);
        db.executeMaj();
    }

    public void ouvrirHistorique() {
        try {
            String compteNumero = compteSourceDepotRetraitComboBox.getValue(); // Exemple
            if (compteNumero == null) {
                showNotification( "Attention", "Sélectionnez un compte d'abord !",NotificationType.ERROR);
                return;
            }
            int compteId = getCompteIdByNumero(compteNumero);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/banking/digitalbankingproject/fxml/Historique.fxml"));
            Parent root = loader.load();
            HistoriqueController controller = loader.getController();
            controller.setCompteId(compteId); // Passe le compteId

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Historique des Transactions");
            stage.show();
        } catch (IOException | SQLException e) {
            showNotification( "Erreur", "Impossible d'ouvrir l'historique : " + e.getMessage(),NotificationType.ERROR);
        }
    }
    public void retourAccueil(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/accueil.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) retourClientsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showNotification(  "Erreur", "Impossible d'ouvrir la liste des clients !",NotificationType.ERROR);
        }
    }
}