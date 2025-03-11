package com.banking.digitalbankingproject.service.impl;

import com.banking.digitalbankingproject.database.Db;
import com.banking.digitalbankingproject.entity.Compte;
import com.banking.digitalbankingproject.entity.Operation;
import com.banking.digitalbankingproject.service.IOperation;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OperationImpl implements IOperation {

    private Db db = new Db();

    @Override
    public void ajouterOperation(Operation operation) {
        String sql = "INSERT INTO operations (compte_numero, date, description, montant, solde) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Utilisation du nouveau getter getCompte() pour récupérer le numéro du compte associé
            ps.setString(1, operation.getCompte().getNumero());
            ps.setTimestamp(2, Timestamp.valueOf(operation.getDate()));
            ps.setString(3, operation.getDescription());
            ps.setDouble(4, operation.getMontant());
            ps.setDouble(5, operation.getSolde());
            ps.executeUpdate();

            // Optionnel : récupérer la clé générée si nécessaire
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                // Par exemple, on peut récupérer l'identifiant généré si la table dispose d'un id auto-incrémenté
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Operation> getOperationsByCompte(Compte compte) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM operations WHERE compte_numero = ?";
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, compte.getNumero());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Operation operation = new Operation();
                // Conversion de la colonne date en LocalDateTime
                operation.setDate(rs.getTimestamp("date").toLocalDateTime());
                operation.setDescription(rs.getString("description"));
                operation.setMontant(rs.getDouble("montant"));
                operation.setSolde(rs.getDouble("solde"));
                // Affectation du compte associé à l'opération
                operation.setCompte(compte);
                operations.add(operation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return operations;
    }
}
