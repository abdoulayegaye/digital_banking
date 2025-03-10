package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.enums.TypeOperation;
import com.banking.digitalbankingproject.service.IOperation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private final Db db = new Db();

    @Override
    public boolean depot(int compteId, double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        String sql = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (?, ?, ?, ?)";
        String updateSoldeSql = "UPDATE comptes SET balance = balance + ? WHERE id = ?";

        try {
            // Insert operation record
            db.initPrepar(sql);
            db.getPstm().setTimestamp(1, Timestamp.from(Instant.now()));
            db.getPstm().setDouble(2, montant);
            db.getPstm().setString(3, TypeOperation.DEPOT.name());
            db.getPstm().setInt(4, compteId);
            db.executeMaj();

            // Update account balance
            db.initPrepar(updateSoldeSql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setInt(2, compteId);
            db.executeMaj();

            return true;
        } catch (SQLException e) {
            handleException(e);
            return false;
        } finally {
            db.closeConnection(); // Fermer la connexion ici
        }
    }

    @Override
    public boolean retrait(int compteId, double montant) {
        if (montant <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }

        String sql = "INSERT INTO operations (date_op, amount, type, compte_id) VALUES (?, ?, ?, ?)";
        String updateSoldeSql = "UPDATE comptes SET balance = balance - ? WHERE id = ?";

        try {
            // Check current balance
            double solde = getSoldeCompte(compteId);
            if (solde < montant) {
                throw new IllegalArgumentException("Solde insuffisant");
            }

            // Insert operation record
            db.initPrepar(sql);
            db.getPstm().setTimestamp(1, Timestamp.from(Instant.now()));
            db.getPstm().setDouble(2, montant);
            db.getPstm().setString(3, TypeOperation.RETRAIT.name());
            db.getPstm().setInt(4, compteId);
            db.executeMaj();

            // Update account balance
            db.initPrepar(updateSoldeSql);
            db.getPstm().setDouble(1, montant);
            db.getPstm().setInt(2, compteId);
            db.executeMaj();

            return true;
        } catch (SQLException e) {
            handleException(e);
            return false;
        } finally {
            db.closeConnection(); // Fermer la connexion ici
        }
    }

    @Override
    public List<Operation> consulterHistorique() {
        String sql = "SELECT o.*, c.id as compte_id, c.numero, c.balance, c.created_at " +
                "FROM operations o " +
                "JOIN comptes c ON o.compte_id = c.id " +
                "ORDER BY o.date_op DESC";
        return getHistorique(sql);
    }

    @Override
    public List<Operation> consulterHistorique(int compteId) {
        String sql = "SELECT o.*, c.id as compte_id, c.numero, c.balance, c.created_at " +
                "FROM operations o " +
                "JOIN comptes c ON o.compte_id = c.id " +
                "WHERE o.compte_id = ? " +
                "ORDER BY o.date_op DESC";
        return getHistorique(sql, compteId);
    }

    @Override
    public List<Operation> consulterHistorique(int compteId, String dateDebut, String dateFin) {
        String sql = "SELECT o.*, c.id as compte_id, c.numero, c.balance, c.created_at " +
                "FROM operations o " +
                "JOIN comptes c ON o.compte_id = c.id " +
                "WHERE o.compte_id = ? AND o.date_op BETWEEN ? AND ? " +
                "ORDER BY o.date_op DESC";
        return getHistorique(sql, compteId, dateDebut, dateFin);
    }

    @Override
    public int countTransactionsRecent() {
        String sql = "SELECT COUNT(*) FROM operations WHERE date_op >= NOW() - INTERVAL 7 DAY";
        try {
            db.initPrepar(sql);
            ResultSet rs = db.executeSelect();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            handleException(e);
            return 0;
        } finally {
            db.closeConnection();
        }
    }

    // Utility methods
    private List<Operation> getHistorique(String sql, Object... params) {
        try {
            db.initPrepar(sql);
            for (int i = 0; i < params.length; i++) {
                db.getPstm().setObject(i + 1, params[i]);
            }
            return mapResultSetToOperations(db.executeSelect());
        } catch (SQLException e) {
            handleException(e);
            return List.of();
        } finally {
            db.closeConnection();
        }
    }

    private List<Operation> mapResultSetToOperations(ResultSet rs) throws SQLException {
        List<Operation> operations = new ArrayList<>();
        while (rs.next()) {
            operations.add(mapResultSetToOperation(rs));
        }
        return operations;
    }

    private Operation mapResultSetToOperation(ResultSet rs) throws SQLException {
        Operation operation = new Operation();
        operation.setId(rs.getInt("id"));
        operation.setDateOp(rs.getTimestamp("date_op").toInstant());
        operation.setAmount(rs.getDouble("amount"));
        operation.setType(TypeOperation.valueOf(rs.getString("type")));

        Compte compte = new Compte();
        compte.setId(rs.getInt("compte_id"));
        compte.setNumero(rs.getString("numero"));
        compte.setBalance(rs.getDouble("balance"));
        compte.setCreatedAt(rs.getTimestamp("created_at").toInstant());

        operation.setCompte(compte);
        return operation;
    }

    private double getSoldeCompte(int compteId) throws SQLException {
        String sql = "SELECT balance FROM comptes WHERE id = ?";
        try {
            db.initPrepar(sql);
            db.getPstm().setInt(1, compteId);
            ResultSet rs = db.executeSelect();
            return rs.next() ? rs.getDouble("balance") : 0;
        } finally {
            db.closeConnection();
        }
    }

    private void handleException(SQLException e) {
        e.printStackTrace(); // ou utilisez un logger
        throw new RuntimeException("Erreur lors de l'exécution de la requête SQL", e);
    }
}