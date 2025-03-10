package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;
import com.banking.digitalbankingproject.service.impl.CompteImpl;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OperationController {

    @FXML
    private TableColumn<Operation, String> compteCol;

    @FXML
    private ComboBox<Compte> compteSourceCombo;

    @FXML
    private TableColumn<Operation, String> dateCol;

    @FXML
    private DatePicker dateOperationPicker;

    @FXML
    private TableColumn<Operation, Integer> idCol;

    @FXML
    private TableColumn<Operation, Double> montantCol;

    @FXML
    private TextField montantField;

    @FXML
    private Button pdfBtn;

    @FXML
    private Button retourBtn;

    @FXML
    private TableView<Operation> transactionsTable;

    @FXML
    private TableColumn<Operation, String> typeCol;

    @FXML
    private ComboBox<TypeOperation> typeOperationCombo;

    @FXML
    private Button validerBtn;

    private final IOperation operationService = new OperationImpl();
    private final CompteImpl compteService = new CompteImpl();
    private ObservableList<Operation> operationsList = FXCollections.observableArrayList();
    private ObservableList<Compte> comptesList = FXCollections.observableArrayList();
    private ObservableList<TypeOperation> typesOperationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadComptes();
        loadTypesOperation();

        typeOperationCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == TypeOperation.RETRAIT) {
                compteSourceCombo.setVisible(true);
            } else {
                compteSourceCombo.setVisible(false);
            }
        });

        compteSourceCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            loadOperations();
        });

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        montantCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(cellData -> {
            Instant instant = cellData.getValue().getDateOp();
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            return javafx.beans.binding.Bindings.createStringBinding(() ->
                    localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });
        compteCol.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cellData.getValue().getCompte().getNumero()));

        loadOperations();
    }

    private void loadComptes() {
        comptesList.clear();
        comptesList.addAll(compteService.getAllComptes());
        compteSourceCombo.setItems(comptesList);
    }

    private void loadTypesOperation() {
        typesOperationList.clear();
        typesOperationList.addAll(TypeOperation.DEPOT, TypeOperation.RETRAIT); // Ajouter uniquement DEPOT et RETRAIT
        typeOperationCombo.setItems(typesOperationList);
    }

    private void loadOperations() {
        operationsList.clear();
        operationsList.addAll(operationService.consulterHistorique());
        transactionsTable.setItems(operationsList);
    }

    @FXML
    void genererPDF(ActionEvent event) {
        String filePath = "operations.pdf";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Paragraph title = new Paragraph("Historique des Opérations");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Créer un tableau pour afficher les opérations
            PdfPTable table = new PdfPTable(4); // 4 colonnes : ID, Montant, Type, Date
            table.setWidthPercentage(100);

            // En-têtes du tableau
            table.addCell("ID");
            table.addCell("Montant");
            table.addCell("Type");
            table.addCell("Date");

            for (Operation operation : operationsList) {
                table.addCell(String.valueOf(operation.getId()));
                table.addCell(String.valueOf(operation.getAmount()));
                table.addCell(operation.getType().name());
                table.addCell(operation.getDateOp().toString());
            }

            document.add(table);

            document.close();

            showAlert("Succès", "Le PDF a été généré avec succès : " + filePath);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la génération du PDF.");
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

    @FXML
    void validerOperation(ActionEvent event) {
        Compte compteSource = compteSourceCombo.getSelectionModel().getSelectedItem();
        TypeOperation typeOperation = typeOperationCombo.getSelectionModel().getSelectedItem();
        double montant;
        try {
            montant = Double.parseDouble(montantField.getText().trim());
            if (montant <= 0) {
                showAlert("Erreur", "Le montant doit être positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le montant doit être un nombre valide !");
            return;
        }
        LocalDate dateOperation = dateOperationPicker.getValue();

        if (typeOperation == TypeOperation.RETRAIT && compteSource == null) {
            showAlert("Erreur", "Veuillez sélectionner un compte source pour le retrait !");
            return;
        }
        if (typeOperation == null || dateOperation == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs correctement !");
            return;
        }

        boolean success = false;
        try {
            if (typeOperation == TypeOperation.DEPOT) {
                success = operationService.depot(compteSource.getId(), montant);
            } else if (typeOperation == TypeOperation.RETRAIT) {
                success = operationService.retrait(compteSource.getId(), montant);
            }

            if (success) {
                showAlert("Succès", "Opération effectuée avec succès !");
                clearFields();
                loadOperations(); // Recharger les opérations dans la table
            } else {
                showAlert("Erreur", "Échec de l'opération !");
            }
        } catch (Exception e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    private void clearFields() {
        compteSourceCombo.getSelectionModel().clearSelection();
        typeOperationCombo.getSelectionModel().clearSelection();
        montantField.clear();
        dateOperationPicker.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}