package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.IOperation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OperationImpl implements IOperation {

    private static final Logger logger = Logger.getLogger(OperationImpl.class.getName());
    private Db db = new Db();

    @Override
    public boolean createOperation(Operation operation) {
        if (operation == null) {
            logger.warning("Tentative de création d'une opération null.");
            return false;
        }

        String sql = "INSERT INTO operations (operation_date, description, montant, solde, compte_id, type) VALUES (?, ?, ?, ?, ?, ?)";
        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setTimestamp(1, Timestamp.valueOf(operation.getDate()));
            preparedStatement.setString(2, operation.getDescription());
            preparedStatement.setDouble(3, operation.getMontant());
            preparedStatement.setDouble(4, operation.getSolde());
            preparedStatement.setInt(5, operation.getCompte().getId());
            preparedStatement.setString(6, operation.getType());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected == 1;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la création de l'opération", e);
            return false;
        }
    }

    @Override
    public List<Operation> getAllOperations() {
        String sql = "SELECT * FROM operations ORDER BY operation_date ASC";
        List<Operation> operations = new ArrayList<>();

        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Operation operation = new Operation();
                operation.setId(rs.getInt("id"));
                operation.setDescription(rs.getString("description"));
                operation.setMontant(rs.getDouble("montant"));
                operation.setSolde(rs.getDouble("solde"));
                operation.setDate(rs.getTimestamp("operation_date").toLocalDateTime());
                operation.setType(rs.getString("type"));

                operations.add(operation);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des opérations", e);
        }

        return operations;
    }

    @Override
    public List<Operation> getOperationsByCompte(int compteId) {
        String sql = "SELECT * FROM operations WHERE compte_id = ? ORDER BY operation_date ASC";
        List<Operation> operations = new ArrayList<>();

        try (var connection = db.getConnection();
             var preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, compteId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    LocalDateTime date = rs.getTimestamp("operation_date").toLocalDateTime();
                    String description = rs.getString("description");
                    double montant = rs.getDouble("montant");
                    double solde = rs.getDouble("solde");
                    String type = rs.getString("type");

                    Operation operation = new Operation();
                    operation.setId(rs.getInt("id"));
                    operation.setDescription(description);
                    operation.setMontant(montant);
                    operation.setSolde(solde);
                    operation.setDate(date);
                    operation.setType(type);

                    operations.add(operation);
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des opérations", e);
        }

        return operations;
    }
}