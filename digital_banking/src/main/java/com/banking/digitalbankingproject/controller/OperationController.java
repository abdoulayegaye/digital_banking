package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.ICompte;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.text.DecimalFormat;

public class OperationController {
    @FXML private ComboBox<Compte> comboCompte;
    @FXML private TextField txtMontant;
    @FXML private Label lblSolde;
    @FXML private TableView<Operation> tableOperations;
    @FXML private TableColumn<Operation, String> colDate;
    @FXML private TableColumn<Operation, String> colType;
    @FXML private TableColumn<Operation, Double> colMontant;
    @FXML private Button btnDepot;
    @FXML private Button btnRetrait;
    @FXML private Button btnReleve;

    private IOperation operationService;
    private ICompte compteService;
    private ObservableList<Operation> operationsList;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.00 €");

    @FXML
    private void initialize() {
        try {
            operationService = new OperationImpl();
            compteService = new CompteImpl();
            setupComboBox();
            setupTableColumns();
            setupButtons();
            setupMoneyField();
        } catch (SQLException e) {
            AlertUtil.showError("Erreur de connexion", "Impossible de se connecter à la base de données");
        }
    }

    private void setupComboBox() {
        try {
            ObservableList<Compte> comptes = FXCollections.observableArrayList(compteService.getAllComptes());
            comboCompte.setItems(comptes);
            comboCompte.setConverter(new StringConverter<>() {
                @Override
                public String toString(Compte compte) {
                    if (compte != null) {
                        return String.format("%s - %s %s", 
                            compte.getNumero(), 
                            compte.getClient().getNom(),
                            compte.getClient().getPrenom());
                    }
                    return "";
                }

                @Override
                public Compte fromString(String string) {
                    return null;
                }
            });

            comboCompte.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    updateSoldeLabel(newVal);
                    loadOperations(newVal);
                    enableControls(true);
                } else {
                    lblSolde.setText("0,00 €");
                    tableOperations.getItems().clear();
                    enableControls(false);
                }
            });

            enableControls(false);
        } catch (Exception e) {
            AlertUtil.showError("Erreur", "Impossible de charger la liste des comptes");
        }
    }

    private void setupTableColumns() {
        colDate.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getDateOp()
                    .atZone(ZoneId.systemDefault())
                    .format(dateFormatter)
            )
        );

        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // Format amount with currency
        colMontant.setCellFactory(column -> new TableCell<Operation, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(moneyFormat.format(amount));
                }
            }
        });
    }

    private void setupButtons() {
        btnDepot.setOnAction(e -> handleOperation(TypeOperation.DEPOT));
        btnRetrait.setOnAction(e -> handleOperation(TypeOperation.RETRAIT));
        btnReleve.setOnAction(e -> handleGenererReleve());
    }

    private void setupMoneyField() {
        // Only allow numbers and decimal point
        txtMontant.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                txtMontant.setText(oldVal);
            }
        });
    }

    private void handleOperation(TypeOperation type) {
        if (validateInputs()) {
            try {
                Compte selectedCompte = comboCompte.getValue();
                double montant = Double.parseDouble(txtMontant.getText());

                if (type == TypeOperation.DEPOT) {
                    operationService.deposit(selectedCompte.getNumero(), montant);
                } else {
                    operationService.withdraw(selectedCompte.getNumero(), montant);
                }

                updateSoldeLabel(selectedCompte);
                loadOperations(selectedCompte);
                clearInputs();

                String message = type == TypeOperation.DEPOT ? 
                    "Dépôt effectué avec succès" :
                    "Retrait effectué avec succès";
                AlertUtil.showSuccess("Opération réussie", message);
            } catch (IllegalStateException e) {
                AlertUtil.showError("Solde insuffisant", "Le solde actuel ne permet pas d'effectuer ce retrait");
            } catch (Exception e) {
                String errorMessage = type == TypeOperation.DEPOT ?
                    "Impossible d'effectuer le dépôt" :
                    "Impossible d'effectuer le retrait";
                AlertUtil.showError("Erreur", errorMessage);
            }
        }
    }

    private void handleGenererReleve() {
        Compte selectedCompte = comboCompte.getValue();
        if (selectedCompte != null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choisir le dossier de sauvegarde");
            File directory = directoryChooser.showDialog(new Stage());
            
            if (directory != null) {
                try {
                    String filePath = operationService.generatePdfStatement(
                        selectedCompte.getNumero(), 
                        directory.getAbsolutePath()
                    );
                    AlertUtil.showSuccess("Relevé généré", 
                        "Le relevé a été généré avec succès dans:\n" + filePath);
                } catch (Exception e) {
                    AlertUtil.showError("Erreur", 
                        "Impossible de générer le relevé");
                }
            }
        }
    }

    private void updateSoldeLabel(Compte compte) {
        try {
            double solde = compteService.getBalance(compte.getNumero());
            lblSolde.setText(moneyFormat.format(solde));
        } catch (Exception e) {
            lblSolde.setText("Erreur");
        }
    }

    private void loadOperations(Compte compte) {
        try {
            operationsList = FXCollections.observableArrayList(
                operationService.getAccountHistory(compte.getNumero())
            );
            tableOperations.setItems(operationsList);
        } catch (Exception e) {
            AlertUtil.showError("Erreur", 
                "Impossible de charger l'historique des opérations");
        }
    }

    private boolean validateInputs() {
        if (comboCompte.getValue() == null) {
            AlertUtil.showError("Compte requis", "Veuillez sélectionner un compte");
            return false;
        }

        try {
            double montant = Double.parseDouble(txtMontant.getText());
            if (montant <= 0) {
                AlertUtil.showError("Montant invalide", "Le montant doit être supérieur à 0");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertUtil.showError("Format invalide", "Le montant doit être un nombre valide");
            return false;
        }

        return true;
    }

    private void enableControls(boolean enable) {
        txtMontant.setDisable(!enable);
        btnDepot.setDisable(!enable);
        btnRetrait.setDisable(!enable);
        btnReleve.setDisable(!enable);
    }

    private void clearInputs() {
        txtMontant.clear();
        txtMontant.requestFocus();
    }
}
