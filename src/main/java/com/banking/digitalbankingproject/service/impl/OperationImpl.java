package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;

public class OperationImpl implements IOperation {
    private final Db db = new Db();

    @Override
    public void effectuerVirement(int sourceId, int destId, double montant) {

    }

    @Override
    public boolean ajouterOperation(double amount, TypeOperation type, Compte compte) {
        String sql = "INSERT INTO operations (amount, type, compte_id, date_op) VALUES (?, ?, ?, NOW())";
        try {
            db.initPrepar(sql);
            db.getPstm().setDouble(1, amount);
            db.getPstm().setString(2, type.name());
            db.getPstm().setInt(3, compte.getId());
            db.executeMaj();

            // Mise à jour du solde du compte
            double newBalance = type == TypeOperation.DEPOT ?
                    compte.getBalance() + amount :
                    compte.getBalance() - amount;
            updateBalance(compte.getId(), newBalance);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean effectuerVirement(double amount, Compte compteSource, Compte compteDest) {
        if (compteSource.getBalance() < amount) {
            return false;
        }
        try {
            db.startTransaction();
            ajouterOperation(amount, TypeOperation.RETRAIT, compteSource);
            ajouterOperation(amount, TypeOperation.DEPOT, compteDest);
            db.commitTransaction();
            return true;
        } catch (Exception e) {
            db.rollbackTransaction();
            e.printStackTrace();
            return false;
        }
    }

    private void updateBalance(int compteId, double newBalance) throws SQLException {
        String sql = "UPDATE comptes SET balance = ? WHERE id = ?";
        db.initPrepar(sql);
        db.getPstm().setDouble(1, newBalance);
        db.getPstm().setInt(2, compteId);
        db.executeMaj();
    }



    public ObservableList<Operation> getHistorique(int compteId) {
        ObservableList<Operation> operations = FXCollections.observableArrayList();
        String sql = "SELECT o.*, c.numero FROM operations o JOIN comptes c ON o.compte_id = c.id WHERE o.compte_id = ? ORDER BY o.date_op DESC";

        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            try (ResultSet rs = db.executeSelect()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String type = rs.getString("type");
                    double amount = rs.getDouble("amount");
                    Timestamp dateOp = rs.getTimestamp("date_op");
                    String numero = rs.getString("numero");

                    Operation op = new Operation(id, compteId, type, amount, dateOp, numero);
                    operations.add(op);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }

    public ObservableList<Operation> getToutesOperations() {
        ObservableList<Operation> operations = FXCollections.observableArrayList();
        String sql = "SELECT o.*, c.numero FROM operations o JOIN comptes c ON o.compte_id = c.id ORDER BY o.date_op DESC";

        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            while (rs.next()) {
                Timestamp dateOp = rs.getTimestamp("date_op");

                operations.add(new Operation(
                        rs.getInt("id"),
                        rs.getInt("compte_id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        dateOp, // Utilisation de Timestamp directement
                        rs.getString("numero")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }

}

