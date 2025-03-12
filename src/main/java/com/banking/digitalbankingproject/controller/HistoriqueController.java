package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.impl.OperationImpl;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tray.notification.NotificationType;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HistoriqueController implements Initializable {
    private final OperationImpl operationService = new OperationImpl();
    private int compteId;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy - HH:mm:ss");

    @FXML private TableView<Operation> transactionTable;
    @FXML private TableColumn<Operation, String> typeColumn;
    @FXML private TableColumn<Operation, Double> montantColumn;
    @FXML private TableColumn<Operation, String> dateColumn;
    @FXML private Button fermerButton;
    @FXML private Button genererReleveButton;

    private final Db db = new Db();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCompteId(int id) {
        this.compteId = id;
        loadHistorique();
    }

    private void loadHistorique() {
        if (compteId <= 0) {
            showNotification("Erreur", "Aucun compte sélectionné !", NotificationType.ERROR);
            transactionTable.setItems(FXCollections.observableArrayList());
            return;
        }

        try {
            ObservableList<Operation> transactionList = operationService.getHistorique(compteId);
            transactionTable.setItems(FXCollections.observableArrayList(transactionList));
        } catch (Exception e) {
            showNotification("Erreur", "Impossible de charger l'historique", NotificationType.ERROR);
        }
    }

    @FXML
    public void fermerFenetre() {
        Stage stage = (Stage) transactionTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void genererReleve() {
        if (compteId <= 0) {
            showNotification("Erreur", "Aucun compte sélectionné pour générer le relevé !", NotificationType.ERROR);
            return;
        }

        ObservableList<Operation> transactionList = transactionTable.getItems();
        if (transactionList.isEmpty()) {
            showNotification("Avertissement", "Aucune opération à inclure dans le relevé !", NotificationType.WARNING);
            return;
        }

        try {
            String numeroCompte = getNumeroCompteById(compteId);
            String fileName = "Releve_Compte_" + numeroCompte + "_" + System.currentTimeMillis() + ".pdf";

            PdfWriter writer = new PdfWriter(fileName);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("=== Relevé Bancaire ===")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(16));
            document.add(new Paragraph("Compte : " + numeroCompte));
            document.add(new Paragraph("Date : " + LocalDateTime.now().format(DATE_FORMATTER)));
            document.add(new Paragraph(" "));

            Table table = new Table(new float[]{150, 100, 200});
            table.addCell(new Cell().add(new Paragraph("Type")).setTextAlignment(TextAlignment.CENTER).setBold());
            table.addCell(new Cell().add(new Paragraph("Montant")).setTextAlignment(TextAlignment.CENTER).setBold());
            table.addCell(new Cell().add(new Paragraph("Date")).setTextAlignment(TextAlignment.CENTER).setBold());

            for (Operation op : transactionList) {
                table.addCell(new Cell().add(new Paragraph(op.getType())));
                table.addCell(new Cell().add(new Paragraph(String.format("%.2f", op.getMontant()))).setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(op.getDate()))));
            }

            document.add(table);
            document.close();

            showNotification("Succès", "Relevé généré avec succès : " + fileName, NotificationType.SUCCESS);
        } catch (IOException | SQLException e) {
            showNotification("Erreur", "Erreur lors de la génération du relevé : " + e.getMessage(), NotificationType.ERROR);
        }
    }

    private void showNotification(String title, String message, NotificationType type) {

    }

    private String getNumeroCompteById(int compteId) throws SQLException {
        String sql = "SELECT numero FROM comptes WHERE id = ?";
        db.initPrepar(sql);
        db.getPstm().setInt(1, compteId);
        try (ResultSet rs = db.executeSelect()) {
            if (rs.next()) {
                return rs.getString("numero");
            } else {
                throw new SQLException("Compte non trouvé pour l'ID : " + compteId);
            }
        }
    }
}
