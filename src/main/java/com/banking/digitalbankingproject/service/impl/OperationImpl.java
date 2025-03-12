package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.IOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {
    private List<Operation> operations = new ArrayList<>();
    private Connection connection;

    // Implémentation de la méthode effectuerDepot

    public OperationImpl(Connection connection) throws SQLException {
        this.connection = Db.getConnection();
    }
    @Override
    public void effectuerDepot(Compte compte, double amount) {
        Operation operation = new Operation(
                generateOperationId(),
                Instant.now(),
                amount,
                Operation.TypeOperation.DEPOT,
                compte
        );
        operations.add(operation);
    }

    // Implémentation de la méthode effectuerRetrait
    @Override
    public void effectuerRetrait(Compte compte, double amount) {
        Operation operation = new Operation(
                generateOperationId(),
                Instant.now(),
                amount,
                Operation.TypeOperation.RETRAIT,
                compte
        );
        operations.add(operation);
    }

    // Implémentation de la méthode consulterHistorique
    @Override
    public List<Operation> consulterHistorique(Compte compte) {
        List<Operation> historique = new ArrayList<>();
        for (Operation operation : operations) {
            if (operation.getCompte().equals(compte)) {
                historique.add(operation);
            }
        }
        return historique;
    }

    @Override
    public List<Operation> getOperationsByCompte(Compte compte) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operations WHERE compte_id = ? ORDER BY date_op DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, compte.getId());  // On utilise l'ID du compte pour filtrer les opérations

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Création de l'objet Operation à partir des données de la base de données
                    int id = rs.getInt("id");
                    Instant dateOp = rs.getTimestamp("date_op").toInstant();
                    double amount = rs.getDouble("amount");
                    String typeStr = rs.getString("type");
                    Operation.TypeOperation type = Operation.TypeOperation.valueOf(typeStr);  // Conversion du type en enum
                    Compte compteAssocie = compte;  // L'opération est associée au compte passé en paramètre

                    // Création d'une instance de Operation et ajout à la liste
                    Operation operation = new Operation(id, dateOp, amount, type, compteAssocie);
                    operations.add(operation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // En cas d'erreur, on affiche la stack trace
        }

        return operations;
    }

    // Méthode pour générer un id unique pour l'opération
    private int generateOperationId() {
        return operations.size() + 1;
    }
}
