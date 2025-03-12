package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.IOperation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/digital_banking_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @Override
    public void creerOperation(Operation operation) {
        String sql = "INSERT INTO operations (type, amount, date_op, compte_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, operation.getType());         // 1: type
            pstmt.setDouble(2, operation.getMontant());      // 2: amount
            pstmt.setTimestamp(3, operation.getDateOperation()); // 3: date_op
            pstmt.setInt(4, operation.getCompteId());        // 4: compte_id
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    operation.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur création opération : " + e.getMessage());
        }
    }

    @Override
    public List<Operation> listerOperationsParCompte(int compteId) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operations WHERE compte_id = ? ORDER BY date_op DESC";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, compteId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                operations.add(new Operation(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("date_op"), // Utilise "date_op" au lieu de "date"
                        rs.getInt("compte_id")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Erreur listage opérations : " + e.getMessage());
        }
        return operations;
    }

    @Override
    public Operation consulterOperation(int id) {
        String sql = "SELECT * FROM operations WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Operation(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("date_op"), // Utilise "date_op" au lieu de "date"
                        rs.getInt("compte_id")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur consultation opération : " + e.getMessage());
        }
        return null;
    }
}