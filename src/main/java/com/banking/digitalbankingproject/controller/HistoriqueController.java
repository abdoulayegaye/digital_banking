package com.banking.digitalbankingproject.controller;

import com.banking.digitalbankingproject.database.Db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import com.banking.digitalbankingproject.entity.Transaction;

public class HistoriqueController {

    @FXML private TableView<Transaction> tableTransactions;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, Double> colMontant;
    @FXML private TableColumn<Transaction, String> colType;

    private int compteId; // ID du compte sélectionné

    public void setCompteId(int compteId) {
        this.compteId = compteId;
        chargerHistorique();
    }

    private void chargerHistorique() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String query = "SELECT date_transaction, montant, type FROM transactions WHERE compte_id = ?";


        try (Connection conn = Db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, compteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getString("date_transaction"),
                        rs.getDouble("montant"),
                        rs.getString("type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableTransactions.setItems(transactions);
    }
}
