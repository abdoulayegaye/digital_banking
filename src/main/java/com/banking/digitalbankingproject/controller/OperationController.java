package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.banking.digitalbankingproject.tools.Outils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.Notifications;

import java.time.format.DateTimeFormatter;

public class OperationController {

    @FXML
    private ComboBox<TypeOperation> typeOperationCombo;

    @FXML
    private TextField numeroCompteField;

    @FXML
    private TextField montantField;

    @FXML
    private TableView<Operation> operationsTable;

    @FXML
    private TableColumn<Operation, String> dateColumn;

    @FXML
    private TableColumn<Operation, String> typeColumn;

    @FXML
    private TableColumn<Operation, Double> montantColumn;

    @FXML
    private TableColumn<Operation, String> compteColumn;

    private final IOperation operationService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public OperationController() {
        this.operationService = new OperationImpl();
    }

    @FXML
    void handleEffectuerAction(ActionEvent event) {
        try {
            // Validation des champs
            if (typeOperationCombo.getValue() == null || 
                numeroCompteField.getText().trim().isEmpty() || 
                montantField.getText().trim().isEmpty()) {
                
                Notifications.create()
                    .title("Erreur")
                    .text("Tous les champs sont obligatoires")
                    .showError();
                return;
            }

            // Récupération et validation du montant
            double montant;
            try {
                montant = Double.parseDouble(montantField.getText().trim());
                if (montant <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                Notifications.create()
                    .title("Erreur")
                    .text("Le montant doit être un nombre positif")
                    .showError();
                return;
            }

            // Exécution de l'opération
            String numeroCompte = numeroCompteField.getText().trim();
            TypeOperation type = typeOperationCombo.getValue();
            
            if (operationService.effectuerOperation(numeroCompte, montant, type)) {
                Notifications.create()
                    .title("Succès")
                    .text("Opération effectuée avec succès")
                    .showInformation();
                
                // Réinitialisation des champs
                typeOperationCombo.setValue(null);
                numeroCompteField.clear();
                montantField.clear();
                
                // Rafraîchir le tableau des opérations
                refreshOperationsTable();
            } else {
                Notifications.create()
                    .title("Erreur")
                    .text("Impossible d'effectuer l'opération. Vérifiez le numéro de compte et le solde disponible.")
                    .showError();
            }
        } catch (Exception e) {
            Notifications.create()
                .title("Erreur")
                .text("Une erreur est survenue: " + e.getMessage())
                .showError();
        }
    }

    @FXML
    void handleRetourAction(ActionEvent event) {
        try {
            Outils.load(event, "Digital Banking", "/fxml/accueil.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        // Initialiser le ComboBox avec les types d'opérations
        typeOperationCombo.getItems().addAll(TypeOperation.values());
        
        // Configuration des colonnes du tableau
        dateColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getDateOp().atZone(java.time.ZoneId.systemDefault()).format(DATE_FORMATTER)
            )
        );
        typeColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getType().toString()
            )
        );
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        compteColumn.setCellValueFactory(cellData -> 
            javafx.beans.binding.Bindings.createStringBinding(
                () -> cellData.getValue().getCompte().getNumero()
            )
        );
        
        // Charger les opérations
        refreshOperationsTable();
    }

    private void refreshOperationsTable() {
        operationsTable.setItems(FXCollections.observableArrayList(operationService.getAllOperations()));
    }
}
